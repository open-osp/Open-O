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

import java.util.Hashtable;

public class Tag {

    public int source;
    public int sources;
    public String language;
    public int languages;
    public String country;
    public int countries;
    public int author;
    public int authors;
    public String modified_after;
    boolean return_tags;
    /*
hashtable.put("classes", new Boolean(true));
hashtable.put("generics", new Boolean(true));
hashtable.put("branded", new Boolean(true));
hashtable.put("composites", new Boolean(true));
hashtable.put("return_tags", new Boolean(false));
     */

    public Tag() {
        // default
    }

    public Tag(Hashtable h) {
        source = getInt(h.get("source"));//,new Integer(0));
        sources = getInt(h.get("sources"));//,new Integer(sources));
        language = (String) h.get("language");//, "");
        languages = getInt(h.get("languages"));//, new Integer(languages));
        country = (String) h.get("country");//,"");
        countries = getInt(h.get("countries"));//;,new Integer(countries));
        author = getInt(h.get("author"));//, new Integer(0));
        authors = getInt(h.get("authors"));//, new Integer(authors));
        modified_after = (String) h.get("modified_after");//, new SimpleDateFormat("yyyy-MM-dd").parse(modified_after));
        //return_tags      =h.get("return_tags");//,Boolean.toString(return_tags));

    }

    int getInt(Object obj) {
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return -1;
        }
    }

    public int getSource() {
        return source;
    }

    public int getSources() {
        return sources;
    }

    public String getLanguage() {
        return language;
    }

    public int getLanguages() {
        return languages;
    }

    public String getCountry() {
        return country;
    }

    public int getCountries() {
        return countries;
    }

    public int getAuthor() {
        return author;
    }

    public int getAuthors() {
        return authors;
    }

    public String getModified_after() {
        return modified_after;
    }

    public boolean isReturn_tags() {
        return return_tags;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
