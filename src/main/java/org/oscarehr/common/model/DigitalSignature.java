/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */
package org.oscarehr.common.model;

import org.oscarehr.common.dao.DigitalSignatureDao;
import org.oscarehr.util.EncryptionUtils;
import org.oscarehr.util.SpringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;

@Entity
public class DigitalSignature extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	/** The facility in which this was captured */
	private Integer facilityId=null;
	
	/** The provider who captured the signature */
	private String providerNo = null;

	/** The client for which this signature belongs */
	private Integer demographicId = null;

	/** The date the signature was captured */
	private Date dateSigned = null;

	/** Image of the signature as a jpg */
	private byte[] signatureImage = null;

	@Override
    public Integer getId() {
		return id;
	}

	public Integer getFacilityId() {
    	return facilityId;
    }

	public void setFacilityId(Integer facilityId) {
    	this.facilityId = facilityId;
    }

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Integer getDemographicId() {
		return demographicId;
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public Date getDateSigned() {
		return dateSigned;
	}

	public void setDateSigned(Date dateSigned) {
		this.dateSigned = dateSigned;
	}

	public byte[] getSignatureImage() {
		if (Objects.isNull(this.signatureImage)) {
			return null;
		}
		try {
			return EncryptionUtils.decrypt(this.signatureImage);
		} catch (Exception e) {

			// the data is not encrypted, encrypt and save it for future use
			try {
				setSignatureImage(this.signatureImage);

				DigitalSignatureDao dao = SpringUtils.getBean(DigitalSignatureDao.class);
				dao.merge(this);

				return this.signatureImage;
			} catch (Exception ex) {
				return this.signatureImage;
			}
		}
	}

	public void setSignatureImage(byte[] signatureImage) {
		try {
			this.signatureImage = EncryptionUtils.encrypt(signatureImage);
		} catch (Exception e) {
			throw new RuntimeException("Error while securely saving Digital Signature.");
		}
	}

}
