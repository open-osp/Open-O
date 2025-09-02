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

<%@ page
        import="ca.openosp.openo.messenger.docxfer.send.*, ca.openosp.openo.messenger.docxfer.util.*, ca.openosp.openo.util.*" %>
<%@ page import="java.util.*, org.w3c.dom.*" %>
<%@ page import="ca.openosp.openo.messenger.pageUtil.MsgSessionBean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_msg" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_msg");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<c:if test="${empty sessionScope.msgSessionBean}">
    <% response.sendRedirect("index.jsp"); %>
</c:if>
<c:if test="${not empty sessionScope.msgSessionBean}">
    <% 
        // Directly accessing the bean from the session
        MsgSessionBean bean = (MsgSessionBean) session.getAttribute("msgSessionBean");
        if (!bean.isValid()) {
            response.sendRedirect("index.jsp");
        }
    %>
</c:if>
<%
    String pdfAttch = (String) request.getAttribute("PDFAttachment");
    String id = request.getParameter("id");
%>
