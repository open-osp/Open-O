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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.enumerator.DocumentType;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.PDFGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.oscarehr.documentManager.ConvertToEdoc;
import org.oscarehr.documentManager.DocumentAttachmentManager;
import org.oscarehr.documentManager.EDoc;
import org.oscarehr.hospitalReportManager.HRMUtil;

import oscar.eform.EFormUtil;
import oscar.eform.data.EForm;
import oscar.log.LogAction;
import oscar.oscarEncounter.data.EctFormData;

public interface EformDataManager{
	
	public Integer saveEformData( LoggedInInfo loggedInInfo, EForm eform );
	
	/**
	 * Saves an form as PDF EDoc. 
	 * Returns the Eform id that was saved.
	 */
	public Integer saveEformDataAsEDoc( LoggedInInfo loggedInInfo, String fdid );

	public Integer saveEFormWithAttachmentsAsEDoc(LoggedInInfo loggedInInfo, String fdid, String demographicId, Path eFormPDFPath) throws PDFGenerationException;
	
	public EFormData findByFdid(LoggedInInfo loggedInInfo, Integer fdid);
	
	/**
	 * Saves an form as PDF in a temp directory.
	 *  
	 * Path to a temp file is returned. Remember to change the .tmp filetype and to delete the tmp file when finished. 
	 */
	public Path createEformPDF( LoggedInInfo loggedInInfo, int fdid ) throws PDFGenerationException;
	
	
    /**
     * Get all current eForms by demographic number but do not include the HTML data. 
     * This is a good method for getting just the list and status of eForms. It's a little lighter on the database.
     * 
     * Returns a map - not an entity
     */
    public List<Map<String,Object>> findCurrentByDemographicIdNoData(LoggedInInfo loggedInInfo, Integer demographicId);

	public ArrayList<HashMap<String, ? extends Object>> getHRMDocumentsAttachedToEForm(LoggedInInfo loggedInInfo, String fdid, String demographicId);

	public List<EctFormData.PatientForm> getFormsAttachedToEForm(LoggedInInfo loggedInInfo, String fdid, String demographicId);

	public void removeEFormData(LoggedInInfo loggedInInfo, String fdid);

}
