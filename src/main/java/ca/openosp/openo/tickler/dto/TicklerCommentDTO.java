//CHECKSTYLE:OFF
/**
 * Copyright (c) 2026. Magenta Health. All Rights Reserved.
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
 */
package ca.openosp.openo.tickler.dto;

import ca.openosp.openo.util.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

/**
 * Data Transfer Object for Tickler comments in list views.
 * <p>
 * This DTO provides a lightweight representation of tickler comment data optimized for display,
 * avoiding the EAGER-loaded Provider relationship on TicklerComment entities.
 * </p>
 *
 * @since 2026-01-30
 */
public class TicklerCommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer ticklerNo;
    private String message;
    private Date updateDate;
    private String providerLastName;
    private String providerFirstName;

    /**
     * Default constructor required for frameworks.
     */
    public TicklerCommentDTO() {
    }

    /**
     * Constructor for JPQL projection queries.
     *
     * @param id Integer the comment ID
     * @param ticklerNo Integer the parent tickler ID
     * @param message String the comment message content
     * @param updateDate Date the comment update/creation date
     * @param providerLastName String the comment author's last name
     * @param providerFirstName String the comment author's first name
     */
    public TicklerCommentDTO(Integer id, Integer ticklerNo, String message, Date updateDate,
                             String providerLastName, String providerFirstName) {
        this.id = id;
        this.ticklerNo = ticklerNo;
        this.message = message;
        this.updateDate = updateDate;
        this.providerLastName = providerLastName;
        this.providerFirstName = providerFirstName;
    }

    /**
     * Returns the formatted provider name in "LastName, FirstName" format.
     *
     * @return String the formatted name or empty string if provider is null
     */
    public String getProviderFormattedName() {
        if (providerLastName == null && providerFirstName == null) {
            return "";
        }
        if (providerLastName == null) {
            return providerFirstName;
        }
        if (providerFirstName == null) {
            return providerLastName;
        }
        return providerLastName + ", " + providerFirstName;
    }

    /**
     * Checks if the update date is today.
     *
     * @return boolean true if the update date is the same day as today
     */
    public boolean isUpdateDateToday() {
        if (updateDate == null) {
            return false;
        }
        return org.apache.commons.lang3.time.DateUtils.isSameDay(updateDate, new Date());
    }

    /**
     * Returns the formatted update time for the current locale.
     *
     * @param locale Locale the locale for formatting
     * @return String the formatted time
     */
    public String getUpdateTime(Locale locale) {
        return DateUtils.formatTime(updateDate, locale);
    }

    /**
     * Returns the formatted update date and time for the current locale.
     *
     * @param locale Locale the locale for formatting
     * @return String the formatted date and time
     */
    public String getUpdateDateTime(Locale locale) {
        return DateUtils.formatDateTime(updateDate, locale);
    }

    /**
     * Returns the formatted update date for the current locale.
     *
     * @param locale Locale the locale for formatting
     * @return String the formatted date
     */
    public String getUpdateDate(Locale locale) {
        return DateUtils.formatDate(updateDate, locale);
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTicklerNo() {
        return ticklerNo;
    }

    public void setTicklerNo(Integer ticklerNo) {
        this.ticklerNo = ticklerNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getProviderLastName() {
        return providerLastName;
    }

    public void setProviderLastName(String providerLastName) {
        this.providerLastName = providerLastName;
    }

    public String getProviderFirstName() {
        return providerFirstName;
    }

    public void setProviderFirstName(String providerFirstName) {
        this.providerFirstName = providerFirstName;
    }
}
