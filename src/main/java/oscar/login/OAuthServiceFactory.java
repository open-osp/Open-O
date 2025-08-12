// src/main/java/oscar/login/OAuthServiceFactory.java
package oscar.login;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Component;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth10aService;

@Component
public class OAuthServiceFactory {
    private static final Logger logger = MiscUtils.getLogger();

    /**
     * Build an OAuth10aService for this app/config + callback.
     * Now uses our shared GenericOAuth10aApi implementation to DRY up endpoint definitions.
     */
    public OAuth10aService buildService(AppOAuth1Config cfg,
                                        String callbackUrl,
                                        HttpServletResponse resp)
                                        throws IOException {
        try {
            return new ServiceBuilder(cfg.getConsumerKey())
                .apiSecret(cfg.getConsumerSecret())
                .callback(callbackUrl)
                .build(new GenericOAuth10aApi(cfg.getBaseUrl()));
        } catch (Exception e) {
            logger.error("Failed to build OAuth service: {}", e.getMessage(), e);
            // send a 500 back to the client
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                           "OAuth service initialization error");
            return null;
        }
    }
}
