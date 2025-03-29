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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.Misc"%>
<%@page import="oscar.util.UtilMisc"%>
<%@include file="/casemgmt/taglibs.jsp"%>
<%@taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@page import="java.util.Enumeration"%>
<%@page import="oscar.oscarEncounter.pageUtil.NavBarDisplayDAO"%>
<%@page	import="java.util.Arrays,java.util.Properties,java.util.List,java.util.Set,java.util.ArrayList,java.util.Enumeration,java.util.HashSet,java.util.Iterator,java.text.SimpleDateFormat,java.util.Calendar,java.util.Date,java.text.ParseException"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.common.model.UserProperty,org.oscarehr.casemgmt.model.*,org.oscarehr.casemgmt.service.* "%>
<%@page import="org.oscarehr.casemgmt.web.formbeans.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="oscar.util.DateUtils"%>
<%@page import="org.oscarehr.documentManager.EDocUtil"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.casemgmt.common.Colour"%>
<%@page import="org.oscarehr.documentManager.EDoc"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.quatro.dao.security.*,com.quatro.model.security.Secrole"%>
<%@page import="org.oscarehr.util.EncounterUtil"%>
<%@page import="org.apache.cxf.common.i18n.UncheckedException"%>
<%@page import="org.oscarehr.casemgmt.web.NoteDisplay"%>
<%@page import="org.oscarehr.casemgmt.web.CaseManagementViewAction"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="oscar.oscarRx.data.RxPrescriptionData"%>
<%@page import="org.oscarehr.casemgmt.dao.CaseManagementNoteLinkDAO"%>
<%@page import="org.oscarehr.common.dao.ProfessionalSpecialistDao"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="oscar.util.UtilDateUtilities"%>
<%@page import="org.oscarehr.casemgmt.web.NoteDisplayNonNote"%>
<%@page import="org.oscarehr.common.dao.EncounterTemplateDao"%>
<%@page import="org.oscarehr.casemgmt.web.CheckBoxBean"%>
<%@page import="org.oscarehr.common.model.CasemgmtNoteLock"%>
<%@page import="org.oscarehr.common.model.EmailLog"%>
<%@page import="org.oscarehr.managers.EmailManager"%>
<%@ page import="org.owasp.encoder.Encode" %>


<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_casemgmt.notes" rights="r" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_casemgmt.notes");%>
</security:oscarSec>
<%
	if(!authed2) {
		return;
	}
%>

<%!
	CaseManagementManager caseManagementManager = SpringUtils.getBean(CaseManagementManager.class);
%>

<%
String ctx = request.getContextPath();

LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
Facility facility = loggedInInfo.getCurrentFacility();
ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao)SpringUtils.getBean(ProfessionalSpecialistDao.class);

EmailManager emailManager = SpringUtils.getBean(EmailManager.class);

String pId = (String)session.getAttribute("case_program_id");
Program program = null;
if (pId == null) {
    pId = "";
} else {
    ProgramDao programDao=(ProgramDao)SpringUtils.getBean(ProgramDao.class);
    program = programDao.getProgram(Integer.valueOf(pId));
}

String demographicNo = request.getParameter("demographicNo");
oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
String strBeanName = "casemgmt_oscar_bean" + demographicNo;
if ((bean = (oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute(strBeanName)) == null)
{
	response.sendRedirect("error.jsp");
	return;
}

String provNo = bean.providerNo;

String dateFormat = "dd-MMM-yyyy H:mm";
String anotherDateFormat = "dd-MM-yyyy";
long savedId = 0;
boolean found = false;
String bgColour;
ArrayList<Integer> lockedNotes = new ArrayList<Integer>();
ArrayList<Integer> unLockedNotes = new ArrayList<Integer>();
ArrayList<Integer> unEditableNotes = new ArrayList<Integer>();

@SuppressWarnings("unchecked")
ArrayList<NoteDisplay> notesToDisplay = (ArrayList<NoteDisplay>)request.getAttribute("notesToDisplay");
int noteSize = notesToDisplay.size();

SimpleDateFormat jsfmt = new SimpleDateFormat("MMM dd, yyyy");
Date dToday = new Date();
String strToday = jsfmt.format(dToday);

String frmName = "caseManagementEntryForm" + demographicNo;
CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean)session.getAttribute(frmName);

if (request.getParameter("caseManagementEntryForm") == null)
{
	request.setAttribute("caseManagementEntryForm", cform);
}

Integer offset = Integer.parseInt(request.getParameter("offset"));
int maxId = 0;

//We determine the lock status of the note
CasemgmtNoteLock casemgmtNoteLock = (CasemgmtNoteLock)session.getAttribute("casemgmtNoteLock"+demographicNo);
%>

<c:if test="${not empty notesToDisplay}">
	<%
		int idx = 0;

		//Notes list will contain all notes including most recently saved
		//we need to skip this one when displaying

		//if we're editing a note, check to see if it is locked
		//
		if (cform.getCaseNote().getId() != null)
		{		    
			savedId = cform.getCaseNote().getId();
		}

		//Check user property for stale date and show appropriately
		UserProperty uProp = (UserProperty)request.getAttribute(UserProperty.STALE_NOTEDATE);

		Date dStaleDate = null;
		int numToDisplay = Integer.MAX_VALUE;
		int numDisplayed = 0;
		Calendar cal = Calendar.getInstance();
		if (uProp != null)
		{
			String strStaleDate = uProp.getValue();
			if (strStaleDate.equalsIgnoreCase("A"))
			{
				cal.set(0, 1, 1);
			}
			else if(strStaleDate.equalsIgnoreCase("0"))
			{
				cal.add(Calendar.MONTH,1);
			}
			else
			{
				int pastMths = Integer.parseInt(strStaleDate);
				cal.add(Calendar.MONTH, pastMths);
			}

		}
		else
		{
			cal.add(Calendar.YEAR, -1);
		}

		dStaleDate = cal.getTime();

		String noteStr;
		int length;

		/*
		 *  Cycle through notes starting from the most recent and marking them for full inclusion or one line display
		 *  Need to do this now as we only count face to face encounters against limit of how many to fully display
		 *  If no user preference, show at most five face to face encounter notes
		 *  Else show all notes withing the user preference
		*/
		ArrayList<Boolean> fullTxtFormat = new ArrayList<Boolean>(noteSize);
		int pos;
		idx = 0;

		for (pos = noteSize - 1; pos >= 0; --pos)
		{
			NoteDisplay cmNote = notesToDisplay.get(pos);

			if (cmNote.isCpp())
			{
				fullTxtFormat.add(Boolean.FALSE);
				continue;
			}

			if (cmNote.isEmailNote())
			{
				fullTxtFormat.add(Boolean.TRUE);
				continue;
			}

			if( cmNote.getObservationDate() == null ) {
				fullTxtFormat.add(Boolean.FALSE);
				continue;
			}

			if (noteSize > numToDisplay)
			{
				if (uProp == null)
				{
					if (numDisplayed < numToDisplay && cmNote.getObservationDate().compareTo(dStaleDate) >= 0)
					{
						fullTxtFormat.add(Boolean.TRUE);

						if (EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue().equalsIgnoreCase(cmNote.getEncounterType()))
						{
							++numDisplayed;
						}
					}
					else
					{
						fullTxtFormat.add(Boolean.FALSE);
					}
				}
				else
				{
					if (cmNote.getObservationDate().compareTo(dStaleDate) >= 0)
					{
						fullTxtFormat.add(Boolean.TRUE);
					}
					else
					{
						fullTxtFormat.add(Boolean.FALSE);
					}
				}
			}
			else
			{
				if (cmNote.getObservationDate().compareTo(dStaleDate) >= 0)
				{
					fullTxtFormat.add(Boolean.TRUE);
				}
				else
				{
					fullTxtFormat.add(Boolean.FALSE);
				}
			}
		} //end of for loop

		boolean fulltxt;
		pos = noteSize - 1;

		String issuesToHide = OscarProperties.getInstance().getProperty("encounter.hide_notes_with_issue","");
		String[] is =issuesToHide.split(",");

		boolean remoteCapableProfessionalSpecialists = professionalSpecialistDao.hasRemoteCapableProfessionalSpecialists();
		
		int currentNcId = 0;
		String strCurrentNcId = null;
		// begin for loop for rendering notes
		for (idx = 0; idx < noteSize; ++idx)
		{

			NoteDisplay note = notesToDisplay.get(idx);
			noteStr = note.getNote();
			Integer noteId = note.getNoteId();
			EDoc doc = new EDoc();
			String dispDocNo = "";
			String dispFilename = "";
			String dispStatus = " ";
			String globalNoteId = "";
			
			if (note.getRemoteFacilityId() != null) {
				globalNoteId = "UUID" + note.getUuid();
			}
			
			if (noteId!=null)
			{			    
			    globalNoteId = note.getNoteId().toString();
			    
				if (note.isDocument()) {
				    
				    globalNoteId = "DOC" + note.getNoteId();
					doc = EDocUtil.getDocFromNote((long)noteId.intValue());
					
					if (doc != null)
					{
						dispDocNo = doc.getDocId();
						dispFilename = doc.getFileName();
						Character status = doc.getStatus();

						if (status == 'A')
						{
							dispStatus = "active";
						}
						//find docname, docno and docstatus
					}
				} else if (note.isEformData()) {												
					globalNoteId = "EFORM" + note.getNoteId();
				} else if (note.isInvoice()) {
					globalNoteId = "INV" + note.getNoteId();
				} else if (note.isEmailNote()) {
					EmailLog emailLog = emailManager.getEmailLogByCaseManagementNoteId(loggedInInfo, Long.valueOf(noteId));
					if (emailLog == null) { continue; }
					dispDocNo = String.valueOf(emailLog.getId());
				}
			}

			noteStr = Encode.forHtmlContent(noteStr);
			// for remote notes, the full text is always shown.
			fulltxt = fullTxtFormat.get(pos) || note.getRemoteFacilityId()!=null;
			--pos;
			bgColour = CaseManagementViewAction.getNoteColour(note);
			if (fulltxt)
			{
				noteStr = noteStr.replaceAll("\n", "<br>");
			}
			else
			{
				length = noteStr.length() > 50?50:noteStr.length();
				noteStr = noteStr.substring(0, length);
			}

			boolean editWarn = !note.isSigned() && !note.getProviderNo().equals(provNo);
			boolean hideCppNotes = OscarProperties.getInstance().isPropertyActive("encounter.hide_cpp_notes");
			boolean hideDocumentNotes = OscarProperties.getInstance().isPropertyActive("encounter.hide_document_notes");
			boolean hideEformNotes = OscarProperties.getInstance().isPropertyActive("encounter.hide_eform_notes");
			//boolean hideMetaData = OscarProperties.getInstance().isPropertyActive("encounter.hide_metadata");
			boolean hideInvoices = OscarProperties.getInstance().isPropertyActive("encounter.hide_invoices");
			
			String noteDisplay = "block";
			if(note.isCpp() && hideCppNotes) {
				noteDisplay="none";
			}
			if(note.isDocument() && hideDocumentNotes) {
				noteDisplay="none";
			}
			if(note.isEformData() && hideEformNotes) {
				noteDisplay="none";
			}
			
			if(note.isInvoice() && hideInvoices) {
				noteDisplay="none";
			}

			if(!noteDisplay.equals("none") && issuesToHide.length()>0) {
				for(String i:is) {
					if(note.containsIssue(i)) {
						noteDisplay="none";
						break;
					}
				}
			}
			
			strCurrentNcId = offset.toString() + String.valueOf(idx+1);
			currentNcId = Integer.parseInt(strCurrentNcId);
			
			if( currentNcId > maxId ) {
			    maxId = currentNcId;
			}

			//String metaDisplay = (hideMetaData)?"none":"block";
			
			String noteIdAttribute = new StringBuilder("nc").append(offset > 0 ? offset : "").append(idx+1).toString();
			boolean isMagicNote = note.isDocument() || note.isCpp() || note.isEformData() || note.isEncounterForm() || note.isInvoice();
			String noteClassAttribute = new StringBuilder("note").append(isMagicNote ? "" : " noteRounded encounter-note").toString();
		%>
		
		<%
			String cursorStyle = (note.isCpp()) ? "cursor: pointer;" : "";
		%>
		<div id="<%=noteIdAttribute%>" 
			 style="display: <%= noteDisplay %>; <%= cursorStyle %>" 
			 class="<%=noteClassAttribute%>">
			 
			<input type="hidden" id="signed<%=globalNoteId%>" value="<%=note.isSigned()%>" />
			<input type="hidden" id="full<%=globalNoteId%>" value="<%=fulltxt || (note.getNoteId() !=null && note.getNoteId().equals(savedId))%>" />
			<input type="hidden" id="bgColour<%=globalNoteId%>" value="<%=bgColour%>" /> 
			<input type="hidden" id="editWarn<%=globalNoteId%>" value="<%=editWarn%>" />
			<%
			if (note.isEmailNote()) {
			%>
				<input type="hidden" id="emailNote<%=globalNoteId%>" value="true" /> 
			<%
			}
			%>

	  		<div id="n<%=globalNoteId%>" class="note-contents">
			<%
				//display last saved note for editing
				if (note.getNoteId()!=null && !"".equals(note.getNoteId()) && note.getNoteId().intValue() == savedId )
				{
					found = true;
					%>
						<script>
							savedNoteId=<%=note.getNoteId()%>;
						</script>
						<%
 						if (OscarProperties.getInstance().getBooleanProperty("note_program_ui_enabled", "true")) {
 						%>
 						<script>
 							_setupNewNote();
 						</script>
 						<% } %>

				    <textarea tabindex="7" cols="84" rows="10" class="txtArea boxsizingBorder <%= note.isSigned() ? "" : "unsigned-textarea"%>" wrap="soft" style="line-height: 1.1em;" name="caseNote_note" id="caseNote_note<%=savedId%>"><%=cform.getCaseNote_note()%></textarea>
						
						<div class="sig <%= note.isSigned() ? "" : "note-unsigned"%>" id="sig<%=globalNoteId%>">
							<%@ include file="noteIssueList.jsp"%>
						</div>

						<c:if test="${sessionScope.passwordEnabled=='true'}">
							<div id='notePasswd'>

								<input type="hidden" id="caseNote.password" name="caseNote.password" value="" autocomplete="off" />

								<input type='hidden' id="caseNote.passwordConfirm" name='caseNote.passwordConfirm' value="" autocomplete="off" />

							</div>
						</c:if>
					<%
		 		}
				else //else display contents of note for viewing
				{
					/*
					 * automatically unlock notes for the loggedin user
					 * trying to short circuit by loggedin provider number match in order to
					 * save processing of unlocking for loggedin provider
					 * It was done this way because refactoring the code to cover various injection
					 * hacks would have taken too much time.
					 * Methods note.getProviderNo() and note.isLocked are a little sketchy. The CaseManagementManager method
					 * will reconcile any attempts to hack
					 *
					 * Sorry everybody :(
					 */

					// Lock note from view, if the note is locked and does not belong to the logged in user.
					if (! loggedInInfo.getLoggedInProviderNo().equals(note.getProviderNo()) && note.isLocked())
					{
					%>

						<div class="alert alert-status locked-note" role="alert" id="txt<%=globalNoteId%>">
							<div>

								<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-lock2" viewBox="0 0 16 16">
									<path d="M8 5a1 1 0 0 1 1 1v1H7V6a1 1 0 0 1 1-1m2 2.076V6a2 2 0 1 0-4 0v1.076c-.54.166-1 .597-1 1.224v2.4c0 .816.781 1.3 1.5 1.3h3c.719 0 1.5-.484 1.5-1.3V8.3c0-.627-.46-1.058-1-1.224"></path>
									<path d="M4 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zm0 1h8a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1"></path>
								</svg>
							</div>

							<div>
								<bean:message key="oscarEncounter.Index.msgLocked" />
								<%=Encode.forHtmlContent(note.getProviderName()) + " " + DateUtils.getDate(note.getUpdateDate(), dateFormat, request.getLocale())%>
							</div>
							<div>
								<a href="javascript:void(0)" class="unlock-note" style="color:grey;" data-action="unlock" onclick="unlockNote('n<%=globalNoteId%>', this)">
									unlock
								</a>
							</div>

						</div>
					<%}

					/* otherwise; proceed to display the note if the note is NOT locked
					 * OR if the note belongs to the loggedin user and IS locked; then display the
					 * note only if the note can be unlocked with the loggedin user password.
					 */
					else if( ! note.isLocked() ||
							(loggedInInfo.getLoggedInProviderNo().equals(note.getProviderNo())
							&& caseManagementManager.unlockNoteForLoggedinUser(loggedInInfo, note.getNoteId()))
					) {

						%>
			            <div class="note-control-panel">
				            <%
				            /*
				            * if this is a note locked by the loggedin user and is now being displayed only
				            * for the authorized user, then add a heading to display the lock status of the
				            * note.
				            */
				            if(note.isLocked() && loggedInInfo.getLoggedInProviderNo().equals(note.getProviderNo())) {
				            %>
				            <div class="locked-note-control" title="Note is locked for other users">
					            <div>
						            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-lock2" viewBox="0 0 16 16">
							            <path d="M8 5a1 1 0 0 1 1 1v1H7V6a1 1 0 0 1 1-1m2 2.076V6a2 2 0 1 0-4 0v1.076c-.54.166-1 .597-1 1.224v2.4c0 .816.781 1.3 1.5 1.3h3c.719 0 1.5-.484 1.5-1.3V8.3c0-.627-.46-1.058-1-1.224"></path>
							            <path d="M4 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zm0 1h8a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1"></path>
						            </svg>
					            </div>
					            <div>
						            note locked for other users
					            </div>
					            <div>
						            <a href="javascript:void(0)" class="unlock-note" style="color:grey;" data-action="unlock" onclick="unlockNote('n<%=globalNoteId%>', this)">
							            unlock
						            </a>
					            </div>
				            </div>
				            <% } %>
				            <div class="note-controls">
				            <%
						String rev = note.getRevision();
						if (note.getRemoteFacilityId()==null) // always display full note for remote notes
						{
							if (note.isDocument() || note.isCpp() || note.isEformData() || note.isEncounterForm() || note.isInvoice() || note.isEmailNote())
							{
								// blank if so it never displays min/max icon for documents
							}
							else if (fulltxt)
							{
							%>
			                    <div class="note-control expand-collapse" title="<bean:message key="oscarEncounter.MinDisplay.title"/>" id='quitImg<%=globalNoteId%>' data-state="expanded" onclick="toggleView(this)">
								    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chevron-bar-contract" viewBox="0 0 16 16">
									    <path fill-rule="evenodd" d="M3.646 14.854a.5.5 0 0 0 .708 0L8 11.207l3.646 3.647a.5.5 0 0 0 .708-.708l-4-4a.5.5 0 0 0-.708 0l-4 4a.5.5 0 0 0 0 .708m0-13.708a.5.5 0 0 1 .708 0L8 4.793l3.646-3.647a.5.5 0 0 1 .708.708l-4 4a.5.5 0 0 1-.708 0l-4-4a.5.5 0 0 1 0-.708M1 8a.5.5 0 0 1 .5-.5h13a.5.5 0 0 1 0 1h-13A.5.5 0 0 1 1 8"></path>
								    </svg>
			                    </div>
							<%
		 					}
							else
							{
							%>
			                    <div class="note-control expand-collapse" title="<bean:message key="oscarEncounter.MaxDisplay.title"/>" id='quitImg<%=globalNoteId%>' data-state="contracted" onclick="toggleView(this)">
							    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chevron-bar-expand" viewBox="0 0 16 16">
								    <path fill-rule="evenodd" d="M3.646 10.146a.5.5 0 0 1 .708 0L8 13.793l3.646-3.647a.5.5 0 0 1 .708.708l-4 4a.5.5 0 0 1-.708 0l-4-4a.5.5 0 0 1 0-.708m0-4.292a.5.5 0 0 0 .708 0L8 2.207l3.646 3.647a.5.5 0 0 0 .708-.708l-4-4a.5.5 0 0 0-.708 0l-4 4a.5.5 0 0 0 0 .708M1 8a.5.5 0 0 1 .5-.5h13a.5.5 0 0 1 0 1h-13A.5.5 0 0 1 1 8"></path>
							    </svg>
			                    </div>
							<%
							}
						}

						if (note.getRemoteFacilityId()!=null) // if it's a remote note, say where if came from on the top of the note
						{
					 	%>
						 	<div class="note-control">
						 		<bean:message key="oscarEncounter.noteFrom.label" />&nbsp;<%=note.getLocation()%>,<%=note.getProviderName()%>
						 	</div>
						<%
						}

						if (note.isGroupNote()) // if it's a remote note, say where if came from on the top of the note
						{
					 	%>
						 	<div class="note-control">
						 		Group Note - Editable note in this <a  href="javascript:void(0)" onClick="popupPage(700,1000,'Master1','<%=request.getContextPath()%>/demographic/demographiccontrol.jsp?demographic_no=<%=note.getLocation() %>&displaymode=edit&dboperation=search_detail');return false;">client</a>
						 	</div>
						<%
						}

						if (!note.isDocument() && !note.isCpp() && !note.isEformData() && !note.isEncounterForm() && !note.isInvoice() && !note.isEmailNote())
						{

					 	%>
<%--						 	<img title="<bean:message key="oscarEncounter.print.title"/>" id='print<%=globalNoteId%>' --%>
<%--						         alt="<bean:message key="oscarEncounter.togglePrintNote.title"/>" onclick="togglePrint('<%=globalNoteId%>'   , event)" --%>
<%--						         style='float: right; margin-right: 5px; margin-top: 2px;' src='<%=ctx %>/oscarEncounter/graphics/printer.png' />--%>
			                <div class="note-control" title="<bean:message key="oscarEncounter.print.title"/>" id='print<%=globalNoteId%>' onclick="togglePrint('<%=globalNoteId%>', this)">
				                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-printer" viewBox="0 0 16 16">
					                <path d="M2.5 8a.5.5 0 1 0 0-1 .5.5 0 0 0 0 1"></path>
					                <path d="M5 1a2 2 0 0 0-2 2v2H2a2 2 0 0 0-2 2v3a2 2 0 0 0 2 2h1v1a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2v-1h1a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2h-1V3a2 2 0 0 0-2-2zM4 3a1 1 0 0 1 1-1h6a1 1 0 0 1 1 1v2H4zm1 5a2 2 0 0 0-2 2v1H2a1 1 0 0 1-1-1V7a1 1 0 0 1 1-1h12a1 1 0 0 1 1 1v3a1 1 0 0 1-1 1h-1v-1a2 2 0 0 0-2-2zm7 2v3a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1v-3a1 1 0 0 1 1-1h6a1 1 0 0 1 1 1"></path>
				                </svg>
			                </div>
						<%
						}

					 	if (!note.isDocument() && !note.isRxAnnotation())
					 	{
					 		// only allow editing for local notes
					 		// also disallow editing of cpp's inline (can be edited in the cpp area)
					 		if (note.getRemoteFacilityId()==null && !note.isCpp() && !note.isEformData() && !note.isEncounterForm() && !note.isInvoice() && !note.isEmailNote())
							{
					 			if(!note.isReadOnly())
					 			{
						 		%>
								    <div class="note-control" title="<bean:message key="oscarEncounter.edit.msgEdit"/>" id="edit<%=globalNoteId%>" onclick="<%=editWarn?"noPrivs(this)":"editNote(this)"%>;return false;">
									    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">
										    <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"></path>
										    <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"></path>
									    </svg>
								    </div>
								<%
								}

					 			if (remoteCapableProfessionalSpecialists)
					 			{
					 			%>
					 				<a href="javascript:void(0)" class="note-control" onclick="window.open('<%=request.getContextPath()+"/lab/CA/ALL/sendOruR01.jsp?noteId="+globalNoteId%>', 'eSend');return(false);"
								       title="<bean:message key="oscarEncounter.eSendTitle"/>" style="float: right; margin-right: 5px;"><bean:message key="oscarEncounter.eSend" /></a>
					 			<%
					 			}
					 		}
					 	}
					 	else if(note.isRxAnnotation())//prescription note
					 	{
	                        String winName="dummie";
	                        int hash = Math.abs(winName.hashCode());
	                        //get drug from note id.
	                        RxPrescriptionData.Prescription rx=note.getRxFromAnnotation(note.getNoteLink());

	                        if (note.getRemoteFacilityId()==null) // only allow editing for local notes
							{
	                      		if(!note.isReadOnly())
	                      		{
								%>
									    <div class="note-control" title="<bean:message key="oscarEncounter.edit.msgEdit"/>" id="edit<%=globalNoteId%>" onclick="<%=editWarn?"noPrivs(this)":"editNote(this)"%>;return false;">
										    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">
											    <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"></path>
											    <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"></path>
										    </svg>
									    </div>
						 		<%
								}
	                   		}

		                    if(rx!=null)
	       		            {
	               		        String url="popupPage(700,800,'" + hash + "', '" + request.getContextPath() + "/oscarRx/StaticScript2.jsp?demographicNo=" + rx.getDemographicNo() + "&regionalIdentifier="+rx.getRegionalIdentifier()+"&cn="+response.encodeURL(rx.getCustomName())+"');";
		                        %>
			                <div class="view-links" style="<%=(note.isDocument()||note.isCpp()||note.isEformData()||note.isEncounterForm()||note.isInvoice())?(bgColour):""%>">
		                        	<a class="links" title="<%=rx.getSpecial()%>" id="view<%=globalNoteId%>" href="javascript:void(0);" onclick="<%=url%>" style="float: right; margin-right: 5px; "> <bean:message key="oscarEncounter.view.rxView" /> </a>
			                </div>
				        <%
	                        }
		                }
						else if (note.isDocument() && !note.getProviderNo().equals("-1"))
						{
							//document annotation
							String url;

							Enumeration em = request.getAttributeNames();

							String winName = "docs" + demographicNo;
							int hash = Math.abs(winName.hashCode());

							url = "popupPage(700,800,'" + hash + "', '" + request.getContextPath() + "/documentManager/showDocument.jsp?inWindow=true&segmentID=" + dispDocNo + "&providerNo=" + provNo + "');";
							url = url + "return false;";

							String editUrl = "window.open('/oscar/annotation/annotation.jsp?display=Documents&amp;table_id=" + dispDocNo + "&amp;demo=" + demographicNo + "','anwin','width=400,height=500');";

							if (note.getRemoteFacilityId()==null) // only allow editing for local notes
							{
								if(!note.isReadOnly())
								{
								%>
							 		<a title="<bean:message key="oscarEncounter.edit.msgEdit"/>" id="edit<%=globalNoteId%>"
							 		href="javascript:void(0);" onclick="<%=editUrl%> return false;" style="<%=bgColour%> order: 1; padding: 2px 5px;">
							 		<bean:message key="oscarEncounter.edit.msgEdit" />
							 		</a>
						 		<%
								}
							}
			 				%>
			                <div class="view-links" style="<%=(note.isDocument()||note.isCpp()||note.isEformData()||note.isEncounterForm()||note.isInvoice())?(bgColour):""%>">
								<a class="links" title="<bean:message key="oscarEncounter.view.docView"/>" id="view<%=globalNoteId%>" href="javascript:void(0)" onclick="<%=url%>" style="float: right;"> <bean:message key="oscarEncounter.view" /> </a>
			                </div>
				        <%
			 			}
						else
						{ //document note
							String url;

							Enumeration em = request.getAttributeNames();
							String winName = "docs" + demographicNo;
							int hash = Math.abs(winName.hashCode());

							url = "popupPage(700,800,'" + hash + "', '" + request.getContextPath() + "/documentManager/showDocument.jsp?inWindow=true&segmentID=" + dispDocNo + "&providerNo=" + provNo + "');";
							url = url + "return false;";
						 	%>
			                <div class="view-links" style="<%=(note.isDocument()||note.isCpp()||note.isEformData()||note.isEncounterForm()||note.isInvoice())?(bgColour):""%>">
							 	<a class="links" title="<bean:message key="oscarEncounter.view.docView"/>" id="view<%=globalNoteId%>" href="javascript:void(0);" onclick="<%=url%>" >
							 		<bean:message key="oscarEncounter.view" />
								</a>
			                </div>
							<%
					 	}

					 	if (note.isEformData())
						{
							String winName = "eforms"+demographicNo;
							int hash = Math.abs(winName.hashCode());
							String url = "popupPage(700,800,'"+hash+"','"+request.getContextPath()+"/eform/efmshowform_data.jsp?appointment=' + appointmentNo + '&fdid=";

							CaseManagementNoteLink noteLink = note.getNoteLink();
							if (noteLink!=null) url += noteLink.getTableId();
							else url+=note.getNoteId();

							url += "'); return false;";
							%>
			                <div class="view-links" style="<%=(note.isDocument()||note.isCpp()||note.isEformData()||note.isEncounterForm()||note.isInvoice())?(bgColour):""%>">
								<a class="links" title="<bean:message key="oscarEncounter.view.eformView"/>" id="view<%=globalNoteId%>" href="javascript:void(0)" onclick="<%=url%>"> <bean:message key="oscarEncounter.view" /> </a>
			                </div>
				                <%
						} else if (note.isInvoice()) {
							String winName = "invoice"+demographicNo;
							int hash = Math.abs(winName.hashCode());
							String url = "popupPage(700,800,'"+hash+"','"+request.getContextPath()+StringEscapeUtils.escapeHtml(((NoteDisplayNonNote)note).getLinkInfo())+"'); return false;";
							%>
			                <div class="view-links" style="<%=(note.isDocument()||note.isCpp()||note.isEformData()||note.isEncounterForm()||note.isInvoice())?(bgColour):""%>">
								<a class="links" title="<bean:message key="oscarEncounter.view.eformView"/>" id="view<%=globalNoteId%>" href="javascript:void(0)" onclick="<%=url%>" > <bean:message key="oscarEncounter.view" /> </a>
			                </div>
				        <%
						} else if (note.isEncounterForm()) {
							NoteDisplayNonNote formEntry = (NoteDisplayNonNote) note;
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat(anotherDateFormat);
							String createdDate = "";
							if (formEntry.getCreated() != null) { createdDate = simpleDateFormat.format(formEntry.getCreated()); }
							String winName = formEntry.getNote().trim() + demographicNo + createdDate;
							int hash = Math.abs(winName.hashCode());
							String url = "popupPage(700,800,'"
											+hash+"started"+"','"
											+request.getContextPath()
											+ StringEscapeUtils.escapeHtml("/form/forwardshortcutname.do?formname=" + formEntry.getNote())
											+ "&demographic_no=" + demographicNo
											+ "&formId=" + formEntry.getNoteId()
											+"'); return false;";
							%>
			                <div class="view-links" style="<%=(note.isDocument()||note.isCpp()||note.isEformData()||note.isEncounterForm()||note.isInvoice())?(bgColour):""%>">
								<a class="links" title="<bean:message key="oscarEncounter.view.eformView"/>" id="view<%=globalNoteId%>" href="javascript:void(0)" onclick="<%=url%>"><bean:message key="oscarEncounter.view" /></a>
			                </div>
				        <%
						} else if (note.isEmailNote()) {
							String url = "viewEmailByLogId(1100,1000,'" + request.getContextPath() + "/admin/ManageEmails.do?method=resendEmail&logId=" + dispDocNo + "');" + "return false;";
							if (fulltxt) {
								%>
									<img title='Minimize Display' id='quitImg<%=globalNoteId%>'
									     alt='Minimize Display' onclick='minNonEditableNoteView(<%=globalNoteId%>)'
									     src='<%=ctx %>/oscarEncounter/graphics/triangle_up.gif'>
								<%
							} else {
								%>

									<img title="<bean:message key="oscarEncounter.MaxDisplay.title"/>"
									     id='fullImg<%=globalNoteId%>'
									     alt="Maximize Display" onclick="fullView(event)"
									     src='<%=ctx %>/oscarEncounter/graphics/triangle_down.gif' />

								<%

							}
						 	%>
								<div class="view-links" style="float: right; <%=(isMagicNote)?(bgColour):""%>">
							 		<a class="links" title="<bean:message key="oscarEncounter.view.docView"/>" id="view<%=globalNoteId%>" href="javascript:void(0);" onclick="<%=url%>" ><bean:message key="oscarEncounter.view" />					</a>
								</div>
							<%
						}
					 	if (!note.isDocument() && !note.isCpp() && !note.isEformData() && !note.isEncounterForm() && !note.isInvoice() && !note.isEmailNote()) {
					 		String atbname = "anno" + String.valueOf(new Date().getTime());
					 		String addr = request.getContextPath() + "/annotation/annotation.jsp?atbname=" + atbname + "&table_id=" + String.valueOf(note.getNoteId()) + "&display=EChartNote&demo=" + demographicNo;
						%>

<%--							<input type="image" id="anno<%=globalNoteId%>" src='<%=ctx %>/oscarEncounter/graphics/annotation.png' --%>
<%--							       title='<bean:message key="oscarEncounter.Index.btnAnnotation"/>' style="float: right; margin-right: 5px; margin-bottom: 3px; height:10px;width:10px" --%>
<%--							       onclick="window.open('<%=addr%>','anwin','width=400,height=500');$('annotation_attribname').value='<%=atbname%>'; return false;" />--%>
<%--			    --%>
			                <div class="note-control" id="anno<%=globalNoteId%>" title='<bean:message key="oscarEncounter.Index.btnAnnotation"/>' onclick="window.open('<%=addr%>','anwin','width=400,height=500');$('annotation_attribname').value='<%=atbname%>'; return false;" >
				                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chat-right-text" viewBox="0 0 16 16">
					                <path d="M2 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h9.586a2 2 0 0 1 1.414.586l2 2V2a1 1 0 0 0-1-1zm12-1a2 2 0 0 1 2 2v12.793a.5.5 0 0 1-.854.353l-2.853-2.853a1 1 0 0 0-.707-.293H2a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2z"></path>
					                <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5"></path>
				                </svg>
			                </div>

						<%}%>
				            </div>
					    </div> <!-- end note-control-panel -->
							<div id="wrapper<%=globalNoteId%>" style="<%=(note.isDocument()||note.isCpp()||note.isEformData()||note.isEncounterForm()||note.isInvoice())?(bgColour):""%>">
							<%-- render the note contents here --%>
			  				<div id="txt<%=globalNoteId%>" name="<%=(note.isCpp()||note.isEmailNote())?"expandableReadonlyNoteText":""%>">

		  						<%=noteStr%>
							</div> <!-- end of txt<%=globalNoteId%> -->
		  						<%
		  							if (note.isCpp()||note.isEformData()||note.isEncounterForm()||note.isInvoice())
		  							{
		  								%>
											<div id="observation<%=globalNoteId%>" style="display:ruby;">
													<label for="obs<%=globalNoteId%>"><bean:message key="oscarEncounter.encounterDate.title"/>:&nbsp;</label>
													<span id="obs<%=globalNoteId%>"><%=note.getObservationDate() != null ? DateUtils.getDate(note.getObservationDate(), dateFormat, request.getLocale()) : "N/A"%></span>
													<%
														if (note.isCpp())
														{
															%>
																&nbsp;
																<bean:message key="oscarEncounter.noteRev.title" />
															<%

															if (rev!=null)
															{
																if(globalNoteId.contains("EFORM")){
																	%>
																	 <a href="javascript:void(0)" onclick="return showHistory('<%=globalNoteId.replace("EFORM","")%>', event);"><%=rev%></a>
																	<%
																}else{
																	%>
																	 <a href="javascript:void(0)" onclick="return showHistory('<%=globalNoteId%>', event);"><%=rev%></a>
																	<%
																}
															}
															else
															{
																%>
																	N/A
																<%
															}
														}
													%>
											</div> <!-- end of observation<%=globalNoteId%> -->
		  								<%
		  							}
		  						%>
			  				</div> <!-- end of wrapper<%=globalNoteId%> -->
<%--						<%--%>

<%--			 			if (!note.isEmailNote() && largeNote(noteStr))--%>
<%--						{--%>
<%--			 			%>--%>
<%--						 	<img title="<bean:message key="oscarEncounter.MinDisplay.title"/>" id='bottomQuitImg<%=globalNoteId%>' alt="<bean:message key="oscarEncounter.MinDisplay.title"/>" onclick="minView(event)" style='float: right; margin-right: 5px; margin-bottom: 3px;'--%>
<%--							src='<%=ctx %>/oscarEncounter/graphics/triangle_up.gif' />--%>
<%--						<%--%>
<%--				 		}--%>
						<%
						if (!note.isDocument() && !note.isCpp() && !note.isEformData() && !note.isEncounterForm() && !note.isInvoice())
						{
						
							if (OscarProperties.getInstance().getBooleanProperty("note_program_ui_enabled", "true")) {
							%>
						 		<div class ="_program" noteId="<%=globalNoteId %>" programName="<%=note.getProgramName() %>" roleName="<%=note.getRoleName() %>">
						 			<span class="program"><%=note.getProgramName() %> (<%=note.getRoleName() %>)</span>
						 		</div>
							<%
							}
						%>						
							<div id="sig<%=globalNoteId%>" class="sig" style="<%=note.isEmailNote() || note.isRxAnnotation()?(bgColour):""%>">
								<div id="sumary<%=globalNoteId%>" style="<%=note.isEmailNote() || note.isRxAnnotation()?"color: #FFF !important":""%>">
									<div id="observation<%=globalNoteId%>" style="float: right; margin-right: 3px;">
											<label for="obs<%=globalNoteId%>"><bean:message key="oscarEncounter.encounterDate.title"/>:&nbsp;</label>
											<span id="obs<%=globalNoteId%>"><%=DateUtils.getDate(note.getObservationDate(), dateFormat, request.getLocale())%></span>&nbsp;
											<%if (!note.isEmailNote()) {%>
												<label for="history<%=globalNoteId%>"><bean:message key="oscarEncounter.noteRev.title" /></label>
												<%
													if (rev!=null)
													{
														%>
															<a href="javascript:void(0)" id="history<%=globalNoteId%>" onclick="return showHistory('<%=globalNoteId%>', event);"><%=rev%></a>
														<%
													}
													else
													{
														%>
															<span>N/A</span>
														<%
													}
												%>
											<%}%>
									</div>



									<%if (!note.isEmailNote()) {%>
									<div>
										<span style="float: left;"><bean:message key="oscarEncounter.editors.title" />:</span>
										<ul style="list-style: none inside none; margin: 0;">
											<%
												ArrayList<String> editorNames = note.getEditorNames();
												Iterator<String> it = editorNames.iterator();
												int count = 0;
												int MAXLINE = 2;
												while (it.hasNext())
												{
													String providerName = it.next();

													if (count % MAXLINE == 0)
													{
														out.print("<li>" + providerName + "; ");
													}
													else
													{
														out.print(providerName + "</li>");
													}
													if (it.hasNext()) ++count;
												}
												if (count % MAXLINE == 0) out.print("</li>");
											%>
										</ul>
									</div>
									<%}%>


									<%
									if(facility.isEnableEncounterTime() || (program != null && program.isEnableEncounterTime())) {
									%>
									<div style="clear: right; margin-right: 3px; float: right;">
										<bean:message key="oscarEncounter.encounterTime.title"/>:&nbsp;<span id="encTime<%=globalNoteId%>"><%=note.getEncounterTime()%></span>
									</div>
									<% } %>
									<%
									if(facility.isEnableEncounterTransportationTime() || (program != null && program.isEnableEncounterTransportationTime())) {
									%>
									<div style="clear: right; margin-right: 3px; float: right;">
										<bean:message key="oscarEncounter.encounterTransportation.title"/>:&nbsp;<span id="encTransTime<%=globalNoteId%>"><%=note.getEncounterTransportationTime()%></span>
									</div>
									<% } %>

									<%if (!note.isEmailNote()) {%>
									<div style="clear: right; margin-right: 3px; float: right;">
										<bean:message key="oscarEncounter.encType.title"/>:&nbsp;
										<span id="encType<%=globalNoteId%>"><%=note.getEncounterType().equals("")?"":"&quot;" + note.getEncounterType() + "&quot;"%></span>
									</div>

									<div>
										<span style="float: left;"><bean:message key="oscarEncounter.assignedIssues.title" /></span>
										<%
											ArrayList<String> issueDescriptions = note.getIssueDescriptions();

											if (issueDescriptions.size() > 0)
											{
												%>
													<ul style="float: left; list-style:none; margin: 0;">
														<%
															for (String issueDescription : issueDescriptions)
															{
																%>
																	<li><%=issueDescription.trim()%></li>
																<%
															}
														%>
													</ul>
												<%
											}
										%>
										<br style="clear: both;" />
									</div> <!-- end of assigned title -->
									<%}%>

									<%if (note.isEmailNote()) {%>
									<div>
										Email Note
									</div>
									<%}%>
								</div> <!-- end of div summary<%=globalNoteId%> -->
							</div> <!-- end of div sig<%=globalNoteId%> -->

			            <%

						} // end of if (!note.isDocument() && !note.isCpp() && !note.isEformData() && !note.isEncounterForm() && !note.isInvoice())

			    /*
			     * If the note is locked and cannot be displayed because authentication
			     * failed, then display an error.
			     * Only locked notes that belong to the loggedin user can get this far.
			     */
					} else if(note.isLocked() && ! caseManagementManager.unlockNoteForLoggedinUser(loggedInInfo, note.getNoteId())) { %>
					    <div class="alert alert-danger locked-note" role="alert" title="Cannot Unlock Note: try resetting the note lock password in preferences">
						    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-lock2" viewBox="0 0 16 16">
							    <path d="M8 5a1 1 0 0 1 1 1v1H7V6a1 1 0 0 1 1-1m2 2.076V6a2 2 0 1 0-4 0v1.076c-.54.166-1 .597-1 1.224v2.4c0 .816.781 1.3 1.5 1.3h3c.719 0 1.5-.484 1.5-1.3V8.3c0-.627-.46-1.058-1-1.224"></path>
							    <path d="M4 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zm0 1h8a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1"></path>
						    </svg>
						    Cannot Unlock Note: try resetting the password in your preferences.
					    </div> <!-- end note-contents -->
				<%}
		}%>

			</div><!-- end of div n<%=globalNoteId%> -->
		</div><!-- end of div <%=noteIdAttribute%> -->
		
<%--		<% if (request.getAttribute("moreNotes") != null && ((Boolean) request.getAttribute("moreNotes"))) { %>--%>
<%--		<script type="text/javascript">--%>
<%--		setupOneNote('<%=offset%><%=idx+1%>');--%>
<%--		</script>--%>
<%--		<% } %>--%>

<%
		//if we are not editing note, remember note ids for setting event listeners
		//Internet Explorer does not play nice with inserting javascript between divs
		//so we store the ids here and list the event listeners at the end of this script
		if (note.getNoteId()!=null && note.getNoteId() != savedId)
		{
			if (note.isLocked())
			{
				lockedNotes.add(note.getNoteId());
			}
			else if (!fulltxt && !note.isDocument() && !note.isEformData() && !note.isEncounterForm() && !note.isRxAnnotation() && !note.isInvoice() && !note.isEmailNote())
			{
				%><script> Element.observe('n<%=note.getNoteId()%>', 'click', fullView); </script><%
				unLockedNotes.add(note.getNoteId());
			}
		}

} //end for */
					%>
</c:if> <%-- END OF "not empty notesToDisplay" --%>


 <%
 	if (!found && request.getAttribute("moreNotes") == null) {
 		//if we didn't find note but savedId is > 0 then we have a note to edit which is not part of the quick chart
 		if( savedId > 0 ) {
 			found = true;
 		}

 			//savedId = 0;
 %>
	<div id="nc<%=offset%><%=savedId%>" class="note noteRounded encounter-note">
		<input type="hidden" id="signed<%=savedId%>" value="false" />
		<input type="hidden" id="full<%=savedId%>" value="true" />
		<input type="hidden" id="bgColour<%=savedId%>" value="color:#000000;background-color:#CCCCFF;" />
		<input type="hidden" id="editWarn<%=savedId%>" value="false" />
		<div id="n<%=savedId%>">
			 <textarea tabindex="7" cols="84" rows="10" class="txtArea boxsizingBorder" wrap="hard" style="line-height: 1.1em;" name="caseNote_note" id="caseNote_note<%=savedId%>"><%=cform.getCaseNote_note() %></textarea>
			<div class="sig" id="sig<%=savedId%>">
				<%@ include file="noteIssueList.jsp"%>
			</div> <!-- end of div sig<%=savedId%> -->

			<c:if test="${sessionScope.passwordEnabled=='true'}">
				<div id='notePasswd'>

<%--						<label for="caseNote.password.back">Password:</label>--%>
						<input type="hidden" id="caseNote.password.back" name="caseNote.password" value="" autocomplete="off" />

<%--						<label for="caseNote.passwordConfirm.back">Confirm:</label>--%>
						<input type='hidden' id="caseNote.passwordConfirm.back" name='caseNote.passwordConfirm' value="" autocomplete="off" />

			</div>
			</c:if>
		</div> <!-- end of div n<%=savedId%>  -->
	</div> <!-- end of div nc<%=offset%><%=savedId%> -->
	
	<% if (OscarProperties.getInstance().getBooleanProperty("note_program_ui_enabled", "true")) { %>
 	<script>
		_setupNewNote();
 	</script>
 	<% } %>

 	<%
	}
	%>	
	
<script type="text/javascript">
	maxNcId = <%=maxId%>;		
</script>


<% if (request.getAttribute("moreNotes") == null) { %>
<script type="text/javascript">	
	caseNote = "caseNote_note" + "<%=savedId%>";
	//save initial note to determine whether save is necessary
	origCaseNote = $F(caseNote);
<%

	if( casemgmtNoteLock.isLocked() ) {
    //note is locked so display message
%>
		alert("Another user is currently editing this note.  Please try again later.");
<%
	}
	else if( casemgmtNoteLock.isLockedBySameUser() && !casemgmtNoteLock.getSessionId().equals(request.getRequestedSessionId()) ) {
    	//note is locked by same user so offer to unlock note and view locked note in progress    	    
%>
		var viewEditedNote = confirm("You have started to edit this note in another window at <%=casemgmtNoteLock.getIpAddress()%>.\nDo you wish to continue?");
		if( viewEditedNote ) {	
			doscroll();
			var params = "method=updateNoteLock&demographicNo=" + demographicNo;
			jQuery.ajax({
				type: "POST",
				url:  "<%=ctx%>/CaseManagementEntry.do",
				data: params,
				success: function() {
					//force save when exiting chart in case we loaded edited note in other chart
					origCaseNote += ".";
					tmpSaveNeeded = true;
				}
			});
		}
		else {
			window.close();
		}
<%
	}
%>

	jQuery(document).ready(function() {

		<%
		String singleLineFormat="false";
    	UserProperty slProp = (UserProperty)request.getAttribute(UserProperty.STALE_FORMAT);
		if (slProp != null && slProp.getValue().equals("yes")) {
			singleLineFormat="true";
		}
		%>
		if('<%=singleLineFormat%>'=='true') {
    		var staleIds = new Array();

        	jQuery("img[id^='quitImg']").each(function(){
				if (jQuery(this).attr('src').indexOf('/oscarEncounter/graphics/triangle_down.gif')!=-1) {
					var iid = jQuery(this).attr('id');
					jQuery(this).trigger('click');
					staleIds.push(iid);
				}
        	});

			for (var i=0;i<staleIds.length;i++) {
				jQuery("#"+staleIds[i]).trigger('click');
			}
		}

	});

    document.forms["caseManagementEntryForm"].noteId.value = "<%=savedId%>";
    
    //are we editing existing note?  if not init newNoteIdx as we are dealing with a new note
   
   <%if (!bean.oscarMsg.equals(""))
			{%>
        $(caseNote).value +="\n\n<%=org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(bean.oscarMsg)%>";
   <%bean.reason = "";
				bean.oscarMsg = "";
			}

			if (request.getParameter("noteBody") != null)
			{
				String noteBody = request.getParameter("noteBody");
				noteBody = noteBody.replaceAll("<br>|<BR>", "\n");%>
        $(caseNote).value +="\n\n<%=org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(noteBody)%>";
   <%}

			if (found != true)
			{%>
        document.forms["caseManagementEntryForm"].newNoteIdx.value = <%=savedId%>;
   <%}
			else
			{%>
        document.forms["caseManagementEntryForm"].note_edit.value = "existing";
    <%}%>
    setupNotes();
    Element.observe(caseNote, "keyup", monitorCaseNote);
    Element.observe(caseNote, 'click', getActiveText);


//			Iterator<Integer> iterator = lockedNotes.iterator();
<%--			while (iterator.hasNext())--%>
<%--			{--%>
<%--				num = iterator.next();%>--%>
            <%--Element.observe('n<%=num%>', 'click', unlockNote);--%>
	<%
            Integer num;
            Iterator<Integer> iterator = unLockedNotes.iterator();
            while (iterator.hasNext())
            {
                num = iterator.next();%>
            Element.observe('n<%=num%>', 'click', fullView);
    <%}%>

    //flag for determining if we want to submit case management entry form with enter key pressed in auto completer text box
    var submitIssues = false;
   //AutoCompleter for Issues
<%--    <c:url value="/CaseManagementEntry.do?method=issueList&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}" var="issueURL" />--%>
<%--    let issueAutoCompleter = new Ajax.Autocompleter("issueAutocomplete", "issueAutocompleteList", "<c:out value="${issueURL}"/>", {minChars: 3, indicator: 'busy', afterUpdateElement: saveIssueId, onShow: autoCompleteShowMenu, onHide: autoCompleteHideMenu});--%>

    <%int MaxLen = 20;
			int TruncLen = 17;
			String ellipses = "...";
			for (int j = 0; j < bean.templateNames.size(); j++)
			{
				String encounterTmp = bean.templateNames.get(j);
				encounterTmp = oscar.util.StringUtils.maxLenString(encounterTmp, MaxLen, TruncLen, ellipses);
				encounterTmp = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(encounterTmp);%>
     autoCompleted["<%=encounterTmp%>"] = "ajaxInsertTemplate('<%=encounterTmp%>')";
     autoCompList.push("<%=encounterTmp%>");
     itemColours["<%=encounterTmp%>"] = "99CCCC";
   <%}%>
   //set default event for assigning issues
   //we do this here so we can change event listener when changing diagnosis
   var obj = { };
   makeIssue = "makeIssue";
   defaultDiv = "sig<%=savedId%>";
   changeIssueFunc;  //set in changeDiagnosis function above
   addIssueFunc = updateIssues.bindAsEventListener(obj, makeIssue, defaultDiv);
   Element.observe('asgnIssues', 'click', addIssueFunc);
   new Autocompleter.Local('enTemplate', 'enTemplate_list', autoCompList, { colours: itemColours, afterUpdateElement: menuAction }  );

   //start timer for autosave
   setTimer();

    reason = "<%=insertReason(request)%>";    //function defined bottom of file

    if(typeof messagesLoaded == 'function') {
 	     messagesLoaded('<%=savedId%>');
 	 }
    <%
	if (OscarProperties.getInstance().getBooleanProperty("note_program_ui_enabled", "true")) {
	%>
	_setupProgramList();
	<% } %>    

</script>

	<%
 	if (OscarProperties.getInstance().getBooleanProperty("note_program_ui_enabled", "true")) {
 	%>
 	<script type="text/javascript">
	jQuery("._program .program").unbind("click");
 	jQuery("._program .program").click(_noteProgramClick);
 	</script>
 	<% } %>
 	

<% } %>

<%!/*
																						 *Insert encounter reason for new note
																						 */
	protected String insertReason(HttpServletRequest request)
	{
		if(OscarProperties.getInstance().isPropertyActive("encounter.empty_new_note")) {
			return new String();
		}
		String encounterText = "";
		String apptDate = request.getParameter("appointmentDate");
		String reason = request.getParameter("reason");

		if( reason == null ) {
			reason = "";
		}

		if( apptDate == null || apptDate.equals("") || apptDate.equalsIgnoreCase("null") ) {
			encounterText = "\n[" + oscar.util.UtilDateUtilities.DateToString(new java.util.Date(), "dd-MMM-yyyy", request.getLocale()) + " .: " + reason + "] \n";
		}
		else {
			apptDate = convertDateFmt(apptDate);
			encounterText = "\n[" + apptDate + " .: " + reason + "]\n";
		}

		encounterText = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(encounterText);
		return encounterText;
	}

	protected String convertDateFmt(String strOldDate)
	{
		String strNewDate = "";
		if (strOldDate != null && strOldDate.length() > 0)
		{
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			try
			{

				Date tempDate = fmt.parse(strOldDate);
				strNewDate = new SimpleDateFormat("dd-MMM-yyyy").format(tempDate);

			}
			catch (ParseException ex)
			{
				MiscUtils.getLogger().error("Error", ex);
			}
		}

		return strNewDate;
	}

	protected boolean largeNote(String note)
	{
		final int THRESHOLD = 10;
		boolean isLarge = false;
		int pos = -1;

		for (int count = 0; (pos = note.indexOf("\n", pos + 1)) != -1; ++count)
		{
			if (count == THRESHOLD)
			{
				isLarge = true;
				break;
			}
		}

		return isLarge;
	}
%>
