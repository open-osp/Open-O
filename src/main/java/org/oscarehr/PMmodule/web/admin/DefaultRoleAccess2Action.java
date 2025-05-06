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

package org.oscarehr.PMmodule.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.utility.RoleCache;
import org.oscarehr.util.SpringUtils;

import com.quatro.service.security.RolesManager;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class DefaultRoleAccess2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private ProgramManager programManager = SpringUtils.getBean(ProgramManager.class);
    private RolesManager roleManager = SpringUtils.getBean(RolesManager.class);

    public String execute() {
        if ("save".equals(request.getParameter("method"))) {
            return save();
        }
        if ("edit".equals(request.getParameter("method"))) {
            return edit();
        }
        if ("delete".equals(request.getParameter("method"))) {
            return delete();
        }
        return list();
    }

    public String list() {
        //DynaActionForm accessForm = (DynaActionForm)form;
        request.setAttribute("default_roles", programManager.getDefaultRoleAccesses());
        return "list";
    }

    public String edit() {
        DefaultRoleAccess dra = null;

        String id = request.getParameter("id");

        if (id != null) {
            dra = programManager.getDefaultRoleAccess(id);
            if (dra != null) {
                this.setForm(dra);
            }
        }
        if (dra == null) {
            dra = new DefaultRoleAccess();
        }

        this.setForm(dra);
        request.setAttribute("roles", roleManager.getRoles());
        request.setAttribute("access_types", programManager.getAccessTypes());

        RoleCache.reload();
        return "form";
    }

    public String save() {
        DefaultRoleAccess dra = this.getForm();

        if (dra.getId().longValue() == 0) {
            dra.setId(null);
        }

        if (programManager.findDefaultRoleAccess(dra.getRoleId(), dra.getAccessTypeId()) == null) {
            programManager.saveDefaultRoleAccess(dra);
        }
        this.addMessage(request, "message", "Saved Access");

        RoleCache.reload();

        return "rlist";
    }

    public String delete() {
        String id = request.getParameter("id");

        if (id != null) {
            programManager.deleteDefaultRoleAccess(id);
        }

        this.addMessage(request, "message", "Removed Access");

        RoleCache.reload();

        return "rlist";
    }

    public void setProgramManager(ProgramManager mgr) {
        this.programManager = mgr;
    }

    public void setRolesManager(RolesManager mgr) {
        this.roleManager = mgr;
    }

    private void addMessage(HttpServletRequest req, String key, String val) {
        addActionMessage(getText(key, val));
    }

    private DefaultRoleAccess form;

    public DefaultRoleAccess getForm() {
        return form;
    }

    public void setForm(DefaultRoleAccess form) {
        this.form = form;
    }
}
