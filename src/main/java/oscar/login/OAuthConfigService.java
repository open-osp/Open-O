// src/main/java/oscar/login/OAuthConfigService.java
package oscar.login;

import java.io.IOException;
import java.net.URI;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Component;

@Component
public class OAuthConfigService {
    private static final Logger logger = MiscUtils.getLogger();

    public AppOAuth1Config loadConfig(AppDefinition app, HttpServletResponse resp) throws IOException {
        Object doc = app.getConfig();
        if (doc == null) {
            logger.error("Configuration document missing for app id {}", app.getId());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Configuration retrieval error");
            return null;
        }
        AppOAuth1Config cfg = AppOAuth1Config.fromDocument(doc);
        if (cfg == null
         || cfg.getConsumerKey() == null
         || cfg.getConsumerSecret() == null
         || cfg.getBaseUrl() == null) {
            logger.error("Invalid OAuth config in app id {}", app.getId());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid OAuth configuration");
            return null;
        }
        return cfg;
    }

    public boolean validateRegisteredCallback(AppOAuth1Config cfg, String callbackUrl, HttpServletResponse resp) throws IOException {
        // same logic you already had…
        if ((cfg.getCallbackURI()==null||cfg.getCallbackURI().isEmpty())
         && (cfg.getApplicationURI()==null||cfg.getApplicationURI().isEmpty())) return true;
        if (cfg.getCallbackURI()!=null && cfg.getCallbackURI().equals(callbackUrl)) return true;
        if (cfg.getApplicationURI()!=null && callbackUrl.startsWith(cfg.getApplicationURI())) return true;
        logger.error("Callback URL '{}' is not allowed by config (registered='{}', appBase='{}')",
                     callbackUrl, cfg.getCallbackURI(), cfg.getApplicationURI());
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid callback URL");
        return false;
    }

    public boolean validateCallbackFormat(String callbackUrl, HttpServletResponse resp) throws IOException {
        try {
            URI uri = new URI(callbackUrl);
            uri.toURL();
            return true;
        } catch (Exception e) {
            logger.error("Invalid callback URL: {}", callbackUrl);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid callback URL");
            return false;
        }
    }

    public boolean validateApp(AppDefinition app, HttpServletResponse resp) throws IOException {
        if (app == null) {
            logger.error("AppDefinition is null");
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Missing application configuration");
            return false;
        }
        return true;
    }
}
