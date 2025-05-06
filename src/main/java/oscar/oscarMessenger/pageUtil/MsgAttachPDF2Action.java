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

import org.apache.logging.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.util.Doc2PDF;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class MsgAttachPDF2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private static Logger logger = MiscUtils.getLogger();

    public String execute() throws IOException, ServletException {
        logger.info("Starting...");

        MsgSessionBean bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean) request.getSession().getAttribute("msgSessionBean");

        // Multiple attachment

        if (isPreview) {
            logger.info("Got source text: " + srcText);

            Doc2PDF.parseString2PDF(request, response, "<HTML>" + srcText + "</HTML>");
            isPreview = false;
        } else {

            try {
                if (bean != null) {
                    if (isNew) {
                        logger.debug("Nullifying attachment");
                        bean.nullAttachment();
                    }

                    bean.setTotalAttachmentCount(Integer.parseInt(attachmentCount));

                    if (bean.getCurrentAttachmentCount() < bean.getTotalAttachmentCount()) {
                        String resultString = Doc2PDF.parseString2Bin(request, response, "<HTML>" + srcText + "</HTML>");
                        bean.setAppendPDFAttachment(resultString, attachmentTitle);
                        bean.setCurrentAttachmentCount(bean.getCurrentAttachmentCount() + 1);
                        logger.info("Sleeping for a short period...");
                        Thread.sleep(500);
                    }

                    if (bean.getCurrentAttachmentCount() >= bean.getTotalAttachmentCount()) {
                        bean.setTotalAttachmentCount(0);
                        bean.setCurrentAttachmentCount(0);
                        return SUCCESS;
                    } else {
                        return "attaching";
                    }
                } else {
                    logger.error("Bean is null");
                }
            } catch (Exception e) {
                logger.error("Error: " + e.getMessage(), e);
            }

        }
        return null;
    }
    private String attachmentCount = "0";
    private String attachmentTitle = "";
    private String srcText = "";
    private boolean isPreview = false;
    private boolean isAttaching = false;
    private boolean isNew = true;
    private String[] indexArray;

    public String getAttachmentCount() {
        return attachmentCount;
    }

    public void setAttachmentCount(String attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public String getAttachmentTitle() {
        return attachmentTitle;
    }

    public void setAttachmentTitle(String attachmentTitle) {
        this.attachmentTitle = attachmentTitle;
    }

    public String getSrcText() {
        return srcText;
    }

    public void setSrcText(String srcText) {
        this.srcText = srcText;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public void setIsPreview(boolean preview) {
        isPreview = preview;
    }

    public boolean isAttaching() {
        return isAttaching;
    }

    public void setIsAttaching(boolean attaching) {
        isAttaching = attaching;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean aNew) {
        isNew = aNew;
    }

    public String[] getIndexArray() {
        return indexArray;
    }

    public void setIndexArray(String[] indexArray) {
        this.indexArray = indexArray;
    }
}
