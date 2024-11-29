/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.managers;

import org.oscarehr.common.dao.DigitalSignatureDao;
import org.oscarehr.common.model.DigitalSignature;
import org.oscarehr.util.DigitalSignatureUtils;
import org.oscarehr.util.EncryptionUtils;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Objects;

@Service
@Transactional
public class DigitalSignatureManagerImpl implements DigitalSignatureManager {

    private final DigitalSignatureDao digitalSignatureDao;

    @Autowired
    public DigitalSignatureManagerImpl(DigitalSignatureDao digitalSignatureDao) {
        this.digitalSignatureDao = digitalSignatureDao;
    }


    @Override
    public DigitalSignature getDigitalSignature(int id) {
        DigitalSignature digitalSignature = this.digitalSignatureDao.findDetached(id);

        if (Objects.isNull(digitalSignature.getSignatureImage())) {
            return null;
        }

        try {
            digitalSignature.setSignatureImage(EncryptionUtils.decrypt(digitalSignature.getSignatureImage()));
        } catch (Exception e) {

            // the data is not encrypted, fetching attached entity, encrypt and save it for future use
            try {
                digitalSignature = this.digitalSignatureDao.find(id);
                digitalSignature.setSignatureImage(EncryptionUtils.encrypt(digitalSignature.getSignatureImage()));
                digitalSignatureDao.merge(digitalSignature);
                return this.getDigitalSignature(id);
            } catch (Exception ex) {
                return digitalSignature;
            }
        }

        return digitalSignature;
    }

    @Override
    public DigitalSignature saveDigitalSignature(Integer facilityId, String providerNo, Integer demographicNo, byte[] imageData) {
        DigitalSignature digitalSignature = new DigitalSignature();
        digitalSignature.setDateSigned(new Date());
        digitalSignature.setDemographicId(demographicNo);
        digitalSignature.setFacilityId(facilityId);
        digitalSignature.setProviderNo(providerNo);

        try {
            digitalSignature.setSignatureImage(EncryptionUtils.encrypt(imageData));
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting and saving digital signature.", e);
        }

        digitalSignatureDao.persist(digitalSignature);
        logger.debug("Signature saved to database with ID: {}", digitalSignature.getId());

        return digitalSignature;
    }

    @Override
    public DigitalSignature processAndSaveDigitalSignature(LoggedInInfo loggedInInfo, String signatureRequestId, Integer demographicNo) {

        if (loggedInInfo.getCurrentFacility().isEnableDigitalSignatures()) {
            String filename = DigitalSignatureUtils.getTempFilePath(signatureRequestId);
            if (filename.isEmpty()) {
                return null;
            }
            try (FileInputStream fileInputStream = new FileInputStream(filename)) {
                byte[] image = new byte[1024 * 256];
                fileInputStream.read(image);

                return this.saveDigitalSignature(loggedInInfo.getCurrentFacility().getId(),
                        loggedInInfo.getLoggedInProviderNo(), demographicNo, image);
            } catch (FileNotFoundException e) {
                logger.debug("Signature file not found. User probably didn't collect a signature.", e);
            } catch (Exception e) {
                logger.error("UnexpectedError.", e);
            }
        }

        return null;
    }

}
