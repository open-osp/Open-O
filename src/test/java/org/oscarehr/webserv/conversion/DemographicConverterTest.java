/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.webserv.conversion;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import ca.openosp.openo.commn.model.Demographic;
import ca.openosp.openo.commn.model.DemographicExt;
import ca.openosp.openo.commn.model.Provider;
import ca.openosp.openo.webserv.rest.conversion.DemographicConverter;
import ca.openosp.openo.webserv.rest.to.model.DemographicTo1;

public class DemographicConverterTest extends DaoTestFixtures {

    private DemographicConverter demoConverter = new DemographicConverter();

    @Test
    public void testConversion() throws Exception {
        Demographic demo = new Demographic();
        EntityDataGenerator.generateTestDataForModelClass(demo);
        demo.setDateOfBirth("01");
        demo.setMonthOfBirth("01");
        demo.setYearOfBirth("01");

        Provider provider = new Provider();
        EntityDataGenerator.generateTestDataForModelClass(provider);
        demo.setProvider(provider);

        DemographicExt demoExt = new DemographicExt();
        EntityDataGenerator.generateTestDataForModelClass(demoExt);
        demo.setExtras(new DemographicExt[]{demoExt});

        DemographicTo1 demoTo = demoConverter.getAsTransferObject(getLoggedInInfo(), demo);
        Demographic demoCheck = demoConverter.getAsDomainObject(getLoggedInInfo(), demoTo);

        assertEquals(demo, demoCheck);
    }

}
