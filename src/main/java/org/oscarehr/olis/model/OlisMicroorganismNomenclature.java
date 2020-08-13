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
package org.oscarehr.olis.model;

import org.oscarehr.common.model.AbstractModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "olis_microorganism_nomenclature")
public class OlisMicroorganismNomenclature extends AbstractModel<Integer> {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "microorganism_code")
    private String microorganismCode;
    @Column(name = "microorganism_type")
    private String microorganismType;
    @Column(name = "taxonomic_level")
    private String taxonomicLevel;
    @Column(name = "microorganism_name")
    private String microorganismName;
    @Column(name = "alternate_name_1")
    private String alternateName1;
    @Column(name = "alternate_name_2")
    private String alternateName2;
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "source")
    private String source;
    @Column(name = "external_link")
    private String externalLink;
    @Column(name = "reportable")
    private String reportable;
    @Column(name = "reportable_context")
    private String reportableContext;
    @Column(name = "effective_start_date")
    private String effectiveStartDate;
    @Column(name = "effective_end_date")
    private String effectiveEndDate;
    @Column(name = "change_note")
    private String changeNote;
    @Column(name = "comments")
    private String comments;
    @Column(name = "workflow_status_indicator")
    private String workflowStatusIndicator;
    @Column(name = "validation_status_indicator")
    private String validationStatusIndicator;

    @Override
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getMicroorganismCode() {
        return microorganismCode;
    }
    public void setMicroorganismCode(String microorganismCode) {
        this.microorganismCode = microorganismCode;
    }

    public String getMicroorganismType() {
        return microorganismType;
    }
    public void setMicroorganismType(String microorganismType) {
        this.microorganismType = microorganismType;
    }

    public String getTaxonomicLevel() {
        return taxonomicLevel;
    }
    public void setTaxonomicLevel(String taxonomicLevel) {
        this.taxonomicLevel = taxonomicLevel;
    }

    public String getMicroorganismName() {
        return microorganismName;
    }
    public void setMicroorganismName(String microorganismName) {
        this.microorganismName = microorganismName;
    }

    public String getAlternateName1() {
        return alternateName1;
    }
    public void setAlternateName1(String alternateName1) {
        this.alternateName1 = alternateName1;
    }

    public String getAlternateName2() {
        return alternateName2;
    }
    public void setAlternateName2(String alternateName2) {
        this.alternateName2 = alternateName2;
    }

    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }

    public String getExternalLink() {
        return externalLink;
    }
    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String getReportable() {
        return reportable;
    }
    public void setReportable(String reportable) {
        this.reportable = reportable;
    }

    public String getReportableContext() {
        return reportableContext;
    }
    public void setReportableContext(String reportableContext) {
        this.reportableContext = reportableContext;
    }

    public String getEffectiveStartDate() {
        return effectiveStartDate;
    }
    public void setEffectiveStartDate(String effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public String getEffectiveEndDate() {
        return effectiveEndDate;
    }
    public void setEffectiveEndDate(String effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    public String getChangeNote() {
        return changeNote;
    }
    public void setChangeNote(String changeNote) {
        this.changeNote = changeNote;
    }

    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getWorkflowStatusIndicator() {
        return workflowStatusIndicator;
    }
    public void setWorkflowStatusIndicator(String workflowStatusIndicator) {
        this.workflowStatusIndicator = workflowStatusIndicator;
    }

    public String getValidationStatusIndicator() {
        return validationStatusIndicator;
    }
    public void setValidationStatusIndicator(String validationStatusIndicator) {
        this.validationStatusIndicator = validationStatusIndicator;
    }
}
