/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 *
 * Modifications made by Magenta Health in 2024.
 */

package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderLabRoutingModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface ProviderLabRoutingDao extends AbstractDao<ProviderLabRoutingModel> {
    public static final String UNCLAIMED_PROVIDER = "0";

	public enum LAB_TYPE {
		DOC, HL7
	}

	public enum STATUS {
		X, N, A, D, F
	}

	public List<ProviderLabRoutingModel> findByLabNoAndLabTypeAndProviderNo(int labNo, String labType,
			String providerNo);

	public List<ProviderLabRoutingModel> getProviderLabRoutingDocuments(Integer labNo);

	public List<ProviderLabRoutingModel> getProviderLabRoutingForLabProviderType(Integer labNo, String providerNo,
			String labType);

	public List<ProviderLabRoutingModel> getProviderLabRoutingForLabAndType(Integer labNo, String labType);

	public List<ProviderLabRoutingModel> findAllLabRoutingByIdandType(Integer labNo, String labType);

	public void updateStatus(Integer labNo, String labType);

	public ProviderLabRoutingModel findByLabNo(int labNo);

	public List<ProviderLabRoutingModel> findByLabNoIncludingPotentialDuplicates(int labNo);

	public ProviderLabRoutingModel findByLabNoAndLabType(int labNo, String labType);

	public List<Object[]> getProviderLabRoutings(Integer labNo, String labType);

	public List<ProviderLabRoutingModel> findByStatusANDLabNoType(Integer labNo, String labType, String status);

	public List<ProviderLabRoutingModel> findByProviderNo(String providerNo, String status);

	public List<ProviderLabRoutingModel> findByLabNoTypeAndStatus(int labId, String labType, String status);

	public List<Integer> findLastRoutingIdGroupedByProviderAndCreatedByDocCreator(String docCreator);

	public List<Object[]> findProviderAndLabRoutingById(Integer id);

	public List<Object[]> findMdsResultResultDataByManyThings(String status, String providerNo, String patientLastName,
			String patientFirstName, String patientHealthNumber);

	public List<Object[]> findMdsResultResultDataByDemographicNoAndLabNo(Integer demographicNo, Integer labNo);

	public List<Object[]> findMdsResultResultDataByDemoId(String demographicNo);

	public List<Object[]> findProviderAndLabRoutingByIdAndLabType(Integer id, String labType);
}
