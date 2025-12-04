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


package org.oscarehr.managers;

import javax.servlet.http.HttpSession;
import java.util.Collection;

/**
 * Manages user sessions.  Provides methods to register, unregister, and retrieve user sessions based on a user security code.
 * This interface is implemented by a service class that handles the actual session management logic.
 *
 */
public interface UserSessionManager {

    /**
     * Registers a user session with a policy to either invalidate existing sessions or keep them.
     *
     * @param userSecurityCode the user's security code
     * @param session the HTTP session
     * @param invalidateExisting if true, invalidate all existing sessions for the user before registering the new one; if false, keep existing sessions and add the new one
     */
    void registerUserSession(Integer userSecurityCode, HttpSession session, boolean invalidateExisting);

    /**
     * Unregister a specific session for a user. If the session or user is not present, this is a no-op.
     * @param userSecurityCode the user's security code
     * @param session the HTTP session to unregister
     */
    void unregisterUserSession(Integer userSecurityCode, HttpSession session);

    /**
     * Returns all registered sessions for a user. Returns an empty collection if none are present.
     * @param userSecurityCode the user's security code
     * @return collection of sessions
     */
    Collection<HttpSession> getRegisteredSessions(Integer userSecurityCode);

}
