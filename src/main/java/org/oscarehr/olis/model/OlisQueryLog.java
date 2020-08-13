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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "olis_query_log")
public class OlisQueryLog extends AbstractModel<Integer> {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_sent")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date dateSent;
    @Column(name = "query_code")
    private String queryCode;
    @Column(name = "type")
    private String type;
    @Column(name = "initiating_provider_no")
    private String initiatingProviderNo;
    @Column(name = "requesting_hic")
    private String requestingHic;
    @Column(name = "external_system")
    private String externalSystem;
    @Column(name = "emr_transaction_id")
    private String emrTransactionId;
    @Column(name = "olis_transaction_id")
    private String olisTransactionId;
    @Column(name = "file_name")
    private String fileName;

    public OlisQueryLog() {
    }

    public OlisQueryLog(String queryCode, String type, String initiatingProviderNo, String requestingHic, String externalSystem, String emrTransactionId, String olisTransactionId, String fileName) {
        this.dateSent = new Date();
        this.queryCode = queryCode;
        this.type = type;
        this.initiatingProviderNo = initiatingProviderNo;
        this.requestingHic = requestingHic;
        this.externalSystem = externalSystem;
        this.emrTransactionId = emrTransactionId;
        this.olisTransactionId = olisTransactionId;
        this.fileName = fileName;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateSent() {
        return dateSent;
    }
    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getQueryCode() {
        return queryCode;
    }
    public void setQueryCode(String queryCode) {
        this.queryCode = queryCode;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getInitiatingProviderNo() {
        return initiatingProviderNo;
    }
    public void setInitiatingProviderNo(String initiatingProviderNo) {
        this.initiatingProviderNo = initiatingProviderNo;
    }

    public String getRequestingHic() {
        return requestingHic;
    }
    public void setRequestingHic(String requestingHic) {
        this.requestingHic = requestingHic;
    }

    public String getExternalSystem() {
        return externalSystem;
    }
    public void setExternalSystem(String externalSystem) {
        this.externalSystem = externalSystem;
    }

    public String getEmrTransactionId() {
        return emrTransactionId;
    }
    public void setEmrTransactionId(String emrTransactionId) {
        this.emrTransactionId = emrTransactionId;
    }

    public String getOlisTransactionId() {
        return olisTransactionId;
    }
    public void setOlisTransactionId(String olisTransactionId) {
        this.olisTransactionId = olisTransactionId;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
