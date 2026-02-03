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

package ca.openosp.openo.digitalsignature;

import ca.openosp.openo.commn.model.DigitalSignature;
import ca.openosp.openo.commn.model.enumerator.ModuleType;
import ca.openosp.openo.managers.DigitalSignatureManager;
import ca.openosp.openo.utility.DigitalSignatureUtils;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;
import ca.openosp.openo.utility.SpringUtils;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.codec.binary.Base64;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Struts2 action for handling digital signature operations.
 * Migrated from Struts1 DispatchAction pattern.
 *
 * @since 2025-01-23
 */
public class DigitalSignature2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private DigitalSignatureManager digitalSignatureManager = SpringUtils.getBean(DigitalSignatureManager.class);

    /**
     * Handles the upload of a digital signature from various sources.
     *
     * @return null to prevent Struts2 from trying to render a view
     * @throws IOException if there's an error writing the response
     */
    public String uploadSignature() throws IOException {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String signatureId = "";
        String uploadSource = request.getParameter("source");
        String imageString = request.getParameter("signatureImage");
        String demographic = request.getParameter("demographicNo");
        boolean saveToDB = "true".equals(request.getParameter("saveToDB"));
        String signatureKey = request.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY);
        ModuleType moduleType = ModuleType.getByName(request.getParameter(ModuleType.class.getSimpleName()));

        JsonObject jsonResponse = new JsonObject();

        if (signatureKey != null) {
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
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    MiscUtils.getLogger().error("Error uploading signature from IPAD: {}", filename, e);
                    return null;
                }
            } else if (uploadSource == null) {
                try (FileOutputStream fos = new FileOutputStream(filename);
                     InputStream is = request.getInputStream()) {

                    int i;
                    int counter = 0;

                    while ((i = is.read()) != -1) {
                        fos.write(i);
                        counter++;
                    }
                    MiscUtils.getLogger().debug("Signature uploaded: {}, size={}", filename, counter);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    MiscUtils.getLogger().error("Error uploading signature: {}", filename, e);
                    return null;
                }
            }

            if (saveToDB) {
                int demographicNo = -1;

                if (demographic != null && !demographic.isEmpty()) {
                    demographicNo = Integer.parseInt(demographic);
                }

                DigitalSignature signature = digitalSignatureManager.processAndSaveDigitalSignature(
                        loggedInInfo,
                        request.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY),
                        demographicNo,
                        moduleType
                );

                if (signature != null) {
                    signatureId = String.valueOf(signature.getId());
                }
            }

            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.addProperty("signatureId", signatureId);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
        } catch (IOException e) {
            MiscUtils.getLogger().error("Error writing JSON response", e);
            throw e;
        }

        return null;
    }
}
