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


package org.oscarehr.eyeform.web;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.oscarehr.eyeform.dao.EyeformProcedureBookDao;
import org.oscarehr.eyeform.model.EyeformProcedureBook;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProcedureBook2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    static Logger logger = org.oscarehr.util.MiscUtils.getLogger();
    protected static EyeformProcedureBookDao procedureBookDao = SpringUtils.getBean(EyeformProcedureBookDao.class);

    public String execute() {
        String method = request.getParameter("method");
        if ("cancel".equals(method)) {
            return cancel();
        } else if ("getNoteText".equals(method)) {
            return getNoteText();
        } else if ("save".equals(method)) {
            return save();
        } else if ("getTicklerText".equals(method)) {
            return getTicklerText();
        }
        return form();
    }

    public String cancel() {
        return form();
    }

    public String form() {
        if (data.getId() != null && data.getId().intValue() > 0) {
            data = procedureBookDao.find(data.getId());
        }
        this.setData(data);
        return "form";
    }

    public String save() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        if (data.getId() != null && data.getId() == 0) {
            data.setId(null);
        }
        data.setProvider(loggedInInfo.getLoggedInProviderNo());
        procedureBookDao.save(data);

        return SUCCESS;
    }

    public String getNoteText() {
        String appointmentNo = request.getParameter("appointmentNo");

        List<EyeformProcedureBook> procedures = procedureBookDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
        StringBuilder sb = new StringBuilder();

        for (EyeformProcedureBook f : procedures) {
            sb.append("book procedure: ").append(f.getProcedureName()).append(" ").append(f.getEye()).append(" ").append(f.getLocation());
            sb.append(" ").append(f.getComment());
            sb.append("\n");
        }

        try {
            response.getWriter().print(sb.toString());
        } catch (IOException e) {
            logger.error(e);
        }

        return null;
    }

    public String getTicklerText() {
        String appointmentNo = request.getParameter("appointmentNo");

        String text = getTicklerText(Integer.parseInt(appointmentNo));

        try {
            response.getWriter().print(text);
        } catch (IOException e) {
            logger.error(e);
        }

        return null;
    }

    public static String getTicklerText(int appointmentNo) {

        List<EyeformProcedureBook> procedures = procedureBookDao.getByAppointmentNo(appointmentNo);
        StringBuilder sb = new StringBuilder();

        for (EyeformProcedureBook f : procedures) {
            String style = new String();
            if (f.getUrgency().equals("URGENT") || f.getUrgency().equals("ASAP")) {
                style = "style=\"color:red;\"";
            }
            sb.append("<span " + style + " title=\"" + f.getComment() + "\">proc:").append(f.getProcedureName()).append(" ").append(f.getEye()).append(" ").append(f.getLocation()).append(" ").append(f.getUrgency()).append(" ").append(f.getComment()).append("</span>");
            sb.append("<br/>");
        }
        return sb.toString();
    }

    private EyeformProcedureBook data;

    public EyeformProcedureBook getData() {
        return data;
    }

    public void setData(EyeformProcedureBook data) {
        this.data = data;
    }
}
