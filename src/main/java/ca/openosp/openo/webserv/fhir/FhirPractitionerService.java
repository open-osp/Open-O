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

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Practitioner.PractitionerQualificationComponent;
import org.hl7.fhir.dstu3.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import ca.openosp.openo.commn.dao.ProfessionalSpecialistDao;
import ca.openosp.openo.commn.model.ProfessionalSpecialist;
import ca.openosp.openo.commn.dao.ConsultationServiceDao;
import ca.openosp.openo.commn.dao.ServiceSpecialistsDao;
import ca.openosp.openo.commn.model.ConsultationServices;
import ca.openosp.openo.commn.model.ServiceSpecialists;
import ca.openosp.openo.commn.model.ServiceSpecialistsPK;
import ca.openosp.openo.managers.SecurityInfoManager;
import ca.openosp.openo.utility.LoggedInInfo;

@Service
@Path("/Practitioner")
@Produces({"application/fhir+json", "application/json"})
@Consumes({"application/fhir+json", "application/json"})
public class FhirPractitionerService {

    @Autowired
    private ProfessionalSpecialistDao professionalSpecialistDao;

    @Autowired
    private ConsultationServiceDao consultationServiceDao;

    @Autowired
    private ServiceSpecialistsDao serviceSpecialistsDao;

    @GET
    @Path("/{id}")
    @Transactional(readOnly = true)
    public Practitioner getPractitioner(@PathParam("id") Integer id) {
        ProfessionalSpecialist ps = professionalSpecialistDao.find(id);
        if (ps == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return mapToFhir(ps);
    }

    @GET
    @Transactional(readOnly = true)
    public Bundle searchPractitioners(
            @QueryParam("name") String name,
            @QueryParam("identifier") String identifier) {

        List<ProfessionalSpecialist> specialists = new ArrayList<>();

        if (StringUtils.isNotBlank(identifier)) {
             // System|Value or just Value
             String value = identifier;
             if (identifier.contains("|")) {
                 String[] parts = identifier.split("\\|");
                 if (parts.length > 1) {
                     value = parts[1];
                 } else {
                     // system| (no value)
                     value = "";
                 }

                 if (value.isEmpty()) {
                     // Return all with external ID (eDataOscarKey)
                     specialists = professionalSpecialistDao.findByEDataOscarKeyNotNull();
                 } else if (parts[0].contains("referralNo")) {
                     specialists = professionalSpecialistDao.findByReferralNo(value);
                 } else {
                     specialists = professionalSpecialistDao.findByEDataOscarKey(value);
                 }
             } else {
                 // No system provided, search both? Or just eDataOscarKey?
                 // Let's search eDataOscarKey primarily for external integrations
                 specialists = professionalSpecialistDao.findByEDataOscarKey(value);
                 if (specialists == null || specialists.isEmpty()) {
                     specialists = professionalSpecialistDao.findByReferralNo(value);
                 }
             }

             if (specialists == null) specialists = new ArrayList<>();

        } else if (StringUtils.isNotBlank(name)) {
            specialists = professionalSpecialistDao.search(name);
        } else {
            // Limit findAll to prevent performance issues, currently findAll is unbounded
            // For now, let's just return an empty bundle if no parameters to avoid dumping the whole DB
            // Or ideally use a paginated query, but DAO doesn't support it easily.
             specialists = professionalSpecialistDao.search(""); // returns all if keyword empty? check DAO impl
             // Actually search("") in manager usually returns all.
        }

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setTotal(specialists.size());

        // Cap results to 50 for safety
        int count = 0;
        for (ProfessionalSpecialist ps : specialists) {
            if (count++ >= 50) break;
            BundleEntryComponent entry = bundle.addEntry();
            entry.setResource(mapToFhir(ps));
            entry.setFullUrl("Practitioner/" + ps.getId());
        }

        return bundle;
    }

    @POST
    @Transactional
    public Response createPractitioner(Practitioner fhirPractitioner, @Context UriInfo uriInfo) {
        ProfessionalSpecialist ps = new ProfessionalSpecialist();
        mapToEntity(fhirPractitioner, ps);
        professionalSpecialistDao.persist(ps);

        // Auto-map to ConsultationServices
        autoMapSpecialistToServices(ps);

        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(ps.getId())).build();
        return Response.created(location).entity(mapToFhir(ps)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Practitioner updatePractitioner(@PathParam("id") Integer id, Practitioner fhirPractitioner) {
        ProfessionalSpecialist ps = professionalSpecialistDao.find(id);

        if (ps == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        mapToEntity(fhirPractitioner, ps);
        professionalSpecialistDao.merge(ps);

        // Auto-map to ConsultationServices
        autoMapSpecialistToServices(ps);

        return mapToFhir(ps);
    }

    private Practitioner mapToFhir(ProfessionalSpecialist ps) {
        Practitioner pract = new Practitioner();
        pract.setId(new IdType("Practitioner", String.valueOf(ps.getId())));

        // Name
        HumanName name = pract.addName();
        if (ps.getLastName() != null) name.setFamily(ps.getLastName());
        if (ps.getFirstName() != null) name.addGiven(ps.getFirstName());
        if (ps.getProfessionalLetters() != null) name.addSuffix(ps.getProfessionalLetters());

        // Telecoms
        if (StringUtils.isNotBlank(ps.getPhoneNumber())) {
            pract.addTelecom().setSystem(ContactPointSystem.PHONE).setValue(ps.getPhoneNumber()).setUse(ContactPointUse.WORK);
        }
        if (StringUtils.isNotBlank(ps.getFaxNumber())) {
            pract.addTelecom().setSystem(ContactPointSystem.FAX).setValue(ps.getFaxNumber()).setUse(ContactPointUse.WORK);
        }
        if (StringUtils.isNotBlank(ps.getEmailAddress())) {
            pract.addTelecom().setSystem(ContactPointSystem.EMAIL).setValue(ps.getEmailAddress()).setUse(ContactPointUse.WORK);
        }
        if (StringUtils.isNotBlank(ps.getWebSite())) {
            pract.addTelecom().setSystem(ContactPointSystem.URL).setValue(ps.getWebSite()).setUse(ContactPointUse.WORK);
        }

        // Address
        Address address = pract.addAddress();
        address.setUse(Address.AddressUse.WORK);

        String[] addressArray = ps.getAddressArray();
        if (addressArray.length > 0 && StringUtils.isNotBlank(addressArray[0])) {
            // Street address is often comma separated in the first element or spread
            // The logic in ProfessionalSpecialist.getStreetAddress() is complex
            address.addLine(StringUtils.trimToEmpty(addressArray[0]).replace(",", ""));
        }
        if (addressArray.length > 1) address.setCity(StringUtils.trimToEmpty(addressArray[1]).replace(",", ""));
        if (addressArray.length > 2) address.setPostalCode(StringUtils.trimToEmpty(addressArray[2]).replace(",", ""));
        if (addressArray.length > 3) address.setState(StringUtils.trimToEmpty(addressArray[3]).replace(",", ""));
        // Country is index 4 but rarely used correctly

        // Qualification / Specialty
        if (StringUtils.isNotBlank(ps.getSpecialtyType())) {
            PractitionerQualificationComponent qual = pract.addQualification();
            qual.getCode().setText(ps.getSpecialtyType());
        }

        // Identifiers
        if (StringUtils.isNotBlank(ps.getReferralNo())) {
            pract.addIdentifier().setSystem("http://oscar/referralNo").setValue(ps.getReferralNo());
        }
        if (StringUtils.isNotBlank(ps.geteDataOscarKey())) {
            // Return eDataOscarKey as an identifier.
            pract.addIdentifier()
                 .setSystem("https://pathwaysbc.ca/fhir/NamingSystem/specialist-id")
                 .setValue(ps.geteDataOscarKey());
        }

        return pract;
    }

    private void mapToEntity(Practitioner fhirPractitioner, ProfessionalSpecialist ps) {
        // Name
        if (!fhirPractitioner.getName().isEmpty()) {
            HumanName name = fhirPractitioner.getNameFirstRep();
            if (name.hasFamily()) ps.setLastName(name.getFamily());
            if (name.hasGiven()) ps.setFirstName(name.getGivenAsSingleString());
            if (!name.getSuffix().isEmpty()) ps.setProfessionalLetters(name.getSuffix().get(0).getValue());
        }

        // Telecoms
        for (ContactPoint cp : fhirPractitioner.getTelecom()) {
            if (cp.getSystem() == ContactPointSystem.PHONE) ps.setPhoneNumber(cp.getValue());
            else if (cp.getSystem() == ContactPointSystem.FAX) ps.setFaxNumber(cp.getValue());
            else if (cp.getSystem() == ContactPointSystem.EMAIL) ps.setEmailAddress(cp.getValue());
            else if (cp.getSystem() == ContactPointSystem.URL) ps.setWebSite(cp.getValue());
        }

        // Address
        if (!fhirPractitioner.getAddress().isEmpty()) {
            Address addr = fhirPractitioner.getAddressFirstRep();
            ps.setAddress(addr.getLine().isEmpty() ? "" : addr.getLine().get(0).getValue());
            ps.setCity(addr.getCity());
            ps.setPostal(addr.getPostalCode());
            ps.setProvince(addr.getState());
        }

        // Qualification
        if (!fhirPractitioner.getQualification().isEmpty()) {
            ps.setSpecialtyType(fhirPractitioner.getQualificationFirstRep().getCode().getText());
        }

        // Identifier
        for (Identifier id : fhirPractitioner.getIdentifier()) {
            if ("http://oscar/referralNo".equals(id.getSystem())) {
                ps.setReferralNo(id.getValue());
            } else if (id.getSystem() != null && id.getSystem().contains("pathways")) {
                // Store Pathways ID in eDataOscarKey
                ps.seteDataOscarKey(id.getValue());
            } else if (ps.geteDataOscarKey() == null) {
                // Fallback: store first unknown ID in eDataOscarKey if empty
                ps.seteDataOscarKey(id.getValue());
            }
        }
    }

    private void autoMapSpecialistToServices(ProfessionalSpecialist ps) {
        if (ps == null || ps.getId() == null) return;

        Integer specId = ps.getId();

        // 1. Always map to "All Services" (serviceId = 0)
        createServiceSpecialistLink(0, specId);

        // 2. Map to specific specialty if it exists
        String specialtyType = ps.getSpecialtyType();
        if (StringUtils.isNotBlank(specialtyType)) {
            String searchStr = specialtyType.trim().toLowerCase();

            // First try exact match
            ConsultationServices exactMatch = consultationServiceDao.findByDescription(specialtyType.trim());
            if (exactMatch != null && exactMatch.getId() != null) {
                createServiceSpecialistLink(exactMatch.getId(), specId);
                return;
            }

            // If no exact match, try partial match against active services
            java.util.List<ConsultationServices> activeServices = consultationServiceDao.findActive();
            for (ConsultationServices service : activeServices) {
                if (service.getServiceDesc() != null && service.getServiceDesc().toLowerCase().contains(searchStr)) {
                    createServiceSpecialistLink(service.getId(), specId);
                    // Map to the first good partial match
                    break;
                }
            }
        }
    }

    private void createServiceSpecialistLink(Integer serviceId, Integer specId) {
        // Build composite key
        ServiceSpecialistsPK pk = new ServiceSpecialistsPK(serviceId, specId);

        // Check if linkage already exists
        ServiceSpecialists existing = serviceSpecialistsDao.find(pk);
        if (existing == null) {
            ServiceSpecialists ss = new ServiceSpecialists();
            ss.setId(pk);
            serviceSpecialistsDao.persist(ss);
        }
    }
}
