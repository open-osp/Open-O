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

public class MsgDisplayMessage {

    private String messageId;
    private String messagePosition;
    private boolean isLastMsg;
    private String status;
    private String thesubject;
    private String thedate;
    private String thetime;
    private String sentby;
    private String sentto;
    private String attach;
    private String pdfAttach;
    private String demographic_no;
    private String nameage;
    private String messageBody;
    private int type;
    private String typeLink;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessagePosition() {
        return messagePosition;
    }

    public void setMessagePosition(String messagePosition) {
        this.messagePosition = messagePosition;
    }

    public boolean isLastMsg() {
        return isLastMsg;
    }

    public void setLastMsg(boolean isLastMsg) {
        this.isLastMsg = isLastMsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThesubject() {
        return thesubject;
    }

    public void setThesubject(String thesubject) {
        this.thesubject = thesubject;
    }

    public String getThedate() {
        return thedate;
    }

    public void setThedate(String thedate) {
        this.thedate = thedate;
    }

    public String getThetime() {
        return thetime;
    }

    public void setThetime(String theime) {
        this.thetime = theime;
    }

    public String getSentby() {
        return sentby;
    }

    public void setSentby(String sentby) {
        this.sentby = sentby;
    }

    public String getSentto() {
        return sentto;
    }

    public void setSentto(String sentto) {
        this.sentto = sentto;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getPdfAttach() {
        return pdfAttach;
    }

    public void setPdfAttach(String pdfAttach) {
        this.pdfAttach = pdfAttach;
    }

    public String getDemographic_no() {
        return demographic_no;
    }

    public void setDemographic_no(String demographic_no) {
        this.demographic_no = demographic_no;
    }

    public String getNameage() {
        return nameage;
    }

    public void setNameage(String nameage) {
        this.nameage = nameage;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeLink() {
        return typeLink;
    }

    public void setTypeLink(String typeLink) {
        this.typeLink = typeLink;
    }

}
