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

import ca.openosp.openo.commn.model.Allergy;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryResponse;
import ca.openosp.openo.utility.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Vigilance API implementation of the AllergyCheckProvider.
 */
@Component
public class VigilanceProvider implements AllergyCheckProvider {

    private final VigilanceAllergyCheckService vigilanceAllergyCheckService;

    @Autowired
    public VigilanceProvider(VigilanceAllergyCheckService vigilanceAllergyCheckService) {
        this.vigilanceAllergyCheckService = vigilanceAllergyCheckService;
    }

    @Override
    public List<Allergy> checkAllergies(LoggedInInfo loggedInInfo, Integer demographicNo, String drugAtcCode, List<Allergy> currentAllergies) throws Exception {
        VigilanceQueryResponse response = vigilanceAllergyCheckService.checkAllergies(loggedInInfo, demographicNo, drugAtcCode);
        return VigilanceAllergyMapper.mapToMatchingAllergies(currentAllergies, response);
    }
}
