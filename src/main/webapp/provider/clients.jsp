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

<%@page import="java.lang.reflect.Field"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="org.oscarehr.common.dao.ServiceRequestTokenDao" %>
<%@ page import="org.oscarehr.common.dao.ServiceAccessTokenDao" %>
<%@ page import="org.oscarehr.common.dao.ServiceClientDao" %>
<%@ page import="org.oscarehr.common.model.ServiceClient" %>
<%@ page import="org.oscarehr.common.model.ServiceRequestToken" %>
<%@ page import="org.oscarehr.common.model.ServiceAccessToken" %>
<%
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String providerNo=loggedInInfo.getLoggedInProviderNo();

	ServiceRequestTokenDao serviceRequestTokenDao = SpringUtils.getBean(ServiceRequestTokenDao.class);
	ServiceAccessTokenDao serviceAccessTokenDao = SpringUtils.getBean(ServiceAccessTokenDao.class);
	ServiceClientDao serviceClientDao = SpringUtils.getBean(ServiceClientDao.class);

	List<ServiceRequestToken> requestTokens = new ArrayList<ServiceRequestToken>();
	List<ServiceAccessToken> accessTokens = new ArrayList<ServiceAccessToken>();

	//find all the tokens/clients associated with this provider
	for(ServiceRequestToken t: serviceRequestTokenDao.findAll()) {
		if(t.getProviderNo() != null && t.getProviderNo().equals(providerNo)){
			requestTokens.add(t);
		}
	}
	for(ServiceAccessToken t: serviceAccessTokenDao.findAll()) {
		if(t.getProviderNo() != null && t.getProviderNo().equals(providerNo)){
			accessTokens.add(t);
		}
	}

	Map<Integer,ServiceClient> clientMap = new HashMap<Integer,ServiceClient>();
	for(ServiceClient c:serviceClientDao.findAll()) {
		clientMap.put(c.getId(),c);
	}

%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Manage API Clients</title>
 <link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">

<script src="<%=request.getContextPath() %>/library/jquery/jquery-3.6.4.min.js"></script>

<script type="text/javascript" language="JavaScript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>


<script>
	function deleteAccessToken(id) {
		jQuery.getJSON("tokenManage.json",
                {
                        method: "deleteAccessToken",
                        id: id
                },
                function(xml){
                	if(xml.success)
                		window.location = 'clients.jsp';
                	else
                		alert(xml.error);
                });
	}

	function deleteRequestToken(id) {
		jQuery.getJSON("tokenManage.json",
                {
                        method: "deleteRequestToken",
                        id: id
                },
                function(xml){
                	if(xml.success)
                		window.location = 'clients.jsp';
                	else
                		alert(xml.error);
                });
	}

</script>
</head>
<body>

<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><h4>Provider</h4></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Manage API Client/Tokens</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" style="width:160px;">
		&nbsp;</td>
		<td class="MainTableRightColumn" style="width:90%" >
			<h4>Request Tokens</h4>
			<table id="requestTokenTable" name="requestTokenTable" class="table table-striped table-condensed" style="width: 100%;">
				<thead>
					<tr>
						<td>Client Name</td>
						<td>Date Created</td>
						<td>Verified</td>
						<td>Actions</td>
					</tr>
				</thead>
				<tbody>
			<%if(requestTokens.size()>0) { %>
					<%for(ServiceRequestToken srt: requestTokens) { %>
					<tr>
						<td><%=clientMap.get(srt.getClientId()).getName() %></td>
						<td><%=dateFormatter.format(srt.getDateCreated()) %></td>
						<td><%=srt.getVerifier()%></td>
						<td><a href="javascript:void(0);" onclick="deleteRequestToken('<%=srt.getId()%>');"><img border="0" title="delete" src="<%=request.getContextPath() %>/images/Delete16.gif"/></a></td>
					</tr>
					<% } %>
			<% } else {%>
				<tr><td colspan="4"><span class="alert alert-warn" style="width:90%; display:inline-block;">No Request Tokens found.<span></td></tr>
			<% } %>
				</tbody>
			</table>

			<br>
			<h4>Access Tokens</h4>

			<table id="accessTokenTable" name="accessTokenTable" class="table table-striped table-condensed" style="width: 100%;">
				<thead>
					<tr>
						<td>Client Name</td>
						<td>Date Created</td>
						<td>Expires</td>
						<td>Actions</td>
					</tr>
				</thead>
				<tbody>
			<%if(accessTokens.size()>0) { %>
					<%for(ServiceAccessToken sat: accessTokens) { %>
					<tr>
						<td><%=clientMap.get(sat.getClientId()).getName() %></td>
						<td><%=sat.getDateCreated() %></td>
						<td>
						<%
							Date d = new Date();
							d.setTime(sat.getIssued()*1000);
							Calendar c = Calendar.getInstance();
							c.setTime(d);
							c.add(Calendar.SECOND, (int)sat.getLifetime());
						%>
						<%=dateFormatter.format(c.getTime()) %>
						</td>
						<td><a href="javascript:void(0);" onclick="deleteAccessToken('<%=sat.getId()%>');"><img border="0" title="delete" src="<%=request.getContextPath() %>/images/Delete16.gif"/></a></td>
					</tr>
					<% } %>
			<% } else {%>
				<tr><td colspan="4"><span class="alert alert-warn" style="width:90%; display:inline-block;">No Access Tokens found.<span></td></tr>
			<% } %>
				</tbody>
			</table>



		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>


</body>


</html:html>