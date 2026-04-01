package ca.openosp.openo.webserv.fhir;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import org.apache.cxf.rs.security.oauth2.filters.OAuthRequestFilter;

/**
 * Filter that applies OAuth 2.0 protection but ignores specific public paths.
 * Used to expose Metadata and Auth endpoints publicly while securing others.
 */
public class IgnorableOAuthFilter extends OAuthRequestFilter {

    @Override
    public void filter(ContainerRequestContext context) {
        String path = context.getUriInfo().getPath();

        // Skip authentication for metadata and auth endpoints
        // CXF getPath() returns relative path without leading slash (e.g. "auth/token", "metadata")
        if (path.equals("metadata") || path.endsWith("metadata")
            || path.startsWith("auth/") || path.startsWith("auth")
            || path.contains("/auth/") || path.contains("/auth")
            || path.contains(".well-known")) {
            return;
        }

        // Skip for OPTIONS (CORS preflight) - handled by container usually but good safety
        if ("OPTIONS".equalsIgnoreCase(context.getMethod())) {
            return;
        }

        super.filter(context);
    }
}
