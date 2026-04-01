package ca.openosp.openo.webserv.fhir;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SmartConfigurationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Construct base URL from request
        String scheme = httpRequest.getScheme();
        String serverName = httpRequest.getServerName();
        int serverPort = httpRequest.getServerPort();
        String contextPath = httpRequest.getContextPath(); // e.g. "/oscar"

        // Handle standard ports
        String portStr = "";
        if (!((scheme.equals("http") && serverPort == 80) || (scheme.equals("https") && serverPort == 443))) {
            portStr = ":" + serverPort;
        }

        String baseUrl = scheme + "://" + serverName + portStr + contextPath;
        String authUrl = baseUrl + "/ws/fhir/auth/authorize";
        String tokenUrl = baseUrl + "/ws/fhir/auth/token";

        String json = "{\n" +
                "  \"authorization_endpoint\": \"" + authUrl + "\",\n" +
                "  \"token_endpoint\": \"" + tokenUrl + "\",\n" +
                "  \"capabilities\": [\n" +
                "    \"launch-ehr\",\n" +
                "    \"client-public\",\n" +
                "    \"client-confidential-symmetric\",\n" +
                "    \"context-ehr-patient\",\n" +
                "    \"sso-openid-connect\"\n" +
                "  ]\n" +
                "}";

        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write(json);
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}
