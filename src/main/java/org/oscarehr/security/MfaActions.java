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

package org.oscarehr.security;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.model.Security;
import org.oscarehr.managers.MfaManager;
import org.oscarehr.managers.SecurityManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class MfaActions extends DispatchAction {

    public static final String METHOD_RESET_MFA = "resetMfa";

    private final SecurityManager securityManager = SpringUtils.getBean(SecurityManager.class);
    private final MfaManager mfaManager = SpringUtils.getBean(MfaManager.class);

    public ActionForward resetMfa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String securityId = request.getParameter("securityId");
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        Security security = this.securityManager.find(loggedInInfo, Integer.valueOf(securityId));
        this.mfaManager.resetMfaSecret(loggedInInfo, security);
        return null;
    }
}
