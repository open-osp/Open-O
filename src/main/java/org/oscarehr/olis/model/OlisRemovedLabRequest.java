/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.olis.model;

import org.oscarehr.common.model.AbstractModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity(name = "olis_removed_lab_request")
public class OlisRemovedLabRequest extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Integer id;
	@Column(name = "emr_transaction_id")
    private String emrTransactionId;
    @Column(name = "removing_provider")
    private String removingProvider;
    @Column(name = "removal_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date removalDate;
    @Column(name = "removal_reason")
    private String removalReason;
    @Column(name = "removal_type")
    private String removalType;
    @Column(name = "download_from")
    private String downloadFrom;
    @Column(name = "accession_number")
    private String accessionNumber;
    @Column(name = "test_request")
    private String testRequest;
    @Column(name = "collection_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date collectionDate;
    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public OlisRemovedLabRequest() {
    }

    public OlisRemovedLabRequest(String emrTransactionId, String removingProvider, Date removalDate, String removalReason, String removalType, String downloadFrom, String accessionNumber, String testRequest, Date collectionDate, Date lastUpdated) {
        this.emrTransactionId = emrTransactionId;
        this.removingProvider = removingProvider;
        this.removalDate = removalDate;
        this.removalReason = removalReason;
        this.removalType = removalType;
        this.downloadFrom = downloadFrom;
        this.accessionNumber = accessionNumber;
        this.testRequest = testRequest;
        this.collectionDate = collectionDate;
        this.lastUpdated = lastUpdated;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmrTransactionId() {
        return emrTransactionId;
    }
    public void setEmrTransactionId(String emrTransactionId) {
        this.emrTransactionId = emrTransactionId;
    }

    public String getRemovingProvider() {
        return removingProvider;
    }
    public void setRemovingProvider(String removingProvider) {
        this.removingProvider = removingProvider;
    }

    public Date getRemovalDate() {
        return removalDate;
    }
    public void setRemovalDate(Date removalDate) {
        this.removalDate = removalDate;
    }

    public String getRemovalReason() {
        return removalReason;
    }
    public void setRemovalReason(String removalReason) {
        this.removalReason = removalReason;
    }

    public String getRemovalType() {
        return removalType;
    }
    public void setRemovalType(String removalType) {
        this.removalType = removalType;
    }

    public String getDownloadFrom() {
        return downloadFrom;
    }
    public void setDownloadFrom(String downloadFrom) {
        this.downloadFrom = downloadFrom;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }
    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getTestRequest() {
        return testRequest;
    }
    public void setTestRequest(String testRequest) {
        this.testRequest = testRequest;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }
    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
