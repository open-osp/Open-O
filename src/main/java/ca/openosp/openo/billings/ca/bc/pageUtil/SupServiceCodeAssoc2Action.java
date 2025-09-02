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

package ca.openosp.openo.billings.ca.bc.pageUtil;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import ca.openosp.openo.utility.SpringUtils;
import ca.openosp.openo.billings.ca.bc.data.SupServiceCodeAssocDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SupServiceCodeAssoc2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    public String execute() {
        SupServiceCodeAssocDAO dao = SpringUtils.getBean(SupServiceCodeAssocDAO.class);
        if (!SupServiceCodeAssocActionForm.MODE_VIEW.equals(this.getActionMode())) {
            if (validateForm()) {
                try {
                    response.sendRedirect("/billing/CA/BC/billingSVCTrayAssoc.jsp");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return NONE;
            } else {
                if (SupServiceCodeAssocActionForm.MODE_DELETE.equals(this.getActionMode())) {
                    dao.deleteServiceCodeAssociation(this.getId());
                } else if (SupServiceCodeAssocActionForm.MODE_EDIT.equals(this.getActionMode())) {
                    dao.saveOrUpdateServiceCodeAssociation(this.getPrimaryCode(),
                            this.getSecondaryCode());
                }
            }
        }

        request.setAttribute("list", dao.getServiceCodeAssociactions());
        return SUCCESS;
    }

    public boolean validateForm() {
        boolean test = true;
        BillingAssociationPersistence per = new BillingAssociationPersistence();
        if (SupServiceCodeAssocActionForm.MODE_EDIT.equals(this.actionMode)) {
            if (primaryCode == null || "".equals(primaryCode)) {
                test = false;
                addActionError(getText("oscar.billing.CA.BC.billingBC.error.nullservicecode", primaryCode));
            } else if (!per.serviceCodeExists(primaryCode)) {
                test = false;
                addActionError(getText("oscar.billing.CA.BC.billingBC.error.invalidsvccode", primaryCode));
            }
            if (secondaryCode == null || "".equals(secondaryCode)) {
                test = false;
                addActionError(getText("oscar.billing.CA.BC.billingBC.error.nullservicecode", secondaryCode));
            } else if (!per.serviceCodeExists(secondaryCode)) {
                test = false;
                addActionError(getText("oscar.billing.CA.BC.billingBC.error.invalidsvccode", secondaryCode));
            }
        }
        return test;
    }

    public static final String MODE_EDIT = "edit";
    public static final String MODE_DELETE = "delete";
    public static final String MODE_VIEW = "view";
    private String actionMode = MODE_VIEW;
    private String primaryCode;
    private String secondaryCode;
    private String id;

    public String getActionMode() {
        return actionMode;
    }

    public void setActionMode(String actionMode) {
        this.actionMode = actionMode;
    }

    public void setSecondaryCode(String secondaryCode) {
        this.secondaryCode = secondaryCode;
    }

    public void setPrimaryCode(String primaryCode) {
        this.primaryCode = primaryCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrimaryCode() {
        return primaryCode;
    }

    public String getSecondaryCode() {
        return secondaryCode;
    }

    public String getId() {
        return id;
    }

}
