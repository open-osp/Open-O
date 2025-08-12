package oscar.login;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class GenericOAuth10aApi extends DefaultApi10a {
    private final String baseUrl;
    public GenericOAuth10aApi(String baseUrl) { this.baseUrl = baseUrl; }

    @Override
    public String getRequestTokenEndpoint() {
        return baseUrl + "/oauth/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return baseUrl + "/oauth/access_token";
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return baseUrl + "/oauth/authorize";
    }
}
