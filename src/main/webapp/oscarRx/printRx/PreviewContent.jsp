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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.oscarehr.util.DigitalSignatureUtils" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="oscar.OscarProperties" %>
<%@ page import="oscar.oscarRx.data.RxPatientData" %>
<%@ page import="oscar.oscarProvider.data.ProviderData" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<!DOCTYPE html>
<html:html lang="en">
    <head>
        <title><bean:message key="RxPreview.title"/></title>
        <style media="print">
            .noprint {
                display: none;
            }
        </style>
        <style media="all">
            * {
                font-family: Arial, "Helvetica Neue", Helvetica, sans-serif !important;
                font-size: 12px;
                overscroll-behavior: none;
                -webkit-font-smoothing: antialiased;
                -moz-osx-font-smoothing: grayscale;
            }

            th {
                border-bottom: 2px solid;
                text-align: left;
            }
        </style>
        <html:base/>

        <logic:notPresent name="RxSessionBean" scope="session">
            <logic:redirect href="error.html"/>
        </logic:notPresent>
        <logic:present name="RxSessionBean" scope="session">
            <bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean"
                         name="RxSessionBean" scope="session"/>
            <logic:equal name="bean" property="valid" value="false">
                <logic:redirect href="error.html"/>
            </logic:equal>
        </logic:present>
    </head>
    <body topmargin="0" leftmargin="0" vlink="#0000FF">

    <html:form action="/form/formname" styleId="preview2Form">
        <input type="hidden" name="demographic_no" value="${sessionScope.RxSessionBean.demographicNo}"/>
        <table>
            <tr>
                <td>
                    <table id="pwTable" width="400px" height="500px" cellspacing=0 cellpadding=10 border=2 rules="none">
                        <thead>
                        <tr>
                            <th valign=top width="100px">
                                <input type="image"
                                       src="img/rx.gif" border="0" alt="[Submit]"
                                       name="submit" title="Print in a half letter size paper"
                                       onclick="${requestScope.reprint eq 'true' ? 'javascript:return onPrint2(\'rePrint\');' : 'javascript:return onPrint2(\'print\');'}"/>
                                    <%-- Clinic title and enhanced RX information are now set in the action --%>
                                <c:choose>
                                    <c:when test="${empty infirmaryView_programAddress}">
                                        <%-- Phone number is now set in the action --%>
                                        <input type="hidden" name="clinicName"
                                               value="<%= StringEscapeUtils.escapeHtml(((String)request.getAttribute("clinicTitle")).replaceAll("(<br>)","\\\n")) %>"/>
                                        <input type="hidden" name="clinicPhone"
                                               value="<%= StringEscapeUtils.escapeHtml((String)request.getAttribute("phone")) %>"/>
                                        <input type="hidden" id="finalFax" name="clinicFax" value=""/>
                                    </c:when>
                                    <c:otherwise>
                                        <%-- Infirmary phone is now set in the action --%>
                                        <input type="hidden" name="clinicName"
                                               value="<c:out value="${infirmaryView_programAddress}"/>"/>
                                        <input type="hidden" name="clinicPhone" value="${requestScope.phone}"/>
                                        <input type="hidden" id="finalFax" name="clinicFax" value=""/>
                                    </c:otherwise>
                                </c:choose>
                                <input type="hidden" name="patientName"
                                       value="<%= StringEscapeUtils.escapeHtml(((RxPatientData.Patient)request.getAttribute("patient")).getFirstName())+ " " +StringEscapeUtils.escapeHtml(((RxPatientData.Patient)request.getAttribute("patient")).getSurname()) %>"/>
                                <input type="hidden" name="patientDOB"
                                       value="<%= StringEscapeUtils.escapeHtml((String)request.getAttribute("patientDOBStr")) %>"/>
                                <input type="hidden" name="pharmaFax" value="${requestScope.pharmacyFax}"/>
                                <input type="hidden" name="pharmaName" value="${requestScope.pharmacyName}"/>
                                <input type="hidden" name="pracNo"
                                       value="<%= StringEscapeUtils.escapeHtml((String)request.getAttribute("pracNo")) %>"/>
                                <input type="hidden" name="showPatientDOB" value="${requestScope.showPatientDOB}"/>
                                <input type="hidden" name="pdfId" id="pdfId" value=""/>
                                <input type="hidden" name="patientAddress"
                                       value="<%= StringEscapeUtils.escapeHtml((String)request.getAttribute("patientAddress")) %>"/>
                                <input type="hidden" name="patientCityPostal"
                                       value="<%= StringEscapeUtils.escapeHtml((String)request.getAttribute("patientCityPostal"))%>"/>
                                <input type="hidden" name="patientHIN"
                                       value="<%= StringEscapeUtils.escapeHtml((String)request.getAttribute("patientHin")) %>"/>
                                <input type="hidden" name="patientChartNo"
                                       value="<%=StringEscapeUtils.escapeHtml((String)request.getAttribute("patientChartNo"))%>"/>
                                <input type="hidden" name="bandNumber" value="${requestScope.bandNumber}"/>
                                <input type="hidden" name="patientPhone"
                                       value="<bean:message key="RxPreview.msgTel"/><%=StringEscapeUtils.escapeHtml((String)request.getAttribute("patientPhone")) %>"/>
                                <input type="hidden" name="rxDate"
                                       value="<%= StringEscapeUtils.escapeHtml((String)request.getAttribute("rxDateFormatted")) %>"/>
                                <input type="hidden" name="sigDoctorName"
                                       value="<%= StringEscapeUtils.escapeHtml((String)request.getAttribute("doctorName")) %>"/>
                                <!--img src="img/rx.gif" border="0"-->
                            </th>
                            <th valign=top height="100px" id="clinicAddress">
                                <b>${requestScope.doctorName}</b><br>
                                <c:choose>
                                    <c:when test="${empty infirmaryView_programAddress}">
                                        <%= ((oscar.oscarRx.data.RxProviderData.Provider) request.getAttribute("provider")).getClinicName().replaceAll("\\(\\d{6}\\)", "") %>
                                        <br>
                                        <%= ((oscar.oscarRx.data.RxProviderData.Provider) request.getAttribute("provider")).getClinicAddress() %>
                                        <br>
                                        <%= ((oscar.oscarRx.data.RxProviderData.Provider) request.getAttribute("provider")).getClinicCity() %>&nbsp;&nbsp;<%= ((oscar.oscarRx.data.RxProviderData.Provider) request.getAttribute("provider")).getClinicProvince() %>&nbsp;&nbsp;
                                        <%= ((oscar.oscarRx.data.RxProviderData.Provider) request.getAttribute("provider")).getClinicPostal() %>
                                        <% if (((oscar.oscarRx.data.RxProviderData.Provider) request.getAttribute("provider")).getPractitionerNo() != null && !((oscar.oscarRx.data.RxProviderData.Provider) request.getAttribute("provider")).getPractitionerNo().equals("")) { %>
                                        <br><bean:message
                                            key="RxPreview.PractNo"/>:<%= ((oscar.oscarRx.data.RxProviderData.Provider) request.getAttribute("provider")).getPractitionerNo() %>
                                        <% } %>
                                        <br>
                                        <bean:message key="RxPreview.msgTel"/>: ${requestScope.phone}<br>
                                        <oscar:oscarPropertiesCheck property="RXFAX" value="yes">
                                            <bean:message
                                                    key="RxPreview.msgFax"/>: <%= ((oscar.oscarRx.data.RxProviderData.Provider) request.getAttribute("provider")).getClinicFax() %>
                                            <br>
                                        </oscar:oscarPropertiesCheck>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${infirmaryView_programAddress}" escapeXml="false"/><br>
                                        <bean:message key="RxPreview.msgTel"/>: ${requestScope.phone}<br>
                                        <oscar:oscarPropertiesCheck property="RXFAX" value="yes">
                                            <bean:message key="RxPreview.msgFax"/>: ${finalFax}
                                        </oscar:oscarPropertiesCheck>
                                    </c:otherwise>
                                </c:choose>
                            </th>
                        </tr>
                        <tr style="border-bottom: 2px solid; border-top: 2px solid;">
                            <th colspan=2 valign=top height="75px">
								<span style="float: left">
									<%= Encode.forHtmlContent(((RxPatientData.Patient) request.getAttribute("patient")).getFirstName()) %> <%= Encode.forHtmlContent(((RxPatientData.Patient) request.getAttribute("patient")).getSurname()) %> <c:if
                                        test="${requestScope.showPatientDOB}"><br>DOB:<%= Encode.forHtmlContent((String) request.getAttribute("patientDOBStr")) %>
                                </c:if><br>
										<%= Encode.forHtmlContent((String) request.getAttribute("patientAddress")) %><br>
										<%= Encode.forHtmlContent((String) request.getAttribute("patientCityPostal")) %><br>
										<%= Encode.forHtmlContent((String) request.getAttribute("patientPhone")) %><br>
										<oscar:oscarPropertiesCheck value="true" property="showRxBandNumber">
                                            <c:if test="${ not empty requestScope.bandNumber }">
                                                <br/>
                                                <b><bean:message key="oscar.oscarRx.bandNumber"/></b>
                                                <c:out value="${ requestScope.bandNumber }"/>
                                            </c:if>
                                        </oscar:oscarPropertiesCheck>
										<b>
											<% if (!OscarProperties.getInstance().getProperty("showRxHin", "").equals("false")) { %>
												<bean:message
                                                        key="oscar.oscarRx.hin"/><%= Encode.forHtmlContent((String) request.getAttribute("patientHin")) %>
											<% } %>
										</b><br>
										<% if (OscarProperties.getInstance().getProperty("showRxChartNo", "").equalsIgnoreCase("true")) { %>
											<bean:message
                                                    key="oscar.oscarRx.chartNo"/><%=(String) request.getAttribute("patientChartNo")%>
										<% } %>
								</span>
                                <span style="float:right">
                                        ${requestScope.rxDateFormatted}
                                </span>
                            </th>
                        </tr>
                        </thead>
                        <tfoot>
                        <c:if test="${not empty requestScope.rxFooter}">
                            ${requestScope.rxFooter}
                        </c:if>

                        <tr valign=bottom>
                            <td height=25px width=25% style="vertical-align:bottom;"><bean:message
                                    key="RxPreview.msgSignature"/>:
                            </td>
                            <td height=25px width=75% style="border-bottom: 2px solid;">
                                    <%-- Digital signature is now set in the action --%>

                                <input type="hidden" name="<%= DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY %>"
                                       value="${requestScope.signatureRequestId}"/>
                                <img id="signature" style="width:260px; height:130px; object-fit: contain;"
                                     src="${requestScope.startimageUrl}" alt="digital_signature"/>
                                <input type="hidden" name="imgFile" id="imgFile" value=""/>

                                <script type="text/javascript">
                                    var POLL_TIME = 2500;
                                    var counter = 0;

                                    function refreshImage() {
                                        counter++;
                                        var img = document.getElementById("signature");
                                        img.src = '${requestScope.imageUrl}&rand=' + counter;

                                        var request = dojo.io.bind({
                                            url: '<%=request.getContextPath()%>/PMmodule/ClientManager/check_signature_status.jsp?<%= DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY %>=${requestScope.signatureRequestId}',
                                            method: "post",
                                            mimetype: "text/html",
                                            load: function (type, data, evt) {
                                                var x = data.trim();
                                            }
                                        });
                                    }
                                </script>
                                &nbsp;
                            </td>
                        </tr>

                        <tr valign=bottom>
                            <td height=25px>
                                <c:if test="${OscarProperties.getInstance().getProperty('signature_tablet', '').equals('yes')}">
                                    <input type="button" value=
                                        <bean:message key="RxPreview.digitallySign"/> class="noprint"
                                           onclick="setInterval('refreshImage()', POLL_TIME); document.location='<%=request.getContextPath()%>/signature_pad/topaz_signature_pad.jnlp.jsp?<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>=${requestScope.signatureRequestId}'"/>
                                </c:if>
                            </td>
                            <td height=25px>
                                &nbsp; <c:out value="${requestScope.doctorName}"/>
                                <c:if test="${not empty requestScope.pracNo && requestScope.pracNo ne 'null'}">
                                    <br>
                                    &nbsp;<bean:message key="RxPreview.PractNo"/> <c:out value="${requestScope.pracNo}"/>
                                </c:if>
                            </td>
                        </tr>

                        <c:if test="${sessionScope.tmpBeanRX.stashSize > 0}">
                            <c:set var="rx" value="${sessionScope.tmpBeanRX.getStashItem(0)}"/>
                            <c:if test="${requestScope.reprint eq 'true' && rx != null}">
                                <tr valign=bottom>
                                    <td height=55px colspan="2">
										<span style="float:right; font-size:10px;">
											<bean:message key="RxPreview.msgReprintBy"/> <c:out
                                                value="${ProviderData.getProviderName(sessionScope.user)}"/> <br>
											<bean:message key="RxPreview.msgOrigPrinted"/>:&nbsp;${rx.printDate} <br>
											<bean:message key="RxPreview.msgTimesPrinted"/>:&nbsp;${rx.numPrints}
										</span>
                                        <input type="hidden" name="origPrintDate" value="${rx.printDate}"/>
                                        <input type="hidden" name="numPrints" value="${rx.numPrints}"/>
                                        <input type="hidden" name="rxReprint" value="true"/>
                                    </td>
                                </tr>
                            </c:if>
                        </c:if>

                        <c:if test="${requestScope.qrCodeEnabled}">
                            <tr>
                                <td colspan="2">
                                    <img src="<%=request.getContextPath()%>/contentRenderingServlet/prescription_qr_code_${sessionScope.tmpBeanRX.stashItem[0].script_no}.png?source=prescriptionQrCode&prescriptionId=${sessionScope.tmpBeanRX.stashItem[0].script_no}"
                                         alt="qr_code"/>
                                </td>
                            </tr>
                        </c:if>

                        <c:if test="${not empty requestScope.formsPromoText}">
                            <tr valign=bottom align="center">
                                <td height=25px colspan="2" style="font-size: 9px"></br>
                                        ${requestScope.formsPromoText}
                                </td>
                            </tr>
                        </c:if>
                        </tfoot>
                        <tbody>
                        <c:forEach var="i" begin="0" end="${sessionScope.tmpBeanRX.stashSize - 1}">
                            <c:set var="rx" value="${sessionScope.tmpBeanRX.getStashItem(i)}"/>
                            <c:set var="fullOutLine" value="${rx.fullOutLine.replaceAll(';','<br />')}"/>

                            <c:if test="${empty fullOutLine || fullOutLine.length() <= 6}">
                                <c:set var="fullOutLine"
                                       value="<span style=\"color:red;font-size:16;font-weight:bold\">An error occurred, please write a new prescription.</span><br />${fullOutLine}"/>
                            </c:if>

                            <tr style="page-break-inside: avoid;">
                                <td colspan=2 style>${fullOutLine}</td>
                            </tr>
                        </c:forEach>
                        <tr valign="bottom">
                            <td colspan="2" id="additNotes"></td>
                        </tr>

                        <input type="hidden" name="rx" value="${requestScope.strRx}"/>
                        <input type="hidden" name="rx_no_newlines" value="${requestScope.strRxNoNewLines}"/>
                        <input type="hidden" name="additNotes" value=""/>
                        </tbody>
                    </table>
                </td>
                <td style="vertical-align: top;padding: 5px;">
                    <div id="pharmInfo">
                            ${requestScope.pharmacyAddress != null ? requestScope.pharmacyAddress : ''}
                    </div>
                </td>
            </tr>
        </table>
    </html:form>
    </body>
</html:html>
