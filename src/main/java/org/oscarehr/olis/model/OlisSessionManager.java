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

import org.oscarehr.util.LoggedInInfo;

import java.util.HashMap;
import java.util.Map;

public class OlisSessionManager {

    /**
     * Tracks the olis sessions for each provider, any new queries a provider runs only impacts their own results
     */
    public static Map<String, ProviderOlisSession> providerSessionMap = new HashMap<String, ProviderOlisSession>();

    /**
     * Gets the provided olis session for the given logged in provider
     * @param sessionOwner the LoggedInInfo of the provider session
     * @return The ProviderOlisSession for the given provider
     */
    public static ProviderOlisSession getSession(LoggedInInfo sessionOwner) {
        if (providerSessionMap.keySet().contains(sessionOwner.getLoggedInProviderNo())) {
            // Get the existing session for this provider
            return providerSessionMap.get(sessionOwner.getLoggedInProviderNo());
        } else {
            // Create new session and return
            return newSession(sessionOwner);
        }
    }

    /**
     * Creates a new ProviderOlisSession for the provided sessionOwner and stores it in the providerSessionMap
     * @param sessionOwner The owner of the new olis session
     * @return the new olis session
     */
    private static ProviderOlisSession newSession(LoggedInInfo sessionOwner) {
        if (providerSessionMap.keySet().contains(sessionOwner.getLoggedInProviderNo())) {
            throw new IllegalArgumentException("Provided ProviderNo already exists in the OLIS session");
        }
        ProviderOlisSession newSession = new ProviderOlisSession(sessionOwner);
        providerSessionMap.put(sessionOwner.getLoggedInProviderNo(), newSession);
        return newSession;
    }
}
