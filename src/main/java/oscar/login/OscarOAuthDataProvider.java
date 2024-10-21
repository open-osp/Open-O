/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.login;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.oscarehr.common.dao.ServiceRequestTokenDaoImpl;

import org.apache.cxf.rs.security.oauth.data.AccessToken;
import org.apache.cxf.rs.security.oauth.data.AccessTokenRegistration;
import org.apache.cxf.rs.security.oauth.data.AuthorizationInput;
import org.apache.cxf.rs.security.oauth.data.Client;
import org.apache.cxf.rs.security.oauth.data.OAuthPermission;
import org.apache.cxf.rs.security.oauth.data.RequestToken;
import org.apache.cxf.rs.security.oauth.data.RequestTokenRegistration;
import org.apache.cxf.rs.security.oauth.data.Token;
import org.apache.cxf.rs.security.oauth.data.UserSubject;
import org.apache.cxf.rs.security.oauth.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth.provider.OAuthServiceException;
import org.apache.logging.log4j.Logger;
import org.oscarehr.common.dao.ServiceAccessTokenDao;
import org.oscarehr.common.dao.ServiceClientDao;
import org.oscarehr.common.dao.ServiceRequestTokenDao;
import org.oscarehr.common.model.ServiceAccessToken;
import org.oscarehr.common.model.ServiceClient;
import org.oscarehr.common.model.ServiceRequestToken;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.oscarehr.util.SpringUtils;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// @EnableJpaRepositories("org.oscarehr.common.model")
@Component
@Transactional
public class OscarOAuthDataProvider implements OAuthDataProvider {

	Logger logger = MiscUtils.getLogger();
	
	// @Autowired	
	// private ServiceClientDao serviceClientDao ;
	@Autowired
	private ServiceRequestTokenDao serviceRequestTokenDao;
	@Autowired
	private ServiceAccessTokenDao serviceAccessTokenDao;
	@Autowired	
	private ServiceClientDao serviceClientDao;
	//private ServiceClientDao serviceClientDao = SpringUtils.getBean(ServiceClientDao.class);
	
	@Override
    public Client getClient(String clientId) throws OAuthServiceException {
        logger.debug("getClient() called");
        ServiceClient sc = serviceClientDao.findByKey(clientId);
        if (sc != null) {
            return new Client(sc.getKey(), sc.getSecret(), sc.getName(), sc.getUri());
        }
        return null;
    }
    @Override
    public RequestToken createRequestToken(RequestTokenRegistration reg) throws OAuthServiceException {
        logger.debug("createRequestToken() called");
        String tokenId = UUID.randomUUID().toString();
        String tokenSecret = UUID.randomUUID().toString();
        RequestToken rt = new RequestToken(reg.getClient(), tokenId, tokenSecret);
        StringBuilder sb = new StringBuilder();
        List<OAuthPermission> perms = new ArrayList<>();
        for (String scope : reg.getScopes()) {
            OAuthPermission p = new OAuthPermission(scope, scope);
            perms.add(p);
            sb.append(scope).append(" ");
        }
        rt.setScopes(perms);
        rt.setCallback(reg.getCallback());
        ServiceRequestToken srt = new ServiceRequestToken();
        srt.setCallback(rt.getCallback());
        srt.setClientId(serviceClientDao.findByKey(rt.getClient().getConsumerKey()).getId());
        srt.setDateCreated(new Date());
        srt.setTokenId(rt.getTokenKey());
        srt.setTokenSecret(rt.getTokenSecret());
        srt.setScopes(sb.toString().trim());
        serviceRequestTokenDao.persist(srt);
        return rt;
    }

    @Override
    public RequestToken getRequestToken(String requestToken) throws OAuthServiceException {
        logger.debug("getRequestToken() called");
        ServiceRequestToken serviceToken = serviceRequestTokenDao.findByTokenId(requestToken);
        if (serviceToken != null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR, -1);
            Date oneHourAgo = cal.getTime();
            if (serviceToken.getDateCreated().before(oneHourAgo)) {
                serviceRequestTokenDao.remove(serviceToken);
                return null;
            }
            ServiceClient sc = serviceClientDao.find(serviceToken.getClientId());
            Client newClient = new Client(sc.getKey(), sc.getSecret(), sc.getName(), sc.getUri());
            RequestToken rt = new RequestToken(newClient, serviceToken.getTokenId(), serviceToken.getTokenSecret());
            List<OAuthPermission> perms = new ArrayList<>();
            String[] scopes = serviceToken.getScopes().split(" ");
            for (String scope : scopes) {
                OAuthPermission p = new OAuthPermission(scope, scope);
                perms.add(p);
            }
            rt.setScopes(perms);
            rt.setCallback(serviceToken.getCallback());
            rt.setVerifier(serviceToken.getVerifier());
            return rt;
        }
        return null;
    }

	@Override
public String finalizeAuthorization(AuthorizationInput data) throws OAuthServiceException {
    logger.debug("finalizeAuthorization() called");
    RequestToken requestToken = data.getToken();
    requestToken.setVerifier(UUID.randomUUID().toString());
    ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(requestToken.getTokenKey());
    if (srt != null) {
        srt.setVerifier(requestToken.getVerifier());
        serviceRequestTokenDao.merge(srt);
    }
    return requestToken.getVerifier();
}
 
@Override
public AccessToken createAccessToken(AccessTokenRegistration reg) throws OAuthServiceException {
    logger.debug("createAccessToken() called");
    RequestToken requestToken = reg.getRequestToken();
    Client client = requestToken.getClient();
    ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(requestToken.getTokenKey());
    if (srt == null) {
        throw new OAuthServiceException("Invalid request token.");
    }
    String accessTokenString = UUID.randomUUID().toString();
    String tokenSecretString = UUID.randomUUID().toString();
    long issuedAt = System.currentTimeMillis() / 1000;
    AccessToken accessToken = new AccessToken(client, accessTokenString,
        tokenSecretString, 3600, issuedAt);
    UserSubject subject = new UserSubject(srt.getProviderNo(), new ArrayList<>());
    accessToken.setSubject(subject);
    accessToken.getClient().setLoginName(srt.getProviderNo());
    accessToken.setScopes(requestToken.getScopes());
    ServiceAccessToken sat = new ServiceAccessToken();
    ServiceClient sc = serviceClientDao.findByKey(client.getConsumerKey());
    sat.setClientId(sc.getId());
    sat.setDateCreated(new Date());
    sat.setIssued(issuedAt);
    sat.setLifetime(sc.getLifetime());
    sat.setTokenId(accessTokenString);
    sat.setTokenSecret(tokenSecretString);
    sat.setProviderNo(srt.getProviderNo());
    sat.setScopes(String.join(" ", requestToken.getScopes().stream().map(OAuthPermission::getPermission).toArray(String[]::new)));
    serviceAccessTokenDao.persist(sat);
    serviceRequestTokenDao.remove(srt); // Correctly removing the entity
    return accessToken;
}
 
@Override
public AccessToken getAccessToken(String accessToken) throws OAuthServiceException {
    ServiceAccessToken sat = serviceAccessTokenDao.findByTokenId(accessToken);
    if (sat == null) {
        throw new OAuthServiceException("Invalid access token.");
    }
    ServiceClient sc = serviceClientDao.find(sat.getClientId());
    Client c = getClient(sc.getKey());
    AccessToken accessTokenObj = new AccessToken(c, sat.getTokenId(),
        sat.getTokenSecret(), sat.getLifetime(), sat.getIssued());
    UserSubject subject = new UserSubject(sat.getProviderNo(), new ArrayList<>());
    accessTokenObj.setSubject(subject);
    accessTokenObj.getClient().setLoginName(sat.getProviderNo());
    List<OAuthPermission> perms = new ArrayList<>();
    String[] scopes = sat.getScopes().split(" ");
    for (String scope : scopes) {
        OAuthPermission p = new OAuthPermission(scope, scope);
        perms.add(p);
    }
    accessTokenObj.setScopes(perms);
    return accessTokenObj;
}
 
@Override
public void removeToken(Token token) throws OAuthServiceException {
    ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(token.getTokenKey());
    if (srt != null) {
        serviceRequestTokenDao.remove(srt);
    }
    ServiceAccessToken sat = serviceAccessTokenDao.findByTokenId(token.getTokenKey());
    if (sat != null) {
        serviceAccessTokenDao.remove(sat);
    }
}}