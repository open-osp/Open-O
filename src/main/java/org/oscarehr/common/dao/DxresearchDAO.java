/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 * Modifications made by Magenta Health in 2024.
 */
package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;
import org.oscarehr.common.model.DxRegistedPTInfo;
import org.oscarehr.common.model.Dxresearch;
import oscar.oscarResearch.oscarDxResearch.bean.dxCodeSearchBean;

import java.util.Date;
import java.util.List;

public interface DxresearchDAO extends AbstractDao<Dxresearch> {
    List<DxRegistedPTInfo> getPatientRegisted(List<Dxresearch> dList, List<String> doctorList);
    List<DxRegistedPTInfo> patientRegistedDistincted(List<dxCodeSearchBean> searchItems, List<String> doctorList);
    List<DxRegistedPTInfo> patientRegistedAll(List<dxCodeSearchBean> searchItems, List<String> doctorList);
    List<DxRegistedPTInfo> patientRegistedActive(List<dxCodeSearchBean> searchItems, List<String> doctorList);
    List<DxRegistedPTInfo> patientRegistedResolve(List<dxCodeSearchBean> searchItems, List<String> doctorList);
    List<DxRegistedPTInfo> patientRegistedDeleted(List<dxCodeSearchBean> searchItems, List<String> doctorList);
    List<Dxresearch> getDxResearchItemsByPatient(Integer demographicNo);
    void save(Dxresearch d);
    List<Dxresearch> getByDemographicNo(int demographicNo);
    List<Dxresearch> find(int demographicNo, String codeType, String code);
    List<Dxresearch> findActive(String codeType, String code);
    boolean entryExists(int demographicNo, String codeType, String code);
    boolean activeEntryExists(int demographicNo, String codeType, String code);
    void removeAllAssociationEntries();
    List<Object[]> findResearchAndCodingSystemByDemographicAndCondingSystem(String codingSystem, String demographicNo);
    List<Dxresearch> findCurrentByCodeTypeAndCode(String codeType, String code);
    List<Dxresearch> getByDemographicNoSince(int demographicNo, Date lastUpdateDate);
    List<Integer> getByDemographicNoSince(Date lastUpdateDate);
    List<Dxresearch> findNonDeletedByDemographicNo(Integer demographicNo);
    List<Integer> findNewProblemsSinceDemokey(String keyName);
    String getDescription(String codingSystem, String code);
    public List<Dxresearch> findByDemographicNoResearchCodeAndCodingSystem(Integer demographicNo, String dxresearchCode, String codingSystem);
    public List<dxCodeSearchBean> getQuickListItems(String quickListName);
    public List<Object[]> getDataForInrReport(Date fromDate, Date toDate);
    Integer countResearches(String researchCode, Date sdate, Date edate);
    Integer countBillingResearches(String researchCode, String diagCode, String creator, Date sdate, Date edate);
}
