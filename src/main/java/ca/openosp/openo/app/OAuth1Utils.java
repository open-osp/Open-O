// src/main/java/org/oscarehr/app/OAuth1Utils.java
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
package ca.openosp.openo.app;

import java.util.List;

import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.apache.logging.log4j.Logger;
import ca.openosp.openo.commn.model.AppDefinition;
import ca.openosp.openo.commn.model.AppUser;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;

/**
 * Deprecated shim for legacy OAuth1 outbound helpers.
 * The Scribe-based client flow has been removed; these methods now
 * log and return null to preserve binary compatibility.
 */
@Deprecated
public final class OAuth1Utils {
    private static final Logger logger = MiscUtils.getLogger();
    private static final JSONProvider<Object> jsonProvider = new JSONProvider<>();

    static {
        // keep previous CXF consumer behavior for callers that still pass this around
        jsonProvider.setDropRootElement(true);
    }

    private OAuth1Utils() {}

    /** Provide a CXF JSONProvider (legacy callers expect this). */
    public static List<Object> getProviderK2A() {
        return List.of(jsonProvider);
    }

    // --- static wrappers preserved for compatibility ---

    public static String getOAuthGetResponse(
        LoggedInInfo info,
        AppDefinition app,
        AppUser user,
        String requestURI,
        String baseRequestURI
    ) {
        logger.warn("OAuth1Utils.getOAuthGetResponse called, but OAuth1 client is disabled. URI={}", requestURI);
        return null;
    }

    public static String getOAuthPostResponse(
        LoggedInInfo info,
        AppDefinition app,
        AppUser user,
        String requestURI,
        String baseRequestURI,
        List<Object> providers,
        Object payload
    ) {
        logger.warn("OAuth1Utils.getOAuthPostResponse called, but OAuth1 client is disabled. URI={}", requestURI);
        return null;
    }

    public static void getOAuthDeleteResponse(
        AppDefinition app,
        AppUser user,
        String requestURI,
        String auditURI
    ) {
        logger.warn("OAuth1Utils.getOAuthDeleteResponse called, but OAuth1 client is disabled. URI={}", requestURI);
    }
}
