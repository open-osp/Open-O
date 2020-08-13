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

import org.oscarehr.olis.OLISUtils;

import java.util.Comparator;
import java.util.Date;

public class OlisLabRequestSortable {
    private String name;
    private int obrIndex;
    private Date collectionDateTime;
    private String groupPlacerNo;
    private String sortKey;
    private OLISRequestNomenclature nomenclature;
    private String setId;
    

    public OlisLabRequestSortable() {
    }

    public OlisLabRequestSortable(String name, int obrIndex, Date collectionDateTime, String groupPlacerNo, String sortKey, OLISRequestNomenclature nomenclature, String setId) {
        this.name = name;
        this.obrIndex = obrIndex;
        this.collectionDateTime = collectionDateTime;
        this.groupPlacerNo = groupPlacerNo;
        this.sortKey = sortKey;
        this.nomenclature = nomenclature;
        this.setId = setId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getObrIndex() {
        return obrIndex;
    }
    public void setObrIndex(int obrIndex) {
        this.obrIndex = obrIndex;
    }
    
    public Date getCollectionDateTime() {
        return collectionDateTime;
    }
    public void setCollectionDateTime(Date collectionDateTime) {
        this.collectionDateTime = collectionDateTime;
    }

    public String getGroupPlacerNo() {
        return groupPlacerNo;
    }
    public void setGroupPlacerNo(String groupPlacerNo) {
        this.groupPlacerNo = groupPlacerNo;
    }

    public String getSortKey() {
        return sortKey;
    }
    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public OLISRequestNomenclature getNomenclature() {
        return nomenclature;
    }
    public void setNomenclature(OLISRequestNomenclature nomenclature) {
        this.nomenclature = nomenclature;
    }

    public String getSetId() {
        return setId;
    }
    public void setSetId(String setId) {
        this.setId = setId;
    }

    public static final Comparator<OlisLabRequestSortable> OLIS_REQUEST_COMPARATOR = new Comparator<OlisLabRequestSortable>() {
        @Override
        public int compare(OlisLabRequestSortable o1, OlisLabRequestSortable o2) {
            Date collectionDate1 = o1.getCollectionDateTime();
            Date collectionDate2 = o2.getCollectionDateTime();
            int compared = 0;
            
            // If both collection dates are not null, compares them using the Date objects compareTo
            // If Collection Date 1 is null and Collection Date 2 is not, sets compared to 1 to indicate that o1 should come later in the list than o2
            // If Collection Date 1 is not null, sets compared to -1 to indicate that o2 should come later in the list than o1
            if (collectionDate1 != null && collectionDate2 != null) {
                compared = o2.getCollectionDateTime().compareTo(o1.getCollectionDateTime());
            } else if (collectionDate1 == null && collectionDate2 != null) {
                compared = 1;
            } else if (collectionDate1 != null) {
                compared = -1;
            }

            // If the dates are the same (either they are both null or compareTo returned 0), continues comparing other elements to determine order
            if (compared == 0) {
                // Compares placer group numbers, continuing to compare other attributes if they are the same
                compared = OLISUtils.compareStringEmptyIsMore(o1.getGroupPlacerNo(), o2.getGroupPlacerNo());
                if (compared == 0) {
                    // Compares the ZBR11 sort key, continuing to compare other attributes if they are the same
                    compared = OLISUtils.compareStringEmptyIsMore(o1.getSortKey(), o2.getSortKey());
                    if (compared == 0) {
                        compared = OLISUtils.compareStringEmptyIsMore(o1.getNomenclature().getSortKey(), o2.getNomenclature().getSortKey());
                        if (compared == 0) {
                            // Compares the alternate names stored in the nomenclature, continuing to compare other attributes if they are the same
                            compared = OLISUtils.compareStringEmptyIsMore(o1.getNomenclature().getRequestAlternateName1(), o2.getNomenclature().getRequestAlternateName1());
                            if (compared == 0) {
                                // If the two set ids are not equal, compare them further, if they are, compared is 
                                // already set to 0
                                if (!o1.getSetId().equals(o2.getSetId())) {
                                    // If the first set id is empty, then it is considered higher than the second
                                    // If the second set id is empty, it is considered higher than the first 
                                    if (o1.getSetId().isEmpty()) {
                                        compared = 1;
                                    } else if (o2.getSetId().isEmpty()) {
                                        compared = -1;
                                    } else {
                                        // Parses both ids to integers
                                        Integer o1SetId = Integer.parseInt(o1.getSetId());
                                        Integer o2SetId = Integer.parseInt(o2.getSetId());
                                        // Compares the set ids to determine order
                                        compared = o1SetId.compareTo(o2SetId);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            return compared;
        }
    };
}
