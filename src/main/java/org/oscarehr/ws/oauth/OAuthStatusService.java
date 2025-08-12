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

package org.oscarehr.ws.oauth;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;

import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ServiceAccessToken;
import org.oscarehr.common.dao.ServiceAccessTokenDao;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.logging.log4j.Logger;
import org.oscarehr.util.MiscUtils;

@Path("/oauth")
public class OAuthStatusService extends AbstractServiceImpl {

    private static final Logger logger = MiscUtils.getLogger();

    @Autowired
    private ServiceAccessTokenDao serviceAccessTokenDao;

    /**
     * Returns JSON containing the authenticated provider’s details
     * and the list of scopes/roles they granted when authorizing this token.
     */
    @GET
    @Path("/info")
    @Produces("application/json")
    public String oauthInfo(@Context HttpHeaders headers) {
        try {
            // 1) grab the LoggedInInfo (set earlier by your interceptor)
            LoggedInInfo loggedInInfo = getLoggedInInfo();
            Provider provider = loggedInInfo.getLoggedInProvider();

            // 2) extract the raw oauth_token from the Authorization header
            //    e.g. "OAuth oauth_consumer_key=…, oauth_token=XYZ, …"
            String authz = headers.getHeaderString("Authorization");
            String token = extractOAuthParam(authz, "oauth_token");

            // 3) lookup the persisted ServiceAccessToken so we can rehydrate scopes
            List<String> roles = Collections.emptyList();
            if (token != null) {
                ServiceAccessToken sat = serviceAccessTokenDao.findByTokenId(token);
                if (sat != null && sat.getScopes() != null) {
                    // scopes were stored as space-separated
                    roles = Arrays.asList(sat.getScopes().split("\\s+"));
                }
            }

            // 4) build JSON exactly as before
            JsonConfig config = new JsonConfig();
            config.registerJsonBeanProcessor(java.sql.Date.class,
                new JsDateJsonBeanProcessor());

            JSONObject obj = new JSONObject();
            obj.put("provider", JSONObject.fromObject(provider, config));
            obj.put("login", provider.getProviderNo());
            obj.put("roles", roles);

            return obj.toString();

        } catch (Exception e) {
            // Log the exception details on the server
            logger.error("Error building OAuth status response", e);
            // Return a generic error message in the JSON response
            return "{\"error\":\"An internal error occurred. Please try again later.\"}";
        }
    }

    /**
     * Utility: pull a given oauth_XXX parameter from an
     * OAuth Authorization header string.
     */
    private String extractOAuthParam(String authz, String name) {
        if (authz == null || !authz.startsWith("OAuth ")) {
            return null;
        }
        // strip "OAuth "
        String[] parts = authz.substring(6).split(",");
        for (String kv : parts) {
            kv = kv.trim();
            if (kv.startsWith(name + "=")) {
                String val = kv.split("=", 2)[1];
                // remove any surrounding quotes
                if (val.startsWith("\"") && val.endsWith("\"")) {
                    val = val.substring(1, val.length() - 1);
                }
                return val;
            }
        }
        return null;
    }
}
