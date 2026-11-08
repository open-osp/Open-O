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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>

<c:set var="roleName" value="${sessionScope.userrole},${sessionScope.user}" />

<security:oscarSec roleName="${roleName}" objectName="_lab" rights="w" reverse="true">
    <c:redirect url="../securityError.jsp">
        <c:param name="type" value="_lab" />
    </c:redirect>
</security:oscarSec>

<!DOCTYPE html>
<html>
<head>
    <title>Manage Comment Box When Acknowledging Labs</title>

    <link href="${pageContext.servletContext.contextPath}/library/bootstrap/5.0.2/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <script src="${pageContext.servletContext.contextPath}/library/jquery/jquery-3.6.4.min.js"></script>
    <script src="${pageContext.servletContext.contextPath}/library/bootstrap/5.0.2/js/bootstrap.bundle.js"></script>
</head>
<body>
<jsp:include page="../images/spinner.jsp" flush="true"/>

<div class="container py-5">
    <div class="card shadow-sm">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0">Manage Comment Box When Acknowledging Labs</h5>
        </div>
        <div class="card-body">

            <div class="form-check form-switch mb-3">
                <input class="form-check-input" type="checkbox" id="disableComment"
                       <c:if test="${disableComment}">checked</c:if>>
                <label class="form-check-label" for="disableComment">
                    Do you want to disable the comment box before acknowledging labs?
                </label>
                <div class="form-text text-muted">(default: no)</div>
            </div>

            <div class="form-check form-switch mb-3">
                <input class="form-check-input" type="checkbox" id="offerFileForOthers"
                       <c:if test="${offerFileForOthers}">checked</c:if>>
                <label class="form-check-label" for="offerFileForOthers">
                    Automatically expand "File Document on Behalf of Others" in comment box
                </label>
                <div class="form-text text-muted">(default: no)</div>
            </div>

            <div class="form-check form-switch mb-3">
                <input class="form-check-input" type="checkbox" id="allowOthersFileForYou"
                       <c:if test="${allowOthersFileForYou}">checked</c:if>>
                <label class="form-check-label" for="allowOthersFileForYou">
                    Allow other providers to file results on your behalf when they acknowledge HL7 lab results
                </label>
                <div class="form-text text-muted">(default: no)</div>
            </div>

            <div id="successMessage" class="alert alert-success d-none" role="alert">
                Preferences updated successfully.
            </div>

        </div>
    </div>
</div>

<script>
    function updatePreference(methodName, key, value) {
        ShowSpin(true);
        jQuery.ajax({
            url: '${pageContext.request.contextPath}/setProviderStaleDate.do?method=' + methodName,
            method: 'POST',
            data: {
                key: key,
                value: value
            },
            success: function (response) {
                const status = response.status;
                jQuery('#' + key).prop('checked', status);
                const msg = jQuery('#successMessage');
                msg.removeClass('d-none');
                setTimeout(() => msg.addClass('d-none'), 3000);
            },
            error: function (xhr, status, error) {
                alert("Error updating preference: " + error);
                jQuery('#' + key).prop('checked', !value);
            },
            complete: function () {
                HideSpin();
            }
        });
    }

    jQuery(function () {
        jQuery('#disableComment').on('change', function () {
            updatePreference('setDisableAckCommentPref', 'disableComment', this.checked);
        });

        jQuery('#offerFileForOthers').on('change', function () {
            updatePreference('setOfferFileForOthersPref', 'offerFileForOthers', this.checked);
        });

        jQuery('#allowOthersFileForYou').on('change', function () {
            updatePreference('setAllowOthersFileForYouPref', 'allowOthersFileForYou', this.checked);
        });
    });
</script>

</body>
</html>
