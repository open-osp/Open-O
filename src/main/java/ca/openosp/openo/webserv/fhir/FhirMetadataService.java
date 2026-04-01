package ca.openosp.openo.webserv.fhir;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementKind;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestSecurityComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.ResourceInteractionComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.SystemInteractionComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.SystemRestfulInteraction;
import org.hl7.fhir.dstu3.model.CapabilityStatement.TypeRestfulInteraction;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.UriType;
import org.springframework.stereotype.Service;

@Service
@Path("/")
public class FhirMetadataService {

    @GET
    @Path("metadata")
    @Produces({"application/fhir+json", "application/json"})
    public CapabilityStatement getMetadata(@Context HttpServletRequest request, @Context UriInfo uriInfo) {
        CapabilityStatement cs = new CapabilityStatement();
        cs.setStatus(PublicationStatus.ACTIVE);
        cs.setDate(new java.util.Date());
        cs.setPublisher("Open OSP");
        cs.setKind(CapabilityStatementKind.INSTANCE);
        cs.setFhirVersion("3.0.2");
        cs.setAcceptUnknown(CapabilityStatement.UnknownContentCode.NO);
        cs.addFormat("application/fhir+json");
        cs.addFormat("application/json");

        CapabilityStatementRestComponent rest = cs.addRest();
        rest.setMode(CapabilityStatement.RestfulCapabilityMode.SERVER);

        // Security configuration (SMART on FHIR)
        CapabilityStatementRestSecurityComponent security = rest.getSecurity();
        CodeableConcept service = new CodeableConcept();
        Coding coding = service.addCoding();
        coding.setSystem("http://hl7.org/fhir/restful-security-service");
        coding.setCode("SMART-on-FHIR");
        coding.setDisplay("SMART-on-FHIR");
        security.addService(service);

        Extension oauthUris = new Extension();
        oauthUris.setUrl("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris");

        // Dynamic construction of OAuth URLs based on request context
        String baseUrl = uriInfo.getBaseUri().toString();
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }

        // Use the new OAuth 2.0 endpoints under /ws/fhir/auth
        oauthUris.addExtension("authorize", new UriType(baseUrl + "auth/authorize"));
        oauthUris.addExtension("token", new UriType(baseUrl + "auth/token"));
        security.addExtension(oauthUris);

        // Practitioner Resource
        CapabilityStatementRestResourceComponent practitioner = rest.addResource();
        practitioner.setType("Practitioner");
        practitioner.addInteraction().setCode(TypeRestfulInteraction.READ);
        practitioner.addInteraction().setCode(TypeRestfulInteraction.SEARCHTYPE);
        practitioner.addInteraction().setCode(TypeRestfulInteraction.CREATE);
        practitioner.addInteraction().setCode(TypeRestfulInteraction.UPDATE);
        // practitioner.addInteraction().setCode(TypeRestfulInteraction.DELETE); // Not supported yet safely

        // Organization Resource
        CapabilityStatementRestResourceComponent organization = rest.addResource();
        organization.setType("Organization");
        organization.addInteraction().setCode(TypeRestfulInteraction.READ);
        organization.addInteraction().setCode(TypeRestfulInteraction.SEARCHTYPE);
        organization.addInteraction().setCode(TypeRestfulInteraction.CREATE);
        organization.addInteraction().setCode(TypeRestfulInteraction.UPDATE);

        return cs;
    }
}
