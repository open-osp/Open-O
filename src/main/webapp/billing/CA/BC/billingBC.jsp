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

<%@page import="java.net.URLEncoder"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="oscar.oscarDemographic.data.*"%>
<%@page import="java.text.*, java.util.*, oscar.oscarBilling.ca.bc.data.*,oscar.oscarBilling.ca.bc.pageUtil.*,oscar.*,oscar.entities.*"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.BillingreferralDao" %>
<%@ page import="oscar.oscarResearch.oscarDxResearch.util.dxResearchCodingSystem"%>

<%!
  public void fillDxcodeList(BillingFormData.BillingService[] servicelist, Map dxcodeList) {
    for (int i = 0; i < servicelist.length; i++) {
      BillingAssociationPersistence per = new BillingAssociationPersistence();
      ServiceCodeAssociation assoc = per.getServiceCodeAssocByCode(servicelist[i].getServiceCode());
      List dxcodes = assoc.getDxCodes();
      if (!dxcodes.isEmpty()) {
        dxcodeList.put(servicelist[i].getServiceCode(), (String) dxcodes.get(0));
      }
    }
  }
  public String createAssociationJS(Map assocCodeMap,String assocArrayName) {

    Set e = assocCodeMap.keySet();
    StringBuffer out = new StringBuffer();
    out.append("var " + assocArrayName + "  = new Array();");
    out.append("\n");
    int index = 0;
    for (Iterator iter = e.iterator(); iter.hasNext(); ) {
      String key = (String) iter.next();
      String value = (String) assocCodeMap.get(key);
      String rowName = assocArrayName + "row";
      out.append("var " + rowName + index + " = new Array(2);\n");
      out.append(rowName + index + "[0]='" + key + "'; ");
      out.append(rowName + index + "[1]='" + value + "';\n");
      out.append(assocArrayName + "[" + index + "]=" + rowName  + index + ";\n");
      index++;
    }
    return out.toString();
  }
%>
<%
	LoggedInInfo loggedInInfo =	LoggedInInfo.getLoggedInInfoFromSession(request);
  int year = 0; //Integer.parseInt(request.getParameter("year"));
  int month = 0; //Integer.parseInt(request.getParameter("month"));
  //int day = now.get(Calendar.DATE);
  int delta = 0; //request.getParameter("delta")==null?0:Integer.parseInt(request.getParameter("delta")); //add or minus month
  GregorianCalendar now = new GregorianCalendar();
  year = now.get(Calendar.YEAR);
  month = now.get(Calendar.MONTH) + 1;
  String sxml_location = "", sxml_provider = "", sxml_visittype = "";
  String color = "", colorflag = "";
  BillingSessionBean bean = (BillingSessionBean) pageContext.findAttribute("billingSessionBean");
  oscar.oscarDemographic.data.DemographicData demoData = new oscar.oscarDemographic.data.DemographicData();
  org.oscarehr.common.model.Demographic demo = demoData.getDemographic(loggedInInfo, bean.getPatientNo());
  bean.setPatientName(demo.getFullName());
  oscar.oscarBilling.ca.bc.MSP.ServiceCodeValidationLogic lgc = new oscar.oscarBilling.ca.bc.MSP.ServiceCodeValidationLogic();
  BillingFormData billform = new BillingFormData();
  BillingFormData.BillingService[] billlist1 = lgc.filterServiceCodeList(billform.getServiceList("Group1", bean.getBillForm(), bean.getBillRegion(),new Date()), demo);
  BillingFormData.BillingService[] billlist2 = lgc.filterServiceCodeList(billform.getServiceList("Group2", bean.getBillForm(), bean.getBillRegion(),new Date()), demo);
  BillingFormData.BillingService[] billlist3 = lgc.filterServiceCodeList(billform.getServiceList("Group3", bean.getBillForm(), bean.getBillRegion(),new Date()), demo);
  String group1Header = billform.getServiceGroupName("Group1", bean.getBillForm());
  String group2Header = billform.getServiceGroupName("Group2", bean.getBillForm());
  String group3Header = billform.getServiceGroupName("Group3", bean.getBillForm());
  BillingFormData.BillingPhysician[] billphysician = billform.getProviderList();
  BillingFormData.BillingVisit[] billvisit = billform.getVisitType(bean.getBillRegion());
  BillingFormData.Location[] billlocation = billform.getLocationList(bean.getBillRegion());
  BillingFormData.Diagnostic[] diaglist = billform.getDiagnosticList(bean.getBillForm(), bean.getBillRegion());
  BillingFormData.BillingForm[] billformlist = billform.getFormList();
  SupServiceCodeAssocDAO supDao = SpringUtils.getBean(SupServiceCodeAssocDAO.class);
  HashMap assocCodeMap = new HashMap();
  fillDxcodeList(billlist1, assocCodeMap);
  fillDxcodeList(billlist2, assocCodeMap);
  fillDxcodeList(billlist3, assocCodeMap);
  String frmType = request.getParameter("billType");
  if (frmType != null && frmType.equals("Pri")) {
    billform.setPrivateFees(billlist1);
    billform.setPrivateFees(billlist2);
    billform.setPrivateFees(billlist3);
  }
  String loadFromSession = request.getParameter("loadFromSession");
  if (request.getAttribute("loadFromSession") != null) {
    loadFromSession = "y";
  }
  if (loadFromSession == null) {
    request.getSession().removeAttribute("BillingCreateBillingForm");
  }

  BillingreferralDao billingReferralDao = (BillingreferralDao)SpringUtils.getBean("BillingreferralDAO");
  
  String newWCBClaim = (String)request.getAttribute("newWCBClaim");
  
  String mRecRefDoctor = "";
  String mRecRefDoctorNum = "";

  if(! "".equals(demo.getFamilyDoctorNumber())){
   mRecRefDoctor = demo.getFamilyDoctorName();
   mRecRefDoctorNum = demo.getFamilyDoctorNumber();
  }else{
   mRecRefDoctor = "none";
  }

  ArrayList<String> recentList = billform.getRecentReferralDoctorsList(demo.getDemographicNo());
  
  dxResearchCodingSystem codingSys = new dxResearchCodingSystem();
  pageContext.setAttribute("dxCodeSystemList", codingSys);
%>
<!DOCTYPE html>
<html>
<head>
<title>
<bean:message key="billing.bc.title"/>
</title>
<html:base/>
<link rel="stylesheet" type="text/css" media="all" href="${pageContext.servletContext.contextPath}/library/jquery/jquery-ui.theme-1.12.1.min.css" />
<link rel="stylesheet" type="text/css" media="all" href="${pageContext.servletContext.contextPath}/library/jquery/jquery-ui.structure-1.12.1.min.css" />
<link rel="stylesheet" type="text/css" media="all" href="${pageContext.servletContext.contextPath}/share/calendar/calendar.css" title="win2k-cold-1"/>
<link rel="stylesheet" type="text/css" media="all" href="${pageContext.servletContext.contextPath}/library/bootstrap/3.0.0/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" media="all" href="${pageContext.servletContext.contextPath}/css/bootstrap-datetimepicker-standalone.css" />
<link rel="stylesheet" type="text/css" media="all" href="${pageContext.servletContext.contextPath}/css/bootstrap-datetimepicker.min.css" />


<script type="text/javascript" src="${pageContext.servletContext.contextPath}/library/moment.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/library/jquery/jquery-1.12.0.min.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/library/jquery/jquery-ui-1.12.1.min.js"></script> 
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/library/bootstrap-datetimepicker.min.js" ></script>

<script type="text/javascript" src="${pageContext.servletContext.contextPath}/share/calendar/calendar.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/share/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/share/javascript/prototype.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/share/javascript/Oscar.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/share/javascript/boxover.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/dxJSONCodeSearch.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/library/jquery/jquery.validate-1.19.1.min.js"></script>

<style type="text/css">
	table {
	  margin-bottom: 5px !important;
	}
	div#wcbForms p {
		padding:0px;
		margin:0px;
	}
	div#wcbForms table {
		margin:0px 0 5px 0;
	}
	div#wcbForms table th {
		font-weight: normal;
	}
	div#wcbForms pre {
		display:none;
	}
	div#wcbForms table tr td, div#wcbForms table tr th, div#wcbForms table{
		border-color:#ddd;
	}
	
	.button-bar {
		margin-bottom:15px;
	}
	
	table {
		width: 100%;
	}
	
	table.tool-bar tr td {
		vertical-align: bottom;
	}
	
	strong, label {
		font-weight: normal !important;
	}
	
	#billingPatientInfo {
		margin-top: 5px;
	}
	
	.wrapper {
		margin: 40px 10px 10px 10px;
	}
	
	div#page-header {
		position: fixed;
		width: 100%;
		top: 0;
		left: 0;
		z-index: 100000;
	}
	
	div#page-header::after {
    	clear: both;
	}
	
	div#page-header::before, div#page-header::after {
	    display: table;
	    content: " ";
	}
	
	div#page-header table {
		margin:0px !important;
	}
	
	#oscarBillingHeader {
		width: 100%;
		border-collapse: collapse;
		background-color: #F3F3F3;
	}
	
	table#oscarBillingHeader tr td {
		padding: 1px 5px;
		background-color: #F3F3F3;
		vertical-align: middle;
	}
	
	#oscarBillingHeader #oscarBillingHeaderLeftColumn {
		width: 20% !important;
		background-color: black;	
		border-bottom: black 1px solid;
		line-height: 1;
		border-top: black 1px solid;	
	}
	
	#oscarBillingHeader #oscarBillingHeaderLeftColumn h1 {
		margin: 0px;
		padding: 7px !important;
		display: block;
		font-size: large !important;
		background-color: black;
		color: white;
		font-weight: bold;
	}
	
	#oscarBillingHeaderRightColumn {
		vertical-align: top !important;
		text-align: right;
		padding-top: 3px !important;
		padding-right: 3px !important;

	}
	
	#oscarBillingHeader #oscarBillingHeaderRightColumn, #oscarBillingHeader #oscarBillingHeaderCenterColumn {
		border-bottom: 1px solid #ccc;
	}
	
	span.HelpAboutLogout a {
		font-size: x-small;
		color: black;
		float: right;
		padding: 0 3px;
	}
	
	h3 {
		font-size: small;
		width: 100%;
		border-top: red thin solid;
		border-bottom: red thin solid;
		margin: 0px;
		margin-top: 5px;
		padding: 0px;
	}
	h3 ul {
		margin:0px;
	}
	
	table#billingFormTable table.tool-table tr td table {
		background-color: whitesmoke;
		margin: 4px 0;
		border:#ccc thin solid;
	}
	
	table#billingFormTable table.tool-table tr:nth-of-type(2) td table {
		margin: 0 0 4px 0;
	}
	
	table#billingFormTable table.tool-table {
		background-color: whitesmoke;
		margin-top: 5px;
		border:#ccc thin solid;
	}

	table#billingFormTable table.tool-table tr td table tr:first-of-type td {
		padding-top:5px !important;
	}
	table#billingFormTable table.tool-table tr td table tr:last-of-type td {
		padding-bottom:5px !important;
	}
	
	.serviceCodesTable {
		margin-bottom: 5px !important;
	}
	
	.serviceCodesTable tr:nth-child(even) {
		background: #f5f5f5
	}
	
	.serviceCodesTable tr:nth-child(odd) {
		background: #FFF
	}
	
	#billingFormTable table tr td {
		padding: 1px 5px !important;
	}
	
	tr#buttonRow td {
		padding-top: 15px !important;
		padding-bottom: 15px !important;
	}
	
	.ui-autocomplete {
		max-height: 200px;
		overflow-y: auto;
		/* prevent horizontal scrollbar */
		overflow-x: hidden;
		width: 200px;
	}
	/* IE 6 doesn't support max-height
		   * we use height instead, but this forces the menu to always be this tall
		   */
	* html .ui-autocomplete {
		height: 100px;
	}
	
	table.table-borderless tr td, table.table-borderless tr th, table.table-borderless {
	   	border: none;
		border: 0;
	}
	.has-error {
		background-color: #f2dede;
	}

	@media only screen and (max-width: 820px) {
		.wrapper {
			margin: 80px 10px 10px 10px;
		}
	}
	
</style>
<script type="text/javascript" >

jQuery.noConflict();

// set the context path for javascript functions
var ctx = '${pageContext.servletContext.contextPath}';

//creates a javaspt array of associated dx codes
<%=createAssociationJS(assocCodeMap,"jsAssocCodes")%>
<%=createAssociationJS(supDao.getAssociationKeyValues(),"trayAssocCodes")%>



function codeEntered(svcCode){
	myform = document.forms[0];
	return((myform.xml_other1.value == svcCode)||(myform.xml_other2.value == svcCode)||(myform.xml_other3.value == svcCode))
}
function addSvcCode(svcCode) {
    myform = document.forms[0];
    if(myform.service)
    {
	    for (var i = 0; i < myform.service.length; i++) {
	      if (myform.service[i].value == svcCode) {
	        if (myform.service[i].checked) {
	          if (myform.xml_other1.value == "") {
	            myform.xml_other1.value = svcCode;
	            var trayCode =  getAssocCode(svcCode,trayAssocCodes);
	            if(trayCode!=''){
	              myform.xml_other2.value = trayCode;
	            }
	            myform.xml_diagnostic_detail1.value = getAssocCode(svcCode,jsAssocCodes);
	          }
	          else if (myform.xml_other2.value == "") {
	            myform.xml_other2.value = svcCode;
	            var trayCode =  getAssocCode(svcCode,trayAssocCodes);
	            if(trayCode!=''){
	              myform.xml_other3.value = trayCode;
	            }
	            myform.xml_diagnostic_detail2.value = getAssocCode(svcCode,jsAssocCodes);
	          }
	          else if (myform.xml_other3.value == "") {
	            myform.xml_other3.value = svcCode;
	            myform.xml_diagnostic_detail3.value = getAssocCode(svcCode,jsAssocCodes);
	          }
	          else {
	            alert("There are already three service codes entered");
	            myform.service[i].checked = false;
	            return;
	          }
	        }
	        else {
	          if (myform.xml_other1.value == svcCode) {
	            myform.xml_other1.value = "";
	            myform.xml_other2.value = "";
	            myform.xml_diagnostic_detail1.value = "";
	          }
	          else if (myform.xml_other2.value == svcCode) {
	            myform.xml_other2.value = "";
	            myform.xml_diagnostic_detail2.value = "";
	          }
	          else if (myform.xml_other3.value == svcCode) {
	            myform.xml_other3.value = "";
	            myform.xml_diagnostic_detail3.value = "";
	          }
	        }return;
	      }
	    }
    }
  }
function getAssocCode(svcCode,assocCodes){
  var retcode = ""
  for (var i = 0; i < assocCodes.length; i++) {
    var row = assocCodes[i];

    if(row[0] == svcCode){
      return row[1];
    }
  }
  return retcode;
}
function checkSelectedCodes(){
    myform = document.forms[0];
    if(myform.service)
    {
	    for (var i = 0; i < myform.service.length; i++) {
	        if (myform.service[i].checked) {
	            if(!codeEntered(myform.service[i].value)){
	                myform.service[i].checked = false;
	            }
	        }
	    }
	}
}

function HideElementById(ele){
	document.getElementById(ele).style.display='none';
}

function ShowElementById(ele){
	document.getElementById(ele).style.display='';
}
function CheckType(){
	if (document.BillingCreateBillingForm.xml_billtype.value == "ICBC"){
		ShowElementById('ICBC');
		document.BillingCreateBillingForm.mva_claim_code.options[1].selected = true;
	}else{
		HideElementById('ICBC');
		document.BillingCreateBillingForm.mva_claim_code.options[0].selected = true;
	}
	
    toggleWCB();
}

function callReplacementWebService(url,id){
               var ran_number=Math.round(Math.random()*1000000);
               var params = "demographicNo=<%=bean.getPatientNo()%>&wcb=&rand="+ran_number;  //hack to get around ie caching the page
               new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:true}); 
} 

          <%
          String wcb = "";
          Integer wcbid = (Integer) request.getAttribute("WCBFormId");
          if (wcbid != null){
              wcb = "?wcbid="+wcbid;
          }
          %>


function toggleWCB(){
        <%
       if(! "1".equals(newWCBClaim)){
        %>
		       if (document.BillingCreateBillingForm.xml_billtype.value == "WCB"){
		        document.BillingCreateBillingForm.fromBilling.value = "true";
		       }
		       else{
		          document.BillingCreateBillingForm.fromBilling.value = "false";
		       }
        <%}
        %>

       	if (document.BillingCreateBillingForm.xml_billtype.value == "WCB"){
          callReplacementWebService("wcbForms.jsp<%=wcb%>",'wcbForms');
       	} 
       	else
    	{
       		jQuery("#wcbForms").empty();
    	}

}

function replaceWCB(id){
    oscarLog("In replaceWCB");
  var ur = "wcbForms.jsp?wcbid="+id;
  callReplacementWebService(ur,'wcbForms');
  oscarLog("replaceWCB out == "+ur);
}

function gotoPrivate(){
   if (document.BillingCreateBillingForm.xml_billtype.value == "Pri"){
 	  var url = "<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/" %>billing.do?billRegion=<%=bean.getBillRegion()%>&billForm=Pri&hotclick=&appointment_no=<%=bean.getApptNo()%>&demographic_name=<%=URLEncoder.encode(bean.getPatientName(),"UTF-8")%>&demographic_no=<%=bean.getPatientNo()%>&user_no=<%=bean.getCreator()%>&appointment_date=<%=bean.getApptDate()%>&status=<%=bean.getApptStatus()%>&start_time=<%=bean.getApptStart()%>&bNewForm=1&billType=Pri";
	  url += "&providerview=" + jQuery("select[name='xml_provider']").val();
	  url += "&apptProvider_no=" + jQuery("select[name='xml_provider']").val();

 	  window.location.href = url;
   }
   if (document.BillingCreateBillingForm.xml_billtype.value == "MSP"){
	  var url = "<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/" %>billing.do?billRegion=<%=bean.getBillRegion()%>&billForm=<%=OscarProperties.getInstance().getProperty("default_view")%>&hotclick=&appointment_no=<%=bean.getApptNo()%>&demographic_name=<%=URLEncoder.encode(bean.getPatientName(),"UTF-8")%>&demographic_no=<%=bean.getPatientNo()%>&user_no=<%=bean.getCreator()%>&appointment_date=<%=bean.getApptDate()%>&status=<%=bean.getApptStatus()%>&start_time=<%=bean.getApptStart()%>&bNewForm=1&billType=MSP";
	  url += "&providerview=" + jQuery("select[name='xml_provider']").val();
	  url += "&apptProvider_no=" + jQuery("select[name='xml_provider']").val(); 
      window.location.href = url;
   	}
}

function correspondenceNote(){
	if (document.BillingCreateBillingForm.correspondenceCode.value == "0" ){
		HideElementById('CORRESPONDENCENOTE');
	}else if (document.BillingCreateBillingForm.correspondenceCode.value == "C" ){
		HideElementById('CORRESPONDENCENOTE');
	}else if (document.BillingCreateBillingForm.correspondenceCode.value == "N" ){
	  ShowElementById('CORRESPONDENCENOTE');
	}else {(document.BillingCreateBillingForm.correspondenceCode.value == "B" )
     ShowElementById('CORRESPONDENCENOTE');
	}
}

function quickPickDiagnostic(diagnos){

	if (document.BillingCreateBillingForm.xml_diagnostic_detail1.value == ""){
		document.BillingCreateBillingForm.xml_diagnostic_detail1.value = diagnos;
        }else if ( document.BillingCreateBillingForm.xml_diagnostic_detail2.value == ""){
		document.BillingCreateBillingForm.xml_diagnostic_detail2.value= diagnos;
	}else if ( document.BillingCreateBillingForm.xml_diagnostic_detail3.value == "" ){
		document.BillingCreateBillingForm.xml_diagnostic_detail3.value = diagnos;
	}else{
		alert("All of the Diagnostic Coding Boxes are full");
	}
}

function isNumeric(strString){
        var validNums = "0123456789.";
        var strChar;
        var retval = true;

        for (i = 0; i < strString.length && retval == true; i++){
           strChar = strString.charAt(i);
           if (validNums.indexOf(strChar) == -1){
              retval = false;
           }
        }
         return retval;
   }

function checkTextLimit(textField, maximumlength) {
   if (textField.value.length > maximumlength + 1){
      alert("Maximun "+maximumlength+" characters");
   }
   if (textField.value.length > maximumlength){
      textField.value = textField.value.substring(0, maximumlength);
   }
}

function RecordAttachments(Files, File0, File1, File2) {
  window.document.serviceform.elements["File0Data"].value = File0;
  window.document.serviceform.elements["File1Data"].value = File1;
  window.document.serviceform.elements["File2Data"].value = File2;
    window.document.all.Atts.innerText = Files;
  }

var remote=null;

function rs(n,u,w,h,x) {
  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
  remote=window.open(u,n,args);
  if (remote != null) {
    if (remote.opener == null)
      remote.opener = self;
  }
  if (x == 1) { return remote; }
}


var awnd=null;

function OtherScriptAttach() {
  t0 = escape(document.BillingCreateBillingForm.xml_other1.value);
  t1 = escape(document.BillingCreateBillingForm.xml_other2.value);
  t2 = escape(document.BillingCreateBillingForm.xml_other3.value);
  awnd=rs('att','<rewrite:reWrite jspPage="billingCodeNewSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=',820,740,1);
  awnd.focus();
}

function ReferralScriptAttach1(){
    ReferralScriptAttach('xml_refer1');
}

function ReferralScriptAttach2(){
    ReferralScriptAttach('xml_refer2');
}


function ReferralScriptAttach(elementName) {
     var d = elementName;
     t0 = escape(document.BillingCreateBillingForm.elements[d].value);
     t1 = escape("");
     awnd=rs('att','<rewrite:reWrite jspPage="billingReferCodeSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=&search=&formElement=' +d+ '&formName=BillingCreateBillingForm',600,600,1);
     awnd.focus();
}


function ResearchScriptAttach() {
  t0 = escape(document.serviceform.xml_referral1.value);
  t1 = escape(document.serviceform.xml_referral2.value);

  awnd=rs('att','../<rewrite:reWrite jspPage="billingReferralCodeSearch.jsp"/>?name='+t0 + '&name1=' + t1 +  '&search=',600,600,1);
  awnd.focus();
}

function POP(n,h,v) {
  window.open(n,'OSCAR','toolbar=no,location=no,directories=no,status=yes,menubar=no,resizable=yes,copyhistory=no,scrollbars=yes,width='+h+',height='+v+',top=100,left=200');
}


function grabEnter(event,callb){
  if( (window.event && window.event.keyCode == 13) || (event && event.which == 13) )  {
     eval(callb);
     return false;
  }
}

function reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.pgW=innerWidth; document.pgH=innerHeight; onresize=reloadPage; }}
  else if (innerWidth!=document.pgW || innerHeight!=document.pgH) location.reload();
}
reloadPage(true);


function findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}
function getOffsetLeft (el) {
	var ol=el.offsetLeft;
	while ((el=el.offsetParent) != null) { ol += el.offsetLeft; }
	return ol;
	}

function getOffsetTop (el) {
	var ot=el.offsetTop;
	while((el=el.offsetParent) != null) { ot += el.offsetTop; }
	return ot;
	}
var objPopup = null;
var shim = null;
function formPopup(event,objectId){
  objPopTrig = document.getElementById(event);
  objPopup = document.getElementById(objectId);
  shim = document.getElementById('DivShim');
 xPos = getOffsetLeft(objPopTrig);
  yPos = getOffsetTop(objPopTrig) + objPopTrig.offsetHeight;

  objPopup.style.zIndex = 9999;

  shim.style.width = objPopup.offsetWidth + 2;
  shim.style.height = objPopup.offsetHeight;
  shim.style.top = objPopup.style.top;
  shim.style.left = objPopup.style.left;
  shim.style.zIndex = objPopup.style.zIndex - 1;
  shim.style.display = "block";
  objPopup.style.display = "block";
  shim.style.visibility = 'visible';
  objPopup.style.visibility = 'visible';
}

function formPopupHide(){
  objPopup.style.visibility = 'hidden';
  shim.style.visibility = 'hidden';
  objPopup = null;
  shim = null;
}

function addCodeToList(svcCode){
    if (myform.xml_other1.value == "") {
        myform.xml_other1.value = svcCode;
        return true;
    }else if (myform.xml_other2.value == "") {
        myform.xml_other2.value = svcCode;
        return true;
    }else if (myform.xml_other3.value == "") {
        myform.xml_other3.value  = svcCode;
        return true;
    }
}

function setCodeToChecked(svcCode){
    myform = document.forms[0];
    var codeset = false;
    for (var i = 0; i < myform.service.length; i++) {
        if (myform.service[i].value == svcCode) {
            var wasAbleToAddCode = addCodeToList(svcCode);
            if(wasAbleToAddCode){
               myform.service[i].checked = true;
               codeset = true;
            }
            return;
        }
    }
    
    if(codeEntered(svcCode) == false){
        if (myform.xml_other1.value == "") {
            myform.xml_other1.value = svcCode;
            return;
            //myform.xml_diagnostic_detail1.value = "";
        }else if (myform.xml_other2.value == "") {
            myform.xml_other2.value = svcCode;
            //myform.xml_diagnostic_detail2.value = "";
        }else if (myform.xml_other3.value == "") {
            myform.xml_other3.value  = svcCode;
            //myform.xml_diagnostic_detail3.value = "";
        }
    }
    
    
    
}

/*
 * Sets the currently selected provider as the default on 
 * this client computer.
 */
function setDefaultProvider() {
	var selectedProvider = jQuery('select[name=xml_provider] option:selected');
	var providerId = selectedProvider.val();
	var providerName = selectedProvider.text();		
	localStorage.setItem( 'default_billing_provider', providerId );		
	alert(providerName + " is now set as the default billing physician on this computer.");

}

/*
 * Loads the default provider - only if - the provider is not 
 * already set from the billing or encounter interface.
 */
function loadDefaultProvder() {
	var currentProvider = document.BillingCreateBillingForm.xml_provider.value;

	if(! currentProvider)
	{
		var providerId = localStorage.getItem('default_billing_provider');
		jQuery("select[name=xml_provider]").val(providerId);
	}
}

function setReferralDoctor() {
	jQuery(".referral-doctor").on('click', function() {

		 mRecordRefDocNum = jQuery(this).attr('data-num');  
		 mRecordRefDoc= jQuery(this).attr('data-doc');  
		 
		 one = jQuery('[name="xml_refer1"]');
		 two = jQuery('[name="xml_refer2"]');
		 
		 if(one.val().length>0){
		  two.val(mRecordRefDocNum);
		  two.attr("title", mRecordRefDoc );
		 }else{
		  one.val(mRecordRefDocNum);
		  one.attr("title", mRecordRefDoc );
		 }
	});
}

jQuery(document).ready(function(jQuery){
	setReferralDoctor();
	loadDefaultProvder();
	
	jQuery("#bcBillingForm").attr('autocomplete', 'off');
	
	/* for setting times */
    jQuery(function () {
        jQuery('.datetimepicker').datetimepicker({
            format: 'HH:mm'
        });
    });
    
	/* New billing form selection method*/
    jQuery("#selectBillingForm").on('change',function() {
    	var url = ctx + '/billing.do?demographic_no=' + <%=bean.getPatientNo()%> + '&appointment_no=' + <%=bean.getApptNo()%> + '&billRegion=BC&billForm=' + this.value;
      	jQuery("#billingFormTableWrapper").load(url + " #billingFormTable", function(){
      		// re-bind all the javascript
    		getDxInformation();
    		bindDxJSONEvents();
    		setReferralDoctor();
      	});
    });

	jQuery("#serviceStartTime").on('blur', function() {
	    var time = this.value;
	    if(time) {
	        var hour = time.split(":")[0];
	        var minute = time.split(":")[1];
	        var endtime = jQuery("#serviceEndTime").val();
	       if(endtime) {
	        	timeCompare( hour+minute, endtime.replace(":", "") );
	       }
			jQuery("#xml_starttime_hr").val(hour);
			jQuery("#xml_starttime_min").val(minute);
	    }
	 })
	 
	 
	 jQuery("#serviceEndTime").on('blur', function() {
	    var time = this.value;
	    
	    if(time) {    	
	        var hour = time.split(":")[0];
	        var minute = time.split(":")[1];
	        var starttime = jQuery("#serviceStartTime").val();
	        timeCompare( starttime.replace(":", ""), hour+minute );
			jQuery("#xml_endtime_hr").val(hour);
			jQuery("#xml_endtime_min").val(minute);
	    }
	 })
	 
	 function timeCompare( start, end ) {
	    if( !start || start > end ) {
	    	alert("Warning: the start time is greater than the end time.");
	    }	
	 }
	
	/*
	 * All form validation code following
	 *
	 */
	 jQuery("#bcBillingForm").validate({
 
		 rules: {			 
			 /*
			  * Is provider selected
			  */
			 xml_provider: {
				 required: true
			 },
			 /*
			  * Referral provider codes must be numeric
			  */
			 xml_refer1: {
				 alphanumeric: true
			 },

			 xml_refer2: {
                 alphanumeric: true
			 },
			 
			 /*
			  * Validate all 3 service codes and 
			  * unit values
			  */
			 xml_other1: {
				 required: true
			 },
			 xml_other1_unit: {
			      number: true
			 },
			 xml_other2_unit: {
			      number: true
			 },
			 xml_other3_unit: {
			      number: true
			 },
			 WCBid: {
				 required: function(element){
					 return document.BillingCreateBillingForm.xml_billtype.value == "WCB";
				 }
			 },
			 /*
			  * Validate all 3 Diagnostic codes.
			  */
		 	 xml_diagnostic_detail1: {
		 		 required: function(element) {
		 			return document.BillingCreateBillingForm.xml_billtype.value != "Pri";
		 		 },
		 		 remote: {
		 			url: ctx + "\/dxCodeSearchJSON.do",
		 			type: "post",
		 			dataType: "json",
		 			data: {
						keyword: function(element){
							return jQuery( "input[name='xml_diagnostic_detail1']" ).val();
						},
						method: "validateCode",
						codeSystem: function() {
							return (jQuery( '#codingSystem option:selected, input#codingSystem' ).val()).toLowerCase();
						}	
		 			},
		 			dataFilter: function (response) {
		 				var data = JSON.parse(response);
						return data.dxvalid;
					}
		 		 }
		 	 },
		 	 xml_diagnostic_detail2: {
		 		 required: false,
		 		 remote: {
		 			url: ctx + "\/dxCodeSearchJSON.do",
		 			type: "post",
		 			dataType: "json",
		 			data: {
						keyword: function(element){
							return jQuery( "input[name='xml_diagnostic_detail2']" ).val();
						},
						method: "validateCode",
						codeSystem: function() {
							return (jQuery( '#codingSystem option:selected, input#codingSystem' ).val()).toLowerCase();
						}	
		 			},
		 			dataFilter: function (response) {
		 				var data = JSON.parse(response);
						return data.dxvalid;
					}
		 		 }
		 	 },
		 	 xml_diagnostic_detail3: {
		 		 required: false,
		 		 remote: {
		 			url: ctx + "\/dxCodeSearchJSON.do",
		 			type: "post",
		 			dataType: "json",
		 			data: {
						keyword: function(element){
							return jQuery( "input[name='xml_diagnostic_detail3']" ).val();
						},
						method: "validateCode",
						codeSystem: function() {
							return (jQuery( '#codingSystem option:selected, input#codingSystem' ).val()).toLowerCase();
						}	
		 			},
		 			dataFilter: function (response) {
		 				var data = JSON.parse(response);
						return data.dxvalid;
					}
		 		 }
		 	 }
	 	 },
	 	 
		 /*
		  * Error messages to return on each validation
		  */
		 messages: {
		 	 xml_diagnostic_detail1: {
		 		 required: "At least 1 diagnostic code is required",
		 		 remote: "Invalid diagnostic code 1"
		 	 },
		 	 xml_diagnostic_detail2: {
		 		 remote: "Invalid diagnostic code 2"
		 	 },
		 	 xml_diagnostic_detail3: {
		 		 remote: "Invalid diagnostic code 3"
		 	 },
			 xml_other1_unit: "Service code units must be numeric",
			 xml_other2_unit: "Service code units must be numeric",
			 xml_other3_unit: "Service code units must be numeric",
			 xml_provider: "Select a billing physician",
			 xml_other1: "At least 1 service code is required",
			 xml_refer1: "1: Invalid Referral Doctor code",
			 xml_refer2: "2: Invalid Referral Doctor code",
			 WCBid: "A WCB Form must be selected."
		 },
		 
		 /*
		  * Error highlight and message methods
		  */
         highlight: function (element) { 
             jQuery(element).addClass('has-error');             
         },
         unhighlight: function (element) {
        	 jQuery(element).removeClass('has-error'); 
         },
		 submitHandler: function(form) {
			 toggleWCB();
		     form.submit();
		 },
		 onkeyup: false,
		 onfocusout: false,
         focusInvalid: true,
         errorElement: 'div', 
         errorClass: 'alert alert-danger', 
         errorLabelContainer: '#bcBillingError',

	 })
	/*
	 * End form validation code
	 *
	 */

	 
	 /*
	 *  clears out the dx code list everytime 
	 *  the code system is changed.
	 */
	 jQuery("#codingSystem").change(function(){
		 jQuery("#jsonDxSearchInput-1").val(""); 
		 jQuery("#jsonDxSearchInput-2").val(""); 
		 jQuery("#jsonDxSearchInput-3").val(""); 
	 })
	 
}); //<!-- End Document Ready //-->
</script>
</head>
<%!
  /**
   Generates a string list of option tags in numeric order
   **/
  String generateNumericOptionList(int range,String selected) {
    selected = selected == null?"":selected;
    StringBuffer buff = new StringBuffer();
    buff.append("<option value=''></option>");
    for (int i = 0; i < range; i++) {
      String prefix = i < 10 ? "0" : "";
      String val = prefix + String.valueOf(i);
      String sel = "";
      if(val.equals(selected)){
        sel = "selected";
      }
      buff.append("<option value='" + val + "' " + sel + ">" + val + "</option>");
    }
    return buff.toString();
  }
%>
<body style="background-color:#FFFFFF;" onLoad="CheckType();correspondenceNote();">
	<div id="page-header" >	
		<table id="oscarBillingHeader" class="table-borderless">
			<tr>
				<td id="oscarBillingHeaderLeftColumn"><h1><bean:message key="billing.bc.title"/></h1></td>

				<td id="oscarBillingHeaderCenterColumn">				
					<span class="badge badge-primary"><bean:message key="billing.patient"/></span>
	                <label class="label-text" ><%=demo.getLastName()%>, <%=demo.getFirstName()%></label>
	            	
	            	<span class="badge badge-primary"><bean:message key="billing.patient.age"/></span>  
	            	<label class="label-text"><%=demo.getAge()%></label>

<%-- 	Keep until confirmed not needed.			

					<span class="badge badge-primary"><bean:message key="billing.patient.status"/></span> 
					<strong class="label-text"><%=demo.getPatientStatus()%></label>
	
              <span class="badge badge-primary"><bean:message key="billing.patient.roster"/></span> 
	                <label><%=demo.getRosterStatus()%></label> 
--%>	         
	                <span class="badge badge-primary" title="Most Responsible Provider"><bean:message key="billing.provider.assignedProvider"/></span>
	                <label class="label-text">
		                <c:choose>
		                	<c:when test="<%= demo.getProviderNo() != null && ! demo.getProviderNo().trim().isEmpty() %>">
		                		<%=billform.getProviderName(demo.getProviderNo())%> 
		                	</c:when>
		                	<c:otherwise>
		                		Unknown
		                	</c:otherwise>
		                </c:choose>
	                </label>

					<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="x" >
		                <button type="button" class="btn btn-link" title="View this patient's Electronic Chart" onclick="popup(710, 1024,'${pageContext.servletContext.contextPath}/casemgmt/forward.jsp?action=view&demographicNo=<%=demo.getDemographicNo()%>&providerNo=<%=loggedInInfo.getLoggedInProviderNo()%>&providerName=<%=loggedInInfo.getLoggedInProvider().getFullName()%>&appointmentNo=&reason=null&appointmentDate=&start_time=&apptProvider=null&providerview=null&msgType=null&OscarMsgTypeLink=null&noteId=null','encounter',12556);return false;">
							<bean:message key="billing.patient.encounter" />
						</button>	
					</security:oscarSec>
					
	                <button type="button" class="btn btn-link" title="View previous invoices for this patient" onclick="popup(800, 1000, 'billStatus.jsp?lastName=<%=demo.getLastName()%>&firstName=<%=demo.getFirstName()%>&filterPatient=true&demographicNo=<%=demo.getDemographicNo()%>','InvoiceList');return false;">
						<bean:message key="demographic.demographiceditdemographic.msgInvoiceList"/>
					</button>
					
					<button type="button" class="btn btn-link" title="Set a default Billing Physician to use when a physician is not pre-selected" onclick="setDefaultProvider()">
						<bean:message key="billing.provider.defaultProvider"/>
					</button>				
				</td>
				<td id="oscarBillingHeaderRightColumn" align=right>
					<span class="HelpAboutLogout"> 
						<a style="font-size: 10px; font-style: normal;" href="${ ctx }oscarEncounter/About.jsp" target="_new">About</a>
						<a style="font-size: 10px; font-style: normal;" target="_blank"
									href="http://www.oscarmanual.org/search?SearchableText=&Title=Chart+Interface&portal_type%3Alist=Document">Help</a>		
					</span>
				</td>
			</tr>
		</table>
	</div>
<div class="wrapper">



<div class="container-fluid">
<html:errors/>

<!-- end error row -->

<%
List<String> wcbneeds = (List) request.getAttribute("WCBFormNeeds");
if(wcbneeds != null){%>
<div>
    WCB Form needs:
    <ul>
    <%for (String s: wcbneeds) { %>
    <li><bean:message key="<%=s%>"/></li>
    <%}%>
    </ul>
</div>
<%}%>

<html:form styleId="bcBillingForm" styleClass="form-inline" action="/billing/CA/BC/CreateBilling" onsubmit="toggleWCB();">
	<input autocomplete="false" name="hidden" type="text" style="display:none;">
  	<input type="hidden" name="fromBilling" value=""/>

<%
  BillingCreateBillingForm thisForm = (BillingCreateBillingForm) request.getSession().getAttribute("BillingCreateBillingForm");
  if (thisForm != null) {
    sxml_provider = ((String) thisForm.getXml_provider());
    sxml_location = ((String) thisForm.getXml_location());
    sxml_visittype = ((String) thisForm.getXml_visittype());
    
    if(sxml_provider == null || "".equals(sxml_provider) || "none".equals(sxml_provider))
    {
        sxml_provider = bean.getApptProviderNo();
    }
    thisForm.setXml_provider(sxml_provider);
    
    if (sxml_location.compareTo("") == 0) {
      sxml_location = OscarProperties.getInstance().getProperty("visitlocation");
      sxml_visittype = OscarProperties.getInstance().getProperty("visittype");
      thisForm.setXml_location(sxml_location);
      thisForm.setXml_visittype(sxml_visittype);
      if ( OscarProperties.getInstance().getProperty("BC_DEFAULT_ALT_BILLING") != null && OscarProperties.getInstance().getProperty("BC_DEFAULT_ALT_BILLING").equalsIgnoreCase("YES")){
         thisForm.setXml_encounter("8");
      }
    }
    String apDate = thisForm.getXml_appointment_date();
    if (apDate != null && apDate.trim().length() == 0) {
      thisForm.setXml_appointment_date(bean.getApptDate());
    }
    if (bean != null && bean.getBillType() != null) {
      thisForm.setXml_billtype(bean.getBillType());
    }
    else if (request.getParameter("billType") != null) {
      thisForm.setXml_billtype(request.getParameter("billType"));
    }
    if (demo != null && demo.getVer() != null && demo.getVer().equals("66")) {
      thisForm.setDependent("66");
    }
    thisForm.setCorrespondenceCode(bean.getCorrespondenceCode());
    oscar.oscarBilling.ca.bc.data.BillingPreferencesDAO dao = SpringUtils.getBean(oscar.oscarBilling.ca.bc.data.BillingPreferencesDAO.class);
    oscar.oscarBilling.ca.bc.data.BillingPreference pref = null;
    //checking for a bug where the passed in provider number is actually "none" rather than numeral 0
    if (oscar.util.StringUtils.isNumeric(thisForm.getXml_provider())) {
      pref = dao.getUserBillingPreference((String) thisForm.getXml_provider());
    }
    String userReferralPref = "";
    if (pref != null) {
      if (pref.getReferral() == 1) {
        userReferralPref = "T";
      }
      else if (pref.getReferral() == 2) {
        userReferralPref = "B";
      }
      thisForm.setRefertype1(userReferralPref);
      thisForm.setRefertype2(userReferralPref);
    }
  }
%>

  <table >
    <tr>
      <td>
        <table class="tool-bar" id="billingPatientInfo">
          <tr>
          	<td>

	          	<div class="form-group" > 
			      
			        <label><bean:message key="billing.billingform"/></label>
			        
          		    <select class="form-control" id="selectBillingForm">
          		      <% for (int i = 0; i < billformlist.length; i++) { %>
          		       <option <% if( bean.getBillForm().equalsIgnoreCase( billformlist[i].getFormCode() ) ) {%> 
          		       				selected 
          		       			<% } %> 
          		      		value="<%=billformlist[i].getFormCode()%>" >
          		      		<%= billformlist[i].getDescription() %>
          		      	</option>          		      
          		      <%} %>
          		    </select>
          		    
          		</div>
          	
          	</td>
          
             <td>
              <div class="form-group" > 

		        <label><bean:message key="billing.provider.billProvider"/></label>
                <html:select styleClass="form-control" property="xml_provider" value="<%=sxml_provider%>">
                  <html:option value="">
                    Select Provider
                  </html:option>
                <%for (int j = 0; j < billphysician.length; j++) {                %>
                  <html:option value="<%=billphysician[j].getProviderNo()%>"><%=billphysician[j].getProviderName()%>                  </html:option>
                <%}                %>
                </html:select>
                
           
               </div>
            </td>

            <td>
                         <div class="form-group" > 
		     
           		 <label><bean:message key="billing.billingtype"/></label>
                <html:select styleClass="form-control" property="xml_billtype" onchange="CheckType();gotoPrivate();">
                  <html:option value="MSP">Bill MSP</html:option>
                  <html:option value="WCB">Bill WCB</html:option>
                  <html:option value="ICBC">Bill ICBC</html:option>
                  <html:option value="Pri">Private</html:option>
                  <html:option value="DONOTBILL">Do Not Bill</html:option>
                </html:select>
                
                </div>
                
            </td>
            <td>
               <div class="form-group" > 
		      
                <label>Clarification Code</label>
                <html:select styleClass="form-control" property="xml_location">
                <%
                  for (int i = 0; i < billlocation.length; i++) {
                    String locationDescription = billlocation[i].getBillingLocation() + "|" + billlocation[i].getDescription();
                %>
                  <html:option value="<%=locationDescription%>"><%=billlocation[i].getDescription()%>                  </html:option>
                <%}                %>
                </html:select> 
                </div>
                          
            </td>

            <td>
             <div class="form-group" > 
		      		      
		      <label>Service Location</label>
                <html:select styleClass="form-control" property="xml_visittype">
                <%
                  for (int i = 0; i < billvisit.length; i++) {
                    String visitTypeDescription = billvisit[i].getVisitType() + "|" + billvisit[i].getDescription();
                %>
                  <html:option value="<%=visitTypeDescription%>"><%=visitTypeDescription%>                  </html:option>
                <%}                %>
                </html:select>
                
                </div>
               
            </td>
          </tr>
        </table>
</td>
</tr>
<tr>
<td>
        <table class="tool-bar" >
          <tr>
            <td>
			  <div class="form-group" > 
		     
	              <a href="javascript:void(0)" id="hlSDate">
	                  <label><bean:message key="billing.servicedate"/></label>
	              </a>
	              <html:text style="min-width:100px;" styleClass="form-control" property="xml_appointment_date" size="10" readonly="true" styleId="xml_appointment_date"/>
              </div>

            </td>
            <td>
			<div class="form-group" > 
		     
              <a href="javascript:void(0)" id="serviceToDate">
                  <label>To Date</label>
              </a>
             
              <html:text styleClass="form-control" property="service_to_date" size="2" maxlength="2" styleId="service_to_date"/>
              </div>
           
            </td>
            <td>              
            <div class="form-group" > 
		      
           <label>After Hours</label>
              <html:select styleClass="form-control" property="afterHours">
                <html:option value="0">No</html:option>
                <html:option value="E">Evening</html:option>
                <html:option value="N">Night</html:option>
                <html:option value="W">Weekend</html:option>
              </html:select>
              </div>
             
            </td>
            <td title="(HHMM 24hr):">
            <div class="form-group">
		         
                <label>Time Call</label>
              <html:text styleClass="form-control" property="timeCall" />
              
              </div>
            </td>
            
			<td>
		
		            <div class="form-group">
		             <label>Start</label>
		                <div class='input-group date datetimepicker'> 
		 
		                    <input type='text' id="serviceStartTime" class="form-control" />
		                  	<input type=hidden id="xml_starttime_hr" name="xml_starttime_hr" />
		                    <input type=hidden id="xml_starttime_min" name="xml_starttime_min" />
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-time"></span>
		                    </span>
						</div>
					</div>
			<td>			

		            <div class="form-group">
		            <label>End</label>
		                <div class='input-group date datetimepicker'>
		                    <input type='text' id="serviceEndTime" class="form-control" />
		                    <input type=hidden id="xml_endtime_hr" name="xml_endtime_hr" />
		                    <input type=hidden id="xml_endtime_min" name="xml_endtime_min" />
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-time"></span>
		                    </span>
		                </div>
		            </div>

			</td>
                        
            <td>
               <div class="form-group" > 
		                 
                <label>Dependent</label>
              <html:select styleClass="form-control"  property="dependent">
                <html:option value="00">No</html:option>
                <html:option value="66">Yes</html:option>
              </html:select>
              </div>
            
            </td>
            <td title="Submission Code">              
             <div class="form-group" > 
		     
            	<label>Sub Code</label>
              <html:select styleClass="form-control" property="submissionCode">
                <html:option value="0">O - Normal</html:option>
                <html:option value="D">D - Duplicate</html:option>
                <html:option value="E">E - Debit</html:option>
                <html:option value="C">C - Subscriber Coverage</html:option>
                <html:option value="R">R - Resubmitted</html:option>
                <html:option value="I">I - ICBC Claim > 90 Days</html:option>
                <html:option value="A">A - Requested Preapproval</html:option>
                <html:option value="W">W - WCB Rejected Claim</html:option>
                <html:option value="X">X - Resubmitting Refused / Partially Paid Claim</html:option>
              </html:select>
              </div>
              
            </td>
            <td>
            <div class="form-group" > 
		    
                <label>Payment Method</label>
            <%
              ArrayList types = billform.getPaymentTypes();
              if ("Pri".equalsIgnoreCase(thisForm.getXml_billtype())) {
                for (int i = 0; i < types.size(); i++) {
                  PaymentType item = (PaymentType) types.get(i);
                  if (item.getId().equals("6") || item.getId().equals("8")) {
                    types.remove(i);
                    break;
                  }
                }
              }
              else {
                for (int i = 0; i < types.size(); i++) {
                  PaymentType item = (PaymentType) types.get(i);
                  if (!item.getId().equals("6") && !item.getId().equals("8")) {
                    types.remove(i);
                    i = i - 1;
                  }
                }
              }
              request.setAttribute("paymentMethodList", types);
              request.setAttribute("defaultPaymentMethod" , OscarProperties.getInstance().getProperty("DEFAULT_PAYMENT_METHOD",""));
            %>
                <select class="form-control" id="xml_encounter" name="xml_encounter">
                    <c:forEach items="${paymentMethodList}" var="paymentMethod" >
                        <option value="${paymentMethod.id}" ${ defaultPaymentMethod eq paymentMethod.id ? 'selected' : ''}>${paymentMethod.paymentType}</option>
                    </c:forEach>
                </select>
              </div>
            
            </td>
            <td>
	            
	            <div class="form-group">
			       <label>BCP Facility</label> 
	              	<html:text styleClass="form-control"  property="facilityNum" size="5" maxlength="5"/>
	             	
	            </div> 
	        </td>
	        
	        <!-- sub facility not currently used. But it does work. Unhide to use --> 
	       <td style="display: none;">
	            
	            <div class="form-group">
			        <label>Sub Facility</label>
	              		<html:text styleClass="form-control"  property="facilitySubNum" size="5" maxlength="5"/> 
	             	
	            </div> 
	        </td>
     </tr>
   </table>
</td>
</tr>
<tr>
<td>
        <div style="display: none;">
          <table class="tool-bar" >
            <tr>
              <td>
              <bean:message key="billing.admissiondate"/>
				<div class="form-group" > 
		      	<div class='input-group text'>

                <html:text property="xml_vdate" readonly="true" value="" size="10" styleId="xml_vdate"/>
                <a id="hlADate">
                  <img title="Calendar" src="${pageContext.servletContext.contextPath}/images/cal.gif" alt="Calendar" border="0"/>
                </a>
                </div>
                </div>
              </td>
            </tr>
          </table>
          <script type="text/javascript" >
			   Calendar.setup({inputField:"xml_appointment_date",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlSDate",singleClick:true,step:1});
			   //Calendar.setup({inputField:"xml_appointment_date", ifFormat:""%d/%m/%Y",",button:"hlSDate", align:"Bl", singleClick:true});
			   Calendar.setup({inputField:"xml_vdate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlADate",singleClick:true,step:1});
			   Calendar.setup({inputField:"service_to_date", ifFormat:"%d",button:"serviceToDate", align:"Bl", singleClick:true});
			</script>
        </div>


        <div id="ICBC">
          <table class="tool-bar" >
            <tr>
              <td>
    
		      <div class='form-group'>
                 <label>ICBC Claim No</label>
					<html:text styleClass="form-control" property="icbc_claim_no" maxlength="8"/>
				</div>
				<div class='form-group'> 
					<label>MVA?</label>
	                <html:select styleClass="form-control" property="mva_claim_code">
	                  <html:option value="N">No</html:option>
	                  <html:option value="Y">Yes</html:option>
	                </html:select>
					
                </div>
            
                </td>
 
            </tr>
          </table>
        </div>

</td>
</tr>
<tr>
<td>
<div id="billingFormTableWrapper">
        <table id="billingFormTable">
          <tr>
            <td valign="top" style="width:32%; padding-right:5px;" >
              <table class="table table-condensed table-bordered serviceCodesTable" >
                <tr style="background-color:#CCCCFF;">
                  <td width="25%">
                    <div align="left">
                        <label>
                          <%=group1Header%>
                        </label>
                    </div>
                  </td>
                  <td width="61%" style="background-color:#CCCCFF;">
                    <label>
                        <bean:message key="billing.service.desc"/>
                    </label>
                  </td>
                  <td width="14%">
                    <div align="right">
                      <label>&dollar;<bean:message key="billing.service.fee"/></label>
                    </div>
                  </td>
                </tr>
              <%for (int i = 0; i < billlist1.length; i++) {              %>
                <tr >
                <%String svcCall = "addSvcCode('" + billlist1[i].getServiceCode() + "')";                %>
                  <td width="25%" valign="middle">
                    <label class="checkbox">
                      <html:multibox property="service" value="<%=billlist1[i].getServiceCode()%>" onclick="<%=svcCall%>"/>
                      <%=billlist1[i].getServiceCode()%>
                    </label>
                  </td>
                  <td width="61%">
                    <%=billlist1[i].getDescription()%> 
                  </td>
                  <td width="14%">
                    <div align="right">
                      <%=billlist1[i].getPrice()%>                 
                    </div>
                  </td>
                </tr>
              <%}              %>
              </table>
              <table style="background-color:#CC0000;" class="tool-table">
                <tr>
                  <td>
                    <table>
                      <tr>
                        <td>
                          <label>
                              <bean:message key="billing.referral.doctor"/>
                          </label>
                        </td>
                        <td>
                          <label>
                              <bean:message key="billing.referral.type"/>
                          </label>
                        </td>
                      </tr>
                      <tr>
                        <td>
                        <div class="input-group">
                            <html:text styleClass="form-control" property="xml_refer1" onkeypress="return grabEnter(event,'ReferralScriptAttach1()')"/>
	                     	<span class="input-group-btn">
		                     	<button type="button" class="btn btn-primary" onclick="ReferralScriptAttach('xml_refer1')">
	                            	<span class="glyphicon glyphicon-search"></span>
	                          	</button>
                          	</span>
	                    </div>
                        </td>
                        <td>
                            <html:select styleClass="form-control" property="refertype1">
                              <html:option value="">Select Type</html:option>
                              <html:option value="T">Refer To</html:option>
                              <html:option value="B">Refer By</html:option>
                            </html:select>
                        </td>
                      </tr>
         
                      <tr>
                        <td>
                         	<div class="input-group">
	                            <html:text styleClass="form-control" property="xml_refer2" onkeypress="return grabEnter(event,'ReferralScriptAttach2()')"/>
	                            <span class="input-group-btn">
			                     	<button type="button" class="btn btn-primary" onclick="ReferralScriptAttach('xml_refer2')">
		                            	<span class="glyphicon glyphicon-search"></span>
		                          	</button>
	                          	</span>
                          	</div>
                        </td>
                        <td>
                            <html:select styleClass="form-control" property="refertype2">
                              <html:option value="">Select Type</html:option>
                              <html:option value="T">Refer To</html:option>
                              <html:option value="B">Refer By</html:option>
                            </html:select>
                        </td>
                      </tr>

                    </table>
                  </td>
  
                </tr>
                <tr>
                <td valign="top" >

                <table style="background-color:#fff;" align="left">
                <tr><td width="50%" valign="top">
                
                <table class="table table-condensed table-borderless" style="background-color:#fff;">
                <tr><td colspan="2">Recent Referral Doctors</td></tr>
                  <%
                  String bgColor="#fff";
                  String rProvider = "";

				  if(recentList.size()>0){
		                  for (String r : recentList){ 
		                  rProvider = billingReferralDao.getReferralDocName(r);
		                  %>
		                	  <tr bgcolor="<%=bgColor%>">
		                	  <td width="20%">
		                	  	<a href="javascript:void(0)" class="referral-doctor" data-num="<%=r%>" data-doc="<%=rProvider%>"><%=r%></a>
		                	  </td>
		                	  <td><%=rProvider%></td>
		                	  </tr> 
		                  <%
		                  if(bgColor=="#fff"){bgColor="#ccc";}else{bgColor="#fff";}
		                  
		                  }
				  }else{
				  %>
		                	  <tr><td width="20%"></td><td>none</td></tr> 
				  <%
				  }
                  %>
                 </table> 
                 
                 </td>
                 <td width="50%" valign="top">
                 
                <table class="table table-condensed table-borderless" style="background-color:#fff;">
                <tr><td style="border-top:none;" colspan="2">Referral Doctor on Master Record</td></tr>
                <tr>
                	<td width="20%">
                		<a href="javascript:void(0)" title="Populate referral doctor from master record" class="referral-doctor" data-num="<%=mRecRefDoctorNum%>" data-doc="<%=mRecRefDoctor%>"><%=mRecRefDoctorNum%></a>
                	</td>
                	<td><%=mRecRefDoctor%></td>
                </tr> 
                </table>
                
                </td></tr>
                 </table>
                 
                </td>
                </tr>
              </table>
              
            </td>
            <td valign="top" style="width:32%; padding-right:5px;">
              <table class="table table-condensed table-bordered serviceCodesTable">
                <tr style="background-color:#CCCCFF;">
                  <td width="21%">
                        <label>
                          <%=group2Header%>
                        </label>
                  </td>
                  <td width="60%" style="background-color:#CCCCFF;">
                    <label><bean:message key="billing.service.desc"/></label>
                  </td>
                  <td width="19%" align="right" >
                      <label>&dollar;<bean:message key="billing.service.fee"/></label>
                  </td>
                </tr>
              <%for (int i = 0; i < billlist2.length; i++) {              %>
                <tr >
                <%String svcCall = "addSvcCode('" + billlist2[i].getServiceCode() + "')";                %>
                  <td width="25%">
                  <label class="checkbox">
                      <html:multibox property="service" value="<%=billlist2[i].getServiceCode()%>" onclick="<%=svcCall%>"/>
                      <%=billlist2[i].getServiceCode()%>
                  </label>
                  </td>
                  <td width="61%">
                   <%=billlist2[i].getDescription()%>
                  </td>
                  <td width="14%">
                    <div align="right">
                     <%=billlist2[i].getPrice()%> 
                    </div>
                  </td>
                </tr>
              <%}              %>
              </table>
              <table style="background-color:#999900;" class="tool-table table table-condensed table-borderless">
                <tr>
                  <td valign="top">
                    <table>
                      <tr>
                        <td width="70%">
                          <label><bean:message key="billing.service.otherservice"/></label>
                        </td>
                        <td width="30%">
                          <label><bean:message key="billing.service.unit"/></label>
                        </td>
                      </tr>
                      <tr>
                        <td>
                        <div class="input-group">
 							<span class="input-group-addon">
								1
							</span>
                            <html:text styleClass="form-control" property="xml_other1" onblur="checkSelectedCodes()" onkeypress="return grabEnter(event,'OtherScriptAttach()')"/>
                           	<span class="input-group-btn">
		                     	<button type="button" class="btn btn-primary" title="Search code" onclick="OtherScriptAttach('xml_other1')">
	                            	<span class="glyphicon glyphicon-search"></span>
	                          	</button>
                          	</span>
                        </div>
                        </td>
                        <td>
                        <div class="input-group">
                            <html:text styleClass="form-control" property="xml_other1_unit" size="6" maxlength="6" styleId="xml_other1_unit"/>
                             <span class="input-group-btn">
                            	<button type="button" class="btn btn-primary" value=".5" onClick="$('xml_other1_unit').value = '0.5'">.5</button>
                            </span>
                        </div>
                        </td>
                      </tr>
                      <tr>
                        <td>
                        <div class="input-group">
 							<span class="input-group-addon">
								2
							</span>
                            <html:text styleClass="form-control" property="xml_other2" onblur="checkSelectedCodes()" onkeypress="return grabEnter(event,'OtherScriptAttach()')"/>
                            <span class="input-group-btn">
		                     	<button type="button" class="btn btn-primary" title="Search code" onclick="OtherScriptAttach('xml_other2')">
	                            	<span class="glyphicon glyphicon-search"></span>
	                          	</button>
                          	</span>
             			</div>
                        </td>
                        <td>
                        <div class="input-group">
                            <html:text styleClass="form-control" property="xml_other2_unit" size="6" maxlength="6" styleId="xml_other2_unit"/>
                            <span class="input-group-btn"> 
                             	<button type="button" class="btn btn-primary" value=".5" onClick="$('xml_other2_unit').value = '0.5'" >.5</button>
                             </span>
                         </div>
                        </td>
                      </tr>
                      <tr>
                        <td>
						<div class="input-group">
 							<span class="input-group-addon">
								3
							</span>
                            <html:text styleClass="form-control" property="xml_other3" onblur="checkSelectedCodes()" onkeypress="return grabEnter(event,'OtherScriptAttach()')"/>
                            <span class="input-group-btn">
		                     	<button type="button" class="btn btn-primary" title="Search code" onclick="OtherScriptAttach('xml_other3')">
	                            	<span class="glyphicon glyphicon-search"></span>
	                          	</button>
                          	</span>
                        </div> 
                        </td>
                        <td>
                        <div class="input-group">
                            <html:text styleClass="form-control" property="xml_other3_unit" styleId="xml_other3_unit"/>
                            <span class="input-group-btn"> 
                            	<button type="button" class="btn btn-primary" value=".5" onClick="$('xml_other3_unit').value = '0.5'" >.5</button>
                            </span>
                        </div>
                        </td>
                      </tr>
                      <!-- <tr>
                      <td></td>
                        <td>
                          <button class="btn btn-info pull-right btn-xs" onclick="javascript:OtherScriptAttach()">
                          	Code Search	
                          </button>
                        </td>
                      </tr> -->
                    </table>
                  </td>
                </tr>
              </table>
            </td>
            <td valign="top" style="width:32%;" >
              <table class="table table-condensed table-bordered serviceCodesTable">
                <tr style="background-color:#CCCCFF;">
                  <td width="25%" align="left" valign="middle">
                        <label><%=group3Header%></label>
                  </td>
                  <td width="61%" style="background-color:#CCCCFF;">
                    <label><bean:message key="billing.service.desc"/></label>
                  </td>
                  <td width="14%" align="right">      
                      <label>&dollar;<bean:message key="billing.service.fee"/></label>
                  </td>
                </tr>
              <%for (int i = 0; i < billlist3.length; i++) {              %>
                <tr >
                <%String svcCall = "addSvcCode('" + billlist3[i].getServiceCode() + "')";                %>
                  <td width="25%" >
                  	<label class="checkbox">
                      <html:multibox property="service" value="<%=billlist3[i].getServiceCode()%>" onclick="<%=svcCall%>"/>
                      <%=billlist3[i].getServiceCode()%>
                      </label>
                  </td>
                  <td width="61%" >
                    <%=billlist3[i].getDescription()%>
                  </td>
                  <td width="14%" align="right">
                      <%=billlist3[i].getPrice()%>
                  </td>
                </tr>
              <%}              %>
              </table>
              <!-- ONSCREEN DX CODE DISPLAY -->
              <table style="background-color:#CCCCFF;" class="tool-table table table-condensed table-borderless">
                <tr>
                  <td valign="top" width="80%">
                         <table class="table table-condensed table-borderless">
                         <tr><td style="width:60%">
                            <div class="input-group">
 
								<%--
									If the list of coding systems includes ICD10, then offer a list of options 
									including the specific MSP Dx table. 
									If the user wants a coding system but does not want the MSP table then 
									the DISABLE_MSP_DX_SYSTEM switch can be set in OSCAR properties. When this is 
									disabled the user will be presented with the other selected tables. 
								 --%>
								<c:set scope="page" var="icd10" value="false" />
								<logic:iterate id="codeSystem" name="dxCodeSystemList" property="codingSystems">									
									<c:if test="${ codeSystem eq 'icd10' }">									
										<c:set scope="page" var="isIcd10" value="true" />
									</c:if>								
								</logic:iterate>
								<c:choose>
									<c:when test="${ isIcd10 }">
										<span class="input-group-addon">
											<bean:message key="billing.diagnostic.code"/>
										</span>
										<select style="min-width: 70px;" class="form-control" name="dxCodeSystem" id="codingSystem" >							
											<oscar:oscarPropertiesCheck value="false" property="DISABLE_MSP_DX_SYSTEM">
												<option value="msp" selected>MSP Dx</option>
								 			</oscar:oscarPropertiesCheck>
								 			<logic:iterate id="codeSystem" name="dxCodeSystemList" property="codingSystems">
												<option value="<bean:write name="codeSystem"/>"><bean:write name="codeSystem" /></option>
											</logic:iterate>									
										</select>
									</c:when>
									<c:otherwise>
										<input type="hidden" id="codingSystem" value="msp" />
										<bean:message key="billing.diagnostic.code"/>
									</c:otherwise>
								</c:choose>
							</div>	
						</td>
						<td style="width:40%">
							Recently used
						</td>
						</tr>
						<tr><td>
							<div class="input-group">
								<span class="input-group-addon">
									1
								</span> 
                            	<html:text styleClass="form-control jsonDxSearchInput" styleId="jsonDxSearchInput-1" property="xml_diagnostic_detail1" />
                            	<span class="input-group-btn">
		                     		<button type="button" title="Search diagnostic code" class="btn btn-primary jsonDxSearchButton" value="jsonDxSearchInput-1">
	                            		<span class="glyphicon glyphicon-search"></span>
		                          	</button>
	                          	</span>
							</div>
						</td>
						<td rowspan="3" style="width:50%" valign="top" >
							<div id="DX_REFERENCE"></div>
						</td>
						</tr>
						<tr><td>
  							<div class="input-group">
  								<span class="input-group-addon">
									2
								</span>
                            	<html:text styleClass="form-control jsonDxSearchInput" styleId="jsonDxSearchInput-2" property="xml_diagnostic_detail2" /> 
								<span class="input-group-btn">
		                     		<button type="button"  title="Search Dx Description" class="btn btn-primary jsonDxSearchButton" value="jsonDxSearchInput-2">
	                            		<span class="glyphicon glyphicon-search"></span>
	                          		</button>
	                          	</span>
							</div>
						</td></tr>
						<tr><td>	
  							<div class="input-group">
  								<span class="input-group-addon">
									3
								</span>
	                            <html:text styleClass="form-control jsonDxSearchInput" styleId="jsonDxSearchInput-3" property="xml_diagnostic_detail3" />
	                            <span class="input-group-btn">
		                     		<button type="button" title="Search Dx Description" class="btn btn-primary jsonDxSearchButton" value="jsonDxSearchInput-3">
	                            		<span class="glyphicon glyphicon-search"></span>
	                          		</button>
	                          	</span>
							</div>
						</td></tr>
	
						</table>
                  </td>
                </tr>
              </table>
              <!-- ONSCREEN DX CODE DISPLAY END-->
              
              <table class="tool-table table table-condensed table-borderless">
                <tr>
                  <td style="padding-top:5px !important;">
                      <label for="shortClaimNote"></label><label>Short Claim Note</label></label>
                    <html:text styleClass="form-control" property="shortClaimNote" />
                  </td>
                  
                </tr>
                
                <tr>
                  <td align="left" colspan="2" >
                    <html:select styleClass="form-control" property="correspondenceCode" onchange="correspondenceNote();">
                      <html:option value="0">No Correspondence</html:option>
                      <html:option value="N">Electronic Correspondence</html:option>
                      <html:option value="C">Paper Correspondence</html:option>
                      <html:option value="B">Both</html:option>
                    </html:select>
                  </td>
                </tr>
                <tr>
                  <td style="padding-bottom:5px !important;" colspan="2" valign="top">
                    <div id="CORRESPONDENCENOTE" style="display:none;">
                      <html:textarea styleClass="form-control notes-box" property="notes" onkeyup="checkTextLimit(this.form.notes,400);"></html:textarea>
                      <small>400 characters max.</small>
                    </div>
                    <div>
                      <div>
                      <label>Billing Notes</label> 
                      <small>(Internal use. Not sent to MSP)</small>
                      </div>
                      <html:textarea styleClass="form-control notes-box" property="messageNotes"></html:textarea>
                    </div>
                  </td>
                </tr>
                
              </table>
              <div id="bcBillingError"></div>
            </td>
          </tr>
        </table>
      </div>
      </td>
    </tr>
  </table>

  	<div class="row-fluid pull-right ">
  		<div id="ignoreWarningsButton">
				<label class="checkbox" for="ignoreWarn" title="Check to ignore validation warnings">     
				   <input type="checkbox" name="ignoreWarn" id="ignoreWarn"/> 
				    Ignore Warnings
				</label>
        </div>
		<div id="buttonRow" class="button-bar">
            <input class="btn btn-md btn-primary" type="submit" name="Submit" value="Continue">
              <input class="btn btn-md btn-danger" type="button" name="Button" value="Cancel" onClick="window.close();"> 
		</div>
	</div>
    <div class="container-fluid">
    	<div id="wcbForms"></div>
    </div>
</html:form>
 </div>
 </div>
 
<oscar:oscarPropertiesCheck property="BILLING_DX_REFERENCE" value="yes">
	<script type="text/javascript">
		function getDxInformation(origRequest){
		      var url = "DxReference.jsp";
		      var ran_number=Math.round(Math.random()*1000000);
		      var params = "demographicNo=<%=bean.getPatientNo()%>&rand="+ran_number;  //hack to get around ie caching the page
		      new Ajax.Updater('DX_REFERENCE',url, {method:'get',parameters:params,asynchronous:true}); 
		}
		getDxInformation();
	</script>
</oscar:oscarPropertiesCheck>
</body>
</html>
