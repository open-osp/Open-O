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
<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.*" %>
<%@ page import="oscar.oscarLab.ca.on.*" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.dao.ViewDao" %>
<%@ page import="org.oscarehr.common.model.View" %>
<%@ page import="org.oscarehr.common.model.TicklerLink" %>
<%@ page import="org.oscarehr.common.dao.TicklerLinkDao" %>
<%@ page import="oscar.MyDateFormat" %>
<%@ page import="oscar.OscarProperties" %>
<%@ page import="org.oscarehr.common.model.Site" %>
<%@ page import="org.oscarehr.common.dao.SiteDao" %>
<%@ page import="org.oscarehr.common.model.Tickler" %>
<%@ page import="org.oscarehr.common.model.TicklerComment" %>
<%@ page import="org.oscarehr.common.model.CustomFilter" %>
<%@ page import="org.oscarehr.managers.TicklerManager" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.Duration" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>

<%
	String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
	boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_tickler" rights="r" reverse="<%=true%>">
	<%authed = false; %>
	<%response.sendRedirect("../securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if (!authed) {
		return;
	}
%>
<%!
	TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
	TicklerLinkDao ticklerLinkDao = SpringUtils.getBean(TicklerLinkDao.class);
	ViewDao viewDao = SpringUtils.getBean(ViewDao.class);
%>

<%
	String labReqVer = oscar.OscarProperties.getInstance().getProperty("onare_labreqver", "07");
	if (labReqVer.equals("")) {
		labReqVer = "07";
	}

	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
	String user_no = (String) session.getAttribute("user");
	String createReport = request.getParameter("Submit");
	boolean doCreateReport = createReport != null && createReport.equals("Create Report");
	String userRole = (String) session.getAttribute("userrole");

	String demographic_no = request.getParameter("demoview");
	if (demographic_no == null || demographic_no.isEmpty()) {
		demographic_no = "0";
	}

	Map<String, View> ticklerView = viewDao.getView("tickler", userRole, user_no);

	String providerview = "all";
	if (! "0".equals(demographic_no)) {
		// do nothing
	} else if (ticklerView.get("providerview") != null && !doCreateReport) {
		providerview = ticklerView.get("providerview").getValue();
	} else if (request.getParameter("providerview") != null) {
		providerview = request.getParameter("providerview");
	}

	String assignedTo = "all";
	if (! "0".equals(demographic_no)){
		// do nothing
	} else if (ticklerView.get("assignedTo") != null && !doCreateReport) {
		assignedTo = ticklerView.get("assignedTo").getValue();
	} else if (request.getParameter("assignedTo") != null) {
		assignedTo = request.getParameter("assignedTo");
	}

	String mrpview = "all";
	if (! "0".equals(demographic_no)){
		// do nothing
	} else if (ticklerView.get("mrpview") != null && !doCreateReport) {
		mrpview = ticklerView.get("mrpview").getValue();
	} else if (request.getParameter("mrpview") != null) {
		mrpview = request.getParameter("mrpview");
	}

	String ticklerview = "A";
	View statusView = ticklerView.get("ticklerview");
	if (statusView != null && !doCreateReport) {
		ticklerview = statusView.getValue();
	} else if (request.getParameter("ticklerview") != null) {
		ticklerview = request.getParameter("ticklerview");
	}

	String xml_vdate = "";
//	View beginDateView = ticklerView.get("dateBegin");
//	if (beginDateView != null && !doCreateReport) {
//		xml_vdate = beginDateView.getValue();
//	} else
	if (request.getParameter("xml_vdate") != null) {
		xml_vdate = request.getParameter("xml_vdate");
	}

	Calendar now = Calendar.getInstance();
	int curYear = now.get(Calendar.YEAR);
	int curMonth = (now.get(Calendar.MONTH) + 1);
	int curDay = now.get(Calendar.DAY_OF_MONTH);

	String xml_appointment_date = MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay);
//	View endDateView = ticklerView.get("dateEnd");
//	else if (endDateView != null && !doCreateReport) {
//		xml_appointment_date = endDateView.getValue();
//	}
	if (request.getParameter("xml_appointment_date") != null) {
		xml_appointment_date = request.getParameter("xml_appointment_date");
	}
	if (! "0".equals(demographic_no)) {
		xml_appointment_date = "8888-12-31";
	}

%>

<html:html locale="true">
	<head>
		<title><bean:message key="tickler.ticklerMain.title"/> Manager</title>

		<script src="${pageContext.request.contextPath}/library/jquery/jquery-3.6.4.min.js"
		        type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/library/bootstrap/3.0.0/js/bootstrap.min.js"
		        type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/library/jquery/jquery-ui-1.12.1.min.js"
		        type="text/javascript"></script>
		<script type="text/javascript"
		        src="${pageContext.request.contextPath}/library/DataTables/DataTables-1.13.4/js/jquery.dataTables.js"></script>
		<link href="${pageContext.request.contextPath}/library/DataTables/DataTables-1.13.4/css/jquery.dataTables.css"
		      rel="stylesheet" type="text/css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${pageContext.request.contextPath}/css/print.css"/>
		<link rel="stylesheet" type="text/css"
		      href="${pageContext.request.contextPath}/library/jquery/jquery-ui-1.12.1.min.css"/>
		<link href="${pageContext.request.contextPath}/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet"
		      type="text/css"/>

		<style>
            tr.error td {
                color: red !important;
            }

            a.noteDialogLink {
                text-decoration: none !important;
                text-underline: none !important;
            }

            *:not(h2) {
                line-height: 1 !important;
                font-size: 12px !important;
            }

            tr.comment-row td {
                color: grey;
                background-color: white !important;
            }
		</style>
		<script type="application/javascript">


			const ctx = '${pageContext.request.contextPath}';
			let ticklerResultsTable;
			jQuery(document).ready(function () {
				jQuery("#note-form").dialog({
					autoOpen: false,
					height: 200,
					width: 450,
					modal: true,
					close: function () {

					}
				});
				//
				// const editFormDialog = jQuery( "#edit-form" ).dialog({
				//     autoOpen: false,
				//     modal: true,
				//     close: function() {
				//
				//     }
				// });
				let groupColumn = 11;
				ticklerResultsTable = jQuery("#ticklerResults").dataTable({
					"searching": false,
					"aLengthMenu": [[25, 50, 75, -1], [25, 50, 75, "All"]],
					"iDisplayLength": 25,
					columns: [
						{orderable: false},
						{orderable: false},
						{},
						{},
						{},
						{orderable: false},
						{},
						{},
						{},
						{},
						{orderable: false},
						{orderable: false}
					],
					columnDefs: [
						{visible: false, targets: groupColumn}
					],
					drawCallback: function (settings) {
						let api = this.api();
						let rows = api.rows({page: 'current'}).nodes();
						let last = null;

						api.column(groupColumn, {page: 'current'})
							.data()
							.each(function (group, i) {
								if (last !== group) {
									jQuery(rows)
										.eq(i)
										.after(jQuery(".followup-comment-" + group))
									last = group;
								}
							});
					},
					order: [[4, 'desc']]

				})

				/*
				 * Reload the search results with the Tickler status is changed.
				 */
				jQuery("#ticklerview").change(function(){
					document.forms['serviceform'].Submit.value='Create Report';
					document.forms['serviceform'].submit();
				})

			});

			function openNoteDialog(demographicNo, ticklerNo) {

				jQuery("#tickler_note_demographicNo").val(demographicNo);
				jQuery("#tickler_note_ticklerNo").val(ticklerNo);
				jQuery("#tickler_note_noteId").val('');
				jQuery("#tickler_note").val('');
				jQuery("#tickler_note_revision").html('');
				jQuery("#tickler_note_revision_url").attr('onclick', '');
				jQuery("#tickler_note_editor").html('');
				jQuery("#tickler_note_obsDate").html('');

				//is there an existing note?
				jQuery.ajax({
					method: "POST", url: ctx + '/CaseManagementEntry.do',
					data: {method: "ticklerGetNote", ticklerNo: jQuery('#tickler_note_ticklerNo').val()},
					async: false,
					dataType: 'json',
					success: function (data) {
						if (data != null) {
							jQuery("#tickler_note_noteId").val(data.noteId);
							jQuery("#tickler_note").val(data.note);
							jQuery("#tickler_note_revision").html(data.revision);
							jQuery("#tickler_note_revision_url").attr("onclick", "window.open(" + ctx + "'/CaseManagementEntry.do?method=notehistory&noteId='+data.noteId')')");
							jQuery("#tickler_note_editor").html(data.editor);
							jQuery("#tickler_note_obsDate").html(data.obsDate);
						}
					},
					error: function (jqXHR, textStatus, errorThrown) {
						console.log(errorThrown);
					}
				});

				jQuery("#note-form").dialog("open");
			}

			function closeNoteDialog() {
				jQuery("#note-form").dialog("close");
			}

			function saveNoteDialog() {
				//alert('not yet implemented');
				jQuery.ajax({
					url: ctx + '/CaseManagementEntry.do',
					data: {
						method: "ticklerSaveNote",
						noteId: jQuery("#tickler_note_noteId").val(),
						value: jQuery('#tickler_note').val(),
						demographicNo: jQuery('#tickler_note_demographicNo').val(),
						ticklerNo: jQuery('#tickler_note_ticklerNo').val()
					},
					async: false,
					success: function (data) {
						jQuery("#note-form").dialog("close");
					},
					error: function (jqXHR, textStatus, errorThrown) {
						alert(errorThrown);
					}
				});


			}

			function popupPage(vheight, vwidth, varpage) { //open a new popup window
				var page = "" + varpage;
				windowprops = "height=" + vheight + ",width=" + vwidth + ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
				var popup = window.open(page, "attachment", windowprops);
				if (popup != null) {
					if (popup.opener == null) {
						popup.opener = self;
					}
				}
			}

			function selectprovider(s) {
				if (self.location.href.lastIndexOf("&providerview=") > 0) a = self.location.href.substring(0, self.location.href.lastIndexOf("&providerview="));
				else a = self.location.href;
				self.location.href = a + "&providerview=" + s.options[s.selectedIndex].value;
			}

			function openBrWindow(theURL, winName, features) {
				window.open(theURL, winName, features);
			}

			function setfocus() {
				this.focus();
			}

			function refresh() {
				var u = self.location.href;
				if (u.lastIndexOf("view=1") > 0) {
					self.location.href = u.substring(0, u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1") + 6));
				} else {
					history.go(0);
				}
			}


			function allYear() {
				var newD = "8888-12-31";
				var beginD = "1900-01-01"
				document.serviceform.xml_appointment_date.value = newD;
				document.serviceform.xml_vdate.value = beginD;
			}

			function Check(e) {
				e.checked = true;
				//Highlight(e);
			}

			function Clear(e) {
				e.checked = false;
				//Unhighlight(e);
			}

			function reportWindow(page) {
				windowprops = "height=660, width=960, location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
				var popup = window.open(page, "labreport", windowprops);
				popup.focus();
			}

			function CheckAll() {
				var ml = document.ticklerform;
				var len = ml.elements.length;
				for (var i = 0; i < len; i++) {
					var e = ml.elements[i];
					if (e.name == "checkbox") {
						Check(e);
					}
				}
				//ml.toggleAll.checked = true;
			}

			function ClearAll() {
				var ml = document.ticklerform;
				var len = ml.elements.length;
				for (var i = 0; i < len; i++) {
					var e = ml.elements[i];
					if (e.name == "checkbox") {
						Clear(e);
					}
				}
				//ml.toggleAll.checked = false;
			}

			function Highlight(e) {
				var r = null;
				if (e.parentNode && e.parentNode.parentNode) {
					r = e.parentNode.parentNode;
				} else if (e.parentElement && e.parentElement.parentElement) {
					r = e.parentElement.parentElement;
				}
				if (r) {
					if (r.className == "msgnew") {
						r.className = "msgnews";
					} else if (r.className == "msgold") {
						r.className = "msgolds";
					}
				}
			}

			function Unhighlight(e) {
				var r = null;
				if (e.parentNode && e.parentNode.parentNode) {
					r = e.parentNode.parentNode;
				} else if (e.parentElement && e.parentElement.parentElement) {
					r = e.parentElement.parentElement;
				}
				if (r) {
					if (r.className == "msgnews") {
						r.className = "msgnew";
					} else if (r.className == "msgolds") {
						r.className = "msgold";
					}
				}
			}

			function AllChecked() {
				ml = document.messageList;
				len = ml.elements.length;
				for (var i = 0; i < len; i++) {
					if (ml.elements[i].name == "Mid" && !ml.elements[i].checked) {
						return false;
					}
				}
				return true;
			}

			function Delete() {
				var ml = document.messageList;
				ml.DEL.value = "1";
				ml.submit();
			}

			// function SynchMoves(which) {
			// var ml=document.messageList;
			// if(which==1) {
			//     ml.destBox2.selectedIndex=ml.destBox.selectedIndex;
			// }
			// else {
			//     ml.destBox.selectedIndex=ml.destBox2.selectedIndex;
			// }
			// }

			// function SynchFlags(which)
			// {
			// var ml=document.messageList;
			// if (which == 1) {
			//     ml.flags2.selectedIndex = ml.flags.selectedIndex;
			// }
			// else {
			//     ml.flags.selectedIndex = ml.flags2.selectedIndex;
			// }
			// }

			<%--function SetFlags()--%>
			<%--{--%>
			<%--var ml = document.messageList;--%>
			<%--ml.FLG.value = "1";--%>
			<%--ml.submit();--%>
			<%--}--%>

			<%--function Move() {--%>
			<%--var ml = document.messageList;--%>
			<%--var dbox = ml.destBox;--%>
			<%--if(dbox.options[dbox.selectedIndex].value == "@NEW") {--%>
			<%--    nn = window.prompt("<bean:message key="tickler.ticklerMain.msgFolderName"/>","");--%>
			<%--    if(nn == null || nn == "null" || nn == "") {--%>
			<%--	dbox.selectedIndex = 0;--%>
			<%--	ml.destBox2.selectedIndex = 0;--%>
			<%--    }--%>
			<%--    else {--%>
			<%--	ml.NewFol.value = nn;--%>
			<%--	ml.MOV.value = "1";--%>
			<%--	ml.submit();--%>
			<%--    }--%>
			<%--}--%>
			<%--else {--%>
			<%--    ml.MOV.value = "1";--%>
			<%--    ml.submit();--%>
			<%--}--%>
			<%--}--%>

			function saveView() {
				let url = ctx + "/saveWorkView.do";
				let params = {
					method: 'save',
					view_name: 'tickler',
					userrole: '${userrole}',
					providerno: '${user}',
					ticklerview: document.getElementById('ticklerview').value,
					// dateBegin: document.getElementById('xml_vdate').value,
					// dateEnd: document.getElementById('xml_appointment_date').value,
					providerview: document.getElementById('providerview').value,
					assignedTo: document.getElementById('assignedTo').value,
					mrpview: document.getElementById('mrpview').value
				};
				console.log(params)
				jQuery.post(url, params).done(function () {
					jQuery("#saveViewButton").attr('class', 'btn btn-success')
				})
					.fail(function () {
						jQuery("#saveViewButton").attr('class', 'btn btn-danger')
					});
			}

			function generateRenalLabReq(demographicNo) {
				var url = ctx + '/form/formlabreq<%=labReqVer%>.jsp?demographic_no=' + demographicNo + '&formId=0&provNo=<%=session.getAttribute("user")%>&fromSession=true';
				jQuery.ajax({
					url: ctx + '/renal/Renal.do?method=createLabReq&demographicNo=' + demographicNo,
					async: false,
					success: function (data) {
						popupPage(900, 850, url);
					}
				});
			}

		</script>

	</head>

	<body>
	<div class="container">
		<table>
			<tr class="noprint">
				<td>
					<h2><bean:message key="tickler.ticklerMain.msgTickler"/> Manager
					</h2>
				</td>
			</tr>
		</table>

		<form name="serviceform" method="get" action="ticklerMain.jsp" class="form-inline">
			<input type="hidden" name="Submit" value="">
			<input type="hidden" name="demoview" value="${param.demoview}">

			<c:if test="${empty param.demoview}">
			<div class="control-container">
				<label for="dateRange"><bean:message key="tickler.ticklerMain.formDateRange"/> <a
						href="javascript:void(0)" id="dateRange" onClick="allYear()"><bean:message
						key="tickler.ticklerMain.btnViewAll"/></a></label>
				<div class="form-group">
					<label for="xml_vdate">From</label>
					<input type="date" class="form-control" name="xml_vdate" id="xml_vdate">
				</div>
				<div class="form-group">
					<label for="xml_appointment_date">To</label>
					<input type="date" class="form-control" name="xml_appointment_date" id="xml_appointment_date"
					       value="<%=xml_appointment_date%>">
				</div>

				<div class="form-group">
					<label for="mrpview"> <bean:message key="tickler.ticklerMain.MRP"/></label>
					<select id="mrpview" class="form-control" name="mrpview">
						<option value="all" <%=mrpview.equals("all") ? "selected" : ""%>><bean:message
								key="tickler.ticklerMain.formAllProviders"/></option>
						<%
							ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
							List<Provider> providers = providerDao.getActiveProviders();
							for (Provider p : providers) {
						%>
						<option value="<%=p.getProviderNo()%>" <%=mrpview.equals(p.getProviderNo()) ? "selected" : ""%>><%=p.getLastName()%>
							,<%=p.getFirstName()%>
						</option>
						<%
							}
						%>
					</select>
				</div>
				<div class="form-group">
					<label for="providerview"><bean:message key="tickler.ticklerMain.msgCreator"/></label>

					<select id="providerview" class="form-control" name="providerview">
						<option value="all" <%=providerview.equals("all") ? "selected" : ""%>><bean:message
								key="tickler.ticklerMain.formAllProviders"/></option>
						<%
							for (Provider p : providers) {
						%>
						<option value="<%=p.getProviderNo()%>" <%=providerview.equals(p.getProviderNo()) ? "selected" : ""%>><%=p.getLastName()%>
							,<%=p.getFirstName()%>
						</option>
						<%
							}
						%>
					</select>
				</div>
				<div class="form-group">
					<label for="assignedTo"><bean:message key="tickler.ticklerMain.msgAssignedTo"/></label>
					<%
						if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) {
							SiteDao siteDao = (SiteDao) SpringUtils.getBean("siteDao");
							List<Site> sites = siteDao.getActiveSitesByProviderNo(user_no);
					%>
					<script>
						let _providers = [];
						<%for (int i=0; i<sites.size(); i++) {%>
						_providers["<%=sites.get(i).getSiteId()%>"] = "<%Iterator<Provider> iter = sites.get(i).getProviders().iterator();
							while (iter.hasNext()) {
								Provider p=iter.next();
								if ("1".equals(p.getStatus())) {%><option value='<%=p.getProviderNo()%>'><%=p.getLastName()%>, <%=p.getFirstName()%></option><%}%>";
						<%}}%>
						function changeSite(sel) {
							sel.form.assignedTo.innerHTML = sel.value == "none" ? "" : _providers[sel.value];
						}
					</script>
					<select id="site" class="form-control" name="site" onchange="changeSite(this)">
						<option value="none">---select clinic---</option>
						<%
							for (int i = 0; i < sites.size(); i++) {
						%>
						<option value="<%=sites.get(i).getSiteId()%>" <%=sites.get(i).getSiteId().toString().equals(request.getParameter("site")) ? "selected" : ""%>><%=sites.get(i).getName()%>
						</option>
						<%
							}
						%>
					</select>
					<select id="assignedTo" name="assignedTo" style="width:140px"></select>
					<%
						if (request.getParameter("assignedTo") != null) {
					%>
					<script>
						changeSite(document.getElementById("site"));
						document.getElementById("assignedTo").value = '<%=request.getParameter("assignedTo")%>';
					</script>
					<%
						}
					} else {
					%>
					<select id="assignedTo" class="form-control" name="assignedTo">
						<%
							// Check for property to default assigned provider and if present - default to user logged in
							boolean ticklerDefaultAssignedProvier = OscarProperties.getInstance().isPropertyActive("tickler_default_assigned_provider");
							if (ticklerDefaultAssignedProvier) {
								if ("all".equals(assignedTo)) {
									assignedTo = user_no;
								}
							}
						%>
						<option value="all" <%=assignedTo.equals("all") ? "selected" : ""%>><bean:message
								key="tickler.ticklerMain.formAllProviders"/></option>
						<%
							List<Provider> providersActive = providerDao.getActiveProviders();
							for (Provider p : providersActive) {
						%>
						<option value="<%=p.getProviderNo()%>" <%=assignedTo.equals(p.getProviderNo()) ? "selected" : ""%>><%=p.getLastName()%>
							, <%=p.getFirstName()%>
						</option>
						<%
							}
						%>
					</select>
					<%
						}
					%>
				</div>
				<div class="pull-right" style="text-align: right; vertical-align: bottom; padding:20px 15px 15px 15px;">
					<label for="formSubmitBtn"></label>
					<input type="button" class="btn btn-primary mbttn noprint" id="formSubmitBtn"
					       value="<bean:message key="tickler.ticklerMain.btnCreateReport"/>"
					       onclick="document.forms['serviceform'].Submit.value='Create Report'; document.forms['serviceform'].submit();">
					<label for="saveViewButton"> </label>
					<input type="button" class="btn" id="saveViewButton"
					       value="<bean:message key="tickler.ticklerMain.msgSaveView"/>" onclick="saveView();">
				</div>

			</div>

			</c:if>
			<div class="pull-left" style="margin-bottom:10px;">
					<label for="ticklerview"></label>
				<select id="ticklerview" class="form-control" name="ticklerview">
					<option value="A" <%=ticklerview.equals("A") ? "selected" : ""%>><bean:message
							key="tickler.ticklerMain.formActive"/></option>
					<option value="C" <%=ticklerview.equals("C") ? "selected" : ""%>><bean:message
							key="tickler.ticklerMain.formCompleted"/></option>
					<option value="D" <%=ticklerview.equals("D") ? "selected" : ""%>><bean:message
							key="tickler.ticklerMain.formDeleted"/></option>
				</select>
			</div>
		</form>

		<form name="ticklerform" method="post" action="dbTicklerMain.jsp">
			<% Locale locale = request.getLocale();%>
			<input type="hidden" name="parentAjaxId" value="<c:out value='${param.parentAjaxId}' />"/>
			<table id="ticklerResults" class="table table-striped table-compact">
				<thead>
				<tr>
					<th>&nbsp</th>
					<th>&nbsp;</th>
					<th>
						<bean:message key="tickler.ticklerMain.msgDemographicName"/>
					</th>
					<th>
						<bean:message key="tickler.ticklerMain.msgCreator"/>
					</th>
					<th>
						<bean:message key="tickler.ticklerMain.msgDate"/>
					</th>
					<th>
						<bean:message key="tickler.ticklerMain.msgUpdateDate"/>
<%--						<bean:message key="tickler.ticklerMain.msgCreationDate"/>--%>
					</th>

					<th>
						<bean:message key="tickler.ticklerMain.Priority"/>
					</th>

					<th>
						<bean:message key="tickler.ticklerMain.taskAssignedTo"/>
					</th>

					<th>
						<bean:message key="tickler.ticklerMain.status"/>
					</th>
					<th>
						Comment
					</th>
					<th></th>
					<th></th>
				</tr>
				</thead>
				<tbody>
				<%
					String dateBegin = xml_vdate;
					String dateEnd = xml_appointment_date;

					String vGrantdate = "1980-01-07 00:00:00.0";
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm", locale);

					if (dateEnd.compareTo("") == 0) {
						dateEnd = MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay);
					}

					if (dateBegin.compareTo("") == 0) {
						dateBegin = "1950-01-01"; // any early start date should suffice for selecting since the beginning
					}

					CustomFilter filter = new CustomFilter();
					filter.setPriority(null);

					filter.setStatus(ticklerview);

					filter.setStartDateWeb(dateBegin);
					filter.setEndDateWeb(dateEnd);
					filter.setPriority(null);

					if (!mrpview.isEmpty() && !mrpview.equals("all")) {
						filter.setMrp(mrpview);
					}

					if (!providerview.isEmpty() && !providerview.equals("all")) {
						filter.setProvider(providerview);
					}

					if (!assignedTo.isEmpty() && !assignedTo.equals("all")) {
						filter.setAssignee(assignedTo);
					}

					filter.setSort_order("desc");

					int targetDemographic = Integer.parseInt(demographic_no);
					List<Tickler> ticklers = Collections.emptyList();
					if (targetDemographic > 0) {
						ticklers = ticklerManager.search_tickler_bydemo(loggedInInfo, targetDemographic, ticklerview, filter.getStartDate(), filter.getEndDate());
					} else {
						ticklers = ticklerManager.getTicklers(loggedInInfo, filter);
					}

					String rowColour = "white";
					String numDaysUntilWarn = OscarProperties.getInstance().getProperty("tickler_warn_period");
					if(numDaysUntilWarn == null || numDaysUntilWarn.isEmpty()) {
						numDaysUntilWarn = "0";
					}
					for (Tickler tickler : ticklers) {
						Demographic demo = tickler.getDemographic();
						LocalDateTime serviceDate = tickler.getServiceDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
						LocalDateTime currentDate = LocalDateTime.now();

						long daysDifference = Duration.between(serviceDate, currentDate).toDays();
						long ticklerWarnDays = Long.parseLong(numDaysUntilWarn);
						boolean ignoreWarning = (ticklerWarnDays <= 0);
						boolean warning = false;

						//Set the colour of the table cell
						String warnColour = "";
						if (!ignoreWarning && (daysDifference >= ticklerWarnDays)) {
							warnColour = "Red";
							warning = true;
						}

						String cellColour = rowColour + warnColour;
				%>

				<tr <%=warning ? "class='error'" : ""%> >
					<td class="<%=cellColour%>"><input type="checkbox" name="checkbox" value="<%=tickler.getId()%>"
					                                   class="noprint"></td>
					<td class="<%=cellColour%>">
						<a href="javascript:void(0)" title="<bean:message key="tickler.ticklerMain.editTickler"/>"
						   onClick="window.open('../tickler/ticklerEdit.jsp?tickler_no=<%=tickler.getId()%>')">
							<span class="glyphicon glyphicon-pencil"></span>
						</a>
					</td>
					<td class="<%=cellColour%>"><a href="javascript:void(0)"
					                               onClick="popupPage(600,800,'../demographic/demographiccontrol.jsp?demographic_no=<%=demo.getDemographicNo()%>&displaymode=edit&dboperation=search_detail')">
						<%=Encode.forHtmlContent(demo.getLastName())%>,<%=Encode.forHtmlContent(demo.getFirstName())%>
					</a></td>
					<td class="<%=cellColour%>"><%=tickler.getProvider() == null ? "N/A" : Encode.forHtmlContent(tickler.getProvider().getFormattedName())%>
					</td>
					<td class="<%=cellColour%>"><%=tickler.getServiceDate()%>
					</td>
					<td class="<%=cellColour%>"><%=tickler.getUpdateDate()%>
					</td>
					<td class="<%=cellColour%>"><%=tickler.getPriority()%>
					</td>
					<td class="<%=cellColour%>"><%=tickler.getAssignee() != null ? tickler.getAssignee().getLastName() + ", " + tickler.getAssignee().getFirstName() : "N/A"%>
					</td>
					<td class="<%=cellColour%>"><%=tickler.getStatusDesc(locale)%>
					</td>
					<td class="<%=cellColour%>"><%=Encode.forHtmlContent(tickler.getMessage())%>

						<%
							List<TicklerLink> linkList = ticklerLinkDao.getLinkByTickler(tickler.getId().intValue());
							if (linkList != null) {
								for (TicklerLink tl : linkList) {
									String type = tl.getTableName();
						%>

						<%
							if (LabResultData.isMDS(type)) {
						%>
						<a href="javascript:reportWindow('SegmentDisplay.jsp?segmentID=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
						<%
						} else if (LabResultData.isCML(type)) {
						%>
						<a href="javascript:reportWindow('../lab/CA/ON/CMLDisplay.jsp?segmentID=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
						<%
						} else if (LabResultData.isHL7TEXT(type)) {
						%>
						<a href="javascript:reportWindow('../lab/CA/ALL/labDisplay.jsp?segmentID=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
						<%
						} else if (LabResultData.isDocument(type)) {
						%>
						<a href="javascript:reportWindow('../dms/ManageDocument.do?method=display&doc_no=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
						<%
						} else if (LabResultData.isHRM(type)) {
						%>
						<a href="javascript:reportWindow('../hospitalReportManager/Display.do?id=<%=tl.getTableId()%>&segmentID=<%=tl.getTableId()%>')">ATT</a>
						<%
						} else {
						%>
						<a href="javascript:reportWindow('../lab/CA/BC/labDisplay.jsp?segmentID=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
						<%
							}
						%>
						<%
								}
							}
						%>

					</td>
					<td class="<%=cellColour%> noprint">
						<a href="javascript:void(0)" class="noteDialogLink"
						   onClick="openNoteDialog('<%=demo.getDemographicNo() %>','<%=tickler.getId() %>')"
						   title="Add Encounter Note">
							<span class="glyphicon glyphicon-comment"></span>
						</a>
					</td>
					<td><%=tickler.getId()%>
					</td>
				</tr>
				<% Set<TicklerComment> tcomments = tickler.getComments();
					for (TicklerComment tc : tcomments) { %>

				<tr class="followup-comment-<%=tickler.getId()%> comment-row no-sort">
					<td></td>
					<td></td>
					<td></td>
					<td class="no sort"><%=Encode.forHtmlContent(tc.getProvider().getLastName())%>
						,<%=Encode.forHtmlContent(tc.getProvider().getFirstName())%>
					</td>
					<td></td>

					<td class="no-sort">
						<% if (tc.isUpdateDateToday()) { %>
						<%=tc.getUpdateTime(locale)%>
						<% } else { %>
						<%=tc.getUpdateDate(locale)%>
						<% } %>
					</td>

					<td></td>
					<td></td>
					<td></td>
					<td class="no sort"><%=Encode.forHtmlContent(tc.getMessage())%>
					</td>
					<td></td>
					<td><%=tickler.getId()%>
					</td>
				</tr>
				<% }
				} %>
				</tbody>
			</table>

			<table id="tablefoot">

				<tr class="noprint">
					<td class="white"><a id="checkAllLink" name="checkAllLink"
					                     href="javascript:CheckAll();"><bean:message
							key="tickler.ticklerMain.btnCheckAll"/></a> - <a href="javascript:ClearAll();"><bean:message
							key="tickler.ticklerMain.btnClearAll"/></a>

						<input type="hidden" name="submit_form" value="">
						<%
							if (ticklerview.compareTo("D") == 0) {
						%>
						<input type="button" class="btn"
						       value="<bean:message key="tickler.ticklerMain.btnEraseCompletely"/>" class="sbttn"
						       onclick="document.forms['ticklerform'].submit_form.value='Erase Completely'; document.forms['ticklerform'].submit();">
						<%
						} else {
						%>
						<input type="button" class="btn" value="<bean:message key="tickler.ticklerMain.btnComplete"/>"
						       class="sbttn"
						       onclick="document.forms['ticklerform'].submit_form.value='Complete'; document.forms['ticklerform'].submit();">
						<input type="button" class="btn btn-danger"
						       value="<bean:message key="tickler.ticklerMain.btnDelete"/>" class="sbttn"
						       onclick="document.forms['ticklerform'].submit_form.value='Delete'; document.forms['ticklerform'].submit();">
						<%
							}
						%>
						<input type="button" class="btn btn-primary" name="button"
						       value="<bean:message key="tickler.ticklerMain.btnAddTickler"/>"
						       onClick="popupPage('500','800', 'ticklerAdd.jsp?updateParent=true&parentAjaxId=${parentAjaxId}&bFirstDisp=false&messageID=null&demographic_no=${param.demoview}')"
						       class="sbttn">
						<input type="button" name="button" class="btn btn-warning"
						       value="<bean:message key="global.btnCancel"/>" onClick="window.close()" class="sbttn">
					</td>
				</tr>
			</table>


		</form>

		<p class="yesprint">
			<%=OscarProperties.getConfidentialityStatement()%>
		</p>

		<div id="note-form" title="Edit Tickler Note">
			<form>

				<table>
					<tbody>
					<textarea id="tickler_note" name="tickler_note" style="width:100%;"
					          oninput='this.style.height = "";this.style.height = this.scrollHeight + "px"'
					          onfocus='this.style.height = "";this.style.height = this.scrollHeight + "px"'></textarea>
					<input type="hidden" name="tickler_note_demographicNo" id="tickler_note_demographicNo" value=""/>
					<input type="hidden" name="tickler_note_ticklerNo" id="tickler_note_ticklerNo" value=""/>
					<input type="hidden" name="tickler_note_noteId" id="tickler_note_noteId" value=""/>
					</tbody>
				</table>
				<table>
					<tr>
						<td nowrap="nowrap">
							Date: <span id="tickler_note_obsDate"></span> rev <a id="tickler_note_revision_url"
							                                                     href="javascript:void(0)"
							                                                     onClick=""><span
								id="tickler_note_revision"></span></a><br/>
							Editor: <span id="tickler_note_editor"></span>
						</td>
					</tr>

				</table>
				<button class="btn btn-primary" onclick="saveNoteDialog()">Save</button>
				<button class="btn btn-danger" onclick="closeNoteDialog()">Exit</button>

			</form>
		</div>

		<div id="edit-form" title="Edit Tickler">
				<%--    onclick="editFormDialog.load('${pageContext.request.contextPath}/tickler/ticklerEdit.jsp?tickler_no=<%=t.getId()%>').dialog('open')">--%>
		</div>

	</div>
	</body>
</html:html>
