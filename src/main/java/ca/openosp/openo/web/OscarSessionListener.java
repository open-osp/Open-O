//CHECKSTYLE:OFF
/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package ca.openosp.openo.web;

import ca.openosp.openo.commn.dao.CasemgmtNoteLockDao;
import ca.openosp.openo.commn.exception.UserSessionNotFoundException;
import ca.openosp.openo.commn.model.CasemgmtNoteLock;
import ca.openosp.openo.managers.UserSessionManager;
import ca.openosp.openo.managers.UserSessionManagerImpl;
import ca.openosp.openo.utility.MiscUtils;
import ca.openosp.openo.utility.SpringUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class OscarSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        MiscUtils.getLogger().info("Creating new OSCAR session.");
        MiscUtils.getLogger().info("Session id: " + se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String id = se.getSession().getId();
        MiscUtils.getLogger().info("session is being destroyed - " + id);

        CasemgmtNoteLockDao casemgmtNoteLockDao = SpringUtils.getBean(CasemgmtNoteLockDao.class);

        for (CasemgmtNoteLock lock : casemgmtNoteLockDao.findBySession(id)) {
            MiscUtils.getLogger().info("removing note locks for this session - " + lock);

            casemgmtNoteLockDao.remove(lock.getId());
        }

		HttpSession session = se.getSession();
		Integer userSecurityCode = (Integer) session.getAttribute(UserSessionManagerImpl.KEY_USER_SECURITY_CODE);
		if (userSecurityCode != null) {
			try {
				UserSessionManager userSessionManager = SpringUtils.getBean(UserSessionManager.class);
				userSessionManager.unregisterUserSession(userSecurityCode);
			} catch (UserSessionNotFoundException e) {
				MiscUtils.getLogger().warn("Failed to unregister session on destroy: {}", e.getMessage());
			}
		}
    }

}
