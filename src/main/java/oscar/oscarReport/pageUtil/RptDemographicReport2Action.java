//CHECKSTYLE:OFF
/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * <p>
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarReport.pageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarReport.data.*;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class RptDemographicReport2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    public String execute() throws IOException, ServletException {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        MiscUtils.getLogger().debug("RptDemographicReport2Action Jackson");
        MiscUtils.getLogger().debug("query " + query);

        if (query == null) {
            MiscUtils.getLogger().error("Query is null");
            return ERROR; 
        }

        if (query.equals("Run Query")) {
            MiscUtils.getLogger().debug("run query");
            RptDemographicQuery2Builder demoQ = new RptDemographicQuery2Builder();
            java.util.ArrayList searchedArray = demoQ.buildQuery(loggedInInfo, form);
            MiscUtils.getLogger().debug("searchArray size " + searchedArray.size());
            request.setAttribute("searchedArray", searchedArray);
            request.setAttribute("selectArray", select);
            request.setAttribute("studyId", studyId);
        } else if (query.equals("Save Query")) {
            RptDemographicQuery2Saver demoS = new RptDemographicQuery2Saver();
            demoS.saveQuery(form);
        } else if (query.equals("Load Query")) {
            RptDemographicQuery2Loader demoL = new RptDemographicQuery2Loader();
            RptDemographicReport2Form dRF = demoL.queryLoader(form);
            request.setAttribute("formBean", dRF);
        } else if (query.equals("Add to Study")) {
            RptDemographicQuery2Builder demoQ = new RptDemographicQuery2Builder();
            java.util.ArrayList searchedArray = demoQ.buildQuery(loggedInInfo, form);
            request.setAttribute("searchedArray", searchedArray);
            MiscUtils.getLogger().info("SELECT ARRAY IS NULL " + String.valueOf(select == null));
            MiscUtils.getLogger().info("STUDY ID IS " + studyId);
            request.setAttribute("selectArray", select);
            request.setAttribute("studyId", studyId);
            return "addToStudy";
        } else if (query.equals("Run Query And Save to Patient Set")) {
            MiscUtils.getLogger().debug("run query and save to patient set");
            RptDemographicQuery2Builder demoQ = new RptDemographicQuery2Builder();
            java.util.ArrayList searchedArray = demoQ.buildQuery(loggedInInfo, form);

            if (select != null && select.length > 0 && select[0].equals("demographic_no")) {
                DemographicSets demoSet = new DemographicSets();


                List<String> theDemos = new ArrayList<String>();
                for (int x = 0; x < searchedArray.size(); x++) {
                    ArrayList<String> row = (ArrayList<String>) searchedArray.get(x);
                    theDemos.add(row.get(0));
                }
                demoSet.addDemographicSet(form.getSetName(), theDemos);
            }

            MiscUtils.getLogger().debug("searchArray size " + searchedArray.size());
            request.setAttribute("searchedArray", searchedArray);
            request.setAttribute("selectArray", select);
            request.setAttribute("studyId", studyId);
        }

        return SUCCESS;
    }

    public String[] select;
    public String age;
    public String startYear;
    public String endYear;
    public String firstName;
    public String lastName;
    public String[] rosterStatus;
    public String sex;
    public String[] providerNo;
    public String[] patientStatus;
    public String query;
    public String queryName;
    public String savedQuery;
    public String orderBy;
    public String resultNum;
    public String ageStyle;
    public String asofDate;
    public String studyId;
    public String demoIds;

    public String setName;

    public String[] getSelect() {
        return select;
    }

    public void setSelect(String[] select) {
        this.select = select;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String[] getRosterStatus() {
        return rosterStatus;
    }

    public void setRosterStatus(String[] rosterStatus) {
        this.rosterStatus = rosterStatus;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String[] getProviderNo() {
        return providerNo;
    }

    public void setProviderNo(String[] providerNo) {
        this.providerNo = providerNo;
    }

    public String[] getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(String[] patientStatus) {
        this.patientStatus = patientStatus;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getSavedQuery() {
        return savedQuery;
    }

    public void setSavedQuery(String savedQuery) {
        this.savedQuery = savedQuery;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getResultNum() {
        return resultNum;
    }

    public void setResultNum(String resultNum) {
        this.resultNum = resultNum;
    }

    public String getAgeStyle() {
        return ageStyle;
    }

    public void setAgeStyle(String ageStyle) {
        this.ageStyle = ageStyle;
    }

    public String getAsofDate() {
        return asofDate;
    }

    public void setAsofDate(String asofDate) {
        this.asofDate = asofDate;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public String getDemoIds() {
        return demoIds;
    }

    public void setDemoIds(String demoIds) {
        this.demoIds = demoIds;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    private RptDemographicReport2Form form;

    public RptDemographicReport2Form getForm() {
        return form;
    }

    public void setForm(RptDemographicReport2Form form) {
        this.form = form;
    }
}
