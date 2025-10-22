//CHECKSTYLE:OFF
/**
 * Copyright (c) 2008-2012 Indivica Inc.
 * <p>
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package ca.openosp.openo.hospitalReportManager.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import ca.openosp.openo.commn.model.AbstractModel;

@Entity
public class HRMProviderConfidentialityStatement extends AbstractModel<String> {

    @Id
    private String providerNo;

    private String statement;

    @Override
    public String getId() {
        return providerNo;
    }

    public void setId(String id) {
        this.providerNo = id;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

}
