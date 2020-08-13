/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.olis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.oscarehr.common.model.AbstractModel;

import java.util.Date;

@Entity(name = "olis_request_nomenclature")
public class OLISRequestNomenclature extends AbstractModel<Integer> {
	@Id
    @Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
    @Column(name = "nomenclature_version_id")
	private Integer version;
    @Column(name = "olis_test_request_code")
	private String requestCode;
    @Column(name = "external_test_request_code")
    private String externalTestRequestCode;
    @Column(name = "external_source")
    private String externalSource;
    @Column(name = "test_request_name")
    private String testRequestName;
    @Column(name = "request_alternate_name_1")
    private String requestAlternateName1;
    @Column(name = "request_alternate_name_2")
    private String requestAlternateName2;
    @Column(name = "request_alternate_name_3")
    private String requestAlternateName3;
    @Column(name = "comments")
    private String comments;
    @Column(name = "test_request_category")
    private String testRequestCategory;
    @Column(name = "test_request_sub_category")
    private String testRequestSubCategory;
    @Column(name = "reportable_indicator")
    private String reportableIndicator;
    @Column(name = "reportable_context")
    private String reportableContext;
    @Column(name = "external_code_version")
    private String externalCodeVersion;
    @Column(name = "effective_date")
    private String effectiveDate;
    @Column(name = "end_date")
    private String endDate;
    @Column(name = "workflow_status_indicator")
    private String workflowStatusIndicator;
    @Column(name = "validation_status_indicator")
    private String validationStatusIndicator;
    @Column(name = "registration_status_indicator")
    private String registrationStatusIndicator;
    @Column(name = "change_note")
    private String changeNote;
    @Column(name = "documentext")
    private String documentext;
    @Column(name = "description")
    private String description;
    @Column(name = "anonymous_testing")
    private String anonymousTesting;
    @Column(name = "sort_key")
    private String sortKey;
    @Column(name = "report_category")
    private String category;
    @Column(name = "update_date")
    private Date updateDate;

    @Override
    public Integer getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public String getExternalTestRequestCode() {
        return externalTestRequestCode;
    }

    public String getExternalSource() {
        return externalSource;
    }

    public String getTestRequestName() {
        return testRequestName;
    }

    public String getRequestAlternateName1() {
        return requestAlternateName1;
    }

    public String getRequestAlternateName2() {
        return requestAlternateName2;
    }

    public String getRequestAlternateName3() {
        return requestAlternateName3;
    }

    public String getComments() {
        return comments;
    }

    public String getTestRequestCategory() {
        return testRequestCategory;
    }

    public String getTestRequestSubCategory() {
        return testRequestSubCategory;
    }

    public String getReportableIndicator() {
        return reportableIndicator;
    }

    public String getReportableContext() {
        return reportableContext;
    }

    public String getExternalCodeVersion() {
        return externalCodeVersion;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getWorkflowStatusIndicator() {
        return workflowStatusIndicator;
    }

    public String getValidationStatusIndicator() {
        return validationStatusIndicator;
    }

    public String getRegistrationStatusIndicator() {
        return registrationStatusIndicator;
    }

    public String getChangeNote() {
        return changeNote;
    }

    public String getDocumentext() {
        return documentext;
    }

    public String getDescription() {
        return description;
    }

    public String getAnonymousTesting() {
        return anonymousTesting;
    }

    public String getSortKey() {
        return sortKey;
    }

    public String getCategory() {
        return category;
    }

    public Date getUpdateDate() {
        return updateDate;
    }
}
