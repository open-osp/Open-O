/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.dashboard.handler;

import java.util.List;

import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.common.model.OscarMsgType;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarMessenger.data.MessengerSystemMessage;
import org.oscarehr.common.model.Security;
import org.oscarehr.managers.MessagingManager;

public class MessageHandler {

	private static final String SYSTEM_USER_ID = "-1";
	private static final String SYSTEM_USER_NAME = "System";

	public void notifyProvider(
			String subject,
			String messageBody,
			String providerNo
	) {
		notifyProvider(
				subject,
				messageBody,
				providerNo,
				null);
	}

	// This is based on EaapsHandler.notifyProvider
	public void notifyProvider(
		String subject,
		String messageBody,
		String providerNo,
		List<Integer> linkedDemographicNumbers
	) {
		String userName = SYSTEM_USER_NAME;
		String userNo = SYSTEM_USER_ID;
		
		MessagingManager messagingManager = SpringUtils.getBean(MessagingManager.class);
		ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
		org.oscarehr.common.model.Provider provider = providerManager.getProvider(SYSTEM_USER_ID);
		Security security = new Security();
		LoggedInInfo loggedInInfo = new LoggedInInfo();
		loggedInInfo.setLoggedInProvider(provider);
		loggedInInfo.setLoggedInSecurity(security);
		
        MessengerSystemMessage systemMessage = new MessengerSystemMessage(new String[] { providerNo });
        systemMessage.setType(OscarMsgType.GENERAL_TYPE);
        systemMessage.setSentByNo(userNo);
        systemMessage.setSentBy(userName);
        systemMessage.setSubject(subject);
        systemMessage.setMessage(messageBody);

		if(linkedDemographicNumbers != null && ! linkedDemographicNumbers.isEmpty()) {
			systemMessage.setAttachedDemographicNo(linkedDemographicNumbers.toArray(new Integer[linkedDemographicNumbers.size()]));
		}

        messagingManager.sendSystemMessage(loggedInInfo, systemMessage);

	}
}
