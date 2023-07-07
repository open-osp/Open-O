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

package oscar.login;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import oscar.log.LogAction;
import oscar.log.LogConst;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutAction extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return logout(mapping, form, request, response);
	}

	public ActionForward ssoLogout(ActionMapping mapping, ActionForm form,
	                            HttpServletRequest request, HttpServletResponse response) {
		logout(mapping, form, request, response);
		return mapping.findForward("logoutsso");
	}

	public ActionForward sessionTimeout(ActionMapping mapping, ActionForm form,
	                            HttpServletRequest request, HttpServletResponse response) {
		return logout(mapping, form, request, response);
	}

	public ActionForward failure(ActionMapping mapping, ActionForm form,
	                            HttpServletRequest request, HttpServletResponse response) {
		return logout(mapping, form, request, response);
	}

	public ActionForward logout(ActionMapping mapping, ActionForm form,
	                            HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(false);
		String login = request.getParameter("login");
		String errorMessage = request.getParameter("errorMessage");
		String nameId = request.getParameter("nameId");

		if (session != null) {
			String user = (String) session.getAttribute("user");
			session.invalidate();
			if (user != null) {
				LogAction.addLog(user, LogConst.LOGOUT, LogConst.CON_LOGIN, "", request.getRemoteAddr());
			}
		}

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}

		ActionRedirect redirect = new ActionRedirect(mapping.findForward("success"));
		redirect.addParameter("login",login);
		redirect.addParameter("errorMessage",errorMessage);
		redirect.addParameter("nameId",nameId);

		return mapping.findForward("success");
	}
}
