/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.ws.rest;

import org.apache.commons.lang.StringUtils;

import org.oscarehr.common.model.Document;
import org.oscarehr.managers.DocumentManager;
import org.oscarehr.util.LoggedInInfo;

import org.oscarehr.ws.rest.conversion.DocumentConverter;
import org.oscarehr.ws.rest.to.model.DocumentTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Service
@Path("/document")
@Component("documentService")
public class DocumentService extends AbstractServiceImpl{
    @Autowired
    DocumentManager documentManager;

    @POST
    @Path("/saveDocumentToDemographic")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveDocumentToDemographic(DocumentTo1 documentT) {
        Response response;

        if (StringUtils.isNotEmpty(documentT.getFileName()) && documentT.getFileContents().length > 0 && documentT.getDemographicNo() != null) {
            try {
                DocumentConverter documentConverter = new DocumentConverter();
                LoggedInInfo loggedInInfo = getLoggedInInfo();
                if (StringUtils.isEmpty(documentT.getSource())) {
                    documentT.setSource("REST API");
                }
                Document document = documentConverter.getAsDomainObject(loggedInInfo, documentT);
                document = documentManager.createDocument(loggedInInfo, document, documentT.getDemographicNo(), documentT.getProviderNo(), documentT.getFileContents());
                response = Response.ok(documentConverter.getAsTransferObject(loggedInInfo, document)).build();
            } catch (IOException e) {
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("The document could not be saved.").build();
            }
        } else {
            response = Response.status(Response.Status.BAD_REQUEST).entity("The request body must contain a title, encoded documentData, a fileType (png, jpg, pdf, etc.), and a demographicNo").build();
        }

        return response;
    }
}
