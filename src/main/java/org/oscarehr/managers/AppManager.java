/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
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
 *
 * Modifications made by Magenta Health in 2024.
 */

package org.oscarehr.managers;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.Logger;
import org.oscarehr.app.OAuth1Utils;
import org.oscarehr.common.dao.AppDefinitionDao;
import org.oscarehr.common.dao.AppUserDao;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.common.model.AppUser;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.model.AppDefinitionTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

public interface AppManager {


	public List<AppDefinitionTo1> getAppDefinitions(LoggedInInfo loggedInInfo);
	public AppDefinition saveAppDefinition(LoggedInInfo loggedInInfo,  AppDefinition appDef);
	
	public AppDefinition updateAppDefinition(LoggedInInfo loggedInInfo,  AppDefinition appDef);
	
	public AppDefinition getAppDefinition(LoggedInInfo loggedInInfo,  String appName);
	
	public boolean hasAppDefinition(LoggedInInfo loggedInInfo,  String appName);
	
	public Integer getAppDefinitionConsentId(LoggedInInfo loggedInInfo,  String appName);
	public boolean isK2AUser(LoggedInInfo loggedInInfo);
	public boolean isK2AUser(String providerNo);
	
	public boolean isK2AEnabled();
	
	public String getK2ANotificationNumber(LoggedInInfo loggedInInfo);
	
}
