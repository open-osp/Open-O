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


package ca.openosp.openo.messenger.pageUtil;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MsgWriteToEncounter2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();



    public String execute() throws IOException, ServletException {
        GregorianCalendar now = new GregorianCalendar();
        int curYear = now.get(Calendar.YEAR);
        int curMonth = (now.get(Calendar.MONTH) + 1);
        int curDay = now.get(Calendar.DAY_OF_MONTH);
        String dateString = curYear + "-" + curMonth + "-" + curDay;
        String provider = (String) request.getSession().getAttribute("user");


        //../oscarEncounter/IncomingEncounter.do?providerNo=<%=curProvider_no%>&appointmentNo=&demographicNo=<%=demographic_no%>&curProviderNo=&reason=<%=URLEncoder.encode("Tel-Progress Notes")%>&userName=<%=URLEncoder.encode( userfirstname+" "+userlastname) %>&curDate=<%=""+curYear%>-<%=""+curMonth%>-<%=""+curDay%>&appointmentDate=&startTime=&status=
        StringBuilder forward = new StringBuilder("/oscarEncounter/IncomingEncounter.do");
        forward.append("providerNo=").append(provider);
        forward.append("appointmentNo=").append("");
        forward.append("demographicNo=").append(request.getParameter("demographic_no"));
        forward.append("curProviderNo=").append(provider);
        forward.append("reason=").append("oscarMessenger");
        forward.append("userName=").append(request.getSession().getAttribute("userfirstname") + " " + request.getSession().getAttribute("userlastname"));
        forward.append("curDate=").append(dateString);
        forward.append("appointmentDate=").append("");
        forward.append("startTime=").append("");
        forward.append("status=").append("");
        forward.append("msgId=").append(request.getParameter("msgId"));
        String encType = request.getParameter("encType");
        if (encType != null)
            forward.append("encType").append(encType);
        response.sendRedirect(forward.toString());
        return NONE;
    }
}
