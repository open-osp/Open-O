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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="oscar.*" %>
<%@ page import="oscar.log.*" %>
<%@ page import="org.springframework.util.StringUtils" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.PMmodule.model.Program" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProgramDao" %>
<%@ page import="org.oscarehr.PMmodule.model.ProgramProvider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProgramProviderDAO" %>
<%@ page import="org.oscarehr.common.model.SecRole" %>
<%@ page import="org.oscarehr.common.dao.SecRoleDao" %>
<%@ page import="com.quatro.model.security.Secuserrole" %>
<%@ page import="com.quatro.dao.security.SecuserroleDao" %>
<%@ page import="org.oscarehr.common.model.RecycleBin" %>
<%@ page import="org.oscarehr.common.dao.RecycleBinDao" %>
<%@ page import="org.oscarehr.common.dao.ProviderDataDao" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.oscarehr.PMmodule.service.ProgramProviderService" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    ProgramDao programDao = SpringUtils.getBean(ProgramDao.class);
    SecRoleDao secRoleDao = SpringUtils.getBean(SecRoleDao.class);
    ProviderDataDao providerDao = SpringUtils.getBean(ProviderDataDao.class);

    SecuserroleDao secUserRoleDao = (SecuserroleDao) SpringUtils.getBean(SecuserroleDao.class);
    RecycleBinDao recycleBinDao = SpringUtils.getBean(RecycleBinDao.class);
    ProgramProviderDAO programProviderDao = (ProgramProviderDAO) SpringUtils.getBean(ProgramProviderDAO.class);

    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(application);
    ProgramProviderService programProviderService = (ProgramProviderService) ctx.getBean("programProviderService");


    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    String curUser_no = (String) session.getAttribute("user");

    boolean isSiteAccessPrivacy = false;
    boolean authed = true;
%>

<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.userAdmin");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>


<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r"
                   reverse="false"><%isSiteAccessPrivacy = true; %></security:oscarSec>

<%
    //check to see if new case management is request
    ArrayList<String> users = (ArrayList<String>) session.getServletContext().getAttribute("CaseMgmtUsers");
    boolean newCaseManagement = false;

    if (!org.oscarehr.common.IsPropertiesOn.isCaisiEnable()) {
        //This should only temporarily apply to oscar, not caisi.
        //You cannot assign provider to one program "OSCAR" here if you have caisi enabled.
        //If there is no program called "OSCAR", it will only assign empty program to the provider which is not acceptable.
        if ((users != null && users.size() > 0) || OscarProperties.getInstance().getProperty("CASEMANAGEMENT", "").equalsIgnoreCase("all"))
            newCaseManagement = true;
    }

    String ip = request.getRemoteAddr();
    String msg = "";
    String caisiProgram = null;

//get caisi programid for oscar
    if (newCaseManagement) {
        Program p = programDao.getProgramByName("OSCAR");
        if (p != null) {
            caisiProgram = String.valueOf(p.getId());
        }
    }

// get role from database
    Vector vecRoleName = new Vector();
    String sql;
    String adminRoleName = "";

    String omit = "";
    if (isSiteAccessPrivacy) {
        omit = OscarProperties.getInstance().getProperty("multioffice.admin.role.name", "");
    }

    List<SecRole> secRoles = secRoleDao.findAllOrderByRole();
    for (SecRole secRole : secRoles) {
        if (!secRole.getName().equals(omit)) {
            vecRoleName.add(secRole.getName());
        }
    }
    java.util.ResourceBundle oscarRec = ResourceBundle.getBundle("oscarResources", request.getLocale());
//set the primary role
    if (request.getParameter("buttonSetPrimaryRole") != null && request.getParameter("buttonSetPrimaryRole").length() > 0) {
        String providerNo = request.getParameter("primaryRoleProvider");
        String roleName = request.getParameter("primaryRoleRole");
        SecRole secRole = secRoleDao.findByName(roleName);
        Long roleId = secRole.getId().longValue();
        ProgramProvider pp = programProviderDao.getProgramProvider(providerNo, Long.valueOf(caisiProgram));
        if (pp != null) {
            pp.setRoleId(roleId);
            programProviderDao.saveProgramProvider(pp);
        } else {
            pp = new ProgramProvider();
            pp.setProgramId(Long.valueOf(caisiProgram));
            pp.setProviderNo(providerNo);
            pp.setRoleId(roleId);
            programProviderDao.saveProgramProvider(pp);
        }
    }


// update the role
    if (request.getParameter("buttonUpdate") != null && request.getParameter("buttonUpdate").length() > 0) {
        String number = request.getParameter("providerId");
        String roleId = request.getParameter("roleId");
        String roleOld = request.getParameter("roleOld");
        String roleNew = request.getParameter("roleNew");
        String encodedRoleNew = Encode.forHtmlContent(roleNew);

        if (!"-".equals(roleNew)) {
            Secuserrole secUserRole = secUserRoleDao.findById(Integer.parseInt(roleId));
            if (secUserRole != null) {
                secUserRole.setRoleName(roleNew);
                secUserRoleDao.updateRoleName(Integer.parseInt(roleId), roleNew);
                msg = "Role " + encodedRoleNew + " is updated. (" + number + ")";

                RecycleBin recycleBin = new RecycleBin();
                recycleBin.setProviderNo(curUser_no);
                recycleBin.setUpdateDateTime(new java.util.Date());
                recycleBin.setTableName("secUserRole");
                recycleBin.setKeyword(number + "|" + roleOld);
                recycleBin.setTableContent("<provider_no>" + number + "</provider_no>" + "<role_name>" + roleOld + "</role_name>" + "<role_id>" + roleId + "</role_id>");
                recycleBinDao.persist(recycleBin);

                LogAction.addLog(curUser_no, LogConst.UPDATE, LogConst.CON_ROLE, number + "|" + roleOld + ">" + roleNew, ip);

                if (newCaseManagement) {
                    ProgramProvider programProvider = programProviderDao.getProgramProvider(number, Long.valueOf(caisiProgram));
                    if (programProvider == null) {
                        programProvider = new ProgramProvider();
                    }

                    programProvider.setProgramId(Long.valueOf(caisiProgram));
                    programProvider.setProviderNo(number);
                    programProvider.setRoleId(Long.valueOf(secRoleDao.findByName(roleNew).getId()));
                    programProviderDao.saveProgramProvider(programProvider);
                }

            } else {
                msg = "Role " + encodedRoleNew + " is <span style='text-color: red;'>NOT</span> updated!!! (" + number + ")";
            }
        }

    }

// add the role
    String add = oscarRec.getString("global.btnAdd");
    if (request.getParameter("submit") != null && request.getParameter("submit").equals(add)) {
        String number = request.getParameter("providerId");
        String roleNew = request.getParameter("roleNew");
        String encodedRoleNew = Encode.forHtmlContent(roleNew);
        if (!"-".equals(roleNew)) {
            Secuserrole secUserRole = new Secuserrole();
            secUserRole.setProviderNo(number);
            secUserRole.setRoleName(roleNew);
            secUserRole.setActiveyn(1);
            secUserRoleDao.save(secUserRole);
            msg = "Role " + encodedRoleNew + " is added. (" + number + ")";
            LogAction.addLog(curUser_no, LogConst.ADD, LogConst.CON_ROLE, number + "|" + roleNew, ip);
            if (newCaseManagement) {
                ProgramProvider programProvider = programProviderDao.getProgramProvider(number, Long.valueOf(caisiProgram));
                if (programProvider == null) {
                    programProvider = new ProgramProvider();
                }
                programProvider.setProgramId(Long.valueOf(caisiProgram));
                programProvider.setProviderNo(number);
                programProvider.setRoleId(Long.valueOf(secRoleDao.findByName(roleNew).getId()));
                programProviderDao.saveProgramProvider(programProvider);
            }
        } else {
            msg = "Role " + encodedRoleNew + " is <span style='text-color: red;'>NOT</span> added!!! (" + number + ")";
        }

    }

// delete the role
    String delete = oscarRec.getString("global.btnDelete");
    if (request.getParameter("submit") != null && request.getParameter("submit").equals(delete)) {
        String number = request.getParameter("providerId");
        String roleId = request.getParameter("roleId");
        String roleOld = request.getParameter("roleOld");
        String roleNew = request.getParameter("roleNew");
        String encodedRoleOld = Encode.forHtmlContent(roleOld);

        Secuserrole secUserRole = secUserRoleDao.findById(Integer.parseInt(roleId));
        if (secUserRole != null) {
            secUserRoleDao.deleteById(secUserRole.getId());
            msg = "Role " + encodedRoleOld + " is deleted. (" + number + ")";

            RecycleBin recycleBin = new RecycleBin();
            recycleBin.setProviderNo(curUser_no);
            recycleBin.setUpdateDateTime(new java.util.Date());
            recycleBin.setTableName("secUserRole");
            recycleBin.setKeyword(number + "|" + roleOld);
            recycleBin.setTableContent("<provider_no>" + number + "</provider_no>" + "<role_name>" + roleOld + "</role_name>");
            recycleBinDao.persist(recycleBin);

            LogAction.addLog(curUser_no, LogConst.DELETE, LogConst.CON_ROLE, number + "|" + roleOld, ip);

            if (newCaseManagement) {
                ProgramProvider programProvider = programProviderDao.getProgramProvider(number, Long.valueOf(caisiProgram));
                if (programProvider != null) {
                    programProviderService.deleteProgramProvider(programProvider.getId());
                }
            }
        } else {
            msg = "Role " + encodedRoleOld + " is <span style='text-color: red;'>NOT</span> deleted!!! (" + number + ")";
        }

    }

    String keyword = request.getParameter("keyword") != null ? request.getParameter("keyword") : "";


%>
<%
    String lastName = "";
    String firstName = "";
    String[] temp = keyword.split("\\,");
    if (temp.length > 1) {
        lastName = temp[0] + "%";
        firstName = temp[1] + "%";
    } else {
        lastName = keyword + "%";
        firstName = "%";
    }

    List<Object[]> providerList = null;
    providerList = providerDao.findProviderSecUserRoles(lastName, firstName);

    Vector<Properties> vec = new Vector<Properties>();
    for (Object[] providerSecUser : providerList) {

        String id = String.valueOf(providerSecUser[0]);
        String role_name = String.valueOf(providerSecUser[1]);
        String provider_no = String.valueOf(providerSecUser[2]);
        String first_name = String.valueOf(providerSecUser[3]);
        String last_name = String.valueOf(providerSecUser[4]);

        Properties prop = new Properties();
        prop.setProperty("provider_no", provider_no == "null" ? "" : provider_no);
        prop.setProperty("first_name", first_name);
        prop.setProperty("last_name", last_name);
        prop.setProperty("role_id", id != "null" ? id : "");
        prop.setProperty("role_name", role_name != "null" ? role_name : "");
        vec.add(prop);
    }

    List<Boolean> primaries = new ArrayList<Boolean>();

//when caisi is off, we need to show which role is the one in the program_provider table for each provider.
    if (newCaseManagement) {
        for (Properties prop : vec) {
            boolean res = false;
            String providerNo = prop.getProperty("provider_no");
            String secUserRoleId = prop.getProperty("role_id");
            String roleName = prop.getProperty("role_name");
            if (!roleName.equals("")) {
                SecRole secRole = secRoleDao.findByName(roleName);
                if (secRole != null) {
                    ProgramProvider pp = programProviderDao.getProgramProvider(providerNo, Long.valueOf(caisiProgram), secRole.getId().longValue());
                    res = (pp != null);
                }
            } else {
                res = false;
            }
            primaries.add(res);
        }
    }


%>
<html>
<head>

    <link href="${ pageContext.request.contextPath }/css/bootstrap.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/css/font-awesome.min.css">

    <script src="${ pageContext.request.contextPath }/library/jquery/jquery-3.6.4.min.js"></script>

    <script src="${ pageContext.request.contextPath }/js/jqBootstrapValidation-1.3.7.min.js"></script>
    <title><fmt:setBundle basename="oscarResources"/><fmt:message key="global.update"/> <fmt:setBundle basename="oscarResources"/><fmt:message key="admin.admin.provider"/> <fmt:setBundle basename="oscarResources"/><fmt:message key="role"/></title>


    <script>

        function setfocus() {
            this.focus();
            document.forms[0].keyword.select();
        }

        function submit(form) {
            form.submit();
        }

    </script>

    <script>
        var items = new Array();
        <%
                for(Properties prop:vec) {
                        %>
        item = {
            providerNo: "<%=prop.get("provider_no")%>",
            role_id: "<%=prop.get("role_id")%>",
            roleName: "<%=Encode.forHtmlAttribute((String)prop.get("role_name"))%>"
        };
        items.push(item);
        <%
}
%>
    </script>
    <script>
        $(document).ready(function () {
            $("#primaryRoleProvider").val("");
        });

        function primaryRoleChooseProvider() {
            $("#primaryRoleRole").find('option').remove();
            var provider = $("#primaryRoleProvider").val();
            for (var i = 0; i < items.length; i++) {
                if (items[i].providerNo == provider && items[i].role_id != "") {
                    $("#primaryRoleRole").append('<option value="' + items[i].roleName + '">' + items[i].roleName + '</option>');
                }
            }
        }

        function setPrimaryRole() {
            var providerNo = $("#primaryRoleProvider").val();
            var roleName = $("#primaryRoleRole").val();
            if (providerNo != '' && roleName != '') {
                return true;
            } else {
                alert('Please enter in a provider and a corresponding role');
                return false;
            }
        }
    </script>

</head>
<body onLoad="setfocus()">

<div id="header" class="navbar">
    <div class="navbar-inner">
        <div class="brand"><i class="icon-lock"></i>&nbsp;<fmt:setBundle basename="oscarResources"/><fmt:message key="global.update"/>&nbsp;<fmt:setBundle basename="oscarResources"/><fmt:message key="admin.admin.provider"/>&nbsp;<fmt:setBundle basename="oscarResources"/><fmt:message key="role"/></div>
    </div>
</div>


<form name="myform" action="providerRole.jsp" method="POST">

    <% if (msg.length() > 1) {%>
    <div class="alert alert-info">
        <%=msg%>
    </div>
    <% } %>
    <div class="well">

        <div class="controls">
            <div class="input-append">
                <input type="text" placeholder="<fmt:setBundle basename="oscarResources"/><fmt:message key="admin.securityrecord.formUserName"/>" name="keyword"
                       value="<%=Encode.forHtmlAttribute(keyword)%>"/>
                <input type="submit" class="btn btn-primary" name="search" value="<fmt:setBundle basename="oscarResources"/><fmt:message key="global.search"/>">
            </div>
        </div>

    </div>
</form>

<table id="provTable" class="table table-striped table-hover table-condensed">
    <thead>
    <tr>
        <th><fmt:setBundle basename="oscarResources"/><fmt:message key="admin.admin.provider"/></th>
        <th><fmt:setBundle basename="oscarResources"/><fmt:message key="admin.provider.formFirstName"/></th>
        <th><fmt:setBundle basename="oscarResources"/><fmt:message key="admin.provider.formLastName"/></th>
        <% if (newCaseManagement) { %>
        <th>
            <fmt:setBundle basename="oscarResources"/><fmt:message key="role"/>
        </th>
        <th>
            <fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographiceditdemographic.primaryEMR"/> <fmt:setBundle basename="oscarResources"/><fmt:message key="role"/>
        </th>
        <% } else {%>
        <th>
            <fmt:setBundle basename="oscarResources"/><fmt:message key="role"/>
        </th>
        <%} %>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <%

        for (int i = 0; i < vec.size(); i++) {
            Properties item = vec.get(i);
            String providerNo = item.getProperty("provider_no", "");
    %>
    <form name="myform" action="providerRole.jsp" method="POST">
        <tr>
            <td><%= providerNo %>
            </td>
            <td><%= Encode.forHtmlContent(item.getProperty("first_name", "")) %>
            </td>
            <td><%= Encode.forHtmlContent(item.getProperty("last_name", "")) %>
            </td>
            <td>
                <select name="roleNew">
                    <option value="-">-</option>
                    <%
                        for (int j = 0; j < vecRoleName.size(); j++) {
                    %>
                    <option value="<%=Encode.forHtmlAttribute(String.valueOf(vecRoleName.get(j)))%>" <%= vecRoleName.get(j).equals(item.getProperty("role_name", "")) ? "selected" : "" %>>
                        <%= Encode.forHtmlContent(String.valueOf(vecRoleName.get(j))) %>
                    </option>
                    <%
                        }
                    %>
                </select>
            </td>
            <% if (newCaseManagement) { %>
            <td>
                <%=(primaries.get(i) != null && (primaries.get(i)).booleanValue() == true) ? oscarRec.getString("global.yes") : "" %>
            </td>
            <% } %>

            <td>
                <input type="hidden" name="keyword" value="<%=Encode.forHtmlAttribute(keyword)%>"/>
                <input type="hidden" name="providerId" value="<%=providerNo%>">
                <input type="hidden" name="roleId" value="<%= item.getProperty("role_id", "")%>">
                <input type="hidden" name="roleOld"
                       value="<%= Encode.forHtmlAttribute(item.getProperty("role_name", ""))%>">
                <div class="button-group">
                    <input type="submit" name="submit" class="btn btn-primary"
                           value="<fmt:setBundle basename="oscarResources"/><fmt:message key="global.btnAdd"/>">
                    <input type="submit" name="buttonUpdate" class="btn btn-info"
                           value="<fmt:setBundle basename="oscarResources"/><fmt:message key="global.update"/>" <%= StringUtils.hasText(item.getProperty("role_id"))?"":"disabled"%>>
                    <input type="submit" name="submit" class="btn-link" style="color:red;"
                           value="<fmt:setBundle basename="oscarResources"/><fmt:message key="global.btnDelete"/>" <%= StringUtils.hasText(item.getProperty("role_id"))?"":"disabled"%>>
                </div>
            </td>
        </tr>
    </form>
    <%
        }
    %>
    </tbody>
</table>
<hr>

<% if (newCaseManagement) { %>
<div class="well">
    <form name="myform" action="providerRole.jsp" method="POST">
        <table>
            <tr>
                <td><fmt:setBundle basename="oscarResources"/><fmt:message key="global.update"/>&nbsp;<fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographiceditdemographic.primaryEMR"/>&nbsp;<fmt:setBundle basename="oscarResources"/><fmt:message key="role"/></td>
            </tr>
            <tr>
                <td>
                    <label class="control-label" for="primaryRoleProvider"><fmt:setBundle basename="oscarResources"/><fmt:message key="admin.admin.provider"/>:</label>
                    <select id="primaryRoleProvider" name="primaryRoleProvider" onChange="primaryRoleChooseProvider()">
                        <option value="">Select Below</option>
                        <%
                            List<String> temp1 = new ArrayList<String>();
                            for (Properties prop : vec) {
                                String providerNo = prop.getProperty("provider_no");
                                if (!temp1.contains(providerNo)) {
                        %>
                        <option value="<%=providerNo%>"><%=Encode.forHtmlContent(prop.getProperty("last_name") + "," + prop.getProperty("first_name")) %>
                        </option>
                        <%
                                    temp1.add(providerNo);
                                }
                            }
                        %>
                    </select>
                </td>
            </tr>

            <tr>
                <td>
                    <label class="control-label" for="primaryRoleRole"><fmt:setBundle basename="oscarResources"/><fmt:message key="role"/>:</label>
                    <select id="primaryRoleRole" name="primaryRoleRole">
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="buttonSetPrimaryRole"
                           value="<fmt:setBundle basename="oscarResources"/><fmt:message key="global.update"/>&nbsp;<fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographiceditdemographic.primaryEMR"/>&nbsp;<fmt:setBundle basename="oscarResources"/><fmt:message key="role"/>"
                           class="btn btn-primary" onClick="return setPrimaryRole();">
                </td>
            </tr>
        </table>
    </form>
</div>
<% } %>


</body>
</html>