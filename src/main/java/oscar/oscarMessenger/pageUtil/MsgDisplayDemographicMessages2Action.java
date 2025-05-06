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


/*
 * MsgDisplayDemographicMessages2Action.java
 *
 * Created on May 8, 2005, 12:20 AM
 */

package oscar.oscarMessenger.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarMessenger.util.MsgDemoMap;

/**
 * @author root
 */
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class MsgDisplayDemographicMessages2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public String execute()
            throws IOException, ServletException {

        if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_msg", "r", null)) {
            throw new SecurityException("missing required security object (_msg)");
        }

        // Extract attributes we will need

        MiscUtils.getLogger().debug("this is displayDemographicMessages.Action");

        // Setup variables
        MsgSessionBean bean = (MsgSessionBean) request.getSession().getAttribute("msgSessionBean");

        if (bean == null) {
            bean = new MsgSessionBean();
            String providerNo = (String) request.getSession().getAttribute("user");
            String userName = request.getParameter("userName");
            String demographicNo = request.getParameter("demographic_no");
            
            if (providerNo == null || userName == null || demographicNo == null) {
                MiscUtils.getLogger().error("Missing required parameters: " + 
                                          "providerNo=" + providerNo + 
                                          ", userName=" + userName + 
                                          ", demographic_no=" + demographicNo);
                return "error"; 
            }
            
            bean.setProviderNo(providerNo);
            bean.setUserName(userName);
            bean.setDemographic_no(demographicNo);

            request.getSession().setAttribute("msgSessionBean", bean);
            MiscUtils.getLogger().debug("Created new MsgSessionBean for provider: " + providerNo);
        }


        //Unlinked selected messages
        if (messageNo != null) {
            MsgDemoMap msgDemoMap = new MsgDemoMap();
            for (int i = 0; i < messageNo.length; i++) {
                msgDemoMap.unlinkMsg(request.getParameter("demographic_no"), messageNo[i]);
            }
        }

        return "demoMsg";
    }


    String[] messageNo;

    /**
     * Used to get the MessageNo in the DisplayMessagesAction class
     *
     * @return String[], these are the messages the will be set to del
     */
    public String[] getMessageNo() {
        if (messageNo == null) {
            messageNo = new String[]{};
        }
        return messageNo;
    }

    /**
     * Used to set the MessageNo, these are the messageNo that will be set to be deleted
     *
     * @param mess String[], these are the message No to be deleted
     */
    public void setMessageNo(String[] mess) {
        this.messageNo = mess;
    }
}
