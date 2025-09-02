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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.openosp.openo.commn.model.MessageTbl;
import ca.openosp.openo.commn.model.MsgDemoMap;
import ca.openosp.openo.managers.MessagingManager;
import ca.openosp.openo.managers.MessengerDemographicManager;
import ca.openosp.openo.managers.SecurityInfoManager;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.SpringUtils;


import ca.openosp.openo.messenger.data.ContactIdentifier;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class MsgHandleMessages2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    private MessagingManager messagingManager = SpringUtils.getBean(MessagingManager.class);
    private MessengerDemographicManager messengerDemographicManager = SpringUtils.getBean(MessengerDemographicManager.class);

    public String execute() throws IOException, ServletException {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_msg", "r", null)) {
            throw new SecurityException("missing required sec object (_msg)");
        }

        //String messageNo = frm.getMessageNo();
        String demographicNo = this.getDemographic_no();

        //String reply = frm.getReply();
        //String replyAll = frm.getReplyAll();
        //String delete = frm.getDelete();
        //String forward = frm.getForward();
        String unlinkedIntegratorDemographicName = request.getParameter("unlinkedIntegratorDemographicName");

        /*
         * Set Demographic_no attribute if there's any
         * Check the current demographic map and replace the incoming demographicNo attribute
         */
        List<MsgDemoMap> msgDemoMap = messengerDemographicManager.getAttachedDemographicList(loggedInInfo, Integer.parseInt(messageNo));
        if (msgDemoMap != null && msgDemoMap.size() > 0) {
            demographicNo = msgDemoMap.get(0).getDemographic_no() + "";
        }

        /*
         * Check the unlinked demographic table and reuse
         */
        else if (unlinkedIntegratorDemographicName != null && !unlinkedIntegratorDemographicName.isEmpty()) {
            request.setAttribute("unlinkedIntegratorDemographicName", unlinkedIntegratorDemographicName);
            demographicNo = null;
        }

        /*
         * The final demographic number being sent to the page.
         */
        if (demographicNo != null) {
            request.setAttribute("demographic_no", demographicNo);
        }

        java.util.Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String param = ((String) enumeration.nextElement());
            if (param.equals("delete")) {
                delete = "Delete";
            } else if (param.equals("reply")) {
                reply = "Reply";
            } else if (param.equals("replyAll")) {
                replyAll = "reply All";
            } else if (param.equals("forward")) {
                forward = "Forward";
            }
        }

        if (delete.compareToIgnoreCase("Delete") == 0) {

            messagingManager.deleteMessage(loggedInInfo, Integer.parseInt(messageNo));

        } else if (reply.equalsIgnoreCase("Reply") || (replyAll.equalsIgnoreCase("reply All"))) {

            StringBuilder theSendMessage = new StringBuilder();
            MessageTbl message = messagingManager.getMessage(loggedInInfo, Integer.parseInt(messageNo));
            String themessage = message.getMessage();
            themessage = themessage.replaceAll("\n", "\n>");//puts > at the beginning of each line

            // stamp the original message
            theSendMessage = new StringBuilder(String.format("\n\n\n>On %1$td-%1$tm-%1$tY, at %2$s, %3$s wrote: \n", message.getDate(), message.getTime(), message.getSentBy()));
            theSendMessage.append(">" + themessage);

            StringBuilder subject = new StringBuilder("");

            if (!message.getSubject().startsWith("Re:")) {
                subject.append("Re:");
            }

            subject.append(message.getSubject());

            List<ContactIdentifier> replyList = Collections.emptyList();

            if ("Reply".equalsIgnoreCase(reply)) {
                replyList = messagingManager.getReplyToSender(loggedInInfo, message);
            } else if ("reply All".equalsIgnoreCase(replyAll)) {
                replyList = messagingManager.getAllMessageReplyRecipients(loggedInInfo, message);
            }

//			String replyListString = new Gson().toJson(replyList);
//		
            request.setAttribute("ReText", theSendMessage.toString());
            request.setAttribute("replyList", replyList); // used to set the providers that will get the reply message
            request.setAttribute("ReSubject", subject.toString());

            return "reply";

        } else if (forward.equals("Forward")) {

            StringBuilder subject = new StringBuilder("Fwd:");
            String themessage = new String();
            StringBuilder theSendMessage = new StringBuilder();

            MessageTbl forwardMessage = messagingManager.getMessage(loggedInInfo, Integer.parseInt(messageNo));

            subject.append(forwardMessage.getSubject());
            themessage = forwardMessage.getMessage();
            themessage = themessage.replace('\n', '>'); //puts > at the beginning
            theSendMessage = new StringBuilder(themessage); //of each line
            theSendMessage.insert(0, "\n\n\n>");

            request.setAttribute("ReText", theSendMessage.toString()); //this one is a goody
            request.setAttribute("ReSubject", subject.toString());
            return "reply";
        }

        return SUCCESS;
    }


    String reply;
    String replyAll;
    String delete;
    String forward;
    String messageNo;
    String demographic_no;

    public String getForward() {
        if (this.forward == null) {
            this.forward = new String();
        }
        return this.forward;
    }

    public void setForward(String str) {
        this.forward = str;
    }

    public void setMessageNo(String messageNo) {
        this.messageNo = messageNo;
    }

    public String getMessageNo() {
        return messageNo != null ? messageNo : "";
    }

    public void setReply(String reply) {
        this.reply = reply;
        this.replyAll = "";
        this.delete = "";
    }

    public String getReply() {
        return reply != null ? reply : "";
    }

    public void setReplyAll(String replyAll) {
        this.replyAll = replyAll;
        this.reply = "";
        this.delete = "";
    }

    public String getReplyAll() {
        return replyAll != null ? replyAll : "";
    }

    public void setDelete(String delete) {
        this.delete = delete;
        this.reply = "";
        this.replyAll = "";
    }

    public String getDelete() {
        return delete != null ? delete : "";
    }

    public String getDemographic_no() {
        return demographic_no;
    }

    public void setDemographic_no(String demographic_no) {
        this.demographic_no = demographic_no;
    }

}
