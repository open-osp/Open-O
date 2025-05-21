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

<%@ page import="oscar.OscarProperties, java.util.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo" %>
<%@ page import="org.oscarehr.util.DigitalSignatureUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>

<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.oscarehr.common.model.*" %>
<%@ page import="oscar.oscarProvider.data.ProviderData" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.oscarehr.common.model.enumerator.ModuleType" %>

<%
    LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
%>


<c:set var="roleName" value="${sessionScope.userrole},${sessionScope.user}"/>
<security:oscarSec roleName="${roleName}" objectName="_rx" rights="r" reverse="true">
    <c:redirect url="../securityError.jsp?type=_rx"/>
</security:oscarSec>


<html:html lang="en">

    <head>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/oscarRx/printRx/PrintPreview.js"></script>
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
            List<String> addressName = (List<String>) request.getAttribute("addressName");
            List<String> address = (List<String>) request.getAttribute("address");

            String prefPharmacy = (String) request.getAttribute("prefPharmacy");
            PharmacyInfo pharmacy = (PharmacyInfo) request.getAttribute("pharmacy");
            String comment = (String) request.getAttribute("comment");

            OscarProperties props = OscarProperties.getInstance();

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
                let useSC = false;
                let scAddress = "";
                const rxPageSize = $('printPageSize').value;
                console.log("rxPagesize  " + rxPageSize);

                <% if(addressName != null) { %>
                useSC = true;
                <%for(int i=0; i<addressName.size(); i++) { %>
                if (document.getElementById("addressSel").value === "<%=i%>") {
                    scAddress = "<%=Encode.forUriComponent(StringEscapeUtils.unescapeHtml(address.get(i)))%>";
                }
                <%
                }
                }
                %>
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
                    text = "";
                    <% if (props.isPropertyActive("rx_paste_asterisk")) { %>
                    text += "**********************************************************************************\n";
                    <% } %>

                    if (print) {
                        text += "Prescribed and printed by <%= Encode.forJavaScript(loggedInInfo.getLoggedInProvider().getFormattedName())%>\n";
                    } else if (fax) {
                        <%--    	 <% if(echartPreferencesMap.getOrDefault("echart_paste_fax_note", false)) {--%>
                        <% String timeStamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(Calendar.getInstance().getTime()); %>
                        // %>
                        text = "[Rx faxed to " + '<%= pharmacy!=null?StringEscapeUtils.escapeJavaScript(pharmacy.getName()):""%>' + " Fax#: " + '<%= pharmacy!=null?pharmacy.getFax():""%>';

                        <%--    	 <% if (rxPreferencesMap.getOrDefault("rx_paste_provider_to_echart", false)) { %>--%>
                        text += " prescribed by <%= Encode.forJavaScript(loggedInInfo.getLoggedInProvider().getFormattedName())%>";
                        <%--    	 <% } %>--%>
                        text += ", <%= timeStamp %>]\n";
                        <%--   		 <%--%>
                        <%--    	 }--%>
                        <%--    	 %>    	--%>
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
                    <% if (props.isPropertyActive("rx_paste_asterisk")) {
                            if(!prefPharmacy.trim().equals("")){ %>
                    text += "<%=prefPharmacy%>\n"
                    <% } %>
                    text += "****<%=Encode.forJavaScript(oscar.oscarProvider.data.ProviderData.getProviderName(sessionBean.getProviderNo()))%>********************************************************************************\n";
                    <% } %>

                    //we support pasting into orig encounter and new casemanagement
                    demographicNo = <%=sessionBean.getDemographicNo()%>;
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
                            writeToEncounter(<%=request.getContextPath() %>,
                                print,
                                text,
                                <%=Encode.forJavaScriptBlock(prefPharmacy)%>,
                                <%=sessionBean.getProviderNo()%>,
                                <%= sessionBean.getDemographicNo() %>,
                                <%= sessionBean.getProviderNo() %>,
                                <%=Encode.forUriComponent(ProviderData.getProviderName(sessionBean.getProviderNo()))%>
                            );
                        }
                    } else {
                        writeToEncounter(<%=request.getContextPath() %>,
                            print,
                            text,
                            <%=Encode.forJavaScriptBlock(prefPharmacy)%>,
                            <%=sessionBean.getProviderNo()%>,
                            <%= sessionBean.getDemographicNo() %>,
                            <%= sessionBean.getProviderNo() %>,
                            <%=Encode.forUriComponent(ProviderData.getProviderName(sessionBean.getProviderNo()))%>
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
                <% if(addressName != null) {
                 %>
                setDefaultAddr();
                <%      for(int i=0; i<addressName.size(); i++) {%>
                if (document.getElementById("addressSel").value === "<%=i%>") {
                    frames['preview'].document.getElementById("clinicAddress").innerHTML = "<%=address.get(i)%>";
                }
                <%       }
                      }%>

                <%if (comment != null){ %>
                setComment();
                <%}%>
            }

        </script>

        <script type="text/javascript">
            const POLL_TIME = 1500;
            let counter = 0;

            var isSignatureDirty = false;
            var isSignatureSaved = false;
            <% if (OscarProperties.getInstance().isRxFaxEnabled()) { %>
            var hasFaxNumber = <%= pharmacy != null && pharmacy.getFax() != null && !pharmacy.getFax().trim().isEmpty() ? "true" : "false" %>;
            <% } %>

            <% if (OscarProperties.getInstance().isRxFaxEnabled()) { %>
            window.hasFaxNumber = <%= pharmacy != null && pharmacy.getFax() != null && !pharmacy.getFax().trim().isEmpty() ? "true" : "false" %>;
            <% } %>

            <% if (sessionBean.getStashSize() > 0) { %>
            window.contextPath = "<%=request.getContextPath() %>";
            window.scriptNo = <%=sessionBean.getStashItem(0).getScript_no() %>;
            <% } %>

            window.imageUrl = "${requestScope.imageUrl}";
            window.tempPath = '<%=System.getProperty("java.io.tmpdir").replaceAll("\\\\", "/")%>/signature_${requestScope.signatureRequestId}.jpg';
            window.requestIdKey = "${requestScope.signatureRequestId}";


            <%--function signatureHandler(e) {--%>
            <%--    isSignatureDirty = e.isDirty;--%>
            <%--    isSignatureSaved = e.isSave;--%>
            <%--    e.target.onbeforeunload = null;--%>
            <%--    <% if (OscarProperties.getInstance().isRxFaxEnabled()) { //%>--%>
            <%--    let disabled = !hasFaxNumber || !e.isSave;--%>
            <%--    toggleFaxButtons(disabled);--%>
            <%--    <% } %>--%>
            <%--    if (e.isSave) {--%>
            <%--        <% if (OscarProperties.getInstance().isRxFaxEnabled()) { //%>--%>
            <%--        if (hasFaxNumber) {--%>
            <%--            e.target.onbeforeunload = unloadMess;--%>
            <%--        }--%>
            <%--        <% }--%>

            <%--        if (sessionBean.getStashSize() > 0) {--%>
            <%--        %>--%>
            <%--        try {--%>
            <%--            let signId = new URLSearchParams(e.storedImageUrl.split('?')[1]).get('digitalSignatureId')--%>
            <%--            this.setDigitalSignatureToRx(<%=request.getContextPath() %>, signId, <%=sessionBean.getStashItem(0).getScript_no() %>);--%>
            <%--        } catch (e) {--%>
            <%--            console.error(e);--%>
            <%--        }--%>
            <%--        <% } %>--%>

            <%--        refreshImage(<%=imageUrl%>, '<%=System.getProperty("java.io.tmpdir").replaceAll("\\\\", "/")%>/signature_<%=signatureRequestId%>.jpg');--%>
            <%--    }--%>
            <%--}--%>

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

    <body topmargin="0" leftmargin="0" vlink="#0000FF"
          onload="addressSelect();
                  printPharmacy(<c:out value="${ctx}"/>, '${requestScope.prefPharmacyId}',
                  '<bean:message key="oscarRx.printPharmacyInfo.paperSizeWarning"/>'); showFaxWarning();">

    <!-- added by vic, hsfo -->
    <c:if test="${requestScope.dx >= 0 && requestScope.dx < 7}">
    <div id="hsfoPop"
         style="border: ridge; background-color: ivory; width: 550px; height: 150px; position: absolute; left: 100px; top: 100px;">
        <form name="hsfoForm">
            <center><BR>
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
                        <td colspan="3" align="center">
                            <hr>
                            <input type="button" name="hsfo_submit" value="submit"
                                   onclick="toggleView(this.form);">
                        </td>
                    </tr>
                </table>
            </center>
        </form>
    </div>
    <div id="bodyView" style="display: none">
        </c:if>
        <c:if test="${requestScope.dx < 0 || requestScope.dx >= 7}">
        <div id="bodyView">
            </c:if>


            <table border="0" cellpadding="0" cellspacing="0"
                   style="border-collapse: collapse" bordercolor="#111111" width="100%"
                   id="AutoNumber1" height="100%">
                <tr>
                    <td width="100%"
                        style="padding-left: 3px; padding-right: 3px; padding-top: 2px; padding-bottom: 2px"
                        height="0%" colspan="2">

                    </td>
                </tr>

                <tr>
                    <td width="100%" class="leftGreyLine" height="100%" valign="top">
                        <table style="border-collapse: collapse" bordercolor="#111111"
                               width="100%" height="100%">

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
                                                    style="width: 465px; height: 90vh; border: none; display: block; margin: 0 auto;"
                                                    src="${iframeSrc}">
                                            </iframe>
                                        </c:if>
                                    </div>
                                </td>

                                <td valign=top><html:form action="/oscarRx/clearPending">
                                    <html:hidden property="action" value=""/>
                                    <div class="warning-note" id="faxWarningNote">
                                        <strong>Warning:</strong> faxing is disabled because no pharmacy fax number is
                                        available.</br></br>To enable faxing, close this window and select a pharmacy
                                        with a fax number before trying again.
                                    </div>
                                </html:form>

                                    <table cellpadding=10 cellspacingp=0>
                                        <c:if test="${not empty requestScope.address}">
                                            <tr>
                                                <td align="left" colspan="2"><bean:message key="ViewScript.msgAddress"/>
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
                                            <td colspan=2 style="font-weight: bold;"><span><bean:message
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
                                            <td style="padding-bottom: 0"><span><input type=button
                                                                                       value="<bean:message key="ViewScript.msgPrint"/>"
                                                                                       class="ControlPushButton"
                                                                                       style="width: 210px"
                                                                                       onClick="printIframe();"/></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="padding-top: 0"><span><input type=button
                                                                                    <c:if test="${requestScope.reprint eq 'true'}">disabled</c:if>
                                                                                    value="Print &amp; Add to encounter note"
                                                                                    class="ControlPushButton"
                                                                                    style="width: 210px"
                                                                                    onClick="printPaste2Parent(true, false, true);"/></span>
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
                                                            <input type=button
                                                                   value="Fax"
                                                                   class="ControlPushButton"
                                                                   id="faxButton"
                                                                   style="width: 210px"
                                                                   onClick="sendFax(${param.scriptId}, ${requestScope.signatureRequestId});"
                                                                   disabled/></span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding-top: 0">
                                                    <span>
                                                        <input type=button
                                                               value="Fax &amp; Add to encounter note"
                                                               class="ControlPushButton"
                                                               id="faxPasteButton"
                                                               style="width: 210px"
                                                               onClick="printPaste2Parent(false, true, true);
                                                                       sendFax(${param.scriptId}, ${requestScope.signatureRequestId});"
                                                               disabled/></span>
                                                </td>
                                            </tr>
                                        </c:if>
                                        <tr>
                                            <!--td width=10px></td-->
                                            <td><span><input type=button
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
                                            <td><span><input type=button
                                                             value="<bean:message key="ViewScript.msgBackToOscar"/>"
                                                             class="ControlPushButton" style="width: 210px"
                                                             onClick="clearPending('close');
                                                                        parent.window.close();"/></span>
                                            </td>
                                        </tr>

                                        <c:if test="${empty sessionScope.rePrint}">
                                            <tr>
                                                <td colspan=2 style="font-weight: bold">
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
                                                    <td colspan=2 style="font-weight: bold"><span>Signature</span></td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="hidden"
                                                               name="${DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY}"
                                                               value="${requestScope.signatureRequestId}"/>
                                                        <iframe style="width:400px; height:132px;" id="signatureFrame"
                                                                src="${pageContext.request.contextPath}/signature_pad/tabletSignature.jsp?inWindow=true&${DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY}=${requestScope.signatureRequestId}&saveToDB=true&demographicNo=${requestScope.sessionBean.demographicNo}&ModuleType=${ModuleType.PRESCRIPTION}"></iframe>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:if>
                                        <tr>
                                            <td colspan=2 style="font-weight: bold"><span><bean:message
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
                            <tr height="100%">
                                <td></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td height="0%" class="leftBottomGreyLine"></td>
                    <td height="0%" class="leftBottomGreyLine"></td>
                </tr>
                <tr>
                    <td width="100%" height="0%" colspan="2">&nbsp;</td>
                </tr>
            </table>
        </div>
    </div>
    </body>
</html:html>
