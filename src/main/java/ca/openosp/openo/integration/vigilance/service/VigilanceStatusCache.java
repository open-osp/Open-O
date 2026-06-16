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

import ca.openosp.openo.integration.vigilance.model.VigilanceStatusResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Thread-safe in-memory cache for Vigilance API status responses.
 * Provides application-wide caching of service health status to avoid redundant
 * calls to the external status endpoint while still detecting outages within
 * a configurable TTL window.
 */
@Component
public class VigilanceStatusCache {

    private static final Logger log = LoggerFactory.getLogger(VigilanceStatusCache.class);
    private static final String OPERATIONAL_STATUS = "operational";

    private final AtomicReference<CacheEntry> cache = new AtomicReference<>();

    @Value("${vigilance.status.cache.ttl.minutes:60}")
    private long ttlMinutes;

    @Value("${vigilance.status.down.ttl.minutes:5}")
    private long downTtlMinutes;

    /**
     * Returns the cached status if it exists and is still valid (not expired).
     *
     * @return the cached VigilanceStatusResponse, or null if cache is empty/expired
     */
    public VigilanceStatusResponse getIfPresent() {
        CacheEntry entry = cache.get();
        if (entry == null) {
            return null;
        }

        Instant now = Instant.now();
        if (now.isAfter(entry.expiresAt)) {
            log.debug("Vigilance status cache expired at {}", entry.expiresAt);
            return null;
        }

        return entry.statusResponse;
    }

    /**
     * Stores a healthy status response in the cache with normal TTL.
     *
     * @param statusResponse the status response to cache
     */
    public void putHealthy(VigilanceStatusResponse statusResponse) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(ttlMinutes * 60);
        cache.set(new CacheEntry(statusResponse, expiresAt));
        log.info("Vigilance status cached as healthy (expires at {})", expiresAt);
    }

    /**
     * Stores a down/unhealthy status in the cache with short TTL to prevent
     * hammering the API during an outage.
     *
     * @param statusResponse the status response indicating unhealthy state, or null if unknown
     */
    public void putDown(VigilanceStatusResponse statusResponse) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(downTtlMinutes * 60);
        cache.set(new CacheEntry(statusResponse, expiresAt));
        log.warn("Vigilance status cached as DOWN (expires at {})", expiresAt);
    }

    /**
     * Checks if the cached status indicates all Vigilance products are operational.
     * Returns false if cache is empty/expired or any product is not operational.
     *
     * @return true if all products are operational, false otherwise
     */
    public boolean isHealthy() {
        VigilanceStatusResponse response = getIfPresent();
        if (response == null) {
            return false;
        }

        List<VigilanceStatusResponse.Product> products = response.products();
        if (products == null || products.isEmpty()) {
            log.warn("Vigilance status cache contains no products");
            return false;
        }

        for (VigilanceStatusResponse.Product product : products) {
            if (!OPERATIONAL_STATUS.equalsIgnoreCase(product.status())) {
                log.debug("Product '{}' is not operational: {}", product.name(), product.status());
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if a specific product (e.g., RxAuth) is operational.
     *
     * @param productName the name of the product to check
     * @return true if the product exists and is operational, false otherwise
     */
    public boolean isProductOperational(String productName) {
        VigilanceStatusResponse response = getIfPresent();
        if (response == null || StringUtils.isBlank(productName)) {
            return false;
        }

        for (VigilanceStatusResponse.Product product : response.products()) {
            if (productName.equalsIgnoreCase(product.name())) {
                return OPERATIONAL_STATUS.equalsIgnoreCase(product.status());
            }
        }

        log.debug("Product '{}' not found in status response", productName);
        return false;
    }

    /**
     * Checks if the cache has any entry (healthy or down), regardless of validity.
     * This is used to distinguish between "never checked yet" vs "cache says unhealthy".
     *
     * @return true if there's a cached entry, false if cache is empty
     */
    public boolean hasValidEntry() {
        CacheEntry entry = cache.get();
        return entry != null;
    }

    private record CacheEntry(VigilanceStatusResponse statusResponse, Instant expiresAt) {
    }
}
