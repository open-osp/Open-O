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


package ca.openosp.openo.messenger.tld;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import ca.openosp.openo.PMmodule.service.ProviderManager;
import ca.openosp.openo.commn.model.Provider;
import ca.openosp.openo.commn.model.Security;
import ca.openosp.openo.managers.FacilityManager;
import ca.openosp.openo.managers.MessagingManager;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;
import ca.openosp.openo.utility.SpringUtils;

public class MsgNewMessageTag extends TagSupport {

    private static MessagingManager messagingManager = SpringUtils.getBean(MessagingManager.class);
    private static ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
    private static FacilityManager facilityManager = SpringUtils.getBean(FacilityManager.class);

    private String providerNo;
    private int numNewMessages;
    private int numNewDemographicMessages;
    private int numIntegratedMessages;
    private LoggedInInfo loggedInInfo;

    public MsgNewMessageTag() {
        numNewMessages = 0;
        numNewDemographicMessages = 0;
        numIntegratedMessages = 0;
        loggedInInfo = new LoggedInInfo();
    }

    public void setProviderNo(String providerNo1) {
        providerNo = providerNo1;
    }

    public String getProviderNo() {
        return providerNo;
    }

    public int doStartTag() throws JspException {

        Provider provider = providerManager.getProvider(providerNo);
        loggedInInfo.setLoggedInProvider(provider);
        loggedInInfo.setLoggedInSecurity(new Security());

        numNewMessages = messagingManager.getMyInboxMessageCount(loggedInInfo, providerNo, false);
        numNewDemographicMessages = messagingManager.getMyInboxMessageCount(loggedInInfo, providerNo, true);

        if (facilityManager.getDefaultFacility(loggedInInfo).isIntegratorEnabled()) {
            numIntegratedMessages = messagingManager.getMyInboxIntegratorMessagesCount(loggedInInfo, providerNo);
        }

        try {
            JspWriter out = super.pageContext.getOut();
            if (numNewMessages > 0) {
                out.print("<span class='tabalert'>");
            } else {
                out.print("<span>");
            }
        } catch (Exception p) {
            MiscUtils.getLogger().error("Error", p);
        }
        return (EVAL_BODY_INCLUDE);
    }

    public int doEndTag() throws JspException {
        //ronnie 2007-4-26
        try {
            JspWriter out = super.pageContext.getOut();
            if (numNewMessages > 0) {
                StringBuilder stringBuilder = new StringBuilder("<sup>");
                stringBuilder.append("<span id='demographicMessageCount' title='New Demographic Messages'>");
                stringBuilder.append(numNewDemographicMessages);
                stringBuilder.append("</span>");

                if (facilityManager.getDefaultFacility(loggedInInfo).isIntegratorEnabled()) {
                    stringBuilder.append("|<span id='integratorMessageCount' title='New Integrator Messages'>");
                    stringBuilder.append(numIntegratedMessages);
                    stringBuilder.append("</span>");
                }

                stringBuilder.append("/<span id='totalMessageCount' title='Total New Messages'>");
                stringBuilder.append(numNewMessages);
                stringBuilder.append("</span>");

                stringBuilder.append("</sup></span>  ");

                out.print(stringBuilder);
            } else {
                out.print("</span>  ");
            }
        } catch (Exception p) {
            MiscUtils.getLogger().error("Error", p);
        }
        return EVAL_PAGE;
    }


}
