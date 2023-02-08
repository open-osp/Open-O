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
<!DOCTYPE html>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.*"%>
<%@ page import="oscar.oscarLab.ca.on.*"%>
<%@ page import="org.oscarehr.util.DbConnectionFilter"%>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.dao.ViewDao" %>
<%@ page import="org.oscarehr.common.model.View,org.oscarehr.util.LocaleUtils" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.oscarehr.common.model.TicklerLink" %>
<%@ page import="org.oscarehr.common.dao.TicklerLinkDao" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="oscar.MyDateFormat" %>
<%@ page import="oscar.OscarProperties" %>
<%@ page import="org.oscarehr.common.model.Site" %>
<%@ page import="org.oscarehr.common.dao.SiteDao" %>
<%@ page import="org.oscarehr.common.model.Tickler" %>
<%@ page import="org.oscarehr.common.model.TicklerComment" %>
<%@ page import="org.oscarehr.common.model.TicklerUpdate" %>
<%@ page import="org.oscarehr.common.model.CustomFilter" %>
<%@ page import="org.oscarehr.managers.TicklerManager" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Locale" %>

<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_tickler" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
	TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);

	String labReqVer = oscar.OscarProperties.getInstance().getProperty("onare_labreqver","07");
	if(labReqVer.equals("")) {labReqVer="07";}

	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

	String user_no;
     user_no = (String) session.getAttribute("user");

     TicklerLinkDao ticklerLinkDao = (TicklerLinkDao) SpringUtils.getBean("ticklerLinkDao");

     String createReport = request.getParameter("Submit");
     boolean doCreateReport = createReport != null && createReport.equals("Create Report");

     ViewDao viewDao = (ViewDao) SpringUtils.getBean("viewDao");
     String userRole = (String) session.getAttribute("userrole");
     Map<String, View> ticklerView = viewDao.getView("tickler", userRole, user_no);

     String providerview = "all";
     View providerView = ticklerView.get("providerview");

     if( providerView != null  && !doCreateReport)
     {
         providerview = providerView.getValue();
     }
     else if (request.getParameter("providerview") != null)
     {
         providerview = request.getParameter("providerview");
     }

     String assignedTo = "all";
     View assignedToView = ticklerView.get("assignedTo");

     if( assignedToView != null && !doCreateReport)
     {
         assignedTo = assignedToView.getValue();
     }
     else if (request.getParameter("assignedTo") != null)
     {
         assignedTo = request.getParameter("assignedTo");
     }

     String mrpview = "all";
     View mrpView = ticklerView.get("mrpview");

     if( mrpView != null && !doCreateReport)
     {
   	  mrpview = mrpView.getValue();
     }
     else if (request.getParameter("mrpview") != null)
     {
         mrpview = request.getParameter("mrpview");
     }

    String ticklerview = "A";
    View statusView = ticklerView.get("ticklerview");

    if( statusView != null && !doCreateReport)
    {
       ticklerview = statusView.getValue();
    }
     else if (request.getParameter("ticklerview") != null)
     {
         ticklerview = request.getParameter("ticklerview");
     }

    String xml_vdate = "";
    View beginDateView = ticklerView.get("dateBegin");

    if( beginDateView != null && !doCreateReport)
    {
        xml_vdate = beginDateView.getValue();
    }
    else if (request.getParameter("xml_vdate") != null)
    {
         xml_vdate = request.getParameter("xml_vdate");
    }

    Calendar now=Calendar.getInstance();
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH)+1);
    int curDay = now.get(Calendar.DAY_OF_MONTH);

    String xml_appointment_date = MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay);
    View endDateView = ticklerView.get("dateEnd");

    if( endDateView != null && !doCreateReport)
    {
        xml_appointment_date = endDateView.getValue();
    }
    else if (request.getParameter("xml_appointment_date") != null)
    {
        xml_appointment_date = request.getParameter("xml_appointment_date");
    }
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<html:html locale="true">
<head>
<title><bean:message key="tickler.ticklerMain.title"/> Manager</title>

    <script src="${pageContext.request.contextPath}/library/jquery/jquery-1.12.0.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/library/jquery/jquery-ui-1.12.1.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/library/DataTables-1.10.12/media/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/share/javascript/prototype.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/share/javascript/scriptaculous.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/library/bootstrap/3.0.0/js/bootstrap.min.js" type="text/javascript"></script>

    <link href="${pageContext.request.contextPath}/library/DataTables-1.10.12/media/css/jquery.dataTables.min.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" media="all" href="${pageContext.request.contextPath}/css/print.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/library/jquery/jquery-ui-1.12.1.min.css">
    <link href="${pageContext.request.contextPath}/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet" type="text/css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-awesome.min.css" type="text/css">
<style>
    tr.error td {
        color:red !important;
    }
</style>
    <script type="application/javascript">
jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery( "#note-form" ).dialog({
		autoOpen: false,
		height: 340,
		width: 450,
		modal: true,
		
		close: function() {
			
		}
	});

    jQuery("#ticklerResults").dataTable({
        "searching": false
    })
		
});

function openNoteDialog(demographicNo, ticklerNo) {
	jQuery("#tickler_note_demographicNo").val(demographicNo);
	jQuery("#tickler_note_ticklerNo").val(ticklerNo);
	

	jQuery("#tickler_note_noteId").val('');
	jQuery("#tickler_note").val('');
	jQuery("#tickler_note_revision").html('');
	jQuery("#tickler_note_revision_url").attr('onclick','');
	jQuery("#tickler_note_editor").html('');
	jQuery("#tickler_note_obsDate").html('');
		
	//is there an existing note?
	jQuery.ajax({url:'../CaseManagementEntry.do',
		data: { method: "ticklerGetNote", ticklerNo: jQuery('#tickler_note_ticklerNo').val()  },
		async:false, 
		dataType: 'json',
		success:function(data) {
			if(data != null) {
				jQuery("#tickler_note_noteId").val(data.noteId);
				jQuery("#tickler_note").val(data.note);
				jQuery("#tickler_note_revision").html(data.revision);
				jQuery("#tickler_note_revision_url").attr('onclick','window.open(\'../CaseManagementEntry.do?method=notehistory&noteId='+data.noteId+'\');return false;');
				jQuery("#tickler_note_editor").html(data.editor);
				jQuery("#tickler_note_obsDate").html(data.obsDate);
			}
		},
		error: function(jqXHR, textStatus, errorThrown ) {
			alert(errorThrown);
		}
		});
	
	jQuery( "#note-form" ).dialog( "open" );
}
function closeNoteDialog() {
	jQuery( "#note-form" ).dialog( "close" );
}
function saveNoteDialog() {
	//alert('not yet implemented');
	jQuery.ajax({url:'../CaseManagementEntry.do',
		data: { method: "ticklerSaveNote", noteId: jQuery("#tickler_note_noteId").val(), value: jQuery('#tickler_note').val(), demographicNo: jQuery('#tickler_note_demographicNo').val(), ticklerNo: jQuery('#tickler_note_ticklerNo').val()  },
		async:false, 
		success:function(data) {
		 // alert('ok');		  
		},
		error: function(jqXHR, textStatus, errorThrown ) {
			alert(errorThrown);
		}
		});	
	
	jQuery( "#note-form" ).dialog( "close" );
}

function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
  var popup=window.open(page, "attachment", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}
function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}
function openBrWindow(theURL,winName,features) {
  window.open(theURL,winName,features);
}
function setfocus() {
  this.focus();
}
function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}



function allYear()
{
var newD = "8888-12-31";
var beginD = "1900-01-01"
	document.serviceform.xml_appointment_date.value = newD;
		document.serviceform.xml_vdate.value = beginD;
}

    function Check(e)
    {
	e.checked = true;
	//Highlight(e);
    }

    function Clear(e)
    {
	e.checked = false;
	//Unhighlight(e);
    }

    function reportWindow(page) {
    windowprops="height=660, width=960, location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
    var popup = window.open(page, "labreport", windowprops);
    popup.focus();
}

    function CheckAll()
    {
	var ml = document.ticklerform;
	var len = ml.elements.length;
	for (var i = 0; i < len; i++) {
	    var e = ml.elements[i];
	    if (e.name == "checkbox") {
		Check(e);
	    }
	}
	//ml.toggleAll.checked = true;
    }

    function ClearAll()
    {
	var ml = document.ticklerform;
	var len = ml.elements.length;
	for (var i = 0; i < len; i++) {
	    var e = ml.elements[i];
	    if (e.name == "checkbox") {
		Clear(e);
	    }
	}
	//ml.toggleAll.checked = false;
    }

    function Highlight(e)
    {
	var r = null;
	if (e.parentNode && e.parentNode.parentNode) {
	    r = e.parentNode.parentNode;
	}
	else if (e.parentElement && e.parentElement.parentElement) {
	    r = e.parentElement.parentElement;
	}
	if (r) {
	    if (r.className == "msgnew") {
		r.className = "msgnews";
	    }
	    else if (r.className == "msgold") {
		r.className = "msgolds";
	    }
	}
    }

    function Unhighlight(e)
    {
	var r = null;
	if (e.parentNode && e.parentNode.parentNode) {
	    r = e.parentNode.parentNode;
	}
	else if (e.parentElement && e.parentElement.parentElement) {
	    r = e.parentElement.parentElement;
	}
	if (r) {
	    if (r.className == "msgnews") {
		r.className = "msgnew";
	    }
	    else if (r.className == "msgolds") {
		r.className = "msgold";
	    }
	}
    }

    function AllChecked()
    {
	ml = document.messageList;
	len = ml.elements.length;
	for(var i = 0 ; i < len ; i++) {
	    if (ml.elements[i].name == "Mid" && !ml.elements[i].checked) {
		return false;
	    }
	}
	return true;
    }

    function Delete()
    {
	var ml=document.messageList;
	ml.DEL.value = "1";
	ml.submit();
    }

    function SynchMoves(which) {
	var ml=document.messageList;
	if(which==1) {
	    ml.destBox2.selectedIndex=ml.destBox.selectedIndex;
	}
	else {
	    ml.destBox.selectedIndex=ml.destBox2.selectedIndex;
	}
    }

    function SynchFlags(which)
    {
	var ml=document.messageList;
	if (which == 1) {
	    ml.flags2.selectedIndex = ml.flags.selectedIndex;
	}
	else {
	    ml.flags.selectedIndex = ml.flags2.selectedIndex;
	}
    }

    function SetFlags()
    {
	var ml = document.messageList;
	ml.FLG.value = "1";
	ml.submit();
    }

    function Move() {
	var ml = document.messageList;
	var dbox = ml.destBox;
	if(dbox.options[dbox.selectedIndex].value == "@NEW") {
	    nn = window.prompt("<bean:message key="tickler.ticklerMain.msgFolderName"/>","");
	    if(nn == null || nn == "null" || nn == "") {
		dbox.selectedIndex = 0;
		ml.destBox2.selectedIndex = 0;
	    }
	    else {
		ml.NewFol.value = nn;
		ml.MOV.value = "1";
		ml.submit();
	    }
	}
	else {
	    ml.MOV.value = "1";
	    ml.submit();
	}
    }

    function saveView() {

        var url = "<c:out value="${ctx}"/>/saveWorkView.do";
        var role = "<%=(String)session.getAttribute("userrole")%>";
        var provider_no = "<%=(String) session.getAttribute("user")%>";
        var params = "method=save&view_name=tickler&userrole=" + role + "&providerno=" + provider_no +
            "&name=ticklerview&value=" + $F("ticklerview") +
            // "&name=dateBegin&value=" + $F("xml_vdate") + "&name=dateEnd&value=" + $F("xml_appointment_date") +
            "&name=providerview&value=" + encodeURI($F("providerview")) +
            "&name=assignedTo&value=" + encodeURI($F("assignedTo"))  + "&name=mrpview&value=" + encodeURI($F("mrpview"));
        var sortables = document.getElementsByClassName('tableSortArrow');

        var attrib = null;
        var columnId = -1;
        for( var idx = 0; idx < sortables.length; ++idx ) {
            attrib = sortables[idx].readAttribute("sortOrder");
            if( attrib != null ) {
                columnId = sortables[idx].previous().readAttribute("columnId");
                break;
            }
        }

        if( columnId != -1 ) {
            params += "&name=columnId&value=" + columnId + "&name=sortOrder&value=" + attrib;
        }

        //console.log(params);
        new Ajax.Request (
            url,
            {
                method: "post",
                postBody: params,
                onSuccess: function(response) {
                    alert("View Saved");
                },
                onFailure: function(request) {
                    alert("View not saved! " + request.status);
                }
            }
        );

    }
    
    function generateRenalLabReq(demographicNo) {
		var url = '${pageContext.request.contextPath}/form/formlabreq<%=labReqVer%>.jsp?demographic_no='+demographicNo+'&formId=0&provNo=<%=session.getAttribute("user")%>&fromSession=true';
		jQuery.ajax({url:'${pageContext.request.contextPath}/renal/Renal.do?method=createLabReq&demographicNo='+demographicNo,async:false, success:function(data) {
			popupPage(900,850,url);
		}});
	}

</script>

</head>


<body>
<div class="container">
<table>
  <tr class="noprint">
    <td>
      <h2><bean:message key="tickler.ticklerMain.msgTickler"/> Manager
      </h2>
    </td>
<%--	<td>--%>
<%--            <i class="icon-question-sign"></i>--%>
<%--            <a href="#" target="_blank"><bean:message key="app.top1"/></a>--%>
<%--            <i class=" icon-info-sign" style="margin-left:10px;"></i>--%>
<%--            <a href="javascript:void(0)"  onClick="window.open('${pageContext.request.contextPath}/oscarEncounter/About.jsp','About OSCAR','scrollbars=1,resizable=1,width=800,height=600,left=0,top=0')" ><bean:message key="global.about" /></a>--%>
<%--    </td>--%>
  </tr>        
</table>
             
<form name="serviceform" method="get" action="ticklerMain.jsp" class="form-inline">
    <div class="control-container">
    <label for=""><bean:message key="tickler.ticklerMain.formDateRange"/>  <a href="#" onClick="allYear()"><bean:message key="tickler.ticklerMain.btnViewAll"/></a></label>
    <div class="form-group">
        <label for="xml_vdate">From</label>
        <input type="date" class="form-control" name="xml_vdate" id="xml_vdate">
    </div>
    <div class="form-group">
        <label for="xml_appointment_date">To</label>
        <input type="date" class="form-control" name="xml_appointment_date" id="xml_appointment_date" value="<%=xml_appointment_date%>">
    </div>


            <div class="form-group">
                <label for="ticklerview"><bean:message key="tickler.ticklerMain.formMoveTo"/></label>
                <select id="ticklerview" class="form-control" name="ticklerview">
                <option value="A" <%=ticklerview.equals("A")?"selected":""%>><bean:message key="tickler.ticklerMain.formActive"/></option>
                <option value="C" <%=ticklerview.equals("C")?"selected":""%>><bean:message key="tickler.ticklerMain.formCompleted"/></option>
                <option value="D" <%=ticklerview.equals("D")?"selected":""%>><bean:message key="tickler.ticklerMain.formDeleted"/></option>
                </select>
            </div>
            <div class="form-group">
                 <label for="mrpview"> <bean:message key="tickler.ticklerMain.MRP"/></label>
                <select id="mrpview" class="form-control" name="mrpview">
                <option value="all" <%=mrpview.equals("all")?"selected":""%>><bean:message key="tickler.ticklerMain.formAllProviders"/></option>
                <%
                    ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
                                        List<Provider> providers = providerDao.getActiveProviders();
                                        for (Provider p : providers) {
                %>
                <option value="<%=p.getProviderNo()%>" <%=mrpview.equals(p.getProviderNo())?"selected":""%>><%=p.getLastName()%>,<%=p.getFirstName()%></option>
                <%
                    }
                %>
                  </select>
            </div>
        <div class="form-group">
              <label for="providerview"><bean:message key="tickler.ticklerMain.msgCreator"/></label>

            <select id="providerview" class="form-control" name="providerview">
            <option value="all" <%=providerview.equals("all")?"selected":""%>><bean:message key="tickler.ticklerMain.formAllProviders"/></option>
            <%
                for (Provider p : providers) {
            %>
            <option value="<%=p.getProviderNo()%>" <%=providerview.equals(p.getProviderNo())?"selected":""%>><%=p.getLastName()%>,<%=p.getFirstName()%></option>
            <%
                }
            %>
              </select>
        </div>
    <div class="form-group">
          <label for="assignedTo"><bean:message key="tickler.ticklerMain.msgAssignedTo"/></label>
<%
	if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable())
{
        	SiteDao siteDao = (SiteDao)SpringUtils.getBean("siteDao");
          	List<Site> sites = siteDao.getActiveSitesByProviderNo(user_no);
%>
      <script>
var _providers = [];
<%for (int i=0; i<sites.size(); i++) {%>
	_providers["<%=sites.get(i).getSiteId()%>"]="<%Iterator<Provider> iter = sites.get(i).getProviders().iterator();
	while (iter.hasNext()) {
		Provider p=iter.next();
		if ("1".equals(p.getStatus())) {%><option value='<%=p.getProviderNo()%>'><%=p.getLastName()%>, <%=p.getFirstName()%></option><%}}%>";
<%}%>
function changeSite(sel) {
	sel.form.assignedTo.innerHTML=sel.value=="none"?"":_providers[sel.value];
}
      </script>
      	<select id="site" class="form-control" name="site" onchange="changeSite(this)">
      		<option value="none">---select clinic---</option>
      	<%
      		for (int i=0; i<sites.size(); i++) {
      	%>
      		<option value="<%=sites.get(i).getSiteId()%>" <%=sites.get(i).getSiteId().toString().equals(request.getParameter("site"))?"selected":""%>><%=sites.get(i).getName()%></option>
      	<%
      		}
      	%>
      	</select>
      	<select id="assignedTo" name="assignedTo" style="width:140px"></select>
<%
	if (request.getParameter("assignedTo")!=null) {
%>
      	<script>
     	changeSite(document.getElementById("site"));
      	document.getElementById("assignedTo").value='<%=request.getParameter("assignedTo")%>';
      	</script>
<%
	}
} else {
%>
        <select id="assignedTo" class="form-control" name="assignedTo">
        <% 
        // Check for property to default assigned provider and if present - default to user logged in
        	boolean ticklerDefaultAssignedProvier = OscarProperties.getInstance().isPropertyActive("tickler_default_assigned_provider");
        	if (ticklerDefaultAssignedProvier) { 
        		if("all".equals(assignedTo)) {
        			assignedTo = user_no;
        		}
        	}
        %>
        	<option value="all" <%=assignedTo.equals("all")?"selected":""%>><bean:message key="tickler.ticklerMain.formAllProviders"/></option>
        <% 	
        	List<Provider> providersActive = providerDao.getActiveProviders(); 
                                    for (Provider p : providersActive) {
        %>
        <option value="<%=p.getProviderNo()%>" <%=assignedTo.equals(p.getProviderNo())?"selected":""%>><%=p.getLastName()%>, <%=p.getFirstName()%></option>
        <%
        	}
        %>
        </select>
<%
	}
%>
    </div>
    </div>
        <div class="button-container pull-right">
        <input type="hidden" name="Submit" value="">
        <input type="button" class="btn btn-primary" value="<bean:message key="tickler.ticklerMain.btnCreateReport"/>" class="mbttn noprint" onclick="document.forms['serviceform'].Submit.value='Create Report'; document.forms['serviceform'].submit();">
        <input type="button" class="btn" value="<bean:message key="tickler.ticklerMain.msgSaveView"/>" class="mbttn" onclick="saveView();">
        </div>

</form>

<form name="ticklerform" method="post" action="dbTicklerMain.jsp">
		<% Locale locale = request.getLocale();%>

<table id="ticklerResults" class="table table-striped table-compact" >
    <thead>
        <tr>
            <th>&nbsp;</th>
<% 
            boolean ticklerEditEnabled = Boolean.parseBoolean(OscarProperties.getInstance().getProperty("tickler_edit_enabled")); 
            if (ticklerEditEnabled) { 
%>
            <th>&nbsp;</th>
<%          
            }
%>            
            <th>

                    <bean:message key="tickler.ticklerMain.msgDemographicName"/>
            </th>
            <th>
                    <bean:message key="tickler.ticklerMain.msgCreator"/>
            </th>           
            <th>
                    <bean:message key="tickler.ticklerMain.msgDate"/>

            </th>
            <th >
                <bean:message key="tickler.ticklerMain.msgCreationDate"/>
            </th>

            <th >
                <bean:message key="tickler.ticklerMain.Priority"/>
            </th>

            <th >
                <bean:message key="tickler.ticklerMain.taskAssignedTo"/>

            </th>

            <th>

                    <bean:message key="tickler.ticklerMain.status"/>
            </th>
            <th>
                Comment

            </th>
            <th></th>
        </tr>
    </thead>
    <tfoot>
<%
                                Integer footerColSpan = 10;
                                if (ticklerEditEnabled) {
                                    footerColSpan = 11;
                                }
%>
                                <tr class="noprint"><td colspan="<%=footerColSpan%>" class="white"><a id="checkAllLink" name="checkAllLink" href="javascript:CheckAll();"><bean:message key="tickler.ticklerMain.btnCheckAll"/></a> - <a href="javascript:ClearAll();"><bean:message key="tickler.ticklerMain.btnClearAll"/></a>     
                                    <input type="button" class="btn" name="button" value="<bean:message key="tickler.ticklerMain.btnAddTickler"/>" onClick="window.open('ticklerAdd.jsp')" class="sbttn">
                                    <input type="hidden" name="submit_form" value="">
                                    <%
                                    	if (ticklerview.compareTo("D") == 0){
                                    %>
                                    <input type="button" class="btn" value="<bean:message key="tickler.ticklerMain.btnEraseCompletely"/>" class="sbttn" onclick="document.forms['ticklerform'].submit_form.value='Erase Completely'; document.forms['ticklerform'].submit();">
                                    <%
                                    	} else{
                                    %>
                                    <input type="button" class="btn" value="<bean:message key="tickler.ticklerMain.btnComplete"/>" class="sbttn" onclick="document.forms['ticklerform'].submit_form.value='Complete'; document.forms['ticklerform'].submit();">
                                    <input type="button" class="btn btn-danger" value="<bean:message key="tickler.ticklerMain.btnDelete"/>" class="sbttn" onclick="document.forms['ticklerform'].submit_form.value='Delete'; document.forms['ticklerform'].submit();">
                                    <%
                                    	}
                                    %>
                            <input type="button" name="button" class="btn" value="<bean:message key="global.btnCancel"/>" onClick="window.close()" class="sbttn"> </td></tr>
                        </tfoot>


                        <tbody>

                            <%
                            	String dateBegin = xml_vdate;
								  String dateEnd = xml_appointment_date;
								  
								  String vGrantdate = "1980-01-07 00:00:00.0";
								  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm.SSS", locale);
								
								  if (dateEnd.compareTo("") == 0) dateEnd = MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay);
								  if (dateBegin.compareTo("") == 0) dateBegin="1950-01-01"; // any early start date should suffice for selecting since the beginning
								                            
								  CustomFilter filter = new CustomFilter();
								  filter.setPriority(null);
								  
								  filter.setStatus(ticklerview);
								  		
								  filter.setStartDateWeb(dateBegin);
								  filter.setEndDateWeb(dateEnd);
								  filter.setPriority(null);
								  
								  if( !mrpview.isEmpty() && !mrpview.equals("all")) {
								  	filter.setMrp(mrpview);
								  }
								  
								  if (!providerview.isEmpty() && !providerview.equals("all")) {
								      filter.setProvider(providerview);
								  }
								  
								  if (!assignedTo.isEmpty() && !assignedTo.equals("all")) {
								      filter.setAssignee(assignedTo);
								  }
                                                 
                                  filter.setSort_order("desc");
                                                                  
								  List<Tickler> ticklers = ticklerManager.getTicklers(loggedInInfo, filter);
								  

								  String rowColour = "white";
								
								  for (Tickler t : ticklers) {
								      Demographic demo = t.getDemographic(); 
								     
								      vGrantdate = t.getServiceDate() + " 00:00:00.0";
								      java.util.Date grantdate = dateFormat.parse(vGrantdate);
								      java.util.Date toDate = new java.util.Date();
								      long millisDifference = toDate.getTime() - grantdate.getTime();
								
								      long ONE_DAY_IN_MS = (1000 * 60 * 60 * 24);                                                      
								      long daysDifference = millisDifference / (ONE_DAY_IN_MS);
								
								      String numDaysUntilWarn = OscarProperties.getInstance().getProperty("tickler_warn_period");
								      long ticklerWarnDays = Long.parseLong(numDaysUntilWarn);
								      boolean ignoreWarning = (ticklerWarnDays < 0);
								      boolean warning = false;
								      
								      //Set the colour of the table cell 
								      String warnColour = "";
								      if (!ignoreWarning && (daysDifference >= ticklerWarnDays)){
								          warnColour = "Red";
                                          warning = true;
								      }

								      String cellColour = rowColour + warnColour;
                            %>

                                <tr <%=warning?"class='error'":""%>>
                                    <td class="<%=cellColour%>"><input type="checkbox" name="checkbox" value="<%=t.getId()%>" class="noprint"></td>
                                    <%
                                    	if (ticklerEditEnabled) {
                                    %>
                                    <td width="3%" ROWSPAN="1" class="<%=cellColour%>"><a href=# title="<bean:message key="tickler.ticklerMain.editTickler"/>" onClick="window.open('../tickler/ticklerEdit.jsp?tickler_no=<%=t.getId()%>')"><i class="icon-pencil"></i></a></td>
                                    <%
                                    	}
                                    %>                                    
                                    <TD  class="<%=cellColour%>"><a href=# onClick="popupPage(600,800,'../demographic/demographiccontrol.jsp?demographic_no=<%=demo.getDemographicNo()%>&displaymode=edit&dboperation=search_detail')"><%=demo.getLastName()%>,<%=demo.getFirstName()%></a></TD>
                                    <TD  class="<%=cellColour%>"><%=t.getProvider() == null ? "N/A" : t.getProvider().getFormattedName()%></TD>
                                    <TD  class="<%=cellColour%>"><%=t.getServiceDate()%></TD>
                                    <TD  class="<%=cellColour%>"><%=t.getUpdateDate()%></TD>
                                    <TD  class="<%=cellColour%>"><%=t.getPriority()%></TD>
                                    <TD  class="<%=cellColour%>"><%=t.getAssignee() != null ? t.getAssignee().getLastName() + ", " + t.getAssignee().getFirstName() : "N/A"%></TD>
                                    <TD  class="<%=cellColour%>"><%=t.getStatusDesc(locale)%></TD>
                                    <TD  class="<%=cellColour%>"><%=t.getMessage()%>
                                        
                                        <%
                                                                                	List<TicklerLink> linkList = ticklerLinkDao.getLinkByTickler(t.getId().intValue());
                                                                                                                        if (linkList != null){
                                                                                                                            for(TicklerLink tl : linkList){
                                                                                                                                String type = tl.getTableName();
                                                                                %>

                                                <%
                                                	if ( LabResultData.isMDS(type) ){
                                                %>
                                                <a href="javascript:reportWindow('SegmentDisplay.jsp?segmentID=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
                                                <%
                                                	}else if (LabResultData.isCML(type)){
                                                %>
                                                <a href="javascript:reportWindow('../lab/CA/ON/CMLDisplay.jsp?segmentID=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
                                                <%
                                                	}else if (LabResultData.isHL7TEXT(type)){
                                                %>
                                                <a href="javascript:reportWindow('../lab/CA/ALL/labDisplay.jsp?segmentID=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
                                                <%
                                                	}else if (LabResultData.isDocument(type)){
                                                %>
                                                <a href="javascript:reportWindow('../dms/ManageDocument.do?method=display&doc_no=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
                                                <%
                                                	}else if (LabResultData.isHRM(type)){
                                                %>
                                                <a href="javascript:reportWindow('../hospitalReportManager/Display.do?id=<%=tl.getTableId()%>')">ATT</a>                                                
                                                <%
                                                	}else {
                                                %>
                                                <a href="javascript:reportWindow('../lab/CA/BC/labDisplay.jsp?segmentID=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
                                                <%
                                                	}
                                                %>
                                        <%
                                        	}
                                                                                }
                                        %>
                                        
                                    </td>
                                    <td  class="<%=cellColour%> noprint">
                                    	<a href="#" onClick="return openNoteDialog('<%=demo.getDemographicNo() %>','<%=t.getId() %>');return false;" title="note">
                                    		<i class="icon-comment"></i>
                                    	</a>
                                    </td>
                                </tr>
                                <%
                                	Set<TicklerComment> tcomments = t.getComments();
                                                                    if (ticklerEditEnabled && !tcomments.isEmpty()) {
                                                                        for(TicklerComment tc : tcomments) {
                                %>
                                    <tr>
                                        <td class="<%=cellColour%>"></td>
                                        <td class="<%=cellColour%>"></td>
                                        <td class="<%=cellColour%>"></td>
                                        <td  class="<%=cellColour%>"><%=tc.getProvider().getLastName()%>,<%=tc.getProvider().getFirstName()%></td>
                                        <td  class="<%=cellColour%>"></td>
                                        <% if (tc.isUpdateDateToday()) { %>
                                        <td  class="<%=cellColour%>"><%=tc.getUpdateTime(locale)%></td>
                                        <% } else { %>
                                        <td  class="<%=cellColour%>"><%=tc.getUpdateDate(locale)%></td>
                                        <% } %>
                                        <td  class="<%=cellColour%>"></td>
                                        <td  class="<%=cellColour%>"></td>
                                        <td  class="<%=cellColour%>"></td>
                                        <td  class="<%=cellColour%>"><%=tc.getMessage()%></td>
                                        <td  class="<%=cellColour%>"></td>
                                    </tr>
                                <%      }                                        
                                    }
                            }

                            if (ticklers.isEmpty()) {
                            %>
                            <tr><td colspan="10" class="white"><bean:message key="tickler.ticklerMain.msgNoMessages"/></td></tr>                                                            
                            <%}%>
                        </tbody>

        
</table></form>
    
<p class="yesprint">
	<%=OscarProperties.getConfidentialityStatement()%>
</p>

<div id="note-form" title="Tickler Note">
	<form>
		
			<table>
				<tbody>
					<textarea id="tickler_note" name="tickler_note" style="width:100%;height:80%"></textarea>		
					<input type="hidden" name="tickler_note_demographicNo" id="tickler_note_demographicNo" value=""/>	
					<input type="hidden" name="tickler_note_ticklerNo" id="tickler_note_ticklerNo" value=""/>	
					<input type="hidden" name="tickler_note_noteId" id="tickler_note_noteId" value=""/>	
				</tbody>
			</table>
			<br/>
			<table>
				<tr>
					<td >
						<a href="javascript:void(0)" onclick="saveNoteDialog();return false;">
							<img src="${pageContext.request.contextPath}/oscarEncounter/graphics/note-save.png"/>
						</a>
						<a href="javascript:void(0)" onclick="closeNoteDialog();return false;">
							<img src="${pageContext.request.contextPath}/oscarEncounter/graphics/system-log-out.png"/>
						</a>
					</td>
					<td style="width:40%" nowrap="nowrap">
					Date: <span id="tickler_note_obsDate"></span> rev <a id="tickler_note_revision_url" href="javascript:void(0)" onClick=""><span id="tickler_note_revision"></span></a><br/>
					Editor: <span id="tickler_note_editor"></span>
					</td>
				</tr>
			
			</table>
	<input type='button' class="btn" name='print' value='<bean:message key="global.btnPrint"/>' onClick='window.print()' class="sbttn">
	</form>	
</div>

</div>
</body>
</html:html>
