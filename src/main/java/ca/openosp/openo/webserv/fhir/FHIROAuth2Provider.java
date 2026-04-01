package ca.openosp.openo.webserv.fhir;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeDataProvider;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * OAuth 2.0 Data Provider for SMART on FHIR.
 * Persists tokens to existing 'oauth_access_token' and 'oauth_code' tables
 * (originally intended for Spring Security OAuth, but reused here for CXF).
 *
 * Uses JSON serialization to store complex objects in the BLOB columns.
 */
public class FHIROAuth2Provider implements AuthorizationCodeDataProvider {

    private static final ThreadLocal<Boolean> PKCE_REQUEST = new ThreadLocal<>();

    public static void setPkceRequest(boolean isPkce) {
        if (isPkce) {
            PKCE_REQUEST.set(true);
        } else {
            PKCE_REQUEST.remove();
        }
    }

    private JdbcTemplate jdbcTemplate;
    private ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Client getClient(String clientId) throws OAuthServiceException {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT client_id, client_secret, web_server_redirect_uri, scope FROM oauth_client_details WHERE client_id = ?",
                new Object[]{clientId},
                (rs, rowNum) -> {
                    String secret = rs.getString("client_secret");
                    boolean isConfidential = (secret != null && !secret.trim().isEmpty());

                    // Dynamic PKCE check: If the request is flagged as PKCE by the OAuth2Service ThreadLocal, treat as public client dynamically
                    if (Boolean.TRUE.equals(PKCE_REQUEST.get())) {
                        System.out.println("[FHIR-OAUTH-DEBUG] Bypassing secret check via ThreadLocal! isConfidential -> false");
                        isConfidential = false;
                        secret = null; // CXF 3.5 exactly requires getClientSecret() == null for isValidPublicClient to pass
                    }

                    Client c = new Client(rs.getString("client_id"), secret, isConfidential);
                    c.setConfidential(isConfidential);

                    String uri = rs.getString("web_server_redirect_uri");
                    if (uri != null && !uri.isEmpty()) {
                        c.setRedirectUris(Collections.singletonList(uri));
                    }

                    String scope = rs.getString("scope");
                    if (scope != null && !scope.isEmpty()) {
                        List<String> scopes = new ArrayList<>();
                        for (String s : scope.split(",")) {
                            scopes.add(s.trim());
                        }
                        c.setRegisteredScopes(scopes);
                    }
                    return c;
                }
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new OAuthServiceException("Error retrieving client: " + clientId, e);
        }
    }

    @Override
    public ServerAccessToken createAccessToken(org.apache.cxf.rs.security.oauth2.common.AccessTokenRegistration reg) throws OAuthServiceException {
        try {
            // Create a default BearerAccessToken
            org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken token =
                new org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken(reg.getClient(), 3600L);

            token.setSubject(reg.getSubject());
            token.setGrantType(reg.getGrantType());
            token.setAudiences(reg.getAudiences());
            token.setScopes(convertScopeToPermissions(reg.getClient(), reg.getRequestedScope()));

            // Generate IDs
            // BearerAccessToken constructor generates tokenKey

            saveAccessToken(token);
            return token;
        } catch (Exception e) {
            throw new OAuthServiceException("Error creating access token", e);
        }
    }

    private void saveAccessToken(ServerAccessToken accessToken) {
        try {
            String tokenId = accessToken.getTokenKey();
            String tokenJson = objectMapper.writeValueAsString(accessToken);
            String authId = java.util.UUID.randomUUID().toString();

            String username = (accessToken.getSubject() != null) ? accessToken.getSubject().getLogin() : null;
            String clientId = accessToken.getClient().getClientId();
            String refreshToken = (accessToken.getRefreshToken() != null) ? accessToken.getRefreshToken() : null;

            jdbcTemplate.update(
                "INSERT INTO oauth_access_token (token_id, token, authentication_id, user_name, client_id, refresh_token) VALUES (?, ?, ?, ?, ?, ?)",
                tokenId,
                tokenJson.getBytes("UTF-8"),
                authId,
                username,
                clientId,
                refreshToken
            );
        } catch (Exception e) {
            throw new OAuthServiceException("Error saving access token", e);
        }
    }

    @Override
    public ServerAccessToken getAccessToken(String accessToken) throws OAuthServiceException {
        try {
            System.out.println("[FHIR-OAUTH-DEBUG] getAccessToken called, token=" + accessToken);
            byte[] blob = jdbcTemplate.queryForObject(
                "SELECT token FROM oauth_access_token WHERE token_id = ?",
                new Object[]{accessToken},
                byte[].class
            );

            if (blob == null) {
                System.out.println("[FHIR-OAUTH-DEBUG] getAccessToken: blob is null");
                return null;
            }
            ServerAccessToken result = objectMapper.readValue(blob, org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken.class);
            System.out.println("[FHIR-OAUTH-DEBUG] getAccessToken: deserialized OK, tokenKey=" + result.getTokenKey());
            return result;
        } catch (EmptyResultDataAccessException e) {
            System.out.println("[FHIR-OAUTH-DEBUG] getAccessToken: token not found in DB");
            return null;
        } catch (Exception e) {
            System.out.println("[FHIR-OAUTH-DEBUG] getAccessToken ERROR: " + e.getMessage());
            throw new OAuthServiceException("Error retrieving access token", e);
        }
    }

    // Removed removeAccessToken as it is not in interface

    @Override
    public ServerAccessToken refreshAccessToken(Client client, String refreshToken, List<String> requestedScopes)
            throws OAuthServiceException {
        throw new OAuthServiceException("Refresh token not supported yet");
    }

    @Override
    public ServerAuthorizationCodeGrant createCodeGrant(org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeRegistration reg) throws OAuthServiceException {
        try {
            // Create a new ServerAuthorizationCodeGrant
            ServerAuthorizationCodeGrant grant = new ServerAuthorizationCodeGrant(reg.getClient(), 600L); // 10 minutes
            grant.setRedirectUri(reg.getRedirectUri());
            grant.setSubject(reg.getSubject());
            grant.setRequestedScopes(reg.getRequestedScope());
            grant.setClientCodeChallenge(reg.getClientCodeChallenge());
            grant.setClientCodeChallengeMethod(reg.getClientCodeChallengeMethod());
            grant.setNonce(reg.getNonce());

            String code = grant.getCode();
            System.out.println("[FHIR-OAUTH-DEBUG] createCodeGrant called, code=" + code + ", subject=" + reg.getSubject());
            String json = objectMapper.writeValueAsString(grant);
            jdbcTemplate.update("INSERT INTO oauth_code (code, authentication) VALUES (?, ?)",
                code, json.getBytes("UTF-8"));
            System.out.println("[FHIR-OAUTH-DEBUG] Code grant stored in DB, code=" + code);
            return grant;
        } catch (Exception e) {
            System.out.println("[FHIR-OAUTH-DEBUG] ERROR in createCodeGrant: " + e.getMessage());
            throw new OAuthServiceException("Error creating code grant", e);
        }
    }

    @Override
    public ServerAuthorizationCodeGrant removeCodeGrant(String code) throws OAuthServiceException {
         try {
            System.out.println("[FHIR-OAUTH-DEBUG] removeCodeGrant called, code=" + code);
            byte[] blob = jdbcTemplate.queryForObject(
                "SELECT authentication FROM oauth_code WHERE code = ?",
                new Object[]{code},
                byte[].class
            );

            if (blob != null) {
                System.out.println("[FHIR-OAUTH-DEBUG] Code found in DB, deleting and deserializing");
                jdbcTemplate.update("DELETE FROM oauth_code WHERE code = ?", code);
                return objectMapper.readValue(blob, ServerAuthorizationCodeGrant.class);
            }
            System.out.println("[FHIR-OAUTH-DEBUG] Code NOT found (blob is null)");
            return null;
        } catch (EmptyResultDataAccessException e) {
            System.out.println("[FHIR-OAUTH-DEBUG] Code NOT found (EmptyResultDataAccessException)");
            return null;
        } catch (Exception e) {
            System.out.println("[FHIR-OAUTH-DEBUG] ERROR in removeCodeGrant: " + e.getMessage());
            throw new OAuthServiceException("Error removing code grant", e);
        }
    }

    @Override
    public List<ServerAccessToken> getAccessTokens(Client client, UserSubject subject) throws OAuthServiceException {
        return Collections.emptyList();
    }

    @Override
    public List<org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken> getRefreshTokens(Client client, UserSubject subject) throws OAuthServiceException {
        return Collections.emptyList();
    }

    @Override
    public void revokeToken(Client client, String tokenKey, String tokenTypeHint) throws OAuthServiceException {
         jdbcTemplate.update("DELETE FROM oauth_access_token WHERE token_id = ?", tokenKey);
    }

    @Override
    public List<ServerAuthorizationCodeGrant> getCodeGrants(Client c, UserSubject subject) {
        return Collections.emptyList();
    }

    @Override
    public ServerAccessToken getPreauthorizedToken(Client client, List<String> requestedScopes,
            UserSubject subject, String grantType) throws OAuthServiceException {
        return null;
    }

    @Override
    public List<org.apache.cxf.rs.security.oauth2.common.OAuthPermission> convertScopeToPermissions(Client client, List<String> requestedScopes) {
        List<org.apache.cxf.rs.security.oauth2.common.OAuthPermission> list = new ArrayList<>();
        // Default impl: just map strings to permissions
        if (requestedScopes != null) {
            for (String s : requestedScopes) {
                list.add(new org.apache.cxf.rs.security.oauth2.common.OAuthPermission(s));
            }
        }
        return list;
    }
}
