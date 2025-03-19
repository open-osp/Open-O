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

public class Interaction implements Comparable {
    public int compareTo(Object obj) {
        int retval = 0;
        int compVal = 0;
        int thisVal = 0;
        Interaction t = (Interaction) obj;
        String sig = t.significance;
        try {
            compVal = Integer.parseInt(sig);
        } catch (Exception e1) {
            retval = -1;
        }
        try {
            thisVal = Integer.parseInt(significance);
        } catch (Exception e2) {
            retval = 1;
        }

        if (retval == 0) {
            if (thisVal < compVal) {
                retval = 1;
            } else if (thisVal > compVal) {
                retval = -1;
            }

        }
        // If this < obj, return a negative value
        // If this = obj, return 0
        // If this > obj, return a positive value
        return retval;
    }

    public String significance = null;
    public String affectingatc = null;
    public String affectingdrug = null;
    public String evidence = null;
    public String effect = null;
    public String affecteddrug = null;
    public String affectedatc = null;
    public String comment = null;
}
