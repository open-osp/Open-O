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

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.eyeform.dao.EyeFormDao;
import org.oscarehr.eyeform.dao.EyeformFollowUpDao;
import org.oscarehr.eyeform.dao.EyeformProcedureBookDao;
import org.oscarehr.eyeform.dao.EyeformTestBookDao;
import org.oscarehr.eyeform.model.EyeForm;
import org.oscarehr.eyeform.model.EyeformFollowUp;
import org.oscarehr.eyeform.model.EyeformProcedureBook;
import org.oscarehr.eyeform.model.EyeformTestBook;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class NoteAddon2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    static Logger logger = org.oscarehr.util.MiscUtils.getLogger();

    private static ProviderDao providerDao = (ProviderDao) SpringUtils.getBean(ProviderDao.class);
    protected EyeformProcedureBookDao procedureBookDao = SpringUtils.getBean(EyeformProcedureBookDao.class);
    protected EyeformTestBookDao testDao = SpringUtils.getBean(EyeformTestBookDao.class);
    protected EyeFormDao eyeformDao = SpringUtils.getBean(EyeFormDao.class);
    protected TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);

    public String execute() {
        String method = request.getParameter("method");
        if ("getCurrentNoteData".equals(method)) {
            return getCurrentNoteData();
        } else if ("save".equals(method)) {
            return save();
        } else if ("getNoteText".equals(method)) {
            return getNoteText();
        } else if ("sendTickler".equals(method)) {
            return sendTickler();
        }
        return getCurrentNoteData();
    }


    public String getCurrentNoteData() {
        String demographicNo = request.getParameter("demographicNo");
        String noteId = request.getParameter("noteId");
        String appointmentNo = request.getParameter("appointmentNo");

        request.setAttribute("internalList", providerDao.getActiveProviders());

        //load up tests/procedures and extra info
        EyeformFollowUpDao followUpDao = SpringUtils.getBean(EyeformFollowUpDao.class);


        ProviderDao providerDao = (ProviderDao) SpringUtils.getBean(ProviderDao.class);
        DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean(DemographicDao.class);

        List<EyeformFollowUp> followUps = followUpDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
        for (EyeformFollowUp fu : followUps) {
            fu.setProvider(providerDao.getProvider(fu.getFollowupProvider()));
            fu.setDemographic(demographicDao.getDemographic(String.valueOf(fu.getDemographicNo())));
        }
        request.setAttribute("followUps", followUps);

        List<EyeformTestBook> testBookRecords = testDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
        request.setAttribute("testBookRecords", testBookRecords);

        List<EyeformProcedureBook> procedures = procedureBookDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
        request.setAttribute("procedures", procedures);

        EyeForm eyeform = eyeformDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
        request.setAttribute("eyeform", eyeform);


        return "current_note";
    }

    /*
     * followNo
     * followFrame
     * internalNo
     * ack1 - check
     * ack2
     * ack3
     *
     * procedure/test
     */

    public String save() {
        String appointmentNo = request.getParameter("appointmentNo");

        String ack1Checked = request.getParameter("ack1_checked");
        String ack2Checked = request.getParameter("ack2_checked");
        String ack3Checked = request.getParameter("ack3_checked");


        EyeForm eyeform = eyeformDao.getByAppointmentNo(Integer.parseInt(appointmentNo));

        if (eyeform == null) {
            eyeform = new EyeForm();
            eyeform.setDate(new Date());
            eyeform.setAppointmentNo(Integer.parseInt(appointmentNo));
        }

        eyeform.setDischarge(ack1Checked);
        eyeform.setStat(ack2Checked);
        eyeform.setOpt(ack3Checked);

        eyeformDao.save(eyeform);

        return null;
    }

    public String getNoteText() {
        String appointmentNo = request.getParameter("appointmentNo");


        EyeForm eyeform = eyeformDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
        StringBuilder sb = new StringBuilder();

        if (eyeform != null) {
            if (eyeform.getDischarge() != null && eyeform.getDischarge().equals("true")) {
                sb.append("Patient is discharged from my active care.");
                sb.append("\n");
            }

            if (eyeform.getStat() != null && eyeform.getStat().equals("true")) {
                sb.append("Follow up as needed with me STAT or PRN if symptoms are worse.");
                sb.append("\n");
            }

            if (eyeform.getOpt() != null && eyeform.getOpt().equals("true")) {
                sb.append("Routine eye care by an optometrist is recommended.");
                sb.append("\n");
            }

        }

        try {
            response.getWriter().print(sb.toString());
        } catch (IOException e) {
            logger.error(e);
        }
        return null;
    }

    /*
     * appointmentNo
     * test
     * recip
     */

    public String sendTickler() {
        String aptNo = request.getParameter("appointmentNo");
        String text = request.getParameter("text");
        String recipient = request.getParameter("recip");
        String demographicNo = request.getParameter("demographicNo");

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        Tickler t = new Tickler();
        t.setCreator(loggedInInfo.getLoggedInProviderNo());
        t.setAssignee(providerDao.getProvider(recipient));
        t.setDemographicNo(Integer.parseInt(demographicNo));
        t.setMessage(text);

        t.setProvider(loggedInInfo.getLoggedInProvider());
        t.setProgramId(Integer.valueOf((String) request.getSession().getAttribute("programId_oscarView")));

        t.setTaskAssignedTo(recipient);

        ticklerManager.addTickler(loggedInInfo, t);


        return null;
    }

}
