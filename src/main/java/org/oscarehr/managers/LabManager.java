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
package org.oscarehr.managers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.dao.Hl7TextMessageDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;

import oscar.log.LogAction;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;


@Service
public class LabManager {

	private static final String TEMP_PDF_DIRECTORY = "hl7PDF";
	private static final String DEFAULT_FILE_SUFFIX = ".pdf";

	@Autowired
	Hl7TextInfoDao hl7textInfoDao;

	@Autowired
	Hl7TextMessageDao hl7TextMessageDao;
	
	@Autowired
	private PatientLabRoutingDao patientLabRoutingDao;
	
	@Autowired
	SecurityInfoManager securityInfoManager;

	public List<Hl7TextMessage> getHl7Messages(LoggedInInfo loggedInInfo, Integer demographicNo, int offset, int limit) {
		checkPrivilege(loggedInInfo, "r");
		
		LogAction.addLogSynchronous(loggedInInfo, "LabManager.getHl7Messages", "demographicNo="+demographicNo);
		
		List<Hl7TextMessage> results = hl7TextMessageDao.findByDemographicNo(demographicNo, offset, limit);

		return results;
	}
	
	public List<Hl7TextInfo> getHl7TextInfo(LoggedInInfo loggedInInfo, int demographicNo) {
		checkPrivilege(loggedInInfo, "r");
		
		List<PatientLabRouting> patientLabRoutingList = patientLabRoutingDao.findByDemographicAndLabType(demographicNo, PatientLabRoutingDao.HL7);
		List<Integer> labIds = new ArrayList<Integer>();
		if(patientLabRoutingList != null) {
			for(PatientLabRouting patientLabRouting : patientLabRoutingList) {
				labIds.add(patientLabRouting.getLabNo());
			}			
		}
		
		LogAction.addLogSynchronous(loggedInInfo, "LabManager.getHl7TextInfo", "demographicNo="+demographicNo);
		
		return hl7textInfoDao.findByLabIdList(labIds);

	}
	
	public Hl7TextMessage getHl7Message(LoggedInInfo loggedInInfo, int labId) {
		checkPrivilege(loggedInInfo, "r");
		
		LogAction.addLogSynchronous(loggedInInfo, "LabManager.getHl7Message", "labId="+labId);
		
		Hl7TextMessage result = hl7TextMessageDao.find(labId);

		return result;
	}
	
	public Path getHl7MessageAsPDF(LoggedInInfo loggedInInfo, int labId) {
		checkPrivilege(loggedInInfo, "r");
		
		LogAction.addLogSynchronous(loggedInInfo, "LabManager.getHl7MessageAsPDF", "labId="+labId);
		
		Path path = null;
		
		try {
			byte[] pdfBytes = LabPDFCreator.getPdfBytes(labId+"", null);
			String fileName = System.currentTimeMillis() + "_" + labId + "_LabReport";		
			Path directory = Files.createTempDirectory(TEMP_PDF_DIRECTORY + System.currentTimeMillis());			
			Path file = Files.createTempFile(directory, fileName, DEFAULT_FILE_SUFFIX);				
			path = Files.write(file, pdfBytes);
		} catch (IOException e) {
			MiscUtils.getLogger().error("A problem creating PDF for lab id " + labId, e);
		} catch (DocumentException e) {
			MiscUtils.getLogger().error("A problem creating PDF for lab id " + labId, e);
		}

		return path;
	}

	private void checkPrivilege(LoggedInInfo loggedInInfo, String privilege) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_lab", privilege, null)) {
			throw new RuntimeException("missing required security object (_lab)");
		}
	}
}
