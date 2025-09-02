//CHECKSTYLE:OFF
/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
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
 * <p>
 * Modifications made by Magenta Health in 2024.
 */

package ca.openosp.openo.managers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.openosp.openo.commn.dao.PropertyDao;
import ca.openosp.openo.commn.model.Measurement;
import ca.openosp.openo.commn.model.MeasurementMap;
import ca.openosp.openo.commn.model.Property;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.SpringUtils;

import ca.openosp.OscarProperties;
import ca.openosp.openo.encounter.oscarMeasurements.MeasurementFlowSheet;

public interface MeasurementManager {

    public List<Measurement> getCreatedAfterDate(LoggedInInfo loggedInInfo, Date updatedAfterThisDateExclusive,
                                                 int itemsToReturn);

    public Measurement getMeasurement(LoggedInInfo loggedInInfo, Integer id);

    public List<Measurement> getMeasurementByType(LoggedInInfo loggedInInfo, Integer id, List<String> types);

    public List<Measurement> getMeasurementByDemographicIdAfter(LoggedInInfo loggedInInfo, Integer demographicId,
                                                                Date updateAfter);

    public List<MeasurementMap> getMeasurementMaps();

    public Measurement addMeasurement(LoggedInInfo loggedInInfo, Measurement measurement);

    public List<Measurement> getMeasurementsByProgramProviderDemographicDate(LoggedInInfo loggedInInfo,
                                                                             Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive,
                                                                             int itemsToReturn);

    public String getDShtml(String groupName);

    public boolean isProperty(String prop);

    public String findGroupId(String groupName);

    public void addMeasurementGroupDS(String groupName, String dsHTML);

    public void removeMeasurementGroupDS(String propKey);

    public List<Measurement> getLatestMeasurementsByDemographicIdObservedAfter(LoggedInInfo loggedInInfo, Integer demographicId, Date observedDate);

    public static List<String> getFlowsheetDsHTML() {
        List<String> dsHtml = new ArrayList<String>();
        String path_set_by_property = OscarProperties.getInstance().getProperty("MEASUREMENT_DS_HTML_DIRECTORY");

        if (path_set_by_property != null) {
            File[] files1 = new File(path_set_by_property).listFiles();

            for (File file1 : files1) {
                if (file1.isFile()) {
                    dsHtml.add(file1.getName());
                }
            }
        }

        URL path_of_resource = MeasurementFlowSheet.class.getClassLoader()
                .getResource("/oscar/oscarEncounter/oscarMeasurements/flowsheets/html/");
        File[] files2 = new File(path_of_resource.getPath()).listFiles();

        for (File file2 : files2) {
            if (file2.isFile()) {
                dsHtml.add(file2.getName());
            }
        }

        return dsHtml;
    }

    public static String getPropertyValue(String prop) {
        PropertyDao propertyDao = (PropertyDao) SpringUtils.getBean(PropertyDao.class);
        Property p = propertyDao.checkByName(prop);
        String value = p.getValue();

        return value;
    }
}
