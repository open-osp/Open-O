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
import org.oscarehr.common.model.Prescription;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.model.PrescriptionTo1;
import org.springframework.stereotype.Component;
import java.util.List;


public interface PrescriptionConverter {

    public Prescription getAsDomainObject(LoggedInInfo loggedInInfo, PrescriptionTo1 t);
    public PrescriptionTo1 getAsTransferObject(LoggedInInfo loggedInInfo, Prescription p);
    public List<PrescriptionTo1> getAllAsTransferObjects(LoggedInInfo loggedInInfo, List<Prescription> ds) throws ConversionException;
    
}
