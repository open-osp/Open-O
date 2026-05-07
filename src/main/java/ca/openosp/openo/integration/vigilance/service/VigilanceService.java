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
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryRequest;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for interacting with the Vigilance API.
 */
@Service
public class VigilanceService {

    private final VigilanceClient vigilanceClient;

    @Autowired
    public VigilanceService(VigilanceClient vigilanceClient) {
        this.vigilanceClient = vigilanceClient;
    }

    /**
     * Performs a query analysis by calling the Vigilance API.
     * 
     * @param request the query request containing patient and service details
     * @return the response from the Vigilance API containing the analysis result
     */
    public VigilanceQueryResponse queryAnalysis(VigilanceQueryRequest request) {
        return this.vigilanceClient.postForObject("/service/rxvengine/query", request, VigilanceQueryResponse.class);
    }
     
    /**
     * Checks the status of the Vigilance API service.
     * 
     * @return The status response from the Vigilance API as a string
     */
    public String statusCheck() {
        return vigilanceClient.getStatus();
    }
}