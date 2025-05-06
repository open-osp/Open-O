<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/ticklerPlus/header.jsp" %>

<tr>
    <td class="searchTitle" colspan="4">Prepared Ticklers</td>
</tr>
</table>
<%@ include file="messages.jsp" %>

<br/>
<table width="30%" border="0" cellpadding="0" cellspacing="1"
       bgcolor="#C0C0C0">
    <form action="${pageContext.request.contextPath}/Tickler.do" method="post">
        <input type="hidden" name="method" value="prepared_tickler_list"/>
        <tr class="title">
            <th width="20%"></th>
            <th width="80%">Name</th>
        </tr>
        <%
            int index = 0;
            String bgcolor;
        %>
        <c:forEach var="preparedTickler" items="${preparedTicklers}" varStatus="status">
            <c:set var="bgcolor" value="${status.index % 2 == 0 ? '#EEEEFF' : 'white'}"/>
            <tr bgcolor="${bgcolor}" align="center"
                onMouseOver="this.style.cursor='hand';this.style.backgroundColor='pink';"
                onMouseout="this.style.backgroundColor='${bgcolor}';"
                onclick="location.href="<%= request.getContextPath() %>/Tickler.do?method=prepared_tickler_edit&id=${preparedTickler.name}";">
                <td valign="middle"><input type="checkbox" name="checkbox"
                                           value="${preparedTickler.name}"/></td>
                <td>${preparedTickler.name}</td>
            </tr>
        </c:forEach>
    </form>
</table>

<table>
    <!--
        <tr>
            <td colspan="2"><a href="#" onclick="CheckAll(document.ticklerForm);">Check All</a>&nbsp;<a href="#" onclick="ClearAll(document.ticklerForm);">Clear All</a></td>
        </tr>
    -->
    <tr>
        <td><input type="button" value="New"
                   onclick="location.href='Tickler.do?method=prepared_tickler_edit'"/></td>
        <td><input type="button" value="Delete"
                   onclick="this.form.method.value='prepared_tickler_delete';this.form.submit();"/></td>
    </tr>
</table>

<br/>
<table width="100%">
    <tr>
        <td><a href="${pageContext.request.contextPath}/Tickler.do">Back to Ticklers</a></td>
    </tr>
</table>
</body>
</html>
