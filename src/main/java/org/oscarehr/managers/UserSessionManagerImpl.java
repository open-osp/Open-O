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

import org.apache.logging.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;

/**
 * Implementation of the {@link UserSessionManager} interface.
 * This class manages user sessions. It supports multiple concurrent sessions per user and can
 * either invalidate existing sessions when registering a new one or keep them based on a policy flag.
 */
@Service
public class UserSessionManagerImpl implements UserSessionManager {

    public static final String KEY_USER_SECURITY_CODE = "UserSecurityCode";
    private static final Logger logger = MiscUtils.getLogger();
    private static final ConcurrentHashMap<Integer, Set<HttpSession>> userSessions = new ConcurrentHashMap<>();

    @Override
    public void registerUserSession(Integer userSecurityCode, HttpSession session, boolean invalidateExisting) {
        Objects.requireNonNull(session, "session must not be null");
        Set<HttpSession> sessions = userSessions.computeIfAbsent(userSecurityCode, k -> ConcurrentHashMap.newKeySet());

        if (invalidateExisting) {
            for (HttpSession s : sessions) {
                try {
                    logger.debug("Invalidating existing session for user {}: {}", userSecurityCode, s.getId());
                    s.invalidate();
                } catch (IllegalStateException e) {
                    // session already invalidated; ignore
                    logger.debug("Tried to invalidate an already invalidated session: {}", e.getMessage());
                }
            }
            sessions.clear();
        }

        session.setAttribute(KEY_USER_SECURITY_CODE, userSecurityCode);
        sessions.add(session);
        logger.debug("User Session successfully registered for user {}: {} (invalidateExisting={})", userSecurityCode, session.getId(), invalidateExisting);
    }

    @Override
    public void unregisterUserSession(Integer userSecurityCode, HttpSession session) {
        if (session == null) return;
        Set<HttpSession> sessions = userSessions.get(userSecurityCode);
        if (sessions == null) return;
        if (sessions.remove(session)) {
            try {
                session.removeAttribute(KEY_USER_SECURITY_CODE);
            } catch (IllegalStateException ignored) {
            }
            if (sessions.isEmpty()) userSessions.remove(userSecurityCode);
            logger.debug("Unregistered specific session for user {}: {}", userSecurityCode, session.getId());
        }
    }

    @Override
    public Collection<HttpSession> getRegisteredSessions(Integer userSecurityCode) {
        Set<HttpSession> sessions = userSessions.get(userSecurityCode);
        if (sessions == null) return Collections.emptyList();
        return Collections.unmodifiableSet(sessions);
    }

    /**
     * Checks if a session is registered for the given user security code.
     *
     * @param userSecurityCode The user security code.
     * @return True if the session is registered, false otherwise.
     */
    private boolean isUserSessionRegistered(Integer userSecurityCode) {
        Set<HttpSession> sessions = userSessions.get(userSecurityCode);
        return sessions != null && !sessions.isEmpty();
    }
}
