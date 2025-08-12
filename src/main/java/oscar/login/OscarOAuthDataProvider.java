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
 */
package oscar.login;

import java.io.IOException;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.Logger;
import org.oscarehr.common.dao.ServiceAccessTokenDao;
import org.oscarehr.common.dao.ServiceClientDao;
import org.oscarehr.common.dao.ServiceRequestTokenDao;
import org.oscarehr.common.model.ServiceAccessToken;
import org.oscarehr.common.model.ServiceClient;
import org.oscarehr.common.model.ServiceRequestToken;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

/**
 * OAuth 1.0a Data Provider using ScribeJava
 * Migrated from CXF OAuth implementation
 */
@Component
@Transactional
public class OscarOAuthDataProvider {

    private static final Logger logger = MiscUtils.getLogger();

    @Autowired
    private ServiceRequestTokenDao serviceRequestTokenDao;
    @Autowired
    private ServiceAccessTokenDao serviceAccessTokenDao;
    @Autowired
    private ServiceClientDao serviceClientDao;
    @Value("${oauth.token.lifetime:3600}")
    private long requestTokenLifetime;

    /**
     * Generic OAuth 1.0a API for custom providers
     */
    private static class GenericOAuth10aApi extends DefaultApi10a {
        private final String requestTokenUrl;
        private final String accessTokenUrl;
        private final String authorizationUrl;
        
        public GenericOAuth10aApi(String baseUrl) {
            this.requestTokenUrl = baseUrl + "/oauth/request_token";
            this.accessTokenUrl = baseUrl + "/oauth/access_token";
            this.authorizationUrl = baseUrl + "/oauth/authorize";
        }
        
        @Override
        public String getRequestTokenEndpoint() {
            return requestTokenUrl;
        }
        
        @Override
        public String getAccessTokenEndpoint() {
            return accessTokenUrl;
        }
        
        @Override
        public String getAuthorizationBaseUrl() {
            return authorizationUrl;
        }
    }

    public ServiceClient getClient(String clientId) {
        logger.debug("getClient() called");
        return serviceClientDao.findByKey(clientId);
    }

    public OAuth1RequestToken createRequestToken(String clientKey, String callback, List<String> scopes) {
        logger.debug("createRequestToken() called");
        
        ServiceClient client = serviceClientDao.findByKey(clientKey);
        if (client == null) {
            throw new RuntimeException("Client not found: " + clientKey);
        }

        try {
            // Create OAuth 1.0a service
            OAuth10aService service = new ServiceBuilder(client.getKey())
                    .apiSecret(client.getSecret())
                    .callback(callback != null ? callback : "oob")
                    .build(new GenericOAuth10aApi(client.getUri())); // Use dynamically retrieved base URL

            // Get request token from ScribeJava
            OAuth1RequestToken requestToken = service.getRequestToken();

            // Store in database
            ServiceRequestToken srt = new ServiceRequestToken();
            srt.setCallback(callback);
            srt.setClientId(client.getId());
            srt.setDateCreated(new Date());
            srt.setTokenId(requestToken.getToken());
            srt.setTokenSecret(requestToken.getTokenSecret());
            srt.setScopes(scopes != null ? String.join(" ", scopes) : "");
            serviceRequestTokenDao.persist(srt);

            return requestToken;
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.error("Error creating request token", e);
            throw new RuntimeException("Failed to create request token", e);
        }
    }

    /**
     * Looks up a request‐token in the DB and returns a ScribeJava object,
     * but only if it hasn’t expired (per requestTokenLifetime seconds).
     */
    public OAuth1RequestToken getRequestToken(String requestTokenId) {
        ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(requestTokenId);
        if (srt == null) {
            return null;
        }

        long ageMillis = System.currentTimeMillis() - srt.getDateCreated().getTime();
        // lifetime is in seconds, so convert to ms
        if (ageMillis > (requestTokenLifetime * 1_000L)) {
            // expired -> delete and bail
            serviceRequestTokenDao.remove(srt);
            return null;
        }

        // still valid
        return new OAuth1RequestToken(srt.getTokenId(), srt.getTokenSecret());
    }

    public String finalizeAuthorization(String requestTokenId, String providerNo) {
        logger.debug("finalizeAuthorization() called");
        String verifier = UUID.randomUUID().toString();
        ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(requestTokenId);
        if (srt != null) {
            srt.setVerifier(verifier);
            srt.setProviderNo(providerNo);
            serviceRequestTokenDao.merge(srt);
        }
        return verifier;
    }

    public OAuth1AccessToken createAccessToken(String requestTokenId, String verifier) {
        logger.debug("createAccessToken() called");
        
        ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(requestTokenId);
        if (srt == null) {
            throw new RuntimeException("Invalid request token.");
        }
        
        if (!verifier.equals(srt.getVerifier())) {
            throw new RuntimeException("Invalid verifier.");
        }

        try {
            ServiceClient client = serviceClientDao.find(srt.getClientId());
            
            // Create OAuth 1.0a service
            OAuth10aService service = new ServiceBuilder(client.getKey())
                    .apiSecret(client.getSecret())
                    .callback(srt.getCallback())
                    .build(new GenericOAuth10aApi(client.getUri())); // Replace with actual base URL

            // Create request token for exchange
            OAuth1RequestToken requestToken = new OAuth1RequestToken(srt.getTokenId(), srt.getTokenSecret());
            
            // Exchange for access token
            OAuth1AccessToken accessToken = service.getAccessToken(requestToken, verifier);

            // Store in database
            ServiceAccessToken sat = new ServiceAccessToken();
            sat.setClientId(client.getId());
            sat.setDateCreated(new Date());
            sat.setIssued(System.currentTimeMillis() / 1000);
            sat.setLifetime(3600);
            sat.setTokenId(accessToken.getToken());
            sat.setTokenSecret(accessToken.getTokenSecret());
            sat.setProviderNo(srt.getProviderNo());
            sat.setScopes(srt.getScopes());
            serviceAccessTokenDao.persist(sat);
            
            // Remove request token
            serviceRequestTokenDao.remove(srt);

            return accessToken;
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.error("Error creating access token", e);
            throw new RuntimeException("Failed to create access token", e);
        }
    }

    public OAuth1AccessToken getAccessToken(String accessTokenId) {
        ServiceAccessToken sat = serviceAccessTokenDao.findByTokenId(accessTokenId);
        if (sat == null) {
            throw new RuntimeException("Invalid access token.");
        }
        
        return new OAuth1AccessToken(sat.getTokenId(), sat.getTokenSecret());
    }

    public void removeToken(String tokenId) {
        ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(tokenId);
        if (srt != null) {
            serviceRequestTokenDao.remove(srt);
        }
        ServiceAccessToken sat = serviceAccessTokenDao.findByTokenId(tokenId);
        if (sat != null) {
            serviceAccessTokenDao.remove(sat);
        }
    }

    /**
     * Return the secret associated with a given OAuth1 token.
     * First checks access-tokens, then request-tokens if not found.
     */
    public String getTokenSecret(String tokenId) {
        // try access‐token table
        ServiceAccessToken sat = serviceAccessTokenDao.findByTokenId(tokenId);
        if (sat != null) {
            return sat.getTokenSecret();
        }
        // fallback to request‐token table
        ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(tokenId);
        return srt != null ? srt.getTokenSecret() : null;
    }

    /**
     * Lookup which OSCAR providerNo “owns” this token.
     * First checks access-tokens, then request-tokens.
     */
    public String getProviderNoByToken(String tokenId) {
        // try access‐token table
        ServiceAccessToken sat = serviceAccessTokenDao.findByTokenId(tokenId);
        if (sat != null) {
            return sat.getProviderNo();
        }
        // fallback to request‐token table
        ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(tokenId);
        return srt != null ? srt.getProviderNo() : null;
    }

        public OAuth10aService buildService(String clientKey, String callbackUrl) {
        ServiceClient client = getClient(clientKey);
        ServiceBuilder builder = new ServiceBuilder(client.getKey())
            .apiSecret(client.getSecret());
        if (callbackUrl != null) {
            builder.callback(callbackUrl);
        }
        return builder.build(new GenericOAuth10aApi(client.getUri()));
    }

    /**
     * Retrieve the stored access token object by its ID.
     */
    public OAuth1AccessToken getStoredAccessToken(String tokenId) {
        return getAccessToken(tokenId);
    }
}
