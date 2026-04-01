package ca.openosp.openo.webserv.fhir;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.SecurityContext;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeGrantHandler;
import org.apache.cxf.rs.security.oauth2.services.AccessTokenService;
import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.provider.ResourceOwnerNameProvider;

/**
 * Valid OAuth 2.0 endpoints for SMART on FHIR.
 * Wraps CXF services to integrate with OSCAR session.
 */
@Path("/auth")
public class OAuth2Service {

    private OAuthDataProvider dataProvider;

    // CXF Services
    private AuthorizationCodeGrantService authService;
    private AccessTokenService tokenService;

    public void setDataProvider(OAuthDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @PostConstruct
    public void init() {
        System.out.println("[FHIR-OAUTH-DEBUG] OAuth2Service.init() called");
        // Initialize Authorization Service with Auto-Approval
        authService = new AutoApprovedAuthorizationCodeGrantService();
        authService.setDataProvider(dataProvider);
        authService.setCanSupportPublicClients(true);

        // Use SubjectCreator to create the UserSubject from the session
        authService.setSubjectCreator(new org.apache.cxf.rs.security.oauth2.provider.SubjectCreator() {
            @Override
            public UserSubject createUserSubject(org.apache.cxf.jaxrs.ext.MessageContext mc,
                                                 MultivaluedMap<String, String> params)
                                                 throws org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException {
                 System.out.println("[FHIR-OAUTH-DEBUG] SubjectCreator.createUserSubject called");
                 HttpServletRequest req = mc.getHttpServletRequest();
                 Object user = req.getSession().getAttribute("user");
                 System.out.println("[FHIR-OAUTH-DEBUG] user in session: " + user);

                 if (user != null) {
                     return new UserSubject(user.toString());
                 }

                 System.out.println("[FHIR-OAUTH-DEBUG] SubjectCreator: User not authenticated (returning null / throwing)");
                 throw new org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException("User not authenticated");
            }
        });

        // Initialize Token Service
        tokenService = new AccessTokenService();
        tokenService.setDataProvider(dataProvider);
        tokenService.setCanSupportPublicClients(true);
        // Register the grant handler
        AuthorizationCodeGrantHandler handler = new AuthorizationCodeGrantHandler();
        handler.setDataProvider(dataProvider);
        handler.setCanSupportPublicClients(true);
        handler.setCodeVerifierTransformer(new org.apache.cxf.rs.security.oauth2.grants.code.DigestCodeVerifier());
        tokenService.setGrantHandlers(Collections.singletonList(handler));
    }

    @Context
    private org.apache.cxf.jaxrs.ext.MessageContext mc;

    @GET
    @Path("/authorize")
    @Produces("text/html")
    public Response authorize(@Context HttpServletRequest request, @Context UriInfo uriInfo) {
        System.out.println("[FHIR-OAUTH-DEBUG] OAuth2Service.authorize() called");
        Object sessionUser = request.getSession().getAttribute("user");
        System.out.println("[FHIR-OAUTH-DEBUG] authorize: user in session=" + sessionUser);

        // 1. Check if logged in
        if (sessionUser == null) {
             try {
                 String currentUri = uriInfo.getRequestUri().toString();
                 // Construct absolute URL to login.jsp
                 URI loginUri = uriInfo.getBaseUriBuilder()
                         .replacePath(request.getContextPath() + "/login.jsp")
                         .replaceQueryParam("next", currentUri)
                         .build();

                 return Response.seeOther(loginUri).build();
             } catch (Exception e) {
                 return Response.serverError().entity("Error encoding redirect").build();
             }
        }

        // --- PKCE Bypass Check ---
        boolean isPkce = request.getParameter(org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.AUTHORIZATION_CODE_CHALLENGE) != null;
        ca.openosp.openo.webserv.fhir.FHIROAuth2Provider.setPkceRequest(isPkce);

        try {
            // 2. Delegate to CXF
            authService.setMessageContext(mc);
            return authService.authorize();
        } finally {
            ca.openosp.openo.webserv.fhir.FHIROAuth2Provider.setPkceRequest(false);
        }
    }

    @POST
    @Path("/authorize/decision")
    public Response authorizeDecision() {
        authService.setMessageContext(mc);
        return authService.authorizeDecision();
    }

    @POST
    @Path("/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response token(MultivaluedMap<String, String> params) {
        tokenService.setMessageContext(mc);

        // --- PKCE Bypass Check ---
        boolean isPkce = params.containsKey(org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.AUTHORIZATION_CODE_VERIFIER);
        ca.openosp.openo.webserv.fhir.FHIROAuth2Provider.setPkceRequest(isPkce);

        try {
            Response cxfResponse = tokenService.handleTokenRequest(params);

            // CXF returns ClientAccessToken with Java field names (tokenKey, tokenType, etc.)
            // but OAuth 2.0 RFC 6749 requires standard names (access_token, token_type, etc.)
            if (cxfResponse.getStatus() == 200 && cxfResponse.getEntity() instanceof org.apache.cxf.rs.security.oauth2.common.ClientAccessToken) {
                org.apache.cxf.rs.security.oauth2.common.ClientAccessToken cat =
                    (org.apache.cxf.rs.security.oauth2.common.ClientAccessToken) cxfResponse.getEntity();

                Map<String, Object> oauthResponse = new HashMap<>();
                oauthResponse.put("access_token", cat.getTokenKey());
                oauthResponse.put("token_type", cat.getTokenType());
                oauthResponse.put("expires_in", cat.getExpiresIn());
                if (cat.getApprovedScope() != null) {
                    oauthResponse.put("scope", cat.getApprovedScope());
                }
                if (cat.getRefreshToken() != null) {
                    oauthResponse.put("refresh_token", cat.getRefreshToken());
                }
                // Copy any extra parameters (e.g., patient context)
                if (cat.getParameters() != null) {
                    oauthResponse.putAll(cat.getParameters());
                }

                return Response.ok(oauthResponse)
                    .header("Cache-Control", "no-store")
                    .header("Pragma", "no-cache")
                    .build();
            }

            return cxfResponse;
        } finally {
            ca.openosp.openo.webserv.fhir.FHIROAuth2Provider.setPkceRequest(false);
        }
    }

    /**
     * Custom AuthorizationCodeGrantService that skips the confirmation form.
     */
    public static class AutoApprovedAuthorizationCodeGrantService extends AuthorizationCodeGrantService {

        @Override
        protected UserSubject createUserSubject(org.apache.cxf.security.SecurityContext sc,
                                              MultivaluedMap<String, String> params) {
            System.out.println("[FHIR-OAUTH-DEBUG] AutoApproved...createUserSubject override called");
            // We need the request to get the session, which isn't in SecurityContext
            // But we can get it from the service's MessageContext
            HttpServletRequest req = getMessageContext().getHttpServletRequest();
            Object user = req.getSession().getAttribute("user");
            if (user != null) {
                System.out.println("[FHIR-OAUTH-DEBUG] createUserSubject: user=" + user);
                return new UserSubject(user.toString());
            }
            return super.createUserSubject(sc, params);
        }

        @Override
        protected Response startAuthorization(MultivaluedMap<String, String> params,
                                              UserSubject userSubject,
                                              org.apache.cxf.rs.security.oauth2.common.Client client,
                                              String redirectUri) {

            System.out.println("[FHIR-OAUTH-DEBUG] startAuthorization called. userSubject=" + userSubject);

            // Fallback: If UserSubject is null, try to create it from the session
            // This fixes the issue where createUserSubject is not called or fails
            if (userSubject == null) {
                try {
                    HttpServletRequest req = getMessageContext().getHttpServletRequest();
                    Object user = req.getSession().getAttribute("user");
                     if (user != null) {
                         System.out.println("[FHIR-OAUTH-DEBUG] startAuthorization: Creating UserSubject from session user " + user);
                         userSubject = new UserSubject(user.toString());
                     }
                } catch (Exception e) {
                     System.out.println("[FHIR-OAUTH-DEBUG] startAuthorization: Error creating fallback subject: " + e.getMessage());
                }
            }

            // Call super to validate params and create the state/data
            Response response = super.startAuthorization(params, userSubject, client, redirectUri);

            // Check if super wants to show the form (returns OAuthAuthorizationData)
            if (response.getEntity() instanceof org.apache.cxf.rs.security.oauth2.common.OAuthAuthorizationData) {
                org.apache.cxf.rs.security.oauth2.common.OAuthAuthorizationData data =
                    (org.apache.cxf.rs.security.oauth2.common.OAuthAuthorizationData) response.getEntity();

                // Auto-approve: directly create the grant
                // We assume all requested scopes are approved
                List<String> requestedScope = org.apache.cxf.rs.security.oauth2.utils.OAuthUtils.parseScope(params.getFirst("scope"));
                // If scope param is missing, use default or client scopes (handled by super logic usually, but here we parse raw)
                if (requestedScope.isEmpty() && client.getRegisteredScopes() != null && !client.getRegisteredScopes().isEmpty()) {
                     // If implicit default scopes mechanism is needed
                     requestedScope = client.getRegisteredScopes();
                }

                // Call createGrant directly
                return createGrant(data, client, requestedScope, requestedScope, userSubject, null);
            }

            return response;
        }
    }
}
