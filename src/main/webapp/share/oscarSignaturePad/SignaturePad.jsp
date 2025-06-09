<%--
   Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
   This software is published under the GPL GNU General Public License.
   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   as published by the Free Software Foundation; either version 2
   of the License, or (at your option) any later version.
   <p>
   This program is distributed in the hope that it will be useful,d
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
   GNU General Public License for more details.
   <p>
   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
   <p>
   This software was written for
   Centre for Research on Inner City Health, St. Michael's Hospital,
   Toronto, Ontario, Canada
--%>

<%@ page import="org.oscarehr.util.DigitalSignatureUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.ui.servlet.ImageRenderingServlet" %>
<%@ page import="org.oscarehr.common.model.enumerator.ModuleType" %>

<!DOCTYPE html>
<html lang="en">
<%
    LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
    String requestIdKey = request.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY);
    if (requestIdKey == null) {
        requestIdKey = DigitalSignatureUtils.generateSignatureRequestId(loggedInInfo.getLoggedInProviderNo());
    }
    String imageUrl = request.getContextPath() + "/imageRenderingServlet?source=" + ImageRenderingServlet.Source.signature_preview.name() + "&" + DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY + "=" + requestIdKey;
    String storedImageUrl = request.getContextPath() + "/imageRenderingServlet?source=" + ImageRenderingServlet.Source.signature_stored.name() + "&digitalSignatureId=";
    boolean saveToDB = "true".equals(request.getParameter("saveToDB"));
%>

<style>

    * {
        -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
        -webkit-text-size-adjust: none;
        -webkit-touch-callout: none;
    }

    #signatureCanvas {
        -webkit-user-select: none;
        cursor: pointer;
        width: 100%;
        height: 100%;
    }

    html, body {
        height: 100%;
        margin: 0;
        padding: 0;
    }

    .signature-container {
        height: 100%;
        display: flex;
        flex-direction: column;
    }

    .canvas-wrapper {
        flex: 1;
        min-height: 0;
        position: relative;
    }
</style>

<body>

<div class="signatureClass container-sm signature-container w-100 p-0">
    <label id="signMessage" class="d-block">Please sign in the box.</label>
    <div class="canvas-wrapper py-1">
        <canvas id='signatureCanvas' class="border rounded shadow-sm">
        </canvas>
    </div>
    <div class="mt-md-2 mt-xl-1">
        <button id="clear" class="btn btn-outline-secondary me-2" disabled>Clear</button>
        <button id="save" class="btn btn-outline-primary" disabled
                onclick="saveSignature(
                        '<%=request.getContextPath() %>',
                        '<%= requestIdKey %>',
                        '<%= imageUrl %>',
                        '<%= storedImageUrl %>',
                        );">Save
        </button>
    </div>
</div>

<form action="<%=request.getContextPath() %>/digitalSignature.do"
      id="signatureForm" method="POST">
    <input type="hidden" id="signatureImage" name="signatureImage" value=""/>
    <input type="hidden" name="method" value="uploadSignature"/>
    <input type="hidden" name="source" value="IPAD"/>
    <input type="hidden" name="<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY %>" value="<%= requestIdKey %>"/>
    <input type="hidden" name="demographicNo" value="<%= request.getParameter("demographicNo") %>"/>
    <input type="hidden" name=<%= ModuleType.class.getSimpleName()%>
            value="<%= request.getParameter(ModuleType.class.getSimpleName()) %>"/>
    <input type="hidden" name="saveToDB" value="<%=saveToDB%>"/>
</form>
</body>
</html>
