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

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.exception.PatientDirectiveException;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.util.OscarRoleObjectPrivilege;

import com.quatro.dao.security.SecobjprivilegeDao;
import com.quatro.dao.security.SecuserroleDao;
import com.quatro.model.security.Secobjprivilege;
import com.quatro.model.security.Secuserrole;

import javax.servlet.http.HttpSession;

public interface SecurityInfoManager {
	public static final String READ = "r";
	public static final String WRITE = "w";
	public static final String UPDATE = "u";
	public static final String DELETE = "d";
	public static final String NORIGHTS = "o";

	public List<Secuserrole> getRoles(LoggedInInfo loggedInInfo);

	public List<Secobjprivilege> getSecurityObjects(LoggedInInfo loggedInInfo);

	public boolean hasPrivilege(LoggedInInfo loggedInInfo, String objectName, String privilege, int demographicNo);

	public boolean hasPrivilege(LoggedInInfo loggedInInfo, String objectName, String privilege, String demographicNo);

	public boolean isAllowedAccessToPatientRecord(LoggedInInfo loggedInInfo, Integer demographicNo);
}
