<%--

    Copyright (c) 2007 Peter Hutten-Czapski based on OSCAR general requirements
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
<!DOCTYPE HTML>

<%@ include file="/casemgmt/taglibs.jsp" %>
<!-- almost all taglibs -->
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="w" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.userAdmin");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<%@ page import="ca.openosp.openo.providers.data.ProviderData" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="ca.openosp.openo.utility.SpringUtils" %>
<%@ page import="ca.openosp.openo.commn.dao.ProviderLabRoutingFavoritesDao" %>
<%@ page import="ca.openosp.openo.commn.model.ProviderLabRoutingFavorite" %>
<%@ page import="ca.openosp.openo.PMmodule.dao.ProviderDao" %>
<%@ page import="ca.openosp.openo.commn.model.Provider" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<html>
<head>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
    <title><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMDS.selectProvider.title"/></title>

    <link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css">
    <!-- Bootstrap 2.3.1 -->
    <link href="${pageContext.request.contextPath}/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/font-awesome.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/library/jquery/jquery-ui.theme-1.12.1.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/library/jquery/jquery-ui.structure-1.12.1.min.css" rel="stylesheet">

    <script src="${pageContext.request.contextPath}/library/jquery/jquery-3.6.4.min.js"></script>
    <script src="${pageContext.request.contextPath}/library/jquery/jquery-ui-1.12.1.min.js"></script>

    <script>
        function preSubmit() {

            if ($('#fwdProviders').children('option').length == 0) {
                alert("Please select a providers first.")
                return false;
            }

            $('select option').prop('selected', true);
            return true;
        }

        $(document).ready(function () {
            $(document).tooltip();

            var url = "${pageContext.request.contextPath}/providers/SearchProvider.do";
            // as the standard autocomplete will do a ?term
            // we need to provide a call back that replaces it with ?query
            // as the standard autocomplete expects first level items
            // the callback will return data.results and not data to expose the nested key value pairs
            $("#keyword").autocomplete({
                source: function (request, response) {
                    $.get(url, {query: request.term}, function (data) {
                        response(data.results);
                    });
                },
                minLength: 2,
                focus: function (event, ui) {
                    $("#keyword").val(ui.item.lastName + ", " + ui.item.firstName);
                    return false;
                },
                select: function (event, ui) {
                    var option = document.createElement("option");
                    option.text = ui.item.lastName + ", " + ui.item.firstName;
                    option.value = ui.item.providerNo;
                    option.id = ui.item.providerNo;
                    $('#fwdProviders').append(option);
                    return false;
                }
            })
                .autocomplete("instance")._renderItem = function (ul, item) {
                return $("<li>")
                    .append("<div><b>" + item.lastName + ", " + item.firstName + "</b>" +
                        "<br>" + "<fmt:setBundle basename="oscarResources"/><fmt:message key="admin.provider.formProviderNo"/>" + ": " + item.providerNo +
                        "<br>" + "<fmt:setBundle basename="oscarResources"/><fmt:message key="admin.provider.formBillingNo"/>" + ": " + item.ohipNo + "</div>")
                    .appendTo(ul);
            };


        });

        function removeProvider(selectObj) {
            selectObj.remove(selectObj.selectedIndex);
        }

    </script>
</head>
<body>
<form name="providerSelectForm" method="post" action="${pageContext.request.contextPath}/study/ManageStudy.do">
    <input type="hidden" name="method" value="AddProvider"/>
    <input type="hidden" name="studyId" value="<%=request.getParameter("studyId")%>"/>
    <h3>&nbsp;&nbsp;<fmt:setBundle basename="oscarResources"/><fmt:message key="admin.admin.btnStudy"/></h3>

    <div class="well">
        <div class="row">
            <div class="span4">
                <fieldset>
                    <legend><fmt:setBundle basename="oscarResources"/><fmt:message key="billing.batchbilling.msgProviderTitle"/></legend>
                    <label><fmt:setBundle basename="oscarResources"/><fmt:message key="billing.batchbilling.msgProvider"/></label>
                    <input type="text" id="keyword" class="input-block-level"
                           placeholder="<fmt:setBundle basename="oscarResources"/><fmt:message key="billing.batchbilling.msgProvider"/>"
                           title="Type in a provider's name and select it.">
                </fieldset>
            </div class="span4">
            <div class="span6">
                <fieldset>
                    <legend><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.providers"/></legend>
                    <label><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicexport.providers"/></label>
                    <select id="fwdProviders" name="providerNo" class="input-block-level" multiple="multiple"
                            ondblclick="removeProvider(this);"></select>
                    <span class="help-block"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMDS.forward.msgInstruction2"/></span>
                    <input type="submit" class="btn btn-primary" value="<fmt:setBundle basename="oscarResources"/><fmt:message key="global.btnSubmit"/>"
                           onclick="return preSubmit();">
                </fieldset>
            </div class="span6">
        </div class="row">
    </div class="well">

</form>

</body>
</html>