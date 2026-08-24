/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package ca.openosp.openo.integration.vigilance.config;

import ca.openosp.openo.webserv.oauth2.OpenOOAuth2ClientProvider;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * OAuth2 client provider for the Vigilance integration.
 * Extends {@link OpenOOAuth2ClientProvider} to provide custom token parameters
 * and a specialized WebClient bean for Vigilance API calls.
 */
@Configuration
@ConditionalOnVigilanceEnabled
public class VigilanceOAuth2ClientProvider extends OpenOOAuth2ClientProvider {

    private final String userId;

    /**
     * Constructs the provider with core OAuth2 credentials.
     *
     * @param clientId     the OAuth2 client ID
     * @param clientSecret the OAuth2 client secret
     * @param tokenUri     the URI of the authorization server's token endpoint
     * @param userId       the user_id required by the Vigilance authorization server
     */
    public VigilanceOAuth2ClientProvider(
            @Value("${vigilance.oauth2.client.id:}") String clientId,
            @Value("${vigilance.oauth2.client.secret:}") String clientSecret,
            @Value("${vigilance.oauth2.client.token_uri:}") String tokenUri,
            @Value("${vigilance.oauth2.client.user_id:}") String userId) {
        super(clientId, clientSecret, tokenUri);
        this.userId = userId;
    }

    /**
     * Adds custom parameters to the OAuth2 token request.
     * Specifically adds the 'user_id' required by the Vigilance authorization server.
     *
     * @return A MultiValueMap containing the custom token parameters
     */
    @Override
    protected MultiValueMap<String, String> getCustomTokenParameters() {
        MultiValueMap<String, String> params = super.getCustomTokenParameters();
        params.add("user_id", this.userId);
        return params;
    }

    /**
     * Creates and configures the WebClient used for Vigilance API integrations.
     *
     * @return a configured {@link WebClient} instance
     */
    @Bean(name = "vigilanceWebClient")
    public WebClient getVigilanceClient() {
        return super.buildClient();
    }

    /**
     * Creates a plain WebClient without OAuth2 authentication.
     * Used for endpoints that do not require an access token (e.g., status API).
     */
    @Bean(name = "vigilanceStatusWebClient")
    public WebClient getVigilanceStatusClient() {
        HttpClient httpClient = HttpClient.create()
                .resolver(DefaultAddressResolverGroup.INSTANCE)
                .responseTimeout(Duration.ofSeconds(5));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
