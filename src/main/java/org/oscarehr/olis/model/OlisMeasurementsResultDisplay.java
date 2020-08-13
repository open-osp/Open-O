/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.olis.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OlisMeasurementsResultDisplay {
    
    private OlisLabResultDisplay parentLab;

    private int measurementObxIndex;
    private String testResultName = "";
    private String status = "";
    private String resultValue = "";
    private String flag = "";
    private String referenceRange = "";
    private String units = "";
    private boolean isAbnormal = false;
    private String natureOfAbnormalText = "";
    private List<String> comments = new ArrayList<>();
    private boolean isAttachment = false;
    private boolean isInvalid = false;
    private boolean isBlocked = false;
    private OlisLabResultSortable resultSortable;
            
    OlisMeasurementsResultDisplay() { }

    public int getMeasurementObxIndex() {
        return measurementObxIndex;
    }
    public void setMeasurementObxIndex(int measurementObxIndex) {
        this.measurementObxIndex = measurementObxIndex;
    }

    public OlisLabResultDisplay getParentLab() {
        return parentLab;
    }
    public void setParentLab(OlisLabResultDisplay parentLab) {
        this.parentLab = parentLab;
    }

    public String getTestResultName() {
        return testResultName;
    }
    public void setTestResultName(String testResultName) {
        this.testResultName = testResultName;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultValue() {
        return resultValue;
    }
    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getReferenceRange() {
        return referenceRange;
    }
    public void setReferenceRange(String referenceRange) {
        this.referenceRange = referenceRange;
    }

    public String getUnits() {
        return units;
    }
    public void setUnits(String units) {
        this.units = units;
    }

    public boolean isAbnormal() {
        return isAbnormal;
    }
    public void setAbnormal(boolean abnormal) {
        isAbnormal = abnormal;
    }

    public String getNatureOfAbnormalText() {
        return natureOfAbnormalText;
    }
    public void setNatureOfAbnormalText(String natureOfAbnormalText) {
        this.natureOfAbnormalText = natureOfAbnormalText;
    }

    public List<String> getComments() {
        return comments;
    }
    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public boolean isAttachment() {
        return isAttachment;
    }
    public void setIsAttachment(boolean attachment) {
        isAttachment = attachment;
    }

    public boolean isInvalid() {
        return isInvalid;
    }
    public void setInvalid(boolean invalid) {
        isInvalid = invalid;
    }

    public boolean isBlocked() {
        return isBlocked;
    }
    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public OlisLabResultSortable getResultSortable() {
        return resultSortable;
    }
    public void setResultSortable(OlisLabResultSortable resultSortable) {
        this.resultSortable = resultSortable;
    }

    /**
     * Compares two OlisMeasurementResultDisplay objects to order them to OLIS' specifications
     * It first sorts by the information in the lab request
     * If the previous attributes are all the same, then it proceeds to sort the results using the OLIS_RESULT_COMPARATOR
     */
    static final Comparator<OlisMeasurementsResultDisplay> DEFAULT_OLIS_SORT_COMPARATOR = new Comparator<OlisMeasurementsResultDisplay>() {
        @Override
        public int compare(OlisMeasurementsResultDisplay o1, OlisMeasurementsResultDisplay o2) {
            // Compares the parent lab requests
            int compared = OlisLabResultDisplay.OLIS_LAB_RESULT_DISPLAY_COMPARATOR.compare(o1.getParentLab(), o2.getParentLab());

            // If the two measurements are under the same request, compares the results
            if (compared == 0) {
                compared = OlisLabResultSortable.OLIS_RESULT_COMPARATOR.compare(o1.getResultSortable(), o2.getResultSortable());
            }
            
            // Returns the value retrieved from comparing
            return compared;
        }
    };
}
