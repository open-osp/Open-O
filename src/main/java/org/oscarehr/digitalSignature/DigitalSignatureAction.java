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

package org.oscarehr.digitalSignature;

import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.model.DigitalSignature;
import org.oscarehr.common.model.enumerator.ModuleType;
import org.oscarehr.managers.DigitalSignatureManager;
import org.oscarehr.util.DigitalSignatureUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class DigitalSignatureAction extends DispatchAction {

    /**
     * Handles the upload of a digital signature from various sources
     * 
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return An ActionForward to the JSP page that will display the result
     */
    public ActionForward uploadSignature(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String signatureId = "";
        String uploadSource = request.getParameter("source");
        String imageString = request.getParameter("signatureImage");
        String demographic = request.getParameter("demographicNo");
        boolean saveToDB = "true".equals(request.getParameter("saveToDB"));
        String signatureKey = request.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY);
        ModuleType moduleType = ModuleType.getByName(request.getParameter(ModuleType.class.getSimpleName()));

        JsonObject jsonResponse = new JsonObject();

        if(signatureKey != null) {
            String filename = DigitalSignatureUtils.getTempFilePath(signatureKey);

            if ("IPAD".equalsIgnoreCase(uploadSource) && imageString != null && !imageString.isEmpty()) {
                try (FileOutputStream fos = new FileOutputStream(filename)) {
                    imageString = imageString.substring(imageString.indexOf(",") + 1);

                    Base64 b64 = new Base64();
                    byte[] imageByteData = imageString.getBytes();
                    byte[] imageData = b64.decode(imageByteData);

                    if (imageData != null) {
                        fos.write(imageData);
                        MiscUtils.getLogger().debug("Signature uploaded: {}, size={}", filename, imageData.length);
                    }
                } catch (Exception e) {
                    try {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    } catch (Exception ex) {
                        MiscUtils.getLogger().error("Error sending error response", ex);
                    }
                    MiscUtils.getLogger().error("Error uploading signature from IPAD: {}", filename, e);
                }
            } else if (uploadSource == null) {
                try (FileOutputStream fos = new FileOutputStream(filename);
                     InputStream is = request.getInputStream()) {

                    int i = 0;
                    int counter = 0;

                    while ((i = is.read()) != -1) {
                        fos.write(i);
                        counter++;
                    }
                    MiscUtils.getLogger().debug("Signature uploaded : " + filename + ", size=" + counter);
                } catch (Exception e) {
                    try {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    } catch (Exception ex) {
                        MiscUtils.getLogger().error("Error sending error response", ex);
                    }
                    MiscUtils.getLogger().error("Error uploading signature: {}", filename, e);
                }
            }

            if (saveToDB) {
                int demographicNo = -1;

                if (demographic != null && !demographic.isEmpty()) {
                    demographicNo = Integer.parseInt(demographic);
                }

                DigitalSignatureManager digitalSignatureManager = SpringUtils.getBean(DigitalSignatureManager.class);
                DigitalSignature signature = digitalSignatureManager.processAndSaveDigitalSignature(loggedInInfo,
                        request.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY),
                        demographicNo, moduleType);
                if (signature != null) {
                    signatureId = "" + signature.getId();
                }
            }

            try {
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.addProperty("signatureId", signatureId);
            } catch (Exception e) {
                MiscUtils.getLogger().error("Error setting response status or request attribute", e);
            }
        }


        response.setContentType("application/json");

        try(PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
        } catch (IOException e) {
            MiscUtils.getLogger().error("Error writing JSON response", e);
        }

        return null;
    }
}
