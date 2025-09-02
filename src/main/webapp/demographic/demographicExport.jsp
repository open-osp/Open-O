<!DOCTYPE html>
<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@page import="ca.openosp.openo.commn.model.Provider" %>
<%@page import="ca.openosp.openo.PMmodule.dao.ProviderDao" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic,_demographicExport" rights="w"
                   reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic&type=_demographicExport");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<%@page import="ca.openosp.openo.utility.SpringUtils" %>
<%@page
        import="java.util.*,ca.openosp.openo.demographic.data.*,ca.openosp.openo.prevention.*,ca.openosp.openo.providers.data.*,ca.openosp.openo.util.*,ca.openosp.openo.report.data.*,ca.openosp.openo.prevention.pageUtil.*,ca.openosp.openo.demographic.pageUtil.*" %>
<%@ page import="ca.openosp.openo.demographic.pageUtil.Util" %>
<%@ page import="ca.openosp.openo.demographic.pageUtil.DemographicExportAction42Action" %>
<%@ page import="ca.openosp.openo.demographic.pageUtil.PGPEncrypt" %>
<%@ page import="ca.openosp.openo.report.data.DemographicSets" %>
<%@ page import="ca.openosp.OscarProperties" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

<%

    OscarProperties op = OscarProperties.getInstance();
    String tmp_dir = op.getProperty("TMP_DIR");
    boolean tmp_dir_ready = Util.checkDir(tmp_dir);

    String pgp_ready = (String) session.getAttribute("pgp_ready");
    if (pgp_ready == null || pgp_ready.equals("No")) {
        PGPEncrypt pgp = new PGPEncrypt();
        if (pgp.check(tmp_dir)) pgp_ready = "Yes";
        else pgp_ready = "No";
    }
    session.setAttribute("pgp_ready", pgp_ready);

    String demographicNo = request.getParameter("demographicNo");
    DemographicSets ds = new DemographicSets();
    List<String> sets = ds.getDemographicSets();

//  ca.openosp.openo.report.data.RptSearchData searchData  = new ca.openosp.openo.report.data.RptSearchData();
//  ArrayList queryArray = searchData.getQueryTypes();

    String userRole = (String) session.getAttribute("userrole");

%>

<html>
    <script src="${pageContext.request.contextPath}/csrfguard"></script>
    <head>
        <title><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.title"/></title>

        <link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

        <SCRIPT LANGUAGE="JavaScript">

            function showHideItem(id) {
                if (document.getElementById(id).style.display == 'none')
                    document.getElementById(id).style.display = '';
                else
                    document.getElementById(id).style.display = 'none';
            }

            function showItem(id) {
                document.getElementById(id).style.display = '';
            }

            function hideItem(id) {
                document.getElementById(id).style.display = 'none';
            }

            function showHideNextDate(id, nextDate, nexerWarn) {
                if (document.getElementById(id).style.display == 'none') {
                    showItem(id);
                } else {
                    hideItem(id);
                    document.getElementById(nextDate).value = "";
                    document.getElementById(nexerWarn).checked = false;

                }
            }

            function disableifchecked(ele, nextDate) {
                if (ele.checked == true) {
                    document.getElementById(nextDate).disabled = true;
                } else {
                    document.getElementById(nextDate).disabled = false;
                }
            }

            function checkSelect(slct) {
                if (slct == -1) {
                    alert("Please select a Patient Set");
                    return false;
                } else return true;
            }

            function checkValidOptions() {
                var pt = document.getElementById("patientSet").value;
                var pn = document.getElementById("providerNo").value;

                if (pt != -1 && pn != -1) {
                    alert("Please choose either a Patient Set or a Provider");
                    return false;
                }

                if (pt == -1 && pn == -1) {
                    alert("Please choose either a Patient Set or a Provider");
                    return false;
                }

                return true;
            }

            function checkAll(all) {
                var frm = document.DemographicExportForm;
                if (all) {
                    frm.exPersonalHistory.checked = true;
                    frm.exFamilyHistory.checked = true;
                    frm.exPastHealth.checked = true;
                    frm.exProblemList.checked = true;
                    frm.exRiskFactors.checked = true;
                    frm.exAllergiesAndAdverseReactions.checked = true;
                    frm.exMedicationsAndTreatments.checked = true;
                    frm.exImmunizations.checked = true;
                    frm.exLaboratoryResults.checked = true;
                    frm.exAppointments.checked = true;
                    frm.exClinicalNotes.checked = true;
                    frm.exReportsReceived.checked = true;
                    frm.exAlertsAndSpecialNeeds.checked = true;
                    frm.exCareElements.checked = true;
                } else {
                    frm.exPersonalHistory.checked = false;
                    frm.exFamilyHistory.checked = false;
                    frm.exPastHealth.checked = false;
                    frm.exProblemList.checked = false;
                    frm.exRiskFactors.checked = false;
                    frm.exAllergiesAndAdverseReactions.checked = false;
                    frm.exMedicationsAndTreatments.checked = false;
                    frm.exImmunizations.checked = false;
                    frm.exLaboratoryResults.checked = false;
                    frm.exAppointments.checked = false;
                    frm.exClinicalNotes.checked = false;
                    frm.exReportsReceived.checked = false;
                    frm.exAlertsAndSpecialNeeds.checked = false;
                    frm.exCareElements.checked = false;
                }
            }

            function toggle(source) {
                var c = new Array();
                c = document.getElementsByTagName('input');
                for (var i = 0; i < c.length; i++) {
                    if (c[i].type == 'checkbox') {
                        c[i].checked = source.checked;
                    }
                }
            }
        </SCRIPT>

        <style type="text/css">
            input[type="checkbox"] {
                line-height: normal;
                margin: 4px 4px 4px;
            }
        </style>

    </head>

    <body>
    <%
        if (!userRole.toLowerCase().contains("admin")) { %>
    <div class="alert alert-block alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.msgsorry"/>
    </div>
    <%
    } else if (!tmp_dir_ready) { %>
    <div class="alert alert-block alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.msgerror"/>
    </div>
    <%
    } else {
    %>

    <div class="container-fluid well">
        <h3><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.title"/> </h3>

        <div class="span2">
            <% if (demographicNo == null) { %>
            <a href='<c:out value="${ctx}/demographic/cihiExportOMD4.do"></c:out>'><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.cihiexport"/></a><br>
            <a href='<c:out value="${ctx}/demographic/eRourkeExport.do"></c:out>'><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.rourke2009export"/></a>
            <%} %>
        </div><!--span2-->

        <div class="span4">

            <form action="${pageContext.request.contextPath}/demographic/DemographicExport.do" method="get" onsubmit="return checkValidOptions();">

                <% if (demographicNo != null) { %>
                <input type="hidden" name="demographicNo" id="demographicNo" value="<%=demographicNo%>"/>
                <fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.exportingdemographicno"/><%=demographicNo%>
                <%} else {%>
                <fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.patientset"/><br>
                <select style="width: 189px" name="patientSet" id="patientSet">
                    <option value="-1"><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.selectset"/></option>
                    <%
                        /*			    for (int i =0 ; i < queryArray.size(); i++){
                        RptSearchData.SearchCriteria sc = (RptSearchData.SearchCriteria) queryArray.get(i);
                        String qId = sc.id;
                        String qName = sc.queryName;
                        */
                        for (int i = 0; i < sets.size(); i++) {
                            String setName = sets.get(i);
                    %>
                    <option value="<%=setName%>"><%=setName%>
                    </option>
                    <%}%>
                </select>

                <br>

                <fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.providers"/><br>
                <select style="width: 189px" name="providerNo" id="providerNo">
                    <option value="-1"><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.selectProvider"/></option>
                    <%
                        ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
                        List<Provider> providers = providerDao.getActiveProviders();
	/*			    for (int i =0 ; i < queryArray.size(); i++){
	RptSearchData.SearchCriteria sc = (RptSearchData.SearchCriteria) queryArray.get(i);
	String qId = sc.id;
	String qName = sc.queryName;
	*/
                        for (int i = 0; i < providers.size(); i++) {
                            Provider p = providers.get(i);
                    %>
                    <option value="<%=p.getProviderNo()%>"><%=p.getFormattedName()%>
                    </option>
                    <%}%>
                </select>

                <%}%>


                <br>


                <fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.exporttemplate"/><br>
                <select style="width: 189px" name="template">
                    <option
                            value="<%=(new Integer(DemographicExportAction42Action.CMS4)).toString() %>">EMR DM 5.0</option>
                    <option value="<%=(new Integer(DemographicExportAction42Action.E2E)).toString() %>">E2E</option>
                </select>

                <br>

                <fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.exportcategories"/><br>

                <input type="checkbox" onClick="toggle(this)"/>Select All<br/>

                <input type="checkbox" name="exPersonalHistory" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.personalhistory"/><br>
                <input type="checkbox" name="exFamilyHistory" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.familyhistory"/><br>
                <input type="checkbox" name="exPastHealth" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.pasthealth"/><br>
                <input type="checkbox" name="exProblemList" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.problemlist"/><br>
                <input type="checkbox" name="exRiskFactors" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.riskfactors"/><br>
                <input type="checkbox" name="exAllergiesAndAdverseReactions" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.allergiesadversereaction"/><br>
                <input type="checkbox" name="exMedicationsAndTreatments" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.medicationstreatments"/><br>

                <input type="checkbox" name="exImmunizations" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.immunization"/><br>
                <input type="checkbox" name="exLaboratoryResults" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.laboratoryresults"/><br>
                <input type="checkbox" name="exAppointments" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.appointments"/><br>
                <input type="checkbox" name="exClinicalNotes" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.clinicalnotes"/><br>
                <input type="checkbox" name="exReportsReceived" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.reportsreceived"/><br>
                <input type="checkbox" name="exCareElements" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.careelements"/><br>
                <input type="checkbox" name="exAlertsAndSpecialNeeds" value="true" /><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.alertsandspecialneeds"/>

                <br>
                <input type="hidden" name="pgpReady" id="pgpReady" value="<%=pgp_ready%>"/>

                <% boolean pgpReady = pgp_ready.equals("Yes") ? true : false;
//    pgpReady = true; //To be removed after CMS4
                    if (!pgpReady) { %>

                <div class="alert alert-block alert-error">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.msgwarning"/>
                </div>

                <% } %>

                <input class="btn btn-primary" type="submit" value="<fmt:setBundle basename="oscarResources"/><fmt:message key="export"/>"/>

            </form>

        </div><!--span4-->

    </div><!--container-->
    <%}%>

    <script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
    <script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>

    </body>
</html>
