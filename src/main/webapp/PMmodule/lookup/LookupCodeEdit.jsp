<%--

    Copyright (c) 2005, 2009 IBM Corporation and others.
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

    Contributors:
        <Quatro Group Software Systems inc.>  <OSCAR Team>

--%>
<%@ include file="/taglibs.jsp" %>
<script type="text/javascript">
    function submitForm() {
        trimInputBox();
        if (!isDateValid) return;
        document.forms[0].method.value = "save";
        if (noChanges()) {
            alert("There are no changes detected to save");
        } else {
            var obj1 = document.getElementsByName('tableId')[0];
            if (obj1.value == 'SHL') {
                var obj2 = document.getElementsByName('field[4].val')[0];
                if (obj2.value == '0' && !confirm("Deactivating this shelter will also deactivate facilities and programs in the shelter. Select Ok to proceed or Cancel to cancel."))
                    return;
            }
            if (obj1.value == 'OGN') {
                var obj2 = document.getElementsByName('field[2].val')[0];
                if (obj2.value == '0' && !confirm("Deactivating this organization will also deactivate shelters, facilities and programs in the organization. Select Ok to proceed or Cancel to cancel."))
                    return;
            }
            document.forms[0].submit();
        }
    }
</script>
<form action="${pageContext.request.contextPath}/Lookup/LookupCodeEdit.do" method="post">
    <input type="hidden" name="scrollPosition" value='<c:out value="${scrPos}"/>'/>
    <input type="hidden" name="tableId" value='<c:out value="${lookupCodeEditForm.tableDef.tableId}"/>'/>
    <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
        <tr>
            <th class="pageTitle" align="center"><span
                    id="lblTitle" align="left">Code Edit - 
                <c:out value="${lookupCodeEditForm.tableDef.description}"/>
            </span></th>
        </tr>
        <tr>
            <td align="left" class="buttonBar2">
                <input type="hidden" id="method" name="method"></input>
                <a href="${pageContext.request.contextPath}/Lookup/LookupCodeList.do" paramId="id" paramName="lookupCodeEditForm"
                           paramProperty="tableDef.tableId">
                    <img src="<%= request.getContextPath() %>/images/close16.png" border="0"/> Close</a>
                <c:if test="${!isReadOnly}">
                    &nbsp;|&nbsp; <a href="javascript:void1();"
                                     onclick="javascript:setNoConfirm();return deferedSubmit('');">
                    <img src="<%= request.getContextPath() %>/images/Save16.png" border="0"/> Save </a>
                </c:if>

            </td>
        </tr>
        <tr>
            <td align="left" class="message">
                <c:if test="${not empty pmm}">
                    <c:forEach var="message" items="${pmm}">
                        <c:out escapeXml="false" value="${message}"/><br/>
                    </c:forEach>
                </c:if>
            </td>
        </tr>
        <tr>
            <td height="100%">
                <div style="color: Black; background-color: White; border-width: 1px; border-style: Ridge;
                    height: 100%; width: 100%; overflow: auto;" id="scrollBar">
                    <table width="100%">

                        <c:forEach var="field" items="${lookupCodeEditForm.codeFields}" varStatus="status">
                            <tr>
                                <td width="30%">${field.fieldDesc}</td>
                                <td>
                                    <c:choose>
                                        <%-- String Field Type (S) --%>
                                        <c:when test="${field.fieldType == 'S'}">
                                            <c:choose>
                                                <c:when test="${not field.editable}">
                                                    ${field.val}
                                                    <c:if test="${not empty field.valDesc}">
                                                        - ${field.valDesc}
                                                    </c:if>
                                                    <input type="hidden" name="field[${status.index}].val" value="${field.val}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:choose>
                                                        <c:when test="${empty field.lookupTable}">
                                                            <input type="text" name="field[${status.index}].val" value="${field.val}"
                                                                   style="width:100%" maxlength="${field.fieldLengthStr}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="hidden" name="field[${status.index}].lookupTable" value="${field.lookupTable}"/>
                                                            <quatro:lookupTag name="field" tableName="${field.lookupTable}" indexed="true"
                                                                              formProperty="lookupCodeEditForm" codeWidth="10%"
                                                                              codeProperty="val" bodyProperty="valDesc"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>

                                        <%-- Date Field Type (D) --%>
                                        <c:when test="${field.fieldType == 'D'}">
                                            <c:choose>
                                                <c:when test="${field.editable}">
                                                    <quatro:datePickerTag name="field" property="val" indexed="true"
                                                                          openerForm="lookupCodeEditForm" width="200px"/>
                                                </c:when>
                                                <c:otherwise>
                                                    ${field.val}
                                                    <input type="hidden" name="field[${status.index}].val" value="${field.val}"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>

                                        <%-- Number Field Type (N) --%>
                                        <c:when test="${field.fieldType == 'N'}">
                                            <c:choose>
                                                <c:when test="${field.editable}">
                                                    <input type="text" name="field[${status.index}].val" value="${field.val}" maxlength="10"/>
                                                </c:when>
                                                <c:otherwise>
                                                    ${field.val}
                                                    <input type="hidden" name="field[${status.index}].val" value="${field.val}"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>

                                        <%-- Boolean Field Type (B) --%>
                                        <c:when test="${field.fieldType == 'B'}">
                                            <c:choose>
                                                <c:when test="${field.editable}">
                                                    <select name="field[${status.index}].val">
                                                        <option value="1" ${field.val == '1' ? 'selected' : ''}>Yes</option>
                                                        <option value="0" ${field.val == '0' ? 'selected' : ''}>No</option>
                                                    </select>
                                                </c:when>
                                                <c:otherwise>
                                                    <select name="field[${status.index}].val" disabled>
                                                        <option value="1" ${field.val == '1' ? 'selected' : ''}>Yes</option>
                                                        <option value="0" ${field.val == '0' ? 'selected' : ''}>No</option>
                                                    </select>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>

                    </table>
                </div>
            </td>
        </tr>
    </table>
    <%@ include file="/common/readonly.jsp" %>
</form>
