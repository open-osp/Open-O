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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
   String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

    LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="java.util.*"%>
<%@page import="org.oscarehr.common.model.Episode" %>
<%@page import="org.oscarehr.common.dao.EpisodeDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>

<%

%>
<html:html locale="true">
<head>
<title>Pregnancy History</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.min.css" >
<link rel="stylesheet" href="<%=request.getContextPath() %>/library/DataTables-1.10.12/media/css/jquery.dataTables.min.css" >

<script src="<%=request.getContextPath() %>/library/jquery/jquery-3.6.4.min.js"></script>
<script src="<%=request.getContextPath() %>/library/DataTables/datatables.min.js"></script>
<script src="<%=request.getContextPath() %>/library/DataTables-1.10.12/media/js/dataTables.bootstrap.min.js" ></script>


<style>
body
{
	text-align: center;
}

div#demo
{
	margin-left: auto;
	margin-right: auto;
	width: 90%;
	text-align: left;
}
</style>
<script>
	$(document).ready(function() {
		$('#pregTable').DataTable({
            "language": {
                        "url": "<%=request.getContextPath() %>/library/DataTables/i18n/<bean:message key="global.i18nLanguagecode"/>.json"
                    }
	      //  "aaSorting": [[ 1, "desc" ]]
	    });
	} );
</script>

</head>

<body>

<br>
<h2 style="text-align:center">Pregnancy History</h2>
<br>

<div id="demo">
			<table id="pregTable" class="display" style="width:100%">
				<thead>
					<tr>
						<th></th>
						<th>Description</th>
						<th>Start Date</th>
						<th>End Date</th>
						<th>Status</th>
						<th>Notes</th>
					</tr>
				</thead>
				<tbody>
					<%
						SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
						List<Episode> episodes = (List<Episode>)request.getAttribute("episodes");

						for(int x=0;x<episodes.size();x++) {
							Episode episode = episodes.get(x);

							String startDateStr = "";
							if(episode.getStartDate() != null) {
								startDateStr = dateFormatter.format(episode.getStartDate());
							}
							String endDateStr = "";
							if(episode.getEndDate() != null) {
								endDateStr = dateFormatter.format(episode.getEndDate());
							}
							Integer formId = org.oscarehr.common.dao.PregnancyFormsDao.getLatestFormIdByPregnancy(episode.getId());
							String url = request.getContextPath() + "/form/formonarenhancedpg1.jsp?demographic_no="+episode.getDemographicNo()+"&formId="+formId+"&provNo=" + loggedInInfo.getLoggedInProviderNo();
					%>
					<tr class="gradeB">
						<td>
							<a href="<%=url%>" title="Go to latest form" target="_blank"><img src="<%=request.getContextPath()%>/images/notepad.gif" alt="antenatal"></a>
						</td>
						<td>
							<a href="<%=request.getContextPath()%>/Episode.do?method=edit&episode.id=<%=episode.getId()%>"><%=episode.getDescription() %></a>
						</td>
						<td style="text-align:center"><%=startDateStr %></td>
						<td style="text-align:center"><%=endDateStr %></td>
						<td style="text-align:center"><%=episode.getStatus() %></td>
						<td>
							<%=episode.getNotes()!=null?episode.getNotes():"" %>
						</td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
</div>

<br><br>

</html:html>