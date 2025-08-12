package org.oscarehr.app;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.common.model.AppUser;
import org.oscarehr.common.model.ServiceClient;
import oscar.login.OscarOAuthDataProvider;

/**
 * Helper to execute OAuth1 requests with minimal boilerplate.
 */
@Component
public class OAuth1Executor {
    private final OscarOAuthDataProvider dataProvider;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public OAuth1Executor(OscarOAuthDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    /**
     * Execute a signed OAuth1 request.
     *
     * @param app   application definition containing OAuth config
     * @param user  authenticated user with stored token
     * @param verb  HTTP verb (GET, POST, DELETE, etc.)
     * @param path  request path to append to the service base URI
     * @param body  optional payload object to JSON-serialize (POST/PUT)
     * @return      raw ScribeJava response
     */
    public com.github.scribejava.core.model.Response execute(
            AppDefinition app,
            AppUser user,
            Verb verb,
            String path,
            Object body
    ) throws IOException, InterruptedException, ExecutionException {
        // 1) parse OAuth config and extract credentials
        final AppOAuth1Config cfg;
        final Map<String, String> authData;
        try {
            cfg = AppOAuth1Config.fromDocument(app.getConfig());
            authData = AppOAuth1Config.getKeySecret(user.getAuthenticationData());
        } catch (Exception e) {
            throw new IllegalStateException(
                "Failed to parse OAuth1 config or credentials for appId=" + app.getId(), e);
        }
        // 2) build service
        OAuth10aService service = dataProvider.buildService(cfg.getConsumerKey(), null);
        // 3) lookup stored access token
        OAuth1AccessToken token = dataProvider.getStoredAccessToken(authData.get("key"));
        // 4) construct request
        ServiceClient client = dataProvider.getClient(cfg.getConsumerKey());
        String url = client.getUri() + path;
        OAuthRequest req = new OAuthRequest(verb, url);
        if (body != null) {
            String payload = mapper.writeValueAsString(body);
            req.addHeader("Content-Type", "application/json; charset=UTF-8");
            req.setPayload(payload);
        }
        // 5) sign and execute
        service.signRequest(token, req);
        return service.execute(req);
    }
}
