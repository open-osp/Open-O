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

package ca.openosp.openo.integration.vigilance.client;

import ca.openosp.openo.integration.vigilance.exception.VigilanceIntegrationException;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryResponse;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryViewerResponse;
import ca.openosp.openo.integration.vigilance.model.VigilanceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * Client for interacting with the Vigilance integration services.
 * Provides methods to send requests and retrieve status information from the Vigilance API.
 */
@Component
public class VigilanceClient {

    @Value("${vigilance.base.url:}")
    private String baseUrl;

    @Value("${vigilance.status.base.url:}")
    private String statusBaseUrl;

    @Value("${vigilance.status.endpoint:}")
    private String statusEndpoint;

    private final WebClient vigilanceWebClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs a new VigilanceClient.
     *
     * @param vigilanceWebClient the OAuth2-configured WebClient for Vigilance API calls
     */
    public VigilanceClient(
            @Qualifier("vigilanceWebClient") WebClient vigilanceWebClient) {
        this.vigilanceWebClient = vigilanceWebClient;
    }

    /**
     * Sends a POST request to the specified service endpoint with a request body serialized as JSON.
     * The request is sent as application/x-www-form-urlencoded with the JSON body in the 'intrant' field.
     *
     * @param serviceEndPoint the endpoint path to call
     * @param requestBody the object to be serialized as JSON and sent
     * @return the deserialized response object
     * @throws VigilanceIntegrationException if serialization fails or the API returns an error
     */
    @SuppressWarnings("unchecked")
    public VigilanceQueryViewerResponse postForObject(String serviceEndPoint, VigilanceRequest requestBody) {
        try {
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("intrant", jsonBody);

            String jsonResponse = this.vigilanceWebClient.post()
                    .uri(UriComponentsBuilder.fromHttpUrl(baseUrl)
                            .path(serviceEndPoint)
                            .build()
                            .toUri()
                    )
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> reactor.core.publisher.Mono.error(
                                        new VigilanceIntegrationException("Vigilance API error: " + response.statusCode() + " - " + body)))
                    )
                    .bodyToMono(String.class)
                    .block();

            VigilanceQueryResponse parsed = objectMapper.readValue(jsonResponse, VigilanceQueryResponse.class);
            return new VigilanceQueryViewerResponse(jsonResponse, parsed);
        } catch (IOException e) {
            throw new VigilanceIntegrationException("Failed to process Vigilance API request", e);
        } catch (Exception e) {
            if (e instanceof VigilanceIntegrationException) throw e;
            throw new VigilanceIntegrationException("Unexpected error during Vigilance API call", e);
        }
    }

    /**
     * Sends a POST request to the specified service endpoint.
     *
     * @param serviceEndPoint the endpoint path to call
     * @return the response body as a string
     */
    public String post(String serviceEndPoint) {
        return this.vigilanceWebClient.post()
                .uri(UriComponentsBuilder.fromHttpUrl(baseUrl).path(serviceEndPoint).build().toUri())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * Sends a POST request to the specified service endpoint and returns the raw response body as a string.
     * Useful for endpoints that return non-JSON content (e.g., HTML).
     *
     * @param serviceEndPoint the endpoint path to call
     * @param requestBody the object to be serialized as JSON and sent
     * @return the raw response body as a string
     * @throws VigilanceIntegrationException if serialization fails or the API returns an error
     */
    public String postForHtml(String serviceEndPoint, VigilanceRequest requestBody) {
        try {
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("intrant", jsonBody);

            return this.vigilanceWebClient.post()
                    .uri(UriComponentsBuilder.fromHttpUrl(baseUrl)
                            .path(serviceEndPoint)
                            .build()
                            .toUri()
                    )
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> reactor.core.publisher.Mono.error(
                                        new VigilanceIntegrationException("Vigilance API error: " + response.statusCode() + " - " + body)))
                    )
                    .bodyToMono(String.class)
                    .block();
        } catch (IOException e) {
            throw new VigilanceIntegrationException("Failed to process Vigilance API request", e);
        } catch (Exception e) {
            if (e instanceof VigilanceIntegrationException) throw e;
            throw new VigilanceIntegrationException("Unexpected error during Vigilance API call", e);
        }
    }

    /**
     * Makes a GET request to the status API endpoint.
     * Status API uses a different host than other Vigilance services.
     */
    public String getStatus() {
        return this.vigilanceWebClient.get()
                .uri(UriComponentsBuilder.fromHttpUrl(statusBaseUrl)
                        .path(statusEndpoint)
                        .build()
                        .toUri()
                )
                .retrieve()
                .onStatus(HttpStatus::isError, response ->
                    response.bodyToMono(String.class)
                            .flatMap(body -> reactor.core.publisher.Mono.error(
                                    new VigilanceIntegrationException("Vigilance Status API error: " + response.statusCode() + " - " + body)
                            ))
                )
                .bodyToMono(String.class)
                .block();
    }
}