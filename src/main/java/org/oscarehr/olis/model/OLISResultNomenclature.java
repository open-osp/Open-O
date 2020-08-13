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

@Entity(name = "olis_result_nomenclature")
public class OLISResultNomenclature extends AbstractModel<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private String id;
	@Column(name = "nomenclature_version_id")
	private Integer nomenclatureVersionId;
	@Column(name = "loinc_code")
	private String loincCode;
	@Column(name = "external_code")
	private String externalCode;
	@Column(name = "external_source_other_than_loinc")
	private String externalSourceOtherThanLoinc;
	@Column(name = "loinc_component_name")
	private String loincComponentName;
	@Column(name = "loinc_property")
	private String loincProperty;
	@Column(name = "units")
	private String units;
	@Column(name = "loinc_time")
	private String loincTime;
	@Column(name = "loinc_system")
	private String loincSystem;
	@Column(name = "loinc_scale")
	private String loincScale;
	@Column(name = "loinc_method")
	private String loincMethod;
	@Column(name = "loinc_short_name")
	private String loincShortName;
	@Column(name = "loinc_fully_specified_name")
	private String loincFullySpecifiedName;
	@Column(name = "result_alternate_name_1")
	private String resultAlternateName1;
	@Column(name = "result_alternate_name_2")
	private String resultAlternateName2;
	@Column(name = "result_alternate_name_3")
	private String resultAlternateName3;
	@Column(name = "result_category")
	private String resultCategory;
	@Column(name = "result_sub_category")
	private String resultSubCategory;
	@Column(name = "loinc_answer_list")
	private String loincAnswerList;
	@Column(name = "loinc_status")
	private String loincStatus;
	@Column(name = "lili_code")
	private String liliCode;
	@Column(name = "reportable")
	private String reportable;
	@Column(name = "reportable_context")
	private String reportableContext;
	@Column(name = "external_code_version")
	private String externalCodeVersion;
	@Column(name = "change_note")
	private String changeNote;
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
	@Column(name = "description")
	private String description;
	@Column(name = "sort_key")
	private String sortKey;
	@Column(name = "update_date")
	private Date updateDate;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getNomenclatureVersionId() {
		return nomenclatureVersionId;
	}

	public void setNomenclatureVersionId(Integer nomenclatureVersionId) {
		this.nomenclatureVersionId = nomenclatureVersionId;
	}

	public String getLoincCode() {
		return loincCode;
	}

	public void setLoincCode(String loincCode) {
		this.loincCode = loincCode;
	}

	public String getExternalCode() {
		return externalCode;
	}

	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}

	public String getExternalSourceOtherThanLoinc() {
		return externalSourceOtherThanLoinc;
	}

	public void setExternalSourceOtherThanLoinc(String externalSourceOtherThanLoinc) {
		this.externalSourceOtherThanLoinc = externalSourceOtherThanLoinc;
	}

	public String getLoincComponentName() {
		return loincComponentName;
	}

	public void setLoincComponentName(String loincComponentName) {
		this.loincComponentName = loincComponentName;
	}

	public String getLoincProperty() {
		return loincProperty;
	}

	public void setLoincProperty(String loincProperty) {
		this.loincProperty = loincProperty;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getLoincTime() {
		return loincTime;
	}

	public void setLoincTime(String loincTime) {
		this.loincTime = loincTime;
	}

	public String getLoincSystem() {
		return loincSystem;
	}

	public void setLoincSystem(String loincSystem) {
		this.loincSystem = loincSystem;
	}

	public String getLoincScale() {
		return loincScale;
	}

	public void setLoincScale(String loincScale) {
		this.loincScale = loincScale;
	}

	public String getLoincMethod() {
		return loincMethod;
	}

	public void setLoincMethod(String loincMethod) {
		this.loincMethod = loincMethod;
	}

	public String getLoincShortName() {
		return loincShortName;
	}

	public void setLoincShortName(String loincShortName) {
		this.loincShortName = loincShortName;
	}

	public String getLoincFullySpecifiedName() {
		return loincFullySpecifiedName;
	}

	public void setLoincFullySpecifiedName(String loincFullySpecifiedName) {
		this.loincFullySpecifiedName = loincFullySpecifiedName;
	}

	public String getResultAlternateName1() {
		return resultAlternateName1;
	}

	public void setResultAlternateName1(String resultAlternateName1) {
		this.resultAlternateName1 = resultAlternateName1;
	}

	public String getResultAlternateName2() {
		return resultAlternateName2;
	}

	public void setResultAlternateName2(String resultAlternateName2) {
		this.resultAlternateName2 = resultAlternateName2;
	}

	public String getResultAlternateName3() {
		return resultAlternateName3;
	}

	public void setResultAlternateName3(String resultAlternateName3) {
		this.resultAlternateName3 = resultAlternateName3;
	}

	public String getResultCategory() {
		return resultCategory;
	}

	public void setResultCategory(String resultCategory) {
		this.resultCategory = resultCategory;
	}

	public String getResultSubCategory() {
		return resultSubCategory;
	}

	public void setResultSubCategory(String resultSubCategory) {
		this.resultSubCategory = resultSubCategory;
	}

	public String getLoincAnswerList() {
		return loincAnswerList;
	}

	public void setLoincAnswerList(String loincAnswerList) {
		this.loincAnswerList = loincAnswerList;
	}

	public String getLoincStatus() {
		return loincStatus;
	}

	public void setLoincStatus(String loincStatus) {
		this.loincStatus = loincStatus;
	}

	public String getLiliCode() {
		return liliCode;
	}

	public void setLiliCode(String liliCode) {
		this.liliCode = liliCode;
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

	public String getExternalCodeVersion() {
		return externalCodeVersion;
	}

	public void setExternalCodeVersion(String externalCodeVersion) {
		this.externalCodeVersion = externalCodeVersion;
	}

	public String getChangeNote() {
		return changeNote;
	}

	public void setChangeNote(String changeNote) {
		this.changeNote = changeNote;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public String getRegistrationStatusIndicator() {
		return registrationStatusIndicator;
	}

	public void setRegistrationStatusIndicator(String registrationStatusIndicator) {
		this.registrationStatusIndicator = registrationStatusIndicator;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
