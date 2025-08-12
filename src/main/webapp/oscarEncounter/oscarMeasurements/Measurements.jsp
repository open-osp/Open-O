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
<!DOCTYPE html>
<%
    if (session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page
        import="oscar.oscarEncounter.oscarMeasurements.bean.EctMeasuringInstructionBeanHandler, oscar.oscarEncounter.oscarMeasurements.bean.EctMeasuringInstructionBean" %>
<%@ page import="org.oscarehr.managers.MeasurementManager" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%
    String demo = (String) request.getAttribute("demographicNo"); //bean.getDemographicNo();

    MeasurementManager measurementManager = SpringUtils.getBean(MeasurementManager.class);
    String groupName = (String) request.getAttribute("groupName");
%>

<html>

    <head>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <title><c:if test="${not empty groupName}">
            <c:out value="${groupName}"/>
        </c:if> <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarEncounter.Index.measurements"/></title>

        <base href="<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" %>">


        <link href="/oscar/css/bootstrap.css" rel="stylesheet" type="text/css">
        <link href="/oscar/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">


        <link rel="stylesheet" href="/oscar/css/font-awesome.min.css">

        <style>
            body {
                line-height: 14px;
            }

            h3 {
                line-height: 14px;
            }

            .note {
                padding: 0px;
                font-size: 12px;
            }

            .table td {
                line-height: 14px;
                padding: 3px;
            }

            .MainTableLeftColumn {
                vertical-align: top;
                padding: 14px;
            }
        </style>
        <script src="${ pageContext.request.contextPath }/library/jquery/jquery-3.6.4.min.js"></script>
        <script src="${ pageContext.request.contextPath }/share/calendar/calendar.js"></script>
        <script src="${ pageContext.request.contextPath }/share/calendar/lang/<fmt:setBundle basename="oscarResources"/><fmt:message key="global.javascript.calendar"/>"></script>
        <script src="${ pageContext.request.contextPath }/share/calendar/calendar-setup.js"></script>
        <link rel="stylesheet" type="text/css" media="all"
              href="${ pageContext.request.contextPath }/share/calendar/calendar.css" title="win2k-cold-2"/>

        <script type="text/javascript">

            function write2Parent(text) {

                self.close();
                opener.document.encForm.enTextarea.value = opener.document.encForm.enTextarea.value + text;
            }

            function getDropboxValue(ctr) {
                var selectedItem = document.forms[0].value(inputMInstrc - ctr).options[document.forms[0].value(inputMInstrc - ctr).selectedIndex].value;
                alert("hello!");
            }

            function popupPage(vheight, vwidth, page) { //open a new popup window

                windowprops = "height=" + vheight + ",width=" + vwidth + ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
                var popup = window.open(page, "blah", windowprops);
            }

            parentChanged = false;

            function check() {
                var ret = true;

                if (parentChanged) {
                    document.forms[0].elements["value(parentChanged)"].value = "true";

                    if (!confirm("<fmt:setBundle basename="oscarResources"/><fmt:message key="oscarEncounter.oscarMeasurements.Measurements.msgParentChanged"/> <oscar:nameage demographicNo="<%=demo%>"/>"))
                        ret = false;
                }

                if (ret) {

                    $.post('<%=request.getContextPath()%>/oscarEncounter/Measurements.do?ajax=true&skipCreateNote=true', $('#theForm').serialize(), function (data) {
                        $("#errors_list").empty();
                        if (data.errors) {
                            $("#errors_list").prepend("<div class='alert alert-error'>");
                            for (var x = 0; x < data.errors.length; x++) {
                                $(".alert").append(data.errors[x]);
                            }

                        } else {
                            opener.postMessage(data, "*");
                            window.close();
                        }
                    }, 'json');

                }
            }
        </script>
    </head>
    <body class="BodyStyle" onload="window.focus();">
    <form action="${pageContext.request.contextPath}/oscarEncounter/Measurements.do" method="post" styleId="theForm">
        <c:if test="${not empty css}">
            <link rel="stylesheet" type="text/css" href="${css}">
        </c:if>
        <c:if test="${empty css}">
            <!--<link rel="stylesheet" type="text/css" href="styles/measurementStyle.css">-->
        </c:if>

        <table class="MainTable" id="scrollNumber1">
            <tr class="MainTableTopRow">
                <td class="MainTableTopRowLeftColumn"><h4>
                    <c:if test="${not empty groupName}">
                    <h4>${groupName}</h4>
                    </c:if>
                </td>
                <td class="MainTableTopRowRightColumn" style="padding:0px">
                    <table class="TopStatusBar" style="width:100%; height:100%;">
                        <tr>
                            <td class="Header"><h3><oscar:nameage demographicNo="<%=demo%>"/></h3></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td class="MainTableLeftColumn">
                    <table>
                        <tr>
                            <td><a
                                    href="javascript: function myFunction() {return false; }"
                                    onClick="popupPage(150,200,'<%=request.getContextPath()%>/oscarEncounter/calculators.jsp?demo=<%=demo%>'); return false;"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarEncounter.Index.calculators"/></a></td>
                        </tr>
                    </table>
                </td>
                <td class="MainTableRightColumn">

                    <%=measurementManager.getDShtml(groupName)%>
                    <ul id="errors_list" style="color:red">
                    </ul>

                    <table>
                        <tr>
                            <td>
                                <table>
                                    <tr>
                                        <td>
                                            <div class="well">
                                                <table class="table table-striped">
                                                    <% 
    java.util.List<String> actionErrors = (java.util.List<String>) request.getAttribute("actionErrors");
    if (actionErrors != null && !actionErrors.isEmpty()) {
%>
    <div class="action-errors">
        <ul>
            <% for (String error : actionErrors) { %>
                <li><%= error %></li>
            <% } %>
        </ul>
    </div>
<% } %>
                                                    <tr class="Header">
                                                        <th style="width:120px"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarEncounter.oscarMeasurements.Measurements.headingType"/>
                                                        </th>
                                                        <th style="width:160px"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarEncounter.oscarMeasurements.Measurements.headingMeasuringInstrc"/>
                                                        </th>
                                                        <th style="width:30px"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarEncounter.oscarMeasurements.Measurements.headingValue"/>
                                                        </th>
                                                        <th style="width:40px"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarEncounter.oscarMeasurements.Measurements.headingObservationDate"/>
                                                        </th>
                                                        <th style="width:80px"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarEncounter.oscarMeasurements.Measurements.headingComments"/>
                                                        </th>
                                                        <th style="width:10px"></th>
                                                    </tr>
                                                    <% int i = 0;%>
                                                    <c:forEach var="measurementType" items="${measurementTypes.measurementTypeVector}" varStatus="ctr">
                                                        <tr class="data" id="row-${measurementType.type}">
                                                            <td>
                                                                <span title="${measurementType.typeDesc}">${measurementType.typeDisplayName}</span>
                                                            </td>
                                                            <td>
                                                                <c:set var="attributeName" value="mInstrcs${ctr.index}" />
                                                                <c:forEach var="mInstrc" items="${pageContext.request.getAttribute(attributeName).measuringInstructionList}">
                                                                    <input type="radio" name="value(inputMInstrc-${ctr.index})" value="${mInstrc.measuringInstrc}" checked />
                                                                    ${mInstrc.measuringInstrc}<br>
                                                                </c:forEach>
                                                            </td>

                                                            <c:choose>
                                                                <c:when test="${measurementType.measuringInstrc.startsWith('Choose radio')}">
                                                                    <td>
                                                                        <c:forEach var="option" items="${fn:split(measurementType.measuringInstrc.substring(12), ',')}">
                                                                            <input type="radio" name="value(inputValue-${ctr.index})" value="${fn:trim(option)}"> ${fn:trim(option)}&nbsp;
                                                                        </c:forEach>
                                                                    </td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <td><input type="text" class="input-small" name="value(inputValue-${ctr.index})" id="inputValue-${ctr.index}"/></td>
                                                                </c:otherwise>
                                                            </c:choose>

                                                            <td><input type="text" class="input-medium" name="value(date-${ctr.index})" id="date-${ctr.index}"/></td>
                                                            <script>
                                                                Calendar.setup({
                                                                    inputField: "date-${ctr.index}",
                                                                    ifFormat: "%Y-%m-%d",
                                                                    button: "date-${ctr.index}"
                                                                });
                                                            </script>

                                                            <td><input type="text" class="input-large" name="value(comments-${ctr.index})" id="comments-${ctr.index}"/></td>
                                                            <td>
                                                                <input type="hidden" name="value(inputType-${ctr.index})" value="${measurementType.type}"/>
                                                                <input type="hidden" name="value(inputTypeDisplayName-${ctr.index})" value="${measurementType.typeDisplayName}"/>
                                                                <input type="hidden" name="value(validation-${ctr.index})" value="${measurementType.validation}"/>
                                                            </td>
                                                        </tr>

                                                        <c:if test="${not empty measurementType.lastMInstrc}">
                                                            <tr class="note">
                                                                <td><fmt:message key="oscarEncoutner.oscarMeasurements.msgTheLastValue"/>:</td>
                                                                <td>&nbsp;${measurementType.lastMInstrc}</td>
                                                                <td>&nbsp;${measurementType.lastData}</td>
                                                                <td>&nbsp;${measurementType.lastDateEntered}</td>
                                                                <td>&nbsp;${measurementType.lastComments}</td>
                                                                <td>
                                                                    <i class="icon-time icon-large" title="<fmt:message key='oscarEncounter.Index.oldMeasurements'/>"
                                                                       onclick="popupPage(300,800,'oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?type=${measurementType.type}'); return false;">
                                                                    </i>
                                                                </td>
                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>

                                                    <input type="hidden" name="value(numType)" value="${fn:length(measurementTypes.measurementTypeVector)}"/>
                                                    <input type="hidden" name="value(groupName)" value="${groupName}"/>
                                                    <input type="hidden" name="value(parentChanged)" value="false"/>
                                                    <input type="hidden" name="value(demographicNo)" value="${demo}"/>
                                                    <input type="hidden" name="demographic_no" value="${demo}"/>

                                                    <c:if test="${not empty css}">
                                                        <input type="hidden" name="value(css)" value="${css}"/>
                                                    </c:if>
                                                    <c:if test="${empty css}">
                                                        <input type="hidden" name="value(css)" value=""/>
                                                    </c:if>

                                                </table>
                                            </div> <!-- well -->
                                            <table>
                                                <tr>
                                                    <td><input type="button" name="Button" class="btn"
                                                               value="<fmt:setBundle basename="oscarResources"/><fmt:message key="global.btnCancel"/>"
                                                               onClick="window.close()"></td>
                                                    <td><input type="button" name="Button" class="btn btn-primary"
                                                               value="<fmt:setBundle basename="oscarResources"/><fmt:message key="global.btnSubmit"/>"
                                                               onclick="check();"/></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td class="MainTableBottomRowLeftColumn"></td>
                <td class="MainTableBottomRowRightColumn"></td>
            </tr>
        </table>
    </form>

    <script>
        $(document).ready(function () {

//if WT, HT and BMI exists then allow the link
            if ($('#row-WT').length && $('#row-HT').length && $('#row-BMI').length) {

                $('#row-WT td:eq(2) input').on("keyup", function () {
                    calcBMI($(this).val(), $('#row-HT td:eq(2) input').val());
                });

                $('#row-HT td:eq(2) input').on("keyup", function () {
                    calcBMI($('#row-WT td:eq(2) input').val(), $(this).val());
                });
            }

        });

        var utc = new Date().toJSON().slice(0, 10);
        $("[id^=date-]").val(utc);

        function calcBMI(w, h) {
            b = '';

            if (!isNaN(parseFloat(w)) && !isNaN(parseFloat(h)) && h !== "" && w !== "") {
                if (h > 0) {
                    b = (w / Math.pow(h / 100, 2)).toFixed(1);
                    $('#row-BMI td:eq(2) input').val(b);
                    $('#row-BMI').css("background-color", "#d9e6f2");
                }
            }
        }
    </script>


    </body>
</html>
