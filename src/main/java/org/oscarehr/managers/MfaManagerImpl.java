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

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.oscarehr.common.dao.SecurityDao;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.EncryptionUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.QrCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.IllegalStateException;
import java.io.IOException;
import java.util.Base64;

@Service
public class MfaManagerImpl implements MfaManager {

    private final SecurityDao securityDao;
    private final SecurityManager securityManager;

    @Autowired
    public MfaManagerImpl(SecurityDao securityDao, SecurityManager securityManager) {
        this.securityDao = securityDao;
        this.securityManager = securityManager;
    }

    @Override
    public String getTotpUrl(String appName, String username, String secret) {
        return String.format(TOTP_URL_FORMAT, appName, username, secret, appName);
    }

    @Override
    public byte[] getQRCodeImageData(String appName, String username, String secret) {
        try {
            String totpUrl = this.getTotpUrl(appName, username, secret);
            return QrCodeUtils.toSingleQrCodePng(totpUrl, ErrorCorrectionLevel.H, 4);
        } catch (IOException | WriterException e) {
            throw new RuntimeException("Error while generating QR code image data", e);
        }
    }

    @Override
    public String getQRCodeImageData(Integer securityId, String secret) {
        Security security = this.securityDao.find(securityId);
        if (security == null) {
            return null;
        }

        byte[] qrCodeImageData = this.getQRCodeImageData("OpenOSP", security.getUserName(), secret);
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrCodeImageData);
    }

    @Override
    public boolean isMfaRegistrationRequired(Integer securityId) throws IllegalStateException {
        Security security = this.securityDao.find(securityId);
        if (security == null) {
            throw new IllegalStateException("Security not found");
        }
        return security.isMfaRegistrationNeeded();
    }

    @Override
    public void saveMfaSecret(LoggedInInfo loggedInInfo, Security security, String mfaSecret) throws Exception {
        security.setMfaSecret(EncryptionUtils.encrypt(mfaSecret));
        this.securityManager.updateSecurityRecord(loggedInInfo, security);
    }

    @Override
    public String getMfaSecret(Security security) throws Exception {
        return EncryptionUtils.decrypt(security.getMfaSecret());
    }

    @Override
    public void resetMfaSecret(LoggedInInfo loggedInInfo, Security security) {
        security.setMfaSecret(null);
        this.securityManager.updateSecurityRecord(loggedInInfo, security);
    }

}
