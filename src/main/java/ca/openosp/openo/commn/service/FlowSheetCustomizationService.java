//CHECKSTYLE:OFF
/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
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
 * <p>
 * Modifications made by Magenta Health in 2024.
 */
package ca.openosp.openo.commn.service;

import ca.openosp.openo.commn.dao.FlowSheetCustomizationDao;
import ca.openosp.openo.commn.model.FlowSheetCustomization;
import ca.openosp.openo.managers.SecurityInfoManager;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.SpringUtils;

/**
 * Service layer for FlowSheetCustomization cascading rules.
 *
 * <p>Enforces scope hierarchy where higher levels (clinic > provider > patient)
 * block customizations at lower levels. When a higher level creates a customization,
 * lower levels cannot override it.</p>
 *
 * @since 2025-12-23
 */
public class FlowSheetCustomizationService {

    private FlowSheetCustomizationDao flowSheetCustomizationDao;
    private SecurityInfoManager securityInfoManager;

    /**
     * Gets the FlowSheetCustomizationDao lazily to avoid initialization issues.
     */
    private FlowSheetCustomizationDao getFlowSheetCustomizationDao() {
        if (flowSheetCustomizationDao == null) {
            flowSheetCustomizationDao = SpringUtils.getBean(FlowSheetCustomizationDao.class);
        }
        return flowSheetCustomizationDao;
    }

    /**
     * Gets the SecurityInfoManager lazily to avoid initialization issues.
     */
    private SecurityInfoManager getSecurityInfoManager() {
        if (securityInfoManager == null) {
            securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
        }
        return securityInfoManager;
    }

    /**
     * Result object for cascading validation checks.
     */
    public static class CascadeCheckResult {
        private final boolean blocked;
        private final String blockingLevel;
        private final FlowSheetCustomization blockingCustomization;

        private CascadeCheckResult(boolean blocked, String blockingLevel, FlowSheetCustomization blockingCustomization) {
            this.blocked = blocked;
            this.blockingLevel = blockingLevel;
            this.blockingCustomization = blockingCustomization;
        }

        /**
         * Creates a result indicating the operation is allowed.
         *
         * @return CascadeCheckResult with blocked=false
         */
        public static CascadeCheckResult allowed() {
            return new CascadeCheckResult(false, null, null);
        }

        /**
         * Creates a result indicating the operation is blocked.
         *
         * @param blockingLevel the scope level that is blocking (clinic or provider)
         * @param blockingCustomization the customization that is blocking
         * @return CascadeCheckResult with blocked=true
         */
        public static CascadeCheckResult blocked(String blockingLevel, FlowSheetCustomization blockingCustomization) {
            return new CascadeCheckResult(true, blockingLevel, blockingCustomization);
        }

        /**
         * Returns whether the operation is blocked.
         *
         * @return true if blocked by a higher-level customization
         */
        public boolean isBlocked() {
            return blocked;
        }

        /**
         * Returns the scope level that is blocking.
         *
         * @return "clinic" or "provider", or null if not blocked
         */
        public String getBlockingLevel() {
            return blockingLevel;
        }

        /**
         * Returns the customization that is blocking.
         *
         * @return the blocking FlowSheetCustomization, or null if not blocked
         */
        public FlowSheetCustomization getBlockingCustomization() {
            return blockingCustomization;
        }
    }

    /**
     * Checks if an action is blocked by a higher-level customization.
     *
     * @param flowsheet the flowsheet name
     * @param measurement the measurement name
     * @param action the action type (ADD, UPDATE, DELETE)
     * @param providerNo current provider number (empty string for clinic scope)
     * @param demographicNo current demographic number ("0" for clinic/provider scope)
     * @return CascadeCheckResult indicating whether the action is blocked
     */
    public CascadeCheckResult checkCascadingBlocked(
            String flowsheet, String measurement, String action,
            String providerNo, String demographicNo) {

        FlowSheetCustomization blocking = getFlowSheetCustomizationDao().findHigherLevelCustomization(
            flowsheet, measurement, action, providerNo, demographicNo);

        if (blocking == null) {
            return CascadeCheckResult.allowed();
        }

        String blockingLevel = getScopeLevelName(blocking);
        return CascadeCheckResult.blocked(blockingLevel, blocking);
    }

    /**
     * Determines the scope level name for a customization.
     *
     * @param cust the FlowSheetCustomization to check
     * @return "clinic", "provider", or "patient"
     */
    public String getScopeLevelName(FlowSheetCustomization cust) {
        if ("".equals(cust.getProviderNo()) && "0".equals(cust.getDemographicNo())) {
            return "clinic";
        } else if (!"0".equals(cust.getDemographicNo())) {
            return "patient";
        } else {
            return "provider";
        }
    }

    /**
     * Validates that the user has permission for the requested scope.
     *
     * <p>Clinic-level operations require admin privileges.</p>
     *
     * @param loggedInInfo the logged-in user information
     * @param scope the requested scope ("clinic", "provider", or patient demographic)
     * @throws SecurityException if clinic scope requested without admin role
     */
    public void validateScopePermission(LoggedInInfo loggedInInfo, String scope) {
        if ("clinic".equals(scope)) {
            if (!getSecurityInfoManager().hasPrivilege(loggedInInfo, "_admin", "w", null)) {
                throw new SecurityException("Clinic-level customization requires admin privileges");
            }
        }
    }

    /**
     * Checks if a customization can be archived at the current scope level.
     *
     * <p>Users cannot archive customizations created at a higher scope level.</p>
     *
     * @param cust the customization to check
     * @param currentScope the current scope ("clinic", "provider", or patient demographic)
     * @param currentProviderNo the current provider number
     * @param currentDemographicNo the current demographic number
     * @return CascadeCheckResult indicating whether archiving is blocked
     */
    public CascadeCheckResult checkCanArchive(
            FlowSheetCustomization cust,
            String currentScope, String currentProviderNo, String currentDemographicNo) {

        String custScope = getScopeLevelName(cust);
        boolean isCurrentPatientScope = currentDemographicNo != null && !"0".equals(currentDemographicNo);
        boolean isCurrentProviderScope = !isCurrentPatientScope && !"clinic".equals(currentScope);

        boolean isHigherLevel = false;

        if (isCurrentPatientScope) {
            // Patient scope - cannot archive clinic or provider level
            isHigherLevel = "clinic".equals(custScope) || "provider".equals(custScope);
        } else if (isCurrentProviderScope) {
            // Provider scope - cannot archive clinic level
            isHigherLevel = "clinic".equals(custScope);
        }

        if (isHigherLevel) {
            return CascadeCheckResult.blocked(custScope, cust);
        }

        return CascadeCheckResult.allowed();
    }
}
