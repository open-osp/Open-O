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
package org.oscarehr.app; 

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.apache.logging.log4j.Logger;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.common.model.AppUser;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.scribejava.core.model.Verb;

import oscar.log.LogAction;

/**
 * REST helpers that perform OAuth1-protected calls and audit them.
 * Provides static wrappers for backward compatibility.
 */
@Component
public class OAuth1Utils {
    private static final Logger logger = MiscUtils.getLogger();
    private static final JSONProvider<Object> jsonProvider = new JSONProvider<>();
    private static OAuth1Utils self;

    private final OAuth1Executor executor;

    @Autowired
    public OAuth1Utils(OAuth1Executor executor) {
        this.executor = executor;
        jsonProvider.setDropRootElement(true);
        self = this;
    }

    /**
     * Provide a CXF JSONProvider that drops root elements, for web client usage.
     */
    public static List<Object> getProviderK2A() {
        return List.of(jsonProvider);
    }

    // --- internal instance methods ---
    private String doGet(
        LoggedInInfo info,
        AppDefinition app,
        AppUser user,
        String requestURI,
        String baseRequestURI
    ) {
        try {
            var resp = executor.execute(app, user, Verb.GET, requestURI, null);
            int code = resp.getCode();
            if (code < 200 || code >= 300) {
                logger.warn("GET {} -> status {} (baseURI={})", requestURI, code, baseRequestURI);
            }
            String body = resp.getBody();
            LogAction.addLog(
                info,
                "oauth1.GET, AppId=" + app.getId() + ", AppUser=" + user.getId(),
                baseRequestURI,
                "AppUser=" + user.getId(),
                null,
                body
            );
            return body;
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.error("Error during GET to {}", requestURI, e);
        }
        return null;
    }

    private String doPost(
        LoggedInInfo info,
        AppDefinition app,
        AppUser user,
        String requestURI,
        String baseRequestURI,
        Object payload
    ) {
        try {
            var resp = executor.execute(app, user, Verb.POST, requestURI, payload);
            String body = resp.getBody();
            LogAction.addLog(
                info,
                "oauth1.POST, AppId=" + app.getId() + ", AppUser=" + user.getId(),
                baseRequestURI,
                "AppUser=" + user.getId(),
                null,
                body
            );
            return body;
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.error("Error during POST to {}", requestURI, e);
        }
        return null;
    }

    private void doDelete(
        LoggedInInfo info,
        AppDefinition app,
        AppUser user,
        String requestURI,
        String auditURI
    ) {
        try {
            var resp = executor.execute(app, user, Verb.DELETE, requestURI, null);
            int code = resp.getCode();
            String body = resp.getBody();
            logger.info("DELETE {} -> {}: {}", requestURI, code, body);
            if (code < 200 || code >= 300) {
                logger.error("DELETE request failed (status={}): {}", code, body);
            }
            LogAction.addLog(
                info,
                "oauth1.DELETE, AppId=" + app.getId() + ", AppUser=" + user.getId(),
                auditURI,
                "AppUser=" + user.getId(),
                null,
                body
            );
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.error("Error during DELETE to {}", requestURI, e);
        }
    }

    // --- static wrappers for backward compatibility ---

    public static String getOAuthGetResponse(
        LoggedInInfo info,
        AppDefinition app,
        AppUser user,
        String requestURI,
        String baseRequestURI
    ) {
        return self.doGet(info, app, user, requestURI, baseRequestURI);
    }

    public static String getOAuthPostResponse(
        LoggedInInfo info,
        AppDefinition app,
        AppUser user,
        String requestURI,
        String baseRequestURI,
        List<Object> providers,
        Object payload
    ) {
        return self.doPost(info, app, user, requestURI, baseRequestURI, payload);
    }

    public static void getOAuthDeleteResponse(
        AppDefinition app,
        AppUser user,
        String requestURI,
        String auditURI
    ) {
        // legacy delete had no info param
        self.doDelete(null, app, user, requestURI, auditURI);
    }
}
