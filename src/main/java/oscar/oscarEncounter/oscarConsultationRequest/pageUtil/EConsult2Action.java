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


package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

/**
 * Class for use with the Ontario MD / eHealth eConsult project
 */
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class EConsult2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private final OscarProperties oscarProperties = OscarProperties.getInstance();
    private final String frontendEconsultUrl = oscarProperties.getProperty("frontendEconsultUrl");
    private final String backendEconsultUrl = oscarProperties.getProperty("backendEconsultUrl");

    public String execute() {
        if ("backend".equals(request.getParameter("method"))) {
            return backend();
        } else if ("login".equals(request.getParameter("method"))) {
            return login();
        }
        return frontend();
    }

    /**
     * Builds a proper eConsult frontend redirect link
     * <p>
     * ie: /?{oneIdEmail}&{delegateOneIdEmail}#!/{task}?{parameter}
     */
    public String frontend() {

        // no token, no fun.
        String oneIdToken = (String) request.getSession().getAttribute("oneid_token");
        if (oneIdToken == null || oneIdToken.isEmpty()) {
            return login();
        }

        String demographicNo = request.getParameter("demographicNo");
        String task = request.getParameter("task");
        StringBuilder stringBuilder = new StringBuilder(frontendEconsultUrl);

        if (!frontendEconsultUrl.endsWith(File.separator)) {
            stringBuilder.append(File.separator);
        }

        setLoginId(stringBuilder, request);

        // Add the method
        stringBuilder.append(task);

        // method parameters.
        if (demographicNo != null && !demographicNo.isEmpty()) {
            stringBuilder.append(String.format("?%1$s=%2$s", "patient_id", demographicNo));
        }

        try {
            response.sendRedirect(stringBuilder.toString());
        } catch (IOException e) {
            MiscUtils.getLogger().error("There was a problem with the redirect of " + stringBuilder.toString(), e);
        }

        return null;
    }

    public String backend() {

        // no token, no fun.
        String oneIdToken = (String) request.getSession().getAttribute("oneid_token");
        if (oneIdToken == null || oneIdToken.isEmpty()) {
            return login();
        }

//		String backendEconsultUrl = oscarProperties.getProperty("backendEconsultUrl");

//        //Creates an HttpGet with the url to get eConsults and sets a header for the oneIdEmail
//        HttpGet httpGet = new HttpGet(url);
//        httpGet.addHeader("x-oneid-email", URLEncoder.encode(oneIdEmail, StandardCharsets.UTF_8.toString()));
//        httpGet.addHeader("x-access-token", oneIdToken);
//        
//        //Gets an HttpClient that will ignore SSL validation
//        HttpClient httpClient = getHttpClient2();
//        //Executes the GET request and stores the response
//        HttpResponse httpResponse = httpClient.execute(httpGet);
//        //Gets the entity from the response and stores it as a JSONObject
//        String entity = EntityUtils.toString(httpResponse.getEntity());
//        JSONObject object = new JSONObject(entity);

        return null;
    }

    /**
     * Creates a proper login redirect.
     */
    public String login() {

        //Gets the request URL
        StringBuffer oscarUrl = request.getRequestURL();

        //Determines the initial length by subtracting the length of the servlet path from the full url's length
        Integer urlLength = oscarUrl.length() - request.getServletPath().length();

        //Sets the length of the URL, found by subtracting the length of the servlet path from the length of the full URL, that way it only gets up to the context path
        oscarUrl.setLength(urlLength);

        oscarUrl.append(String.format("%1$s%2$s", File.separator, "econsultSSOLogin.do"));

        StringBuilder stringBuilder = new StringBuilder(backendEconsultUrl);

        if (!backendEconsultUrl.endsWith(File.separator)) {
            stringBuilder.append(File.separator);
        }

        stringBuilder.append("SAML2/login");

        try {
            stringBuilder.append(String.format("?%1$s=%2$s", "oscarReturnURL", URLEncoder.encode(oscarUrl.toString(), StandardCharsets.UTF_8.toString())));
            stringBuilder.append(String.format("?%1$s=%2$s", "loginStart", new Date().getTime() / 1000));
            response.sendRedirect(stringBuilder.toString());
        } catch (IOException e) {
            MiscUtils.getLogger().error("There was a problem with the redirect of " + stringBuilder.toString(), e);
        }

        return null;
    }

    /**
     * Adds the proper user name line for both the user and user delegate.
     * <p>
     * ie: ?{oneIdEmail}&{delegateOneIdEmail}#!/
     */
    private void setLoginId(StringBuilder stringBuilder, HttpServletRequest request) {

        String oneIdEmail = (String) request.getSession().getAttribute("oneIdEmail");
        String delegateOneIdEmail = (String) request.getSession().getAttribute("delegateOneIdEmail");

        try {
            stringBuilder.append(String.format("?%1$s=%2$s", "oneid_email", URLEncoder.encode(oneIdEmail, StandardCharsets.UTF_8.toString())));

            // Add if there is a delegate too.
            if (delegateOneIdEmail != null && !delegateOneIdEmail.isEmpty()) {
                stringBuilder.append(String.format("&%1$s=%2$s", "delegate_oneid_email", URLEncoder.encode(delegateOneIdEmail, StandardCharsets.UTF_8.toString())));
            }

            // Add the shebang
            stringBuilder.append(String.format("#!%s", File.separator));
        } catch (UnsupportedEncodingException e) {
            MiscUtils.getLogger().error("There was a problem with construction of the login ids " + stringBuilder.toString(), e);
        }
    }


}