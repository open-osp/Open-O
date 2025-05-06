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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="oscar.util.ConversionUtils" %>
<%@page import="org.oscarehr.casemgmt.web.NoteDisplay" %>
<% long start = System.currentTimeMillis(); %>
<%@include file="/casemgmt/taglibs.jsp" %>
<%@page
        import="java.util.List, java.util.Set, java.util.Iterator, org.oscarehr.casemgmt.model.CaseManagementIssue, org.oscarehr.casemgmt.model.CaseManagementNoteExt" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.provider.web.CppPreferencesUIBean" %>
<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@page import="org.oscarehr.casemgmt.web.CaseManagementViewAction" %>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO" %>
<%@page import="org.oscarehr.common.model.UserProperty" %>
<%@page import="org.oscarehr.common.model.PartialDate" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="java.util.ResourceBundle"%>
<%
    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_casemgmt.notes");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
    ResourceBundle bundle = ResourceBundle.getBundle("oscarResources", request.getLocale());
    String titleParam = request.getParameter("title");
    String titleMsg = bundle.getString(titleParam);
%>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="num" value="${fn:length(Notes)}" />
<div class="nav-menu-heading" style="background-color:#<c:out value="${param.hc}"/>">
    <div class="nav-menu-add-button">
        <h3>
            <%
                LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
                com.quatro.service.security.SecurityManager securityManager = new com.quatro.service.security.SecurityManager();
                if (securityManager.hasWriteAccess("_" + request.getParameter("issue_code"), roleName$)) {
            %>
            <a href="javascript:void(0)" title='Add Item' onclick="return showEdit(event,'<%=titleMsg%>','',0,'','','','<%=request.getAttribute("addUrl")%>0', 
                '<c:out value="${param.cmd}"/>','<%=request.getAttribute("identUrl")%>','<%=request.getAttribute("cppIssue")%>','','<c:out value="${param.demographicNo}"/>');">+</a>
            <% } else { %>
            &nbsp;
            <% } %>
        </h3>
    </div>
    <div class="nav-menu-title">
        <h3>
            <a href="javascript:void(0)" onclick="return showIssueHistory('<c:out
                    value="${param.demographicNo}"/>','<%=request.getAttribute("issueIds")%>');"><%=titleMsg%></a>
        </h3>
    </div>
</div>
<c:choose>
    <c:when test='${param.title == "oscarEncounter.oMeds.title" || param.title == "oscarEncounter.riskFactors.title" || param.title == "oscarEncounter.famHistory.title"|| param.noheight == "true"}'>
        <div style='clear:both;' class='topBox-notes'>
    </c:when>
    <c:otherwise>
        <div style='clear:both;' class='topBox-notes'>
    </c:otherwise>
</c:choose>

<ul>
    <c:forEach var="note" items="${Notes}" varStatus="status">
        <c:set var="noteId" value="${note.id}" />
        <c:set var="backgroundColor" value="${status.index % 2 == 0 ? '#F3F3F3' : '#FFFFFF'}" />
        
        <li class="cpp" style="background-color: ${backgroundColor};">
            <span id="spanListNote${noteId}">
                <c:choose>
                    <c:when test="${param.title == 'oscarEncounter.oMeds.title' || param.title == 'oscarEncounter.riskFactors.title' || param.title == 'oscarEncounter.famHistory.title' || param.noheight == 'true'}">
                        <a class="links" onmouseover="this.className='linkhover'" onmouseout="this.className='links'"
                           title="Rev:${note.revision} - Last update:${note.update_date}"
                           id="listNote${noteId}" href="javascript:void(0)"
                           onclick="showEdit(event,'${titleMsg}','${noteId}','${editors}','${note.observation_date}','${note.revision}','${note.note}','${addUrl}${noteId}','${param.cmd}','${identUrl}','${noteIssues}','${noteExts}','${param.demographicNo}');return false;">
                            ${htmlNoteTxt}
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a class="topLinks" onmouseover="this.className='topLinkhover'" onmouseout="this.className='topLinks'"
                           title="Rev:${note.revision} - Last update:${note.update_date}"
                           id="listNote${noteId}" href="javascript:void(0)"
                           onclick="showEdit(event,'${titleMsg}','${noteId}','${editors}','${note.observation_date}','${note.revision}','${note.note}','${addUrl}${noteId}','${param.cmd}','${identUrl}','${noteIssues}','${noteExts}','${param.demographicNo}');return false;">
                            ${htmlNoteTxt}
                        </a>
                    </c:otherwise>
                </c:choose>
            </span>
        </li>
    </c:forEach>

    <!-- Handling remoteNotes -->
    <c:if test="${not empty remoteNotes}">
        <c:forEach var="remoteNote" items="${remoteNotes}" varStatus="status">
            <c:set var="remoteBackgroundColor" value="${status.index % 2 == 0 ? '#FFCCCC' : '#CCA3A3'}" />
            <li class="cpp" style="background-color: ${remoteBackgroundColor};">
                <a class="links" onmouseover="this.className='linkhover'" onmouseout="this.className='links'"
                   title="${remoteNote.location} by ${remoteNote.providerName} on ${remoteNote.observationDate}"
                   href="javascript:void(0)"
                   onclick="showIntegratedNote('${titleMsg}', '${remoteNote.note}', '${remoteNote.location}', '${remoteNote.providerName}', '${remoteNote.observationDate}');">
                    ${remoteNote.note}
                </a>
            </li>
        </c:forEach>
    </c:if>
</ul>

<input type="hidden" id="<c:out value="${param.cmd}"/>num" value="${num}">
<input type="hidden" id="<c:out value="${param.cmd}"/>threshold" value="0">
<%!
    String getNoteExts(Long noteId, List<CaseManagementNoteExt> lcme) {
        StringBuffer strcme = new StringBuffer();
        for (CaseManagementNoteExt cme : lcme) {
            if (cme.getNoteId().equals(noteId)) {
                String key = cme.getKeyVal();
                String val = null;
                if (key.contains(" Date")) {
                    val = readPartialDate(cme);
                } else {
                    val = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(cme.getValue());
                }
                if (strcme.length() > 0) strcme.append(";");
                strcme.append(key + ";" + val);
            }
        }
        return strcme.toString();
    }

    String readPartialDate(CaseManagementNoteExt cme) {
        String type = cme.getValue();
        String val = null;

        if (type != null && !type.trim().equals("")) {
            if (type.equals(PartialDate.YEARONLY))
                val = oscar.util.UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy");
            else if (type.equals(PartialDate.YEARMONTH))
                val = oscar.util.UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM");
            else val = oscar.util.UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd");
        } else {
            val = oscar.util.UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd");
        }
        return val;
    }
%>
