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

<%@ include file="/casemgmt/taglibs.jsp" %>

<%
    if (session.getValue("user") == null)
        response.sendRedirect("../logout.htm");
    String curUser_no;
    curUser_no = (String) session.getAttribute("user");

    boolean bFirstLoad = request.getAttribute("status") == null;

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<c:set var="ctx" value="${pageContext.request.contextPath}"
       scope="request"/>
<html>
    <head>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <base href="<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" %>">
        <title><fmt:setBundle basename="oscarResources"/><fmt:message key="provider.setNoteStaleDate.title"/></title>

        <link rel="stylesheet" type="text/css"
              href="<%= request.getContextPath() %>/oscarEncounter/encounterStyles.css">
        <!-- calendar stylesheet -->
        <link rel="stylesheet" type="text/css" media="all"
              href="<c:out value="${ctx}"/>/share/calendar/calendar.css"
              title="win2k-cold-1">

        <script src="<c:out value="${ctx}"/>/share/javascript/prototype.js"
                type="text/javascript"></script>
        <script src="<c:out value="${ctx}"/>/share/javascript/scriptaculous.js"
                type="text/javascript"></script>

        <script type="text/javascript">

            function validate() {
                var date = document.getElementById("staleDate");
                if (date.value == "") {
                    alert("Please select a date before saving");
                    return false;
                }

                return true;
            }
        </script>

    </head>

    <body class="BodyStyle" vlink="#0000FF">

    <table class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn"><fmt:setBundle basename="oscarResources"/><fmt:message key="provider.setNoteStaleDate.msgPrefs"/></td>
            <td style="color: white" class="MainTableTopRowRightColumn"><fmt:setBundle basename="oscarResources"/><fmt:message key="provider.setNoteStaleDate.msgProviderStaleDate"/></td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">&nbsp;</td>
            <td class="MainTableRightColumn">
                <%
                    if (request.getAttribute("status") == null) {

                %> <form style="frmProperty" action="${pageContext.request.contextPath}/setProviderStaleDate.do" method="post">
                <input type="hidden" id="method" name="method" value="save">
                <input type="hidden" name="name" id="name"/>
                <input type="hidden" name="providerNo" id="providerNo"/>
                <input type="hidden" name="id" id="id"/>
                <fmt:setBundle basename="oscarResources"/><fmt:message key="provider.setNoteStaleDate.msgEdit"/>
                <select name="dateProperty.value" id="staleDate">
                    <option value="A">All</option>
                    <option value="0">0</option>
                    <option value="-1">1</option>
                    <option value="-2">2</option>
                    <option value="-3">3</option>
                    <option value="-4">4</option>
                    <option value="-5">5</option>
                    <option value="-6">6</option>
                    <option value="-7">7</option>
                    <option value="-8">8</option>
                    <option value="-9">9</option>
                    <option value="-10">10</option>
                    <option value="-11">11</option>
                    <option value="-12">12</option>
                    <option value="-13">13</option>
                    <option value="-14">14</option>
                    <option value="-15">15</option>
                    <option value="-16">16</option>
                    <option value="-17">17</option>
                    <option value="-18">18</option>
                    <option value="-19">19</option>
                    <option value="-20">20</option>
                    <option value="-21">21</option>
                    <option value="-22">22</option>
                    <option value="-23">23</option>
                    <option value="-24">24</option>
                    <option value="-25">25</option>
                    <option value="-26">26</option>
                    <option value="-27">27</option>
                    <option value="-28">28</option>
                    <option value="-29">29</option>
                    <option value="-30">30</option>
                    <option value="-31">31</option>
                    <option value="-32">32</option>
                    <option value="-33">33</option>
                    <option value="-34">34</option>
                    <option value="-35">35</option>
                    <option value="-36">36</option>
                </select>
                <br/>
                <input type="hidden" name="name" id="name"/>
                <input type="hidden" name="providerNo" id="providerNo"/>
                <input type="hidden" name="id" id="id"/>
                Use Single Line View:
                <select name="singleViewProperty.value" id="staleDate">
                    <option value="no">No</option>
                    <option value="yes">Yes</option>
                </select>

                <br/>
                <input type="submit"
                       value="<fmt:setBundle basename="oscarResources"/><fmt:message key="provider.setNoteStaleDate.btnSubmit"/>"/>
                <input type="submit" onclick="$('method').value='remove';"
                       value="<fmt:setBundle basename="oscarResources"/><fmt:message key="provider.setNoteStaleDate.btnReset"/>"/>
            </form> <%
            } else {
            %> <fmt:setBundle basename="oscarResources"/><fmt:message key="provider.setNoteStaleDate.msgSuccess"/>
                <br>

                <%
                    }
                %>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn"></td>
            <td class="MainTableBottomRowRightColumn"></td>
        </tr>
    </table>
    </body>
</html>
