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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<%@ page import="oscar.oscarReport.data.RptSearchData,java.util.*" %>


<jsp:useBean id="providerBean" class="java.util.Properties" scope="session"/>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/oscarEncounter/encounterStyles.css">
<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
<script>
    $(document).ready(function () {
        $("#demographicNoCheckbox").bind('change', function () {
            updateSetBox();
            //alert('test');
        });
        updateSetBox();
    });

    function updateSetBox() {
        if ($("#demographicNoCheckbox").is(":checked")) {
            $("#submitPatientSet").show();
        } else {
            $("#submitPatientSet").hide();
        }
    }
</script>
<%
    oscar.oscarReport.data.RptSearchData searchData = new oscar.oscarReport.data.RptSearchData();
    java.util.ArrayList rosterArray;
    java.util.ArrayList patientArray;
    java.util.ArrayList providerArray;
    java.util.ArrayList queryArray;
    rosterArray = searchData.getRosterTypes();
    patientArray = searchData.getPatientTypes();
    providerArray = searchData.getProvidersWithDemographics();
    queryArray = searchData.getQueryTypes();

    String studyId = request.getParameter("studyId");
    if (studyId == null) {
        studyId = (String) request.getAttribute("studyId");
    }
%>

<html>
    <head>
        <title>
            Demographic Report tool
        </title>
        <base href="<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" %>">
        <link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/share/calendar/calendar.css" title="win2k-cold-1"/>

        <script type="text/javascript" src="<%= request.getContextPath() %>/share/calendar/calendar.js"></script>
        <script type="text/javascript"
                src="<%= request.getContextPath() %>/share/calendar/lang/<fmt:setBundle basename="oscarResources"/><fmt:message key="global.javascript.calendar"/>"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/calendar/calendar-setup.js"></script>

        <script language="JavaScript">
            function checkQuery() {
                var ret = false;
                var chks = document.forms[0].select;
                for (var i = 0; i < chks.length; i++) {
                    if (chks[i].checked) {
                        ret = true;
                        break;
                    }
                }
                if (!ret) alert("Please select at least one field");
                return ret;
            }

        </script>

        <style type="text/css" media="print">
            .MainTable {
                display: none;
            }

            .hiddenInPrint {
                display: none;
            }

            /
            /
            .header INPUT {
            / / display: none;
            / /
            }

            /
            /
            /
            /
            .header A {
            / / display: none;
            / /
            }

        </style>

        <style type="text/css">
            #provider {
                margin-top: 0;
                margin-left: 0;
                margin-bottom: 3px;
                padding: 0;
            }

            #provider li {
                display: inline;
                float: left;
                list-style-type: none;
            / / padding-left: 30 px;
                padding: 4px;
                padding-top: 6px;
                padding-bottom: 0px;
                border-bottom: 1px solid #666;
                border-right: 1px solid #666;
                border-top: 1px solid #666;
                border-left: 1px solid #666;
                margin-right: 2px;
                margin-bottom: 2px;
            }


        </style>


    </head>

    <body class="BodyStyle" vlink="#0000FF">

    <table class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                oscarReport
            </td>
            <td class="MainTableTopRowRightColumn">
                <form action="${pageContext.request.contextPath}/report/DemographicReport.do" method="post" onsubmit="return checkQuery();">
                <input type="hidden" name="studyId" id="studyId" value='<%=studyId == null ? "" : studyId%>'/>
                <table class="TopStatusBar">
                    <tr>
                        <td>
                            Demographic Search
                        </td>
                        <td>
                            <select name="savedQuery" id="savedQuery">
                                <%
                                    for (int i = 0; i < queryArray.size(); i++) {
                                        RptSearchData.SearchCriteria sc = (RptSearchData.SearchCriteria) queryArray.get(i);
                                        String qId = sc.id;
                                        String qName = sc.queryName;%>
                                <option value="<%=qId%>"><%=qName%>
                                </option>

                                <%}%>
                            </select>
                            <input type="submit" value="Load Query" name="query"/>
                            <a href="oscarReport/ManageDemographicQueryFavourites.jsp">manage</a>
                        </td>
                        <td style="text-align:right">
                            <a
                                href="javascript:popupStart(300,400,'About.jsp')">About</a> | <a
                                href="javascript:popupStart(300,400,'License.jsp')">License</a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>

            <td class="MainTableLeftColumn">
                &nbsp;
            </td>
            <td class="MainTableRightColumn">

                <%
                    oscar.oscarReport.pageUtil.RptDemographicReport2Form thisForm = null;

                    // Get the form bean from the request
                    if (request.getAttribute("formBean") != null) {
                        thisForm = (oscar.oscarReport.pageUtil.RptDemographicReport2Form) request.getAttribute("formBean");
                    }

                    // Initialize the form bean if it is null
                    if (thisForm == null) {
                        thisForm = new oscar.oscarReport.pageUtil.RptDemographicReport2Form();
                        request.setAttribute("formBean", thisForm); // Ensure the form bean is set in the request
                    }

                    // Call the copy constructor if the form is already in the request
                    if (request.getAttribute("RptDemographicReport2Form") != null) {
                        thisForm.copyConstructor((oscar.oscarReport.pageUtil.RptDemographicReport2Form) request.getAttribute("RptDemographicReport2Form"));
                    }

                    // Set the default values for the form bean
                    if (thisForm.getAgeStyle() == null || "2".equals(thisForm.getAgeStyle())) {
                        thisForm.setAgeStyle("1");
                    }
                %>

                <table border=1>
                    <tr>
                        <td>
                            <table>
                                <tr>
                                    <th colspan="2">
                                        Search For
                                    </th>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value=""/>
                                    </td>
                                    <td>
                                        Demographic #
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="last_name"/>
                                    </td>
                                    <td>
                                        Last Name
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="first_name"/>
                                    </td>
                                    <td>
                                        First Name
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="address"/>
                                    </td>
                                    <td>
                                        Address
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="city"/>
                                    </td>
                                    <td>
                                        City
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="province"/>
                                    </td>
                                    <td>
                                        Province
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="postal"/>
                                    </td>
                                    <td>
                                        Postal Code
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="phone"/>
                                    </td>
                                    <td>
                                        Phone
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="phone2"/>
                                    </td>
                                    <td>
                                        Phone 2
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="email"/>
                                    </td>
                                    <td>
                                        <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarReport.oscarReportscpbDemo.msgEmail"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="year_of_birth"/>
                                    </td>
                                    <td>
                                        Year of Birth
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="month_of_birth"/>
                                    </td>
                                    <td>
                                        Month of Birth
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="date_of_birth"/>
                                    </td>
                                    <td>
                                        Date of Birth
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="hin"/>
                                    </td>
                                    <td>
                                        HIN
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="ver"/>
                                    </td>
                                    <td>
                                        Version Code
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="roster_status"/>
                                    </td>
                                    <td>
                                        Roster Status
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="patient_status"/>
                                    </td>
                                    <td>
                                        Patient Status
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="date_joined"/>
                                    </td>
                                    <td>
                                        Date Joined
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="chart_no"/>
                                    </td>
                                    <td>
                                        Chart #
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="provider_no"/>
                                    </td>
                                    <td>
                                        Provider #
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="provider_name"/>
                                    </td>
                                    <td>
                                        Provider Name
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="sex"/>
                                    </td>
                                    <td>
                                        Sex
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="end_date"/>
                                    </td>
                                    <td>
                                        End Date
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="eff_date"/>
                                    </td>
                                    <td>
                                        Eff. Date
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="pcn_indicator"/>
                                    </td>
                                    <td>
                                        Pcn indicator
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="hc_type"/>
                                    </td>
                                    <td>
                                        Health Card Type
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="hc_renew_date"/>
                                    </td>
                                    <td>
                                        HC Renew Date
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="family_doctor"/>
                                    </td>
                                    <td>
                                        Family Doctor
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="select" id="select" value="newsletter"/>
                                    </td>
                                    <td>
                                        <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarReport.oscarReportDemoReport.frmNewsletter"/>
                                    </td>
                                </tr>
                            </table>
                            <input type="text" name="queryName" id="queryName" /><br>
                            <input type="submit" value="Save Query" name="query"/>
                            <input type="submit" value="Run Query" name="query"/><br/>
                            <span id="submitPatientSet">
	<input type="submit" value="Run Query And Save to Patient Set" name="query"/>&nbsp;<input type="text" name="setName"
                                                                                              placeholder="Set Name"/>
</span>
                            <%if (studyId != null && !studyId.equals("") && !studyId.equalsIgnoreCase("null")) {%>
                            <input type="submit" value="Add to Study" name="query"/>
                            <%} %>
                        </td>
                        <td valign=top>

                            <table border=1>

                                <tr>
                                    <th colspan="4">
                                        Where
                                    </th>
                                </tr>
                                <tr>
                                    <td>
                                        AGE
                                    </td>
                                    <td>
                                        <select name="age" id="age">
                                            <option value="0">---NO AGE SPECIFIED---</option>
                                            <option value="1">younger than</option>
                                            <option value="2">older than</option>
                                            <option value="3">equal too</option>
                                            <option value="4">ages between</option>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="checkbox" name="startYear" size="4" />
                                        <input type="checkbox" name="endYear" size="4" />
                                    </td>
                                    <td>

                                        Age Style:
                                        Exact:
                                        <input type="radio" name="ageStyle" value="1"/>
                                        In the year
                                        <input type="radio" name="ageStyle" value="2"/>
                                        As of : <input type="text" name="asofDate" size="9" id="asofDate"/> <a
                                            id="date"><img title="Calendar" src="<%= request.getContextPath() %>/images/cal.gif" alt="Calendar"
                                                           border="0"/></a> <br>
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        First Name
                                    </td>
                                    <td>
                                        <input type="text" name="firstName" id="firstName" />
                                    </td>
                                    <td>
                                        Last Name
                                    </td>
                                    <td>
                                        <input type="text" name="lastName" id="lastName" />
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        Roster Status
                                    </td>
                                    <td>
                                        <table border=1>
                                            <tr>


                                                <%
                                                    for (int i = 0; i < rosterArray.size(); i++) {
                                                        String ros = (String) rosterArray.get(i);%>
                                                <td><%=ros%><br>
                                                    <input type="checkbox" name="rosterStatus" value="<%=ros%>" />
                                                </td>
                                                <%
                                                    }
                                                %>


                                            </tr>
                                        </table>
                                    </td>
                                    <td colspan='2'>
                                        &nbsp;
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        Sex
                                    </td>
                                    <td>
                                        <select name="sex" id="sex">
                                            <option value="0">---NO SEX SPECIFIED---</option>
                                            <option value="1">Female</option>
                                            <option value="2">Male</option>
                                        </select>
                                    </td>
                                    <td colspan='2'>
                                        &nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Provider No
                                    </td>
                                    <td colspan="3">
                                        <ul id="provider">

                                            <%
                                                for (int i = 0; i < providerArray.size(); i++) {
                                                    String pro = (String) providerArray.get(i);
                                                    if (pro != null && !"".equals(pro)) {
                                            %>
                                            <li><%=providerBean.getProperty(pro, pro)%>
                                                <input type="checkbox" name="providerNo" value="<%=pro%>"/>
                                            </li>
                                            <%
                                                    }
                                                }
                                            %>
                                        </ul>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Patient Status
                                    </td>
                                    <td colspan="3">
                                        <table border=1>
                                            <tr>


                                                <%
                                                    for (int i = 0; i < patientArray.size(); i++) {
                                                        String pat = (String) patientArray.get(i);%>
                                                <td><%=pat%><br>
                                                    <input type="submit" name="patientStatus" value="<%=pat%>"/>
                                                </td>
                                                <%
                                                    }
                                                %>


                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Demographic ID(s):
                                    </td>
                                    <td colspan="3">
                                        <textarea name="demoIds" cols="60" rows="5"> </textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Order By
                                    </td>
                                    <td>
                                        <select name="orderBy" id="orderBy">
                                            <option value="0">---NO ORDER---</option>
                                            <option value="Demographic #">Demographic #</option>
                                            <option value="Last Name">Last Name</option>
                                            <option value="First Name">First Name</option>
                                            <option value="Address">Address</option>
                                            <option value="City">City</option>
                                            <option value="Province">Province</option>
                                            <option value="Postal Code">Postal Code</option>
                                            <option value="Phone">Phone</option>
                                            <option value="Phone 2">Phone 2</option>
                                            <option value="Year of Birth">Year of Birth</option>
                                            <option value="Month of Birth">Month of Birth</option>
                                            <option value="Date of Birth">Date of Birth</option>
                                            <option value="HIN">HIN</option>
                                            <option value="Version Code">Version Code</option>
                                            <option value="Roster Status">Roster Status</option>
                                            <option value="Patient Status">Patient Status</option>
                                            <option value="Date Joined">Date Joined</option>
                                            <option value="Chart #">Chart #</option>
                                            <option value="Provider #">Provider #</option>
                                            <option value="Sex">Sex</option>
                                            <option value="End Date">End Date</option>
                                            <option value="Eff. Date">Eff. Date</option>
                                            <option value="Pcn indicator">Pcn indicator</option>
                                            <option value="HC Type">HC Type</option>
                                            <option value="HC Renew Date">HC Renew Date</option>
                                            <option value="Family Doctor">Family Doctor</option>
                                            <option value="Random">Random</option>
                                        </select>
                                    </td>
                                    <td colspan='2'>
                                        &nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Limit Results to:
                                    </td>
                                    <td>
                                        <select name="resultNum" id="resultNum">
                                            <option value="0">---NO LIMIT---</option>
                                            <option value="10">10</option>
                                            <option value="50">50</option>
                                            <option value="100">100</option>
                                            <option value="150">150</option>
                                            <option value="200">200</option>
                                            <option value="250">250</option>
                                            <option value="300">300</option>

                                        </select>
                                    </td>
                                    <td colspan='2'>

                                    </td>
                                </tr>

                            </table>
                            </form>
                        </td>
                    </tr>
                </table>


            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
                &nbsp;
            </td>
            <td class="MainTableBottomRowRightColumn">
                &nbsp;
            </td>
        </tr>
    </table>
    <%
        String[] selectArray = (String[]) request.getAttribute("selectArray");
        java.util.ArrayList searchList = (java.util.ArrayList) request.getAttribute("searchedArray");
        if (searchList != null) {
            oscar.oscarReport.data.RptDemographicColumnNames dcn = new oscar.oscarReport.data.RptDemographicColumnNames();
    %>

    Search Returned : <%=searchList.size()%> Results


    <table border=1>
        <tr>
            <%for (int i = 0; i < selectArray.length; i++) {%>
            <th>
                <%=dcn.getColumnTitle(selectArray[i])%>
            </th>
            <%}%>
        </tr>
        <%

            for (int i = 0; i < searchList.size(); i++) {
                java.util.ArrayList al = (java.util.ArrayList) searchList.get(i);
        %>
        <tr>
            <%
                for (int j = 0; j < al.size(); j++) {
                    String str = (String) al.get(j);
                    if (str == null || str.equals("")) {
                        str = "&nbsp;";
                    }
            %>
            <td>
                <%=str%>

            </td>

            <%}%>
        </tr>
        <%}%>

    </table>


    <%}%>
    <script type="text/javascript">
        Calendar.setup({
            inputField: "asofDate",
            ifFormat: "%Y-%m-%d",
            showsTime: false,
            button: "date",
            singleClick: true,
            step: 1
        });
    </script>
    </body>
</html>
