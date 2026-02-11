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

import ca.openosp.openo.commn.model.Tickler;
import ca.openosp.openo.utility.LocaleUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Data Transfer Object for Tickler list views.
 * <p>
 * This DTO provides a lightweight representation of tickler data optimized for list display,
 * reducing database queries from ~25 per page load (due to EAGER relationships) to a small, fixed set
 * of batched queries (currently loading ticklers, their comments, and their links).
 * It contains only the fields needed for display in ticklerMain.jsp.
 * </p>
 *
 * @since 2026-01-30
 */
public class TicklerListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String NOT_APPLICABLE = "N/A";

    private Integer id;
    private String message;
    private Date serviceDate;
    private Date createDate;
    private Tickler.STATUS status;
    private Tickler.PRIORITY priority;
    private Integer demographicNo;
    private String demographicLastName;
    private String demographicFirstName;
    private String creatorLastName;
    private String creatorFirstName;
    private String assigneeLastName;
    private String assigneeFirstName;
    private List<TicklerCommentDTO> comments;
    private List<TicklerLinkDTO> links;

    /**
     * Default constructor required for frameworks.
     */
    public TicklerListDTO() {
        this.comments = new ArrayList<>();
        this.links = new ArrayList<>();
    }

    /**
     * Constructor for JPQL projection queries.
     *
     * @param id Integer the tickler ID
     * @param message String the tickler message content
     * @param serviceDate Date the service/due date
     * @param createDate Date the creation date
     * @param status Tickler.STATUS the tickler status (A, C, D)
     * @param priority Tickler.PRIORITY the priority level
     * @param demographicNo Integer the patient demographic number
     * @param demographicLastName String the patient's last name
     * @param demographicFirstName String the patient's first name
     * @param creatorLastName String the creator provider's last name
     * @param creatorFirstName String the creator provider's first name
     * @param assigneeLastName String the assigned provider's last name
     * @param assigneeFirstName String the assigned provider's first name
     */
    public TicklerListDTO(Integer id, String message, Date serviceDate, Date createDate,
                          Tickler.STATUS status, Tickler.PRIORITY priority,
                          Integer demographicNo, String demographicLastName, String demographicFirstName,
                          String creatorLastName, String creatorFirstName,
                          String assigneeLastName, String assigneeFirstName) {
        this.id = id;
        this.message = message;
        this.serviceDate = serviceDate;
        this.createDate = createDate;
        this.status = status;
        this.priority = priority;
        this.demographicNo = demographicNo;
        this.demographicLastName = demographicLastName;
        this.demographicFirstName = demographicFirstName;
        this.creatorLastName = creatorLastName;
        this.creatorFirstName = creatorFirstName;
        this.assigneeLastName = assigneeLastName;
        this.assigneeFirstName = assigneeFirstName;
        this.comments = new ArrayList<>();
        this.links = new ArrayList<>();
    }

    /**
     * Returns the formatted demographic name in "LastName, FirstName" format.
     *
     * @return String the formatted name or empty string if both names are null
     */
    public String getDemographicFormattedName() {
        if (demographicLastName == null && demographicFirstName == null) {
            return "";
        }
        if (demographicLastName == null) {
            return demographicFirstName;
        }
        if (demographicFirstName == null) {
            return demographicLastName;
        }
        return demographicLastName + ", " + demographicFirstName;
    }

    /**
     * Returns the formatted creator name in "LastName, FirstName" format.
     *
     * @return String the formatted name or "N/A" if creator is null
     */
    public String getCreatorFormattedName() {
        if (creatorLastName == null && creatorFirstName == null) {
            return NOT_APPLICABLE;
        }
        if (creatorLastName == null) {
            return creatorFirstName;
        }
        if (creatorFirstName == null) {
            return creatorLastName;
        }
        return creatorLastName + ", " + creatorFirstName;
    }

    /**
     * Returns the formatted assignee name in "LastName, FirstName" format.
     *
     * @return String the formatted name or "N/A" if assignee is null
     */
    public String getAssigneeFormattedName() {
        if (assigneeLastName == null && assigneeFirstName == null) {
            return NOT_APPLICABLE;
        }
        if (assigneeLastName == null) {
            return assigneeFirstName;
        }
        if (assigneeFirstName == null) {
            return assigneeLastName;
        }
        return assigneeLastName + ", " + assigneeFirstName;
    }

    /**
     * Returns the localized status description.
     *
     * @param locale Locale the locale for message lookup
     * @return String the localized status description
     */
    public String getStatusDesc(Locale locale) {
        String statusStr = "";
        if (status == null) {
            return statusStr;
        }
        if (status.equals(Tickler.STATUS.A)) {
            statusStr = LocaleUtils.getMessage(locale, "tickler.ticklerMain.stActive");
        } else if (status.equals(Tickler.STATUS.C)) {
            statusStr = LocaleUtils.getMessage(locale, "tickler.ticklerMain.stComplete");
        } else if (status.equals(Tickler.STATUS.D)) {
            statusStr = LocaleUtils.getMessage(locale, "tickler.ticklerMain.stDeleted");
        }
        return statusStr;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        if (message == null) {
            return "";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Tickler.STATUS getStatus() {
        return status;
    }

    public void setStatus(Tickler.STATUS status) {
        this.status = status;
    }

    public Tickler.PRIORITY getPriority() {
        return priority;
    }

    public void setPriority(Tickler.PRIORITY priority) {
        this.priority = priority;
    }

    public Integer getDemographicNo() {
        return demographicNo;
    }

    public void setDemographicNo(Integer demographicNo) {
        this.demographicNo = demographicNo;
    }

    public String getDemographicLastName() {
        return demographicLastName;
    }

    public void setDemographicLastName(String demographicLastName) {
        this.demographicLastName = demographicLastName;
    }

    public String getDemographicFirstName() {
        return demographicFirstName;
    }

    public void setDemographicFirstName(String demographicFirstName) {
        this.demographicFirstName = demographicFirstName;
    }

    public String getCreatorLastName() {
        return creatorLastName;
    }

    public void setCreatorLastName(String creatorLastName) {
        this.creatorLastName = creatorLastName;
    }

    public String getCreatorFirstName() {
        return creatorFirstName;
    }

    public void setCreatorFirstName(String creatorFirstName) {
        this.creatorFirstName = creatorFirstName;
    }

    public String getAssigneeLastName() {
        return assigneeLastName;
    }

    public void setAssigneeLastName(String assigneeLastName) {
        this.assigneeLastName = assigneeLastName;
    }

    public String getAssigneeFirstName() {
        return assigneeFirstName;
    }

    public void setAssigneeFirstName(String assigneeFirstName) {
        this.assigneeFirstName = assigneeFirstName;
    }

    public List<TicklerCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<TicklerCommentDTO> comments) {
        this.comments = comments != null ? new ArrayList<>(comments) : new ArrayList<>();
    }

    public List<TicklerLinkDTO> getLinks() {
        return links;
    }

    public void setLinks(List<TicklerLinkDTO> links) {
        this.links = links != null ? new ArrayList<>(links) : new ArrayList<>();
    }
}
