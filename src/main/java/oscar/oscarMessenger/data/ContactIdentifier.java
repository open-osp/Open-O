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
package oscar.oscarMessenger.data;

import java.io.Serializable;

public class ContactIdentifier implements Serializable {

	private String contactId;
	private int facilityId;
	private int clinicLocationNo;
	private String contactUniqueId;
	private String compositeId;
	private int groupId;
	
	public ContactIdentifier() {
		this.contactId = "0";
		this.facilityId = 0;
		this.clinicLocationNo = 0;
		this.groupId = 0;
	}
	
	public ContactIdentifier(String compositeId) {
		this();
		setCompositeId(compositeId);
	}
	
	public String getContactId() {
		return contactId;
	}
	
	public void setContactId(String contactId) {
		setCompositeId(contactId, this.facilityId, this.clinicLocationNo);
	}
	
	public int getFacilityId() {
		return facilityId;
	}
	
	public void setFacilityId(int facilityId) {
		setCompositeId(this.contactId, facilityId, this.clinicLocationNo);
	}

	public int getClinicLocationNo() {
		return clinicLocationNo;
	}

	public void setClinicLocationNo(int clinicLocationNo) {
		setCompositeId(this.contactId, this.facilityId, clinicLocationNo);
	}

	public String getContactUniqueId() {
		return contactUniqueId;
	}
	
	public void setContactUniqueId(String contactUniqueId) {
		this.contactUniqueId = contactUniqueId;
	}

	public String getCompositeId() {
		if(compositeId == null) 
		{
			compositeId = this.contactId + "-" + this.facilityId + "-" + this.clinicLocationNo;
		}
		return compositeId;
	}
	
	public void setCompositeId(String contactId, int facilityId, int clinicLocationNo) {
		setCompositeId(contactId.trim() + "-" + facilityId + "-" + clinicLocationNo);
	}

	/**
	 * A composite id is the ids in this id object separated by a dash 
	 * in the form of a string 
	 * contactId - facilityId - clinicLocationNo - messenger group identifier (groupId)
	 * Defaults for each Id is 0
	 * @param compositeId
	 */
	public void setCompositeId(String compositeId) {
		
		if(compositeId.contains("-"))
		{
	       	String[] theSplit = compositeId.split("-");            	
	        this.contactId = theSplit[0].trim();
	        this.facilityId = Integer.parseInt(theSplit[1].trim());
	        if(theSplit.length == 3)
	        {
	        	this.clinicLocationNo = Integer.parseInt(theSplit[2].trim());
	        }
			if(theSplit.length == 4) {
				this.groupId = Integer.parseInt(theSplit[3].trim());
			}
		}
		else
		{
			this.contactId = compositeId;
		}
		
		this.compositeId = compositeId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
}
