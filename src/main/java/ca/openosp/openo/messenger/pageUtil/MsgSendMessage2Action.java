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
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.openosp.openo.PMmodule.dao.ProviderDao;
import ca.openosp.openo.commn.dao.MessageListDao;
import ca.openosp.openo.commn.dao.MessageTblDao;
import ca.openosp.openo.commn.model.MessageList;
import ca.openosp.openo.commn.model.MessageTbl;
import ca.openosp.openo.commn.model.Provider;
import ca.openosp.openo.managers.SecurityInfoManager;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.SpringUtils;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class MsgSendMessage2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public String execute() throws IOException, ServletException {
        // Extract attributes we will need
        // Setup variables
        if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_msg", "w", null)) {
            throw new SecurityException("missing required sec object (_msg)");
        }

        String[] providers = this.getProvider();
        subject.trim();

        if (subject.length() == 0) {
            subject = "none";
        }

        //By this far it should be safe that its a valid message
        //create a string with all the providers it will be sent too
        ProviderDao pDao = SpringUtils.getBean(ProviderDao.class);
        List<Provider> ps = pDao.getProviders(providers);
        StringBuilder sentToWho = new StringBuilder("Sent to : ");
        for (Provider p : ps) {
            sentToWho.append(" " + p.getFirstName() + " " + p.getLastName() + ". ");
        }

        MessageTbl mt = new MessageTbl();
        mt.setDate(new Date());
        mt.setTime(new Date());
        mt.setMessage(message);
        mt.setSubject(subject);
        mt.setSentBy("jay");
        mt.setSentTo(sentToWho.toString());

        MessageTblDao dao = SpringUtils.getBean(MessageTblDao.class);
        dao.persist(mt);

        MessageListDao mld = SpringUtils.getBean(MessageListDao.class);

        for (int i = 0; i < providers.length; i++) {
            MessageList ml = new MessageList();
            ml.setMessage(mt.getId());
            ml.setProviderNo(providers[i]);
            ml.setStatus("new");
            mld.persist(ml);
        }
        request.setAttribute("SentMessageProvs", sentToWho.toString());
        return SUCCESS;
    }

    private String[] provider;
    private String message;
    private String subject;
    private String demographic_no;

    public String[] getProvider() {
        return provider;
    }

    public void setProvider(String[] provider) {
        this.provider = provider;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDemographic_no() {
        return demographic_no;
    }

    public void setDemographic_no(String demographic_no) {
        this.demographic_no = demographic_no;
    }
}
