package org.oscarehr.ws.oauth.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import oscar.login.AppOAuth1Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import oscar.login.OscarOAuthDataProvider;

@Service
public class OAuth1SignatureVerifier {
    @Autowired
    private OscarOAuthDataProvider dataProvider;

    /**
     * Recomputes the OAuth1 signature for the incoming request and compares
     * it (constant-time) against the provided oauth_signature. Returns the
     * token if valid, or throws IllegalArgumentException if not.
     */
    public String verifySignature(HttpServletRequest req, AppOAuth1Config cfg) {
        // 1) Extract all oauth_* params
        Map<String,String> params = OAuthRequestParser.extractOAuthParameters(req);
        String token       = params.get(OAuthConstants.TOKEN);
        String incomingSig = params.get(OAuthConstants.SIGNATURE);
        if (token == null || incomingSig == null) {
            throw new IllegalArgumentException("Missing OAuth token or signature");
        }

        // 2) Build the ScribeJava service with our private API class
        OAuth10aService service = new ServiceBuilder(cfg.getConsumerKey())
            .apiSecret(cfg.getConsumerSecret())
            .build(new GenericApi(cfg.getBaseUrl()));

        // 3) Retrieve the stored token secret
        String tokenSecret = dataProvider.getTokenSecret(token);
        if (tokenSecret == null) {
            throw new IllegalArgumentException("Unknown or expired token: " + token);
        }
        OAuth1AccessToken at = new OAuth1AccessToken(token, tokenSecret);

        // 4) Reconstruct the exact same request
        OAuthRequest sreq = buildScribeRequest(req);

        // 5) Sign it
        service.signRequest(at, sreq);

        // 6) Extract computed signature and compare
        String authzHeader  = sreq.getHeaders().get("Authorization");
        String computedSig  = OAuthRequestParser.extractSignatureFromHeader(authzHeader);

        byte[] incomingBytes = incomingSig.getBytes(StandardCharsets.UTF_8);
        byte[] computedBytes = computedSig != null
            ? computedSig.getBytes(StandardCharsets.UTF_8)
            : new byte[0];

        if (!MessageDigest.isEqual(incomingBytes, computedBytes)) {
            throw new IllegalArgumentException("Invalid OAuth1 signature");
        }

        return token;
    }

    /**
     * After verifySignature, use this to look up which provider owns the token.
     */
    public String getProviderNo(String token) {
        return dataProvider.getProviderNoByToken(token);
    }

    /**
     * Builds a ScribeJava OAuthRequest matching the incoming servlet request,
     * including query/form params (but omitting oauth_*).
     */
    private OAuthRequest buildScribeRequest(HttpServletRequest req) {
        String url = req.getRequestURL().toString();
        String qs  = req.getQueryString();
        if (qs != null && !qs.isEmpty()) {
            url += "?" + qs;
        }
        Verb verb = Verb.valueOf(req.getMethod());
        OAuthRequest sreq = new OAuthRequest(verb, url);

        if ("POST".equalsIgnoreCase(req.getMethod())) {
            req.getParameterMap().forEach((k, vals) -> {
                if (!k.startsWith("oauth_")) {
                    for (String v : vals) {
                        sreq.addParameter(k, v);
                    }
                }
            });
        }
        return sreq;
    }

    /**
     * Private DefaultApi10a implementation, so we don’t need a public helper.
     */
    private static class GenericApi extends DefaultApi10a {
        private final String baseUrl;
        GenericApi(String baseUrl) { this.baseUrl = baseUrl; }

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
}
