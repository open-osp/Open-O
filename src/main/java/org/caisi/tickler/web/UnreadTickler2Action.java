//CHECKSTYLE:OFF
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

package org.caisi.tickler.web;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.CustomFilter;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import oscar.login.LoginCheckLogin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;

public class UnreadTickler2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public String execute() throws Exception {
        if ("login".equals(request.getParameter("method"))) {
            return login();
        }
        if ("logout".equals(request.getParameter("method"))) {
            return logout();
        }
        String providerNo = (String) request.getSession().getAttribute("user");
        if (providerNo == null) {
            return "login";
        }
        return refresh();
    }

    public String refresh() {

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String providerNo = (String) request.getSession().getAttribute("user");
        if (providerNo == null) {
            return "login";
        }

        if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_tickler", "r", null)) {
            throw new SecurityException("missing required security object (_tickler)");
        }

        UserPropertyDAO propDao = (UserPropertyDAO) SpringUtils.getBean(UserPropertyDAO.class);
        UserProperty prop = propDao.getProp(providerNo, UserProperty.PROVIDER_FOR_TICKLER_WARNING);
        ProviderDao provDao = (ProviderDao) SpringUtils.getBean(ProviderDao.class);
        Provider prov = provDao.getProvider((prop != null) ? prop.getValue() : providerNo);
        request.setAttribute("tklerProviderName", prov.getFormattedName());

        int oldNum = -1;
        if (request.getSession().getAttribute("num_ticklers") != null) {
            oldNum = ((Integer) request.getSession().getAttribute("num_ticklers")).intValue();
        }
        CustomFilter filter = new CustomFilter();
        filter.getAssignees().add(new Provider(prov.getProviderNo()));
        String programId = "";
        Collection<Tickler> coll = ticklerManager.getTicklers(loggedInInfo, filter, prov.getProviderNo(), programId);
        if (oldNum != -1 && (coll.size() > oldNum)) {
            request.setAttribute("difference", new Integer(coll.size() - oldNum));
        }
        request.setAttribute("ticklers", coll);
        request.getSession().setAttribute("num_ticklers", new Integer(coll.size()));
        return "view";

    }

    public String login() {
        //LazyValidatorForm loginForm = (LazyValidatorForm) form;

        String ip = request.getRemoteAddr();

        String userName = username;
        //String password = (String) loginForm.get("password");
        //String pin = (String) loginForm.get("pin");

        if (userName == null || password == null || pin == null) {
            String errorMsg = "Need username/password/ping";
            request.setAttribute("errorMsg", errorMsg);
            return "login";
        }

        LoginCheckLogin cl = new LoginCheckLogin();
        if (cl.isBlock(ip, userName)) {
            String errorMsg = "Your account is locked. Please contact your administrator to unlock.";
            request.setAttribute("errormsg", errorMsg);
            return "login";
        }

        String[] strAuth;
        try {
            strAuth = cl.auth(userName, password, pin, ip);
        } catch (Exception e) {
            String errorMsg = "DB Connection error";
            request.setAttribute("errormsg", errorMsg);
            return "login";
        }

        if (strAuth == null || strAuth.length == 1) {
            String errorMsg = "Login Failed";
            request.setAttribute("errormsg", errorMsg);
            return "login";
        }

        //invalidate the existing sesson
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            session = request.getSession(); // Create a new session for this user
        }

        request.getSession().setAttribute("user", strAuth[0]);

        return refresh();
    }

    public String logout() {
        request.getSession().invalidate();
        return "login";
    }

    private String username;
    private String password;
    private String pin;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
