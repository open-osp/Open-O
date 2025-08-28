<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>

<c:set var="roleName" value="${sessionScope.userrole},${sessionScope.user}" />

<security:oscarSec roleName="${roleName}" objectName="_admin.lab" rights="w" reverse="true">
    <c:redirect url="../securityError.jsp">
        <c:param name="type" value="_admin.lab" />
    </c:redirect>
</security:oscarSec>
<security:oscarSec roleName="${roleName}" objectName="_admin.hrm" rights="w" reverse="true">
    <c:redirect url="../securityError.jsp">
        <c:param name="type" value="_admin.hrm" />
    </c:redirect>
</security:oscarSec>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MRP Auto-Linking Configuration</title>

    <link href="${pageContext.servletContext.contextPath}/library/bootstrap/5.0.2/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <script src="${pageContext.servletContext.contextPath}/library/jquery/jquery-3.6.4.min.js"></script>
    <script src="${pageContext.servletContext.contextPath}/library/bootstrap/5.0.2/js/bootstrap.bundle.js"></script>

    <script>
        jQuery(document).ready(function () {
            function setStatusMessage(message, type) {
                const statusEl = jQuery('#statusMessage');
                statusEl.removeClass('text-success text-danger text-warning text-secondary')
                        .addClass(type)
                        .text(message);
            }

            ShowSpin(true);
            jQuery.ajax({
                url: '${pageContext.request.contextPath}/autoLinkToMrp.do?method=viewAutoLinkToMrpPropertyStatus',
                type: 'POST',
                success: function (response) {
                    const status = response.status;
                    jQuery('#mrpAutoLinkSwitch').prop('checked', status);
                    setStatusMessage('Current setting: Auto-linking to MRP is ' + (status ? 'ON' : 'OFF') + '.', status ? 'text-success' : 'text-secondary');
                },
                error: function () {
                    setStatusMessage('Error: Unable to load current MRP auto-link setting.', 'text-danger');
                },
                complete: function () {
                    HideSpin();
                }
            });

            jQuery('#mrpAutoLinkSwitch').on('change', function () {
                const isChecked = jQuery(this).is(':checked');
                setStatusMessage('Updating setting to "' + (isChecked ? 'ON' : 'OFF') + '"...', 'text-warning');
                ShowSpin(true);

                jQuery.ajax({
                    url: '${pageContext.request.contextPath}/autoLinkToMrp.do?method=updateAutoLinkToMrpProperty',
                    type: 'POST',
                    data: { status: isChecked },
                    success: function (response) {
                        const status = response.status;
                        jQuery('#mrpAutoLinkSwitch').prop('checked', status);
                        setStatusMessage('Setting updated: Auto-linking to MRP is now ' + (status ? 'ON' : 'OFF') + '.', status ? 'text-success' : 'text-secondary');
                    },
                    error: function () {
                        setStatusMessage('Error: Failed to update MRP auto-link setting.', 'text-danger');
                        jQuery('#mrpAutoLinkSwitch').prop('checked', !isChecked);
                    },
                    complete: function () {
                        HideSpin();
                    }
                });
            });
        });
    </script>
</head>
<body>
<jsp:include page="../images/spinner.jsp" flush="true"/>

<div class="container mt-5">
    <div class="card shadow-sm rounded">
        <div class="card-body">
            <h3 class="card-title mb-4">Global MRP Auto-Linking Configuration</h3>

            <p class="text-muted">
                This setting controls whether HL7 and HRM documents are automatically linked to a patient's Most Responsible Provider (MRP)
                when they are uploaded. This applies globally to the entire instance and is not configurable per provider.
            </p>

            <div class="form-check form-switch my-4">
                <input class="form-check-input" type="checkbox" id="mrpAutoLinkSwitch">
                <label class="form-check-label fw-bold" for="mrpAutoLinkSwitch">
                    Enable automatic linking of HL7/HRM to MRP
                </label>
            </div>

            <div id="statusMessage" class="mt-3 fw-semibold"></div>
        </div>
    </div>
</div>
</body>
</html>
