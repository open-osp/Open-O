package ca.openosp.openo.webserv.fhir;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.openosp.openo.commn.dao.ConsultationServiceDao;
import ca.openosp.openo.commn.dao.ProfessionalSpecialistDao;
import ca.openosp.openo.commn.dao.ServiceSpecialistsDao;
import ca.openosp.openo.commn.model.ConsultationServices;
import ca.openosp.openo.commn.model.ProfessionalSpecialist;
import ca.openosp.openo.commn.model.ServiceSpecialists;
import ca.openosp.openo.commn.model.ServiceSpecialistsPK;

@Service
@Path("/Organization")
@Produces({"application/fhir+json", "application/json"})
@Consumes({"application/fhir+json", "application/json"})
public class FhirOrganizationService {

    @Autowired
    private ConsultationServiceDao consultationServiceDao;

    @Autowired
    private ProfessionalSpecialistDao professionalSpecialistDao;

    @Autowired
    private ServiceSpecialistsDao serviceSpecialistsDao;

    @GET
    @Path("/{id}")
    @Transactional(readOnly = true)
    public Organization getOrganization(@PathParam("id") Integer id) {
        ConsultationServices cs = consultationServiceDao.find(id);
        if (cs == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return mapToFhir(cs);
    }

    @GET
    @Transactional(readOnly = true)
    public Bundle searchOrganizations(@QueryParam("identifier") String identifier) {
        List<ConsultationServices> services;

        if (identifier != null && !identifier.isEmpty()) {
            // Check for system|value
            String val = identifier;
            if (identifier.contains("|")) {
                 String[] parts = identifier.split("\\|");
                 if (parts.length > 1) val = parts[1];
                 else val = "";
            }

            if (val.isEmpty()) {
                // Return all with external ID
                services = consultationServiceDao.findByExternalIdNotNull();
            } else {
                ConsultationServices cs = consultationServiceDao.findByExternalId(val);
                services = new ArrayList<>();
                if (cs != null) services.add(cs);
            }
        } else {
            services = consultationServiceDao.findAll();
        }

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setTotal(services.size());

        for (ConsultationServices cs : services) {
            BundleEntryComponent entry = bundle.addEntry();
            entry.setResource(mapToFhir(cs));
            entry.setFullUrl("Organization/" + cs.getId());
        }
        return bundle;
    }

    @POST
    @Transactional
    public Response createOrganization(Organization fhirOrg, @Context UriInfo uriInfo) {
        ConsultationServices cs = new ConsultationServices();
        mapToEntity(fhirOrg, cs);
        consultationServiceDao.persist(cs);

        // Auto-create and map a Consultant entry for this Clinic
        createOrUpdateClinicSpecialist(cs, fhirOrg);

        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(cs.getId())).build();
        return Response.created(location).entity(mapToFhir(cs)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Organization updateOrganization(@PathParam("id") Integer id, Organization fhirOrg) {
        ConsultationServices cs = consultationServiceDao.find(id);
        if (cs == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        mapToEntity(fhirOrg, cs);
        consultationServiceDao.merge(cs);

        // Auto-create and map a Consultant entry for this Clinic
        createOrUpdateClinicSpecialist(cs, fhirOrg);

        return mapToFhir(cs);
    }

    private Organization mapToFhir(ConsultationServices cs) {
        Organization org = new Organization();
        org.setId(new IdType("Organization", String.valueOf(cs.getId())));
        org.setName(cs.getServiceDesc());
        org.setActive("1".equals(cs.getActive()));

        if (cs.getExternalId() != null && !cs.getExternalId().isEmpty()) {
            org.addIdentifier()
               .setSystem("https://pathwaysbc.ca/fhir/NamingSystem/clinic-id")
               .setValue(cs.getExternalId());
        }

        return org;
    }

    private void mapToEntity(Organization fhirOrg, ConsultationServices cs) {
        if (fhirOrg.hasName()) {
            cs.setServiceDesc(fhirOrg.getName());
        }

        if (fhirOrg.hasActive()) {
            cs.setActive(fhirOrg.getActive() ? "1" : "0");
        } else {
            // Default to active if not specified
            cs.setActive("1");
        }

        // Map identifier to externalId
        if (fhirOrg.hasIdentifier()) {
            // Just take the first one or prioritize specific system
            String idVal = fhirOrg.getIdentifierFirstRep().getValue();
            if (idVal != null && !idVal.isEmpty()) {
                cs.setExternalId(idVal);
            }
        }
    }

    private void createOrUpdateClinicSpecialist(ConsultationServices cs, Organization fhirOrg) {
        if (cs == null || cs.getId() == null || cs.getExternalId() == null) return;

        // Use eDataServiceKey to track that this Specialist is actually an Organization
        String clinicExternalId = "ORG-" + cs.getExternalId();

        // Check if we already created a ProfessionalSpecialist for this Clinic
        ProfessionalSpecialist ps = null;
        java.util.List<ProfessionalSpecialist> existing = professionalSpecialistDao.search(cs.getServiceDesc());
        for (ProfessionalSpecialist spec : existing) {
            if (clinicExternalId.equals(spec.geteDataServiceKey())) {
                ps = spec;
                break;
            }
        }

        if (ps == null) {
            ps = new ProfessionalSpecialist();
            ps.seteDataServiceKey(clinicExternalId);
        }

        // Apply Clinic Details to Consultant Profile
        if (fhirOrg.hasName()) {
            ps.setLastName(fhirOrg.getName());
            ps.setFirstName("Clinic");
        } else {
            ps.setLastName(cs.getServiceDesc());
        }

        ps.setSpecialtyType("Clinic / Organization");

        if (ps.getId() == null) {
            professionalSpecialistDao.persist(ps);
        } else {
            professionalSpecialistDao.merge(ps);
        }

        // Link the Clinic to "All Services" (0) and its own literal ConsultationService group
        createServiceSpecialistLink(0, ps.getId());
        createServiceSpecialistLink(cs.getId(), ps.getId());
    }

    private void createServiceSpecialistLink(Integer serviceId, Integer specId) {
        ServiceSpecialistsPK pk = new ServiceSpecialistsPK(serviceId, specId);
        ServiceSpecialists existing = serviceSpecialistsDao.find(pk);
        if (existing == null) {
            ServiceSpecialists ss = new ServiceSpecialists();
            ss.setId(pk);
            serviceSpecialistsDao.persist(ss);
        }
    }
}
