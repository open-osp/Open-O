<%--

   Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
   This software is published under the GPL GNU General Public License.
   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   as published by the Free Software Foundation; either version 2
   of the License, or (at your option) any later version.
   <p>
   This program is distributed in the hope that it will be useful,d
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

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.oscarehr.util.DigitalSignatureUtils" %>
<%@ page import="org.oscarehr.common.model.enumerator.ModuleType" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="myDrugRefEnabled">
    <oscar:oscarPropertiesCheck property="MYDRUGREF_DS" value="yes">true</oscar:oscarPropertiesCheck>
</c:set>

<html>
<body>
<div class="container-fluid" style="min-height: 85vh;">
    <div class="row">
        <div class="col-12">
            <div class="row">
                <div class="col-md-8">
                    <div class="h-100 shadow border border-2 rounded">
                        <c:if test="${requestScope.sessionBean.stashSize > 0}">
                            <c:choose>
                                <c:when test="${requestScope.dx < 0}">
                                    <div id="preview"
                                         class="h-100 w-100"
                                         style="max-width: 650px; min-width: 480px; min-height: 84vh; border: none; display: block;">
                                            <%--                                        <jsp:include page="/oscarRx/Preview2.jsp">--%>
<%--                                        Replace iFrame with jsp-include --%>
                                        <jsp:include page="/oscarRx/printRx/PreviewContent.jsp">
                                            <jsp:param name="scriptId"
                                                       value="${requestScope.sessionBean.getStashItem(0).script_no}"/>
                                            <jsp:param name="rePrint" value="${requestScope.reprint}"/>
                                            <jsp:param name="pharmacyId" value="${param.pharmacyId}"/>
                                            <jsp:param name="pharmaAddress" value="${requestScope.pharmacyAddress}"/>
                                        </jsp:include>
                                    </div>
                                </c:when>
                                <c:when test="${requestScope.dx == 7}">
                                    <jsp:include page="HsfoPreview.jsp">
                                        <jsp:param name="dxCode" value="7"/>
                                    </jsp:include>
                                </c:when>
                                <c:otherwise>
                                    <!-- Empty content -->
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </div>
                </div>

                <div class="col-md-4">
                    <c:if test="${empty requestScope.pharmacyFax || requestScope.pharmacyFax == ''}">
                        <html:form action="/oscarRx/clearPending">
                            <html:hidden property="action" value=""/>
                            <div class="alert alert-warning" id="faxWarningNote">
                                <strong>Warning:</strong> faxing is disabled because no pharmacy fax number is
                                available.<br><br>To enable faxing, close this window and select a pharmacy
                                with a fax number before trying again.
                            </div>
                        </html:form>
                    </c:if>

                    <!-- Address Selection -->
                    <c:if test="${not empty requestScope.address}">
                        <div class="card mb-3">
                            <div class="card-body">
                                <div class="form-group mb-3">
                                    <label for="addressSel" class="form-label"><bean:message
                                            key="ViewScript.msgAddress"/></label>
                                    <select name="addressSel" id="addressSel" onChange="addressSelect()"
                                            class="form-select">
                                        <c:forEach var="addr" items="${requestScope.addressName}"
                                                   varStatus="loop">
                                            <option value="${loop.index}"
                                                    <c:if test="${sessionScope.RX_ADDR eq loop.index}">selected</c:if>>
                                                    ${addr}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <!-- Signature Block -->
                    <c:if test="${requestScope.showSignatureBlock}">
                        <c:if test="${requestScope.sessionBean.stashSize == 0 || empty requestScope.sessionBean.getStashItem(0).digitalSignatureId}">
                            <div class="card mb-3">
                                <div class="card-header">
                                    <h5 class="mb-0">Signature</h5>
                                </div>
                                <div class="card-body">
                                    <input type="hidden"
                                           name="${DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY}"
                                           value="${requestScope.signatureRequestId}"/>
                                    <div style="height:23vh; width: 26vw; max-width: -webkit-fill-available; width: -moz-available; max-height: 210px;"
                                         id="signatureFrame">
                                        <jsp:include page="/share/oscarSignaturePad/SignaturePad.jsp">
                                            <jsp:param name="inWindow" value="true"/>
                                            <jsp:param name="signatureRequestId" value="${requestScope.signatureRequestId}"/>
                                            <jsp:param name="saveToDB" value="true"/>
                                            <jsp:param name="demographicNo"  value="${requestScope.sessionBean.demographicNo}"/>
                                            <jsp:param name="ModuleType" value="${ModuleType.PRESCRIPTION.name()}"/>
                                        </jsp:include>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </c:if>

                    <!-- Actions Section -->
                    <div class="card mb-3">
                        <div class="card-header">
                            <h5 class="mb-0"><bean:message key="ViewScript.msgActions"/></h5>
                        </div>
                        <div class="card-body">

                            <div class="form-check form-switch mb-3">
                                <input class="form-check-input" type="checkbox" id="showAddressSwitch" checked onchange="togglePharmaInfoVisibility(this.checked)">
                                <label class="form-check-label" for="showAddressSwitch">Print Pharmacy Address</label>
                            </div>

                            <div class="form-group mb-3">
                                <label for="printPageSize" class="form-label">Page size:</label>
                                <select name="printPageSize" id="printPageSize" class="form-select">
                                    <c:forEach items="${requestScope.pageSizes}" var="pageSize">
                                        <option value="${pageSize.value}"
                                                <c:if test="${sessionScope.rxPageSize eq pageSize.value}">selected</c:if>>
                                                ${pageSize.key}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="button"
                                        class="btn btn-success mb-2 btn-lg"
                                        onClick="printIframe();">
                                    <bean:message key="ViewScript.msgPrint"/>
                                </button>

                                <button type="button"
                                        class="btn btn-success mb-3"
                                        <c:if test="${requestScope.reprint eq 'true'}">disabled</c:if>
                                        onClick="printPasteToParent('${ctx}',
                                                ${requestScope.rxPasteAsterisk},
                                                '${requestScope.prefPharmacy}',
                                                '${requestScope.demographicNo}',
                                                '${requestScope.providerName}',
                                                '${requestScope.providerNo}',
                                                '${requestScope.pharmacyName}',
                                                '${requestScope.pharmacyFax}',
                                                '${requestScope.prescribedBy}');">
                                    Print &amp; Add to encounter note
                                </button>

                                <!-- Fax Block -->
                                <c:if test="${requestScope.showRxFaxBlock}">
                                    <div class="container-sm border mb-3 px-4 pb-3 shadow-sm">
                                        <label for="faxNumber" class="form-label px-2" style="position: relative; top: -8%; background-color: white">From Fax Number:</label>
                                        <select id="faxNumber" name="faxNumber" class="form-select mb-2">
                                            <c:forEach var="faxConfig" items="${requestScope.faxConfigs}">
                                                <option value="${faxConfig.faxNumber}"
                                                        <c:if test="${requestScope.providerFax eq faxConfig.faxNumber}">selected</c:if>>
                                                        ${faxConfig.accountName}
                                                </option>
                                            </c:forEach>
                                        </select>

                                        <button type="button"
                                                class="btn btn-outline-info mb-2 w-100"
                                                id="faxButton"
                                                onClick="sendFax(${param.scriptId},
                                                    ${requestScope.signatureRequestId},
                                                    ${requestScope.useSC != null ? requestScope.useSC : false},
                                                    ${requestScope.selectedAddress != null ? requestScope.selectedAddress : ''}
                                                        );"
                                                <c:if test="${requestScope.isFaxDisabled}">
                                                    disabled="disabled"
                                                </c:if>>
                                            Fax
                                        </button>

                                        <button type="button"
                                                class="btn btn-outline-info mb-3 w-100"
                                                id="faxPasteButton"
                                                onClick="faxPasteToParent('${ctx}',
                                                    '${requestScope.rxPasteAsterisk}',
                                                    '${requestScope.prefPharmacy}',
                                                    '${requestScope.demographicNo}',
                                                    '${requestScope.providerName}',
                                                    '${requestScope.providerNo}',
                                                    '${requestScope.pharmacyName}',
                                                    '${requestScope.pharmacyFax}',
                                                    '${requestScope.prescribedBy}');
                                                        sendFax(${param.scriptId},
                                                    ${requestScope.signatureRequestId},
                                                    ${requestScope.useSC != null ? requestScope.useSC : false},
                                                    ${requestScope.selectedAddress != null ? requestScope.selectedAddress : ''}
                                                        );"
                                                <c:if test="${requestScope.isFaxDisabled}">
                                                    disabled="disabled"
                                                </c:if>>
                                            Fax &amp; Add to encounter note
                                        </button>
                                    </div>
                                </c:if>

                                <button type="button"
                                        class="btn btn-outline-success mb-2"
                                        onClick="resetStash('<c:out
                                                value="${ctx}"/>', '${myDrugRefEnabled}' === 'yes' ? 'true' : 'false');
                                                resetReRxDrugList('<c:out value="${ctx}"/>');
                                                closeRxPreviewBootstrapModal();">
                                    <bean:message key="ViewScript.msgCreateNewRx"/>
                                </button>

                                <button type="button"
                                        class="btn btn-outline-secondary mb-3"
                                        onClick="parent.window.close();">
                                    <bean:message key="ViewScript.msgBackToOscar"/>
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Additional Notes Section -->
                    <c:if test="${empty sessionScope.rePrint}">
                        <div class="card mb-3">
                            <div class="card-header">
                                <h5 class="mb-0"><bean:message key="ViewScript.msgAddNotesRx"/></h5>
                            </div>
                            <div class="card-body">
                                <div class="form-group">
                                    <label for="additionalNotes"></label>
                                    <textarea id="additionalNotes" class="form-control mb-2"
                                              onchange="addNotes(${param.scriptId});"></textarea>
                                    <button type="button" class="btn btn-primary"
                                            onclick="addNotes(${param.scriptId});">
                                        Additional Rx Notes
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <!-- Drug Info Section -->
                    <div class="card mb-3">
                        <div class="card-header">
                            <h5 class="mb-0"><bean:message key="ViewScript.msgDrugInfo"/></h5>
                        </div>
                        <div class="card-body">
                            <ul class="list-group">
                                <c:forEach begin="0" end="${requestScope.sessionBean.stashSize - 1}" var="i">
                                    <c:set var="rx" value="${requestScope.sessionBean.getStashItem(i)}"/>
                                    <c:if test="${!rx.custom}">
                                        <li class="list-group-item">
                                            <a href="javascript:ShowDrugInfo('${rx.genericName}');">
                                                <div>${rx.genericName}</div>
                                                <small class="text-muted">${rx.brandName}</small>
                                            </a>
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
