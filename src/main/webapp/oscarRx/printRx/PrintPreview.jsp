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
<%@ page import="org.oscarehr.ui.servlet.ImageRenderingServlet" %>


<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.managers.FaxManager" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.oscarehr.common.model.*" %>
<%@ page import="oscar.oscarProvider.data.ProviderData" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.oscarehr.common.model.enumerator.ModuleType" %>
<%@ page import="org.apache.logging.log4j.core.util.KeyValuePair" %>

<c:set var="loggedInInfo" value="${LoggedInInfo.getLoggedInInfoFromSession(pageContext.request)}" />

<c:set var="roleName$" value="${sessionScope.userrole},${sessionScope.user}" />
<c:set var="authed" value="true" />
<security:oscarSec roleName="${roleName$}" objectName="_rx" rights="r" reverse="true">
    <c:set var="authed" value="false" />
    <% response.sendRedirect("../securityError.jsp?type=_rx"); %>
</security:oscarSec>
<c:if test="${!authed}">
    <% return; %>
</c:if>


<html:html lang="en">

    <head>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/global.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/oscarRx/printRx/PrintPreview.js"></script>
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

        <c:set var="sessionBean" value="${pageScope.bean}" />
        <c:set var="reprint" value="${requestScope.reprint != null ? requestScope.reprint : 'false'}" />
        <c:set var="addressName" value="${requestScope.addressName}" />
        <c:set var="address" value="${requestScope.address}" />
        <c:set var="prefPharmacy" value="${requestScope.prefPharmacy}" />
        <c:set var="prefPharmacyId" value="${requestScope.prefPharmacyId}" />
        <c:set var="pharmacy" value="${requestScope.pharmacy}" />
        <c:set var="comment" value="${requestScope.comment}" />
        <c:set var="props" value="${OscarProperties.getInstance()}" />

        <c:if test="${reprint eq 'true'}">
            <c:set var="sessionBean" value="${sessionScope.tmpBeanRX}" />
        </c:if>

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

                <c:if test="${addressName != null}">
                useSC = true;
                <c:forEach var="i" begin="0" end="${addressName.size()-1}">
                if (document.getElementById("addressSel").value === "${i}") {
                    scAddress = "${Encode.forUriComponent(StringEscapeUtils.unescapeHtml(address.get(i)))}";
                }
                </c:forEach>
                </c:if>
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
        <%
            String signatureRequestId = "";
            String imageUrl = "";
            signatureRequestId = DigitalSignatureUtils.generateSignatureRequestId(loggedInInfo.getLoggedInProviderNo());
            imageUrl = request.getContextPath() + "/imageRenderingServlet?source=" + ImageRenderingServlet.Source.signature_preview.name() + "&" + DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY + "=" + signatureRequestId;
        %>
        <script type="text/javascript">
            const POLL_TIME = 1500;
            let counter = 0;

            var isSignatureDirty = false;
            var isSignatureSaved = false;
            <% if (OscarProperties.getInstance().isRxFaxEnabled()) { %>
            var hasFaxNumber = <%= pharmacy != null && pharmacy.getFax() != null && pharmacy.getFax().trim().length() > 0 ? "true" : "false" %>;
            <% } %>

            <% if (OscarProperties.getInstance().isRxFaxEnabled()) { %>
            window.hasFaxNumber = <%= pharmacy != null && pharmacy.getFax() != null && pharmacy.getFax().trim().length() > 0 ? "true" : "false" %>;
            <% } %>

            <% if (sessionBean.getStashSize() > 0) { %>
            window.contextPath = "<%=request.getContextPath() %>";
            window.scriptNo = <%=sessionBean.getStashItem(0).getScript_no() %>;
            <% } %>

            window.imageUrl = "<%=imageUrl%>";
            window.tempPath = '<%=System.getProperty("java.io.tmpdir").replaceAll("\\\\", "/")%>/signature_<%=signatureRequestId%>.jpg';
            window.requestIdKey = "<%=signatureRequestId %>";


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

            var requestIdKey = "<%=signatureRequestId %>";

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
                  printPharmacy(<c:out value="${ctx}"/>, '<%=prefPharmacyId%>', '<bean:message
                  key="oscarRx.printPharmacyInfo.paperSizeWarning"/>');
                  showFaxWarning();">

    <!-- added by vic, hsfo -->
    <%
        int hsfo_patient_id = sessionBean.getDemographicNo();
        oscar.form.study.HSFO.HSFODAO hsfoDAO = new oscar.form.study.HSFO.HSFODAO();
        int dx = hsfoDAO.retrievePatientDx(String.valueOf(hsfo_patient_id));
        if (dx >= 0 && dx < 7) {
            // dx>=0 means patient is enrolled in HSFO program
            // dx==7 means patient has all 3 symptoms, according to hsfo requirement, stop showing the popup
    %>
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
                        <td><input type="checkbox" name="hsfo_Hypertension" value="1"
                                <%= (dx&1)==0?"":"checked" %>> Hypertension
                        </td>
                        <td><input type="checkbox" name="hsfo_Diabetes" value="2"
                                <%= (dx&2)==0?"":"checked" %>> Diabetes
                        </td>
                        <td><input type="checkbox" name="hsfo_Dyslipidemia" value="4"
                                <%= (dx&4)==0?"":"checked" %>> Dyslipidemia
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" align="center">
                            <hr>
                            <input type="button" name="hsfo_submit" value="submit"
                                   onclick="toggleView(this.form);"></td>
                    </tr>
                </table>
            </center>
        </form>
    </div>
    <div id="bodyView" style="display: none">
        <% } else { %>
        <div id="bodyView">
            <% } %>


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
                                        <% if (sessionBean.getStashSize() > 0) {
                                            String iframeSrc;
                                            if (dx < 0) {
                                                iframeSrc = "Preview2.jsp?scriptId=" + sessionBean.getStashItem(0).getScript_no()
                                                        + "&rePrint=" + reprint
                                                        + "&pharmacyId=" + request.getParameter("pharmacyId");
                                            } else if (dx == 7) {
                                                iframeSrc = "HsfoPreview.jsp?dxCode=7";
                                            } else {
                                                iframeSrc = "about:blank";
                                            }
                                        %>
                                        <iframe id="preview"
                                                name="preview"
                                                style="width: 465px; height: 90vh; border: none; display: block; margin: 0 auto;"
                                                src="<%= iframeSrc %>">
                                        </iframe>
                                    </div>
                                    <% } %>
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
                                        <% //vecAddress=null;
                                            if (address != null) { %>
                                        <tr>
                                            <td align="left" colspan=2><bean:message key="ViewScript.msgAddress"/>
                                                <select name="addressSel" id="addressSel" onChange="addressSelect()"
                                                        style="width:200px;">
                                                    <% String rxAddr = (String) session.getAttribute("RX_ADDR");
                                                        for (int i = 0; i < addressName.size(); i++) {
                                                            String te = addressName.get(i);
                                                            String tf = address.get(i);%>

                                                    <option value="<%=i%>"
                                                            <% if ( rxAddr != null && rxAddr.equals(""+i)){ %>SELECTED<%}%>
                                                    ><%=te%>
                                                    </option>
                                                    <% }%>

                                                </select>
                                            </td>
                                        </tr>
                                        <% } %>
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
                                                    <%
                                                        String rxPageSize = (String) request.getSession().getAttribute("rxPageSize");
                                                        List<KeyValuePair> pageSizes = (List<KeyValuePair>) request.getAttribute("pageSizes");
                                                        for (int i = 0; i < pageSizes.size(); i++) {
                                                            String te = pageSizes.get(i).getKey();
                                                            String tf = pageSizes.get(i).getValue();%>
                                                    <option value="<%=tf%>"
                                                            <%if(rxPageSize!=null && rxPageSize.equals(tf)){%>SELECTED<%}%>
                                                    ><%=te%>
                                                    </option>
                                                    <% }%>
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
                                                    <%=reprint.equals("true") ? "disabled='true'" : ""%>
                                                                                    value="Print &amp; Add to encounter note"
                                                                                    class="ControlPushButton"
                                                                                    style="width: 210px"
                                                                                    onClick="printPaste2Parent(true, false, true);"/></span>
                                            </td>
                                        </tr>
                                        <% if (OscarProperties.getInstance().isRxFaxEnabled()) {
                                            FaxManager faxManager = SpringUtils.getBean(FaxManager.class);
                                            List<FaxConfig> faxConfigs = faxManager.getFaxGatewayAccounts(loggedInInfo);
                                        %>
                                        <tr>
                                            <td style="padding-bottom: 0">
                                                <span>From Fax Number:</span>
                                                <select id="faxNumber" name="faxNumber">
                                                    <%
                                                        for (FaxConfig faxConfig : faxConfigs) {
                                                    %>
                                                    <option value="<%=faxConfig.getFaxNumber()%>"
                                                            selected="<%=request.getAttribute("providerFax").equals(faxConfig.getFaxNumber())%>"><%=faxConfig.getAccountName()%>
                                                    </option>
                                                    <%
                                                        }
                                                    %>
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
                                                               onClick="sendFax(<%=request.getParameter("scriptId")%>, <%=signatureRequestId%>);"
                                                               disabled/></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="padding-top: 0"><span><input type=button
                                                                                    value="Fax &amp; Add to encounter note"
                                                                                    class="ControlPushButton"
                                                                                    id="faxPasteButton"
                                                                                    style="width: 210px"
                                                                                    onClick="printPaste2Parent(false, true, true);
                                                                                            sendFax(<%=request.getParameter("scriptId")%>, <%=signatureRequestId%>);"
                                                                                    disabled/></span>

                                            </td>
                                        </tr>

                                        <% } %>
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

                                        <%
                                            if (request.getSession().getAttribute("rePrint") == null) {%>

                                        <tr>
                                            <td colspan=2 style="font-weight: bold"><span><bean:message
                                                    key="ViewScript.msgAddNotesRx"/></span></td>
                                        </tr>
                                        <tr>
                                            <!--td width=10px></td-->
                                            <td>
                                                    <textarea id="additionalNotes" style="width: 200px"
                                                              onchange="addNotes(<%=request.getParameter("scriptId")%>);"></textarea>
                                                <input type="button" value="Additional Rx Notes"
                                                       onclick="addNotes(<%=request.getParameter("scriptId")%>);"/>
                                            </td>
                                        </tr>

                                        <%}%>
                                        <% if (OscarProperties.getInstance().isRxSignatureEnabled() && !OscarProperties.getInstance().getBooleanProperty("signature_tablet", "yes")) { %>
                                        <% if (sessionBean.getStashSize() == 0 || Objects.isNull(sessionBean.getStashItem(0).getDigitalSignatureId())) { %>
                                        <tr>
                                            <td colspan=2 style="font-weight: bold"><span>Signature</span></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="hidden"
                                                       name="<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>"
                                                       value="<%=signatureRequestId%>"/>
                                                <iframe style="width:400px; height:132px;" id="signatureFrame"
                                                        src="<%= request.getContextPath() %>/signature_pad/tabletSignature.jsp?inWindow=true&<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>=<%=signatureRequestId%>&saveToDB=true&demographicNo=<%=sessionBean.getDemographicNo()%>&<%=ModuleType.class.getSimpleName()%>=<%=ModuleType.PRESCRIPTION%>"></iframe>
                                            </td>
                                        </tr>
                                        <% } %>
                                        <%}%>
                                        <tr>
                                            <td colspan=2 style="font-weight: bold"><span><bean:message
                                                    key="ViewScript.msgDrugInfo"/></span></td>
                                        </tr>
                                        <%
                                            for (int i = 0; i < sessionBean.getStashSize(); i++) {
                                                oscar.oscarRx.data.RxPrescriptionData.Prescription rx
                                                        = sessionBean.getStashItem(i);

                                                if (!rx.isCustom()) {
                                        %>
                                        <tr>
                                            <td><span><a
                                                    href="javascript:ShowDrugInfo('<%= rx.getGenericName() %>');">
                            <%= rx.getGenericName() %> (<%= rx.getBrandName() %>) </a></span></td>
                                        </tr>
                                        <%
                                                }
                                            }
                                        %>
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
