//CHECKSTYLE:OFF
/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.casemgmt.web;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.casemgmt.service.ClientImageManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;

public class ClientImage2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private static Logger log = MiscUtils.getLogger();

    private ClientImageManager clientImageManager = SpringUtils.getBean(ClientImageManager.class);

    public String execute() {
        return saveImage();
    }

    public String saveImage() {

        HttpSession session = request.getSession(true);
        String id = (String) (session.getAttribute("clientId"));

        log.info("client image upload: id=" + id);

        File formFile = this.getClientImage();
        String type = formFile.getName().substring(formFile.getName().lastIndexOf(".") + 1);
        if (type != null) type = type.toLowerCase();

        log.info("extension = " + type);

        try {
            byte[] imageData = Files.readAllBytes(formFile.toPath());

            ClientImage clientImage = new ClientImage();
            clientImage.setDemographic_no(Integer.parseInt(id));
            clientImage.setImage_data(imageData);
            clientImage.setImage_type(type);

            clientImageManager.saveClientImage(clientImage);

        } catch (Exception e) {
            log.error("Error", e);
            //post error to page
        }

        request.setAttribute("success", new Boolean(true));

        return SUCCESS;
    }

    private File clientImage;

    public File getClientImage() {
        return clientImage;
    }

    public void setClientImage(File clientImage) {
        this.clientImage = clientImage;
    }
}
