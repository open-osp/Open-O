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
<%--
    SearchDrug3.jsp - Prescription drug search and staging interface

    Purpose: Primary interface for searching, staging, and managing prescription drugs.
    Handles the full prescription workflow including drug search, staging drugs for
    prescribing, re-prescribing (ReRx), saving prescriptions, and print preview.

    Features:
    - Drug search with auto-complete and interaction checking
    - Prescription staging area with add/remove drug cards
    - Re-prescribe (ReRx) checkbox workflow for existing medications
    - Save & Print and Save Only prescription workflows
    - Print preview modal with Edit Rx support
    - Drug favorites management
    - Allergy and interaction warnings

    Requires:
    - RxSessionBean in HTTP session (contains demographicNo, providerNo, stash state)

    @since 2009-09-18
--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="csrf" uri="http://www.owasp.org/index.php/Category:OWASP_CSRFGuard_Project/Owasp.CsrfGuard.tld" %>
<%@page import="ca.openosp.openo.utility.WebUtils" %>
<%@page import="ca.openosp.openo.commn.model.PharmacyInfo" %>
<%@page import="ca.openosp.OscarProperties,ca.openosp.openo.log.*" %>
<%@page import="ca.openosp.openo.casemgmt.service.CaseManagementManager" %>
<%@page import="java.util.*" %>
<%@page import="ca.openosp.openo.utility.SpringUtils" %>
<%@page import="java.util.List" %>
<%@page import="ca.openosp.openo.utility.LoggedInInfo" %>
<%@page import="ca.openosp.openo.prescript.data.RxPrescriptionData" %>
<%@page import="ca.openosp.openo.commn.model.ProviderPreference" %>
<%@page import="ca.openosp.openo.web.admin.ProviderPreferencesUIBean" %>
<%@page import="ca.openosp.openo.casemgmt.model.CaseManagementNote" %>
<%@page import="ca.openosp.openo.casemgmt.model.Issue" %>
<%@ page import="ca.openosp.openo.services.security.SecurityManager" %>
<%@ page import="ca.openosp.openo.prescript.pageUtil.RxSessionBean" %>
<%@ page import="ca.openosp.openo.prescript.data.RxPharmacyData" %>
<%@ page import="ca.openosp.openo.casemgmt.model.CaseManagementNoteLink" %>
<%@ page import="org.owasp.encoder.Encode" %>


<%
  String rx_enhance = OscarProperties.getInstance().getProperty("rx_enhance");
  RxPatientData.Patient patient = (RxPatientData.Patient) request.getSession().getAttribute("Patient");

  if (rx_enhance != null && rx_enhance.equals("true")) {
    if (request.getParameter("ID") != null) {
%>
<script>
  window.opener.location = window.opener.location;
  window.close();
</script>
<%
    }
  }
  SecurityManager securityManager = new SecurityManager();
  String roleName2$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
  boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_rx" rights="r" reverse="<%=true%>">
  <%authed = false; %>
  <%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
  if (!authed) {
    return;
  }
%>


<c:if test="${empty sessionScope.RxSessionBean}">
  <% response.sendRedirect("error.html"); %>
</c:if>
<c:if test="${not empty sessionScope.RxSessionBean}">
  <c:set var="bean" value="${sessionScope.RxSessionBean}" scope="page"/>
  <c:if test="${not bean.valid}">
    <% response.sendRedirect("error.html"); %>
  </c:if>
</c:if>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
  RxSessionBean rxSessionBean = (RxSessionBean) pageContext.findAttribute("bean");

  String usefav = request.getParameter("usefav");
  String favid = request.getParameter("favid");
  int demoNo = rxSessionBean.getDemographicNo();

%>
<security:oscarSec roleName="<%=roleName2$%>"
                   objectName='<%=Encode.forHtmlAttribute(String.valueOf("_rx$"+demoNo))%>' rights="o"
                   reverse="<%=false%>">
  <fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographiceditdemographic.accessDenied"/>
  <% response.sendRedirect(request.getContextPath() + "/acctLocked.html"); %>
</security:oscarSec>

<%
  LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
  String providerNo = rxSessionBean.getProviderNo();
  HashMap hm = (HashMap) session.getAttribute("profileViewSpec");
  boolean show_current = true;
  boolean show_all = true;
  boolean active = true;
  boolean inactive = true;
  //boolean all=true;
  boolean longterm_acute = true;
  boolean longterm_acute_inactive_external = true;
  if (hm != null) {
    if (hm.get("show_current") != null) {
      show_current = (Boolean) hm.get("show_current");
    } else {
      show_current = false;
    }
    if (hm.get("show_all") != null) {
      show_all = (Boolean) hm.get("show_all");
    } else {
      show_all = false;
    }
    if (hm.get("active") != null) {
      active = (Boolean) hm.get("active");
    } else {
      active = false;
    }
    if (hm.get("inactive") != null) {
      inactive = (Boolean) hm.get("inactive");
    } else {
      inactive = false;
    }
    if (hm.get("longterm_acute") != null) {
      longterm_acute = (Boolean) hm.get("longterm_acute");
    } else {
      longterm_acute = false;
    }
    if (hm.get("longterm_acute_inactive_external") != null) {
      longterm_acute_inactive_external = (Boolean) hm.get("longterm_acute_inactive_external");
    } else {
      longterm_acute_inactive_external = false;
    }
  }

  RxPharmacyData pharmacyData = new RxPharmacyData();
  List<PharmacyInfo> pharmacyList = pharmacyData.getPharmacyFromDemographic(Integer.toString(demoNo));

  String drugref_route = OscarProperties.getInstance().getProperty("drugref_route");
  if (drugref_route == null) {
    drugref_route = "";
  }
  String[] d_route = ("Oral," + drugref_route).split(",");

  String annotation_display = CaseManagementNoteLink.DISP_PRESCRIP;

  RxPrescriptionData.Prescription[] prescribedDrugs = patient.getPrescribedDrugScripts(); //this function only returns drugs which have an entry in prescription and drugs table
  String script_no = "";

  //This checks if the providers has the ExternalPresriber feature enabled, if so then a link appear for the providers to access the ExternalPrescriber
  ProviderPreference providerPreference = ProviderPreferencesUIBean.getProviderPreference(loggedInInfo.getLoggedInProviderNo());

  boolean eRxEnabled = false;
  String eRx_SSO_URL = null;
  String eRxUsername = null;
  String eRxPassword = null;
  String eRxFacility = null;
  String eRxTrainingMode = "0"; //not in training mode

  if (providerPreference != null) {
    eRxEnabled = providerPreference.isERxEnabled();
    eRx_SSO_URL = providerPreference.getERx_SSO_URL();
    eRxUsername = providerPreference.getERxUsername();
    eRxPassword = providerPreference.getERxPassword();
    eRxFacility = providerPreference.getERxFacility();

    boolean eRxTrainingModeTemp = providerPreference.isERxTrainingMode();
    if (eRxTrainingModeTemp) {
      eRxTrainingMode = "1";
    }
  }

  CaseManagementManager cmgmtMgr = SpringUtils.getBean(CaseManagementManager.class);
  List<Issue> issues = cmgmtMgr.getIssueInfoByCode(loggedInInfo.getLoggedInProviderNo(), "OMeds");
  String[] issueIds = new String[issues.size()];
  int idx = 0;
  for (Issue issue : issues) {
    issueIds[idx] = String.valueOf(issue.getId());
  }
  List<CaseManagementNote> notes = cmgmtMgr.getNotes(demoNo + "", issueIds);

%>

<!DOCTYPE html>
<html lang="en">
<head>


  <base
    href="<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" %>">

  <script type="text/javascript">
    const ctx = '${ ctx }';
  </script>
  <script type="text/javascript" src="${ ctx }/library/jquery/jquery-3.6.4.min.js"></script>
  <script type="text/javascript" src="${ ctx }/library/jquery/jquery-ui-1.12.1.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/library/DataTables/DataTables-1.13.4/js/jquery.dataTables.js"></script>

  <script type="text/javascript">
    jQuery.noConflict();
  </script>

  <link href="${ ctx }/css/searchDrug3.css" rel="stylesheet" type="text/css"/>
  <link rel="stylesheet" type="text/css" href="${ctx}/library/jquery/jquery-ui-1.12.1.min.css"/>
  <link href="${pageContext.request.contextPath}/library/DataTables/DataTables-1.13.4/css/jquery.dataTables.css" rel="stylesheet" type="text/css"/>

  <style>
    /* Drug maintenance (LT) toggle switch — moved here from ListDrugs.jsp so these
       styles survive rebuildDrugProfile(), which clears #drugProfile on every
       legend-button click and would otherwise discard ListDrugs.jsp's inline <style>. */

    /* Container */
    .drug-maintenance-switch {
        top: -2px;
        position: relative;
        width: 34px;
        height: 16px;
    }

    /* Hide the native checkbox */
    .drug-maintenance-switch-input {
        display: none;
    }

    /* Switch track */
    .drug-maintenance-switch-label {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: #dfdfdf;
        cursor: pointer;
        transition: background-color 0.3s;
        border-radius: 5%;
    }

    /* Knob (unchecked) */
    .drug-maintenance-switch-label::after {
        text-align: center;
        content: '';
        font-size: xx-small;
        font-stretch: extra-expanded;
        display: flex;
        justify-content: center;
        align-items: center;
        position: absolute;
        top: 2px;
        left: 2px;
        width: 12px;
        height: 12px;
        background-color: #FFFFFFAF;
        border-radius: 50%;
        transition: transform 0.3s, content 0.3s, width 0.3s, height 0.3s, border-radius 0.3s;
        box-shadow: 0 1px 6px rgba(0, 0, 0, 0.4);
    }

    /* Knob (checked → shows "LT") */
    .drug-maintenance-switch-input:checked + .drug-maintenance-switch-label::after {
        content: 'LT';
        transform: translateX(14px);
        border-radius: 5%;
        width: 16px;
        height: 12px;
        color: white;
        background-color: #1e7e34AF;
    }

    /* Disabled track */
    .drug-maintenance-switch-input:disabled + .drug-maintenance-switch-label {
        background-color: #e0e0e0;
        cursor: not-allowed;
    }

    /* Disabled knob (checked) */
    .drug-maintenance-switch-input:disabled:checked + .drug-maintenance-switch-label::after {
        background-color: #1e7e349F;
        transform: translateX(14px);
        content: 'LT';
        width: 16px;
        height: 12px;
        border-radius: 5%;
    }

    /* Prescription name column — allow wrapping */
    .list-drugs td:nth-child(5) {
        word-break: break-word;
        white-space: normal;
    }
  </style>

  <script type="text/javascript" src="${ctx}/js/global.js"></script>
  <script src="${ctx}/csrfguard"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/prototype.js"/>"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/screen.js"/>"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/rx.js"/>"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/scriptaculous.js"/>"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/effects.js"/>"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/controls.js"/>"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/Oscar.js"/>"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/dragiframe.js"/>"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/js/checkDate.js"/>"></script>

  <link rel="stylesheet" type="text/css" href="<c:out value="${ctx}/share/yui/css/autocomplete.css"/>">
  <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/yahoo-dom-event.js"/>"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/connection-min.js"/>"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/animation-min.js"/>"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/datasource-min.js"/>"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/autocomplete-min.js"/>"></script>
  <script type="text/javascript" src="<c:out value="${ctx}/js/checkDate.js"/>"></script>

  <%-- RxSessionInterceptor: Enables multi-patient tab support by adding demographicNo to AJAX calls --%>
  <script type="text/javascript">
    var currentDemographicNo = '<%= Encode.forJavaScript(Integer.toString(rxSessionBean.getDemographicNo())) %>';
  </script>
  <script type="text/javascript" src="${ctx}/oscarRx/js/rxSessionInterceptor.js"></script>

  <script type="text/javascript">
    let selectedReRxIDs = [];

    function saveLinks(randNumber) {
      $('method_' + randNumber).onblur();
      $('route_' + randNumber).onblur();
      $('frequency_' + randNumber).onblur();
      $('minimum_' + randNumber).onblur();
      $('maximum_' + randNumber).onblur();
      $('duration_' + randNumber).onblur();
      $('durationUnit_' + randNumber).onblur();
    }


    function handleEnter(inField, ev) {
      let charCode;
      if (ev && ev.which) {
        charCode = ev.which;
      } else if (window.event) {
        ev = window.event;
        charCode = ev.keyCode;
      }
      let id = inField.id.split("_")[1];
      if (charCode === 13) {
        showHideSpecInst('siAutoComplete_' + id);
      }
    }

    //has to be in here, not prescribe.jsp for it to work in IE 6/7 and probably 8.
    function showHideSpecInst(elementId) {
      if ($(elementId).getStyle('display') == 'none') {
        Effect.BlindDown(elementId);
      } else {
        Effect.BlindUp(elementId);
      }
    }

    function resetReRxDrugList() {
      let rand = Math.floor(Math.random() * 10001);
      let url = ctx + "/oscarRx/deleteRx.do?parameterValue=clearReRxDrugList";
      let data = "rand=" + rand;
      new Ajax.Request(url, {
        method: 'post', parameters: data, onSuccess: function (transport) {
          // updateCurrentInteractions();
        }
      });
    }

    function onPrint(cfgPage) {
      let docF = $('printFormDD');

      docF.action = "<%= request.getContextPath() %>/form/createpdf?__title=Rx&__cfgfile=" + cfgPage + "&__template=a6blank";
      docF.target = "_blank";
      docF.submit();
      return true;
    }

    function buildRoute() {

      pickRoute = "";
    }


    function popupRxSearchWindow() {
      let winX = (document.all) ? window.screenLeft : window.screenX;
      let winY = (document.all) ? window.screenTop : window.screenY;

      let top = winY + 70;
      let left = winX + 110;
      let url = ctx + "/oscarRx/searchDrug.do?rx2=true&searchString=" + encodeURIComponent($('searchString').value);
      popup2(600, 800, top, left, url, 'windowNameRxSearch<%=Encode.forJavaScript(String.valueOf(demoNo))%>');

    }


    function popupRxReasonWindow(demographic, id) {
      let winX = (document.all) ? window.screenLeft : window.screenX;
      let winY = (document.all) ? window.screenTop : window.screenY;

      let top = winY + 70;
      let left = winX + 110;
      let url = ctx + "/oscarRx/SelectReason.jsp?demographicNo=" + demographic + "&drugId=" + encodeURIComponent(id);
      popup2(575, 650, top, left, url, 'windowNameRxReason<%=Encode.forJavaScript(String.valueOf(demoNo))%>');

    }


    let highlightMatch = function (full, snippet, matchindex) {
      return "<a title='" + full + "'>" + full.substring(0, matchindex) +
        "<span class=match>" + full.substr(matchindex, snippet.length) + "</span>" + full.substring(matchindex + snippet.length) + "</a>";
    };

    let highlightMatchInactiveMatchWord = function (full, snippet, matchindex) {
      //oscarLog(full+"--"+snippet+"--"+matchindex);
      return "<a title='" + full + "'>" + "<span class=matchInactive>" + full.substring(0, matchindex) +
        "<span class=match>" + full.substr(matchindex, snippet.length) + "</span>" + full.substring(matchindex + snippet.length) + "</span>" + "</a>";
    };
    let highlightMatchInactive = function (full, snippet, matchindex) {
          return "<a title='" + full + "'>" + "<span class=matchInactive>" + full + "</span>" + "</a>";
    };
    let resultFormatter = function (oResultData, sQuery, sResultMatch) {
      let query = sQuery.toUpperCase();
      let drugName = oResultData[0];

      let mIndex = drugName.toUpperCase().indexOf(query);
      let display = '';

      if (mIndex > -1) {
        display = highlightMatch(drugName, query, mIndex);
      } else {
        display = drugName;
      }
      return display;
    };
    let resultFormatter2 = function (oResultData, sQuery, sResultMatch) {
      /*oscarLog("oResultData, sQuery, sResultMatch="+oResultData+"--"+sQuery+"--"+sResultMatch);
               oscarLog("oResultData[0]="+oResultData[0]);
               oscarLog("oResultData.name="+oResultData.name);
               oscarLog("oResultData.name="+oResultData.id);*/
      let query = sQuery.toUpperCase();
      let drugName = oResultData.name;
      let isInactive = oResultData.isInactive;
      //oscarLog("isInactive="+isInactive);

      let mIndex = drugName.toUpperCase().indexOf(query);
      let display = '';
      if (mIndex > -1 && (isInactive == 'true' || isInactive == true)) { //match and inactive
        display = highlightMatchInactiveMatchWord(drugName, query, mIndex);
      } else if (mIndex > -1 && (isInactive == 'false' || isInactive == false || isInactive == undefined || isInactive == null)) { //match and active
        display = highlightMatch(drugName, query, mIndex);
      } else if (mIndex <= -1 && (isInactive == 'true' || isInactive == true)) {//no match and inactive
        display = highlightMatchInactive(drugName, query, mIndex);
      } else {//active and no match
        display = drugName;
      }


      return display;
    };

    // addEvent(window, "load", sortables_init);
    //
    // let SORT_COLUMN_INDEX;
    //
    // function sortables_init() {
    //   // Find all tables with class sortable and make them sortable
    //
    //   if (!document.getElementsByTagName) return;
    //
    //   tbls = document.getElementsByTagName("table");
    //
    //   for (ti = 0; ti < tbls.length; ti++) {
    //     thisTbl = tbls[ti];
    //
    //     if (((' ' + thisTbl.className + ' ').indexOf("sortable") != -1) && (thisTbl.id)) {
    //       //initTable(thisTbl.id);
    //       ts_makeSortable(thisTbl);
    //     }
    //   }
    // }

    // function ts_makeSortable(table) {
    //   oscarLog('making ' + table + ' sortable');
    //   if (table.rows && table.rows.length > 0) {
    //     let firstRow = table.rows[0];
    //   }
    //   if (!firstRow) return;
    //   oscarLog('Gets past here');
    //
    //   // We have a first row: assume it's the header, and make its contents clickable links
    //   for (let i = 0; i < firstRow.cells.length; i++) {
    //     let cell = firstRow.cells[i];
    //     let txt = ts_getInnerText(cell);
    //     cell.innerHTML = '<a href="#"  class="sortheader" ' +
    //       'onclick="ts_resortTable(this, ' + i + ');return false;">' +
    //       txt + '<span class="sortarrow"></span></a>';
    //   }
    // }
    //
    // function ts_getInnerText(el) {
    //   if (typeof el == "string") return el;
    //   if (typeof el == "undefined") {
    //     return el
    //   }
    //   ;
    //   if (el.innerText) return el.innerText;	//Not needed but it is faster
    //   let str = "";
    //
    //   let cs = el.childNodes;
    //   let l = cs.length;
    //   for (let i = 0; i < l; i++) {
    //     switch (cs[i].nodeType) {
    //       case 1: //ELEMENT_NODE
    //         str += ts_getInnerText(cs[i]);
    //         break;
    //       case 3:	//TEXT_NODE
    //         str += cs[i].nodeValue;
    //         break;
    //     }
    //   }
    //   return str;
    // }
    //
    // function ts_resortTable(lnk, clid) {
    //   // get the span
    //   let span;
    //   for (let ci = 0; ci < lnk.childNodes.length; ci++) {
    //     if (lnk.childNodes[ci].tagName && lnk.childNodes[ci].tagName.toLowerCase() == 'span') span = lnk.childNodes[ci];
    //   }
    //   let spantext = ts_getInnerText(span);
    //   let td = lnk.parentNode;
    //   let column = clid;
    //   let table = getParent(td, 'TABLE');
    //
    //   // Work out a type for the column
    //   if (table.rows.length <= 1) return;
    //
    //
    //   let itm = ts_getInnerText(table.rows[1].cells[column]).trim();
    //   sortfn = ts_sort_caseinsensitive;
    //   if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d\d\d$/)) sortfn = ts_sort_date;
    //   if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d$/)) sortfn = ts_sort_date;
    //   if (itm.match(/^[\Uffffffff$]/)) sortfn = ts_sort_currency;
    //   if (itm.match(/^[\d\.]+$/)) sortfn = ts_sort_numeric;
    //   SORT_COLUMN_INDEX = column;
    //   let firstRow = new Array();
    //   let newRows = new Array();
    //   for (i = 0; i < table.rows[0].length; i++) {
    //     firstRow[i] = table.rows[0][i];
    //   }
    //   for (j = 1; j < table.rows.length; j++) {
    //     newRows[j - 1] = table.rows[j];
    //   }
    //
    //   newRows.sort(sortfn);
    //
    //   if (span.getAttribute("sortdir") == 'down') {
    //     ARROW = '&nbsp;&nbsp;&uarr;';
    //     newRows.reverse();
    //     span.setAttribute('sortdir', 'up');
    //   } else {
    //     ARROW = '&nbsp;&nbsp;&darr;';
    //     span.setAttribute('sortdir', 'down');
    //   }
    //
    //   // We appendChild rows that already exist to the tbody, so it moves them rather than creating new ones
    //   // don't do sortbottom rows
    //   for (i = 0; i < newRows.length; i++) {
    //     if (!newRows[i].className || (newRows[i].className && (newRows[i].className.indexOf('sortbottom') == -1))) table.tBodies[0].appendChild(newRows[i]);
    //   }
    //   // do sortbottom rows only
    //   for (i = 0; i < newRows.length; i++) {
    //     if (newRows[i].className && (newRows[i].className.indexOf('sortbottom') != -1)) table.tBodies[0].appendChild(newRows[i]);
    //   }
    //
    //   // Delete any other arrows there may be showing
    //   let allspans = document.getElementsByTagName("span");
    //   for (let ci = 0; ci < allspans.length; ci++) {
    //     if (allspans[ci].className == 'sortarrow') {
    //       if (getParent(allspans[ci], "table") == getParent(lnk, "table")) { // in the same table as us?
    //         allspans[ci].innerHTML = '';
    //       }
    //     }
    //   }
    //
    //   span.innerHTML = ARROW;
    // }
    //
    // function getParent(el, pTagName) {
    //   if (el == null) return null;
    //   else if (el.nodeType == 1 && el.tagName.toLowerCase() == pTagName.toLowerCase())	// Gecko bug, supposed to be uppercase
    //     return el;
    //   else
    //     return getParent(el.parentNode, pTagName);
    // }
    //
    // function ts_sort_date(a, b) {
    //   // y2k notes: two digit years less than 50 are treated as 20XX, greater than 50 are treated as 19XX
    //   aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
    //   bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
    //   if (aa.length == 10) {
    //     dt1 = aa.substr(6, 4) + aa.substr(3, 2) + aa.substr(0, 2);
    //   } else {
    //     yr = aa.substr(6, 2);
    //     if (parseInt(yr) < 50) {
    //       yr = '20' + yr;
    //     } else {
    //       yr = '19' + yr;
    //     }
    //     dt1 = yr + aa.substr(3, 2) + aa.substr(0, 2);
    //   }
    //   if (bb.length == 10) {
    //     dt2 = bb.substr(6, 4) + bb.substr(3, 2) + bb.substr(0, 2);
    //   } else {
    //     yr = bb.substr(6, 2);
    //     if (parseInt(yr) < 50) {
    //       yr = '20' + yr;
    //     } else {
    //       yr = '19' + yr;
    //     }
    //     dt2 = yr + bb.substr(3, 2) + bb.substr(0, 2);
    //   }
    //   if (dt1 == dt2) return 0;
    //   if (dt1 < dt2) return -1;
    //   return 1;
    // }
    //
    // function ts_sort_currency(a, b) {
    //   aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g, '');
    //   bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g, '');
    //   return parseFloat(aa) - parseFloat(bb);
    // }
    //
    // function ts_sort_numeric(a, b) {
    //   aa = parseFloat(ts_getInnerText(a.cells[SORT_COLUMN_INDEX]));
    //   if (isNaN(aa)) aa = 0;
    //   bb = parseFloat(ts_getInnerText(b.cells[SORT_COLUMN_INDEX]));
    //   if (isNaN(bb)) bb = 0;
    //   return aa - bb;
    // }
    //
    // function ts_sort_caseinsensitive(a, b) {
    //   aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]).toLowerCase();
    //   bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]).toLowerCase();
    //   if (aa == bb) return 0;
    //   if (aa < bb) return -1;
    //   return 1;
    // }
    //
    // function ts_sort_default(a, b) {
    //   aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
    //   bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
    //   if (aa == bb) return 0;
    //   if (aa < bb) return -1;
    //   return 1;
    // }


    function addEvent(elm, evType, fn, useCapture)
// addEvent and removeEvent
// cross-browser event handling for IE5+,  NS6 and Mozilla
// By Scott Andrew
    {
      if (elm.addEventListener) {
        elm.addEventListener(evType, fn, useCapture);
        return true;
      } else if (elm.attachEvent) {
        let r = elm.attachEvent("on" + evType, fn);
        return r;
      } else {
        alert("Handler could not be removed");
      }
    }

    function checkFav() {

      let usefav = '<%=Encode.forJavaScript(usefav)%>';
      let favid = '<%=Encode.forJavaScript(favid)%>';
      if (usefav === "true" && favid !== null && favid !== 'null') {
        useFav2(favid);
      } else {
      }
    }

    function renderRxStage() {
      jQuery('#rxText').show();
      jQuery('#prescriptionStageSet').show();
      jQuery('#savePrescriptionButtonSet').show();
    }

    //not used , represcribe a drug
    function represcribeOnLoad(drugId) {
      let data = "method=saveReRxDrugIdToStash&drugId=" + encodeURIComponent(drugId) + "&rand=" + Math.floor(Math.random() * 10001);
      let url = ctx + "/oscarRx/rePrescribe2.do";
      new Ajax.Updater('rxText', url, {
        method: 'POST', parameters: data,
        requestHeaders: {'Accept': 'application/json'},
        evalScripts: true, insertion: Insertion.Bottom,
        onSuccess: function (transport) {
          renderRxStage();
        }
      });

    }


    <%--function moveDrugDown(drugId, swapDrugId, demographicNo) {--%>
    <%--  new Ajax.Request('<c:out value="${ctx}"/>/oscarRx/reorderDrug.do?method=update&direction=down&drugId=' + encodeURIComponent(drugId) + '&swapDrugId=' + encodeURIComponent(swapDrugId) + '&demographicNo=' + demographicNo + "&rand=" + Math.floor(Math.random() * 10001), {--%>
    <%--    method: 'get',--%>
    <%--    onSuccess: function (transport) {--%>
    <%--      callReplacementWebService("ListDrugs.jsp", 'drugProfile');--%>
    <%--      resetReRxDrugList();--%>
    <%--      resetStash();--%>
    <%--    }--%>
    <%--  });--%>
    <%--}--%>

    <%--function moveDrugUp(drugId, swapDrugId, demographicNo) {--%>
    <%--  new Ajax.Request('<c:out value="${ctx}"/>/oscarRx/reorderDrug.do?method=update&direction=up&drugId=' + encodeURIComponent(drugId) + '&swapDrugId=' + encodeURIComponent(swapDrugId) + '&demographicNo=' + demographicNo + "&rand=" + Math.floor(Math.random() * 10001), {--%>
    <%--    method: 'get',--%>
    <%--    onSuccess: function (transport) {--%>
    <%--      callReplacementWebService("ListDrugs.jsp", 'drugProfile');--%>
    <%--      resetReRxDrugList();--%>
    <%--      resetStash();--%>
    <%--    }--%>
    <%--  });--%>
    <%--}--%>

    function showPreviousPrints(scriptNo) {
      popupWindow(720, 700, ctx + '/oscarRx/ShowPreviousPrints.jsp?scriptNo=' + scriptNo, 'ShowPreviousPrints')
    }

    let Lst;

    function CngClass(obj) {
      const defaultEl = document.getElementById("selected_default");
      if (defaultEl) defaultEl.className = '';
      if (Lst) Lst.className = '';
      obj.className = 'selected';
      Lst = obj;
    }

    function toggleStartDateUnknown(rand) {
      let cb = document.getElementById('startDateUnknown_' + rand);
      let txt = document.getElementById('rxDate_' + rand);
      if (cb.checked) {
        <%
    			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
    			String today = formatter.format(new java.util.Date());
    		%>
        txt.disabled = true;
        txt.value = '<%=Encode.forJavaScript(String.valueOf(today))%>';
      } else {
        txt.disabled = false;
      }
    }


    //this is a SJHH specific feature
    function completeMedRec() {
      let ok = confirm("Are you sure you would like to mark the Med Rec as complete?");
      if (ok) {
        let url = ctx + "/oscarRx/completeMedRec.jsp?demographicNo=<%=Encode.forUriComponent(String.valueOf(rxSessionBean.getDemographicNo()))%>";
        let data;
        new Ajax.Request(url, {
          method: 'get', parameters: data, onSuccess: function (transport) {
            alert('Completed.')
          }
        });
      }
    }

    function printDrugProfile() {
      let ids = [];
      jQuery("input[type='checkbox'][id ^= 'reRxCheckBox']").each(function () {
        if (jQuery(this).is(":checked")) {
          let name = jQuery(this).attr('name').substring(9);
          ids.push(name);
        }
      });
      if (ids.length > 0) {
        popupWindow(720, 700, ctx + '/oscarRx/PrintDrugProfile2.jsp?ids=' + ids.join(','), 'PrintDrugProfile');
      } else {
        popupWindow(720, 700, ctx + '/oscarRx/PrintDrugProfile2.jsp', 'PrintDrugProfile');
      }
    }

  </script>
  <style media="screen">

    #Layer1 {
      position: absolute;
      left: 130px;
      top: 50px;
      width: 350px;
      height: auto;
      visibility: hidden;
      z-index: 1;
      background-color: white;
      border: 2px solid grey;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }

    .hiddenLayer {
      width: 100%;
      padding: 2px 10px 10px 10px;
      box-sizing: border-box;
    }

    .wcblayerTitle {
      width: 40%;
      padding-left: 20px;
      font-weight: bold;
      background-color: #f2f2f2;
      text-align: center;
    }

    .wcblayerContent {
      padding-left: 20px;

    }

    #statusDisplay {
      font-size: x-small;
      display: flex;
      flex-direction: row;
      gap: 5px;

    }

    #statusDisplay label {
      font-weight: bold;
    }

    .custom-modal-size {
      max-width: 95vw !important; /* Default for small screens */
      width: 95vw !important;
    }

    @media (min-width: 1200px) {
      /* xl breakpoint */
      .custom-modal-size {
        max-width: 1080px !important;
        width: 90vw !important;
      }
    }

    @media (min-width: 1300px) {
      /* xxl breakpoint */
      .custom-modal-size {
        width: 85vw !important;
      }
    }

    @media (min-width: 1400px) {
      /* xxl breakpoint */
      .custom-modal-size {
        width: 75vw !important;
      }
    }

    @media (min-width: 1500px) {
      /* xxl+ breakpoint */
      .custom-modal-size {
        width: 70vw !important;
      }
    }

    @media (min-width: 1600px) {
      /* xxxl breakpoint */
      .custom-modal-size {
        width: 65vw !important;
      }
    }

    .custom-modal-height {
      height: 95vh;
      max-height: 95vh;
      overflow-y: auto;
    }
  </style>
  <style media="print">
    noprint {
      display: none;
    }

    justforprint {
      float: left;
    }
  </style>

  <style>
    /* Inline styles moved to head */
    .drugForm-inline {
      display: inline;
      margin-bottom: 0;
    }

    #searchDrugAutocompleteSet {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 8px;
    }

    #searchDrugAutocompleteSet label {
      white-space: nowrap;
      margin: 0;
    }

    #searchString {
      border: 1px solid #000;
    }

    #prescriptionStageSet {
      display: none;
    }

    #interactingDrugErrorMsg {
      display: none;
    }

    #previewForm {
      display: none;
    }

    .prescriptionRowTable {
      border-collapse: collapse;
    }

    #reRxConfirmBox p {
      margin-bottom: 12px;
      font-size: 11px;
      text-align: end;
    }

    #selectedCount {
      font-weight: bold;
    }

    #reRxConfirmBox .button-container {
      display: flex;
      gap: 10px;
      justify-content: flex-end;
    }

    #indicator1 {
      display: none;
    }

    #reprint {
      height: 150px;
      overflow: auto;
      border: thin solid #DCDCDC;
    }

    .text-indent-5 {
      text-indent: 5px;
    }

    .legend-change-view {
      text-align: left;
      width: 100px;
    }

    .link-red-no-decoration {
      color: red;
      text-decoration: none;
    }

    .link-default-selected {
      color: #000000;
      text-decoration: none;
      font-weight: bold;
    }

    .selected {
      color: #000000;
      text-decoration: none;
      font-weight: bold;
    }

    .link-no-decoration {
      text-decoration: none;
    }

    #dragifm {
      top: 0px;
      left: 0px;
    }

    #discontinueUI {
      position: absolute;
      width: 500px;
      height: 200px;
      background-color: white;
      padding: 20px;
      border: 1px solid grey;
    }

    /* Bootstrap added to this page resets h1-h6 to font-weight:500,
       which un-bolds the popup heading vs the pre-Bootstrap look. Restore bold here. */
    #discontinueUI h3 {
      font-weight: bold;
    }

    #drugProfile {
      padding-top: 0;
    }

    .flexDisplayContainer {
      display: flex;
      flex-direction: row;
      gap: 5px;
    }

    .reRxActionButtons {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-right: 15px;
    }

    .reRxActionButtons input:not(:first-child) {
      margin-left: 3px;
    }

    #printFormDD {
      display: none;
    }

    .tableRxTitleAdjust {
      margin-top: 5px;
    }

    .rightColumnAdjust {
      padding-left: 10px;
      /* Table cells default to vertical-align: middle, so a long favourites column (left cell)
         makes the row tall and drifts this content to the page middle (#2464). Pin it to top. */
      vertical-align: top;
    }
  </style>

  <title>Medications</title>

  <script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function initializeRxPage() {
      checkFav();
      iterateStash();
      checkReRxLongTerm();
      load();
    });
  </script>
</head>

<%
  boolean showall = false;

  if (request.getParameter("show") != null) {
    if (request.getParameter("show").equals("all")) {
      showall = true;
    }
  }
%>


<body class="yui-skin-sam">

<div id="searchDrug3Wrapper">
  <%=WebUtils.popErrorAndInfoMessagesAsHtml(session)%>
  <table id="AutoNumber1">
    <%@ include file="TopLinks2.jspf" %><!-- Row On included here-->
    <tr>
      <td style="vertical-align: top;">
        <%@ include file="SideLinksEditFavorites2.jsp" %>
      </td>
      <td class="rightColumnAdjust"><!--Column Two Row Two-->

        <div class="floatingWindow" id="reRxConfirmBox">
          <p>
            You have selected <span id="selectedCount">0</span> ReRx
            medications. Click Stage Medication to add them to your prescriptions.
          </p>
          <div class="button-container">
            <input type="button" name="cancel" class="ControlPushButton" value="Cancel"
                   onclick="cancelAndClearSelection()" title="Cancel">
            <input type="button" name="stage" class="ControlPushButton" value="Stage Medication"
                   onclick="stageSelectedReRxMedications()" title="Stage Medications">
          </div>
        </div>

        <table class="prescriptionRowTable">
          <tr id="medicationManagementRow">
            <td>
              <%if (securityManager.hasWriteAccess("_rx", roleName2$, true)) {%>
              <form action="${pageContext.request.contextPath}/oscarRx/searchDrug.do"
                    onsubmit="return checkEnterSendRx();" class="drugForm-inline" id="drugForm"
                    name="drugForm" method="post">
                <input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>

                <input type="hidden" name="demographicNo"
                       value="<%=Encode.forHtmlAttribute(Integer.toString(demoNo))%>"/>
                <table>
                  <tr id="prescriptionStageRow">
                    <td>

                      <div id="prescriptionStageSet">

                        <div id="interactingDrugErrorMsg"></div>

                        <div id="rxText"></div>
                        <%-- Prescriptions are staged here via the prescribe.jsp widget --%>

                        <input type="hidden" id="deleteOnCloseRxBox" value="false"/>
                        <input type="hidden" name="demographicNo" value="<%=Encode.forHtmlAttribute(Integer.toString(demoNo))%>"/>

                      </div>
                      <input type="hidden" id="rxPharmacyId" name="rxPharmacyId" value=""/>

                    </td>
                  </tr>
                  <tr id="searchPrescriptionRow">
                    <td>
                      <div id="searchDrugSet">
                        <div id="searchDrugAutocompleteSet">
                          <label for="searchString"><fmt:message key="SearchDrug.drugSearchTextBox"/></label>
                          <input type="text" class="ui-widget-content" id="searchString" name="searchString"
                                 autocomplete="off">
                          <div id="autocomplete_choices"></div>
                        </div>
                        <div id="advanceSearchParameters">
                          <fieldset id="drugCategorySet">
                            <input type="radio" id="allCategories" name="method"
                                   value="searchAllCategories" class="trigger"
                                   checked="checked"/>
                            <label for="allCategories">All</label>

                            <input type="radio" id="brandName" name="method"
                                   value="searchBrandName" class="trigger" disabled/>
                            <label for="brandName">Brand</label>

                            <input type="radio" id="genericName" name="method"
                                   value="searchGenericName" class="trigger" disabled/>
                            <label for="genericName">Ingredient</label>

                            <input type="radio" id="naturalRemedy" name="method"
                                   disabled="disabled"
                                   value="searchNaturalRemedy" class="trigger"/>
                            <label for="naturalRemedy">Natural</label>
                          </fieldset>
                          <fieldset id="searchParamSet">
                            <input type="radio" id="wildCardBoth" name="wildcard"
                                   value="false" checked="checked"/>
                            <label title="Search exactly as typed (right to left)"
                                   for="wildCardBoth">Exact</label>

                            <input type="radio" id="wildCardRight" name="wildcard"
                                   value="true"/>
                            <label title="Search for all words in all phrases"
                                   for="wildCardRight">Any</label>
                          </fieldset>
                        </div>
                      </div>
                    </td>
                    <td>
                      <div id="searchDrugsButtonSet">
                        <input type="button" name="search" class="btn btn-primary  ControlPushButton"
                               value="<fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.msgSearch"/>"
                               onclick="popupRxSearchWindow();"
                               title="<fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.help.Search"/>">
                        <input id="customDrug" type="button" class="btn btn-primary  ControlPushButton" onclick="customWarning2();"
                               value="<fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.msgCustomDrugRx3"/>"
                               title="<fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.help.CustomDrug"/>"/>
                        <input id="customNote" type="button" class="btn btn-primary  ControlPushButton" onclick="customNoteWarning();"
                               value="<fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.msgNoteRx3"/>"
                               title="<fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.help.CustomNote"/>"/>
                        <input id="reset" type="button" class="btn btn-primary  ControlPushButton" title="Clear pending prescriptions"
                               onclick="resetStash();"
                               value="<fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.msgResetPrescriptionRx3"/>"/>

                        <%if (OscarProperties.getInstance().hasProperty("ONTARIO_MD_INCOMINGREQUESTOR")) {%>
                        <a href="javascript:goOMD();" class="btn btn-primary  ControlPushButton"
                           title="<fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.help.OMD"/>"><fmt:setBundle
                          basename="oscarResources"/><fmt:message key="SearchDrug.msgOMDLookup"/></a>
                        <%}%>
                        <security:oscarSec roleName="<%=roleName2$%>" objectName="_rx" rights="x">
                          <input id="saveButton" type="button" class="btn btn-primary  ControlPushButton"
                                 onclick="updateSaveAllDrugsPrintCheckContinue();"
                                 value="<fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.msgSaveAndPrint"/>"
                                 title="<fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.help.SaveAndPrint"/>"/>
                        </security:oscarSec>

                        <input id="saveOnlyButton" type="button" class="btn btn-primary  ControlPushButton"
                               onclick="updateSaveAllDrugsCheckContinue();"
                               value="<fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.msgSaveOnly"/>"
                               title="<fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.help.Save"/>"/>
                      </div>
                    </td>

                  </tr>
                </table>

              </form>
              <div id="previewForm"></div>
              <%} %>
            </td>
          </tr>
          <tr id="patientDrugListRow"><!--put this left-->
            <td>
              <table><!--drug profile, view and listdrugs.jsp-->
                <tr>
                  <td>
                    <div class="DivContentSectionHead">
<%--                      <fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.section2Title"/>--%>
<%--                 \--%>
                      <a href="javascript:void(0)" class="btn btn-link" onClick="printDrugProfile();"><fmt:setBundle
                        basename="oscarResources"/><fmt:message key="SearchDrug.Print"/></a>

                      <%if (securityManager.hasWriteAccess("_rx", roleName2$, true)) {%>
                      <a href="javascript:void(0);"  class="btn btn-link"  onclick="document.getElementById('reprint').toggleAttribute('hidden');return false;"><fmt:setBundle
                        basename="oscarResources"/><fmt:message key="SearchDrug.Reprint"/></a>

                      <a href="javascript:void(0);"  class="btn btn-link"  id="cmdRePrescribe" onclick="RePrescribeLongTerm();"><fmt:setBundle basename="oscarResources"/><fmt:message
                        key="SearchDrug.msgReprescribeLongTermMed"/></a>

                      <% } %>
                      <a  class="btn btn-link"
                          href="javascript:popupWindow(720,920, ctx + '/oscarRx/chartDrugProfile.jsp?demographic_no=<%=Encode.forUriComponent(String.valueOf(demoNo))%>','PrintDrugProfile2')">Timeline
                        Drug Profile</a>

                    </div>

                  </td>
                </tr>
                <tr>
                  <%-- hidden attribute is toggled by JS via toggleAttribute('hidden') — do not move to CSS or the reprint section will not open --%>
                  <td id="reprint" hidden>


                      <% for (int i = 0; prescribedDrugs.length > i; i++) {
                            RxPrescriptionData.Prescription drug =  prescribedDrugs[i];
                        %>

                      <%
                            if (drug.getScript_no() != null && script_no.equals(drug.getScript_no())) {
                                                    %>


                    <div class="text-indent-5">
                      <a href="javascript:void(0);" onclick="reprint2('<%=Encode.forJavaScript(String.valueOf(drug.getScript_no()))%>')">
                        <%=Encode.forHtml(String.valueOf(drug.getRxDisplay()))%>
                      </a>
                    </div>

                      <%
                            } else {

                                         if(i != 0) { %>
</div>
<%}%>
<div class="reprintRxItem">
  <div class="reprintRxItemHeading">
    <div>
      <strong>Rx: <%=Encode.forHtml(String.valueOf(drug.getRxDate()))%>
      </strong>
    </div>
    <div>
      <a href="javascript:void(0)" onclick="showPreviousPrints(<%=Encode.forJavaScript(String.valueOf(drug.getScript_no()))%>);return false;">
        <%=Encode.forHtml(String.valueOf(drug.getNumPrints()))%> Print(s)
      </a>
    </div>
  </div>
  <div class="text-indent-5">
    <a href="javascript:void(0);" onclick="reprint2('<%=Encode.forJavaScript(String.valueOf(drug.getScript_no()))%>')"><%=Encode.forHtml(String.valueOf(drug.getRxDisplay()))%>
    </a>
  </div>


  <%} %>

  <% script_no = drug.getScript_no() == null ? "" : drug.getScript_no();
    if (prescribedDrugs.length == i + 1) { %>
</div> <!-- closes the LAST reprintRxItem wrapper -->
<%
    }
  }
%>


</td>
</tr>
<tr>
  <td>
    <table>
      <tr>
        <td>
          <table class="legend">
            <tr>
              <td class="legend-change-view">
                <a href="#"
                   title="<fmt:setBundle basename="oscarResources"/><fmt:message key='provider.rxChangeProfileViewMessage'/>"
                   onclick="popupPage(230,860,'../setProviderStaleDate.do?method=viewRxProfileView');"
                   class="link-red-no-decoration">
                  <fmt:message key="provider.rxChangeProfileView"/>
                </a>
              </td>

              <td>

                <table class="legend_items" align="left">
                  <tr>
                    <%if (show_current) {%>
                    <td>
                      <a href="javascript:void(0);"
                         onclick="filterDrugView('current');CngClass(this);"
                         id="selected_default" class="link-default-selected"
                         TITLE="<fmt:setBundle basename="oscarResources"/><fmt:message key='SearchDrug.msgShowCurrentDesc'/>">
                        <fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.msgShowCurrent"/>
                      </a>
                    </td>
                    <%
                      }
                      if (show_all) {
                    %>
                    <td>
                      <a href="javascript:void(0);"
                         onclick="filterDrugView('all');CngClass(this);"
                         Title="<fmt:setBundle basename="oscarResources"/><fmt:message key='SearchDrug.msgShowAllDesc'/>">
                        <fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.msgShowAll"/>
                      </a>
                    </td>
                    <%
                      }
                      if (active) {
                    %>
                    <td>
                      <a href="javascript:void(0);"
                         onclick="filterDrugView('active');CngClass(this);"
                         TITLE="<fmt:setBundle basename="oscarResources"/><fmt:message key='SearchDrug.msgActiveDesc'/>">
                        <fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.msgActive"/>
                      </a>
                    </td>
                    <%
                      }
                      if (inactive) {
                    %>
                    <td>
                      <a href="javascript:void(0);"
                         onclick="filterDrugView('inactive');CngClass(this);"
                         TITLE="<fmt:setBundle basename="oscarResources"/><fmt:message key='SearchDrug.msgInactiveDesc'/>">
                        <fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.msgInactive"/>
                      </a>
                    </td>
                    <%
                      }
                      if (!OscarProperties.getInstance().getProperty("rx.profile_legend.hide", "false").equals("true")) {

                        if (longterm_acute) {
                    %>
                    <td>
                      <a href="javascript:void(0);"
                         onclick="showLongTermAcuteView();CngClass(this);"
                         TITLE="<fmt:setBundle basename='oscarResources'/><fmt:message key='SearchDrug.msgLongTermAcuteDesc'/>">
                        <fmt:setBundle basename="oscarResources"/><fmt:message key="SearchDrug.msgLongTermAcute"/>
                      </a>
                    </td>
                    <%
                      }
                      if (longterm_acute_inactive_external) {
                    %>
                    <td>
                      <a href="javascript:void(0);"
                         onclick="showFullProfileView();CngClass(this);"
                         TITLE="<fmt:setBundle basename='oscarResources'/><fmt:message key='SearchDrug.msgLongTermAcuteInactiveExternalDesc'/>">
                        <fmt:setBundle basename="oscarResources"/><fmt:message
                        key="SearchDrug.msgLongTermAcuteInactiveExternal"/>
                      </a>
                    </td>
                    <%
                        }
                      }
                    %>
                  </tr>
                </table>

              </td>

            </tr>
          </table>
          <div id="drugProfile"></div>

          <div id="themeLegend">
            <a href="javascript:void(0);" class="currentDrug">Drug that is current</a> |
            <%if (!OscarProperties.getInstance().getProperty("rx.delete_drug.hide", "false").equals("true")) {%>
            <a href="javascript:void(0);" class="archivedDrug">Drug that is archived</a> |
            <%} %>
            <a href="javascript:void(0);" class="expireInReference">Drug that is current but will expire within the
              reference range</a> |
            <a href="javascript:void(0);" class="expiredDrug">Drug that is expired</a> |
            <a href="javascript:void(0);" class="longTermMed">Long Term Med Drug</a> |
            <a href="javascript:void(0);" class="discontinued">Discontinued Drug</a> |
            <a href="javascript:void(0);" class="external">Prescribed by an outside provider</a>
          </div>

          <form action="/oscarRx/rePrescribe">
            <input type="hidden" property="drugList"/>
            <input type="hidden" name="method">
          </form>
          <br>
          <form action="${pageContext.request.contextPath}/oscarRx/deleteRx.do" method="post">
            <input type="hidden" name="drugList" id="drugList"/>
          </form>
        </td>

      </tr>
    </table>
  </td>
</tr>
</table>
<table><!--drug profile, view and listdrugs.jsp-->
  <tr>
    <td>
      <div class="DivContentSectionHead">
        Other Medications
      </div>
    </td>
  </tr>
  <tr>
    <td>
      <table class="sortable" id="OMedsTabls" width="50%" border="0" cellpadding="3">
        <tr>
          <th align="left">Date Entered</th>
          <th align="left">Medication</th>
        </tr>
        <%
          // java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

          for (CaseManagementNote note : notes) {
            if (!note.isLocked() && !note.isArchived()) {
              String str = note.getNote();
        %>
        <tr>
          <td><%=Encode.forHtml(String.valueOf(formatter.format(note.getCreate_date())))%>
          </td>
          <td><%=Encode.forHtml(str)%>
          </td>
        </tr>
        <%
            }
          }
        %>

      </table>
    </td>
  </tr>
</table>

</div>
</td>
</tr>
</table>
<%-- End List Drugs Prescribed --%>

</td>
</tr>
</table>


<div id="dragifm"></div>
<%-- hidden attribute is toggled by JS (hidden = true/false) — do not move to CSS as display:none or the discontinue popup will not open (Prototype show() cannot override a stylesheet rule) --%>
<div id="discontinueUI" hidden>
  <h3>Discontinue :<span id="disDrug"></span></h3>
  <input type="hidden" name="disDrugId" id="disDrugId"/>
  <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarRx.discontinuedReason.msgReason"/>
  <select name="disReason" id="disReason">
    <option value="adverseReaction"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.AdverseReaction"/></option>
    <option value="allergy"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.Allergy"/></option>
    <option value="cost"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.Cost"/></option>
    <option value="discontinuedByAnotherPhysician"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.DiscontinuedByAnotherPhysician"/></option>
    <option value="doseChange"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.DoseChange"/></option>
    <option value="drugInteraction"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.DrugInteraction"/></option>
    <option value="increasedRiskBenefitRatio"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.IncreasedRiskBenefitRatio"/></option>
    <option value="ineffectiveTreatment"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.IneffectiveTreatment"/></option>
    <option value="newScientificEvidence"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.NewScientificEvidence"/></option>
    <option value="noLongerNecessary"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.NoLongerNecessary"/></option>
    <option value="enteredInError"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.EnteredInError"/></option>
    <option value="patientRequest"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.PatientRequest"/></option>
    <option value="prescribingError"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.PrescribingError"/></option>
    <option value="simplifyingTreatment"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.SimplifyingTreatment"/></option>
    <option value="unknown"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.Unknown"/></option>

    <option value="other"><fmt:setBundle basename="oscarResources"/><fmt:message
      key="oscarRx.discontinuedReason.Other"/></option>
  </select>


  <br/>
  <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarRx.discontinuedReason.msgComment"/><br/>
  <textarea id="disComment" rows="3" cols="45"></textarea><br/>
  <input type="button" onclick="document.getElementById('discontinueUI').hidden = true;" value="Cancel"/>
  <input type="button"
         onclick="Discontinue2($('disDrugId').value,$('disReason').value,$('disComment').value,$('disDrug').innerHTML);"
         value="Discontinue"/>

</div>

<%
  if (pharmacyList != null) {
%>

<div id="Layer1"><!--  This should be changed to automagically fill if this changes often -->

  <table class="hiddenLayer">
    <tr>
      <td>&nbsp;</td>
      <td><a href="javascript: function myFunction() {return false; }" onclick="hidepic('Layer1');"
                           class="link-no-decoration"><img src='<c:out value="${ctx}/images/close.png"/>'
                                                               border="0"></a></td>
    </tr>

    <tr>
      <td class="wcblayerTitle"><fmt:setBundle basename="oscarResources"/><fmt:message
        key="SearchDrug.pharmacy.msgName"/></td>
      <td class="wcblayerContent" id="pharmacyName"></td>
    </tr>

    <tr>
      <td class="wcblayerTitle"><fmt:setBundle basename="oscarResources"/><fmt:message
        key="SearchDrug.pharmacy.msgAddress"/></td>
      <td class="wcblayerContent" id="pharmacyAddress"></td>
    </tr>
    <tr>
      <td class="wcblayerTitle"><fmt:setBundle basename="oscarResources"/><fmt:message
        key="SearchDrug.pharmacy.msgCity"/></td>
      <td class="wcblayerContent" id="pharmacyCity"></td>
    </tr>

    <tr>
      <td class="wcblayerTitle"><fmt:setBundle basename="oscarResources"/><fmt:message
        key="SearchDrug.pharmacy.msgProvince"/></td>
      <td class="wcblayerContent" id="pharmacyProvince"></td>
    </tr>
    <tr>
      <td class="wcblayerTitle"><fmt:setBundle basename="oscarResources"/><fmt:message
        key="SearchDrug.pharmacy.msgPostalCode"/></td>
      <td class="wcblayerContent" id="pharmacyPostalCode"></td>
    </tr>
    <tr>
      <td class="wcblayerTitle"><fmt:setBundle basename="oscarResources"/><fmt:message
        key="SearchDrug.pharmacy.msgPhone1"/></td>
      <td class="wcblayerContent" id="pharmacyPhone1"></td>
    </tr>
    <tr>
      <td class="wcblayerTitle"><fmt:setBundle basename="oscarResources"/><fmt:message
        key="SearchDrug.pharmacy.msgPhone2"/></td>
      <td class="wcblayerContent" id="pharmacyPhone2"></td>
    </tr>
    <tr>
      <td class="wcblayerTitle"><fmt:setBundle basename="oscarResources"/><fmt:message
        key="SearchDrug.pharmacy.msgFax"/></td>
      <td class="wcblayerContent" id="pharmacyFax"></td>
    </tr>
    <tr>
      <td class="wcblayerTitle"><fmt:setBundle basename="oscarResources"/><fmt:message
        key="SearchDrug.pharmacy.msgEmail"/></td>
      <td class="wcblayerContent" id="pharmacyEmail"></td>
    </tr>
    <tr>
      <td class="wcblayerTitle"><fmt:setBundle basename="oscarResources"/><fmt:message
        key="SearchDrug.pharmacy.msgNotes"/></td>
      <td class="wcblayerContent" id="pharmacyNotes"></td>
    </tr>
  </table>

</div>

<%
  }
%>
<script type="text/javascript">
  // Constants for timing and UI behavior
  const CHECKBOX_REVERT_DELAY_MS = 500;
  const SIGNATURE_PAD_INIT_DELAY_MS = 250;
  const AUTOCOMPLETE_MIN_LENGTH = 3;
  const AUTOCOMPLETE_DELAY_MS = 400;
  const AUTOCOMPLETE_MAX_RESULTS = 40;
  const RANDOM_CACHE_BUSTER_MAX = 10001;

  /**
   * Generates a cryptographically secure random integer for cache busting.
   * Falls back to Math.random() in older browsers.
   * @returns {number} Random integer between 0 and max-1
   */
  function generateSecureRandomId() {
    if (window.crypto && window.crypto.getRandomValues) {
      const array = new Uint32Array(1);
      window.crypto.getRandomValues(array);
      return array[0] % RANDOM_CACHE_BUSTER_MAX;
    }
    // Fallback for older browsers
    return Math.floor(Math.random() * RANDOM_CACHE_BUSTER_MAX);
  }

  /**
   * Safely sets text content of an element, escaping HTML.
   * @param {string} elementId - The ID of the element
   * @param {string} text - The text to set
   */
  function safeSetText(elementId, text) {
    const element = document.getElementById(elementId);
    if (element) {
      element.textContent = text || '';
    }
  }

  /**
   * Safely sets HTML content with basic XSS protection.
   * Only use for trusted content that has been server-side encoded.
   * @param {string} elementId - The ID of the element
   * @param {string} html - The HTML to set (must be pre-encoded)
   */
  function safeSetHTML(elementId, html) {
    const element = document.getElementById(elementId);
    if (element) {
      element.innerHTML = html || '';
    }
  }

  function changeLt(element, drugId) {
    if (confirm('<fmt:setBundle basename="oscarResources"/><fmt:message key="oscarRx.Prescription.changeDrugLongTermConfirm"/>') === true) {
      const data = "ltDrugId=" + encodeURIComponent(drugId) + "&isLongTerm=" + element.checked + "&rand=" + generateSecureRandomId();
      const url = ctx + "/oscarRx/WriteScript.do?parameterValue=updateLongTermStatus";
      new Ajax.Request(url, {
        method: 'post',
        parameters: data,
        onSuccess: function (transport) {
          try {
            const json = JSON.parse(transport.responseText);
            if (json != null && (json.success === 'true' || json.success === true)) {
              callReplacementWebService('ListDrugs.jsp', 'drugProfile');
            } else {
              checkboxRevertStatus(element);
            }
          } catch (e) {
            console.error('Failed to parse response');
            checkboxRevertStatus(element);
          }
        },
        onFailure: function (transport) {
          console.error('Request failed with status: ' + (transport.status || 'unknown'));
          checkboxRevertStatus(element);
        }
      });
    } else {
      checkboxRevertStatus(element);
    }
  }

  function checkboxRevertStatus(checkbox) {
    setTimeout(function () {
      checkbox.checked = !checkbox.checked;
    }, CHECKBOX_REVERT_DELAY_MS);
  }

  function checkReRxLongTerm() {
    let url = window.location.href;
    let match = url.indexOf('ltm=true');
    if (match > -1) {
      RePrescribeLongTerm();
    }
  }

  function changeContainerHeight(ele) {
    let ss = $('searchString').value;
    ss = trim(ss);
    if (ss.length == 0)
      $('autocomplete_choices').setStyle({height: '0%'});
    else
      $('autocomplete_choices').setStyle({height: '100%'});
  }

  function addInstruction(content, randomId) {
    $('instructions_' + randomId).value = content;
    parseIntr($('instructions_' + randomId));
  }

  function addSpecialInstruction(content, randomId) {
    if ($('siAutoComplete_' + randomId).getStyle('display') == 'none') {
      Effect.BlindDown('siAutoComplete_' + randomId);
    } else {
    }
    $('siInput_' + randomId).value = content;
    $('siInput_' + randomId).setStyle({color: 'black'});
  }

  function hideMedHistory() {
    mb.hide();
  }

  let modalBox = function () {
    this.show = function (randomId, displaySRC, H) {
      if (!document.getElementById("xmaskframe")) {
        let divFram = document.createElement('iframe');
        divFram.setAttribute("id", "xmaskframe");
        divFram.setAttribute("name", "xmaskframe");
        divFram.setAttribute("allowtransparency", "false");
        document.body.appendChild(divFram);
        let divSty = document.getElementById("xmaskframe").style;
        divSty.position = "fixed";
        divSty.top = "0px";
        divSty.right = "0px";
        divSty.width = "390px"
        divSty.backgroundColor = "#F5F5F5";
        divSty.zIndex = "45";
      }
      this.waitifrm = document.getElementById("xmaskframe");

      this.waitifrm.setAttribute("src", displaySRC + ".jsp?randomId=" + randomId);
      this.waitifrm.style.display = "block";
      this.waitifrm.style.height = H;

      $("dragifm").appendChild(this.waitifrm);
      Effect.Appear('xmaskframe');
    };
    this.hide = function () {
      Effect.Fade('xmaskframe');

    };
  }
  let mb = new modalBox();
  window.mb = mb; // exposed for iframe close buttons (parent.mb.hide())

  function displayMedHistory(randomId) {
    let data = "randomId=" + randomId;
    new Ajax.Request(ctx + "/oscarRx/WriteScript.do?parameterValue=listPreviousInstructions",
      {
        method: 'post', parameters: data, asynchronous: false, onSuccess: function (transport) {
          mb.show(randomId, ctx + '/oscarRx/displayMedHistory', '200px');
        }
      });
  }

  function displayInstructions(randomId) {
    let data = "randomId=" + randomId;
    mb.show(randomId, '<%= request.getContextPath() %>/oscarRx/displayInstructions', '600px');

  }

  function updateProperty(elementId) {
    let randomId = elementId.split("_")[1];
    if (randomId != null) {
      let url = ctx + "/oscarRx/WriteScript.do?parameterValue=updateProperty";
      let data = "";
      if (elementId.match("prnVal_") != null)
        data = "elementId=" + elementId + "&propertyValue=" + encodeURIComponent($(elementId).value);
      else if (elementId.match("repeats_") != null)
        data = "elementId=" + elementId + "&propertyValue=" + encodeURIComponent($(elementId).value);
      else
        data = "elementId=" + elementId + "&propertyValue=" + encodeURIComponent($(elementId).innerHTML);
      data = data + "&rand=" + Math.floor(Math.random() * 10001);
      new Ajax.Request(url, {method: 'post', parameters: data});
    }
  }

  function lookNonEdittable(elementId) {
    $(elementId).className = '';
  }

  function lookEdittable(elementId) {
    $(elementId).className = 'highlight';
  }

  function setPrn(randomId) {
    let prnStr = $('prn_' + randomId).innerHTML;
    prnStr = prnStr.strip();
    let prnStyle = $('prn_' + randomId).getStyle('textDecoration');
    if (prnStr == 'prn' || prnStr == 'PRN' || prnStr == 'Prn') {
      if (prnStyle.match("line-through") != null) {
        $('prn_' + randomId).setStyle({textDecoration: 'none'});
        $('prnVal_' + randomId).value = true;
      } else {
        $('prn_' + randomId).setStyle({textDecoration: 'line-through'});
        $('prnVal_' + randomId).value = false;
      }
    }
  }

  function focusTo(elementId) {
    $(elementId).contentEditable = 'true';
    $(elementId).focus();
    //IE 6/7 bug..will this call onfocus twice?? may need to do browser check.
    document.getElementById(elementId).onfocus();
  }

  function updateSpecialInstruction(elementId) {
    let randomId = elementId.split("_")[1];
    let url = ctx + "/oscarRx/WriteScript.do?parameterValue=updateSpecialInstruction";
    let data = "randomId=" + randomId + "&specialInstruction=" + encodeURIComponent($(elementId).value);
    data = data + "&rand=" + Math.floor(Math.random() * 10001);
    new Ajax.Request(url, {method: 'post', parameters: data});
  }

  function changeText(elementId) {
    if ($(elementId).value == 'Enter Special Instruction') {
      $(elementId).value = "";
      $(elementId).setStyle({color: 'black'});
    } else if ($(elementId).value == '') {
      $(elementId).value = 'Enter Special Instruction';
      $(elementId).setStyle({color: 'gray'});
    }

  }

  function updateMoreLess(elementId) {
    if ($(elementId).innerHTML == 'more')
      $(elementId).innerHTML = 'less';
    else
      $(elementId).innerHTML = 'more';
  }

  function changeDrugName(randomId, origDrugName) {
    if (confirm('If you change the drug name and write your own drug, you will lose the following functionality:'
      + '\n  *  Known Dosage Forms / Routes'
      + '\n  *  Drug Allergy Information'
      + '\n  *  Drug-Drug Interaction Information'
      + '\n  *  Drug Information'
      + '\n\nAre you sure you wish to use this feature?')) {

      //call another function to bring up prescribe.jsp
      let url = ctx + "/oscarRx/WriteScript.do?parameterValue=normalDrugSetCustom";
      let customDrugName = $("drugName_" + randomId).getValue();
      let data = "randomId=" + randomId + "&customDrugName=" + encodeURIComponent(customDrugName);
      new Ajax.Updater('rxText', url, {
        method: 'get',
        parameters: data,
        asynchronous: true,
        insertion: Insertion.Bottom,
        onSuccess: function (transport) {
          $('set_' + randomId).remove();
          renderRxStage();
        }
      });
    } else {
      $("drugName_" + randomId).value = origDrugName;
    }
  }

  function resetStash() {
    let url = ctx + "/oscarRx/deleteRx.do?parameterValue=clearStash";
    let data = "rand=" + Math.floor(Math.random() * 10001);
    new Ajax.Request(url, {
      method: 'post', parameters: data,
      requestHeaders: {'Accept': 'application/json'},
      onSuccess: function (transport) {
        // updateCurrentInteractions();
      }
    });
    $('rxText').innerHTML = "";//make pending prescriptions disappear.
    renderRxStage();
    $("searchString").focus();
  }

  /*
			 * Re-iterates the medication stack on postback and load through a session rxSessionBean
			 * and action class. A portion of this code also persists parts of the medication in a local stack.
			 */
  function iterateStash() {
    let url = ctx + "/oscarRx/WriteScript.do";
    let data = "parameterValue=iterateStash&rand=" + generateSecureRandomId();
    new Ajax.Updater('rxText', url, {
      method: 'POST', parameters: data, asynchronous: true,
      requestHeaders: {'Accept': 'application/json'},
      evalScripts: true,
      insertion: Insertion.Bottom,
      onSuccess: function (data) {
        // Detect postback or page load
        if (data.responseText) {
          renderRxStage();
        }
      },
      onFailure: function (transport) {
        console.error('Failed to iterate stash with status: ' + (transport.status || 'unknown'));
      }
    });
  }

  function reprint2(scriptNo) {
    let data = "scriptNo=" + scriptNo + "&rand=" + Math.floor(Math.random() * 10001);
    let url = ctx + "/oscarRx/rePrescribe2.do?method=reprint2";
    new Ajax.Request(url,
      {
        method: 'post', postBody: data,
        onSuccess: function (transport) {
          popForm2(scriptNo);

        }
      });
    return false;
  }


  function deletePrescribe(randomId) {
    let url = ctx + "/oscarRx/rxStashDelete.do";
    let headers = {'Content-Type': 'application/x-www-form-urlencoded', 'X-Requested-With': 'XMLHttpRequest'};
    headers['<csrf:tokenname/>'] = document.querySelector('#drugForm input[name="<csrf:tokenname/>"]').value;

    fetch(url, {
      method: 'POST',
      headers: headers,
      body: new URLSearchParams({parameterValue: 'deletePrescribe', randomId: randomId})
    }).then(function (response) {
      if (!response.ok) {
        console.error('Failed to remove drug from stash (HTTP ' + response.status + ')');
      }
      document.getElementById("set_" + randomId)?.remove();
      document.getElementById("prescriptionMoreLessLink_" + randomId)?.remove();
      document.getElementById("deleteMedicationFromPrescription_" + randomId)?.remove();
    }).catch(function (error) {
      console.error('deletePrescribe error:', error);
    });
  }


  skipParseInstr = false;

  function useFav2(favoriteId) {
    let randomId = Math.round(Math.random() * 1000000);
    let data = "favoriteId=" + favoriteId + "&randomId=" + randomId;
    let url = ctx + "/oscarRx/useFavorite.do?parameterValue=useFav2";
    new Ajax.Updater('rxText', url, {
      method: 'get', parameters: data, asynchronous: true, evalScripts: true, insertion: Insertion.Bottom,
      onSuccess: function (transport) {
        skipParseInstr = true;
        renderRxStage();
      }
    });
  }

  function calculateRxData(randomId) {
    if (skipParseInstr) {
      return false;
    }
    let dummie = parseIntr($('instructions_' + randomId));
    if (dummie)
      updateQty($('quantity_' + randomId));
  }

  function Delete2(element) {

    if (confirm('Are you sure you wish to delete the selected prescriptions?')) {
      let id_str = (element.id).split("_");
      let id = id_str[1];

      let rxDate = "rxDate_" + id;
      let reRx = "reRx_" + id;
      let del = "del_" + id;
      let discont = "discont_" + id;
      let prescrip = "prescrip_" + id;

      let url = ctx + "/oscarRx/deleteRx.do?parameterValue=Delete2";
      let data = "deleteRxId=" + element.id + "&rand=" + Math.floor(Math.random() * 10001);
      new Ajax.Request(url, {
        method: 'post', postBody: data, onSuccess: function (transport) {
          $(rxDate).style.textDecoration = 'line-through';
          $(reRx).style.textDecoration = 'line-through';
          $(del).style.textDecoration = 'line-through';
          $(discont).style.textDecoration = 'line-through';
          $(prescrip).style.textDecoration = 'line-through';
          // updateCurrentInteractions();
          location.reload();

        }
      });
    }
    return false;
  }

  function checkAllergy(id, atcCode) {
    const url = ctx + "/oscarRx/showAllergy.do"
    const data = "method=allergyData&atcCode=" + encodeURIComponent(atcCode) + "&id=" + encodeURIComponent(id) + "&rand=" + generateSecureRandomId();
    new Ajax.Request(url, {
      method: 'post', postBody: data,
      requestHeaders: {'Accept': 'application/json'},
      onSuccess: function (transport) {
        try {
          let json = JSON.parse(transport.responseText);
          if (json != null && json.results && json.results.length > 0) {
            // Pick the first allergy warning found
            let allergy = json.results[0];
            let element = document.getElementById('alleg_' + json.id);
            if (element) {
              // Create structured elements instead of HTML string
              element.innerHTML = '';
              let allergyLabel = document.createElement('label');
              allergyLabel.style.color = 'red';
              allergyLabel.textContent = ' Allergy: ';
              let allergyText = document.createTextNode(allergy.DESCRIPTION || 'Unknown');
              let reactionLabel = document.createElement('label');
              reactionLabel.style.color = 'red';
              reactionLabel.textContent = ' Reaction: ';
              let reactionText = document.createTextNode(allergy.reaction || 'Unknown');
              element.appendChild(allergyLabel);
              element.appendChild(allergyText);
              element.appendChild(reactionLabel);
              element.appendChild(reactionText);
              document.getElementById('alleg_tbl_' + json.id).style.display = 'block';
            }
          }
        } catch (e) {
          console.error('Failed to parse allergy data');
        }
      },
      onFailure: function (transport) {
        console.error('Allergy check failed with status: ' + (transport.status || 'unknown'));
      }
    });
  }

  function checkIfInactive(id, dinNumber) {
    let url = ctx + "/oscarRx/searchDrug.do";
    let data = "method=inactiveDate&din=" + dinNumber + "&id=" + id + "&rand=" + generateSecureRandomId();
    new Ajax.Request(url, {
      method: 'post', postBody: data,
      onSuccess: function (transport) {
        try {
          let json = JSON.parse(transport.responseText);
          if (json != null && json.vec && json.vec[0] && json.vec[0].time) {
            let dateStr = new Date(json.vec[0].time).toDateString();
            safeSetText('inactive_' + json.id, 'Inactive Drug Since: ' + dateStr);
          }
        } catch (e) {
          console.error('Failed to parse inactive drug data');
        }
      },
      onFailure: function (transport) {
        console.error('Inactive drug check failed with status: ' + (transport.status || 'unknown'));
      }
    });
  }


  function Discontinue(event, element) {
    let id_str = (element.id).split("_");
    let id = id_str[1];
    let widVal = ($('drugProfile').getWidth() - 400);
    let widStr = widVal + 'px';
    let heightDrugProfile = $('discontinueUI').getHeight();
    let posx = 0, posy = 0;
    if (event.pageX || event.pageY) {
      posx = event.pageX;
      posx = posx - widVal;
      posy = event.pageY - heightDrugProfile / 2;
      posx = posx + 'px';
      posy = posy + 'px';
    } else if (event.clientX || event.clientY) {
      posx = event.clientX + document.body.scrollLeft
        + document.documentElement.scrollLeft;
      posx = posx - widVal;
      posy = event.clientY + document.body.scrollTop
        + document.documentElement.scrollTop - heightDrugProfile / 2;
      posx = posx + 'px';
      posy = posy + 'px';
    } else {
      let xy = Position.page($('drugProfile'));
      posx = (xy[0] + 200) + 'px';
      if (xy[1] >= 0)
        posy = xy[1] + 'px';
      else
        posy = 0 + 'px';
    }
    let styleStr = {left: posx, top: posy, width: widStr};

    let prescripElement = $('prescrip_' + id);
    let drugName = prescripElement ? prescripElement.textContent : '';
    $('discontinueUI').setStyle(styleStr);
    safeSetText('disDrug', drugName);
    document.getElementById('discontinueUI').hidden = false;
    $('disDrugId').value = id;
  }

  function Discontinue2(id, reason, comment, drugSpecial) {
    let url = ctx + "/oscarRx/deleteRx.do?parameterValue=Discontinue";
    let demoNo = '<%=Encode.forJavaScript(String.valueOf(demoNo))%>';
    let data = "drugId=" + encodeURIComponent(id) + "&reason=" + encodeURIComponent(reason) + "&comment=" + encodeURIComponent(comment) + "&demoNo=" + demoNo + "&drugSpecial=" + encodeURIComponent(drugSpecial) + "&rand=" + generateSecureRandomId();
    new Ajax.Request(url, {
      method: 'post', postBody: data,
      onSuccess: function (transport) {
        try {
          let json = JSON.parse(transport.responseText);
          document.getElementById('discontinueUI').hidden = true;
          $('rxDate_' + json.id).style.textDecoration = 'line-through';
          $('reRx_' + json.id).style.textDecoration = 'line-through';
          $('del_' + json.id).style.textDecoration = 'line-through';
          safeSetText('discont_' + json.id, json.reason || '');
          $('prescrip_' + json.id).style.textDecoration = 'line-through';
        } catch (e) {
          console.error('Failed to parse discontinue response');
        }
      },
      onFailure: function (transport) {
        console.error('Discontinue request failed with status: ' + (transport.status || 'unknown'));
        document.getElementById('discontinueUI').hidden = true;
      }
    });
  }

  /*
			 * @Deprecated avoid future use of prototype.
	 */
  function updateCurrentInteractions() {
    new Ajax.Request(ctx + "/oscarRx/GetmyDrugrefInfo.do?method=findInteractingDrugList&rand=" + Math.floor(Math.random() * 10001), {
      method: 'get', onSuccess: function (transport) {
        new Ajax.Request(ctx + "/oscarRx/UpdateInteractingDrugs.jsp?rand=" + Math.floor(Math.random() * 10001), {
          method: 'get', onSuccess: function (transport) {
            let str = transport.responseText;
            str = str.replace('<script type="text/javascript">', '');
            str = str.replace(/<\/script>/, '');
            eval(str);
                 }
        });
      }
    });
  }

  //represcribe long term meds
  function RePrescribeLongTerm() {
    let demoNo = '<%=Encode.forJavaScript(String.valueOf(demoNo))%>';
    let data = "demoNo=" + demoNo + "&showall=<%=Encode.forUriComponent(String.valueOf(showall))%>&rand=" + Math.floor(Math.random() * 10001);
    let url = ctx + "/oscarRx/rePrescribe2.do?method=repcbAllLongTerm";
    new Ajax.Updater('rxText', url, {
      method: 'get',
      parameters: data,
      asynchronous: true,
      insertion: Insertion.Bottom,
      onSuccess: function (transport) {
        renderRxStage();
      }
    });
    return false;
  }

  function customNoteWarning() {
    if (confirm('This feature will allow you to manually enter a prescription.'
      + '\nWarning: you will lose the following functionality:'
      + '\n  *  Quantity and Repeats'
      + '\n  *  Known Dosage Forms / Routes'
      + '\n  *  Drug Allergy Information'
      + '\n  *  Drug-Drug Interaction Information'
      + '\n  *  Drug Information'
      + '\n\nAre you sure you wish to use this feature?')) {
      let randomId = Math.round(Math.random() * 1000000);
      let url = ctx + "/oscarRx/WriteScript.do?parameterValue=newCustomNote";
      let data = "randomId=" + randomId;
      new Ajax.Updater('rxText', url, {
        method: 'get',
        parameters: data,
        asynchronous: true,
        evalScripts: true,
        insertion: Insertion.Bottom
      });
      renderRxStage();
    }
  }

  function customWarning2() {
    if (confirm('This feature will allow you to manually enter a drug.'
      + '\nWarning: Only use this feature if absolutely necessary, as you will lose the following functionality:'
      + '\n  *  Known Dosage Forms / Routes'
      + '\n  *  Drug Allergy Information'
      + '\n  *  Drug-Drug Interaction Information'
      + '\n  *  Drug Information'
      + '\n\nAre you sure you wish to use this feature?')) {
      //call another function to bring up prescribe.jsp
      let randomId = Math.round(Math.random() * 1000000);
      let searchString = $("searchString").value;
      let url = ctx + "/oscarRx/WriteScript.do?parameterValue=newCustomDrug&name=" + encodeURIComponent(searchString);
      let data = "randomId=" + randomId;
      new Ajax.Updater('rxText', url, {
        method: 'get', parameters: data, asynchronous: true, evalScripts: true,
        insertion: Insertion.Bottom, onComplete: function (transport) {
          updateQty($('quantity_' + randomId));
          renderRxStage();
        }
      });

    }

  }

  function saveCustomName(element) {
    let elemId = element.id;
    let ar = elemId.split("_");
    let rand = ar[1];
    let url = ctx + "/oscarRx/WriteScript.do?parameterValue=saveCustomName";
    let data = "customName=" + encodeURIComponent(element.value) + "&randomId=" + rand;
    let instruction = "instructions_" + rand;
    let quantity = "quantity_" + rand;
    let repeat = "repeats_" + rand;
    new Ajax.Request(url, {
      method: 'get', parameters: data, onSuccess: function (transport) {

      }
    });
  }

  function popForm2(scriptId) {
      try {
        const modalElement = document.getElementById('rxPreviewBootstrapModal');
        const modalBodyElement = document.getElementById('rxPreviewBootstrapModalBody');
        let editRxButton = document.getElementById('rxPreviewBootstrapEditRxButton');

        modalBodyElement.innerHTML = ' <div class="d-flex justify-content-center"><div class="spinner-border" role="status"><span class="visually-hidden">Loading...</span></div></div>'

        let pharmacyData = JSON.parse(document.getElementById('Calcs').value || '{}');
        let pharmacyId = pharmacyData.id ? pharmacyData.id : null;
        let url = ctx + '/oscarRx/printPreview.do?method=printPreview&scriptId=' + scriptId + '&pharmacyId=' + pharmacyId;
        fetch(url)
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not OK');
            }
            return response.text();
          })
          .then(html => {
            modalBodyElement.innerHTML = html;

            const modalBodySignaturePadElements = modalBodyElement.getElementsByClassName('signatureClass');
            if (modalBodySignaturePadElements && modalBodySignaturePadElements.length > 0) {
              // Initialize the signature pad if the showSignatureBlock is true
              setTimeout(function () {
                oscarSignaturePad.initializeSignaturePad({
                  onSignaturePadEvent(eventType, data) {
                    if (eventType === SignatureEventType.SAVE) {
                      signatureHandler(data, '${ctx}', scriptId);
                    }
                  }
                });
              }, SIGNATURE_PAD_INIT_DELAY_MS);
            }
          })
          .catch(error => {
            modalBodyElement.innerHTML = '<p>Error loading print preview content.</p>';
          });

        // Set up Edit Rx button
        editRxButton.innerHTML = '<fmt:message key="oscarRx.Preview.EditRx"/>';
        const newEditRxButton = editRxButton.cloneNode(true);
        editRxButton.parentNode.replaceChild(newEditRxButton, editRxButton);
        editRxButton = newEditRxButton;

        editRxButton.onclick = function () {
          const modalInstance = bootstrap.Modal.getInstance(modalElement);
          if (modalInstance) {
            modalInstance.hide();
          }
        };

        let bsModal = bootstrap.Modal.getInstance(modalElement);
        if (!bsModal) {
          bsModal = new bootstrap.Modal(modalElement);
        }
        bsModal.show();

        modalElement.addEventListener('hidden.bs.modal', function () {
          modalBodyElement.innerHTML = '';
        }, {once: true});

      } catch (er) {
        console.error(er);
      }
  }

  function callTreatments(textId, id) {
      let ele = $(textId);
      let url = ctx + "/oscarRx/TreatmentMyD.jsp"
      let ran_number = generateSecureRandomId();
      let params = "demographicNo=<%=Encode.forUriComponent(String.valueOf(demoNo))%>&cond=" + encodeURIComponent(ele.value) + "&rand=" + ran_number;
      new Ajax.Updater(id, url, {
        method: 'get',
        parameters: params,
        asynchronous: true,
        onFailure: function (transport) {
          console.error('Treatments call failed with status: ' + (transport.status || 'unknown'));
        }
      });
      $('treatmentsMyD').toggle();
  }

  //  Drug profile legend view-switching
  //
  // Clicking a legend button (Current, All, Active, Long Term / Acute, etc.)
  // loads ListDrugs.jsp into #drugProfile. Some views consist of a single table
  // (callReplacementWebService), while multi-section views like "Long Term / Acute"
  // fire one replacement call followed by one or more addition calls that each
  // append a labelled section below the first.
  //
  // Problem: both calls are fired at the same time. The addition call is a GET and
  // often resolves first, appending its section to #drugProfile. The replacement
  // call then arrives and calls .html(), which wipes #drugProfile — removing the
  // already-appended section. To prevent this, addition calls are queued until the
  // replacement response has been written to the DOM.
  //
  // drugProfileAdditionQueue is keyed by target element id (e.g. 'drugProfile').
  // It holds an array of pending addition functions while a replacement is in
  // flight, and is set back to null once the replacement is complete.
  let drugProfileAdditionQueue = {};

  // Public entry point for addition calls (appends a section to an existing view).
  // If a replacement is still in flight for the same target, the call is queued
  // and will be executed automatically once the replacement finishes.
  function callAdditionWebService(url, id) {
      let contextPath = ctx;
      if (url.indexOf(contextPath) !== 0) {
        url = contextPath + "/oscarRx/" + url;
      }
      if (drugProfileAdditionQueue[id]) {
        let capturedUrl = url;
        drugProfileAdditionQueue[id].push(function () { doAdditionWebService(capturedUrl, id); });
        return;
      }
      doAdditionWebService(url, id);
  }

  // Fetches a ListDrugs.jsp section and appends it to the target container.
  // Each response contains an <h4> heading and a <table> with its own DataTable
  // init script. jQuery.append() evaluates those inline scripts automatically,
  // so each appended section initialises its own independent DataTable instance.
  function doAdditionWebService(url, id) {
      let ran_number = generateSecureRandomId();
      jQuery.get(url, { demographicNo: '<%=Encode.forJavaScript(String.valueOf(demoNo))%>', rand: ran_number })
        .done(function (responseText) {
          jQuery('#' + id).append(responseText);
        })
        .fail(function (xhr) {
          console.error('Addition web service call failed with status: ' + (xhr.status || 'unknown'));
        });
  }

  // Replaces the entire content of the target container with a fresh ListDrugs.jsp
  // response. Opens drugProfileAdditionQueue before firing so that any concurrent
  // addition calls wait for this response to land before appending their sections.
  function callReplacementWebService(url, id) {
      let contextPath = ctx;
      if (url.indexOf(contextPath) !== 0) {
        url = contextPath + "/oscarRx/" + url;
      }
      drugProfileAdditionQueue[id] = [];
      let ran_number = generateSecureRandomId();
      jQuery.post(url, { demographicNo: '<%=Encode.forJavaScript(String.valueOf(demoNo))%>', rand: ran_number })
        .done(function (responseText) {
          
          // .html() replaces the container content and evaluates inline scripts,
          // which triggers the DataTable init for the first section's table.
          jQuery('#' + id).html(responseText);

          // Re-cache whenever the drug list is refreshed (initial load, re-rx, changeLt).
          // After caching, re-apply the active legend view so the display is consistent:
          //   - Initial load: activeViewMode='current' → shows only non-archived rows even
          //     though the server now always returns all drugs including archived.
          //   - changeLt / save-Rx: re-applies whatever view the user had selected.
          if (id === 'drugProfile') {
              // DataTables pagination removes off-page rows from the live DOM.
              // jQuery('#Drug_table tbody tr') would therefore only return the
              // current page's rows (default: 10), silently truncating the cache.
              // fnGetNodes() returns ALL row nodes from DataTables' internal store,
              // including detached off-page ones, giving us the full dataset.
              const dtInstance = jQuery('#Drug_table').dataTable();
              allDrugRows = jQuery(dtInstance.fnGetNodes()).clone(true, true);
              drugTableHeadHtml = jQuery('#Drug_table thead').html();
              drugTableClasses = jQuery('#Drug_table').attr('class');
              if (activeSectionDefs) {
                  showSections(activeSectionDefs);
              } else {
                  filterDrugView(activeViewMode);
              }
          }

          // Drain the queue — any addition calls that arrived while the replacement
          // was in flight now execute in order.
          let queue = drugProfileAdditionQueue[id] || [];
          drugProfileAdditionQueue[id] = null;
          queue.forEach(function (fn) { fn(); });
        })
        .fail(function (xhr) {
          drugProfileAdditionQueue[id] = null;
          console.error('Replacement web service call failed with status: ' + (xhr.status || 'unknown'));
        });
  }

  // ---------------------------------------------------------------------------
  //  Client-side drug profile filtering
  //
  //  On every drug-list load (page open, re-rx, changeLt), all rows are fetched
  //  once and stored in allDrugRows. Legend buttons then filter that cache
  //  client-side — no extra server round-trips required.
  //
  //  activeViewMode / activeSectionDefs track which legend view is currently
  //  active so that non-legend refreshes (changeLt, save-Rx) can re-apply the
  //  same view automatically after the new data is cached.
  // ---------------------------------------------------------------------------

  // Row cache — populated after every drug-list load via callReplacementWebService.
  let allDrugRows = null;       // cloned jQuery set of all <tr> from the full server response
  let drugTableHeadHtml = null; // cached <thead> innerHTML, reused when building each section table
  let drugTableClasses = null;  // class string copied from #Drug_table to each rebuilt table

  // Exactly one of these is non-null at any time, depending on which legend button is active.
  let activeViewMode = 'current'; // argument for filterDrugView  (single-table views)
  let activeSectionDefs = null;   // argument for showSections     (multi-section views)

  // Row predicate helpers — centralise the data-* attribute checks so filter
  // functions below don't repeat the same string comparisons everywhere.
  const rowIsArchived = r => r.dataset.isArchived === 'true';
  const rowIsLongTerm = r => r.dataset.isLongterm === 'true';
  const rowIsCurrent = r => r.dataset.isCurrent === 'true';
  const rowIsExternal = r => r.dataset.isExternal === 'true';

  // Tear down every DataTable inside #drugProfile, wipe the container, then
  // render each section: an optional heading, a cloned table, and a new DataTable.
  function rebuildDrugProfile(sections) {
      jQuery('#drugProfile table').each(function () {
          if (jQuery.fn.dataTable && jQuery.fn.dataTable.isDataTable(this)) {
              jQuery(this).dataTable().fnDestroy();
          }
      });
      jQuery('#drugProfile').empty();

      sections.forEach(section => {
          if (section.label && section.rows.length === 0) { return; }

          const tableId = section.label
              ? 'Drug_table' + section.label.replace(/\s+/g, '')
              : 'Drug_table';

          if (section.label) {
              jQuery('#drugProfile').append(
                  jQuery('<h4 style="margin-bottom:1px;margin-top:10px;font-size:16px;"></h4>').text(section.label)
              );
          }

          const $tbody = jQuery('<tbody></tbody>');
          section.rows.each(function () {
              $tbody.append(jQuery(this).clone(true, true));
          });

          jQuery('<table></table>')
              .attr({ id: tableId, class: drugTableClasses })
              .append(jQuery('<thead></thead>').html(drugTableHeadHtml), $tbody)
              .appendTo('#drugProfile')
              .dataTable(window.drugListTableConfig);
      });
  }

  // Show a single table containing only the rows that match the given mode.
  function filterDrugView(mode) {
      if (!allDrugRows) { return; }
      activeViewMode = mode;
      activeSectionDefs = null;
      const filtered = allDrugRows.filter(function () {
          switch (mode) {
              case 'current':  return !rowIsArchived(this);
              case 'all':      return true;
              case 'active':   return !rowIsArchived(this) && (rowIsLongTerm(this) || rowIsCurrent(this));
              case 'inactive': return !rowIsArchived(this) && !rowIsCurrent(this);
              default:         return true;
          }
      });
      rebuildDrugProfile([{ label: null, rows: filtered }]);
  }

  // Show multiple labelled sections, each filtered by its own predicate.
  // Called by showLongTermAcuteView() and showFullProfileView() below.
  function showSections(sectionDefs) {
      if (!allDrugRows) { return; }
      activeSectionDefs = sectionDefs;
      activeViewMode = null;
      const sections = sectionDefs.map(def => ({
          label: def.label,
          rows: allDrugRows.filter(function () { return def.filterFn(this); })
      }));
      rebuildDrugProfile(sections);
  }

  // Named entry points for the two multi-section legend buttons — keeps onclick
  // attributes readable and avoids repeating filter logic inline in the HTML.
  function showLongTermAcuteView() {
      showSections([
          { label: 'Long Term Meds', filterFn: r => !rowIsArchived(r) && rowIsLongTerm(r) },
          { label: 'Acute', filterFn: r => !rowIsArchived(r) && !rowIsLongTerm(r) }
      ]);
  }

  function showFullProfileView() {
      showSections([
          { label: 'Long Term Meds', filterFn: r => !rowIsArchived(r) && rowIsLongTerm(r) },
          { label: 'Acute', filterFn: r => !rowIsArchived(r) && !rowIsLongTerm(r) && rowIsCurrent(r) },
          { label: 'Inactive', filterFn: r => !rowIsArchived(r) && !rowIsLongTerm(r) && !rowIsCurrent(r) },
          { label: 'External', filterFn: r => !rowIsArchived(r) && rowIsExternal(r) }
      ]);
  }

  callReplacementWebService("ListDrugs.jsp?show=all", 'drugProfile');

  function searchResultsHandler(type, args) {
    let url = ctx + "/oscarRx/WriteScript.do";
    let ran_number = generateSecureRandomId();
      let params = "parameterValue=createNewRx"
        + "&demographicNo="
        + "${ demographicNo }"
        + "&drugId="
        + encodeURIComponent(args.id)
        + "&text="
        + encodeURIComponent(args.label)
        + "&randomId="
        + ran_number;

    new Ajax.Updater('rxText', url, {
      method: 'POST',
      parameters: params,
      evalScripts: true,
      requestHeaders: {'Accept': 'application/json'},
      insertion: Insertion.Top,
      onSuccess: function (transport) {
        renderRxStage();
      },
      onFailure: function (transport) {
        console.error('Search results handler failed with status: ' + (transport.status || 'unknown'));
      }
    });
  }

    function replaceAll(str, keyword) {
      let matcher;
      let lastkeyword;
      if (keyword !== lastkeyword) {
        matcher = new RegExp("(" + keyword + ")", "ig");
        lastkeyword = keyword;
      }
      return str.replace(matcher, "<span class='drugKeyword' >$1</span>");
    }

    jQuery(document).ready(function () {

      // block the enter key
      jQuery("#searchString").keypress(function (e) {
        let code = (e.keyCode ? e.keyCode : e.which);
        if (code === 13) {
          return false;
        }
      });

      let cache = {};
      jQuery("#searchString").autocomplete({
        source: function (request, response) {

          let term = request.term.toUpperCase(),
            element = this.element,
            cache = this.element.data(document.body, 'autocompleteCache') || {},
            foundInCache = false;

          jQuery.each(cache, function (key, data) {
            if (term.indexOf(key) === 0 && data.length > 0) {
              response(jQuery.map(cache.results, function (item) {
                return {
                  label: item.name,
                  value: item.description,
                  id: item.id,
                  active: item.isInactive,
                  keyword: request.term
                };
              }))
              foundInCache = true;
            }
          });

          if (foundInCache) {
            return;
          }

          let param = jQuery('#drugCategorySet').serialize()
            + "&"
            + jQuery('#searchParamSet').serialize()
            + "&query="
            + request.term.toUpperCase();
          jQuery.ajax({
            url: "${ctx}/oscarRx/searchDrug.do",
            type: 'POST',
            data: param,
            dataType: "json",
            success: function (data) {
              cache[term] = data;
              element.data(document.body, 'autocompleteCache', cache);
              response(jQuery.map(data.results, function (item) {
                return {
                  label: item.name,
                  value: item.description,
                  id: item.id,
                  active: item.isInactive,
                  keyword: request.term
                };
              }))
            },
            error: function (xhr, status, error) {
              console.error('Drug search failed: ' + (status || 'unknown'));
            }
          })
        },
        delay: AUTOCOMPLETE_DELAY_MS,
        minLength: AUTOCOMPLETE_MIN_LENGTH,
        search: function (event, ui) {
          jQuery(ui).empty();
        },
        focus: function (event, ui) {
          event.preventDefault();
        },
        select: function (event, ui) {
          event.preventDefault();
          searchResultsHandler(null, ui.item);
          jQuery('#searchString').val("");
        },
        open: function () {
          jQuery(this).removeClass("ui-corner-all").addClass("ui-corner-top");
          jQuery(this).autocomplete("widget").css({"width": (jQuery(this).width() + "px")});
          jQuery(".ui-autocomplete").css("z-index", 1000);
        },
        close: function () {
          jQuery(this).removeClass("ui-corner-top").addClass("ui-corner-all");
        }

      }).data("ui-autocomplete")._renderItem = function (ul, item) {
        let inactivedrug = item.active ? " inactiveDrug" : "";
        return jQuery("<li></li>")
          .data("item.autocomplete", item)
          .append("<a href='#' id='" + item.id + "'"
            + " class='drugitem"
            + inactivedrug
            + "' >"
            + replaceAll(item.label, jQuery.ui.autocomplete.escapeRegex(item.keyword))
            + "</a>")
          .appendTo(ul);
      };

    })

    function addFav(randomId, brandName) {
      let favoriteName = window.prompt('Please enter a name for the Favorite:', brandName);
      if (favoriteName == null) {
        return;
      }
      favoriteName = encodeURIComponent(favoriteName);
      if (favoriteName.length > 0) {
        let url = ctx + "/oscarRx/addFavorite2.do?parameterValue=addFav2";
        let data = "randomId=" + randomId + "&favoriteName=" + favoriteName;
        new Ajax.Request(url, {
          method: 'get', parameters: data, onSuccess: function (transport) {
            window.location.href = ctx + "/oscarRx/SearchDrug3.jsp";
          }
        })
      }
    }

    let resHidden2 = 0;

    function showHiddenRes() {
      let list = $$('div.hiddenResource');
      if (resHidden2 == 0) {
        list.invoke('show');
        resHidden2 = 1;
        $('showHiddenResWord').update('hide');
        let url = ctx + "/oscarRx/updateHiddenResources.jsp";
        let params = "hiddenResources=&rand=" + Math.floor(Math.random() * 10001);
        new Ajax.Request(url, {method: 'post', parameters: params});
      } else {
        $('showHiddenResWord').update('show');
        list.invoke('hide');
        resHidden2 = 0;
      }
    }

    let showOrHide = 0;

    function showOrHideRes(hiddenRes) {
      hiddenRes = hiddenRes.replace(/\{/g, "");
      hiddenRes = hiddenRes.replace(/\}/g, "");
      hiddenRes = hiddenRes.replace(/\s/g, "");
      let arr = hiddenRes.split(",");
      let numberOfHiddenResources = 0;
      if (showOrHide == 0) {
        numberOfHiddenResources = 0;
        for (let i = 0; i < arr.length; i++) {
          let element = arr[i];
          element = element.replace("mydrugref", "");
          let elementArr = element.split("=");
          let resId = elementArr[0];
          let resUpdated = elementArr[1];
          let id = resId + "." + resUpdated;
          $(id).show();
          $('show_' + id).hide();
          $('showHideWord').update('hide');

          showOrHide = 1;
          numberOfHiddenResources++;
        }
      } else {
        numberOfHiddenResources = 0
        for (let i = 0; i < arr.length; i++) {
          let element = arr[i];
          element = element.replace("mydrugref", "");
          let elementArr = element.split("=");
          let resId = elementArr[0];
          let resUpdated = elementArr[1];
          let id = resId + "." + resUpdated;
          oscarLog("id=" + id);
          $(id).hide();
          $('show_' + id).show();
          $('showHideWord').update('show');
          showOrHide = 0;
          numberOfHiddenResources++;
        }
      }
      $('showHideNumber').update(numberOfHiddenResources);

    }

    let addTextView = 0;

    function showAddText(randId) {
      let addTextId = "addText_" + randId;
      let addTextWordId = "addTextWord_" + randId;
      if (addTextView == 0) {
        $(addTextId).show();
        addTextView = 1;
        $(addTextWordId).update("less")
      } else {
        $(addTextId).hide();
        addTextView = 0;
        $(addTextWordId).update("more")
      }
    }

    function ShowW(id, resourceId, updated) {

      let params = "resId=" + resourceId + "&updatedat=" + updated
      let url = ctx + '/oscarRx/GetmyDrugrefInfo.do?method=setWarningToShow&rand=' + Math.floor(Math.random() * 10001);
      new Ajax.Updater('showHideTotal', url, {
        method: 'get',
        parameters: params,
        asynchronous: true,
        evalScripts: true,
        onSuccess: function (transport) {

          $(id).show();
          $('show_' + id).hide();

        }
      });
    }

    function HideW(id, resourceId, updated) {
      let url = ctx + '/oscarRx/GetmyDrugrefInfo.do?method=setWarningToHide';
      let ran_number = Math.round(Math.random() * 1000000);
      let params = "resId=" + resourceId + "&updatedat=" + updated + "&rand=" + ran_number;  //hack to get around ie caching the page
      //totalHiddenResources++;
      new Ajax.Updater('showHideTotal', url, {
        method: 'get',
        parameters: params,
        asynchronous: true,
        evalScripts: true,
        onSuccess: function (transport) {

          $(id).hide();
          $("show_" + id).show();

        }
      });
    }


    function setSearchedDrug(drugId, name) {

      let url = ctx + "/oscarRx/WriteScript.do";
      let ran_number = Math.round(Math.random() * 1000000);
      let params = "parameterValue=createNewRx"
        + "&demographicNo=" + <%=Encode.forJavaScript(String.valueOf(demoNo))%>
        +"&drugId=" + encodeURIComponent(drugId)
        + "&text=" + encodeURIComponent(name)
        + "&randomId="
        + ran_number;
      new Ajax.Updater('rxText', url, {
        method: 'POST',
        parameters: params,
        asynchronous: true,
        evalScripts: true,
        requestHeaders: {'Accept': 'application/json'},
        insertion: Insertion.Bottom,
        onSuccess: function (transport) {
          renderRxStage();
        }
      });

      $('searchString').value = "";
    }

    let counterRx = 0;

    function updateReRxDrugId(elementId) {
      let ar = elementId.split("_");
      let drugId = ar[1];
      if (drugId != null && $(elementId).checked == true) {
        let data = "reRxDrugId=" + encodeURIComponent(drugId) + "&action=addToReRxDrugIdList&rand=" + Math.floor(Math.random() * 10001);
        let url = ctx + "/oscarRx/WriteScript.do?parameterValue=updateReRxDrug";
        new Ajax.Request(url, {method: 'get', parameters: data});
      } else if (drugId != null) {
        let data = "reRxDrugId=" + encodeURIComponent(drugId) + "&action=removeFromReRxDrugIdList&rand=" + Math.floor(Math.random() * 10001);
        let url = ctx + "/oscarRx/WriteScript.do?parameterValue=updateReRxDrug";
        new Ajax.Request(url, {method: 'get', parameters: data});
      }
    }


    function removeReRxDrugId(drugId) {
      if (drugId != null) {
        const data = "reRxDrugId=" + encodeURIComponent(drugId) + "&action=removeFromReRxDrugIdList&rand=" + Math.floor(Math.random() * 10001);
        const url = ctx + "/oscarRx/WriteScript.do?parameterValue=updateReRxDrug";
        new Ajax.Request(url, {method: 'get', parameters: data});
      }
    }

//represcribe a drug
    function represcribe(element, toArchive) {

      skipParseInstr = true;
      let elemId = element.id;
      let ar = elemId.split("_");
      let drugId = ar[1];
      if (drugId != null && $("reRxCheckBox_" + drugId).checked === true) {

        let url = ctx + "/oscarRx/rePrescribe2.do?method=represcribeMultiple&rand=" + Math.floor(Math.random() * 10001);
        new Ajax.Updater('rxText', url, {
          method: 'get', parameters: data, asynchronous: false, evalScripts: true,
          insertion: Insertion.Bottom, onSuccess: function (transport) {
            renderRxStage();
          }
        });
      } else if (drugId != null) {
        let dataUpdateId = "reRxDrugId=" + encodeURIComponent(toArchive) + "&action=addToReRxDrugIdList&rand=" + Math.floor(Math.random() * 10001);
        let urlUpdateId = ctx + "/oscarRx/WriteScript.do?parameterValue=updateReRxDrug";
        new Ajax.Request(urlUpdateId, {method: 'get', parameters: dataUpdateId});

        let data = "drugId=" + encodeURIComponent(drugId);
        let url = ctx + "/oscarRx/rePrescribe2.do?method=represcribe2&rand=" + Math.floor(Math.random() * 10001);
        new Ajax.Updater('rxText', url, {
          method: 'get', parameters: data, evalScripts: true,
          insertion: Insertion.Bottom, onSuccess: function (transport) {
            // updateCurrentInteractions();
          }
        });

      }
    }

    /**
     * Updates the re-prescribing status of a prescribed drug in the UI and session.
     *
     * @param element The checkbox element that triggered the update.
     * @param drugId The ID of the drug being updated.
     */
    function updateReRxStatusForPrescribedDrug(element, drugId) {
      const uiRefId = element.id.split('_')[1];
      if (drugId == null || uiRefId == null) {
        return;
      }

      if (element.checked === true) {
        this.addDrugToReRxList(uiRefId, drugId);
        selectedReRxIDs.push(drugId);
      } else {
        this.removeDrugFromReRxList(drugId);
        selectedReRxIDs = selectedReRxIDs.filter(id => id !== drugId);
      }
      this.updateReRxStageConfirmBoxVisibility();
    }

    function updateReRxStageConfirmBoxVisibility() {
      const count = selectedReRxIDs.length;
      document.getElementById("selectedCount").innerText = count;

      const confirmBox = document.getElementById("reRxConfirmBox");
      if (count > 0) {
        confirmBox.classList.add("show");
      } else {
        confirmBox.classList.remove("show");
      }
    }

    function cancelAndClearSelection() {
      selectedReRxIDs.forEach(drugId => uncheckReRxForExistingPrescribedDrug(drugId));
      selectedReRxIDs = [];
      this.updateReRxStageConfirmBoxVisibility();
    }

    function stageSelectedReRxMedications() {
      this.rePrescribeMulti();
      selectedReRxIDs = [];
      this.updateReRxStageConfirmBoxVisibility();
    }

    /**
     * Sets off instruction parsing and adds a drug to the re-prescribe list in the UI and session.
     *
     * @param uiRefId The unique ID used in the UI to reference this drug.
     * @param drugId The ID of the drug to add.
     */
    function addDrugToReRxList(uiRefId, drugId) {
      skipParseInstr = true;

      this.addDrugToReRxListInSession(uiRefId, drugId);
    }

    /**
     * Add ReRx drug to UI by making an AJAX request to update the 'rxText' element.
     *
     * @param uiRefId The unique ID used in the UI to reference this drug.
     * @param drugId The ID of the drug to re-prescribe.
     */
    function rePrescribe2(uiRefId, drugId) {
      const data = "drugId=" + encodeURIComponent(drugId);
      const url = ctx + "/oscarRx/rePrescribe2.do?method=represcribe2&rand=" + uiRefId;
      new Ajax.Updater('rxText', url, {
        method: 'get', parameters: data, evalScripts: true,
        insertion: Insertion.Bottom, onSuccess: function (transport) {
          renderRxStage();
        }
      });
    }

    function rePrescribeMulti() {
      const url = ctx + "/oscarRx/rePrescribe2.do?method=represcribeMultiple&rand=" + Math.floor(Math.random() * 10001);
      new Ajax.Updater('rxText', url, {
        method: 'get', asynchronous: false, evalScripts: true,
        insertion: Insertion.Bottom, onSuccess: function (transport) {
          renderRxStage();
        }
      });
    }

    /**
     * Adds a drug to the re-prescribe list in the session.
     *
     * @param uiRefId The unique ID used in the UI to reference this drug.
     * @param drugId The ID of the drug to add.
     */
    function addDrugToReRxListInSession(uiRefId, drugId) {
      const dataUpdateId = "reRxDrugId=" + encodeURIComponent(drugId) + "&action=addToReRxDrugIdList&rand=" + uiRefId;
      const urlUpdateId = ctx + "/oscarRx/WriteScript.do?parameterValue=updateReRxDrug";
      new Ajax.Request(urlUpdateId, {method: 'get', parameters: dataUpdateId});
    }

    /**
     * Removes a drug from the re-prescribe list and updates the UI.
     *
     * @param drugId The ID of the drug to remove.
     */
    function removeDrugFromReRxList(drugId) {
      const card = document.querySelector('#rxText fieldset[data-drug-ref-id="' + drugId + '"]');
      this.removeElementFromUI(card);
      this.removeReRxDrugId(drugId);
    }

    /**
     * Removes a prescribing drug entry from both the UI and the backend.
     * @param cardId The id of the card from which to delete
     * @param drugId The id of the drug to remove
     */
    function removePrescribingDrug(cardId, drugId) {
      const uiRefId = cardId.id.split('_')[1];
      this.deletePrescribingDrugFromUI(uiRefId);
      this.uncheckReRxForExistingPrescribedDrug(drugId)
    }

    /**
     * Deletes a prescribing drug from UI and calls deletePrescribe.
     * @param uiRefId The unique id for referencing the UI element.
     */
    function deletePrescribingDrugFromUI(uiRefId) {
      this.removeElementFromUI(this.getPrescribingDrugCardByUiRefId(uiRefId));
      this.deletePrescribe(uiRefId);
    }

    /**
     * Removes a DOM element from the UI.
     * @param {HTMLElement} element The element to remove.
     */
    function removeElementFromUI(element) {
      if (element)
        element.remove();
    }

    /**
     * Unchecks the "re-prescribe" checkbox for an existing prescribed drug and removes its ID from the re-prescribe list.
     * @param drugId The database ID of the prescribed drug.
     */
    function uncheckReRxForExistingPrescribedDrug(drugId) {
      const checkbox = this.getReRxCheckboxByUiRefId(drugId);
      if (checkbox)
        checkbox.checked = false;
      this.removeReRxDrugId(drugId);
    }

    /**
     * Gets the prescribing/staged drug container element by its UI reference ID.
     * @param uiRefId The UI reference ID.
     * @returns {HTMLElement|null} The drug container element, or null if not found.
     */
    function getPrescribingDrugCardByUiRefId(uiRefId) {
      return $('set_' + uiRefId);
    }

    /**
     * Gets the re-prescribe checkbox element by its UI reference ID.
     * @param uiRefId The UI reference ID.
     * @returns {HTMLElement|null} The checkbox element, or null if not found.
     */
    function getReRxCheckboxByUiRefId(uiRefId) {
      return $('reRxCheckBox_' + uiRefId);
    }

    function updateQty(element) {
      let elemId = element.id;
      let ar = elemId.split("_");
      let rand = ar[1];
      let data = "parameterValue=updateDrug&randomId=" + rand + "&action=updateQty&quantity=" + encodeURIComponent(element.value);
      let url = ctx + "/oscarRx/WriteScript.do";

      let rxMethod = "rxMethod_" + rand;
      let rxRoute = "rxRoute_" + rand;
      let rxFreq = "rxFreq_" + rand;
      let rxDrugForm = "rxDrugForm_" + rand;
      let rxDuration = "rxDuration_" + rand;
      let rxDurationUnit = "rxDurationUnit_" + rand;
      let rxAmt = "rxAmount_" + rand;
      let str;

      let methodStr = "method_" + rand;
      let routeStr = "route_" + rand;
      let frequencyStr = "frequency_" + rand;
      let minimumStr = "minimum_" + rand;
      let maximumStr = "maximum_" + rand;
      let durationStr = "duration_" + rand;
      let durationUnitStr = "durationUnit_" + rand;
      let quantityStr = "quantityStr_" + rand;
      let unitNameStr = "unitName_" + rand;
      let prnStr = "prn_" + rand;
      let prnVal = "prnVal_" + rand;
      new Ajax.Request(url, {
        method: 'POST', parameters: data,
        requestHeaders: {'Accept': 'application/json'},
        onSuccess: function (transport) {
              let json = transport.responseText.evalJSON();
              $(methodStr).innerHTML = json.method;
              $(routeStr).innerHTML = json.route;
              $(frequencyStr).innerHTML = json.frequency;
              $(minimumStr).innerHTML = json.takeMin;
              $(maximumStr).innerHTML = json.takeMax;
              if (json.duration == null || json.duration == "null") {
                  $(durationStr).innerHTML = '';
              } else {
                  $(durationStr).innerHTML = json.duration;
              }
              $(durationUnitStr).innerHTML = json.durationUnit;
              $(quantityStr).innerHTML = json.calQuantity;
              if (json.unitName != null && json.unitName != "null" && json.unitName != "NULL" && json.unitName != "Null") {
                  $(unitNameStr).innerHTML = json.unitName;
              } else {
                  $(unitNameStr).innerHTML = '';
              }
              if (json.prn) {
                  $(prnStr).innerHTML = "prn";
                  $(prnVal).value = true;
              } else {
                  $(prnStr).innerHTML = "";
                  $(prnVal).value = false;
              }

          }
      });
      return true;
    }

    const methods = ["Take", "Apply", "Rub well in"];
    const routes = ["PO", "SL", "IM", "Subcut", "PATCH", "TOP", "INH", "SUPP", "right eye", "left eye", "both eyes"];
    const frequencies = ["BID", "TID", "QID", "once daily", "twice daily", "3x daily", "4x daily", "weekly"];
    const numbers = ["1/4", "1/2", "1", "2", "3", "4", "5", "6", "7", "8", "9"];
    const durations = ["day", "days", "week", "weeks", "month", "months", "3 months"];

    let placeholderInterval;

    function getRandom(arr) {
        return arr[Math.floor(Math.random() * arr.length)];
    }

    function generateExample() {
        return getRandom(methods) + " " +
            getRandom(routes) + " " +
            getRandom(frequencies) + " " +
            getRandom(numbers) + " for " +
            getRandom(durations);
    }

    function startShowingSampleInstructions(element, randId) {
        element.placeholder = "e.g. " + generateExample();
        element._placeholderInterval = setInterval(() => {
            element.placeholder = "e.g. " + generateExample();
        }, 2000);

        document.getElementById("disp_instruct_link_" + randId).classList.add('ripple-wrap');
        document.getElementById("disp_instruct_img_" + randId).classList.add('ripple-icon', 'pulse-icon');
    }


    function stopShowingSampleInstructions(element, randId) {
        clearInterval(element._placeholderInterval);
        element._placeholderInterval = null;

        document.getElementById("disp_instruct_link_" + randId).classList.remove('ripple-wrap');
        document.getElementById("disp_instruct_img_" + randId).classList.remove('ripple-icon', 'pulse-icon');
    }

        function parseIntr(element) {
            let elemId = element.id;
            let ar = elemId.split("_");
            let rand = ar[1];
      let instruction = "parameterValue=updateDrug&instruction=" + encodeURIComponent(element.value) + "&action=parseInstructions&randomId=" + rand;
      let url = ctx + "/oscarRx/UpdateScript.do";
            let quantity = "quantity_" + rand;
            let str;
            let methodStr = "method_" + rand;
            let routeStr = "route_" + rand;
            let frequencyStr = "frequency_" + rand;
            let minimumStr = "minimum_" + rand;
            let maximumStr = "maximum_" + rand;
            let durationStr = "duration_" + rand;
            let durationUnitStr = "durationUnit_" + rand;
            let quantityStr = "quantityStr_" + rand;
            let unitNameStr = "unitName_" + rand;
            let prnStr = "prn_" + rand;
            let prnVal = "prnVal_" + rand;
            new Ajax.Request(url, {
        method: 'POST', parameters: instruction, asynchronous: false,
        requestHeaders: {'Accept': 'application/json'},
        onSuccess: function (transport) {
                    let json = transport.responseText.evalJSON();
                    if (json.policyViolations != null && json.policyViolations.length > 0) {
                        for (let x = 0; x < json.policyViolations.length; x++) {
                            alert(json.policyViolations[x]);
                        }
                        $("saveButton").disabled = true;
                        $("saveOnlyButton").disabled = true;
                    } else {
                        $("saveButton").disabled = false;
                        $("saveOnlyButton").disabled = false;
                    }

                    $(methodStr).innerHTML = json.method;
                    $(routeStr).innerHTML = json.route;
                    $(frequencyStr).innerHTML = json.frequency;
                    $(minimumStr).innerHTML = json.takeMin;
                    $(maximumStr).innerHTML = json.takeMax;
                    if (json.duration == null || json.duration == "null") {
                        $(durationStr).innerHTML = '';
                    } else {
                        $(durationStr).innerHTML = json.duration;
                    }
                    $(durationUnitStr).innerHTML = json.durationUnit;
                    if (json.unitName != null && json.unitName != "null" && json.unitName != "NULL" && json.unitName != "Null") {
                        $(unitNameStr).innerHTML = json.unitName;
                    } else {
                        $(unitNameStr).innerHTML = '';
                    }
                    if (json.calQuantity != 0) {
                        //this is oftentimes zero when re-prescribing a drug where the unitName != null.
                        //Until a more reliable calculated quantity is being returned, don't update if the calculated quantity is 0
                        //silently changing to 0 can be problematic in situations where the quantity has already been set
                        //to an appropriate value.
                        $(quantityStr).innerHTML = json.calQuantity;
                        if ($(unitNameStr).innerHTML != '')
                            $(quantity).value = $(quantityStr).innerHTML + " " + $(unitNameStr).innerHTML;
                        else
                            $(quantity).value = $(quantityStr).innerHTML;

                    }
                    if (json.prn) {
                        $(prnStr).innerHTML = "prn";
                        $(prnVal).value = true;
                    } else {
                        $(prnStr).innerHTML = "";
                        $(prnVal).value = false;
                    }
                }
            });
            return true;
        }

        function addLuCode(eleId, luCode) {
            $(eleId).value = $(eleId).value + " LU Code: " + luCode;
        }

        function getRenalDosingInformation(divId, atcCode) {
      let url = "<%= request.getContextPath() %>/oscarRx/RenalDosing.jsp";
            let ran_number = Math.round(Math.random() * 1000000);
      let params = "demographicNo=<%=Encode.forUriComponent(String.valueOf(demoNo))%>&atcCode=" + encodeURIComponent(atcCode) + "&divId=" + divId + "&rand=" + ran_number;
            new Ajax.Updater(divId, url, {
                method: 'get',
                parameters: params,
                insertion: Insertion.Bottom,
                asynchronous: true
            });
        }

        function getLUC(divId, randomId, din) {
      let url = ctx + "/oscarRx/LimitedUseCode.jsp";
      let params = "randomId=" + randomId + "&din=" + encodeURIComponent(din);
            new Ajax.Updater(divId, url, {
                method: 'get',
                parameters: params,
                insertion: Insertion.Bottom,
                asynchronous: true
            });
        }

        function validateRxDate() {
            let x = true;
            jQuery('input[name^="rxDate__"]').each(function () {
                let str1 = jQuery(this).val();

                 let dt = str1.split("-");
                 if (dt.length>3) {
                 	jQuery(this).focus();
                     alert('Start Date wrong format! Must be yyyy or yyyy-mm or yyyy-mm-dd');
                     x = false;
                     return;
                 }

                 let dt1=1, mon1=0, yr1=parseInt(dt[0],10);
                 if (isNaN(yr1) || yr1<0 || yr1>9999) {
                 	jQuery(this).focus();
                     alert('Invalid Start Date! Please check the year');
                     x = false;
                     return;
                 }
                 if (dt.length>1) {
                 	mon1 = parseInt(dt[1],10)-1;
                 	if (isNaN(mon1) || mon1<0 || mon1>11) {
                 		jQuery(this).focus();
                 		alert('Invalid Start Date! Please check the month');
                         x = false;
                         return;
                 	}
                 }
                 if (dt.length>2) {
                 	dt1 = parseInt(dt[2],10);
                     if (isNaN(dt1) || dt1<1 || dt1>31) {
                     	jQuery(this).focus();
                         alert('Invalid Start Date! Please check the day');
                         x = false;
                         return;
                     }
                 }
                 let date1 = new Date(yr1, mon1, dt1);
                 let now  = new Date();

                 if(date1 > now) {
                 	jQuery(this).focus();
                     alert('Start Date cannot be in the future. (' + str1 +')');
                     x = false;
                     return;
     	        }
             });
             return x;
         }
      
        function validateWrittenDate() {
          let x = true;
            jQuery('input[name^="writtenDate_"]').each(function(){
                let str1  = jQuery(this).val();
    
                let dt = str1.split("-");
                if (dt.length>3) {
                  jQuery(this).focus();
                    alert('Written Date wrong format! Must be yyyy or yyyy-mm or yyyy-mm-dd');
                    x = false;
                    return;
                }
    
                let dt1 = 1, mon1 = 0, yr1 = parseInt(dt[0], 10);
                if (isNaN(yr1) || yr1 < 0 || yr1 > 9999) {
                    jQuery(this).focus();
                    alert('Invalid Written Date! Please check the year');
                    x = false;
                    return;
                }
                if (dt.length > 1) {
                    mon1 = parseInt(dt[1], 10) - 1;
                    if (isNaN(mon1) || mon1 < 0 || mon1 > 11) {
                        jQuery(this).focus();
                        alert('Invalid Written Date! Please check the month');
                        x = false;
                        return;
                    }
                }
                if (dt.length > 2) {
                    dt1 = parseInt(dt[2], 10);
                    if (isNaN(dt1) || dt1 < 1 || dt1 > 31) {
                        jQuery(this).focus();
                        alert('Invalid Written Date! Please check the day');
                        x = false;
                        return;
                    }
                }
                let date1 = new Date(yr1, mon1, dt1);
                let now = new Date();
    
                if (date1 > now) {
                    jQuery(this).focus();
                    alert('Written Date cannot be in the future. (' + str1 + ')');
                    x = false;
                    return;
                }
            });
            return x;
        }
    
    
        /**
         * Counts the medications currently staged for this prescription.
         *
         * Each staged drug renders a drugName_<rand> input inside the drug form;
         * a ReRx box that was only checked (not staged) produces no such input.
         * This deliberately keys on the same drugName_ marker that the server
         * uses to detect staged drugs in RxWriteScript2Action.updateSaveAllDrugs()
         * ("ele.startsWith(\"drugName_\")"), so this gate matches exactly what the
         * server would persist. Keep the two in sync if that marker ever changes.
         *
         * @returns {number} the number of staged medications
         */
        function countStagedMedications() {
            const form = document.getElementById('drugForm');
            if (!form) {
                return 0;
            }
            return form.querySelectorAll('input[name^="drugName_"]').length;
        }

        function updateSaveAllDrugsPrintCheckContinue() {
            // Nothing staged: warn instead of saving an empty prescription (#2453).
            // The ReRx selection is left intact so the user can still click
            // Stage Medication; it is only archived once actually staged and saved
            // (guarded in RxWriteScript2Action.saveDrug()).
            if (countStagedMedications() === 0) {
                alert('No medications have been added.');
                return;
            }
            updateSaveAllDrugsPrintContinue();
        }

        function updateSaveAllDrugsCheckContinue() {
            // Nothing staged: do nothing rather than save an empty prescription (#2453).
            if (countStagedMedications() === 0) {
                return;
            }
            updateSaveAllDrugsContinue();
        }
    
        const CONFIRMATION_MESSAGE = {
            SINGLE: 'is 1 unstaged ReRx drug',
            MULTIPLE: (count) => "are " + count + " unstaged ReRx drugs"
        };
    
        const SAVE_WARNING = 'If you continue, the unstaged ReRx drug(s) will not be re-prescribed.';
        const SAVE_PROMPT = 'Are you sure you want to save this prescription?';
    
        function showUnstagedReRxConfirmation(onConfirm) {
            if (selectedReRxIDs.length === 0) {
                onConfirm();
                return;
            }
    
            const message = buildConfirmationMessage(selectedReRxIDs.length);
            if (confirm(message)) {
                cancelAndClearSelection();
                onConfirm();
            }
        }
    
        function buildConfirmationMessage(count) {
            const statusMessage = count === 1
                ? CONFIRMATION_MESSAGE.SINGLE
                : CONFIRMATION_MESSAGE.MULTIPLE(count);
            return "There " + statusMessage + ".\n" + SAVE_WARNING + "\n" + SAVE_PROMPT;
        }

        function updateSaveAllDrugsPrintContinue() {
            if (!validateWrittenDate()) {
                return false;
            }
            if (!validateRxDate()) {
                return false;
            }
    
        <%if (OscarProperties.getInstance().isPropertyActive("rx_strict_med_term")) {%>
        if(!checkMedTerm()){
          return false;
        }
        <%
          }
          %>
       setPharmacyId();
      let data = Form.serialize($('drugForm'));
      let url = ctx + "/oscarRx/WriteScript.do?parameterValue=updateSaveAllDrugs&rand=" + Math.floor(Math.random() * 10001);
      new Ajax.Request(url,
        {
          method: 'post', postBody: data, asynchronous: false,
          requestHeaders: {'Accept': 'application/json'},
          onSuccess: function (transport) {

            callReplacementWebService("ListDrugs.jsp", 'drugProfile');

            try {
              let saveResponse = JSON.parse(transport.response);
              let scriptId = saveResponse && saveResponse.savedScriptId;
              if (scriptId) {
                popForm2(scriptId);
              } else {
                console.warn("No valid script ID received from server");
              }
            } catch (err) {
              console.error("Error parsing script ID response:", err);
            }
            resetReRxDrugList();
          }
        });
      return false;
    }

    function updateSaveAllDrugsContinue() {
      if (!validateWrittenDate()) {
        return false;
      }
      if (!validateRxDate()) {
        return false;
      }

      <%if (OscarProperties.getInstance().isPropertyActive("rx_strict_med_term")) {%>
      if (!checkMedTerm()) {
        return false;
      }
      <%}%>
      setPharmacyId();
      let data = Form.serialize($('drugForm'));
      let url = ctx + "/oscarRx/WriteScript.do?parameterValue=updateSaveAllDrugs&rand=" + Math.floor(Math.random() * 10001);
      new Ajax.Request(url,
        {
          method: 'post', postBody: data, asynchronous: false,
          requestHeaders: {'Accept': 'application/json'},
          onSuccess: function (transport) {

            callReplacementWebService("ListDrugs.jsp", 'drugProfile');

            resetReRxDrugList();

            resetStash();
          }
        });
      return false;
    }

    /**
     * Gets the selected preferred pharmacy id and then sets it into the
     * rxPharmacyId hidden input for submission with each drug in
     * a prescription.
     */
    function setPharmacyId() {
      let selectedPharmacy = jQuery("#Calcs option:selected").val();
      let selectedPharmacyId = "";
      if (selectedPharmacy) {
        selectedPharmacyId = JSON.parse(selectedPharmacy).id;
      }
      jQuery("#rxPharmacyId").val(selectedPharmacyId);
    }

    function checkEnterSendRx() {
      popupRxSearchWindow();
      return false;
    }


    <%
if (OscarProperties.getInstance().isPropertyActive("rx_strict_med_term")) {
%>

    function checkMedTerm() {

      let randId = 0;
      let isAnyTermChecked = false;
      jQuery("fieldset[id^='set_']").each(function () {
        randId = jQuery(this).attr("id").replace('set_', '');
        isAnyTermChecked = isMedTermChecked(randId);
      });

      if (!isAnyTermChecked) {
        alert("Please review drug(s) and specify medication term!");
      } else {
        return true;
      }

      return false;
    }// end checkMedTerm

    function isMedTermChecked(rnd) {
      let termChecked = false;
      let longTermY = jQuery("#longTermY_" + rnd);
      let longTermN = jQuery("#longTermN_" + rnd);

      let shortTerm = jQuery("#shortTerm_" + rnd);
      let medTermWrap = jQuery("#medTerm_" + rnd);

      if (longTermY.is(":checked") || longTermN.is(":checked")) {
        termChecked = true;
        medTermWrap.css('color', 'black');
      } else {
        termChecked = false;
        medTermWrap.css('color', 'red');
      }

      return termChecked;
    }

    <%
} //end rx_strict_med_term check
%>


    function medTermCheckOne(rnd, el) {
      let longTerm = jQuery("#longTerm_" + rnd);
      let shortTerm = jQuery("#shortTerm_" + rnd);

      if (el.prop("checked")) {
        if (el.attr("id") == "longTerm_" + rnd) {
          shortTerm.attr("checked", false);
        } else {
          longTerm.attr("checked", false);
        }
      }
    }


    jQuery(document).ready(function () {
      jQuery(document).on('change', '.med-term', function () {
        let randId = jQuery(this).attr("id").split("_").pop();

        <%
	    if (OscarProperties.getInstance().isPropertyActive("rx_strict_med_term")) {
	    %>
        isMedTermChecked(randId);
        <%
	    }
	    %>
      });
    });

    function updateShortTerm(rand, val) {
      if (val) {
        jQuery("#shortTerm_" + rand).prop("checked", true);
      } else {
        jQuery("#shortTerm_" + rand).prop("checked", false);
      }

    }

    function updateLongTerm(rand, repeatEl) {
      <% if("true".equals(OscarProperties.getInstance().getProperty("rx_select_long_term_when_repeat", "true"))) {%>
        let repeats = jQuery('#repeats_' + rand).val().trim();
        if (!isNaN(repeats) && repeats > 0) {
          jQuery("#longTermY_" + rand).prop("checked", true);
        }
    <%}%>
    }

</script>

<!-- Bootstrap Modal for Prescription Preview -->
<div class="modal fade" id="rxPreviewBootstrapModal" tabindex="-1"
     aria-labelledby="rxPreviewBootstrapModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-scrollable custom-modal-size custom-modal-height">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="rxPreviewBootstrapModalLabel">Prescription Preview</h5>
        <button type="button" class="btn btn-secondary" id="rxPreviewBootstrapEditRxButton"
                data-bs-dismiss="modal">Edit Rx
        </button>
      </div>
      <div class="modal-body" id="rxPreviewBootstrapModalBody">
      </div>
    </div>
  </div>
</div>


<script language="javascript" src="<%= request.getContextPath() %>/commons/scripts/sort_table/css.js"></script>
<script language="javascript" src="<%= request.getContextPath() %>/commons/scripts/sort_table/common.js"></script>
<script language="javascript" src="<%= request.getContextPath() %>/commons/scripts/sort_table/standardista-table-sorting.js"></script>

<script type="text/javascript" src="<%= request.getContextPath() %>/oscarRx/printRx/PrintPreview.js"></script>

<link rel="stylesheet" href="<%= request.getContextPath() %>/library/bootstrap/5.0.2/css/bootstrap.min.css" type="text/css"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/library/bootstrap/5.0.2/js/bootstrap.min.js"></script>

<script type="text/javascript" src="<%= request.getContextPath() %>/share/oscarSignaturePad/SignaturePad.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/library/signature-pad/5.0.7/signature_pad.umd.min.js"></script>
</body>
</html>
