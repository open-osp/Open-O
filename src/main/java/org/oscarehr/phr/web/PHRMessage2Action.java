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

package org.oscarehr.phr.web;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.oscarehr.PMmodule.caisi_integrator.ConformanceTestHelper;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.documentManager.EDoc;
import org.oscarehr.documentManager.EDocUtil;
import org.oscarehr.documentManager.actions.AddEditDocument2Action;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.myoscar.client.ws_manager.AccountManager;
import org.oscarehr.myoscar.client.ws_manager.MessageManager;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.myoscar_server.ws.*;
import org.oscarehr.phr.dao.PHRActionDAO;
import org.oscarehr.phr.dao.PHRDocumentDAO;
import org.oscarehr.phr.model.PHRAction;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.*;
import org.w3c.dom.Document;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarProvider.data.ProviderData;
import oscar.util.UtilDateUtilities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ClassCastException;
import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PHRMessage2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private static Logger logger = MiscUtils.getLogger();

    PHRDocumentDAO phrDocumentDAO = SpringUtils.getBean(PHRDocumentDAO.class);
    PHRActionDAO phrActionDAO = SpringUtils.getBean(PHRActionDAO.class);
    PHRService phrService = SpringUtils.getBean(PHRService.class);

    public String execute() throws Exception {
        String method = request.getParameter("method");
        if ("viewSentMessages".equals(method)) {
            return viewSentMessages();
        } else if ("viewArchivedMessages".equals(method)) {
            return viewArchivedMessages();
        } else if ("read".equals(method)) {
            return read();
        } else if ("reply".equals(method)) {
            return reply();
        } else if ("createMessage".equals(method)) {
            return createMessage();
        } else if ("sendReply".equals(method)) {
            return sendReply();
        } 
        return viewMessages();
    }

    public String viewMessages() throws Exception {

        clearSessionVariables(request);

        return "view";
    }

    public String viewSentMessages() {

        return "view";
    }

    public String viewArchivedMessages() {

        return "view";
    }

    public String read() {
        return "read";
    }

    //
    // Reply is a create but displays the message being re
    //
    public String reply() {

        return "create";
    }

    public String createMessage() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String demographicNo = request.getParameter("demographicNo");
        String provNo = (String) request.getSession().getAttribute("user");

        MyOscarLoggedInInfo myOscarLoggedInInfo = MyOscarLoggedInInfo.getLoggedInInfo(request.getSession());
        if (myOscarLoggedInInfo == null || !myOscarLoggedInInfo.isLoggedIn()) {
            request.setAttribute("forwardToOnSuccess", request.getContextPath() + "/phr/PhrMessage.do?method=createMessage&providerNo=" + provNo + "&demographicNo=" + demographicNo);
            return "loginAndRedirect";
        }

        //Check if patient has been verified
        DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
        if (!demographicManager.getPhrVerificationLevelByDemographicId(loggedInInfo, Integer.parseInt(demographicNo))) { //Prompt for verification
            request.setAttribute("forwardToOnSuccess", "/phr/PhrMessage.do?method=createMessage&providerNo=" + provNo + "&demographicNo=" + demographicNo);
            request.setAttribute("demographicNo", demographicNo);
            return "verifyAndRedirect";

        }

        DemographicData dd = new DemographicData();
        org.oscarehr.common.model.Demographic d = dd.getDemographic(loggedInInfo, demographicNo);

        String providerName = ProviderData.getProviderName(provNo);

        String toName = d.getFirstName() + " " + d.getLastName();
        String toId = demographicNo;
        String toType = "" + PHRDocument.TYPE_DEMOGRAPHIC;

        String fromName = providerName;
        String fromId = provNo;
        String fromType = "" + PHRDocument.TYPE_PROVIDER;

        request.setAttribute("toName", toName);
        request.setAttribute("toId", toId);
        request.setAttribute("toType", toType);

        request.setAttribute("fromName", fromName);
        request.setAttribute("fromId", fromId);
        request.setAttribute("fromType", fromType);

        return "create";
    }

    public String sendReply() throws Exception {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        Long replyToMessageId = new Long(request.getParameter("replyToMessageId"));
        String message = StringUtils.trimToNull(request.getParameter("body"));
        boolean replyAll = Boolean.parseBoolean(request.getParameter("replyAll"));
        boolean saveFileAttachmentToDocs = WebUtils.isChecked(request, "saveFileAttachmentToDocs");

        File attachment = this.getFileAttachment();

        MyOscarLoggedInInfo myOscarLoggedInInfo = MyOscarLoggedInInfo.getLoggedInInfo(request.getSession());
        MessageTransfer3 previousMessage = MessageManager.getMessage(myOscarLoggedInInfo, replyToMessageId);

        Message2DataTransfer subjectPart = MessageManager.getMessage2DataTransfer(previousMessage, "SUBJECT");
        String replySubject = "re: " + (subjectPart != null ? new String(subjectPart.getContents(), "UTF-8") : "");

        MessageTransfer3 newMessage = MessageManager.makeBasicMessageTransfer(myOscarLoggedInInfo, replyToMessageId, null, replySubject, message);
        List<Long> recipientList = newMessage.getRecipientPeopleIds();

        if (previousMessage.getReplyToPersonId() != null) recipientList.add(previousMessage.getReplyToPersonId());
        else recipientList.add(previousMessage.getSenderPersonId());

        if (replyAll) {
            for (Long recipientId : previousMessage.getRecipientPeopleIds()) {
                if (myOscarLoggedInInfo.getLoggedInPersonId().equals(recipientId)) continue;

                recipientList.add(recipientId);
            }
        }

        Message2DataTransfer attachmentPart = null;
        if (attachment != null) {
            attachmentPart = makeFileAttachmentMessagePart(attachment);
            if (attachmentPart != null) newMessage.getMessageDataList().add(attachmentPart);
        }

        Long messageId = null;
        try {
            messageId = MessageManager.sendMessage(myOscarLoggedInInfo, newMessage);

            if (attachmentPart != null && saveFileAttachmentToDocs) {
                Long senderPersonId = previousMessage.getSenderPersonId();
                MinimalPersonTransfer2 minimalPersonSender = AccountManager.getMinimalPerson(myOscarLoggedInInfo, senderPersonId);
                String myOscarUserName = minimalPersonSender.getUserName();
                Demographic demographic = MyOscarUtils.getDemographicByMyOscarUserName(myOscarUserName);

                saveToDocs(loggedInInfo, demographic.getDemographicNo(), attachment, replySubject);
            }
        } catch (NotAuthorisedException_Exception e) {
            WebUtils.addErrorMessage(request.getSession(), "This patient has not given you permissions to send them a message.");
            return "create";
        }

        if (messageId != null && request.getParameter("andPasteToEchart") != null && request.getParameter("andPasteToEchart").equals("yes")) {
            StringBuilder redirect = new StringBuilder("/oscarEncounter/IncomingEncounter.do?");
            redirect.append("myoscarmsg=").append(messageId.toString());
            redirect.append("&remyoscarmsg=").append(replyToMessageId.toString());
            redirect.append("&appointmentDate=").append(UtilDateUtilities.DateToString(new Date())); //Makes echart note have todays date, which makes sense because we are reply now
            redirect.append("&demographicNo=").append(request.getParameter("demographicNo"));
            response.sendRedirect(redirect.toString());
        }

        return "view";
    }

    private File fileAttachment;          // 上传的文件
    private String fileAttachmentFileName; // 上传文件的名称
    private String fileAttachmentContentType; // 上传文件的类型

    public File getFileAttachment() {
        return fileAttachment;
    }

    public void setFileAttachment(File fileAttachment) {
        this.fileAttachment = fileAttachment;
    }

    private static Message2DataTransfer makeFileAttachmentMessagePart(File attachment) throws ParserConfigurationException, FileNotFoundException, IOException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (attachment == null) return (null);

        Message2DataTransfer result = new Message2DataTransfer();
        result.setMimeType("application/xml");
        result.setDataType("FILE_ATTACHMENT");

        String fileName = attachment.getName();
        String mimeType = Files.probeContentType(attachment.toPath());
        byte[] bytes = Files.readAllBytes(attachment.toPath());

        logger.debug("filename=" + fileName);
        logger.debug("mimeType=" + mimeType);
        logger.debug("bytes=" + bytes.length);

        if (fileName == null || mimeType == null || bytes.length == 0) return (null);

        Document doc = XmlUtils.newDocument("file_attachment");
        XmlUtils.appendChildToRoot(doc, "filename", fileName);
        XmlUtils.appendChildToRoot(doc, "mimeType", mimeType);
        XmlUtils.appendChildToRoot(doc, "bytes", bytes);

        result.setContents(XmlUtils.toBytes(doc, false));

        return (result);
    }

    public String sendPatient() throws Exception {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String subject = request.getParameter("subject");
        String messageBody = request.getParameter("body");
        Integer demographicId = Integer.parseInt(request.getParameter("demographicId"));
        boolean saveFileAttachmentToDocs = WebUtils.isChecked(request, "saveFileAttachmentToDocs");

        File attachment = this.getFileAttachment();

        MyOscarLoggedInInfo myOscarLoggedInInfo = MyOscarLoggedInInfo.getLoggedInInfo(request.getSession());

        DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean(DemographicDao.class);
        Demographic demographic = demographicDao.getDemographicById(demographicId);
        Long recipientMyOscarUserId = AccountManager.getUserId(myOscarLoggedInInfo, demographic.getMyOscarUserName());

        MessageTransfer3 newMessage = MessageManager.makeBasicMessageTransfer(myOscarLoggedInInfo, null, null, subject, messageBody);
        List<Long> recipientList = newMessage.getRecipientPeopleIds();
        recipientList.add(recipientMyOscarUserId);

        Message2DataTransfer attachmentPart = null;
        if (attachment != null) {
            attachmentPart = makeFileAttachmentMessagePart(attachment);
            if (attachmentPart != null) newMessage.getMessageDataList().add(attachmentPart);
        }

        try {
            MessageManager.sendMessage(myOscarLoggedInInfo, newMessage);

            if (attachmentPart != null && saveFileAttachmentToDocs) {
                saveToDocs(loggedInInfo, demographicId, attachment, subject);
            }
        } catch (NotAuthorisedException_Exception e) {
            WebUtils.addErrorMessage(request.getSession(), "This patient has not given you permissions to send them a message.");
            return "create";
        }

        return "view";
    }

    private void saveToDocs(LoggedInInfo loggedInInfo, Integer demographicNo, File attachment, String messageSubject) throws Exception {
        String fileName = attachment.getName();
        String mimeType = Files.probeContentType(attachment.toPath());
        byte[] bytes = Files.readAllBytes(attachment.toPath());

        saveAttachmentToEchartDocuments(loggedInInfo, demographicNo, messageSubject, new GregorianCalendar(), fileName, mimeType, bytes);
    }

    public String delete() {
        String id = request.getParameter("id");
        if (id == null) return viewSentMessages();
        logger.debug("Id to delete:" + id);
        PHRAction action = phrActionDAO.getActionById(id);
        if (action.getStatus() != PHRAction.STATUS_SENT) {
            action.setStatus(PHRAction.STATUS_NOT_SENT_DELETED);
            phrActionDAO.update(action);
        }

        return viewSentMessages();
    }

    public String resend() {
        String id = request.getParameter("id");
        if (id != null) {
            PHRAction action = phrActionDAO.getActionById(id);
            if (action.getStatus() != PHRAction.STATUS_SENT) {
                action.setStatus(PHRAction.STATUS_SEND_PENDING);
                phrActionDAO.update(action);
            }
        }
        return viewSentMessages();
    }

    public String flipActive() throws Exception {
        Long messageId = new Long(request.getParameter("messageId"));

        MyOscarLoggedInInfo myOscarLoggedInInfo = MyOscarLoggedInInfo.getLoggedInInfo(request.getSession());

        Message2RecipientPersonAttributesTransfer recipientAttributes = MessageManager.getMessageRecipientPersonAttributesTransfer(myOscarLoggedInInfo, messageId, myOscarLoggedInInfo.getLoggedInPersonId());
        recipientAttributes.setActive(!recipientAttributes.isActive());
        MessageManager.updateMessageRecipientPersonAttributesTransfer(myOscarLoggedInInfo, recipientAttributes);

        return "view";
    }

    private void clearSessionVariables(HttpServletRequest request) {
        request.getSession().setAttribute("indivoMessages", null);
        request.getSession().setAttribute("indivoSentMessages", null);
        request.getSession().setAttribute("indivoMessageActions", null);
        request.getSession().setAttribute("indivoArchivedMessages", null);
        request.getSession().setAttribute("indivoOtherActions", null);

    }

    public static void saveAttachmentToEchartDocuments(LoggedInInfo loggedInInfo, Integer demographicNo, String messageSubject, Calendar messageSentDate, String filename, String mimeType, byte[] fileBytes) throws Exception {
        String description = "Attachment : " + messageSubject;

        String date = DateUtils.getIsoDate(messageSentDate);
        date = date.replaceAll("-", "/");

        EDoc newDoc = new EDoc(description, "others", filename, "", loggedInInfo.getLoggedInProviderNo(), "", "", 'A', date, "", "", "demographic", demographicNo.toString());

        // new file name with date attached
        String fileName2 = newDoc.getFileName();

        // save local file
        ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes);
        File file = AddEditDocument2Action.writeLocalFile(bais, fileName2);

        newDoc.setContentType(mimeType);
        if ("application/pdf".equals(mimeType)) {
            int numberOfPages = AddEditDocument2Action.countNumOfPages(fileName2);
            newDoc.setNumberOfPages(numberOfPages);
        }

        String doc_no = EDocUtil.addDocumentSQL(newDoc);
        if (ConformanceTestHelper.enableConformanceOnlyTestFeatures) {
            AddEditDocument2Action.storeDocumentInDatabase(file, Integer.parseInt(doc_no));
        }
        LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), LogConst.ADD, LogConst.CON_DOCUMENT, doc_no);
    }
}
