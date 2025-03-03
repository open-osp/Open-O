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

import org.jboss.aerogear.security.otp.api.Base32;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.LoggedInInfo;

import javax.jms.IllegalStateException;

public interface MfaManager {

    String TOTP_URL_FORMAT = "otpauth://totp/%s:%s?secret=%s&issuer=%s";

    default String generateMfaSecret() {
        return Base32.random();
    }

    boolean isMfaRegistrationRequired(Integer securityId) throws IllegalStateException;

    String getTotpUrl(String email, String secret, String appName);

    byte[] getQRCodeImageData(String email, String secret, String appName);

    String getQRCodeImageData(Integer securityId, String secret);

    void saveMfaSecret(LoggedInInfo loggedInInfo, Security security, String mfaSecret) throws Exception;

    String getMfaSecret(Security security) throws Exception;

    void resetMfaSecret(LoggedInInfo loggedInInfo, Security security);
}
