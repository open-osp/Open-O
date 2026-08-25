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

<%@page import="ca.openosp.openo.commn.model.PartialDate" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" prefix="e" %>
<%@ page import="ca.openosp.OscarProperties,ca.openosp.openo.log.*" %>
<%@page import="ca.openosp.openo.casemgmt.service.CaseManagementManager,
                ca.openosp.openo.casemgmt.model.CaseManagementNoteLink,
                ca.openosp.openo.casemgmt.model.CaseManagementNote" %>
<%@page import="ca.openosp.openo.utility.SpringUtils" %>
<%@page import="ca.openosp.openo.utility.LoggedInInfo,ca.openosp.openo.commn.dao.DrugReasonDao,ca.openosp.openo.commn.model.DrugReason" %>
<%@page import="ca.openosp.openo.util.*,java.util.*,ca.openosp.openo.commn.model.Drug,ca.openosp.openo.commn.dao.*" %>
<%@page import="ca.openosp.openo.managers.DrugDispensingManager" %>
<%@page import="ca.openosp.openo.managers.CodingSystemManager" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="ca.openosp.openo.services.security.SecurityManager" %>
<%@ page import="ca.openosp.openo.prescript.pageUtil.RxSessionBean" %>
<%@ page import="ca.openosp.openo.prescript.data.RxPatientData" %>
<%@ page import="ca.openosp.openo.commn.dao.PartialDateDao" %>
<%@ page import="static ca.openosp.openo.prescript.util.RxUtil.DateToString" %>
<%@ page import="ca.openosp.openo.prescript.data.RxPrescriptionData" %>
<fmt:setBundle basename="oscarResources"/>
<%
    RxPatientData.Patient patient = null;
    RxSessionBean bean = null;
%>
<c:if test="${empty sessionScope.RxSessionBean}">
    <c:redirect url="error.html"/>
</c:if>
<c:if test="${not empty sessionScope.RxSessionBean}">
    <%
        // Directly access the RxSessionBean from the session
        bean = (RxSessionBean) session.getAttribute("RxSessionBean");
        if (bean != null && !bean.isValid()) {
            response.sendRedirect("error.html");
            return; // Ensure no further JSP processing
        }

        patient = (RxPatientData.Patient) request.getSession().getAttribute("Patient");
    %>
</c:if>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>




<%
    LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
    SecurityManager securityManager = new SecurityManager();
    PartialDateDao partialDateDao = SpringUtils.getBean(PartialDateDao.class);

    CodingSystemManager codingSystemManager = SpringUtils.getBean(CodingSystemManager.class);

    boolean integratorEnabled = loggedInInfo.getCurrentFacility().isIntegratorEnabled();
    String annotation_display = CaseManagementNoteLink.DISP_PRESCRIP;

    // The legend in SearchDrug3.jsp can load this page multiple times into #drugProfile —
    // once as a replacement (e.g. "Long Term Meds") and one or more times as appended
    // sections (e.g. "Acute", "Inactive", "External"). Each request passes a "heading"
    // param that labels the section and is used to give its table a unique DOM id so that
    // each section can be independently initialised as its own DataTable instance.
    String heading = request.getParameter("heading");
    
    // Remove spaces so the heading is safe to embed directly in an HTML element id.
    String headingSuffix = (heading != null) ? heading.replaceAll("\\s+", "") : "";
    String tableId = "Drug_table" + headingSuffix;
    request.setAttribute("sectionHeading", heading);
    request.setAttribute("drugTableId", tableId);
%>
<c:if test="${not empty sectionHeading}">
    <h4 style="margin-bottom:1px;margin-top:10px;font-size:16px"><e:forHtml value="${sectionHeading}"/></h4>
</c:if>
<div class="drugProfileText" style="">
    <table class="table table-condensed list-drugs" id="<e:forHtmlAttribute value="${drugTableId}"/>">
      <thead>
        <tr>
        	<th>Entered Date</th>
            <th><fmt:message key="SearchDrug.msgRxDate"/></th>
            <th>Days to Exp</th>
            <th>LT Med</th>
            <th><fmt:message key="SearchDrug.msgPrescription"/></th>
			<%if(securityManager.hasWriteAccess("_rx",roleName$,true)) {%>
            <th><fmt:message key="SearchDrug.msgReprescribe"/></th>
            	<%if(!OscarProperties.getInstance().getProperty("rx.delete_drug.hide","false").equals("true")) {%>
            	<th><fmt:message key="SearchDrug.msgDelete"/></th>
            <% 	}	 
			}            
            %>
            <th><fmt:message key="SearchDrug.msgDiscontinue"/></th>		
			<th title="<fmt:message key="SearchDrug.msgReason_help"/>"> <fmt:message key="SearchDrug.msgReason"/></th>
            <th><fmt:message key="SearchDrug.msgPastMed"/></th>
            <%if(securityManager.hasWriteAccess("_rx",roleName$,true)) {%>
            	<th><fmt:message key="SearchDrug.annotation"/></th>
            <% } %>
            <th><fmt:message key="SearchDrug.msgLocationPrescribed"/></th>
            <th title="<fmt:message key="SearchDrug.msgHideCPP_help"/>"><fmt:message key="SearchDrug.msgHideCPP"/></th>
            <%if(OscarProperties.getInstance().getProperty("rx.enable_internal_dispensing","false").equals("true")) {%>
             <th><fmt:message key="SearchDrug.msgDispense"/></th>
             <%} %>
        </tr>
      </thead>
      <tbody>
        <%
            List<Drug> prescriptDrugs = null;
            CaseManagementManager caseManagementManager = SpringUtils.getBean(CaseManagementManager.class);

            prescriptDrugs = caseManagementManager.getPrescriptions(loggedInInfo, patient.getDemographicNo(), true);

            DrugReasonDao drugReasonDao = SpringUtils.getBean(DrugReasonDao.class);

            DrugDispensingManager drugDispensingManager = SpringUtils.getBean(DrugDispensingManager.class);
            List<String> reRxDrugList = bean.getReRxDrugIdList();
            Collections.sort(prescriptDrugs, Drug.START_DATE_COMPARATOR);

            long now = System.currentTimeMillis();
            long month = 1000L * 60L * 60L * 24L * 30L;
            for (int x = 0; x < prescriptDrugs.size(); x++) {
                Drug prescriptDrug = prescriptDrugs.get(x);
                boolean isPrevAnnotation = false;
                String styleColor = "";
                //test for previous note
                HttpSession se = request.getSession();
                Integer tableName = caseManagementManager.getTableNameByDisplay(annotation_display);

                CaseManagementNoteLink cml = null;
                CaseManagementNote p_cmn = null;

                if (prescriptDrug.getRemoteFacilityId() != null) {
                    cml = caseManagementManager.getLatestLinkByTableId(tableName, Long.parseLong(prescriptDrug.getId().toString()));
                }

                if (cml != null) {
                    p_cmn = caseManagementManager.getNote(cml.getNoteId().toString());
                }
                if (p_cmn != null) {
                    isPrevAnnotation = true;
                }

//add all long term med drugIds to an array.
                styleColor = getClassColour(prescriptDrug, now, month);
                String specialText = prescriptDrug.getSpecial();
                specialText = specialText == null ? "" : specialText.replace("\n", " ");
                Integer prescriptIdInt = prescriptDrug.getId();
                String bn = prescriptDrug.getBrandName();

                boolean startDateUnknown = prescriptDrug.getStartDateUnknown();
        %>
        <tr data-is-archived="<%= prescriptDrug.isArchived() %>"
            data-is-longterm="<%= prescriptDrug.isLongTerm() %>"
            data-is-current="<%= prescriptDrug.isCurrent() %>"
            data-is-external="<%= prescriptDrug.isExternal() %>">

        <td><a id="createDate_<%=Encode.forHtmlAttribute(String.valueOf(prescriptIdInt))%>" class="<%=Encode.forHtmlAttribute(String.valueOf(styleColor))%>" href="<%= request.getContextPath() %>/oscarRx/StaticScript2.jsp?regionalIdentifier=<%=Encode.forUriComponent(prescriptDrug.getRegionalIdentifier())%>&amp;cn=<%=Encode.forUriComponent(prescriptDrug.getCustomName())%>&amp;bn=<%=Encode.forUriComponent(bn)%>&amp;atc=<%=Encode.forUriComponent(prescriptDrug.getAtc())%>"><%=Encode.forHtml(String.valueOf(DateToString(prescriptDrug.getCreateDate())))%></a></td>
            <%-- data-order: DataTables sorts this column by the attribute value instead of the cell
                 content. When start date is unknown the cell renders empty, so we fall back to
                 createDate to maintain parity with the previous server-side sort, which used
                 the same fallback to keep rows in a meaningful sort position. --%>
            <td data-order="<%= Encode.forHtmlAttribute(startDateUnknown ? DateToString(prescriptDrug.getCreateDate()) : DateToString(prescriptDrug.getRxDate())) %>">

            	<% if(startDateUnknown) { %>

                <% } else {
                    String startDate = UtilDateUtilities.DateToString(prescriptDrug.getRxDate());
                    startDate = partialDateDao.getDatePartial(startDate, PartialDate.DRUGS, prescriptDrug.getId(), PartialDate.DRUGS_STARTDATE);
                %>
                <a id="rxDate_<%=Encode.forHtmlAttribute(String.valueOf(prescriptIdInt))%>"   class="<%=Encode.forHtmlAttribute(String.valueOf(styleColor))%>"
                   href="<%= request.getContextPath() %>/oscarRx/StaticScript2.jsp?regionalIdentifier=<%=Encode.forUriComponent(prescriptDrug.getRegionalIdentifier())%>&amp;cn=<%=Encode.forUriComponent(prescriptDrug.getCustomName())%>&amp;bn=<%=Encode.forUriComponent(bn)%>"><%=Encode.forHtml(String.valueOf(startDate))%>
                </a>
                <% } %>
            </td>
            <td>
                <% if (startDateUnknown) { %>

                <% } else { %>
                <%=Encode.forHtml(String.valueOf(prescriptDrug.daysToExpire()))%>
                <% } %>
            </td>
            <td>
                <div class="drug-maintenance-switch" style="display: flex; align-items: baseline;">
                    <% String drugMaintenanceSwitch = "drugMaintenanceSwitch_" + prescriptIdInt + Math.abs(new Random().nextInt(10001)); %>
                    <input id="<%=Encode.forHtmlAttribute(String.valueOf(drugMaintenanceSwitch))%>" type="checkbox" name="checkBox_<%=Encode.forHtmlAttribute(String.valueOf(prescriptIdInt))%>"
                           class="drug-maintenance-switch-input"
                           onclick="changeLt(this, '<%=Encode.forJavaScript(String.valueOf(prescriptIdInt))%>');"
                            <% if (!securityManager.hasWriteAccess("_rx", roleName$, true)) {%> disabled <%}%>
                            <% if (prescriptDrug.isLongTerm()) {%> checked <%}%> />
                    <label id="drugMaintenanceSwitchLbl_<%=Encode.forHtmlAttribute(String.valueOf(prescriptIdInt))%>" for="<%=Encode.forHtmlAttribute(String.valueOf(drugMaintenanceSwitch))%>" class="drug-maintenance-switch-label">

                    </label>
                </div>
            </td>
			<%
			//display comment as tooltip if not null - simply using the TITLE attr
			String xComment=prescriptDrug.getComment();
			String tComment="";
			if(xComment!=null ){
				tComment="TITLE='" + Encode.forHtmlAttribute(xComment) + " '";
			}
			
			%>
            <td><a id="prescrip_<%=Encode.forHtmlAttribute(String.valueOf(prescriptIdInt))%>" class="<%=Encode.forHtmlAttribute(String.valueOf(styleColor))%>" href="<%= request.getContextPath() %>/oscarRx/StaticScript2.jsp?regionalIdentifier=<%=Encode.forUriComponent(prescriptDrug.getRegionalIdentifier())%>&amp;cn=<%=Encode.forUriComponent(prescriptDrug.getCustomName())%>&amp;bn=<%=Encode.forUriComponent(bn)%>&amp;atc=<%=Encode.forUriComponent(prescriptDrug.getAtc())%>"   <%=tComment%>   ><%=Encode.forHtml(String.valueOf(RxPrescriptionData.getFullOutLine(prescriptDrug.getSpecial()).replaceAll(";", " ")))%></a></td>
			<%            			
	           	if(securityManager.hasWriteAccess("_rx",roleName$,true)) {            		
           	%>
            <td>

                <%if (prescriptDrug.getRemoteFacilityName() == null) {%>
                <div style="display: flex; align-items: center;">
                    <% String cbxId = "reRxCheckBox_" + prescriptIdInt; %>
                    <input id="<%=Encode.forHtmlAttribute(String.valueOf(cbxId))%>" type=CHECKBOX
                           onclick="updateReRxStatusForPrescribedDrug(this, <%=Encode.forJavaScript(String.valueOf(prescriptIdInt))%>)"
                           <%if(reRxDrugList.contains(prescriptIdInt.toString())){%>checked<%}%>
                           name="checkBox_<%=Encode.forHtmlAttribute(String.valueOf(prescriptIdInt))%>">
                    <label id="reRx_<%=Encode.forHtmlAttribute(String.valueOf(prescriptIdInt))%>" for="<%=Encode.forHtmlAttribute(String.valueOf(cbxId))%>">ReRx</label>
                </div>
                <%} else {%>
                <form action="<%=request.getContextPath()%>/oscarRx/searchDrug.do" method="post">
                    <input type="hidden" name="demographicNo" value="<%=Encode.forHtmlAttribute(String.valueOf(patient.getDemographicNo()))%>"/>
                    <input type="hidden" name="searchString" value="<%=Encode.forHtmlAttribute(String.valueOf(getName(prescriptDrug)))%>"/>
                    <input type="submit" class="ControlPushButton" value="Search to Re-prescribe"/>
                </form>
                <%}%>
            </td>

			<%if(!OscarProperties.getInstance().getProperty("rx.delete_drug.hide","false").equals("true")) { %>
            <td>

                <%if (prescriptDrug.getRemoteFacilityName() == null) {%>
                <a id="del_<%=Encode.forHtmlAttribute(String.valueOf(prescriptIdInt))%>" name="delete" class="<%=Encode.forHtmlAttribute(String.valueOf(styleColor))%>" href="javascript:void(0);"
                   onclick="Delete2(this);">Del</a>
                <%}%>
            </td>

			<% } 
	         }
			%>
            <td>
                <%if(!prescriptDrug.isDiscontinued())
                {
               	 if (prescriptDrug.getRemoteFacilityId()==null)
               	 {
               		
					if(securityManager.hasWriteAccess("_rx",roleName$,true)) {            		
				
                %>
                	<a id="discont_<%=Encode.forHtmlAttribute(String.valueOf(prescriptIdInt))%>" href="javascript:void(0);" onclick="Discontinue(event,this);" class="<%=Encode.forHtmlAttribute(String.valueOf(styleColor))%>" >Discon</a>                
                <% }
               	 }
                }else{%>
                  <%=Encode.forHtml(String.valueOf(prescriptDrug.getArchivedReason()))%>
                <%}%>
            </td>
  <%-- DRUG REASON --%>          
            <td>
            	<% 	
            		List<DrugReason> drugReasons  = drugReasonDao.getReasonsForDrugID(prescriptDrug.getId(),true);            		            					        	
			
            		if (prescriptDrug.getRemoteFacilityId()==null && securityManager.hasWriteAccess("_rx",roleName$,true) )
            		{
            			%>
			           	 	<a href="javascript:void(0);"  onclick="popupRxReasonWindow(<%=Encode.forJavaScript(String.valueOf(patient.getDemographicNo()))%>,<%=Encode.forJavaScript(String.valueOf(prescriptIdInt))%>);"  title="<%=Encode.forHtmlAttribute(String.valueOf(displayDrugReason(codingSystemManager,drugReasons,true)))%>">
            			<%
            		}
            	%>
            	<%=Encode.forHtml(String.valueOf(StringUtils.maxLenString(displayDrugReason(codingSystemManager,drugReasons,false), 4, 3, StringUtils.ELLIPSIS)))%>
				<%
		      		if (prescriptDrug.getRemoteFacilityId()==null  && securityManager.hasWriteAccess("_rx",roleName$,true))
		      		{
		      			%>
			            	</a>
            			<%
            		}
				%>
            </td>
  <%-- END DRUG REASON --%> 		
            <%
            Boolean past_med = prescriptDrug.getPastMed();
            %>
            
            <td >
               	<% if( past_med == null) { %>                        		
        			unk
        		<% } else if(past_med) { %>
        			yes                       		
        		<% } else { %>
        			no
        		<% } %>
            </td>

			<%if(securityManager.hasWriteAccess("_rx",roleName$,true)) {%>
            <td>
                <%
                    if (prescriptDrug.getRemoteFacilityId() == null) {
                %>
                <a href="javascript:void(0);" title="Annotation"
                   onclick="window.open('<%= request.getContextPath() %>/annotation/annotation.jsp?display=<%=Encode.forUriComponent(String.valueOf(annotation_display))%>&amp;table_id=<%=Encode.forUriComponent(String.valueOf(prescriptIdInt))%>&amp;demo=<%=Encode.forUriComponent(String.valueOf(bean.getDemographicNo()))%>&amp;drugSpecial=<%=Encode.forUriComponent(specialText)%>','anwin','width=400,height=500');">
                    <%if (!isPrevAnnotation) {%> <img src="<%= request.getContextPath() %>/images/notes.gif" alt="rxAnnotation" height="16"
                                                      width="13" border="0"><%} else {%><img
                        src="<%= request.getContextPath() %>/images/filledNotes.gif" height="16" width="13" alt="rxFilledNotes" border="0"> <%}%></a>
                <%
                    }
                %>
            </td>
            <% } %>

            <td>
                <%
                    if (prescriptDrug.getRemoteFacilityName() != null) { %>
                <span class="external"><%=Encode.forHtml(String.valueOf(prescriptDrug.getRemoteFacilityName()))%></span>
                <%} else if (prescriptDrug.getOutsideProviderName() != null && !prescriptDrug.getOutsideProviderName().equals("")) {%>
                <span class="external"><%=Encode.forHtml(String.valueOf(prescriptDrug.getOutsideProviderName()))%></span>
                <%} else {%>
                local
                <%}%>


            </td>

			<td>
				<%
					boolean hideCpp = prescriptDrug.getHideFromCpp();
					String checked="";
					if(hideCpp) {
						checked="checked=\"checked\"";
					}
				%>
				<input type="checkbox" id="hidecpp_<%=Encode.forHtmlAttribute(String.valueOf(prescriptIdInt))%>" <%=checked%>/>
			</td>
			
			<%if(OscarProperties.getInstance().getProperty("rx.enable_internal_dispensing","false").equals("true")) {%>
			<td>
				<%
					if(prescriptDrug.getDispenseInternal() != null && prescriptDrug.getDispenseInternal() == true ) {
						if(securityManager.hasWriteAccess("_dispensing",roleName$,true)) {	
							String dispensingStatus = drugDispensingManager.getStatus(prescriptDrug.getId());
				               
				%>
					<a href="javascript:void(0)" onclick="popupWindow(720,700,'<%=request.getContextPath()%>/oscarRx/Dispense.do?method=view&id=<%=Encode.forUriComponent(String.valueOf(prescriptDrug.getId()))%>','Dispense<%=Encode.forJavaScript(String.valueOf(prescriptIdInt))%>'); return false;">Dispense (<%=Encode.forHtml(String.valueOf(dispensingStatus))%>)</a>
				<% 
					} }
				%>
			</td>
			<% } %>
			
<%--			<td nowrap="nowrap" >--%>
<%--				<%if(!(prescriptDrugs.get(prescriptDrugs.size()-1) == prescriptDrug)) {%>--%>
<%--				<img border="0" src="<%=request.getContextPath()%>/images/icon_down_sort_arrow.png" onclick="moveDrugDown(<%=Encode.forJavaScript(String.valueOf(prescriptDrug.getId()))%>,<%=Encode.forJavaScript(String.valueOf(prescriptDrugs.get(x+1).getId()))%>,<%=Encode.forJavaScript(String.valueOf(prescriptDrug.getDemographicId()))%>);return false;"/>--%>
<%--				<% } %>--%>
<%--				<%if(!(prescriptDrugs.get(0) == prescriptDrug)) {%>--%>
<%--				<img border="0" src="<%=request.getContextPath()%>/images/icon_up_sort_arrow.png" onclick="moveDrugUp(<%=Encode.forJavaScript(String.valueOf(prescriptDrug.getId()))%>,<%=Encode.forJavaScript(String.valueOf(prescriptDrugs.get(x-1).getId()))%>,<%=Encode.forJavaScript(String.valueOf(prescriptDrug.getDemographicId()))%>);return false;"/>--%>
<%--				<%} %>--%>
<%--			</td>--%>

        </tr>


        <script>
            (function() {
                var element = $('hidecpp_<%=Encode.forJavaScript(String.valueOf(prescriptIdInt))%>');
                if (element) {
                    Event.observe(element, 'change', function (event) {
                        var val = $('hidecpp_<%=Encode.forJavaScript(String.valueOf(prescriptIdInt))%>').checked;
                        new Ajax.Request('<c:out value="${ctx}"/>/oscarRx/hideCpp.do?method=update&prescriptId=<%=Encode.forUriComponent(String.valueOf(prescriptIdInt))%>&value=' + val, {
                            method: 'get',
                            onSuccess: function (transport) {
                            }
                        });
                    });
                }
            })();
        </script>
        <%}%>
      </tbody>
    </table>

</div>

<script type="text/javascript">

    window.drugListTableConfig = window.drugListTableConfig || {
      // Default page length: the user's saved choice, or "All" (-1) when they've never changed it.
      // A getter (not a static value) because this config object is created once but reused every
      // time the table is rebuilt, so the length must be re-read from storage on each init.
      get pageLength() { return parseInt(localStorage.getItem('drugListPageLength')) || -1; },
      // Persist the choice whenever the user changes the dropdown (tickler uses the same approach).
      initComplete: function (settings) {
        jQuery(settings.nTable).on('length.dt', function (e, s, len) {
          localStorage.setItem('drugListPageLength', len);
        });
      },
      searching: true,
      lengthMenu: [[25, 50, 75, 100, -1], [25, 50, 75, 100, "All"]],
      columns: [
        {}, //entered date
        {}, //start date
        {}, //days to expire
        {orderable: false}, //long term
        {}, //medication
        {orderable: false}, //rerx
        {orderable: false}, //delete
        {orderable: false}, //discontinue
        {orderable: false}, //reason
        {}, //pastmed
        {orderable: false}, // note
        {}, // location
        {orderable: false} // checkbox
      ],
      // columnDefs: [
      //   // {visible: false, targets: groupColumn}
      // ],

      // drawCallback: function (settings) {
      //   let api = this.api();
      //   let rows = api.rows({page: 'current'}).nodes();
      //   let last = null;
      //
      //   api.column(groupColumn, {page: 'current'}) //TODO: this code reorders the rows on the current page, the global order based on the current sort.
      //     //      this means if a global sort is done that results in the tickler comments to NOT be on the current page
      //     //      they will not be visible.  A workaround has been implemented by adding the service date and priority
      //     //      into the tickler comment rows as well.  The datatables row group plugin might be a better approach,
      //     //      but will require refactoring of this code
      //     .data()
      //     .each(function (group, i) {
      //       if (last !== group) {
      //         jQuery(rows)
      //           .eq(i)
      //           .after(jQuery(".followup-comment-" + group))
      //         last = group;
      //       }
      //     });
      // },

      // default sort: column 1 = Start Date (Rx Date), descending (most recent first)
      // matches the original server-side Drug.START_DATE_COMPARATOR behaviour
      order: [[1, 'desc']]

    };
    drugListTable = jQuery('#${e:forJavaScript(drugTableId)}').dataTable(window.drugListTableConfig);
</script>
<%!

    String getName(Drug prescriptDrug) {
        String searchString = prescriptDrug.getBrandName();
        if (searchString == null) {
            searchString = prescriptDrug.getCustomName();
        }
        if (searchString == null) {
            searchString = prescriptDrug.getRegionalIdentifier();
        }
        if (searchString == null) {
            searchString = prescriptDrug.getSpecial();
        }
        return searchString;
    }

    String getClassColour(Drug drug, long referenceTime, long durationToSoon) {
        // Returns only the space-separated CSS class tokens (e.g. "currentDrug expireInReference"),
        // or "" when none apply. Callers wrap this in class="..." and OWASP-encode it as an
        // attribute value (Encode.forHtmlAttribute). Do NOT re-add the class="..." wrapper here:
        // emitting a full attribute fragment and then running it through Encode.forHtml() double-
        // encodes the quotes (class=&#34;...&#34;) and breaks the drug row styling.
        StringBuilder sb = new StringBuilder();

        if (!drug.isLongTerm() && (drug.isCurrent() && drug.getEndDate() != null && (drug.getEndDate().getTime() - referenceTime <= durationToSoon))) {
            sb.append("expireInReference ");
        }

        if ((drug.isCurrent() && !drug.isArchived()) || drug.isLongTerm()) {
            sb.append("currentDrug ");
        }

        if (drug.isArchived()) {
            sb.append("archivedDrug ");
        }

        if (!drug.isLongTerm() && !drug.isCurrent()) {
            sb.append("expiredDrug ");
        }

        if (drug.isLongTerm()) {
            sb.append("longTermMed ");
        }

        if (drug.isDiscontinued()) {
            sb.append("discontinued ");
        }

        if (drug.isDeleted()) {
            sb.append("deleted ");

        }

        if (drug.getOutsideProviderName() != null && !drug.getOutsideProviderName().equals("")) {
            sb = new StringBuilder("external ");
        }
        if (drug.getRemoteFacilityName() != null) {
            sb = new StringBuilder("external ");
        }
        return sb.toString().trim();

    }
    
    String displayDrugReason(CodingSystemManager codingSystemManager, List<DrugReason> drugReasons, boolean title) {
        StringBuilder sb = new StringBuilder();
        boolean multiLoop = false;

        for (DrugReason drugReason : drugReasons) {
            if (multiLoop) {
                sb.append(", ");
            }
            String codeDescr = null;
            if (drugReason.getCodingSystem() != null && !drugReason.getCodingSystem().isEmpty()) {
                codeDescr = codingSystemManager.getCodeDescription(drugReason.getCodingSystem(), drugReason.getCode());
            }
            if (codeDescr != null) {
                sb.append(Encode.forHtml(codeDescr));
            } else {
                sb.append(drugReason.getCode());
            }
            multiLoop = true;
        }
        if (sb.toString().equals("")) {
            if (title) {
                return "No diseases are associated with this medication";
            }
            return "+";
        }

        return sb.toString();
    }

%>
