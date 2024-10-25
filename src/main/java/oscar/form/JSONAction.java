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
package oscar.form;

import net.sf.json.JSONObject;
import org.apache.logging.log4j.Logger;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class JSONAction extends DispatchAction {

    private final String ENCODING = "UTF-8";
    private final String CONTENT_TYPE = "application/json";
    private final Logger logger = MiscUtils.getLogger();

    protected void jsonResponse(HttpServletResponse response, JSONObject jsonObject) {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            out.print(jsonObject.toString());
            out.flush();
        } catch (IOException e) {
            logger.error("Error while creating JSON response", e);
        }
    }

    protected void jsonResponse(HttpServletResponse response, String jsonString) {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            out.print(jsonString);
            out.flush();
        } catch (IOException e) {
            logger.error("Error while creating JSON response", e);
        }
    }

    protected void jsonResponse(HttpServletResponse response, String name, String value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(name, value);
        jsonResponse(response, jsonObject);
    }

    protected void errorResponse(HttpServletResponse response, String name, String value) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        jsonResponse(response, name, value);
    }
}
