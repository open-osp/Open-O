//CHECKSTYLE:OFF
/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 * 
 * Migrated from Apache CXF to ScribeJava OAuth1 implementation.
 */
package oscar.login;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

/**
 * OAuth 1.0a Request Token Service using ScribeJava.
 * 
 * This REST service issues temporary request tokens to clients which will be
 * later authorized and exchanged for access tokens. It replaces the CXF-based
 * OAuth implementation with ScribeJava.
 */
@Component
@Path("/initiate")
public class OscarRequestTokenService {

    private static final Logger logger = MiscUtils.getLogger();

    @Autowired
    private OscarOAuthDataProvider dataProvider;

    /**
     * Handles GET requests for OAuth 1.0a request tokens.
     * 
     * @param request HTTP servlet request
     * @param response HTTP servlet response
     * @return Response containing the request token or error
     */
    @GET
    @Produces("application/x-www-form-urlencoded")
    public Response getRequestTokenWithGET(@Context HttpServletRequest request, 
                                          @Context HttpServletResponse response) {
        return getRequestToken(request, response);
    }

    /**
     * Handles POST requests for OAuth 1.0a request tokens.
     * 
     * This method implements the OAuth 1.0a request token endpoint as specified in RFC 5849.
     * It validates the consumer key, generates a temporary request token, stores it, and
     * returns it to the client in form-encoded format.
     * 
     * @param request HTTP servlet request
     * @param response HTTP servlet response
     * @return Response containing the request token or error
     */
    @POST
    @Produces("application/x-www-form-urlencoded")
    public Response getRequestToken(@Context HttpServletRequest request, 
                                   @Context HttpServletResponse response) {
        try {
            logger.info("Processing OAuth 1.0a request token request");

            // Extract required OAuth parameters
            String consumerKey = request.getParameter("oauth_consumer_key");
            String callbackUrl = request.getParameter("oauth_callback");

            // Validate required parameters
            if (consumerKey == null || consumerKey.trim().isEmpty()) {
                logger.warn("Missing oauth_consumer_key parameter in request token request");
                return buildErrorResponse(Response.Status.BAD_REQUEST, 
                    "parameter_absent", "oauth_consumer_key");
            }

            if (callbackUrl == null || callbackUrl.trim().isEmpty()) {
                logger.warn("Missing oauth_callback parameter in request token request");
                return buildErrorResponse(Response.Status.BAD_REQUEST, 
                    "parameter_absent", "oauth_callback");
            }

            // Get OAuth configuration for this consumer key
            OAuthAppConfig appConfig = getOAuthConfiguration(consumerKey);
            if (appConfig == null) {
                logger.warn("Unknown consumer key: {}", consumerKey);
                return buildErrorResponse(Response.Status.UNAUTHORIZED, 
                    "consumer_key_unknown", null);
            }

            // Validate callback URL against registered callback
            if (!isValidCallback(appConfig, callbackUrl)) {
                logger.warn("Invalid callback URL: {} for consumer: {}", callbackUrl, consumerKey);
                return buildErrorResponse(Response.Status.BAD_REQUEST, 
                    "parameter_rejected", "oauth_callback");
            }

            // Build OAuth 1.0a service
           OAuth10aService service = new ServiceBuilder(appConfig.getConsumerKey())
                    .apiSecret(appConfig.getConsumerSecret())
                    .callback(callbackUrl)
                    .build(new DefaultApi10a() {
                        @Override
                        public String getRequestTokenEndpoint() {
                            return appConfig.getBaseUrl() + "/oauth/request_token";
                        }

                        @Override
                        public String getAccessTokenEndpoint() {
                            return appConfig.getBaseUrl() + "/oauth/access_token";
                        }

                        @Override
                        public String getAuthorizationBaseUrl() {
                            return appConfig.getBaseUrl() + "/oauth/authorize";
                        }
                    });

            // Get request token from OAuth provider
            OAuth1RequestToken requestToken = service.getRequestToken();
            
            if (requestToken == null || requestToken.getToken() == null) {
                logger.error("Failed to generate request token for consumer: {}", consumerKey);
                return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, 
                    "internal_error", null);
            }

            // Store the request token
            dataProvider.createRequestToken(
                requestToken.getToken(), 
                requestToken.getTokenSecret(), 
                appConfig.getScopes()
            );

            // Build and return the response
            String responseBody = String.format(
                "oauth_token=%s&oauth_token_secret=%s&oauth_callback_confirmed=true",
                requestToken.getToken(),
                requestToken.getTokenSecret()
            );

            logger.info("Successfully issued request token: {} for consumer: {}", 
                requestToken.getToken(), consumerKey);

            return Response.ok(responseBody)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        } catch (IOException e) {
            logger.error("I/O error processing request token: {}", e.getMessage(), e);
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, 
                "internal_error", null);
        } catch (InterruptedException e) {
            logger.error("Request token generation was interrupted: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, 
                "internal_error", null);
        } catch (ExecutionException e) {
            logger.error("Execution error during request token generation: {}", e.getMessage(), e);
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, 
                "internal_error", null);
        } catch (Exception e) {
            logger.error("Unexpected error processing request token: {}", e.getMessage(), e);
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, 
                "internal_error", null);
        }
    }

    /**
     * Retrieves OAuth application configuration for the given consumer key.
     * 
     * @param consumerKey The OAuth consumer key
     * @return OAuthAppConfig or null if not found
     */
    private OAuthAppConfig getOAuthConfiguration(String consumerKey) {
        // Check system properties first (useful for development)
        String consumerSecret = System.getProperty("oauth.consumer." + consumerKey + ".secret");
        String baseUrl = System.getProperty("oauth.consumer." + consumerKey + ".baseUrl");
        String callbackUri = System.getProperty("oauth.consumer." + consumerKey + ".callbackUri");
        String applicationUri = System.getProperty("oauth.consumer." + consumerKey + ".applicationUri");
        String scopesStr = System.getProperty("oauth.consumer." + consumerKey + ".scopes");
        
        if (consumerSecret != null && baseUrl != null) {
            logger.debug("Found OAuth configuration in system properties for consumer: {}", consumerKey);
            OAuthAppConfig config = new OAuthAppConfig();
            config.setConsumerKey(consumerKey);
            config.setConsumerSecret(consumerSecret);
            config.setBaseUrl(baseUrl);
            config.setCallbackURI(callbackUri);
            config.setApplicationURI(applicationUri);
            
            if (scopesStr != null && !scopesStr.trim().isEmpty()) {
                config.setScopes(java.util.Arrays.asList(scopesStr.split(",")));
            }
            
            return config;
        }
        
        // Fallback to default configuration for known consumer keys
        if ("oscar-client".equals(consumerKey)) {
            logger.debug("Using default configuration for oscar-client");
            OAuthAppConfig config = new OAuthAppConfig();
            config.setConsumerKey(consumerKey);
            config.setConsumerSecret("oscar-secret");
            config.setBaseUrl("http://localhost:8080");
            config.setCallbackURI("http://localhost:8080/oscar/oauth/callback");
            config.setApplicationURI("http://localhost:8080/oscar");
            config.setScopes(java.util.Arrays.asList("read", "write"));
            return config;
        }
        
        logger.warn("No OAuth configuration found for consumer key: {}", consumerKey);
        return null;
    }

    /**
     * Validates the callback URL against the registered application configuration.
     * 
     * @param config OAuth application configuration
     * @param callbackUrl The callback URL to validate
     * @return true if the callback URL is valid, false otherwise
     */
    private boolean isValidCallback(OAuthAppConfig config, String callbackUrl) {
        // Allow "oob" (out-of-band) callback for desktop applications
        if ("oob".equals(callbackUrl)) {
            return true;
        }
        
        // Check if callback URL matches pre-registered callback URI
        if (config.getCallbackURI() != null && !config.getCallbackURI().isEmpty()) {
            if (callbackUrl.equals(config.getCallbackURI())) {
                return true;
            }
        }

        // Check if callback URL has common root with application URI
        if (config.getApplicationURI() != null && !config.getApplicationURI().isEmpty()) {
            if (callbackUrl.startsWith(config.getApplicationURI())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Builds an OAuth error response in the standard format.
     * 
     * @param status HTTP status code
     * @param problem OAuth problem identifier
     * @param advice Additional problem advice (optional)
     * @return Response with OAuth error
     */
    private Response buildErrorResponse(Response.Status status, String problem, String advice) {
        StringBuilder errorBody = new StringBuilder();
        errorBody.append("oauth_problem=").append(problem);
        
        if (advice != null && !advice.isEmpty()) {
            errorBody.append("&oauth_problem_advice=").append(advice);
        }

        return Response.status(status)
            .entity(errorBody.toString())
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build();
    }

    /**
     * Simple OAuth application configuration class.
     */
    private static class OAuthAppConfig {
        private String consumerKey;
        private String consumerSecret;
        private String baseUrl;
        private String callbackURI;
        private String applicationURI;
        private java.util.List<String> scopes;

        public String getConsumerKey() { return consumerKey; }
        public void setConsumerKey(String consumerKey) { this.consumerKey = consumerKey; }
        
        public String getConsumerSecret() { return consumerSecret; }
        public void setConsumerSecret(String consumerSecret) { this.consumerSecret = consumerSecret; }
        
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        
        public String getCallbackURI() { return callbackURI; }
        public void setCallbackURI(String callbackURI) { this.callbackURI = callbackURI; }
        
        public String getApplicationURI() { return applicationURI; }
        public void setApplicationURI(String applicationURI) { this.applicationURI = applicationURI; }
        
        public java.util.List<String> getScopes() { return scopes; }
        public void setScopes(java.util.List<String> scopes) { this.scopes = scopes; }
    }
}
