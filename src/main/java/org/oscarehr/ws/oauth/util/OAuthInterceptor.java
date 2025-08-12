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

/*
 * Written by Brandon Aubie <brandon@aubie.ca>
 */

package org.oscarehr.ws.oauth.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptor;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.oscarehr.common.dao.AppDefinitionDao;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oscar.login.OscarOAuthDataProvider;
import oscar.login.AppOAuth1Config;

import org.oscarehr.PMmodule.dao.ProviderDao;

@Component
public class OAuthInterceptor implements PhaseInterceptor<Message> {

    @Autowired private AppDefinitionDao           appDefinitionDao;
    @Autowired private OscarOAuthDataProvider     oauthDataProvider;
    @Autowired private OAuth1SignatureVerifier    signatureVerifier;
    @Autowired private ProviderDao                providerDao;              

    @Override public String getPhase() { return Phase.PRE_INVOKE; }

    @Override
    public void handleMessage(Message message) throws Fault {
        HttpServletRequest req =
            (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);

        // 1) Skip non-OAuth1 requests
        if (!OAuthRequestParser.isOAuth1Request(req)) {
            return;
        }

        try {
            // 2) Load AppDefinition & config
            String consumerKey = OAuthRequestParser.getConsumerKey(req);
            AppDefinition appDef = appDefinitionDao.findByConsumerKey(consumerKey);
            if (appDef == null) {
                throw new IllegalArgumentException("Unknown consumer_key: " + consumerKey);
            }
            AppOAuth1Config cfg = AppOAuth1Config.fromDocument(appDef.getConfig());

            // 3) Delegate signature verification & grab the token
            String token = signatureVerifier.verifySignature(req, cfg);

            // 4) Resolve provider via ProviderDao, attach LoggedInInfo
            String providerNo = oauthDataProvider.getProviderNoByToken(token);
            var provider = providerDao.getProvider(providerNo);
            if (provider == null) {
                throw new IllegalArgumentException("Unknown provider for token: " + token);
            }

            LoggedInInfo info = new LoggedInInfo();
            info.setLoggedInProvider(provider);
            req.setAttribute(info.getLoggedInInfoKey(), info);

        } catch (Exception e) {
            throw new Fault(e);
        }
    }

    @Override public void handleFault(Message message) { /* no-op */ }
    @Override public Set<String> getBefore()  { return Collections.emptySet(); }
    @Override public Set<String> getAfter()   { return Collections.emptySet(); }
    @Override public Collection<PhaseInterceptor<? extends Message>> getAdditionalInterceptors() {
        return null;
    }
    @Override public String getId() { return getClass().getSimpleName(); }
}
