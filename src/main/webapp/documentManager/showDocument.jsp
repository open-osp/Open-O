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
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<%@page import="java.text.SimpleDateFormat" %>
<%@ page
        import="ca.openosp.openo.utility.WebUtils" %>
<%@page import="org.apache.commons.text.StringEscapeUtils" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="org.owasp.encoder.Encode"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" prefix="e" %>
<%@ page import="ca.openosp.openo.log.*" %>
<%@ page import="ca.openosp.openo.util.ConversionUtils" %>
<%@page import="ca.openosp.openo.PMmodule.dao.ProviderDao" %>
<%@page import="ca.openosp.openo.lab.ca.all.*,ca.openosp.openo.mds.data.*" %>
<%@page import="ca.openosp.openo.commn.dao.*,ca.openosp.openo.commn.model.*,ca.openosp.openo.utility.SpringUtils" %>
<%@ page import="ca.openosp.openo.documentManager.EDocUtil" %>
<%@ page import="ca.openosp.openo.documentManager.EDoc" %>
<%@ page import="ca.openosp.openo.documentManager.IncomingDocUtil" %>
<%@ page import="ca.openosp.openo.log.LogAction" %>
<%@ page import="ca.openosp.openo.log.LogConst" %>
<%@ page import="ca.openosp.openo.lab.ca.all.AcknowledgementData" %>
<%@ page import="ca.openosp.openo.mds.data.ReportStatus" %>
<%@ page import="ca.openosp.openo.commn.dao.*" %>
<%@ page import="ca.openosp.openo.commn.model.*" %>
<%@ page import="ca.openosp.openo.utility.LoggedInInfo" %>
<%
    ProviderInboxRoutingDao providerInboxRoutingDao = SpringUtils.getBean(ProviderInboxRoutingDao.class);
    UserPropertyDAO userPropertyDAO = SpringUtils.getBean(UserPropertyDAO.class);
    OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
    ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);

    LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    String providerNo = loggedInInfo.getLoggedInProviderNo();
    UserProperty uProp = userPropertyDAO.getProp(providerNo, UserProperty.LAB_ACK_COMMENT);                        
    boolean skipComment = false;

    if (uProp != null && uProp.getValue().equalsIgnoreCase("yes")) {
        skipComment = true;
    }

    uProp = userPropertyDAO.getProp(providerNo, UserProperty.DISPLAY_DOCUMENT_AS);
    String displayDocumentAs = UserProperty.IMAGE;
    if (uProp != null && uProp.getValue().equals(UserProperty.PDF)) {
        displayDocumentAs = UserProperty.PDF;
    }

    String demoName = request.getParameter("demoName");
    String documentNo = request.getParameter("segmentID");
    
    String inQueue = request.getParameter("inQueue");

    boolean inQueueB = false;
    if (inQueue != null) {
        inQueueB = true;
    }

    String defaultQueue = IncomingDocUtil.getAndSetIncomingDocQueue(providerNo, null);
    QueueDao queueDao = SpringUtils.getBean(QueueDao.class);
    List<Hashtable> queues = queueDao.getQueues();
    int queueId = 1;
    if (defaultQueue != null) {
        defaultQueue = defaultQueue.trim();
        queueId = Integer.parseInt(defaultQueue);
    }

    String creator = (String) session.getAttribute("user");
    ArrayList doctypes = EDocUtil.getActiveDocTypes("demographic");
    EDoc curdoc = EDocUtil.getDoc(documentNo);

    String demographicID = curdoc.getModuleId();
    String mrpProviderName = "";
    if ((demographicID != null) && !demographicID.isEmpty() && !demographicID.equals("-1")) {
        DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean(DemographicDao.class);
        Demographic demographic = demographicDao.getDemographic(demographicID);  
				demoName = demographic.getLastName()+","+demographic.getFirstName();
        mrpProviderName = demographic.getProviderNo() == null || demographic.getProviderNo().isEmpty() ? "Unknown" : providerDao.getProviderNameLastFirst(demographic.getProviderNo());
        mrpProviderName = " (MRP: " + mrpProviderName + ")";
    } else {
      demoName = EDocUtil.getProviderName(providerNo);
    }
    LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, documentNo, request.getRemoteAddr(),demographicID);
    String docId = curdoc.getDocId();
    String ackFunc;
    if(skipComment) {
      ackFunc = "updateStatus('acknowledgeForm_" + Encode.forJavaScript(String.valueOf(docId)) + "'," + inQueueB + ");";
    } else {
      ackFunc = "getDocComment('" + Encode.forJavaScript(String.valueOf(docId)) + "','" + Encode.forJavaScript(String.valueOf(providerNo)) + "'," + inQueueB + ");";
    }

    int slash = 0;
    String contentType = "";
    if ((slash = curdoc.getContentType().indexOf('/')) != -1) {
        contentType = curdoc.getContentType().substring(slash + 1);
    }
    String dStatus = "";
    if ((curdoc.getStatus() + "").compareTo("A") == 0) {
        dStatus = "active";
    } else if ((curdoc.getStatus() + "").compareTo("H") == 0) {
        dStatus = "html";
    }
    int numOfPage = curdoc.getNumberOfPages();
    String numOfPageStr = "";
    if (numOfPage == 0)
        numOfPageStr = "unknown";
    else
        numOfPageStr = (new Integer(numOfPage)).toString();
    String cp = request.getContextPath();
    String url = cp + "/documentManager/ManageDocument.do?method=viewDocPage&doc_no=" + docId + "&curPage=1";
    String url2 = cp + "/documentManager/ManageDocument.do?method=display&doc_no=" + docId;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    Integer docCurrentFiledQueue = null;

    request.setAttribute("mrpProviderName", mrpProviderName);
    request.setAttribute("demoName", demoName);
%>

<fmt:setBundle basename="oscarResources"/>

<c:if test="${param.inWindow eq 'true'}">
    <html>
    <head>
        <script type="text/javascript">
            const ctx = "${pageContext.servletContext.contextPath}";
        </script>

        <link rel="stylesheet" type="text/css"
              href="${pageContext.servletContext.contextPath}/library/jquery/jquery-ui.theme-1.12.1.min.css"/>
        <link rel="stylesheet" type="text/css"
              href="${pageContext.servletContext.contextPath}/library/jquery/jquery-ui.structure-1.12.1.min.css"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/css/showDocument.css"/>

        <script type="text/javascript"
                src="${pageContext.servletContext.contextPath}/share/calendar/calendar.js"></script>
        <!-- language for the calendar -->
        <script type="text/javascript"
                src="${pageContext.servletContext.contextPath}/share/calendar/lang/<fmt:message key='global.javascript.calendar'/>"></script>
        <!-- the following script defines the Calendar.setup helper function, which makes adding a calendar a matter of 1 or 2 lines of code. -->
        <script type="text/javascript"
                src="${pageContext.servletContext.contextPath}/share/calendar/calendar-setup.js"></script>
        <!-- calendar stylesheet -->
        <link rel="stylesheet" type="text/css" media="all"
              href="${pageContext.servletContext.contextPath}/share/calendar/calendar.css" title="win2k-cold-1"/>
        <!-- jquery -->
        <script language="javascript" type="text/javascript"
                src="${pageContext.servletContext.contextPath}/share/javascript/Oscar.js"></script>

        <script type="text/javascript"
                src="${pageContext.servletContext.contextPath}/library/jquery/jquery-1.12.0.min.js"></script>
        <script type="text/javascript"
                src="${pageContext.servletContext.contextPath}/library/jquery/jquery-ui-1.12.1.min.js"></script>
        <script type="text/javascript"
                src="${pageContext.servletContext.contextPath}/js/demographicProviderAutocomplete.js"></script>

        <script type="text/javascript">
            jQuery.noConflict();

            function renderCalendar(id, inputFieldId) {
                Calendar.setup({inputField: inputFieldId, ifFormat: "%Y-%m-%d", showsTime: false, button: id});

            }

            function handleDocSave(docid, action) {
                var url = contextpath + "/documentManager/inboxManage.do";
                var data = 'method=isDocumentLinkedToDemographic&docId=' + encodeURIComponent(docid);

                fetch(url, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: data
                })
                .then(function(response) {
                    return response.json();
                })
                .then(function(json) {
                    if (json != null) {
                        var success = json.isLinkedToDemographic;
                        var demoid = '';

                        if (success) {
                            if (action == 'addTickler') {
                                demoid = json.demoId;
                                if (demoid != null && demoid.length > 0)
                                    popupStart(450, 600, contextpath + '/tickler/ForwardDemographicTickler.do?docType=DOC&docId=' + encodeURIComponent(docid) + '&demographic_no=' + encodeURIComponent(demoid), 'tickler')
                            }
                        } else {
                            alert("Make sure demographic is linked and document changes saved!");
                        }
                    }
                })
                .catch(function(error) {
                    console.error('Error:', error);
                });
            }


            function rotate90(id) {
                jQuery("#rotate90btn_" + id).attr('disabled', 'disabled');

                fetch(contextpath + "/documentManager/SplitDocument.do", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: "method=rotate90&document=" + encodeURIComponent(id)
                })
                .then(function(response) {
                    jQuery("#rotate90btn_" + id).removeAttr('disabled');
                    jQuery("#docImg_" + id).attr('src', contextpath + "/documentManager/ManageDocument.do?method=showPage&doc_no=" + encodeURIComponent(id) + "&page=1&rand=" + (new Date().getTime()));
                })
                .catch(function(error) {
                    console.error('Error:', error);
                    jQuery("#rotate90btn_" + id).removeAttr('disabled');
                });
            }


            function split(id, demoName) {
                var loc = "${pageContext.servletContext.contextPath}";
                loc = loc + "/oscarMDS/Split.jsp?document=";
                loc = loc + id;
                loc = loc + "&queueID=";
                loc = loc + "<%=Encode.forJavaScript(String.valueOf(inQueue))%>";
                loc = loc + "&demoName=" + encodeURIComponent(demoName);
                popupStart(1400, 1400, loc, "Splitter");
            }

        </script>

    </head>

    <body>
</c:if>
<script type="text/javascript">
    var _in_window = <%="true".equals(request.getParameter("inWindow"))%>;
    var contextpath = "<%=request.getContextPath()%>";
</script>
<div id="labdoc_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" class="content">
    <%
        ArrayList ackList = AcknowledgementData.getAcknowledgements("DOC", docId);
        ReportStatus reportStatus = null;
        String docCommentTxt = "";
        String rptStatus = "";
        boolean ackedOrFiled = false;
        for (int idx = 0; idx < ackList.size(); ++idx) {
            reportStatus = (ReportStatus) ackList.get(idx);

            if (reportStatus.getOscarProviderNo() != null && reportStatus.getOscarProviderNo().equals(providerNo)) {
                docCommentTxt = reportStatus.getComment();
                if (docCommentTxt == null) {
                    docCommentTxt = "";
                }

                rptStatus = reportStatus.getStatus();

                if (rptStatus != null) {
                    ackedOrFiled = rptStatus.equalsIgnoreCase("A") ? true : rptStatus.equalsIgnoreCase("F") ? true : false;
                }
                break;
            }
        }
    %>
    <form name="acknowledgeForm_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" id="acknowledgeForm_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" onsubmit="<%=Encode.forHtmlAttribute(ackFunc)%>" method="post"
          action="javascript:void(0);">

        <input type="hidden" name="segmentID" value="<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"/>
        <input type="hidden" name="multiID" value="<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"/>
        <input type="hidden" name="providerNo" value="<%=Encode.forHtmlAttribute(String.valueOf(providerNo))%>"/>
        <input type="hidden" name="status" value="A" id="status_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"/>
        <input type="hidden" name="labType" value="DOC"/>
        <input type="hidden" name="ajaxcall" value="yes"/>
        <input type="hidden" name="comment" id="comment_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" value="<%=Encode.forHtmlAttribute(String.valueOf(docCommentTxt))%>">
        <% if (demographicID != null && !demographicID.equals("") && !demographicID.equalsIgnoreCase("null") && !ackedOrFiled) {%>
        <input type="submit" id="ackBtn_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
               value="<fmt:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>">
        <input type="button" value="Comment" onclick="addDocComment('<%=Encode.forJavaScript(String.valueOf(docId))%>','<%=Encode.forJavaScript(String.valueOf(providerNo))%>')"/>
        <%}%>
        <input type="button" id="fwdBtn_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" value="<fmt:message key="oscarMDS.index.btnForward"/>"
               onClick="ForwardSelectedRows(<%=Encode.forJavaScript(String.valueOf(docId))%> + ':DOC', null, null);">
        <%if (!ackedOrFiled) { %>
        <input type="button" id="fileBtn_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" value="<fmt:message key="oscarMDS.index.btnFile"/>"
               onclick="fileDoc('<%=Encode.forJavaScript(String.valueOf(docId))%>');">
        <%} %>
        <input type="button" id="closeBtn_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" value=" <fmt:message key="global.btnClose"/> "
               onClick="window.close()">
        <input type="button" id="printBtn_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" value=" <fmt:message key="global.btnPrint"/> "
               onClick="popup(700,960,'<%=Encode.forJavaScript(String.valueOf(url2))%>','file download')">
        <%
            String btnDisabled = "disabled";
            if (demographicID != null && !demographicID.equals("") && !demographicID.equalsIgnoreCase("null") && !demographicID.equals("-1")) {
                btnDisabled = "";
            }

        %>
        <input type="button" id="msgBtn_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" value="Msg"
               onclick="popupPatient(700,960,'${pageContext.servletContext.contextPath}/messenger/SendDemoMessage.do?demographic_no=','msg', '<%=Encode.forJavaScript(String.valueOf(docId))%>')" <%=Encode.forHtmlAttribute(String.valueOf(btnDisabled))%>/>

        <!--input type="button" id="ticklerBtn_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" value="Tickler" onclick="handleDocSave('<%=Encode.forJavaScript(String.valueOf(docId))%>','addTickler')"/-->
        <input type="button" id="mainTickler_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" value="Tickler" onClick="popupPatientTickler(710, 1024,'${pageContext.servletContext.contextPath}/tickler/ticklerAdd.jsp?', 'Tickler','<%=Encode.forJavaScript(String.valueOf(docId))%>')" <%=Encode.forHtml(String.valueOf(btnDisabled))%>>
        <%
                                                            String refileBtnVisibility = "";
                                                            for (Hashtable ht : queues) {
                                                                int id = (Integer) ht.get("id");

                                                                if (EDocUtil.isDocumentAlreadyRefiledInQueue(curdoc.getDescription(), id)) {
                                                                    docCurrentFiledQueue = id;
                                                                    if (id == queueId) {
                                                                        refileBtnVisibility = "disabled";
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        %>

        <input type="button" id="mainEchart_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
               value=" <fmt:message key="oscarMDS.segmentDisplay.btnEChart"/> "
               onClick="popupPatient(710, 1024,'${pageContext.servletContext.contextPath}/oscarEncounter/IncomingEncounter.do?reason=<fmt:message key="oscarMDS.segmentDisplay.labResults"/>&curDate=<%=Encode.forUriComponent(String.valueOf(currentDate))%>>&appointmentNo=&appointmentDate=&startTime=&status=&demographicNo=', 'encounter', '<%=Encode.forJavaScript(String.valueOf(docId))%>')" <%=Encode.forHtmlAttribute(String.valueOf(btnDisabled))%>>
        <input type="button" id="mainMaster_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" value=" <fmt:message key="oscarMDS.segmentDisplay.btnMaster"/>"
               onClick="popupPatient(710,1024,'${pageContext.servletContext.contextPath}/demographic/demographiccontrol.jsp?displaymode=edit&dboperation=search_detail&demographic_no=','master','<%=Encode.forJavaScript(String.valueOf(docId))%>')" <%=Encode.forHtmlAttribute(String.valueOf(btnDisabled))%>>
        <input type="button" id="mainApptHistory_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
               value=" <fmt:message key="oscarMDS.segmentDisplay.btnApptHist"/>"
               onClick="popupPatient(710,1024,'${pageContext.servletContext.contextPath}/demographic/demographiccontrol.jsp?orderby=appttime&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=25&demographic_no=','ApptHist','<%=Encode.forJavaScript(String.valueOf(docId))%>')" <%=Encode.forHtmlAttribute(String.valueOf(btnDisabled))%>>

        <input type="button" id="refileDoc_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
               value="<fmt:message key="oscarEncounter.noteBrowser.msgRefile"/>" onclick="refileDoc('<%=Encode.forJavaScript(String.valueOf(docId))%>');" <%=Encode.forHtml(String.valueOf(refileBtnVisibility))%> >
        <select id="queueList_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" name="queueList"
                onchange="handleQueueListChange(this, document.getElementById('refileDoc_<%=Encode.forJavaScript(String.valueOf(docId))%>'), '<%=Encode.forJavaScript(String.valueOf(docCurrentFiledQueue))%>')">
            <%
                for (Hashtable ht : queues) {
                    int id = (Integer) ht.get("id");
                    String qName = (String) ht.get("queue");
            %>
            <option value="<%=Encode.forHtmlAttribute(String.valueOf(id))%>" <%=Encode.forHtml(String.valueOf(((id == queueId) ? " selected" : "")))%>><%=Encode.forHtml(String.valueOf(qName))%>
            </option>
            <%}%>
        </select>
    </form>
    <table class="docTable">
        <tr>
            <td valign="top" class="pdfPreviewColumn">
                <div style="text-align: right;font-weight: bold">
                    <% if (numOfPage > 1 && displayDocumentAs.equals(UserProperty.IMAGE)) {%>
                    <a id="firstP_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" style="display: none;" href="javascript:void(0);"
                       onclick="firstPage('<%=Encode.forJavaScript(String.valueOf(docId))%>','<%=Encode.forJavaScript(String.valueOf(cp))%>');">First</a>
                    <a id="prevP_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" style="display: none;" href="javascript:void(0);"
                       onclick="prevPage('<%=Encode.forJavaScript(String.valueOf(docId))%>','<%=Encode.forJavaScript(String.valueOf(cp))%>');">Prev</a>
                    <a id="nextP_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" href="javascript:void(0);"
                       onclick="nextPage('<%=Encode.forJavaScript(String.valueOf(docId))%>','<%=Encode.forJavaScript(String.valueOf(cp))%>');">Next</a>
                    <a id="lastP_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" href="javascript:void(0);"
                       onclick="lastPage('<%=Encode.forJavaScript(String.valueOf(docId))%>','<%=Encode.forJavaScript(String.valueOf(cp))%>');">Last</a>
                    <%} %>
                </div>
                <% if (displayDocumentAs.equals(UserProperty.IMAGE)) { %>
                <a href="<%=Encode.forHtmlAttribute(String.valueOf(url2))%>" target="_blank"><img alt="document" id="docImg_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" src="<%=Encode.forHtmlAttribute(String.valueOf(url))%>"
                                                         onerror="this.src='<%=request.getContextPath()%>/images/icon_alert.gif'"/></a>
                <%} else {%>
                <div id="docDispPDF_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"></div>
                <%}%>
                <div style="text-align: right;font-weight: bold">
                    <% if (numOfPage > 1 && displayDocumentAs.equals(UserProperty.IMAGE)) {%>
                    <a id="firstP2_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" style="display: none;" href="javascript:void(0);"
                       onclick="firstPage('<%=Encode.forJavaScript(String.valueOf(docId))%>','<%=Encode.forJavaScript(String.valueOf(cp))%>');">First</a>
                    <a id="prevP2_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" style="display: none;" href="javascript:void(0);"
                       onclick="prevPage('<%=Encode.forJavaScript(String.valueOf(docId))%>','<%=Encode.forJavaScript(String.valueOf(cp))%>');">Prev</a>
                    <a id="nextP2_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" href="javascript:void(0);" onclick="nextPage('<%=Encode.forJavaScript(String.valueOf(docId))%>','<%=Encode.forJavaScript(String.valueOf(cp))%>');">Next</a>
                    <a id="lastP2_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" href="javascript:void(0);" onclick="lastPage('<%=Encode.forJavaScript(String.valueOf(docId))%>','<%=Encode.forJavaScript(String.valueOf(cp))%>');">Last</a>
                    <%} %>
                </div>
            </td>

            <td valign="top" class="pdfAssignmentToolsColumn">
                <fieldset>
                    <legend><fmt:message key="inboxmanager.document.PatientMsg"/><span
                            id="assignedPId_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"><e:forHtmlContent value='${demoName}' /></span></legend>
                    <table>
                        <tr>
                            <td><fmt:message key="inboxmanager.document.DocumentUploaded"/></td>
                            <td><%=Encode.forHtml(String.valueOf(curdoc.getDateTimeStamp()))%>
                            </td>
                        </tr>
                        <tr>
                            <td><fmt:message key="inboxmanager.document.ContentType"/></td>
                            <td><%=Encode.forHtml(String.valueOf(contentType))%>
                            </td>
                        </tr>
                        <tr>
                            <td><fmt:message key="inboxmanager.document.NumberOfPages"/></td>
                            <td>
                                <input id="shownPage_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" type="hidden" value="1"/>
                                <%if (displayDocumentAs.equals(UserProperty.IMAGE)) { %>
                                <span id="viewedPage_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
                                      class="<%= numOfPage > 1 ? "multiPage" : "singlePage" %>">1</span>&nbsp; of
                                &nbsp;<%}%>
                                <span id="numPages_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
                                      class="<%= numOfPage > 1 ? "multiPage" : "singlePage" %>"><%=Encode.forHtml(String.valueOf(numOfPageStr))%></span>
                            </td>
                        </tr>

                        <tr>
                            <td></td>
                            <td>
                                <% boolean updatableContent = true; %>
                                <oscar:oscarPropertiesCheck property="ALLOW_UPDATE_DOCUMENT_CONTENT" value="false"
                                                            defaultVal="false">
                                    <%
                                        if (!demographicID.equals("-1")) {
                                            updatableContent = false;
                                        }
                                    %>
                                </oscar:oscarPropertiesCheck>
                                <div style="<%=updatableContent==true?"":"visibility: hidden"%>">
                                    <input onclick="split('<%=Encode.forJavaScript(String.valueOf(docId))%>','${e:forJavaScript(demoName)}')"
                                           type="button" value="<fmt:message key="inboxmanager.document.split"/>"/>
                                    <input id="rotate180btn_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" onclick="rotate180('<%=Encode.forJavaScript(String.valueOf(docId))%>')"
                                           type="button"
                                           value="<fmt:message key="inboxmanager.document.rotate180"/>"/>
                                    <input id="rotate90btn_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" onclick="rotate90('<%=Encode.forJavaScript(String.valueOf(docId))%>')" type="button"
                                           value="<fmt:message key="inboxmanager.document.rotate90"/>"/>
                                    <% if (numOfPage > 1) { %><input id="removeFirstPagebtn_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
                                                                     onclick="removeFirstPage('<%=Encode.forJavaScript(String.valueOf(docId))%>')"
                                                                     type="button"
                                                                     value="<fmt:message key="inboxmanager.document.removeFirstPage"/>"/><% } %>
                                </div>
                            </td>
                        </tr>

                    </table>

                    <form id="forms_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" onsubmit="return updateDocument('forms_<%=Encode.forJavaScript(String.valueOf(docId))%>');">
                        <input type="hidden" name="method" value="documentUpdateAjax"/>
                        <input type="hidden" name="documentId" value="<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"/>
                        <input type="hidden" name="providerNo" value="<%=Encode.forHtmlAttribute(String.valueOf(providerNo))%>"/>
                        <input type="hidden" name="curPage_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" id="curPage_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" value="1"/>
                        <input type="hidden" name="totalPage_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" id="totalPage_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
                               value="<%=Encode.forHtmlAttribute(String.valueOf(numOfPage))%>"/>
                        <input type="hidden" name="displayDocumentAs_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" id="displayDocumentAs_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
                               value="<%=Encode.forHtmlAttribute(String.valueOf(displayDocumentAs))%>">
                        <table border="0">
                            <tr>
                                <td><fmt:message key="dms.documentReport.msgCreator"/>:</td>
                                <td><%=Encode.forHtml(String.valueOf(curdoc.getCreatorName()))%>
                                </td>
                            </tr>
                            <tr>
                                <td><fmt:message key="dms.documentReport.msgDocType"/>:</td>
                                <td>
                                    <select name="docType" id="docType_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>">
                                        <option value=""><fmt:message key="dms.addDocument.formSelect"/></option>
                                        <%
                                            for (int j = 0; j < doctypes.size(); j++) {
                                                String doctype = (String) doctypes.get(j);
                                        %>
                                        <option value="<%=Encode.forHtmlAttribute(String.valueOf(doctype))%>" <%=(curdoc.getType().equals(doctype)) ? " selected" : ""%>><%=Encode.forHtml(String.valueOf(doctype))%>
                                        </option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td><fmt:message key="dms.documentReport.msgDocDesc"/>:</td>
                                <td><input id="docDesc_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" type="text" name="documentDescription"
                                           value="<%=Encode.forHtmlAttribute(String.valueOf(curdoc.getDescription()))%>"
                                           onfocus="this.select(); this.setAttribute('data-original-value', this.value)"
                                           onblur="if (this.value.trim() === '') this.value = this.getAttribute('data-original-value')"/></td>
                            </tr>
                            <tr>
                                <td><fmt:message key="inboxmanager.document.ObservationDateMsg"/></td>
                                <td id="observation-calendar">
                                    <input class="input-field" id="observationDate<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" name="observationDate"
                                           type="text" value="<%=Encode.forHtmlAttribute(String.valueOf(curdoc.getObservationDate()))%>">
                                    <a class="calendar-icon" id="obsdate<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
                                       onmouseover="renderCalendar(this.id,'observationDate<%=Encode.forJavaScript(String.valueOf(docId))%>' );"
                                       href="javascript:void(0);">
                                        <img class="calendar-image" title="Calendar"
                                             src="<%=request.getContextPath()%>/images/cal.gif" alt="Calendar"/>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td><fmt:message key="inboxmanager.document.DemographicMsg"/></td>
                                <td><%
                                    if (!demographicID.equals("-1")) {%>
                                    <input id="saved<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" type="hidden" name="saved" value="true"/>
                                    <input type="hidden" value="<%=Encode.forHtmlAttribute(String.valueOf(demographicID))%>" name="demog"
                                           id="demofind<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"/>
                                    <input type="hidden" name="demofindName" value="${e:forHtmlAttribute(demoName)}"
                                           id="demofindName<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"/>
                                    <e:forHtmlContent value='${demoName}' /><e:forHtmlContent value='${mrpProviderName}' /><%} else {%>
                                    <input id="saved<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" type="hidden" name="saved" value="false"/>
                                    <input type="hidden" name="demog" value="<%=Encode.forHtmlAttribute(String.valueOf(demographicID))%>"
                                           id="demofind<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"/>
                                    <input type="hidden" name="demofindName" value="${e:forHtmlAttribute(demoName)}"
                                           id="demofindName<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"/>

                                    <input type="checkbox" id="activeOnly<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" name="activeOnly" checked="checked"
                                           value="true" onclick="setupDemoAutoCompletion()">Active Only<br>
                                    <input type="text" id="autocompletedemo<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
                                           onchange="checkSave('<%=Encode.forJavaScript(String.valueOf(docId))%>');" name="demographicKeyword"
                                           placeholder="Search Demographic"/>
                                    <div id="autocomplete_choices<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" class="autocomplete"></div>

                                    <%}%>
                                    <input type="button" id="createNewDemo" value="Create New Demographic"
                                           onclick="popup(700,960,'${pageContext.servletContext.contextPath}/demographic/demographicaddarecordhtm.jsp','demographic')"/>

                                    <input id="saved_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" type="hidden" name="saved" value="false"/>
                                    <br><input id="mrp_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" style="display: none;" type="checkbox"
                                               onclick="sendMRP(this)" name="demoLink">
                                    <a id="mrp_fail_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
                                       style="color:red;font-style: italic;display: none;"><fmt:message key="inboxmanager.document.SendToMRPFailedMsg"/></a>
                                </td>
                            </tr>

                            <tr>
                                <td valign="top"><fmt:message key="inboxmanager.document.FlagProviderMsg"/></td>
                                <td>
                                    <input type="hidden" name="provi" id="provfind<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"/>
                                    <input type="text" id="autocompleteprov<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" name="demographicKeyword"
                                           placeholder="Search Provider"/>
                                    <div id="autocomplete_choicesprov<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" class="autocomplete"></div>


                                    <div class="provider-list-additions" id="providerList<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"></div>
                                </td>
                            </tr>
                            <tr>
                                <td width="30%" colspan="1" align="left"><a id="saveSucessMsg_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
                                                                            style="display:none;color:blue;"><fmt:message key="inboxmanager.document.SuccessfullySavedMsg"/></a></td>
                                <td width="30%" colspan="1" align="left"><%if(demographicID.equals("-1")){%>
                                    <input type="submit" name="save" disabled id="save<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" value="Save"/>
                                    <input type="button" name="save" id="saveNext<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
                                           onclick="saveNext(<%=Encode.forJavaScript(String.valueOf(docId))%>)" disabled
                                           value='<fmt:message key="inboxmanager.document.SaveAndNext"/>'/>
                                        <%}
            else{%>
                                    <input type="submit" name="save" id="save<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" value="Save"/>
                                    <input type="button" name="save" onclick="saveNext(<%=Encode.forJavaScript(String.valueOf(docId))%>)"
                                           id="saveNext<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"
                                           value='<fmt:message key="inboxmanager.document.SaveAndNext"/>'/>

                                        <%}%>

                            </tr>

                            <tr>
                                <td colspan="2">
                                    <fmt:message key="inboxmanager.document.LinkedProvidersMsg"/>
                                    <%
                                        Properties p = (Properties) session.getAttribute("providerBean");
                                        List<ProviderInboxItem> routeList = Collections.emptyList();
                                        if (docId != null) {
                                            routeList = providerInboxRoutingDao.getProvidersWithRoutingForDocument("DOC", Integer.parseInt(docId));
                                        }
                                    %>
                                    <ul>
                                        <%
                                            for (ProviderInboxItem pItem : routeList) {
                                                String s = p.getProperty(pItem.getProviderNo(), pItem.getProviderNo());

                                                if (!s.equals("0") && !s.equals("null") && !pItem.getStatus().equals("X")) {
                                        %>
                                        <li><%=Encode.forHtml(String.valueOf(s))%><a href="#"
                                                     onclick="removeLink('DOC', '<%=Encode.forJavaScript(String.valueOf(docId))%>', '<%=Encode.forJavaScript(String.valueOf(pItem.getProviderNo()))%>', this);return false;"><fmt:message key="inboxmanager.document.RemoveLinkedProviderMsg"/></a></li>
                                        <%
                                                }
                                            }
                                        %>
                                    </ul>
                                </td>
                            </tr>
                        </table>

                    </form>
                </fieldset>


                <%

                    if (ackList.size() > 0) {%>
                <fieldset>
                    <table width="100%" height="20" cellpadding="2" cellspacing="2">
                        <tr>
                            <td align="center" bgcolor="white">
                                <div class="FieldData">
                                    <!--center-->
                                    <% for (int i = 0; i < ackList.size(); i++) {
                                        ReportStatus report = (ReportStatus) ackList.get(i); %>
                                    <%=Encode.forHtml(String.valueOf(report.getProviderName()))%> :

                                    <% String ackStatus = report.getStatus();
                                        if (ackStatus.equals("A")) {
                                            ackStatus = "Acknowledged";
                                        } else if (ackStatus.equals("F")) {
                                            ackStatus = "Filed but not Acknowledged";
                                        } else {
                                            ackStatus = "Not Acknowledged";
                                        }
                                    %>
                                    <font color="red"><%=Encode.forHtml(String.valueOf(ackStatus))%>
                                    </font>
                                    <span id="timestamp_<%=Encode.forHtmlAttribute(String.valueOf(docId + "_" + report.getOscarProviderNo()))%>"><%=Encode.forHtml(String.valueOf(report.getTimestamp() == null ? "&nbsp;" : report.getTimestamp() + "&nbsp;"))%></span>,
                                    comment: <span
                                        id="comment_<%=Encode.forHtmlAttribute(String.valueOf(docId + "_" + report.getOscarProviderNo()))%>"><%=Encode.forHtml(String.valueOf(report.getComment() == null || report.getComment().equals("") ? "no comment" : report.getComment()))%></span>

                                    <br>
                                    <% }
                                        if (ackList.size() == 0) {
                                    %><font color="red">N/A</font><%
                                    }
                                %>
                                    <!--/center-->
                                </div>
                            </td>
                        </tr>
                    </table>
                </fieldset>
                <%
                    }

                %>

                <fieldset>
                    <legend><span class="FieldData"><i><fmt:message key="inboxmanager.document.NextAppointmentMsg"/> <oscar:nextAppt
                            demographicNo="<%=Encode.forHtmlAttribute(String.valueOf(demographicID))%>"/></i></span></legend>
                    <%
                        int iPageSize = 5;
                        Provider prov;
                        boolean HighlightUserAppt = false;
                        if (demographicID != null && !demographicID.isEmpty() && !demographicID.equals("-1")) {

                            List<Appointment> appointmentList = appointmentDao.getAppointmentHistory(Integer.parseInt(demographicID), 0, iPageSize);
                            if (appointmentList != null && appointmentList.size() > 0) {
                    %>

                    <table bgcolor="#c0c0c0" align="center" valign="top">
                        <tr bgcolor="#ccccff">
                            <th colspan="4"><fmt:message key="appointment.addappointment.msgOverview"/></th>
                        </tr>
                        <tr bgcolor="#ccccff">
                            <th><fmt:message key="Appointment.formDate"/></th>
                            <th><fmt:message key="Appointment.formStartTime"/></th>
                            <th><fmt:message key="appointment.addappointment.msgProvider"/></th>
                            <th><fmt:message key="appointment.addappointment.msgComments"/></th>
                        </tr>
                        <%
                            for (Appointment a : appointmentList) {
                                prov = providerDao.getProvider(a.getProviderNo());
                                HighlightUserAppt = false;
                                if (creator.equals(a.getProviderNo())) {
                                    HighlightUserAppt = true;
                                }
                        %>
                        <tr bgcolor="<%=HighlightUserAppt == false ? "#FFFFFF" : "#CCFFCC"%>">
                            <td><%=Encode.forHtml(String.valueOf(ConversionUtils.toDateString(a.getAppointmentDate())))%>
                            </td>
                            <td><%=Encode.forHtml(String.valueOf(ConversionUtils.toTimeString(a.getStartTime())))%>
                            </td>
                            <td><%=Encode.forHtml(String.valueOf(prov == null ? "N/A" : prov.getFormattedName()))%>
                            </td>
                            <td><% if (a.getStatus() == null) {%>
                                "" <% } else if (a.getStatus().startsWith("N")) {%><fmt:message key="oscar.appt.ApptStatusData.msgNoShow"/><% } else if (a.getStatus().startsWith("C")) {%><fmt:message key="oscar.appt.ApptStatusData.msgCanceled"/> <%}%>
                            </td>
                        </tr>
                        <%}%>
                    </table>
                    <%
                            }
                        }
                    %>
                    <form name="reassignForm_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>" id="reassignForm_<%=Encode.forHtmlAttribute(String.valueOf(docId))%>">
                        <input type="hidden" name="flaggedLabs" value="<%=Encode.forHtmlAttribute(String.valueOf(docId))%>"/>
                        <input type="hidden" name="selectedProviders" value=""/>
                        <input type="hidden" name="labType" value="DOC"/>
                        <input type="hidden" name="labType<%=Encode.forHtmlAttribute(String.valueOf(docId))%>DOC" value="imNotNull"/>
                        <input type="hidden" name="providerNo" value="<%=Encode.forHtmlAttribute(String.valueOf(providerNo))%>"/>
                        <input type="hidden" name="favorites" value=""/>
                        <input type="hidden" name="ajax" value="yes"/>
                    </form>
                </fieldset>
            </td>
        </tr>

        <tr>
            <td colspan="2">
                <hr width="100%" color="red">
            </td>
        </tr>
    </table>
</div>

<script type="text/javascript"
        src="${pageContext.servletContext.contextPath}/share/javascript/oscarMDSIndex.js"></script>
<script type="text/javascript" src="showDocument.js"></script>
<script type="text/javascript">

    var displayDocAsEl = document.getElementById('displayDocumentAs_<%=Encode.forJavaScript(String.valueOf(docId))%>');
    if (displayDocAsEl && displayDocAsEl.value == "<%=Encode.forJavaScript(String.valueOf(UserProperty.PDF))%>") {
        showPDF('<%=Encode.forJavaScript(String.valueOf(docId))%>', contextpath);
    }

    var tmp;

    function setupDemoAutoCompletion() {
        if (jQuery("#autocompletedemo<%=Encode.forJavaScript(String.valueOf(docId))%>")) {

            var url;
            if (jQuery("#activeOnly<%=Encode.forJavaScript(String.valueOf(docId))%>").is(":checked")) {
                url = "${pageContext.servletContext.contextPath}/demographic/SearchDemographic.do?jqueryJSON=true&activeOnly=" + jQuery("#activeOnly<%=Encode.forJavaScript(String.valueOf(docId))%>").val();
            } else {
                url = "${pageContext.servletContext.contextPath}/demographic/SearchDemographic.do?jqueryJSON=true";
            }

            jQuery("#autocompletedemo<%=Encode.forJavaScript(String.valueOf(docId))%>").autocomplete({
                source: url,
                minLength: 2,

                focus: function (event, ui) {
                    jQuery("#autocompletedemo<%=Encode.forJavaScript(String.valueOf(docId))%>").val(ui.item.label);
                    return false;
                },
                select: function (event, ui) {
                    jQuery("#autocompletedemo<%=Encode.forJavaScript(String.valueOf(docId))%>").val(ui.item.label);
                    jQuery("#demofind<%=Encode.forJavaScript(String.valueOf(docId))%>").val(ui.item.value);
                    jQuery("#demofindName<%=Encode.forJavaScript(String.valueOf(docId))%>").val(ui.item.formattedName);
                    selectedDemos.push(ui.item.label);
                    console.log(ui.item.providerNo);
                    if (ui.item.providerNo != undefined && ui.item.providerNo != null && ui.item.providerNo != "" && ui.item.providerNo != "null") {
                        addDocToList(ui.item.providerNo, ui.item.provider + " (MRP)", "<%=Encode.forJavaScript(String.valueOf(docId))%>");
                    }

                    //enable Save button whenever a selection is made
                    jQuery('#save<%=Encode.forJavaScript(String.valueOf(docId))%>').removeAttr('disabled');
                    jQuery('#saveNext<%=Encode.forJavaScript(String.valueOf(docId))%>').removeAttr('disabled');

                    jQuery('#msgBtn_<%=Encode.forJavaScript(String.valueOf(docId))%>').removeAttr('disabled');
                    jQuery('#mainTickler_<%=Encode.forJavaScript(String.valueOf(docId))%>').removeAttr('disabled');
                    jQuery('#mainEchart_<%=Encode.forJavaScript(String.valueOf(docId))%>').removeAttr('disabled');
                    jQuery('#mainMaster_<%=Encode.forJavaScript(String.valueOf(docId))%>').removeAttr('disabled');
                    jQuery('#mainApptHistory_<%=Encode.forJavaScript(String.valueOf(docId))%>').removeAttr('disabled');
                    return false;
                }
            });
        }
    }


    jQuery(setupDemoAutoCompletion());

    function setupProviderAutoCompletion() {
        var url = "${pageContext.servletContext.contextPath}/provider/SearchProvider.do?method=labSearch";

        jQuery("#autocompleteprov<%=Encode.forJavaScript(String.valueOf(docId))%>").autocomplete({
            source: url,
            minLength: 2,

            focus: function (event, ui) {
                jQuery("#autocompleteprov<%=Encode.forJavaScript(String.valueOf(docId))%>").val(ui.item.label);
                return false;
            },
            select: function (event, ui) {
                jQuery("#autocompleteprov<%=Encode.forJavaScript(String.valueOf(docId))%>").val("");
                jQuery("#provfind<%=Encode.forJavaScript(String.valueOf(docId))%>").val(ui.item.value);
                addDocToList(ui.item.value, ui.item.label, "<%=Encode.forJavaScript(String.valueOf(docId))%>");

                return false;
            }
        });
    }

    jQuery(setupProviderAutoCompletion());


</script>
<jsp:include page="/images/spinner.jsp"/>
<c:if test="${param.inWindow eq 'true'}">
    </body>
    </html>
</c:if>
