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

    Migrated from Apache CXF to ScribeJava OAuth1 implementation.
--%>

<%@ page import="com.github.scribejava.core.model.OAuth1RequestToken" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="oscar.login.OAuthSessionMerger" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@ page import="oscar.login.OAuthData" %>
<%@ page import="oscar.login.OOBAuthorizationResponse" %>

<%
    // Determine if user already logged in to OSCAR
    boolean loggedIn = false;
    if (session.getAttribute("user") != null) {
        loggedIn = OAuthSessionMerger.mergeSession(request);
    }

    // Retrieve OAuth1 flow data set by your controller
    OAuthData oauthData = (OAuthData) request.getAttribute("oauthData");
    OOBAuthorizationResponse oauthOobResponse =
        (OOBAuthorizationResponse) request.getAttribute("oobauthorizationresponse");

    LoggedInInfo loggedInInfo =
        LoggedInInfo.getLoggedInInfoFromSession(request);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login and Authorize 3rd Party Application</title>
    <link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/css/bootstrap-responsive.css" rel="stylesheet">
    <style type="text/css">
        body {
            padding-top: 40px;
            padding-bottom: 40px;
            background-color: #f5f5f5;
        }
        .form-signin {
            max-width: 300px;
            padding: 19px 29px 29px;
            margin: 0 auto 20px;
            background-color: #fff;
            border: 1px solid #e5e5e5;
            border-radius: 5px;
            box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
        }
        .form-signin .form-signin-heading,
        .form-signin .checkbox {
            margin-bottom: 10px;
        }
        .form-signin input[type="text"],
        .form-signin input[type="password"] {
            font-size: 16px;
            height: auto;
            margin-bottom: 15px;
            padding: 7px 9px;
        }
    </style>
    <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
    <script>
        function deny() {
            $("#oauthDecision").val('deny');
            $("#scopeForm").submit();
        }
        function submitCredentials() {
            $.post('<%=request.getContextPath()%>/login.do;jsessionid=<%=session.getId()%>',
                {
                    username: $('#username').val(),
                    password: $('#password').val(),
                    pin:      $('#pin').val(),
                    ajaxResponse: 'true',
                    invalidate_session: 'false',
                    oauth_token: '<%= oauthData != null ? oauthData.getOauthToken() : "" %>'
                },
                function(data) {
                    if (data.success) {
                        $('#login_div').hide();
                        $('#scope_div').show();
                        $('#loggedin_div').show();
                        $('#providerName').text(data.providerName);
                    } else {
                        $('#login_error').show().find('span span').text(data.error);
                    }
                }, 'json');
        }
        $(document).ready(function() {
            if (loggedIn) {
                $('#login_div').hide();
                $('#scope_div').show();
                $('#loggedin_div').show();
                $('#providerName').text(
                    loggedInInfo.getLoggedInProvider().getFormattedName()
                );
            }
            if (oauthOobResponse != null) {
                $('#login_div').hide();
            }
        });
    </script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="span5">
            <div style="margin-top:25px;">
                <img src="<%=request.getContextPath()%>/images/OSCAR-LOGO.gif"
                     width="450" height="274" alt="OSCAR Logo">
                <p>
                    <font size="-1" face="Verdana, Arial, Helvetica, sans-serif">
                        OSCAR McMaster<br>
                        For more info, visit
                        <a href="http://www.oscarcanada.org">www.oscarcanada.org</a><br>
                    </font>
                </p>
            </div>
        </div>
        <div class="span7">

            <!-- Username / Password Form -->
            <div id="login_div" class="form-container">
                <form class="form-signin">
                    <h2 class="form-signin-heading">Please sign in</h2>
                    <input class="input-block-level" placeholder="Username"
                           type="text" id="username">
                    <input class="input-block-level" placeholder="Password"
                           type="password" id="password">
                    <input class="input-block-level" placeholder="Pin"
                           type="password" id="pin">

                    <div id="login_error" class="help-block" style="display:none;">
                        <span class="text-error">
                            <strong>Login Failed: </strong><span></span>
                        </span>
                    </div>

                    <button class="btn btn-large btn-primary"
                            type="button"
                            onclick="submitCredentials()">
                        Sign in
                    </button>
                </form>
            </div>

            <!-- Out‐of‐band Verifier Display -->
            <div id="oob_div">
                <% if (oauthOobResponse != null) { %>
                    <h5>
                        Request token <%= oauthOobResponse.getRequestToken() %>
                        has verifier <%= oauthOobResponse.getVerifier() %>
                    </h5>
                <% } %>
            </div>

            <!-- OAuth Scope Approval -->
            <div id="loggedin_div">
                <% if (oauthData != null) { %>
                    <form class="form-signin">
                        <h2 class="form-signin-heading">Welcome</h2>
                        <h4><span id="providerName"></span></h4>
                    </form>
                    <h5>
                        The 3rd party application
                        "<%= oauthData.getApplicationName() %>"
                        is requesting access to your OSCAR account.<br>
                        URL: <%= oauthData.getApplicationURI() %>.
                    </h5>
                    <h5>Permissions requested:</h5>
                    <form id="scopeForm" method="post"
                          action="<%= oauthData.getReplyTo() %>;jsessionid=<%=session.getId()%>">
                        <% for (String perm : oauthData.getPermissions()) { %>
                            <div class="control group">
                                <div class="controls">
                                    <label class="checkbox">
                                        <input type="checkbox" checked readonly>
                                        <%= perm %>
                                        <% if (perm.trim().isEmpty()) { %>
                                            <em>(no description)</em>
                                        <% } %>
                                    </label>
                                </div>
                            </div>
                        <% } %>
                        <input type="hidden" name="session_authenticity_token"
                               value="<%= oauthData.getAuthenticityToken() %>"/>
                        <input type="hidden" name="oauth_token" id="oauth_token"
                               value="<%= oauthData.getOauthToken() %>"/>
                        <input type="hidden" name="oauthDecision" id="oauthDecision"
                               value="allow"/>
                        <button type="submit" class="btn btn-primary">
                            Authorize <%= oauthData.getApplicationName() %>
                        </button>
                        <button type="button" class="btn btn-danger"
                                onclick="deny();">
                            Cancel
                        </button>
                    </form>
                <% } %>
            </div>

        </div>
    </div>
</div>
</body>
</html>