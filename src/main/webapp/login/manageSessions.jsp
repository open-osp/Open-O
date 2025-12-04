<%--

   Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
   This software is published under the GPL GNU General Public License.
   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   as published by the Free Software Foundation; either version 2
   of the License, or (at your option) any later version.
   <p>
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
   GNU General Public License for more details.
   <p>
   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
   <p>
   This software was written for
   Centre for Research on Inner City Health, St. Michael's Hospital,
   Toronto, Ontario, Canada

--%>

<%@ page import="oscar.login.ManageSessionsForm" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Session Choice</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <c:set var="ctx" value="${ pageContext.request.contextPath }" scope="page"/>
    <link rel="stylesheet" href="${ctx}/library/bootstrap/5.0.2/css/bootstrap.min.css" type="text/css"/>
    <script type="text/javascript" src="${ctx}/library/bootstrap/5.0.2/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container p-5 d-flex align-items-center justify-content-center">
    <div class="card shadow-sm w-100" style="max-width: 640px;">
        <div class="card-body">
            <h1 class="h5 mb-3">Other active sessions detected</h1>
            <p class="mb-1">
                We noticed that you have
                <strong><c:out value="${requestScope.otherSessionCount}"/></strong>
                other session<c:if test="${requestScope.otherSessionCount != 1}">s</c:if> currently signed in.
            </p>
            <p class="text-muted">Please choose how you want to proceed:</p>

            <form action="<%= request.getContextPath() %>/session/loginManageSessions.do" method="post">
                <input type="hidden" name="manageSessionsFlow" value="${requestScope.manageSessionFlow}"/>

                <div class="row g-2 mt-2">
                    <div class="col-12 col-md-6">
                        <button type="submit"
                                class="btn btn-outline-secondary w-100"
                                name="sessionChoice"
                                value="<%=ManageSessionsForm.SESSION_CHOICE_KEEP%>">
                            Keep other sessions signed in
                        </button>
                    </div>
                    <div class="col-12 col-md-6">
                        <button type="submit"
                                class="btn btn-danger w-100"
                                name="sessionChoice"
                                value="<%=ManageSessionsForm.SESSION_CHOICE_INVALIDATE%>">
                            Sign out other sessions
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
