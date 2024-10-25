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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
        import="oscar.oscarRx.pageUtil.*,oscar.oscarRx.data.*,java.util.*"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_rx" rights="w" reverse="<%=true%>">
    <%authed=false; %>
    <%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
    if(!authed) {
        return;
    }

    String type = request.getParameter("type");
%>

<html:html lang="en">
    <head>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <script type="text/javascript">
            <%
             if (request.getParameter("ID") != null && type != null && type.equals("View")){ %>
            $(function() {
                var data = "pharmacyId=<%=request.getParameter("ID")%>";
                $.get("<%=request.getContextPath()%>/oscarRx/managePharmacy.do?method=getPharmacyInfo",
                    data, function( data ) {
                        if(data.name) {
                            $('#pharmacyId').val(<%=request.getParameter("ID")%>);
                            $('#pharmacyName').html(data.name);
                            $('#pharmacyAddress').html(data.address);
                            $('#pharmacyCity').html(data.city);
                            $('#pharmacyProvince').html(data.province);
                            $('#pharmacyPostalCode').html(data.postalCode);
                            $('#pharmacyPhone1').html(data.phone1);
                            $('#pharmacyPhone2').html(data.phone2);
                            $('#pharmacyFax').html(data.fax);
                            $('#pharmacyEmail').html(data.email);
                            $('#pharmacyServiceLocationId').html(data.serviceLocationIdentifier);
                            $('#pharmacyNotes').html(data.notes);
                        }
                        else {
                            alert("Unable to retrieve pharmacy information");
                        }
                    },"json");
            });
            <% } %>
        </script>
        <title><bean:message key="ManagePharmacy.title" /></title>
<script src="<%=request.getContextPath()%>/csrfguard" type="text/javascript"></script>
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
        <% oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean"); %>
        <link rel="stylesheet" type="text/css" href="styles.css">
    </head>
    <body topmargin="0" leftmargin="0" vlink="#0000FF">

    <table border="0" cellpadding="0" cellspacing="0"
           style="border-collapse: collapse" bordercolor="#111111" width="100%"
           id="AutoNumber1" height="100%">
        <tr>
            <td width="100%" height="100%"
                valign="top" colspan="2">
                <table cellpadding="0" cellspacing="2"
                       style="border-collapse: collapse" bordercolor="#111111" width="100%"
                       height="100%">
                    <!----Start new rows here-->
                    <tr>
                        <td>
                            <div class="DivContentSectionHead" style="height:8px; text-indent: 10px">
                                View Pharmacy Information
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td><form id="pharmacyForm">
                            <table style="margin-left: auto; margin-right:auto; padding-top: 10px; font-size: 12px;">
                                <tr>
                                    <td>
                                        <% %>
                                        <input type="hidden" id="pharmacyId" name="pharmacyId"/>
                                        <input type="hidden" id="demographicNo" name="demographicNo" value="<%=bean.getDemographicNo()%>"/>
                                        <bean:message key="ManagePharmacy.txtfld.label.pharmacyName" /> :</td>
                                    <td><label type="text" readonly="true" id="pharmacyName" name="pharmacyName" /></td>
                                </tr>
                                <tr>
                                    <td><bean:message key="ManagePharmacy.txtfld.label.address" />
                                        :</td>
                                    <td><label type="text" readonly="true" id="pharmacyAddress" name="pharmacyAddress" /></td>
                                </tr>
                                <tr>
                                    <td><bean:message key="ManagePharmacy.txtfld.label.city" />
                                        :</td>
                                    <td><label type="text" readonly="true" id="pharmacyCity" name="pharmacyCity" /></td>
                                </tr>
                                <tr>
                                    <td><bean:message key="ManagePharmacy.txtfld.label.province" />
                                        :</td>
                                    <td><label type="text" readonly="true" id="pharmacyProvince" name="pharmacyProvince" /></td>
                                </tr>
                                <tr>
                                    <td><bean:message
                                            key="ManagePharmacy.txtfld.label.postalCode" /> :</td>
                                    <td><label type="text" readonly="true" id="pharmacyPostalCode" name="pharmacyPostalCode" /></td>
                                </tr>
                                <tr>
                                    <td><bean:message key="ManagePharmacy.txtfld.label.phone1" />
                                        :</td>
                                    <td><label type="text" readonly="true" id="pharmacyPhone1" name="pharmacyPhone1" /></td>
                                </tr>
                                <tr>
                                    <td><bean:message key="ManagePharmacy.txtfld.label.phone2" />
                                        :</td>
                                    <td><label type="text" readonly="true" id="pharmacyPhone2" name="pharmacyPhone2" /></td>
                                </tr>
                                <tr>
                                    <td><bean:message key="ManagePharmacy.txtfld.label.fax" /> :
                                    </td>
                                    <td><label type="text" readonly="true" id="pharmacyFax" name="pharmacyFax" /></td>
                                </tr>
                                <tr>
                                    <td><bean:message key="ManagePharmacy.txtfld.label.email" />
                                        :</td>
                                    <td><label type="text" readonly="true" id="pharmacyEmail" name="pharmacyEmail" /></td>
                                </tr>
                                <tr>
                                    <td><bean:message key="ManagePharmacy.txtfld.label.serviceLocationIdentifier" /> :
                                    </td>
                                    <td><label type="text" readonly="true" id="pharmacyServiceLocationId" name="pharmacyServiceLocationId" /></td>
                                </tr>

                                <tr>
                                    <td><bean:message
                                            key="ManagePharmacy.txtfld.label.notes" /> :</td>
                                    <td><p id="pharmacyNotes" readonly="true" name="pharmacyNotes" rows="4"></p></td>
                                </tr>
                            </table>
                            <form></td>
                    </tr>
                    <!----End new rows here-->
                    <tr height="100%">
                        <td></td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>

    </body>

</html:html>
