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

<%@page import="java.util.Collections"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%><%@page import="org.oscarehr.web.eform.EfmpatientformlistSendPhrAction"%>
<%
String[] s=request.getParameterValues("sendToPhr");
EfmpatientformlistSendPhrAction bean=new EfmpatientformlistSendPhrAction(request);

String clientId = request.getParameter("clientId");

ArrayList<String> newDocIds=bean.sendEFormsToPhr(s);

StringBuilder sb=new StringBuilder();
sb.append(request.getContextPath());
sb.append("/documentManager/SendDocToPhr.do?demoId=");
sb.append(clientId);

for (String docId : newDocIds)
{
	sb.append("&docNo=");
	sb.append(docId);
}

response.sendRedirect(sb.toString());
%>
