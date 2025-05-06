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
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.eyeform.dao.EyeformFollowUpDao;
import org.oscarehr.eyeform.model.EyeformFollowUp;
import org.oscarehr.util.SpringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FollowUp2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    static Logger logger = org.oscarehr.util.MiscUtils.getLogger();
    static EyeformFollowUpDao dao = (EyeformFollowUpDao) SpringUtils.getBean(EyeformFollowUpDao.class);

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
        ProviderDao providerDao = (ProviderDao) SpringUtils.getBean(ProviderDao.class);
        request.setAttribute("providers", providerDao.getActiveProviders());


        return "form";
    }

    public String save() {
        EyeformFollowUp data = followup;
        if (data.getId() != null && data.getId() == 0) {
            data.setId(null);
        }

        dao.save(data);


        return SUCCESS;
    }

    public String getNoteText() {
        String appointmentNo = request.getParameter("appointmentNo");

        ProviderDao providerDao = (ProviderDao) SpringUtils.getBean(ProviderDao.class);


        List<EyeformFollowUp> followUps = dao.getByAppointmentNo(Integer.parseInt(appointmentNo));
        StringBuilder sb = new StringBuilder();

        for (EyeformFollowUp f : followUps) {
            Provider p = providerDao.getProvider(f.getFollowupProvider());
            sb.append(f.getType());
            if (f.getTimespan() > 0) {
                sb.append(" ").append(f.getTimespan()).append(" ").append(f.getTimeframe());
            }
            sb.append(" Dr. ").append(p.getFormattedName()).append(" ").append(f.getUrgency());
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
        ProviderDao providerDao = (ProviderDao) SpringUtils.getBean(ProviderDao.class);

        List<EyeformFollowUp> followUps = dao.getByAppointmentNo(appointmentNo);
        StringBuilder sb = new StringBuilder();

        for (EyeformFollowUp f : followUps) {
            Provider p = providerDao.getProvider(f.getFollowupProvider());
            String type = "f/u:";
            if (f.getType().equals("consult")) {
                type = "consult:";
            }
            sb.append(type);
            if (f.getTimespan() > 0) {
                sb.append(" ").append(f.getTimespan()).append(" ").append(f.getTimeframe());
            }
            sb.append(" Dr. ").append(p.getFormattedName());
            sb.append(" ").append(f.getComment());
            sb.append("<br/>");
        }
        return sb.toString();
    }

    private EyeformFollowUp followup;

    public EyeformFollowUp getFollowup() {
        return followup;
    }

    public void setFollowup(EyeformFollowUp followup) {
        this.followup = followup;
    }
}
