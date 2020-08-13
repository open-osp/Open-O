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

import java.util.Comparator;

public class OlisLabChildResultSortable {
    int index;
    String status;
    String name;
    String sensitivity;
    int commentCount;
    String sortKey = "";
    String susceptibility;

    public OlisLabChildResultSortable() {
    }

    public OlisLabChildResultSortable(int index, String status, String name, String sensitivity, int commentCount, String sortKey, String susceptibility) {
        this.index = index;
        this.status = status;
        this.name = name;
        this.sensitivity = sensitivity;
        this.commentCount = commentCount;
        this.sortKey = sortKey;
        this.susceptibility = susceptibility;
    }

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSensitivity() {
        return sensitivity;
    }
    public void setSensitivity(String sensitivity) {
        this.sensitivity = sensitivity;
    }

    public int getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getSortKey() {
        return sortKey;
    }
    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getSusceptibility() {
        return susceptibility;
    }
    public void setSusceptibility(String susceptibility) {
        this.susceptibility = susceptibility;
    }

    public static final Comparator<OlisLabChildResultSortable> CHILD_RESULT_COMPARATOR = new Comparator<OlisLabChildResultSortable>() {
        @Override
        public int compare(OlisLabChildResultSortable o1, OlisLabChildResultSortable o2) {
            return o1.sortKey.compareTo(o2.sortKey);
        }
    };
}
