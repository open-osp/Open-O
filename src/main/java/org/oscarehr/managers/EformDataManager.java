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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oscar.dms.ConvertToEdoc;
import oscar.dms.EDoc;
import oscar.eform.EFormUtil;
import oscar.eform.data.EForm;
import oscar.log.LogAction;

@Service
public class EformDataManager {

	@Autowired
	SecurityInfoManager securityInfoManager;
	
	@Autowired
	EFormDataDao eFormDataDao;
	
	@Autowired
	DocumentManager documentManager;
	
	public EformDataManager() {
		// Default
	}
	
	public Integer saveEformData( LoggedInInfo loggedInInfo, EForm eform ) {
		Integer formid = null;
		
		if ( ! securityInfoManager.hasPrivilege(loggedInInfo, "_eform", SecurityInfoManager.UPDATE, null)) {
			throw new RuntimeException("missing required security object (_eform)");
		}

		EFormData eFormData = EFormUtil.toEFormData( eform );
		eFormDataDao.persist( eFormData );
		formid = eFormData.getId();
		
		if( formid != null ) {
			LogAction.addLogSynchronous(loggedInInfo, "EformDataManager.saveEformData", "Saved EformDataID=" + formid);
		} else {
			LogAction.addLogSynchronous(loggedInInfo, "EformDataManager.saveEformData", "Failed to save eform EformDataID=" + formid);
		}
		
		return formid;
	}
	
	/**
	 * Saves an form as PDF EDoc. 
	 * Returns the Eform id that was saved.
	 */
	public Integer saveEformDataAsEDoc( LoggedInInfo loggedInInfo, String fdid ) {
		
		if ( ! securityInfoManager.hasPrivilege(loggedInInfo, "_eform", SecurityInfoManager.UPDATE, null)) {
			throw new RuntimeException("missing required security object (_eform)");
		}
		Integer documentId = null;
		Integer formid = null;
		
		if( fdid != null ) {
			formid = Integer.parseInt( fdid );
			EFormData eformData = eFormDataDao.find( formid );			
			EDoc edoc = ConvertToEdoc.from( eformData );
			documentManager.moveDocumentToOscarDocuments( loggedInInfo, edoc.getDocument(), edoc.getFilePath());
			edoc.setFilePath(null);
			documentId = documentManager.saveDocument( loggedInInfo, edoc );
		}
		
		if( documentId != null ) {
			LogAction.addLogSynchronous(loggedInInfo, "EformDataManager.saveEformDataAsEDoc", "Document ID saved: " + documentId );
		} else {
			LogAction.addLogSynchronous(loggedInInfo, "EformDataManager.saveEformDataAsEDoc", "Document conversion for Eform id: " + formid + " failed.");
		}
		
		return documentId;
	}
	
	/**
	 * Saves an form as PDF in a temp directory.
	 *  
	 * Path to a temp file is returned. Remember to change the .tmp filetype and to delete the tmp file when finished. 
	 */
	public Path createEformPDF( LoggedInInfo loggedInInfo, int fdid ) {
		
		if ( ! securityInfoManager.hasPrivilege(loggedInInfo, "_eform", SecurityInfoManager.UPDATE, null)) {
			throw new RuntimeException("missing required security object (_eform)");
		}

		EFormData eformData = eFormDataDao.find( fdid );			
		Path path = ConvertToEdoc.saveAsTempPDF(eformData);
		
		if( Files.isReadable(path) ) {
			LogAction.addLogSynchronous(loggedInInfo, "EformDataManager.saveEformDataAsPDF", "Document saved at " + path.toString() );
		} else {
			LogAction.addLogSynchronous(loggedInInfo, "EformDataManager.saveEformDataAsPDF", "Document failed to save for eform id " + fdid);
		}
		
		return path;
	}
	
	
    /**
     * Get all current eForms by demographic number but do not include the HTML data. 
     * This is a good method for getting just the list and status of eForms. It's a little lighter on the database.
     * 
     * Returns a map - not an entity
     */
    public List<Map<String,Object>> findCurrentByDemographicIdNoData(LoggedInInfo loggedInInfo, Integer demographicId){
    	
  		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_eform", SecurityInfoManager.READ, null)) {
			throw new RuntimeException("missing required security object (_eform)");
		}
    	
    	List<Map<String,Object>> results = eFormDataDao.findByDemographicIdCurrentNoData(demographicId, Boolean.TRUE);
    	
    	if (results != null && results.size() > 0) {
			LogAction.addLogSynchronous(loggedInInfo, "FormsManager.findCurrentByDemographicIdNoData", "demo" + demographicId);
		}

		return results;    	
    }

}
