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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo" %>
<%@ page import="org.oscarehr.util.DigitalSignatureUtils" %>

<%@ page import="oscar.oscarProvider.data.ProviderData" %>
<%@ page import="org.oscarehr.common.model.enumerator.ModuleType" %>


<c:set var="roleName" value="${sessionScope.userrole},${sessionScope.user}"/>
<security:oscarSec roleName="${roleName}" objectName="_rx" rights="r" reverse="true">
    <c:redirect url="../securityError.jsp?type=_rx"/>
</security:oscarSec>


<html:html lang="en">

    <head>
        <title><bean:message key="ViewScript.title"/></title>

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
        <c:set var="ctx" value="${pageContext.request.contextPath}"/>

        <%
            oscar.oscarRx.pageUtil.RxSessionBean sessionBean = (oscar.oscarRx.pageUtil.RxSessionBean) pageContext.findAttribute("bean");

            String reprint = request.getAttribute("reprint") != null ? request.getAttribute("reprint").toString() : "false";
            if (reprint.equalsIgnoreCase("true")) {
                sessionBean = (oscar.oscarRx.pageUtil.RxSessionBean) session.getAttribute("tmpBeanRX");
            }
        %>

        <script type="text/javascript">


            function updateCurrentInteractions() {
                new Ajax.Request("GetmyDrugrefInfo.do?method=findInteractingDrugList", {
                    method: 'get', onSuccess: function (transport) {
                        new Ajax.Request("UpdateInteractingDrugs.jsp", {
                            method: 'get', onSuccess: function (transport) {
                                let str = transport.responseText;
                                str = str.replace('<script type="text/javascript">', '');
                                str = str.replace(/<\/script>/, '');
                                eval(str);
                                //oscarLog("str="+str);
                                <oscar:oscarPropertiesCheck property="MYDRUGREF_DS" value="yes">
                                callReplacementWebService("GetmyDrugrefInfo.do?method=view", 'interactionsRxMyD');
                                </oscar:oscarPropertiesCheck>
                            }
                        });
                    }
                });
            }


            function sendFax(scriptId, signatureRequestId) {
                if ('function' === typeof window.onbeforeunload) {
                    window.onbeforeunload = null;
                }
                let faxNumber = document.getElementById('faxNumber');
                frames['preview'].document.getElementById('finalFax').value = faxNumber.options[faxNumber.selectedIndex].value;
                frames['preview'].document.getElementById('pdfId').value = signatureRequestId;
                onPrint2('oscarRxFax', scriptId);
            }

            function onPrint2(method, scriptId) {
                // Get values from request attributes set by the Action class
                let useSC = ${requestScope.useSC != null ? requestScope.useSC : false};
                let scAddress = "${requestScope.selectedAddress != null ? requestScope.selectedAddress : ''}";
                const rxPageSize = $('printPageSize').value;
                console.log("rxPagesize  " + rxPageSize);

                let action = "../form/createcustomedpdf?__title=Rx&__method=" + method + "&useSC=" + useSC + "&scAddress=" + scAddress + "&rxPageSize=" + rxPageSize + "&scriptId=" + scriptId;
                document.getElementById("preview").contentWindow.document.getElementById("preview2Form").action = action;
                if (method !== "oscarRxFax") {
                    document.getElementById("preview").contentWindow.document.getElementById("preview2Form").target = "_blank";
                }
                document.getElementById("preview").contentWindow.document.getElementById("preview2Form").submit();

                return true;
            }


            function printPaste2Parent(print, fax, pasteRx) {
                //console.log("in printPaste2Parent");
                try {
                    let text = "";
                    <c:if test="${requestScope.rxPasteAsterisk}">
                    text += "**********************************************************************************\n";
                    </c:if>

                    if (print) {
                        text += "Prescribed and printed by ${requestScope.prescribedBy}\n";
                    } else if (fax) {
                        text = "[Rx faxed to " + '${requestScope.pharmacyName}' + " Fax#: " + '${requestScope.pharmacyFax}';
                        text += " prescribed by ${requestScope.prescribedBy}";
                        text += ", ${requestScope.timeStamp}]\n";
                    }

                    if (pasteRx) {
                        if (document.all) {
                            text += preview.document.forms[0].rx_no_newlines.value
                        } else {
                            text += preview.document.forms[0].rx_no_newlines.value + "\n";
                        }

                        if (document.getElementById('additionalNotes') !== null) {
                            text += document.getElementById('additionalNotes').value + "\n";
                        }
                    }
                    <c:if test="${requestScope.rxPasteAsterisk}">
                    <c:if test="${not empty requestScope.prefPharmacy.trim()}">
                    text += "${requestScope.prefPharmacy}\n"
                    </c:if>
                    text += "****<c:out value='${ProviderData.getProviderName(pageScope.sessionBean.providerNo)}' escapeXml='true'/>********************************************************************************\n";
                    </c:if>

                    //we support pasting into orig encounter and new casemanagement
                    demographicNo = <c:out value="${pageScope.sessionBean.demographicNo}"/>;
                    noteEditor = "noteEditor" + demographicNo;
                    if (window.parent.opener) {
                        if (window.parent.opener.document.forms["caseManagementEntryForm"] !== undefined &&
                            window.parent.opener.document.forms["caseManagementEntryForm"].demographicNo &&
                            window.parent.opener.document.forms["caseManagementEntryForm"].demographicNo.value === "<%=sessionBean.getDemographicNo()%>") {
                            //oscarLog("3");
                            window.parent.opener.pasteToEncounterNote(text);
                            if (print) {
                                printIframe();
                            }
                        } else if (window.parent.opener.document.encForm !== undefined) {
                            //oscarLog("4");
                            window.parent.opener.document.encForm.enTextarea.value = window.parent.opener.document.encForm.enTextarea.value + text;
                            if (print) {
                                printIframe();
                            }
                        } else if (window.parent.opener.document.getElementById(noteEditor) !== undefined) {
                            window.parent.opener.document.getElementById(noteEditor).value = window.parent.opener.document.getElementById(noteEditor).value + text;
                            if (print) {
                                printIframe();
                            }
                        } else if (pasteRx) {
                            writeToEncounter('<c:out value="${pageContext.request.contextPath}"/>',
                                print,
                                text,
                                '<c:out value="${requestScope.prefPharmacy}" escapeXml="true"/>',
                                '<c:out value="${sessionScope.RxSessionBean.providerNo}"/>',
                                '<c:out value="${sessionScope.RxSessionBean.demographicNo}"/>',
                                '<c:out value="${sessionScope.RxSessionBean.providerNo}"/>',
                                '<c:out value="${requestScope.providerName}" escapeXml="true"/>'
                            );
                        }
                    } else {
                        writeToEncounter('<c:out value="${pageContext.request.contextPath}"/>',
                            print,
                            text,
                            '<c:out value="${requestScope.prefPharmacy}" escapeXml="true"/>',
                            '<c:out value="${sessionScope.RxSessionBean.providerNo}"/>',
                            '<c:out value="${sessionScope.RxSessionBean.demographicNo}"/>',
                            '<c:out value="${sessionScope.RxSessionBean.providerNo}"/>',
                            '<c:out value="${requestScope.providerName}" escapeXml="true"/>'
                        );
                    }

                } catch (e) {
                    alert("ERROR: could not paste to EMR" + e);
                    if (print) {
                        printIframe();
                    }
                }

            }


            function addressSelect() {
                <c:if test="${not empty requestScope.addressName}">
                setDefaultAddr();
                <c:forEach var="addr" items="${requestScope.addressName}" varStatus="loop">
                if (document.getElementById("addressSel").value === "${loop.index}") {
                    frames['preview'].document.getElementById("clinicAddress").innerHTML = "${requestScope.address[loop.index]}";
                }
                </c:forEach>
                </c:if>

                <c:if test="${not empty requestScope.comment}">
                setComment();
                </c:if>
            }

        </script>

        <script type="text/javascript">
            const POLL_TIME = 1500;
            let counter = 0;

            var isSignatureDirty = false;
            var isSignatureSaved = false;
            <c:if test="${requestScope.showRxFaxBlock}">
            <c:set var="hasFaxNumber" value="${empty requestScope.pharmacyFax ? false : true}"/>
            </c:if>
            <c:if test="${requestScope.showRxFaxBlock}">
            window.hasFaxNumber = <c:set var="hasFaxNumber" value="${empty requestScope.pharmacyFax ? false : true}"/>;
            </c:if>

            <c:if test="${requestScope.sessionBean.stashSize > 0}">
            window.contextPath = "<c:out value="${pageContext.request.contextPath}"/>";
            window.scriptNo = <c:out value="${requestScope.sessionBean.getStashItem(0).script_no}"/>;
            </c:if>

            window.imageUrl = "${requestScope.imageUrl}";
            window.tempPath = '<%=System.getProperty("java.io.tmpdir").replaceAll("\\\\", "/")%>/signature_${requestScope.signatureRequestId}.jpg';
            window.requestIdKey = "${requestScope.signatureRequestId}";

            var requestIdKey = "${requestScope.signatureRequestId}";

        </script>
        <style media="all">
            * {
                font: 13px/1.231 arial, helvetica, clean, sans-serif;
            }

            .warning-note {
                background-color: #ffffcc;
                color: #cc6600;
                padding: 20px;
                border: 1px solid #cc6600;
                border-radius: 5px;
                display: none;
            }
        </style>

    </head>

    <body style="margin-top: 0; margin-left: 0;"
          onload="addressSelect();
                  printPharmacy(<c:out value="${ctx}"/>, '${requestScope.prefPharmacyId}',
                  '<bean:message key="oscarRx.printPharmacyInfo.paperSizeWarning"/>'); showFaxWarning();">

    <!-- added by vic, hsfo -->
    <c:if test="${requestScope.dx >= 0 && requestScope.dx < 7}">
    <div id="hsfoPop"
         style="border: ridge; background-color: ivory; width: 550px; height: 150px; position: absolute; left: 100px; top: 100px;">
        <form name="hsfoForm">
            <div style="text-align: center;"><BR>
                <table>
                    <tr>
                        <td colspan="3"><b>Please mark the corresponding symptom(s)
                            for the enrolled patient.</b></td>
                    </tr>
                    <tr>
                        <td>
                            <input type="checkbox" name="hsfo_Hypertension" value="1"
                                   <c:if test="${(requestScope.dx and 1) != 0}">checked</c:if>> Hypertension
                        </td>
                        <td>
                            <input type="checkbox" name="hsfo_Diabetes" value="2"
                                   <c:if test="${(requestScope.dx and 2) != 0}">checked</c:if>> Diabetes
                        </td>
                        <td>
                            <input type="checkbox" name="hsfo_Dyslipidemia" value="4"
                                   <c:if test="${(requestScope.dx and 4) != 0}">checked</c:if>> Dyslipidemia
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" style="text-align: center;">
                            <hr>
                            <input type="button" name="hsfo_submit" value="submit"
                                   onclick="toggleView(this.form);">
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </div>
    <div id="bodyView" style="display: none">
        </c:if>
        <c:if test="${requestScope.dx < 0 || requestScope.dx >= 7}">
        <div id="bodyView">
            </c:if>


            <table style="border-collapse: collapse; width: 100%; height: 100%; border: 0; padding: 0; margin: 0;"
                   id="AutoNumber1">
                <tr>
                    <td style="width: 100%; height: 0%; padding-left: 3px; padding-right: 3px; padding-top: 2px; padding-bottom: 2px"
                        colspan="2">

                    </td>
                </tr>

                <tr>
                    <td class="leftGreyLine" style="width: 100%; height: 100%; vertical-align: top;">
                        <table style="border-collapse: collapse; border-color: #111111; width: 100%; height: 100%;">

                            <tr>
                                <td style="display: flex; justify-content: start">
                                    <div class="DivContentPadding"><!-- src modified by vic, hsfo -->
                                        <c:if test="${requestScope.sessionBean.stashSize > 0}">
                                            <c:set var="iframeSrc">
                                                <c:choose>
                                                    <c:when test="${requestScope.dx < 0}">
                                                        Preview2.jsp?scriptId=${requestScope.sessionBean.getStashItem(0).script_no}&rePrint=${requestScope.reprint}&pharmacyId=${param.pharmacyId}
                                                    </c:when>
                                                    <c:when test="${requestScope.dx == 7}">
                                                        HsfoPreview.jsp?dxCode=7
                                                    </c:when>
                                                    <c:otherwise>
                                                        about:blank
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:set>
                                            <iframe id="preview"
                                                    name="preview"
                                                    style="width: 60vw; min-width: 600px; max-width: 700px; height: 75vh; border: none; display: block; margin: 0 auto;"
                                                    src="${iframeSrc}"
                                                    onload="<c:if test='${not empty requestScope.pharmacyAddress}'>
                                                            setTimeout(function() {
                                                            document.getElementById('preview').contentWindow.document.getElementById('pharmInfo').innerHTML = '${requestScope.pharmacyAddress}';
                                                            }, 50);
                                                            </c:if>">
                                            </iframe>
                                        </c:if>
                                    </div>
                                </td>

                                <td style="vertical-align: top;"><html:form action="/oscarRx/clearPending">
                                    <html:hidden property="action" value=""/>
                                    <div class="warning-note" id="faxWarningNote">
                                        <strong>Warning:</strong> faxing is disabled because no pharmacy fax number is
                                        available.<br><br>To enable faxing, close this window and select a pharmacy
                                        with a fax number before trying again.
                                    </div>
                                </html:form>

                                    <table style="padding: 10px; border-spacing: 0;">
                                        <c:if test="${not empty requestScope.address}">
                                            <tr>
                                                <td style="text-align: left;" colspan="2"><bean:message key="ViewScript.msgAddress"/>
                                                    <select name="addressSel" id="addressSel" onChange="addressSelect()"
                                                            style="width:200px;">
                                                        <c:forEach var="addr" items="${requestScope.addressName}"
                                                                   varStatus="loop">
                                                            <option value="${loop.index}"
                                                                    <c:if test="${sessionScope.RX_ADDR eq loop.index}">selected</c:if>>
                                                                    ${addr}
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                        </c:if>
                                        <tr>
                                            <td colspan="2" style="font-weight: bold;"><span><bean:message
                                                    key="ViewScript.msgActions"/></span>
                                            </td>
                                        </tr>

                                        <tr>
                                            <!--td width=10px></td-->
                                            <td>Page size:
                                                <select name="printPageSize" id="printPageSize"
                                                        style="height:20px;font-size:10px">
                                                    <c:forEach items="${requestScope.pageSizes}" var="pageSize">
                                                        <option value="${pageSize.value}"
                                                                <c:if test="${sessionScope.rxPageSize eq pageSize.value}">selected</c:if>>
                                                                ${pageSize.key}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td style="padding-bottom: 0"><span><input type="button"
                                                                                       value="<bean:message key="ViewScript.msgPrint"/>"
                                                                                       class="ControlPushButton"
                                                                                       style="width: 210px"
                                                                                       onclick="printIframe();"/></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="padding-top: 0"><span><input type="button"
                                                                                    <c:if test="${requestScope.reprint eq 'true'}">disabled</c:if>
                                                                                    value="Print &amp; Add to encounter note"
                                                                                    class="ControlPushButton"
                                                                                    style="width: 210px"
                                                                                    onclick="printPaste2Parent(true, false, true);"/></span>
                                            </td>
                                        </tr>
                                        <c:if test="${requestScope.showRxFaxBlock}">
                                            <tr>
                                                <td style="padding-bottom: 0">
                                                    <span>From Fax Number:</span>
                                                    <select id="faxNumber" name="faxNumber">
                                                        <c:forEach var="faxConfig" items="${requestScope.faxConfigs}">
                                                            <option value="${faxConfig.faxNumber}"
                                                                    <c:if test="${requestScope.providerFax eq faxConfig.faxNumber}">selected</c:if>>
                                                                    ${faxConfig.accountName}
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding-top: 0; padding-bottom: 0">
                                                        <span>
                                                            <input type="button"
                                                                   value="Fax"
                                                                   class="ControlPushButton"
                                                                   id="faxButton"
                                                                   style="width: 210px"
                                                                   onClick="sendFax(${param.scriptId}, ${requestScope.signatureRequestId});"
                                                                   disabled/>
                                                        </span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding-top: 0">
                                                    <span>
                                                        <input type="button"
                                                               value="Fax &amp; Add to encounter note"
                                                               class="ControlPushButton"
                                                               id="faxPasteButton"
                                                               style="width: 210px"
                                                               onClick="printPaste2Parent(false, true, true);
                                                                       sendFax(${param.scriptId}, ${requestScope.signatureRequestId});"
                                                               disabled/>
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:if>
                                        <tr>
                                            <!--td width=10px></td-->
                                            <td><span><input type="button"
                                                             value="<bean:message key="ViewScript.msgCreateNewRx"/>"
                                                             class="ControlPushButton"
                                                             style="width: 210px"
                                                             onClick="resetStash('<c:out value="${ctx}"/>');
                                                                     resetReRxDrugList('<c:out value="${ctx}"/>');
                                                                     closeRxPreviewBootstrapModal();"/></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <!--td width=10px></td-->
                                            <td><span><input type="button"
                                                             value="<bean:message key="ViewScript.msgBackToOscar"/>"
                                                             class="ControlPushButton" style="width: 210px"
                                                             onClick="clearPending('close');
                                                                        parent.window.close();"/></span>
                                            </td>
                                        </tr>

                                        <c:if test="${empty sessionScope.rePrint}">
                                            <tr>
                                                <td colspan="2" style="font-weight: bold">
                                                    <span><bean:message key="ViewScript.msgAddNotesRx"/></span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <textarea id="additionalNotes" style="width: 200px"
                                                              onchange="addNotes(${param.scriptId});"></textarea>
                                                    <input type="button" value="Additional Rx Notes"
                                                           onclick="addNotes(${param.scriptId});"/>
                                                </td>
                                            </tr>
                                        </c:if>
                                        <c:if test="${requestScope.showSignatureBlock}">
                                            <c:if test="${requestScope.sessionBean.stashSize == 0 || empty requestScope.sessionBean.getStashItem(0).digitalSignatureId}">
                                                <tr>
                                                    <td colspan="2" style="font-weight: bold"><span>Signature</span></td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="hidden"
                                                               name="${DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY}"
                                                               value="${requestScope.signatureRequestId}"/>
                                                        <iframe style="width:25vw; height:18vh; min-width: 275px; max-width: 300px" id="signatureFrame"
                                                                src="${pageContext.request.contextPath}/signature_pad/tabletSignature.jsp?inWindow=true&${DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY}=${requestScope.signatureRequestId}&saveToDB=true&demographicNo=${requestScope.sessionBean.demographicNo}&ModuleType=${ModuleType.PRESCRIPTION}"></iframe>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:if>
                                        <tr>
                                            <td colspan="2" style="font-weight: bold"><span><bean:message
                                                    key="ViewScript.msgDrugInfo"/></span></td>
                                        </tr>
                                        <c:forEach begin="0" end="${requestScope.sessionBean.stashSize - 1}" var="i">
                                            <c:set var="rx" value="${requestScope.sessionBean.getStashItem(i)}"/>
                                            <c:if test="${!rx.custom}">
                                                <tr>
                                                    <td>
                                                        <span>
                                                            <a href="javascript:ShowDrugInfo('${rx.genericName}');">${rx.genericName} (${rx.brandName}) </a>
                                                        </span>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </table>
                                </td>
                            </tr>
                            <tr style="height: 100%;">
                                <td></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td style="height: 0;" class="leftBottomGreyLine"></td>
                    <td style="height: 0;" class="leftBottomGreyLine"></td>
                </tr>
                <tr>
                    <td style="width: 100%; height: 0%;" colspan="2">&nbsp;</td>
                </tr>
            </table>
        </div>

    <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/oscarRx/printRx/PrintPreview.js"></script>

    <link rel="stylesheet" href="${ctx}/library/bootstrap/5.0.2/css/bootstrap.min.css" type="text/css"/>
    <script type="text/javascript" src="${ctx}/library/bootstrap/5.0.2/js/bootstrap.min.js"></script>

    </body>
</html:html>
