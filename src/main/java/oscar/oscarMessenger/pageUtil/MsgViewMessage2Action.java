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


package oscar.oscarMessenger.pageUtil;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.DemographicTransfer;
import org.oscarehr.common.model.OscarMsgType;
import org.oscarehr.managers.MessagingManager;
import org.oscarehr.managers.MessengerDemographicManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.oscarMessenger.data.MsgDisplayMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgViewMessage2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    private MessagingManager messagingManager = SpringUtils.getBean(MessagingManager.class);
    private MessengerDemographicManager messengerDemographicManager = SpringUtils.getBean(MessengerDemographicManager.class);

    public String execute() throws IOException, ServletException {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_msg", SecurityInfoManager.READ, null)) {
            throw new SecurityException("missing required security object (_msg)");
        }

        oscar.oscarMessenger.pageUtil.MsgSessionBean bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean) request.getSession().getAttribute("msgSessionBean");
        String providerNo = LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo();
        if (bean != null) {
            providerNo = bean.getProviderNo();
        } else {
            MiscUtils.getLogger().debug("MsgSessionBean is null");
        }

        String messageNo = request.getParameter("messageID");
        String messagePosition = request.getParameter("messagePosition");
        String linkMsgDemo = request.getParameter("linkMsgDemo");
        String demographic_no = request.getParameter("demographic_no");
        String orderBy = request.getParameter("orderBy");
        // String msgCount = request.getParameter("msgCount");
        String from = request.getParameter("from") == null ? "oscarMessenger" : request.getParameter("from");
        String boxType = request.getParameter("boxType") == null ? "" : request.getParameter("boxType");

        MsgDisplayMessage msgDisplayMessage = messagingManager.getInboxMessage(loggedInInfo, Integer.parseInt(messageNo));

        if (msgDisplayMessage != null) {
            int messageType = msgDisplayMessage.getType();
            String msgType_link = msgDisplayMessage.getTypeLink();
            Map<Integer, String> attachedDemographics = messengerDemographicManager.getAttachedDemographicNameMap(loggedInInfo, Integer.parseInt(msgDisplayMessage.getMessageId()));

            // check if there are any demographic files sent over from other integrated facilities.
            // skip this step if the demographic has already been imported (importedDemographic).
            if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
                // usually only one demographic transfer at a time.
                List<DemographicTransfer> unlinkedDemographics = messengerDemographicManager.getUnlinkedIntegratedDemographics(loggedInInfo, Integer.parseInt(messageNo));

                CachedFacility remoteFacility = null;

                if (unlinkedDemographics != null && unlinkedDemographics.size() > 0) {
                    remoteFacility = CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(), unlinkedDemographics.get(0).getIntegratorFacilityId());
                }

                if (remoteFacility != null) {
                    request.getSession().setAttribute("demographicLocation", remoteFacility.getName());
                    request.getSession().setAttribute("unlinkedDemographics", unlinkedDemographics);
                }

            }

            request.getSession().setAttribute("attachedDemographics", attachedDemographics);
            request.getSession().setAttribute("viewMessageMessage", msgDisplayMessage.getMessageBody());
            request.getSession().setAttribute("viewMessageSubject", msgDisplayMessage.getThesubject());
            request.getSession().setAttribute("viewMessageSentby", msgDisplayMessage.getSentby());
            request.getSession().setAttribute("viewMessageSentto", msgDisplayMessage.getSentto());
            request.getSession().setAttribute("viewMessageTime", msgDisplayMessage.getThetime());
            request.getSession().setAttribute("viewMessageDate", msgDisplayMessage.getThedate());
            request.getSession().setAttribute("viewMessageAttach", msgDisplayMessage.getAttach());
            request.getSession().setAttribute("viewMessagePDFAttach", msgDisplayMessage.getPdfAttach());
            request.getSession().setAttribute("viewMessageId", messageNo);
            request.getSession().setAttribute("viewMessageNo", messageNo);
            request.getSession().setAttribute("viewMessagePosition", messagePosition);
            request.getSession().setAttribute("from", from);
            request.getSession().setAttribute("providerNo", providerNo);
            if (orderBy != null) {
                request.getSession().setAttribute("orderBy", orderBy);
            }

            if (messageType > 0) {
                request.getSession().setAttribute("msgType", messageType + "");
            }

            if (messageType == OscarMsgType.OSCAR_REVIEW_TYPE && msgType_link != null) {

                HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
                String[] keyValues = msgType_link.split(",");

                for (String s : keyValues) {
                    String[] keyValue = s.split(":");
                    if (keyValue.length == 4) {
                        if (hashMap.containsKey(keyValue[0])) {
                            hashMap.get(keyValue[0]).add(keyValue[1] + ":" + keyValue[2] + ":" + keyValue[3]);
                        } else {
                            List<String> list = new ArrayList<String>();
                            list.add(keyValue[1] + ":" + keyValue[2] + ":" + keyValue[3]);
                            hashMap.put(keyValue[0], list);
                        }
                    }
                }

                request.getSession().setAttribute("msgTypeLink", hashMap);
            }

            MiscUtils.getLogger().debug("viewMessagePosition: " + messagePosition + "IsLastMsg: " + request.getAttribute("viewMessageIsLastMsg"));

            // Box Type 1 is the sent messages review page.
            if (!"1".equals(boxType)) {
                messagingManager.setMessageRead(loggedInInfo, Long.parseLong(msgDisplayMessage.getMessageId()), providerNo);
            }
        }

        if (linkMsgDemo != null && demographic_no != null) {
            if (linkMsgDemo.equalsIgnoreCase("true")) {
                messengerDemographicManager.attachDemographicToMessage(loggedInInfo, Integer.parseInt(messageNo), Integer.parseInt(demographic_no));
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        request.getSession().setAttribute("today", simpleDateFormat.format(new Date(System.currentTimeMillis())));


        String actionforward = request.getContextPath() + "/oscarMessenger/ViewMessage.jsp?boxType=" + boxType + "&linkMsgDemo=" + linkMsgDemo;

        response.sendRedirect(actionforward);
        return NONE;
    }
}
