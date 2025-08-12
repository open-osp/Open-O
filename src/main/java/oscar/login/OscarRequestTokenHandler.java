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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

/**
 * OAuth 1.0a Request Token Handler using ScribeJava library.
 * 
 * This class handles the first step of the OAuth 1.0a flow:
 * 1. Obtains a request token from the OAuth provider
 * 2. Stores the temporary token and secret
 * 3. Redirects the user to the authorization URL
 * 
 * Migrated from Apache CXF to ScribeJava for OAuth implementation.
 */
@Component
public class OscarRequestTokenHandler {
    private static final Logger logger = MiscUtils.getLogger();

    @Autowired
    private OAuthConfigService    configService;

    @Autowired
    private OAuthServiceFactory   serviceFactory;

    @Autowired
    private OscarOAuthDataProvider dataProvider;

    @Value("${oauth.token.lifetime:3600}")
    private long tokenLifetime;

    @Value("${oauth.default.scope:}")
    private String defaultScope;

    public void handleRequestToken(HttpServletRequest request,
                                   HttpServletResponse response,
                                   AppDefinition app,
                                   String callbackUrl) throws IOException {
        try {
            // 1) Validate App + callback
            if (!configService.validateApp(app, response)
             || !configService.validateCallbackFormat(callbackUrl, response)) {
                return;
            }

            // 2) Load consumerKey/secret/baseUrl
            AppOAuth1Config cfg = configService.loadConfig(app, response);
            if (cfg == null) return;

            // 3) Enforce registered‐callback rules
            if (!configService.validateRegisteredCallback(cfg, callbackUrl, response)) {
                return;
            }

            // 4) Build OAuth10aService via factory
            OAuth10aService service = serviceFactory.buildService(cfg, callbackUrl, response);
            if (service == null) return;

            // 5) Fetch request token
            OAuth1RequestToken token;
            try {
                token = service.getRequestToken();
            } catch (Exception e) {
                logger.error("Failed to obtain request token: {}", e.getMessage(), e);
                response.sendError(HttpServletResponse.SC_BAD_GATEWAY,
                                   "OAuth provider communication error");
                return;
            }

            // 6) Persist token (plus any scopes)
            List<String> scopes = null;
            if (!defaultScope.isBlank()) {
                scopes = Arrays.stream(defaultScope.split(","))
                               .map(String::trim)
                               .filter(s -> !s.isEmpty())
                               .collect(Collectors.toList());
            }
            dataProvider.createRequestToken(
                token.getToken(),
                token.getTokenSecret(),
                scopes
            );

            // 7) Redirect to the authorization URL
            String authUrl = service.getAuthorizationUrl(token);
            if (authUrl == null || authUrl.isBlank()) {
                logger.error("Authorization URL was blank");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                   "Invalid authorization URL");
            } else {
                response.sendRedirect(authUrl);
            }

        } catch (IOException e) {
            logger.error("I/O error during OAuth request token handling", e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during OAuth request token handling", e);
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                   "OAuth processing error");
            }
        }
    }
}
