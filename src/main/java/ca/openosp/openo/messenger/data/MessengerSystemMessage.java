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
package ca.openosp.openo.messenger.data;

import java.util.Date;

public class MessengerSystemMessage {

    private Date dateTime;
    private String message;
    private String subject;
    private String sentBy;
    private String sentByNo;
    private int sentByLocation;
    private String attachment;
    private String pdfAttachment;
    private String actionStatus;
    private Integer type;
    private String type_link;
    private String[] recipients;
    private Integer[] attachedDemographicNo;

    /**
     * Instantiate with a list of known providers numbers for whom
     * this message is for.
     *
     * @param recipients
     */
    public MessengerSystemMessage(String[] recipients) {
        setRecipients(recipients);
    }

    public Date getDate() {
        return dateTime;
    }

    public void setDate(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        if (subject == null) {
            return "System Message";
        }
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSentBy() {
        if (sentBy == null) {
            return "System";
        }
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getSentByNo() {
        if (sentByNo == null) {
            return "-1";
        }
        return sentByNo;
    }

    public void setSentByNo(String sentByNo) {
        this.sentByNo = sentByNo;
    }

    public int getSentByLocation() {
        return sentByLocation;
    }

    public void setSentByLocation(int sentByLocation) {
        this.sentByLocation = sentByLocation;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getPdfAttachment() {
        return pdfAttachment;
    }

    public void setPdfAttachment(String pdfAttachment) {
        this.pdfAttachment = pdfAttachment;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getType_link() {
        return type_link;
    }

    public void setType_link(String type_link) {
        this.type_link = type_link;
    }

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public Integer[] getAttachedDemographicNo() {
        return attachedDemographicNo;
    }

    public void setAttachedDemographicNo(Integer[] attachedDemographicNo) {
        this.attachedDemographicNo = attachedDemographicNo;
    }

}
