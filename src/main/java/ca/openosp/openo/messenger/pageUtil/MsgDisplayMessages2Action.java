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
package ca.openosp.openo.messenger.pageUtil;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.openosp.openo.commn.dao.MessageListDao;
import ca.openosp.openo.commn.model.MessageList;
import ca.openosp.openo.commn.model.Provider;
import ca.openosp.openo.managers.ProviderManager2;
import ca.openosp.openo.managers.SecurityInfoManager;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;
import ca.openosp.openo.utility.SpringUtils;

import ca.openosp.openo.util.ConversionUtils;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class MsgDisplayMessages2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public String execute() throws IOException, ServletException {

        if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_msg", "r", null)) {
            throw new SecurityException("missing required sec object (_msg)");
        }

        // Setup variables
        MsgSessionBean bean = null;
        String providerNo;

        //Initialize forward location
        String findForward = "success";

        if (request.getParameter("providerNo") != null && request.getParameter("userName") != null) {

            bean = new MsgSessionBean();
            bean.setProviderNo(request.getParameter("providerNo"));
            bean.setUserName(request.getParameter("userName"));
            request.getSession().setAttribute("msgSessionBean", bean);

        }//if
        else if (request.getParameter("providerNo") != null && request.getParameter("userName") == null) {
            ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
            Provider p = providerManager.getProvider(LoggedInInfo.getLoggedInInfoFromSession(request), request.getParameter("providerNo"));
            if (p != null) {
                bean = new MsgSessionBean();
                bean.setProviderNo(request.getParameter("providerNo"));
                bean.setUserName(p.getFirstName() + " " + p.getLastName());
                request.getSession().setAttribute("msgSessionBean", bean);
            }
        } else {
            bean = (MsgSessionBean) request.getSession().getAttribute("msgSessionBean");
        }//else

        /*
         *edit 2006-0811-01 by wreby
         *  Adding a search and clear search action to the DisplayMessages JSP
         */
        if (request.getParameter("btnSearch") != null) {
            MsgDisplayMessagesBean displayMsgBean = (MsgDisplayMessagesBean) request.getSession().getAttribute("DisplayMessagesBeanId");

            displayMsgBean.setFilter(request.getParameter("searchString"));
        } else if (request.getParameter("btnClearSearch") != null) {
            MsgDisplayMessagesBean displayMsgBean = (MsgDisplayMessagesBean) request.getSession().getAttribute("DisplayMessagesBeanId");
            displayMsgBean.clearFilter();
        } else if (request.getParameter("btnDelete") != null) {
            //This will go through the array of message Numbers and set them
            //to del.which stands for deleted. but you prolly could have figured that out

            providerNo = bean.getProviderNo();
            MessageListDao dao = SpringUtils.getBean(MessageListDao.class);
            for (int i = 0; i < messageNo.length; i++) {
                List<MessageList> msgs = dao.findByProviderNoAndMessageNo(providerNo, ConversionUtils.fromLongString(messageNo[i]));
                for (MessageList msg : msgs) {
                    msg.setDeleted(true);
                    dao.merge(msg);
                }
            }//for
        } else {
            MiscUtils.getLogger().debug("Unexpected action in MsgDisplayMessagesBean.java");
        }

        return findForward;
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
