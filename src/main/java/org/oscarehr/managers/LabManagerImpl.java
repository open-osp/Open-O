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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.dao.Hl7TextMessageDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.PDFGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;

import oscar.log.LogAction;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.util.StringUtils;


@Service
public class LabManagerImpl implements LabManager{

	private static final String TEMP_PDF_DIRECTORY = "hl7PDF";
	private static final String DEFAULT_FILE_SUFFIX = ".pdf";

	@Autowired
	Hl7TextInfoDao hl7textInfoDao;

	@Autowired
	Hl7TextMessageDao hl7TextMessageDao;

	@Autowired
	private NioFileManager nioFileManager;
	
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

	public Path renderLab(LoggedInInfo loggedInInfo, Integer segmentId) throws PDFGenerationException {
		checkPrivilege(loggedInInfo, "r");
		LogAction.addLogSynchronous(loggedInInfo, "LabManager.getHl7MessageAsPDF", "labId="+segmentId);

		Path path = null;
		try {
			String fileName = System.currentTimeMillis() + "_" + segmentId + "_LabReport";
			File tempPDF = File.createTempFile(fileName, "pdf");
			try (FileOutputStream fileOutputStream = new FileOutputStream(tempPDF);
				 ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();) {
				LabPDFCreator labPDFCreator = new LabPDFCreator(fileOutputStream, String.valueOf(segmentId), null);
				labPDFCreator.printPdf();
				labPDFCreator.addEmbeddedDocuments(tempPDF, byteOutputStream);
				path = nioFileManager.saveTempFile("temporaryPDF" + new Date().getTime(), byteOutputStream);
			}
			tempPDF.delete();
		} catch (IOException | DocumentException e) {
			throw new PDFGenerationException("Error Details: Lab [" + getDisplayLabName(segmentId) + "] could not be converted into a PDF", e);
		}

		return path;
	}

	private void checkPrivilege(LoggedInInfo loggedInInfo, String privilege) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_lab", privilege, null)) {
			throw new RuntimeException("missing required security object (_lab)");
		}
	}

	private String getDisplayLabName(Integer segmentId) {
		Hl7TextInfo hl7TextInfo = hl7textInfoDao.findLabId(segmentId);
		return StringUtils.isNullOrEmpty(hl7TextInfo.getDiscipline()) ? "UNLABELLED" : hl7TextInfo.getDiscipline();
	}
}
