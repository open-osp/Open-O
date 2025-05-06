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


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oscarehr.util.MiscUtils;

import oscar.util.Doc2PDF;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public final class MsgDoc2PDF2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    public String execute() throws IOException, ServletException {
        if (this.getIsPreview()) {

            Doc2PDF.parseString2PDF(request, response, "<HTML>" + srcText + "</HTML>");
            this.setIsPreview(false);
        } else {

            oscar.oscarMessenger.pageUtil.MsgSessionBean bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean) request.getSession().getAttribute("msgSessionBean");

            if (bean != null) {

                bean.setAppendPDFAttachment(Doc2PDF.parseString2Bin(request, response, "<HTML>" + srcText + "</HTML>"), pdfTitle);

                this.setIsPreview(false);

            } else {
                MiscUtils.getLogger().debug(" oscar.oscarMessenger.pageUtil.MsgSessionBean is null");
            }
        }
        return SUCCESS;
    }

    String srcText;
    boolean isPreview;
    String jsessionid;
    String[] uriArray;
    String attachmentNumber = null;
    String pdfTitle;

    public String getSrcText() {
        return srcText;
    }

    public void setSrcText(String srcText) {
        this.srcText = srcText;
    }


    public boolean getIsPreview() {
        return isPreview;
    }

    public void setIsPreview(boolean isPreview) {
        this.isPreview = isPreview;
    }

    public String getJsessionid() {
        return jsessionid;
    }

    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }

    public String getPdfTitle() {
        return pdfTitle;
    }

    public void setPdfTitle(String pdfTitle) {
        this.pdfTitle = pdfTitle;
    }

    public String[] getUriArray() {
        return uriArray;
    }

    public void setUriArray(String[] uriArray) {
        this.uriArray = uriArray;
    }

    public void setAttachmentNumber(String attachmentNumber) {
        this.attachmentNumber = attachmentNumber;
    }

    public String getAttachmentNumber() {
        return attachmentNumber;
    }
}
