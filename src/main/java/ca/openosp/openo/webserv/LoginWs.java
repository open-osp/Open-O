//CHECKSTYLE:OFF
/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package ca.openosp.openo.webserv;

import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
import org.apache.logging.log4j.Logger;
import ca.openosp.openo.PMmodule.dao.ProviderDao;
import ca.openosp.openo.commn.dao.SecurityDao;
import ca.openosp.openo.commn.model.Provider;
import ca.openosp.openo.commn.model.Security;
import ca.openosp.openo.utility.MiscUtils;
import ca.openosp.openo.utility.NotAuthorisedException;
import ca.openosp.openo.webserv.transfer_objects.LoginResultTransfer;
import ca.openosp.openo.webserv.transfer_objects.LoginResultTransfer2;
import ca.openosp.openo.webserv.transfer_objects.ProviderTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService(targetNamespace = "http://ws.oscarehr.org/")
@Component
@GZIP(threshold = AbstractWs.GZIP_THRESHOLD)
public class LoginWs extends AbstractWs {
    private static final Logger logger = MiscUtils.getLogger();

    @Autowired
    private SecurityDao securityDao = null;

    @Autowired
    private ProviderDao providerDao = null;

    /**
     * @throws NotAuthorisedException if password is incorrect
     * @deprecated 2015-01-28
     * <p>
     * Returns LoginResultTransfer on valid login, will be provided with a sec token too.
     */
    @Deprecated
    public LoginResultTransfer login(String userName, String password) throws NotAuthorisedException {
        logger.info("Login attempt : user=" + userName);
        logger.debug("Login attempt : p =" + password);

        List<Security> securities = securityDao.findByUserName(userName);
        Security security = null;

        if (securities.size() > 0) security = securities.get(0);

        if (WsUtils.checkAuthenticationAndSetLoggedInInfo(getHttpServletRequest(), security, password)) {
            LoginResultTransfer result = new LoginResultTransfer();
            result.setSecurityId(security.getSecurityNo());

            String securityToken = WsUtils.generateSecurityToken(security);
            result.setSecurityTokenKey(securityToken);

            return (result);
        }

        throw (new NotAuthorisedException("Invalid Username/Password"));
    }

    /**
     * @param password can be the users password or a valid token
     * @return LoginResultTransfer2 on valid login, will be provided with a sec token too.
     * @throws NotAuthorisedException if password is incorrect
     */
    public LoginResultTransfer2 login2(String userName, String password) throws NotAuthorisedException {
        logger.info("Login attempt : user=" + userName);
        logger.debug("Login attempt : p =" + password);

        List<Security> securities = securityDao.findByUserName(userName);
        Security security = null;

        if (securities.size() > 0) security = securities.get(0);

        if (WsUtils.checkAuthenticationAndSetLoggedInInfo(getHttpServletRequest(), security, password)) {
            LoginResultTransfer2 result = new LoginResultTransfer2();
            result.setSecurityId(security.getSecurityNo());

            String securityToken = WsUtils.generateSecurityToken(security);
            result.setSecurityTokenKey(securityToken);

            if (security.getProviderNo() != null) {
                Provider provider = providerDao.getProvider(security.getProviderNo());
                result.setProvider(ProviderTransfer.toTransfer(provider));
            }

            return (result);
        }

        throw (new NotAuthorisedException("Invalid Username/Password"));
    }
}
