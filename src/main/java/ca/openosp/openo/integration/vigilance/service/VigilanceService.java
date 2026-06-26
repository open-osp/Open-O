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

package ca.openosp.openo.integration.vigilance.service;

import ca.openosp.openo.integration.vigilance.client.VigilanceClient;
import ca.openosp.openo.integration.vigilance.exception.VigilanceIntegrationException;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryRequest;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryViewerResponse;
import ca.openosp.openo.integration.vigilance.model.VigilanceStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for interacting with the Vigilance API.
 */
@Service
public class VigilanceService {

    private static final Logger log = LoggerFactory.getLogger(VigilanceService.class);

    private final VigilanceClient vigilanceClient;
    private final VigilanceStatusCache statusCache;

    /**
     * Creates a new service with the Vigilance client and status cache.
     */
    @Autowired
    public VigilanceService(VigilanceClient vigilanceClient, VigilanceStatusCache statusCache) {
        this.vigilanceClient = vigilanceClient;
        this.statusCache = statusCache;
    }

    /**
     * Performs a query analysis by calling the Vigilance API.
     *
     * @param request the query request containing patient and service details
     * @return the response from the Vigilance API containing the analysis result
     */
    public VigilanceQueryViewerResponse queryAnalysis(VigilanceQueryRequest request) {
        return this.vigilanceClient.postForObject("/service/rxvengine/query", request, true);
    }

    /**
     * Returns cached status if available and healthy, otherwise null.
     * This is used as a pre-flight check before calling queryAnalysis to avoid
     * unnecessary API calls when the cache is still valid.
     *
     * @return cached VigilanceStatusResponse if healthy, null otherwise
     */
    public VigilanceStatusResponse getStatusIfUp() {
        if (!statusCache.isHealthy()) {
            return null;
        }
        return statusCache.getIfPresent();
    }

    /**
     * Checks if the cache has any entry (healthy or down), regardless of validity.
     * This is used to distinguish between "never checked yet" vs "cache says unhealthy".
     *
     * @return true if there's a cached entry, false if cache is empty
     */
    public boolean hasValidEntry() {
        return statusCache.hasValidEntry();
    }

    /**
     * Checks the status of the Vigilance API service and updates the cache.
     * On success, caches as healthy with normal TTL.
     * On failure or non-operational status, caches as down with short TTL.
     *
     * @return The status response from the Vigilance API, or null if unavailable
     */
    public VigilanceStatusResponse statusCheck() {
        try {
            VigilanceStatusResponse response = vigilanceClient.getStatus();
            if (response != null && isAllProductsOperational(response)) {
                statusCache.putHealthy(response);
                return response;
            } else {
                log.warn("Vigilance status check returned non-operational products: {}", response);
                statusCache.putDown(response);
                return null;
            }
        } catch (Exception e) {
            log.error("Failed to check Vigilance status", e);
            statusCache.putDown(null);
            throw new VigilanceIntegrationException("Vigilance status check failed: " + e.getMessage(), e);
        }
    }

    private boolean isAllProductsOperational(VigilanceStatusResponse response) {
        if (response == null || response.products() == null || response.products().isEmpty()) {
            return false;
        }
        for (VigilanceStatusResponse.Product product : response.products()) {
            if (!"operational".equalsIgnoreCase(product.status())) {
                log.debug("Product '{}' is not operational: {}", product.name(), product.status());
                return false;
            }
        }
        return true;
    }
}