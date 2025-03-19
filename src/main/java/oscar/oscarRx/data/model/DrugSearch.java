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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Assign DrugSearch data.
 */
public class DrugSearch {

    ArrayList brand = null;
    ArrayList gen = null;
    ArrayList afhcClass = null;
    int totalSearch;
    boolean empty = false;
    public boolean failed = false;
    public String errorMessage = null;

    public DrugSearch() {
        brand = new ArrayList();
        gen = new ArrayList();
        afhcClass = new ArrayList();
    }

    public void processResult(Vector vec) {
        for (int i = 0; i < vec.size(); i++) {
            Hashtable h = (Hashtable) vec.get(i);
            if (!h.get("name").equals("None found")) {
                MinDrug d = new MinDrug(h);

                if (d.type.equals("13")) {
                    brand.add(d);
                } else if (d.type.equals("11") || d.type.equals("12")) {
                    gen.add(d);
                } else if (d.type.equals("8") || d.type.equals("10")) {
                    afhcClass.add(d);
                }
            } else {
                this.setEmpty(true);
            }
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean b) {
        empty = b;
    }

    public ArrayList getBrand() {
        return brand;
    }

    public void setBrand(ArrayList brand) {
        this.brand = brand;
    }

    public ArrayList getGen() {
        return gen;
    }

    public void setGen(ArrayList gen) {
        this.gen = gen;
    }

    public ArrayList getAfhcClass() {
        return afhcClass;
    }

    public void setAfhcClass(ArrayList afhcClass) {
        this.afhcClass = afhcClass;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
