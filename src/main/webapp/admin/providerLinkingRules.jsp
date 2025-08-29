<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>

<c:set var="roleName" value="${sessionScope.userrole},${sessionScope.user}" />

<security:oscarSec roleName="${roleName}" objectName="_admin" rights="w" reverse="true">
    <c:redirect url="../securityError.jsp">
        <c:param name="type" value="_admin" />
    </c:redirect>
</security:oscarSec>
<security:oscarSec roleName="${roleName}" objectName="_lab" rights="w" reverse="true">
    <c:redirect url="../securityError.jsp">
        <c:param name="type" value="_lab" />
    </c:redirect>
</security:oscarSec>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Provider Linking Rules</title>

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
                url: '${pageContext.request.contextPath}/providerLinkingRules.do?method=viewProviderLinkingRulesPropertyStatus',
                type: 'POST',
                success: function (response) {
                    const status = response.status;
                    jQuery('#providerLinkingSwitch').prop('checked', status);
                    setStatusMessage('Current Setting: Automatic forwarding is ' + (status ? 'ON' : 'OFF') + '.', status ? 'text-success' : 'text-secondary');
                },
                error: function () {
                    setStatusMessage('Error: Unable to load current Provider Linking Rules.', 'text-danger');
                },
                complete: function () {
                    HideSpin();
                }
            });

            jQuery('#providerLinkingSwitch').on('change', function () {
                const isChecked = jQuery(this).is(':checked');
                setStatusMessage('Updating setting to "' + (isChecked ? 'ON' : 'OFF') + '"...', 'text-warning');
                ShowSpin(true);

                jQuery.ajax({
                    url: '${pageContext.request.contextPath}/providerLinkingRules.do?method=updateProviderLinkingRulesProperty',
                    type: 'POST',
                    data: { status: isChecked },
                    success: function (response) {
                        const status = response.status;
                        jQuery('#providerLinkingSwitch').prop('checked', status);
                        setStatusMessage('Current Setting: Automatic forwarding is ' + (status ? 'ON' : 'OFF') + '.', status ? 'text-success' : 'text-secondary');
                    },
                    error: function () {
                        setStatusMessage('Error: Failed to update Provider Linking Rules.', 'text-danger');
                        jQuery('#providerLinkingSwitch').prop('checked', !isChecked);
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
        <div class="card shadow-sm rounded-3">
            <div class="card-body">
                <h3 class="card-title mb-4">Provider Linking Configuration</h3>

                <p class="text-muted">
                    By default, when an HL7 electronic lab result is received for a particular demographic, only the
                    providers explicitly noted in the HL7 electronic lab result will be forwarded the result and will
                    see the lab result in their inbox.
                </p>
                <p class="text-muted">
                    Enable this feature to ensure a demographic’s provider will also be forwarded the result and will
                    see the lab result in their inbox, even in situations where the provider is not explicitly noted.
                </p>
                <p class="text-muted">
                    This setting will also affect HRM documents (an Ontario-specific document delivery feature) in the
                    same way.
                </p>
                <p class="text-muted mb-4">
                    This setting is not configurable per provider.
                </p>

                <div class="form-check form-switch mb-4">
                    <input class="form-check-input" type="checkbox" id="providerLinkingSwitch">
                    <label class="form-check-label fw-bold" for="providerLinkingSwitch">
                    Automatically forward HL7 and HRM documents to a demographic’s provider
                    </label>
                </div>

                <div id="statusMessage" class="fw-semibold text-primary"></div>
            </div>
        </div>
    </div>
</body>
</html>
