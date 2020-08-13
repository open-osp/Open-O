
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

package oscar.util;

import java.net.URISyntaxException;

public class ResourceUtils {
    public enum Font {
        COURIER_PRIME("Courier Prime.ttf"),
        COURIER_PRIME_BOLD("Courier Prime Bold.ttf");
        
        private final String FONT_PATH = "oscar/fonts/";
        private String fileName;
        
        Font(String fileName) {
            this.fileName = fileName;
        }

        /**
         * Gets the path for the font, returning the full system path to the resource 
         * allowing it to be accessed
         * 
         * @return String containing the full system resource path
         * @throws URISyntaxException Thrown when the path provided is incorrect
         */
        public String getPath() throws URISyntaxException {
            return ResourceUtils.getResourcePath(FONT_PATH + fileName);
        }
    }

    /**
     * Gets the path for a desired resourse using the provided resource folder path.
     * This will find the resource and return the full system path, allowing access to the file
     * 
     * @param path the path in the resource folder to get the full system path for
     * @return String containing the full system path to the desired resource
     * @throws URISyntaxException Thrown when the path provided is incorrect
     */
    public static String getResourcePath(String path) throws URISyntaxException {
        return ResourceUtils.class.getClassLoader().getResource(path).toURI().getPath();
    }
}
