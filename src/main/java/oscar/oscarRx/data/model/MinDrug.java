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

package oscar.oscarRx.data.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.util.MiscUtils;

import java.util.Hashtable;

/**
 * Minimum Drug Data
 */
public class MinDrug {

    public String pKey;
    public String name;
    public String type;
    public Tag tag;

    MinDrug() {
        // default
    }

    public MinDrug(Hashtable h) {
        this.pKey = String.valueOf(h.get("id"));
        this.name = (String) h.get("name");
        //this.type = (String) h.get("category");//type
        this.type = ((Integer) h.get("category")).toString();
        MiscUtils.getLogger().debug("pkey " + pKey + " name " + name + " type " + type);
        //d.tag  = (Tag)    h.get("tag");
    }

    public String getpKey() {
        return pKey;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Tag getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
