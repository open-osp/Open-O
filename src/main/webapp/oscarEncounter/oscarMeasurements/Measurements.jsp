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
<%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ page import="oscar.oscarEncounter.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.bean.EctMeasuringInstructionBeanHandler, oscar.oscarEncounter.oscarMeasurements.bean.EctMeasuringInstructionBean"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.oscarehr.managers.MeasurementManager"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%
    String demo = (String) request.getAttribute("demographicNo"); //bean.getDemographicNo();

    MeasurementManager measurementManager = SpringUtils.getBean(MeasurementManager.class);
    String groupName = (String) request.getAttribute("groupName");
%>

<html:html lang="en">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><logic:present name="groupName">
	<bean:write name="groupName" />
</logic:present> <bean:message key="oscarEncounter.Index.measurements" /></title>

<html:base />


<link href="/oscar/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="/oscar/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">


<link rel="stylesheet" href="/oscar/css/font-awesome.min.css">

<style>
body {
    line-height: 14px;
}
h3 {
    line-height: 14px;
}
.note {
    padding:0px;
    font-size:12px;
}
.table td {
    line-height:14px;
    padding:3px;
}
.MainTableLeftColumn {
vertical-align:top;
padding:14px;
}
</style>
    <script src="${ pageContext.request.contextPath }/library/jquery/jquery-3.6.4.min.js"></script>
    <script src="${ pageContext.request.contextPath }/share/calendar/calendar.js"></script>
    <script src="${ pageContext.request.contextPath }/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
    <script src="${ pageContext.request.contextPath }/share/calendar/calendar-setup.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="${ pageContext.request.contextPath }/share/calendar/calendar.css" title="win2k-cold-2" />

<script type="text/javascript">

function write2Parent(text){

    self.close();
    opener.document.encForm.enTextarea.value = opener.document.encForm.enTextarea.value + text;
 }

function getDropboxValue(ctr){
    var selectedItem = document.forms[0].value(inputMInstrc-ctr).options[document.forms[0].value(inputMInstrc-ctr).selectedIndex].value;
    alert("hello!");
}

function popupPage(vheight,vwidth,page) { //open a new popup window

  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(page, "blah", windowprops);
}

parentChanged = false;

function check() {
	var ret = true;

    if( parentChanged ) {
        document.forms[0].elements["value(parentChanged)"].value = "true";

        if( !confirm("<bean:message key="oscarEncounter.oscarMeasurements.Measurements.msgParentChanged"/> <oscar:nameage demographicNo="<%=demo%>"/>") )
            ret = false;
    }

    if(ret) {

      	 $.post('<%=request.getContextPath()%>/oscarEncounter/Measurements.do?ajax=true&skipCreateNote=true',$('#theForm').serialize(),function(data){
      		$("#errors_list").empty();
      		 if(data.errors) {
                $("#errors_list").prepend("<div class='alert alert-error'>");
      			 for(var x=0;x<data.errors.length;x++) {
      				 $(".alert").append(data.errors[x]);
      			 }

      		 } else {
				opener.postMessage(data,"*");
      			window.close();
      		 }
      	 },'json');

    }
}
</script>
</head>
<body class="BodyStyle" onload="window.focus();">
<html:form action="/oscarEncounter/Measurements" styleId="theForm">
	<logic:present name="css">
		<link rel="stylesheet" type="text/css" href="<bean:write name="css" />">
	</logic:present>
	<logic:notPresent name="css">
		<!--<link rel="stylesheet" type="text/css" href="styles/measurementStyle.css">-->
	</logic:notPresent>

	<table class="MainTable" id="scrollNumber1">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn"><h4><logic:present
				name="groupName">
				<bean:write name="groupName" /></h4>
			</logic:present></td>
			<td class="MainTableTopRowRightColumn" style="padding:0px">
			<table class="TopStatusBar" style="width:100%; height:100%;">
				<tr>
					<td class="Header"><h3><oscar:nameage demographicNo="<%=demo%>" /></h3></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableLeftColumn"  >
			<table>
				<tr>
					<td><a
						href="javascript: function myFunction() {return false; }"
						onClick="popupPage(150,200,'../calculators.jsp?demo=<%=demo%>'); return false;"><bean:message
						key="oscarEncounter.Index.calculators" /></a></td>
				</tr>
			</table>
			</td>
			<td class="MainTableRightColumn">

			<%=measurementManager.getDShtml(groupName)%>
			<ul id="errors_list" style="color:red">
			</ul>

			<table >
				<tr>
					<td>
					<table>
						<tr>
							<td>
                            <div class="well">
							<table class="table table-striped">
								<html:errors />
								<tr class="Header">
									<th style="width:120px"><bean:message
										key="oscarEncounter.oscarMeasurements.Measurements.headingType" />
									</th>
									<th style="width:160px"><bean:message
										key="oscarEncounter.oscarMeasurements.Measurements.headingMeasuringInstrc" />
									</th>
									<th style="width:30px"><bean:message
										key="oscarEncounter.oscarMeasurements.Measurements.headingValue" />
									</th>
									<th style="width:40px"><bean:message
										key="oscarEncounter.oscarMeasurements.Measurements.headingObservationDate" />
									</th>
									<th style="width:80px"><bean:message
										key="oscarEncounter.oscarMeasurements.Measurements.headingComments" />
									</th>
									<th style="width:10px"></th>
								</tr>
								<% int i = 0;%>
								<logic:iterate id="measurementType" name="measurementTypes"
									property="measurementTypeVector" indexId="ctr">
									<tr class="data" id="row-<bean:write name="measurementType" property="type" />">
										<td>
											<span title="<bean:write name="measurementType" property="typeDesc" />"><bean:write
											name="measurementType" property="typeDisplayName" /></span></td>
										<td><logic:iterate id="mInstrc"
											name="<%=\"mInstrcs\"+ ctr%>"
											property="measuringInstructionList">
											<input type="radio"
												name='<%= "value(inputMInstrc-" + ctr + ")" %>'
												value="<bean:write name="mInstrc" property="measuringInstrc"/>"
												checked />
											<bean:write name="mInstrc" property="measuringInstrc" />
											<br>
										</logic:iterate></td>
										<%
										EctMeasuringInstructionBeanHandler mInstrh = (EctMeasuringInstructionBeanHandler) session.getAttribute("mInstrcs"+i);
										EctMeasuringInstructionBean mInstrBean = mInstrh.getMeasuringInstructionList().get(0);
										Integer index;
										String[] options;
										String measuringInstruction = mInstrBean.getMeasuringInstrc();
										if( measuringInstruction.startsWith("Choose radio") ) {
										   index = 12;
										   measuringInstruction = measuringInstruction.substring(index);
										   options = measuringInstruction.split(",");
										%>
										<td>
										<%
											for( int idx = 0; idx < options.length; ++idx ) {
										%>
										<html:radio property='<%= "value(inputValue-" + ctr + ")" %>' value="<%=options[idx].trim()%>"></html:radio><%=options[idx]%>&nbsp;

										<%}%>
										</td>
										<%}else { %>

										<td><input type="text" class="input-small" name='<%= "value(inputValue-" + ctr + ")" %>' id='<%= "inputValue-" + ctr  %>' /></td>
										<%} %>
										<td><input type="text" class="input-medium" name='<%= "value(date-" + ctr + ")" %>' id='<%= "date-" + ctr  %>' /></td>
										<script>Calendar.setup( { inputField : "<%= "date-" + ctr %>", ifFormat : "%Y-%m-%d",  button : "<%= "date-" + ctr %>" });</script>
										<td><input type="text" class="input-large" name='<%= "value(comments-" + ctr + ")" %>' id='<%= "comments-" + ctr  %>' /></td>
                                        <td>
										<input type="hidden"
											name='<%= "value(inputType-" + ctr + ")" %>'
											value="<bean:write name="measurementType" property="type" />" />
										<input type="hidden"
											name='<%= "value(inputTypeDisplayName-" + ctr + ")" %>'
											value="<bean:write name="measurementType" property="typeDisplayName" />" />
										<input type="hidden"
											name='<%= "value(validation-" + ctr + ")" %>'
											value="<bean:write name="measurementType" property="validation" />" />
                                        </td>
										<% i++; %>
									</tr>
									<logic:present name='measurementType' property='lastMInstrc'>
										<tr class="note">
											<td><bean:message
												key="oscarEncoutner.oscarMeasurements.msgTheLastValue" />:</td>
											<td>&nbsp;<bean:write name='measurementType'
												property='lastMInstrc' /></td>
											<td>&nbsp;<bean:write name='measurementType'
												property='lastData' /></td>
											<td>&nbsp;<bean:write name='measurementType'
												property='lastDateEntered' /></td>
											<td>&nbsp;<bean:write name='measurementType'
												property='lastComments' /></td>
											<td><i class="icon-time icon-large"
												title='<bean:message key="oscarEncounter.Index.oldMeasurements"/>'
												onClick="popupPage(300,800,'SetupDisplayHistory.do?type=<bean:write name="measurementType" property="type" />'); return false;" ></i></td>
										</tr>
									</logic:present>
								</logic:iterate>
								<input type="hidden" name="value(numType)"
									value="<%=String.valueOf(i)%>" />
								<input type="hidden" name="value(groupName)"
									value="<bean:write name="groupName"/>" />
								<input type="hidden" name="value(parentChanged)" value="false" />
								<input type="hidden" name="value(demographicNo)"
									value="<%=demo%>" />
								<input type="hidden" name="demographic_no" value="<%=demo%>" />
								<logic:present name="css">
									<input type="hidden" name="value(css)"
										value="<bean:write name="css"/>" />
								</logic:present>
								<logic:notPresent name="css">
									<input type="hidden" name="value(css)" value="" />
								</logic:notPresent>

							</table>
                            </div> <!-- well -->
							<table>
								<tr>
									<td><input type="button" name="Button" class="btn"
										value="<bean:message key="global.btnCancel"/>"
										onClick="window.close()"></td>
									<td><input type="button" name="Button" class="btn btn-primary"
										value="<bean:message key="global.btnSubmit"/>"
										onclick="check();" /></td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableBottomRowLeftColumn"></td>
			<td class="MainTableBottomRowRightColumn"></td>
		</tr>
	</table>
</html:form>

<script>
$( document ).ready(function() {

//if WT, HT and BMI exists then allow the link
if($('#row-WT').length && $('#row-HT').length && $('#row-BMI').length){

$('#row-WT td:eq(2) input').on( "keyup", function(){
  calcBMI( $(this).val(),$('#row-HT td:eq(2) input').val() );
});

$('#row-HT td:eq(2) input').on( "keyup", function(){
  calcBMI( $('#row-WT td:eq(2) input').val(),$(this).val() );
});
}

});

var utc = new Date().toJSON().slice(0,10);
$("[id^=date-]").val(utc);

function calcBMI(w,h) {
b = '';

if ( !isNaN(parseFloat(w)) && !isNaN(parseFloat(h)) && h!=="" && w!=="" ) {
  if (h > 0) {
    b = (w/Math.pow(h/100,2)).toFixed(1);
    $('#row-BMI td:eq(2) input').val(b);
    $('#row-BMI').css("background-color", "#d9e6f2");
  }
 }
}
</script>



</body>
</html:html>