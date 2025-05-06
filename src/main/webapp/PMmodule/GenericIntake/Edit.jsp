<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>


<%-- Updated by Eugene Petruhin on 30 dec 2008 while fixing #2456688 --%>

<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@ include file="/taglibs.jsp" %>
<%@ page import="org.oscarehr.PMmodule.model.Intake" %>
<%@ page import="org.oscarehr.PMmodule.model.IntakeNodeJavascript" %>
<%@ page import="org.oscarehr.PMmodule.web.formbean.GenericIntakeEditFormBean" %>
<%@ page import="org.oscarehr.PMmodule.web.ProgramUtils" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="oscar.OscarProperties" %>

<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ page import="org.oscarehr.util.SessionConstants" %>
<%
    LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

    GenericIntakeEditFormBean intakeEditForm = (GenericIntakeEditFormBean) session.getAttribute("genericIntakeEditForm");
    Intake intake = intakeEditForm.getIntake();
    String clientId = String.valueOf(intake.getClientId());
    String intakeType = intake.getType();

    String intakeFrmDate = intake.getNode().getPublishDateStr();
    String intakeFrmName = intake.getNode().getLabelStr();
    Integer intakeFrmVer = intake.getNode().getForm_version();
    intakeFrmName += intakeFrmVer == null ? " (1)" : " (" + intakeFrmVer + ")";

    boolean readOnlyDates = Boolean.valueOf(OscarProperties.getInstance().getProperty("intake.readonly_dates", "true"));
%>

<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="java.util.GregorianCalendar" %>
<%@page import="java.util.Calendar" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormatSymbols" %>
<%@page import="org.apache.commons.lang.time.DateFormatUtils" %>
<%@page import="org.oscarehr.common.model.Demographic" %>
<html>
    <head>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/check_hin.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.form.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.metadata.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.validate.min.js"></script>


        <title>Generic Intake Edit</title>
        <style type="text/css">
            @import "${request.contextPath}/css/genericIntake.css";
        </style>
        <link rel="stylesheet" type="text/css" href='${request.contextPath}/share/calendar/skins/aqua/theme.css'/>
        <script type="text/javascript" src="${request.contextPath}/share/calendar/calendar.js"/>
        </script>
        <script type="text/javascript" src="${request.contextPath}/share/calendar/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${request.contextPath}/share/calendar/calendar-setup.js"></script>

        <script type="text/javascript">
            <!--
            var djConfig = {
                isDebug: false,
                parseWidgets: false,
                searchIds: ["layoutContainer", "topPane", "clientPane", "bottomPane", "clientTable", "admissionsTable"]
            };

            var programMaleOnly =<%=session.getAttribute("programMaleOnly")%>;
            var programFemaleOnly =<%=session.getAttribute("programFemaleOnly")%>;
            var programTransgenderOnly =<%=session.getAttribute("programTransgenderOnly")%>;

            var oldProgramId = <%=session.getAttribute("intakeCurrentBedCommunityId")%>;
            var isClientDependentOfFamily = <%=session.getAttribute(SessionConstants.INTAKE_CLIENT_IS_DEPENDENT_OF_FAMILY)%>;

            <%=session.getAttribute("programAgeValidationMethod")%>
            <%=session.getAttribute("admitToNewFacilityValidationMethod")%>

            // -->

            function openSurvey(ctl) {
                var formId = ctl.options[ctl.selectedIndex].value;
                if (formId == 0) {
                    return;
                }
                var id = document.getElementById('formInstanceId').value;
                var url = '<%=request.getContextPath() %>/PMmodule/Forms/SurveyExecute.do?method=survey&type=provider&formId=' + formId + '&formInstanceId=' + id + '&clientId=' + <%=clientId%>;
                ctl.selectedIndex = 0;
                popupPage(url);
            }

            function popupPage(varpage) {
                var page = "" + varpage;
                windowprops = "height=600,width=700,location=no,"
                    + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
                window.open(page, "", windowprops);
            }

            function saveForm() {
                var fail = false;
                $("[name='client.sex']").each(function () {
                    var v = $(this).val();
                    if (v.length == 0) {
                        $("#genderreq").css("display", "inline");
                        fail = true;
                    }
                });

                if (fail) return false;

                if (check_mandatory()) {
                    return save();
                }
                return false;
            }

            function saveDraft() {
                document.getElementById('skip_validate').value = 'true';
                $("form[onsubmit]").each(function (idx, form) {
                    var onsubmit = $(form).attr("onsubmit");
                    //alert(onsubmit);
                    $(form)
                        .removeAttr("onsubmit")
                        .submit(new Function(onsubmit));

                });


                return save_draft();
            }

            function check_mandatory() {
                var mquestSingle = new Array();
                var mquestMultiIdx = new Array();
                var mquestMultiName = new Array();
                var mqs = 0;
                var mqm = 0;
                var ret = false;
                for (i = 0; i < document.forms[0].elements.length; i++) {
                    if (document.forms[0].elements[i].name.substring(0, 7) == "mquests") {
                        mquestSingle[mqs] = document.forms[0].elements[i].value;
                        mqs++;
                    } else if (document.forms[0].elements[i].name.substring(0, 7) == "mquestm") {
                        mquestMultiIdx[mqm] = document.forms[0].elements[i].name;
                        mquestMultiName[mqm] = document.forms[0].elements[i].value;
                        mqm++;
                    }
                }
                ret = check_mandatory_single(mquestSingle);
                if (ret) {
                    ret = check_mandatory_multi(mquestMultiIdx, mquestMultiName);
                }
                if (!ret) {
                    alert("All mandatory questions must be answered!");
                }
                return ret;
            }

            function check_mandatory_single(mqSingle) {
                var errFree = true;
                for (i = 0; i < document.forms[0].elements.length; i++) {
                    for (j = 0; j < mqSingle.length; j++) {
                        if (document.forms[0].elements[i].name == mqSingle[j]) {
                            errFree = checkfilled(document.forms[0].elements[i]);
                        }
                        break;
                    }
                    if (!errFree) {
                        break;
                    }
                }
                return errFree;
            }

            function check_mandatory_multi(mqIndex, mqName) {
                var errFree = true;
                var ans_ed = 0;

                for (i = 0; i < mqIndex.length; i++) {
                    for (j = 0; j < document.forms[0].elements.length; j++) {
                        if (document.forms[0].elements[j].name == mqName[i]) {
                            if (checkfilled(document.forms[0].elements[j])) {
                                ans_ed++;
                            }
                            break;
                        }
                    }
                    if (i == mqIndex.length - 1 || (i < mqIndex.length - 1 && nxtGrp(mqIndex[i], mqIndex[i + 1]))) {
                        if (ans_ed == 0) {
                            errFree = false;
                            break;
                        } else {
                            ans_ed = 0;
                        }
                    }
                }
                return errFree;
            }

            function nxtGrp(first, second) {
                var mrk = first.lastIndexOf('_') + 1;
                var firstIdx = first.substring(mrk, first.length);
                mrk = second.lastIndexOf('_') + 1;
                var secondIdx = second.substring(mrk, second.length);

                return (firstIdx != secondIdx);
            }

            function checkfilled(elem) {
                if (elem.type == "text" || elem.type == "textarea") {
                    if (elem.value.replace(" ", "") == "") {
                        return false;
                    }
                } else if (elem.type == "checkbox") {
                    if (!elem.checked) {
                        return false;
                    }
                }
                return true;
            }

        </script>
        <script type="text/javascript" src="${request.contextPath}/dojoAjax/dojo.js"></script>
        <script type="text/javascript" src="${request.contextPath}/js/AlphaTextBox.js"></script>
        <script type="text/javascript">
            <!--
            dojo.require("dojo.widget.*");
            dojo.require("dojo.validate.*");
            // -->
        </script>
        <script type="text/javascript" src="${request.contextPath}/js/genericIntake.js"></script>
        <script type="text/javascript" src="${request.contextPath}/js/checkDate.js"></script>


        <script type="text/javascript">
            $("document").ready(function () {
                $("a.repeat_remove").live('click', function (e) {
                    e.preventDefault();
                    $(this).parent().parent().remove();
                });
            });

            $("document").ready(function () {
                $("form").submit(function (e) {

                    $("table").each(function () {
                        var repeats = $(this).find("input[repeat='true']");
                        if (repeats.length > 0) {
                            repeats.attr("name", function (idx) {
                                var nodeId = $(this).attr("nodeId");
                                return 'intake.answerMapped(' + nodeId + '-' + idx + ').value';
                            });
                            var nodeId = repeats.eq(0).attr("nodeId");
                            var mydom = "<input type='hidden' name='repeat_size' value='" + nodeId + "-" + repeats.length + "'/>";
                            $(this).append(mydom);
                        }

                        var srepeats = $(this).find("select[repeat='true']");
                        if (srepeats.length > 0) {
                            srepeats.attr("name", function (idx) {
                                var nodeId = $(this).attr("nodeId");
                                return 'intake.answerMapped(' + nodeId + '-' + idx + ').value';
                            });
                            var nodeId = srepeats.eq(0).attr("nodeId");
                            var mydom = "<input type='hidden' name='repeat_size' value='" + nodeId + "-" + srepeats.length + "'/>";
                            $(this).append(mydom);
                        }

                    });
                    //alert($(this).serialize());
                });
            });

            $("document").ready(function () {
                $("form").validate({meta: "validate"});

            });

        </script>

        <%
            if (intakeEditForm.getJsLocation() != null) {
                for (IntakeNodeJavascript js : intakeEditForm.getJsLocation()) {
        %>
        <script type="text/javascript" src="<%= request.getContextPath() %><%=js.getLocation()%>"></script>
        <%
                }
            }

        %>


        <style>
            .repeat_remove {
                font-weight: bold;
                text-decoration: none;
            }

            .repeat_add {
                font-weight: bold;
                text-decoration: none;
            }
        </style>
        <base href="<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" %>">
    </head>
    <body class="edit">

    <form action="${pageContext.request.contextPath}/PMmodule/GenericIntake/Edit.do" method="post" onsubmit="return validateEdit()">
        <input type="hidden" name="method" id="method"/>
        <input type="hidden" name="currentBedCommunityProgramId_old"
               value="<%=session.getAttribute("intakeCurrentBedCommunityId")%>"/>
        <input type="hidden" name="intakeType" value="<%=intakeType %>"/>
        <input type="hidden" name="type" value="<%=intakeType %>"/>
        <input type="hidden" name="remoteFacilityId"
               value="<%=StringUtils.trimToEmpty(request.getParameter("remoteFacilityId"))%>"/>
        <input type="hidden" name="remoteDemographicId"
               value="<%=StringUtils.trimToEmpty(request.getParameter("remoteDemographicId"))%>"/>
        <input type="hidden" name="skip_validate" id="skip_validate" value="false"/>
        <input type="hidden" id="facility_name" name="facility_name"
               value="<%=loggedInInfo.getCurrentFacility().getName()%>"/>
        <input type="hidden" id="ocan_service_org_number" name="ocan_service_org_number"
               value="<%=loggedInInfo.getCurrentFacility().getOcanServiceOrgNumber()%>"/>

        <!-- If this is from adding appointment screen, save the intake and go back to there -->
        <input type="hidden" name="fromAppt" value="<%=request.getParameter("fromAppt")%>">
        <input type="hidden" name="originalPage" value="<%=request.getParameter("originalPage")%>">
        <input type="hidden" name="bFirstDisp" value="<%=request.getParameter("bFirstDisp")%>">
        <input type="hidden" name="provider_no" value="<%=request.getParameter("provider_no")%>">
        <input type="hidden" name="start_time" value="<%=request.getParameter("start_time")%>">
        <input type="hidden" name="end_time" value="<%=request.getParameter("end_time")%>">
        <input type="hidden" name="duration" value="<%=request.getParameter("duration")%>">
        <input type="hidden" name="year" value="<%=request.getParameter("year")%>">
        <input type="hidden" name="month" value="<%=request.getParameter("month")%>">
        <input type="hidden" name="day" value="<%=request.getParameter("day")%>">
        <input type="hidden" name="appointment_date" value="<%=request.getParameter("appointment_date")%>">
        <input type="hidden" name="notes" value="<%=request.getParameter("notes")%>">
        <input type="hidden" name="reason" value="<%=request.getParameter("reason")%>">
        <input type="hidden" name="location" value="<%=request.getParameter("location")%>">
        <input type="hidden" name="resources" value="<%=request.getParameter("resources")%>">
        <input type="hidden" name="apptType" value="<%=request.getParameter("apptType")%>">
        <input type="hidden" name="style" value="<%=request.getParameter("style")%>">
        <input type="hidden" name="billing" value="<%=request.getParameter("billing")%>">
        <input type="hidden" name="status" value="<%=request.getParameter("status")%>">
        <input type="hidden" name="createdatetime" value="<%=request.getParameter("createdatetime")%>">
        <input type="hidden" name="creator" value="<%=request.getParameter("creator")%>">
        <input type="hidden" name="remarks" value="<%=request.getParameter("remarks")%>">


        <div id="layoutContainer" dojoType="LayoutContainer" layoutChildPriority="top-bottom"
             class="intakeLayoutContainer">
            <div id="topPane" dojoType="ContentPane" layoutAlign="top" class="intakeTopPane">
                <c:out value="${sessionScope.genericIntakeEditForm.title}"/>
            </div>
            <div id="clientPane" dojoType="ContentPane" layoutAlign="client" class="intakeChildPane">

                <div id="clientTable" dojoType="TitlePane" label="Client Information"
                     title="(Fields marked with * are mandatory)"
                     labelNodeClass="intakeSectionLabel" containerNodeClass="intakeSectionContainer">
                    <table class="intakeTable">
                        <tr>
                            <td><label>First Name<br><input type="text" name="client.firstName" size="20"
                                                                maxlength="30"/></label></td>
                            <td><label>Last Name<br><input type="text" name="client.lastName" size="20"
                                                               maxlength="30"/></label></td>
                            <td>
                                <label>Gender<br>
                                    <select name="client.sex" id="client.sex">
                                        <option value=""></option>
                                        <c:forEach var="gender" items="${genders}">
                                            <option value="${gender.code}">
                                                    ${gender.description}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </label>
                                <span id="genderreq" style="display:none;color:red">* Value is required.</span>
                            </td>
                            <td>
                                <table>
                                    <tr>
                                        <td colspan="3">
                                            <label>Date of Birth</label><br/>
                                            <input id="client.formattedDob" name="client.formattedDob"
                                                   value="<%=intakeEditForm.getClient().getFormattedDob() %>" <%=(readOnlyDates?"readonly=\"readonly\" onfocus=\"this.blur()\"":"") %>
                                                   type="text"><img title="Calendar" id="cal_dob"
                                                                    src="<%= request.getContextPath() %>/images/cal.gif" alt="Calendar"
                                                                    border="0">
                                            <script type="text/javascript">Calendar.setup({
                                                inputField: 'client.formattedDob',
                                                ifFormat: '%Y-%m-%d',
                                                button: 'cal_dob',
                                                align: 'cr',
                                                singleClick: true,
                                                firstDay: 1
                                            });</script>
                                        </td>
                                    </tr>
                                </table>

                            </td>
                        </tr>
                        <tr>
                            <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
                                <td><label>Alias<br><input type="text" size="40" maxlength="70"
                                                               name="client.alias"/></label>
                                </td>
                            </caisi:isModuleLoad>

                            <caisi:isModuleLoad moduleName="GET_OHIP_INFO" reverse="false">
                                <td><label>Health Card #<br>
                                    <input type="text" size="10" maxlength="10" dojoType="IntegerTextBox"
                                           name="client.hin"
                                           value="<c:out value="${genericIntakeEditForm.client.hin}"/>"/>
                                </label></td>
                                <td><label>HC Version<br>
                                    <input type="text" size="2" maxlength="2" dojoType="AlphaTextBox" name="client.ver"
                                           value="<c:out value="${genericIntakeEditForm.client.ver}"/>"/>
                                </label></td>
                                <td>
                                    <label>HC Type</label>
                                    <br/>
                                    <select name="client.hcType" id="client.hcType">
                                        <c:forEach var="province" items="${provinces}">
                                            <option value="${province.value}">
                                                    ${province.label}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <table style="border-collapse:collapse">
                                        <tr>
                                            <td><label>HC Effective date</label></td>
                                            <td><label>HC Renewal Date</label></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <%
                                                    String effDateVal = "";
                                                    if (intakeEditForm.getClient() != null && intakeEditForm.getClient().getEffDate() != null) {
                                                        effDateVal = intakeEditForm.getClient().getFormattedEffDate();
                                                    }
                                                %>
                                                <input style="width:8em" id="client.formattedEffDate"
                                                       name="client.formattedEffDate"
                                                       value="<%=effDateVal %>" <%=(readOnlyDates?"readonly=\"readonly\" onfocus=\"this.blur()\"":"") %>
                                                       type="text"><img title="Calendar" id="cal_effdate"
                                                                        src="<%= request.getContextPath() %>/images/cal.gif" alt="Calendar"
                                                                        border="0">
                                                <script type="text/javascript">Calendar.setup({
                                                    inputField: 'client.formattedEffDate',
                                                    ifFormat: '%Y-%m-%d',
                                                    button: 'cal_effdate',
                                                    align: 'cr',
                                                    singleClick: true,
                                                    firstDay: 1
                                                });</script>
                                            </td>
                                            <td>
                                                <%
                                                    String renewDateVal = "";
                                                    if (intakeEditForm.getClient() != null && intakeEditForm.getClient().getHcRenewDate() != null) {
                                                        renewDateVal = intakeEditForm.getClient().getFormattedRenewDate();
                                                    }
                                                %>
                                                <input style="width:8em" id="client.formattedRenewDate"
                                                       name="client.formattedRenewDate"
                                                       value="<%=renewDateVal%>" <%=(readOnlyDates?"readonly=\"readonly\" onfocus=\"this.blur()\"":"") %>
                                                       type="text"><img title="Calendar" id="cal_renewdate"
                                                                        src="<%= request.getContextPath() %>/images/cal.gif" alt="Calendar"
                                                                        border="0">
                                                <script type="text/javascript">Calendar.setup({
                                                    inputField: 'client.formattedRenewDate',
                                                    ifFormat: '%Y-%m-%d',
                                                    button: 'cal_renewdate',
                                                    align: 'cr',
                                                    singleClick: true,
                                                    firstDay: 1
                                                });</script>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </caisi:isModuleLoad>
                        </tr>

                        <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
                            <tr>
                                <td>
                                    <label>Email<br>
                                        <input type="text" size="20" maxlength="100" dojoType="EmailTextBox"
                                               name="client.email"
                                               value="<c:out value="${genericIntakeEditForm.client.email}"/>"/>
                                    </label></td>
                                <td>
                                    <label>Phone #<br>
                                        <input type="text" size="20" maxlength="20" dojoType="UsPhoneNumberTextbox"
                                               name="client.phone"
                                               value="<c:out value="${genericIntakeEditForm.client.phone}"/>"/>
                                    </label></td>
                                <td>
                                    <label>Secondary Phone #<br>
                                        <input type="text" size="20" maxlength="20" dojoType="UsPhoneNumberTextbox"
                                               name="client.phone2"
                                               value="<c:out value="${genericIntakeEditForm.client.phone2}"/>"/>
                                    </label>
                                </td>
                                <td>
                                    <label>Anonymous</label>
                                    <br/>
                                    <%
                                        String anonymous = intakeEditForm.getClient().getAnonymous();
                                    %>
                                    <select name="anonymous">
                                        <option value="" <%=anonymous == null ? "selected=\"selected\"" : ""%>>not
                                            anonymous
                                        </option>
                                        <option value="<%=Demographic.ANONYMOUS%>" <%="anonymous".equals(Demographic.ANONYMOUS) ? "selected=\"selected\"" : ""%>>
                                            anonymous
                                        </option>
                                        <option value="<%=Demographic.UNIQUE_ANONYMOUS%>" <%="anonymous".equals(Demographic.UNIQUE_ANONYMOUS) ? "selected=\"selected\"" : ""%>>
                                            unique anonymous
                                        </option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td><label>Street<br><input type="text" size="20" maxlength="60"
                                                                name="client.address"/></label></td>
                                <td><label>City<br><input type="text" size="20" maxlength="20" name="client.city"/></label>
                                </td>
                                <td><label>Province<br>
                                    <select name="client.province" id="client.province">
                                        <c:forEach var="province" items="${provinces}">
                                            <option value="${province.value}">
                                                    ${province.label}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </label>
                                </td>
                                <td><label>Postal Code<br><input type="text" name="client.postal" size="9"
                                                                     maxlength="9"/></label></td>
                            </tr>
                            <tr>
                                <td><label>Chart No<br/><input type="text" size="20" maxlength="40" name="client.chartNo"/></label>
                                </td>
                                <td>
                                    <label>Roster Status<br/>
                                        <select name="client.rosterStatus" id="width: 120">
                                            <%String rosterStatus = ""; %>
                                            <option value=""></option>
                                            <option value="RO"><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographiceditdemographic.optRostered"/></option>
                                            <option value="NR"><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographiceditdemographic.optNotRostered"/></option>
                                            <option value="TE"><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographiceditdemographic.optTerminated"/></option>
                                            <option value="FS"><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographiceditdemographic.optFeeService"/></option>
                                            <%
                                                DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean(DemographicDao.class);
                                                List<String> statuses = demographicDao.getRosterStatuses();
                                                for (String status : statuses) {
                                            %>
                                            <option value="<%=status%>"><%=status%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </label>
                                </td>
                                <td><label>Doctor<br/><select name="client.providerNo" id="client.providerNo">
                                    <option value=""></option>
                                    <% ProviderDao providerDao = (ProviderDao) SpringUtils.getBean(ProviderDao.class);
                                        List<Provider> doctors = providerDao.getBillableProviders();
                                        for (Provider doctor : doctors) {
                                    %>
                                    <option value="<%=doctor.getProviderNo() %>"><%=doctor.getFormattedName() %>
                                    </option>
                                    <% }%>
                                </select></label></td>
                                <td></td>
                            </tr>
                            <oscar:oscarPropertiesCheck property="ENABLE_CME_ON_REG_INTAKE" value="true">
                                <c:if test="${not empty sessionScope.genericIntakeEditForm.client.demographicNo}">
                                    <tr>
                                        <td>
                                            <a href="javascript:void(0);"
                                               onclick="window.open('<caisi:CaseManagementLink
                                                       demographicNo="<%=intake.getClientId()%>"
                                                       providerNo="<%=intake.getStaffId()%>"
                                                       providerName="<%=intake.getStaffName()%>"/>', '', 'width=800,height=600,resizable=1,scrollbars=1')">
                                                <span>Case Management Notes</span>
                                            </a>
                                        </td>
                                    </tr>
                                </c:if>
                            </oscar:oscarPropertiesCheck>
                        </caisi:isModuleLoad>
                    </table>
                </div>

                <%if (!Intake.INDEPTH.equalsIgnoreCase(intakeType)) { %>
                <c:if test="${not empty sessionScope.genericIntakeEditForm.bedPrograms || not empty sessionScope.genericIntakeEditForm.communityPrograms || not empty sessionScope.genericIntakeEditForm.servicePrograms}">
                    <div id="admissionsTable" dojoType="TitlePane" label="Program Admissions"
                         labelNodeClass="intakeSectionLabel"
                         containerNodeClass="intakeSectionContainer">
                        <c:if test="${not empty savedMessage}">
                            <table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
                                    <tr>
                                        <td class="error"><c:out value="${savedMessage}"/></td>
                                    </tr>
                            </table>
                        </c:if>
                        <table class="intakeTable">
                            <tr>
                                <c:if test="${not empty sessionScope.genericIntakeEditForm.bedPrograms}">
                                    <td class="intakeBedCommunityProgramCell"><label><c:out
                                            value="${sessionScope.genericIntakeEditForm.bedProgramLabel}"/></label></td>
                                </c:if>
                                <c:if test="${not empty sessionScope.genericIntakeEditForm.servicePrograms}">
                                    <td><label>Service Programs</label></td>
                                </c:if>
                                <c:if test="${not empty sessionScope.genericIntakeEditForm.communityPrograms}">
                                    <td class="intakeBedCommunityProgramCell"><label><c:out
                                            value="${sessionScope.genericIntakeEditForm.communityProgramLabel}"/></label>
                                    </td>
                                </c:if>
                                <td><label>Admission Date</label></td>
                            </tr>
                            <tr>
                                <input type="hidden" name="remoteReferralId"
                                       value="<%=StringUtils.trimToEmpty(request.getParameter("remoteReferralId"))%>"/>
                                <c:if test="${not empty sessionScope.genericIntakeEditForm.bedPrograms}">
                                    <td class="intakeBedCommunityProgramCell">
                                        <select name="bedProgramId"
                                                     value='<%=request.getParameter("destinationProgramId")%>'>
                                            <c:forEach var="bedProgram" items="${bedPrograms}">
                                                <option value="${bedProgram.value}">
                                                        ${bedProgram.label}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </c:if>
                                <c:if test="${not empty sessionScope.genericIntakeEditForm.servicePrograms}">
                                    <td>
                                        <c:forEach var="serviceProgram"
                                                   items="${sessionScope.genericIntakeEditForm.servicePrograms}">
                                            <html-el:multibox property="serviceProgramIds"
                                                              value="${serviceProgram.value}"/>&nbsp;<c:out
                                                value="${serviceProgram.label}"/><br/>
                                        </c:forEach>
                                    </td>
                                </c:if>
                                <c:if test="${not empty sessionScope.genericIntakeEditForm.communityPrograms}">
                                    <td class="intakeBedCommunityProgramCell">
                                        <select name="communityProgramId"
                                                     value='<%=request.getParameter("destinationProgramId")%>'>
                                            <c:forEach var="communityProgram" items="${communityPrograms}">
                                                <option value="${communityProgram.value}">
                                                        ${communityProgram.label}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </c:if>
                                <td><input id="admissionDate" name="admissionDate" value=""
                                        <%=(readOnlyDates?"readonly=\"readonly\" onfocus=\"this.blur()\"":"") %>
                                           type="text"><img title="Calendar" id="cal_admissionDate"
                                                            src="<%=request.getContextPath()%>/images/cal.gif"
                                                            alt="Calendar" border="0">
                                    <script type="text/javascript">Calendar.setup({
                                        inputField: 'admissionDate',
                                        ifFormat: '%Y-%m-%d',
                                        button: 'cal_admissionDate',
                                        align: 'cr',
                                        singleClick: true,
                                        firstDay: 1
                                    });</script>
                                </td>
                            </tr>
                        </table>
                    </div>

                </c:if>
                <%} %>

                <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
                    <div id="admissionsTable" dojoType="TitlePane" label="Intake Location"
                         labelNodeClass="intakeSectionLabel"
                         containerNodeClass="intakeSectionContainer">
                        <table class="intakeTable">
                            <tr>
                                <c:if test="${not empty sessionScope.genericIntakeEditForm.programsInDomain}">
                                    <td class="intakeBedCommunityProgramCell"><label>
                                        <c:out
                                                value="${sessionScope.genericIntakeEditForm.programInDomainLabel}"/></label>
                                    </td>
                                </c:if>

                            </tr>
                            <tr>
                                <c:if test="${not empty sessionScope.genericIntakeEditForm.programsInDomain}">
                                    <td class="intakeBedCommunityProgramCell">
                                        <select name="programInDomainId" id="programInDomainId">
                                            <c:forEach var="p" items="${programsInDomain}">
                                                <option value="${p.value}">
                                                        ${p.label}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </c:if>
                            </tr>
                        </table>
                    </div>

                    <div id="admissionsTable" dojoType="TitlePane" label="External Referral"
                         labelNodeClass="intakeSectionLabel"
                         containerNodeClass="intakeSectionContainer">
                        <table class="intakeTable">
                            <tr>
                                <c:if test="${not empty sessionScope.genericIntakeEditForm.externalPrograms}">
                                    <td class="intakeBedCommunityProgramCell"><label><c:out
                                            value="${sessionScope.genericIntakeEditForm.externalProgramLabel}"/></label>
                                    </td>
                                </c:if>

                            </tr>
                            <tr>
                                <c:if test="${not empty sessionScope.genericIntakeEditForm.externalPrograms}">
                                    <td class="intakeBedCommunityProgramCell">
                                        <select name="externalProgramId" id="externalProgramId">
                                            <c:forEach var="externalProgram" items="${externalPrograms}">
                                                <option value="${ccexternalProgram.value}">
                                                        ${externalProgram.label}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </c:if>
                            </tr>
                        </table>
                    </div>
                    <br/>

                    <c:if test="${not empty sessionScope.genericIntakeEditForm.client.demographicNo}">
                        <div id="admissionsTable" dojoType="TitlePane" label="Intake Assessment"
                             labelNodeClass="intakeSectionLabel"
                             containerNodeClass="intakeSectionContainer">
                            <table class="intakeTable">
                                <tr>
                                    <td><input type="hidden" id="formInstanceId" value="0"/></td>
                                </tr>
                                <tr>
                                    <td>
                                        <select property="form.formId" onchange="openSurvey(this);">
                                            <option value="0">&nbsp;</option>
                                            <c:forEach var="survey" items="${survey_list}">
                                                <option value="<c:out value="${survey.formId}"/>"><c:out
                                                        value="${survey.description}"/></option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </c:if>
                </caisi:isModuleLoad>

                <br/>

                <caisi:intake base="<%=5%>" intake="<%=intake%>"/>
            </div>
            <div id="bottomPane" dojoType="ContentPane" layoutAlign="bottom" class="intakeBottomPane">
                <table class="intakeTable">
                    <c:if test="${not empty savedMessage}">
                        <tr>
                            <td class="error"><c:out value="${savedMessage}"/></td>
                        </tr>
                    </c:if>
                    <tr>
                        <td>
                            <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
                                <input type="submit" name="submit" value="Save" onclick="return saveForm();"/>&nbsp;
                            </caisi:isModuleLoad>
                            <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
                                <!--<input type="submit" onclick="return saveDraft();">Save As Draft</input>&nbsp; -->
                            </caisi:isModuleLoad>
                            <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
                                <!--
                                <c:choose>
                                    <c:when test="${not empty sessionScope.genericIntakeEditForm.client.demographicNo}">
                                        <input type="submit" name="submit" value="Save" onclick="return saveForm();" />&nbsp;
                                    </c:when>
                                    <c:otherwise>
                                        <input type="submit" onclick="return saveForm();">Save And Do Intake Accessment</input>&nbsp;
                                    </c:otherwise>
                                </c:choose>
                                -->
                                <input type="submit" name="submit" value="Temporary Save" onclick="return save_temp()"/>
                                <input type="submit" name="submit" value="Admit, Sign And Save" onclick="return save_admit()" />&nbsp;
                                <input type="submit" name="submit" value="Intake Without Admission, Sign And Save" onclick="return save_notAdmit()" />
                            </caisi:isModuleLoad>
                            <input type="reset" value="Reset"/>
                        </td>
                        <td align="right">
                            <c:choose>
                                <c:when test="${not empty sessionScope.genericIntakeEditForm.client.demographicNo}">
                                    <input type="submit"  onclick="clientEdit()" value="Close" />
                                    <input type="button" value="Back to Search" onclick="history.go(-1)"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="button" value="Back to Search" onclick="history.go(-1)"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </form>
    </body>
</html>
