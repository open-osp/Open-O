/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
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
 *
 * Modifications made by Magenta Health in 2024.
 */

package org.oscarehr.ws.rest.conversion;

import org.apache.logging.log4j.Logger;
import org.oscarehr.common.model.Drug;
import org.oscarehr.managers.DrugLookUp;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.model.DrugSearchTo1;
import org.oscarehr.ws.rest.to.model.DrugTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.oscarehr.common.model.Drug;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.oscarehr.ws.rest.to.model.DrugTo1;


public interface DrugConverter {
       
    public Drug getAsDomainObject(LoggedInInfo loggedInInfo, DrugTo1 t) throws ConversionException;
    public DrugTo1 getAsTransferObject(LoggedInInfo loggedInInfo, Drug d) throws ConversionException;
	public List<DrugTo1> getAllAsTransferObjects(LoggedInInfo loggedInInfo, List<Drug> ds) throws ConversionException;

}
