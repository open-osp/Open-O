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
<%@ page import="oscar.oscarProvider.data.*, oscar.oscarRx.data.*,oscar.OscarProperties, oscar.oscarClinic.ClinicData, java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo" %>
<%@ page import="org.oscarehr.util.DigitalSignatureUtils"%>
<%@ page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="org.oscarehr.ui.servlet.ImageRenderingServlet"%>
<%! boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable(); %>


<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao"%>
<%@ page import="org.oscarehr.managers.FaxManager" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.oscarehr.PMmodule.service.ProviderManager" %>
<%@ page import="org.oscarehr.common.model.*" %>
<%@ page import="oscar.oscarProvider.data.ProviderData" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.oscarehr.common.model.enumerator.ModuleType" %>

<%
	OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
%>

<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<html:html lang="en">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="ViewScript.title" /></title>

<html:base />
<logic:notPresent name="RxSessionBean" scope="session">
	<logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
	<bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean"
		name="RxSessionBean" scope="session" />
	<logic:equal name="bean" property="valid" value="false">
		<logic:redirect href="error.html" />
	</logic:equal>
</logic:present>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
	<%!
		ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
	%>
<%
oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");
	Provider provider = providerManager.getProvider(bean.getProviderNo());
	String providerFax = provider.getWorkPhone();
	if(providerFax == null) {
		providerFax = "";
	}
	providerFax = providerFax.replaceAll("[^0-9]", "");

Vector vecPageSizes=new Vector();
vecPageSizes.add("A4 page");
vecPageSizes.add("A6 page");
vecPageSizes.add("Letter page");
Vector vecPageSizeValues=new Vector();
vecPageSizeValues.add("PageSize.A4");
vecPageSizeValues.add("PageSize.A6");
vecPageSizeValues.add("PageSize.Letter");
//are we printing in the past?
//String reprint = (String)request.getAttribute("rePrint") != null ? (String)request.getAttribute("rePrint") : "false";

String reprint = (String)request.getSession().getAttribute("rePrint") != null ? (String)request.getSession().getAttribute("rePrint") : "false";

String createAnewRx;
if(reprint.equalsIgnoreCase("true") ) {
    bean = (oscar.oscarRx.pageUtil.RxSessionBean)session.getAttribute("tmpBeanRX");
    createAnewRx = "window.location.href = '" + request.getContextPath() + "/oscarRx/SearchDrug.jsp'";
}
else {
	createAnewRx = "javascript:clearPending('')";
}

// for satellite clinics
Vector vecAddressName = null;
Vector vecAddress = null;
Vector vecAddressPhone = null;
Vector vecAddressFax = null;
OscarProperties props = OscarProperties.getInstance();
if(bMultisites) {
	String appt_no=(String)session.getAttribute("cur_appointment_no");
	String location = null;
	if (appt_no!=null) {
		Appointment result = appointmentDao.find(Integer.parseInt(appt_no));
		if (result!=null) location = result.getLocation();
	}

    oscar.oscarRx.data.RxProviderData.Provider rxprovider = new oscar.oscarRx.data.RxProviderData().getProvider(bean.getProviderNo());
    ProSignatureData sig = new ProSignatureData();
    boolean hasSig = sig.hasSignature(bean.getProviderNo());
    String doctorName = "";
    if (hasSig){
       doctorName = sig.getSignature(bean.getProviderNo());
    }else{
       doctorName = (rxprovider.getFirstName() + ' ' + rxprovider.getSurname());
    }
    doctorName = doctorName.replaceAll("\\d{6}","");
    doctorName = doctorName.replaceAll("\\-","");

    vecAddressName = new Vector();
    vecAddress = new Vector();
    vecAddressPhone = new Vector();
    vecAddressFax = new Vector();

    java.util.ResourceBundle rb = java.util.ResourceBundle.getBundle("oscarResources",request.getLocale());

	SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean(SiteDao.class);
	List<Site> sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user"));

	for (int i=0;i<sites.size();i++) {
		Site s = sites.get(i);
        vecAddressName.add(s.getName());
        vecAddress.add("<b>"+doctorName+"</b><br>"+s.getName()+"<br>"+s.getAddress() + "<br>" + s.getCity() + ", " + s.getProvince() + " " + s.getPostal() + "<br>"+rb.getString("RxPreview.msgTel")+": " + s.getPhone() + "<br>"+rb.getString("RxPreview.msgFax")+": " + s.getFax());
        if (s.getName().equals(location))
        	session.setAttribute("RX_ADDR",String.valueOf(i));
	}


} else if(props.getProperty("clinicSatelliteName") != null) {
    oscar.oscarRx.data.RxProviderData.Provider rxprovider = new oscar.oscarRx.data.RxProviderData().getProvider(bean.getProviderNo());
    ProSignatureData sig = new ProSignatureData();
    boolean hasSig = sig.hasSignature(bean.getProviderNo());
    String doctorName = "";
    if (hasSig){
       doctorName = sig.getSignature(bean.getProviderNo());
    }else{
       doctorName = (rxprovider.getFirstName() + ' ' + rxprovider.getSurname());
    }

    ClinicData clinic = new ClinicData();
    vecAddressName = new Vector();
    vecAddress = new Vector();
    vecAddressPhone = new Vector();
    vecAddressFax = new Vector();
    String[] temp0 = props.getProperty("clinicSatelliteName", "").split("\\|");
    String[] temp1 = props.getProperty("clinicSatelliteAddress", "").split("\\|");
    String[] temp2 = props.getProperty("clinicSatelliteCity", "").split("\\|");
    String[] temp3 = props.getProperty("clinicSatelliteProvince", "").split("\\|");
    String[] temp4 = props.getProperty("clinicSatellitePostal", "").split("\\|");
    String[] temp5 = props.getProperty("clinicSatellitePhone", "").split("\\|");
    String[] temp6 = props.getProperty("clinicSatelliteFax", "").split("\\|");
    java.util.ResourceBundle rb = java.util.ResourceBundle.getBundle("oscarResources",request.getLocale());

    for(int i=0; i<temp0.length; i++) {
        vecAddressName.add(temp0[i]);
        vecAddress.add("<b>"+Encode.forHtml(doctorName)+"</b><br>"+Encode.forHtml(temp0[i])+"<br>"+Encode.forHtml(temp1[i]) + "<br>" + temp2[i] + ", " + temp3[i] + " " + temp4[i] + "<br>"+rb.getString("RxPreview.msgTel")+": " + temp5[i] + "<br>"+rb.getString("RxPreview.msgFax")+": " + temp6[i]);
    }
}
String comment = request.getSession().getAttribute("comment")!=null?request.getSession().getAttribute("comment").toString():"";
request.getSession().removeAttribute("comment");
String pharmacyId = request.getParameter("pharmacyId");
RxPharmacyData pharmacyData = new RxPharmacyData();
PharmacyInfo pharmacy = null;

String prefPharmacy = "";
String prefPharmacyId="";
if (pharmacyId != null && !"null".equalsIgnoreCase(pharmacyId)) {
	pharmacy = pharmacyData.getPharmacy(pharmacyId);    
	if( pharmacy != null ) {
    	prefPharmacy = pharmacy.getName().replace("'", "\\'");
    	prefPharmacyId=String.valueOf(pharmacy.getId());
    	prefPharmacy=prefPharmacy.trim();
    	prefPharmacyId=prefPharmacyId.trim();
	}
}

String userAgent = request.getHeader("User-Agent");
String browserType = "";
if (userAgent != null) {
	if (userAgent.toLowerCase().indexOf("ipad") > -1) {
		browserType = "IPAD";
	} else {
		browserType = "ALL";
	}
}
%>
<%--<link rel="stylesheet" type="text/css" href="styles.css" />--%>

<script type="text/javascript" src="../share/javascript/prototype.js"></script>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>

<script type="text/javascript">
    function resetStash(){
               var url="<c:out value="${ctx}"/>" + "/oscarRx/deleteRx.do?parameterValue=clearStash";
               var data = "";
               new Ajax.Request(url, {method: 'post',parameters:data,onSuccess:function(transport){
                            updateCurrentInteractions();
                }});
               parent.document.getElementById('rxText').innerHTML="";//make pending prescriptions disappear.
               parent.document.getElementById('searchString').focus();
    }
    function resetReRxDrugList(){
        var url="<c:out value="${ctx}"/>" + "/oscarRx/deleteRx.do?parameterValue=clearReRxDrugList";
               var data = "";
               new Ajax.Request(url, {method: 'post',parameters:data,onSuccess:function(transport){
                }});
    }
    function updateCurrentInteractions(){
        new Ajax.Request("GetmyDrugrefInfo.do?method=findInteractingDrugList", {method:'get',onSuccess:function(transport){
                            new Ajax.Request("UpdateInteractingDrugs.jsp", {method:'get',onSuccess:function(transport){
                                            var str=transport.responseText;
                                            str=str.replace('<script type="text/javascript">','');
                                            str=str.replace(/<\/script>/,'');
                                            eval(str);
                                            //oscarLog("str="+str);
                                            <oscar:oscarPropertiesCheck property="MYDRUGREF_DS" value="yes">
                                              callReplacementWebService("GetmyDrugrefInfo.do?method=view",'interactionsRxMyD');
                                             </oscar:oscarPropertiesCheck>
                                        }});
                            }});
    }

    function onPrint2(method, scriptId) {
        var useSC=false;
        var scAddress="";
        var rxPageSize=$('printPageSize').value;
        console.log("rxPagesize  "+rxPageSize);

        <% if(vecAddressName != null) { %>
            useSC=true;
            <%for(int i=0; i<vecAddressName.size(); i++) {%>
	            if(document.getElementById("addressSel").value=="<%=i%>") {
    	            scAddress="<%=Encode.forUriComponent(StringEscapeUtils.unescapeHtml((String)vecAddress.get(i)))%>";
                }
			<%}
        }%>
	    let action="../form/createcustomedpdf?__title=Rx&__method=" +  method+"&useSC="+useSC+"&scAddress="+scAddress+"&rxPageSize="+rxPageSize+"&scriptId="+scriptId;
	    document.getElementById("preview").contentWindow.document.getElementById("preview2Form").action = action;
	    if (method !== "oscarRxFax"){
		    document.getElementById("preview").contentWindow.document.getElementById("preview2Form").target="_blank";
	    }
	    document.getElementById("preview").contentWindow.document.getElementById("preview2Form").submit();

		return true;
    }

function setComment(){
    frames['preview'].document.getElementById('additNotes').innerHTML = '<%=Encode.forJavaScript(comment.replaceAll("\n", "<br>"))%>';
    frames['preview'].document.getElementsByName('additNotes')[0].value = frames['preview'].document.getElementById('additNotes').innerHTML ;
}

function setDefaultAddr(){
    var url = "setDefaultAddr.jsp";
    var ran_number=Math.round(Math.random()*1000000);
    var addr = encodeURIComponent(document.getElementById('addressSel').value);
    var params = "addr="+addr+"&rand="+ran_number;
    new Ajax.Request(url, {method: 'post',parameters:params});
}




function addNotes(){


    var url = "AddRxComment.jsp";
    var ran_number=Math.round(Math.random()*1000000);
    var comment = encodeURIComponent(document.getElementById('additionalNotes').value);
    var params = "scriptNo=<%=request.getAttribute("scriptId")%>&comment="+comment+"&rand="+ran_number;  //]
    new Ajax.Request(url, {method: 'post',parameters:params});        
    frames['preview'].document.getElementById('additNotes').innerHTML =  document.getElementById('additionalNotes').value.replace(/\n/g, "<br>");
    frames['preview'].document.getElementsByName('additNotes')[0].value=  document.getElementById('additionalNotes').value.replace(/\n/g, "\r\n");
}


function printIframe(){
	var browserName=navigator.appName;
	   if (browserName=="Microsoft Internet Explorer")
		{
            alert("Use of Microsoft Internet Explorer is not permitted")
		}
		else
		{
			if ('function' === typeof window.onbeforeunload) {
				window.onbeforeunload = null;
			}

			preview.focus();
			preview.print();

			self.onfocus = function () {
				self.setTimeout(
					function(){
						self.parent.close();
					}, 1000);
			};
			self.focus();
		}
	}

function printPaste2Parent(print, fax, pasteRx){
    //console.log("in printPaste2Parent");
   try{
      text ="";
      <% if (props.isPropertyActive("rx_paste_asterisk")) { %>
	   text += "**********************************************************************************\n";
     <% } %>

     if(print) {
	   text += "Prescribed and printed by <%= Encode.forJavaScript(loggedInInfo.getLoggedInProvider().getFormattedName())%>\n";
     } else if(fax) {
<%--    	 <% if(echartPreferencesMap.getOrDefault("echart_paste_fax_note", false)) {--%>
    		 <% String timeStamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(Calendar.getInstance().getTime()); %>
    	 // %>
    	 	text ="[Rx faxed to "+'<%= pharmacy!=null?StringEscapeUtils.escapeJavaScript(pharmacy.getName()):""%>'+" Fax#: "+'<%= pharmacy!=null?pharmacy.getFax():""%>';

<%--    	 <% if (rxPreferencesMap.getOrDefault("rx_paste_provider_to_echart", false)) { %>--%>
    		text += " prescribed by <%= Encode.forJavaScript(loggedInInfo.getLoggedInProvider().getFormattedName())%>";
<%--    	 <% } %>--%>
   			text += ", <%= timeStamp %>]\n";
<%--   		 <%--%>
<%--    	 }--%>
<%--    	 %>    	--%>
     }

	if(pasteRx) {
		if (document.all){
			text += preview.document.forms[0].rx_no_newlines.value
		} else {
			text += preview.document.forms[0].rx_no_newlines.value + "\n";
		}

		if (document.getElementById('additionalNotes') !== null) {
			text += document.getElementById('additionalNotes').value + "\n";
		}
	}
	   <% if (props.isPropertyActive("rx_paste_asterisk")) {
	   		if(prefPharmacy!=null && prefPharmacy.trim()!=""){ %>
       			text += "<%=prefPharmacy%>\n"
	   		<% } %>
		  text += "****<%=Encode.forJavaScript(oscar.oscarProvider.data.ProviderData.getProviderName(bean.getProviderNo()))%>********************************************************************************\n";
	   <% } %>

      //we support pasting into orig encounter and new casemanagement
      demographicNo = <%=bean.getDemographicNo()%>;
      noteEditor = "noteEditor"+demographicNo;
      if (window.parent.opener) {
		  if (window.parent.opener.document.forms["caseManagementEntryForm"] != undefined &&
				  window.parent.opener.document.forms["caseManagementEntryForm"].demographicNo &&
				  window.parent.opener.document.forms["caseManagementEntryForm"].demographicNo.value === "<%=bean.getDemographicNo()%>") {
			  //oscarLog("3");
			  window.parent.opener.pasteToEncounterNote(text);
              if (print) {
                  printIframe();
              }
		  } else if (window.parent.opener.document.encForm != undefined) {
			  //oscarLog("4");
			  window.parent.opener.document.encForm.enTextarea.value = window.parent.opener.document.encForm.enTextarea.value + text;
              if (print) {
                  printIframe();
              }
		  } else if (window.parent.opener.document.getElementById(noteEditor) != undefined) {
			  window.parent.opener.document.getElementById(noteEditor).value = window.parent.opener.document.getElementById(noteEditor).value + text;
              if (print) {
                  printIframe();
              }
		  } else if (pasteRx) {
				  writeToEncounter(print, text);
		  }
	  } else {
		  writeToEncounter(print, text);
	  }
      
   }catch (e){
      alert ("ERROR: could not paste to EMR" + e);
      if (print) { printIframe();}
   }
   
}

	function writeToEncounter(print, text) {
    	try {
			var url = "<%=request.getContextPath() %>/oscarRx/WriteToEncounter.do";
			var prefPharmacy = "<%=prefPharmacy != null ? Encode.forJavaScriptBlock(prefPharmacy) : ""%>";
			new Ajax.Request(url, {method: 'post',
				parameters: "prefPharmacy=" + encodeURIComponent(prefPharmacy) +
						"&additionalNotes=" +
						"&body="+ encodeURIComponent(text),
				onSuccess:function(ret){
					//console.log("success")
					if (print) {
						printIframe();
					}
					openEncounter();
				},
				onError: function(e) {
					alert("ERROR: could not paste to EMR" + e);
					if (print) {
						printIframe();
					}
					openEncounter();
				}});
		} catch (e) {
			alert("ERROR: could not paste to EMR" + e);
		}
	}

	function openEncounter() {
		var windowprops = "height=710,width=1024,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=20,left=20";
		var currentDate = new Date().toISOString().substring(0, 10);
		var url = "../oscarEncounter/IncomingEncounter.do?providerNo=<%= bean.getProviderNo() %>&demographicNo=<%= bean.getDemographicNo() %>&curProviderNo=<%= bean.getProviderNo() %>&userName=<%=Encode.forUriComponent(ProviderData.getProviderName(bean.getProviderNo()))%>&curDate=" + currentDate;

		if (window.parent.opener && window.parent.opener.document.forms["caseManagementEntryForm"] != undefined) {
			// redirect if encounter window open
			window.parent.opener.location = url;
			return window.parent.opener;
		}

		return window.open(url, "encounter", windowprops);
	}

var rxToPaste = null;

function pasteRxToEchart() {
    var encounterWindow = openEncounter();
    encounterWindow.rxToPaste = rxToPaste;
}

function addressSelect() {
   <% if(vecAddressName != null) {
    %>
        setDefaultAddr();
   <%      for(int i=0; i<vecAddressName.size(); i++) {%>
	    if(document.getElementById("addressSel").value=="<%=i%>") {
    	       frames['preview'].document.getElementById("clinicAddress").innerHTML="<%=vecAddress.get(i)%>";
            }
<%       }
      }%>

    <%if (comment != null){ %>
       setComment();
    <%}%>


}



function popupPrint(vheight,vwidth,varpage) { //open a new popup window
	  var page = "" + varpage;
	  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
	  var popup=window.open(page, "groupno", windowprops);
	  if (popup != null) {
	    if (popup.opener == null) {
	      popup.opener = self;
	    }
	    popup.focus();
	}
}

function resizeFrame(height) {
	document.getElementById("preview").height = (parseInt(height) + 10) + "px";
}

</script>
<%
String signatureRequestId = "";
String imageUrl = "";
signatureRequestId = DigitalSignatureUtils.generateSignatureRequestId(loggedInInfo.getLoggedInProviderNo());
imageUrl = request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_preview.name()+"&"+DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY+"="+signatureRequestId;
%>
<script type="text/javascript">
var POLL_TIME=1500;
var counter=0;
var isRxFaxEnabled = "<%=OscarProperties.getInstance().isRxFaxEnabled()%>";

function refreshImage()
{
	counter=counter+1;
	if (frames["preview"].document.getElementById("signature") != null) {
		frames["preview"].document.getElementById("signature").src = "<%=imageUrl%>&rand=" + counter;
	}
	frames['preview'].document.getElementById('imgFile').value='<%=System.getProperty("java.io.tmpdir").replaceAll("\\\\", "/")%>/signature_<%=signatureRequestId%>.jpg';
}

function sendFax() {
	if ('function' === typeof window.onbeforeunload) {
		window.onbeforeunload = null;
	}
	let faxNumber = document.getElementById('faxNumber');
	frames['preview'].document.getElementById('finalFax').value = faxNumber.options[faxNumber.selectedIndex].value;
	frames['preview'].document.getElementById('pdfId').value='<%=signatureRequestId%>';
   	onPrint2('oscarRxFax', "<%=request.getParameter("scriptId")%>");

}

function unloadMess(){
    mess = "Signature found, but fax has not been sent.";
    if (isSignatureDirty) { mess = "Signature changed, but not saved and fax not sent."; }
    return mess;
}

var isSignatureDirty = false;
var isSignatureSaved = false;
<% if (OscarProperties.getInstance().isRxFaxEnabled()) { %>
	var hasFaxNumber = <%= pharmacy != null && pharmacy.getFax() != null && pharmacy.getFax().trim().length() > 0 ? "true" : "false" %>;
<% } %>
function signatureHandler(e) {
	isSignatureDirty = e.isDirty;
	isSignatureSaved = e.isSave;
	e.target.onbeforeunload = null;
	<% if (OscarProperties.getInstance().isRxFaxEnabled()) { //%>
	let disabled = !hasFaxNumber || !e.isSave;
	toggleFaxButtons(disabled);
	<% } %>
	if (e.isSave) {
		<% if (OscarProperties.getInstance().isRxFaxEnabled()) { //%>
		if (hasFaxNumber) {
			e.target.onbeforeunload = unloadMess;
		}
		<% }

		if (bean.getStashSize() > 0) {
		%>
		try {
			let signId = new URLSearchParams(e.storedImageUrl.split('?')[1]).get('digitalSignatureId')
			if(signId === undefined || signId === '') {
				alert("Error while processing your signature, please try again.");
			} else {
				this.setDigitalSignatureToRx(signId, <%=bean.getStashItem(0).getScript_no() %>);
				refreshImage();
			}
		} catch (e) {
			console.error(e);
		}
		<% } %>

	}
}

function setDigitalSignatureToRx(digitalSignatureId, scriptId) {
	new Ajax.Request('<%=request.getContextPath() %>/oscarRx/saveDigitalSignature.do?method=saveDigitalSignature'
			+ '&digitalSignatureId=' + digitalSignatureId
			+ "&scriptId=" + scriptId, {
		method: 'get'
	});
}

function toggleFaxButtons(disabled) {
	document.getElementById("faxButton").disabled = disabled;
	document.getElementById("faxPasteButton").disabled = disabled;
}

function enableExistingSignature() {
	toggleFaxButtons(false);
	frames["preview"].document.onreadystatechange = function(event, readystate) {
		if (frames["preview"].document.readyState === "complete") {
			refreshImage();
		}
	}
}

function showFaxWarning() {
	if (typeof hasFaxNumber !== 'undefined' && !hasFaxNumber) {
		document.getElementById("faxWarningNote").style.display = "block";
	}
}

var requestIdKey = "<%=signatureRequestId %>";

</script>
	<style media="all">
        * {
	        font:13px/1.231 arial,helvetica,clean,sans-serif;
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
	onload="addressSelect();printPharmacy('<%=prefPharmacyId%>');showFaxWarning();">

<!-- added by vic, hsfo -->
<%
	int hsfo_patient_id = bean.getDemographicNo();
	oscar.form.study.HSFO.HSFODAO hsfoDAO = new oscar.form.study.HSFO.HSFODAO();
	int dx = hsfoDAO.retrievePatientDx(String.valueOf(hsfo_patient_id));
	if (dx >=0 && dx < 7) {
		// dx>=0 means patient is enrolled in HSFO program
		// dx==7 means patient has all 3 symptoms, according to hsfo requirement, stop showing the popup
%>
<script type="text/javascript" language="javascript">
function toggleView(form) {
  var dxCode = (form.hsfo_Hypertension.checked?1:0)+(form.hsfo_Diabetes.checked?2:0)+(form.hsfo_Dyslipidemia.checked?4:0);
  // send dx code to HsfoPreview.jsp so that it will be displayed and persisted there
  document.getElementById("hsfoPop").style.display="none";
  document.getElementById("bodyView").style.display="block";
  document.getElementById("preview").src="HsfoPreview.jsp?dxCode="+dxCode;
}

</script>
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
			<%= (dx&1)==0?"":"checked" %>> Hypertension</td>
		<td><input type="checkbox" name="hsfo_Diabetes" value="2"
			<%= (dx&2)==0?"":"checked" %>> Diabetes</td>
		<td><input type="checkbox" name="hsfo_Dyslipidemia" value="4"
			<%= (dx&4)==0?"":"checked" %>> Dyslipidemia</td>
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
				<td>
				<div class="DivContentPadding"><!-- src modified by vic, hsfo -->
					<% if (bean.getStashSize() > 0) { %>
						<iframe id='preview' name='preview' width=420px height=890px
							src="<%= dx<0?"Preview2.jsp?scriptId="+bean.getStashItem(0).getScript_no()+"&rePrint="+reprint+"&pharmacyId="+request.getParameter("pharmacyId"):dx==7?"HsfoPreview.jsp?dxCode=7":"about:blank" %>"
							align=center border=0 frameborder=0></iframe></div>
					<% } %>
				</td>

				<td valign=top><html:form action="/oscarRx/clearPending">
					<html:hidden property="action" value="" />
					<div class="warning-note" id="faxWarningNote">
						<strong>Warning:</strong> faxing is disabled because no pharmacy fax number is available.</br></br>To enable faxing, close this window and select a pharmacy with a fax number before trying again.
					</div>
				</html:form>
							<script type="text/javascript">
                                function clearPending(action){
                                    document.forms.RxClearPendingForm.action.value = action;
                                    document.forms.RxClearPendingForm.submit();
                                }

                                function clearPendingFax(){
                                    parent.window.location = "../oscarRx/close.html";
                                    parent.myLightWindow.deactivate();
								}

                                function ShowDrugInfo(drug){
                                    window.open("drugInfo.do?GN=" + escape(drug), "_blank",
                                        "location=no, menubar=no, toolbar=no, scrollbars=yes, status=yes, resizable=yes");
                                }


                                function printPharmacy(id){
                                    //ajax call to get all info about a pharmacy
                                    //use json to write to html
	                                if(! id) {
										return;
	                                }
                                    var url="<c:out value="${ctx}"/>"+"/oscarRx/managePharmacy2.do?";
                                    var data="method=getPharmacyInfo&pharmacyId="+id;
                                    new Ajax.Request(url, {method: 'get',parameters:data, onSuccess:function(transport){
                                        var json=transport.responseText.evalJSON();

                                            if(json!=null){
                                                var text=json.name+"<br>"+json.address+"<br>"+json.city+", "+json.province+", "
                                                    +json.postalCode+"<br>Tel:"+json.phone1+" "+json.phone2+"<br>Fax:"+json.fax+"<br>Email:"+json.email+"<br>Note:"+json.notes;

                                                    text+='<br><br><a class="noprint" style="text-align:center;" onclick="parent.reducePreview();" href="javascript:void(0);">Remove Pharmacy Info</a>';
													text += "<input type='hidden' name='pharmacyInfo' value=" + id + " />"
                                                expandPreview(text);
                                            }
                                        }});

                                }
                                function expandPreview(text){
                                    parent.document.getElementById('lightwindow_container').style.width="1140px";
									// parent.document.getElementById('lightwindow_container').style.left="-600px"; // commented to resolve a UI alignment issue causing the print-rx window to shift off-screen to the left.
                                    parent.document.getElementById('lightwindow_contents').style.width="1120px";
                                    document.getElementById('preview').style.width="600px";
                                    frames['preview'].document.getElementById('pharmInfo').innerHTML=text;
                                    //frames['preview'].document.getElementById('removePharm').show();
                                    $("selectedPharmacy").innerHTML='<bean:message key="oscarRx.printPharmacyInfo.paperSizeWarning"/>';

                                }
                                function reducePreview(){
                                    parent.document.getElementById('lightwindow_container').style.width="1000px";
                                    parent.document.getElementById('lightwindow_contents').style.width="980px";
                                    document.getElementById('preview').style.width="460px";
                                    frames['preview'].document.getElementById('pharmInfo').innerHTML="";
                                    $("selectedPharmacy").innerHTML="";
                                }
                            </script>

				<table cellpadding=10 cellspacingp=0>
					<% //vecAddress=null;
                                        if(vecAddress != null) { %>
					<tr>
						<td align="left" colspan=2><bean:message key="ViewScript.msgAddress"/>
                                                    <select	name="addressSel" id="addressSel" onChange="addressSelect()" style="width:200px;" >
							<% String rxAddr = (String) session.getAttribute("RX_ADDR");
                                                          for (int i =0; i < vecAddressName.size();i++){
					                 String te = (String) vecAddressName.get(i);
                                                         String tf = (String) vecAddress.get(i);%>

							<option value="<%=i%>"
								<% if ( rxAddr != null && rxAddr.equals(""+i)){ %>SELECTED<%}%>
                                                                ><%=te%></option>
							<%  }%>

                                                    </select>
                                                </td>
					</tr>
					<% } %>
					<tr>
						<td colspan=2 style="font-weight: bold;"><span><bean:message key="ViewScript.msgActions"/></span>
						</td>
					</tr>

                                        <tr>
                                            <!--td width=10px></td-->
                                            <td>Page size:
                                                <select name="printPageSize" id="printPageSize" style="height:20px;font-size:10px" >
                                                     <%
                                                     String rxPageSize=(String)request.getSession().getAttribute("rxPageSize");
                                                     for(int i=0;i<vecPageSizes.size();i++){
                                                                String te=(String)vecPageSizes.get(i);
                                                                String tf=(String)vecPageSizeValues.get(i);%>
                                                    <option value="<%=tf%>" <%if(rxPageSize!=null && rxPageSize.equals(tf)){%>SELECTED<%}%>
                                                        ><%=te%>
                                                    </option>
                                                    <%  }%>
                                                </select>
                                            </td>
                                        </tr>
<%--					<tr>--%>
<%--						<!--td width=10px></td-->--%>
<%--						<td>--%>
<%--						<span>--%>
<%--							<input type=button value="Print PDF" class="ControlPushButton" style="width: 150px" onClick="<%=reprint.equalsIgnoreCase("true") ? "javascript:return onPrint2('rePrint', "+request.getParameter("scriptId")+");" : "javascript:return onPrint2('print', "+request.getParameter("scriptId")+");" %>" />--%>
<%--						</span>--%>
<%--						</td>--%>
<%--					</tr>--%>

					<tr>
						<!--td width=10px></td-->
						<td style="padding-bottom: 0"><span><input type=button value="<bean:message key="ViewScript.msgPrint"/>"
							class="ControlPushButton" style="width: 210px"
							onClick="javascript:printIframe();" /></span></td>
					</tr>
					<tr>
						<td style="padding-top: 0"><span><input type=button
							<%=reprint.equals("true")?"disabled='true'":""%> value="Print &amp; Add to encounter note"
							class="ControlPushButton" style="width: 210px"
							onClick="printPaste2Parent(true, false, true);" /></span></td>
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
								for( FaxConfig faxConfig : faxConfigs ) {
							%>
									<option value="<%=faxConfig.getFaxNumber()%>" selected="<%=providerFax.equals(faxConfig.getFaxNumber())%>"><%=faxConfig.getAccountName()%></option>
							<%
								}
							%>
							</select>
						</td>
					</tr>
					<tr>
						<td style="padding-top: 0; padding-bottom: 0"><span><input type=button value="Fax"
										 class="ControlPushButton" id="faxButton" style="width: 210px"
										 onClick="sendFax();" disabled/></span>
						</td>
					</tr>
					<tr>
                            <td style="padding-top: 0"><span><input type=button value="Fax &amp; Add to encounter note"
                                    class="ControlPushButton" id="faxPasteButton" style="width: 210px"
                                    onClick="printPaste2Parent(false, true, true);sendFax();" disabled/></span>

                           </td>
                    </tr>

                    <% } %>
					<tr>
						<!--td width=10px></td-->
						<td><span><input type=button
							value="<bean:message key="ViewScript.msgCreateNewRx"/>" class="ControlPushButton"
                                                        style="width: 210px"  onClick="resetStash();resetReRxDrugList();javascript:parent.myLightWindow.deactivate();" /></span></td>
					</tr>
					<tr>
						<!--td width=10px></td-->
						<td><span><input type=button value="<bean:message key="ViewScript.msgBackToOscar"/>"
							class="ControlPushButton" style="width: 210px" onClick="javascript:clearPending('close');parent.window.close();" /></span></td>
					</tr>
<%--                                       <%if(prefPharmacy.length()>0 && prefPharmacyId.length()>0){   %>--%>
<%--                                           <tr><td><span><input id="selectPharmacyButton" type=button value="<bean:message key='oscarRx.printPharmacyInfo.addPharmacyButton'/>" class="ControlPushButton" style="width:150px;"--%>
<%--                                                             onclick="printPharmacy('<%=prefPharmacyId%>','<%=prefPharmacy%>');"/>--%>
<%--                                                </span>--%>

<%--                                            </td>--%>
<%--                                        </tr><%}%>--%>
<%--                                        <tr>--%>
<%--                                            <td>--%>
<%--                                                <a id="selectedPharmacy" style="color:red"></a>--%>
<%--                                            </td>--%>
<%--                                        </tr>--%>

                                        <%
                        if (request.getSession().getAttribute("rePrint") == null ){%>

                                        <tr>
						<td colspan=2 style="font-weight: bold"><span><bean:message key="ViewScript.msgAddNotesRx"/></span></td>
					</tr>
					<tr>
                                                <!--td width=10px></td-->
						<td>
							<textarea id="additionalNotes" style="width: 200px" onchange="javascript:addNotes();" ></textarea>
							<input type="button" value="Additional Rx Notes" onclick="javascript:addNotes();"/>
						</td>
					</tr>

                                        <%}%>
					<% if (OscarProperties.getInstance().isRxSignatureEnabled() && !OscarProperties.getInstance().getBooleanProperty("signature_tablet", "yes")) { %>
						<% if (bean.getStashSize() == 0 || Objects.isNull(bean.getStashItem(0).getDigitalSignatureId())) { %>
							<tr>
								<td colspan=2 style="font-weight: bold"><span>Signature</span></td>
							</tr>
							<tr>
								<td>
									<input type="hidden" name="<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>"
										   value="<%=signatureRequestId%>"/>
									<iframe style="width:500px; height:132px;" id="signatureFrame"
											src="<%= request.getContextPath() %>/signature_pad/tabletSignature.jsp?inWindow=true&<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>=<%=signatureRequestId%>&saveToDB=true&demographicNo=<%=bean.getDemographicNo()%>&<%=ModuleType.class.getSimpleName()%>=<%=ModuleType.PRESCRIPTION%>"></iframe>
								</td>
							</tr>
						<% } %>
		            <%}%>
                    <tr>
						<td colspan=2 style="font-weight: bold"><span><bean:message key="ViewScript.msgDrugInfo"/></span></td>
					</tr>
					<%
                        for(int i=0; i<bean.getStashSize(); i++){
                            oscar.oscarRx.data.RxPrescriptionData.Prescription rx
                                = bean.getStashItem(i);

                            if (! rx.isCustom()){
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
</body>
</html:html>
