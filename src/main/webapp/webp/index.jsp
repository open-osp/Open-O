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
<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    // Force the page to un-cache itself so user cannot go back after logout
// The 3 lines ensure that all browsers are covered
// They are necessary for URL not showing this file name (index.jsp)
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

%>

<!DOCTYPE html>
<!-- ng* attributes are references into AngularJS framework -->
<html lang="en" ng-app="oscarProviderViewModule">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/images/Oscar.ico">
    <fmt:setBundle basename="uiResources" var="uiBundle"/>
    
    <title><fmt:message key="global.title" bundle="${uiBundle}"/></title>

    <link href="<%= request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/font-awesome.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/loading-bar.css" rel="stylesheet">


    <!-- we'll combine/minify later -->
    <link href="css/navbar-fixed-top.css" rel="stylesheet">
    <link href="css/navbar-demo-search.css" rel="stylesheet">
    <link href="css/patient-list.css" rel="stylesheet">

    <link href="<%= request.getContextPath() %>/library/ng-table/ng-table.css" rel="stylesheet">

    <link href="<%= request.getContextPath() %>/library/bootstrap-datepicker/css/datepicker3.css" rel="stylesheet">

    <link href="<%= request.getContextPath() %>/css/bootstrap-timepicker.min.css" rel="stylesheet">

    <link href="<%= request.getContextPath() %>/library/bootstrap/3.0.0/assets/css/bootstrap3_badge_colours.css" rel="stylesheet">


    <style>
        /*styles temporary, when design complete styles will be moved to css above*/
        .hand-hover {
            cursor: pointer;
            cursor: hand;
        }

        .hand-hover:hover {
            text-decoration: none;
        }

        #parent {
            position: fixed;
            bottom: 0px;
            width: 100%;
        / / width should be 100 %
        }

        #child {
            width: 100px;
        / / min width should give to center the div . margin: 0 px auto;
        / / here it will make center
        }


        #noteInput, #noteInput2 {
            position: fixed;
            bottom: 0;
            font-family: sans-serif;
            margin-bottom: 0px;
            font-weight: bold;
            bottom: 0;
            z-index: 999;
        }

        #noteInput {
            line-height: 2;
            font-size: 30px;
            /*text-shadow: 0 1px 0 #84BAFF;
            //box-shadow: 0 0 15px #00214B
            //background: #ccccFF;
            //text-align: center;
            //color: #042E64;
            */
        }

        .absolute-left {
            left: 0px;
        }

        .absolute-right {
            right: 0px;
        }

        #noteInput ul.dropdown-menu {
            max-height: 220px;
            overflow-y: auto;
            overflow-x: none;
        }


        pre.noteInEdit {
            border-color: rgba(126, 239, 104, 0.8);
            box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px rgba(126, 239, 104, 0.6);
            outline: 0 none;
        }

        img.navbarlogo {
            height: 40px;
            padding-top: 10px;
        }

        .navbar-form .input-group-btn,
        .navbar-form .input-group-addon {
            width: auto;
        }

        /*needed to fix the broken nav dropdown*/
        #left_pane {
            padding: 8px;
        }

        .add-summary:hover {
            color: #333 !important;
            text-decoration: none;
        }

        li.cpp-note-list {
            background: #fff0;
            border: none;
            border-bottom: 1px solid #ddd;
            text-align: left;
            padding-top: 4px;
            padding-bottom: 10px;
        }

        li.cpp-note-list {
            cursor: pointer;
        }

        li.cpp-note-list:last-child {
            border-bottom: none;
        }

        .well-note {
            border-color: #d2d2d2;
            box-shadow: 0 1px 0 #cfcfcf;
            border-radius: 3px;

            /*not sure if this will stay*/
            max-height: 300px;
            overflow-y: auto;
        }

        .nav .navbar-nav li.dropdown > a .caret {
            border-top-color: red !important; /*#555555;*/
            border-bottom-color: red !important; /*#555555;*/

        }

        .more {
            border-top-color: #727272 !important;
            border-bottom-color: #727272 !important;
        }

        .more:hover {
            border-top-color: #333 !important;
            border-bottom-color: #333 !important;
        }

        .highlight, .highlight:hover {
            color: #FF0000;
        }

        .mild, .mild {
            color: #FFFF33;
        }

        .moderate, .moderate {
            color: #FF6600;
        }

        .severe, .severe {
            color: #CC0000;
        }

        .refused, .refused:hover {
            color: #EAACAC;
        }

        .ineligible, .ineligible:hover {
            color: #FFCC24;
        }

        .deletedItem {
            text-decoration: line-through;
        }

        .greyedOut {
            background-color: grey;
        }

        .favourite {
            background-color: fuchsia;
        }

        .pending, .pending:hover {
            color: #FF00FF;
        }

        /*think of changing this to be consistent*/
        .abnormal-prev, .abnormal-prev:hover {
            color: #FF4D4D;
        }

        .abnormal, .abnormal:hover {
            color: red;
        }

        .glyphicon-chevron-down-disabled {
            color: #999999 !important;
        }

        /*for patient header*/
        @media (max-width: 1320px) {
            .patientHeaderName {
                font-size: 26px
            }

            .patientHeaderExt {
                font-size: 20px
            }

        }

        @media print {
            .noprint {
                display: none;
            }

            .patientHeaderName {
                font-size: 16px;
            }

            .patientHeaderExt {
                font-size: 16px;
            }
        }
    </style>

</head>

<body ng-controller="OscarCtrl">

<!-- Start patient List template -->


<!-- End patient List template -->
<div class="container-fluid" style="padding:5px;">
    <div id="right_pane" ng-class="col-md-12" ui-view ng-cloak></div>
</div>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>

<script src="<%= request.getContextPath() %>/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<!-- script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script -->

<script src="<%= request.getContextPath() %>/library/hogan-2.0.0.js"></script>
<script src="<%= request.getContextPath() %>/library/typeahead.js/typeahead.min.js"></script>
<script src="<%= request.getContextPath() %>/library/angular.min.js"></script>
<script src="<%= request.getContextPath() %>/library/angular-sanitize.min.js"></script>
<!-- script src="<%= request.getContextPath() %>/library/angular-route.min.js"></script  -->
<script src="<%= request.getContextPath() %>/library/angular-ui-router.js"></script>
<script src="<%= request.getContextPath() %>/library/angular-resource.min.js"></script>

<script src="<%= request.getContextPath() %>/library/ui-bootstrap-tpls-2.5.0.js"></script>

<script src="<%= request.getContextPath() %>/library/ng-infinite-scroll.min.js"></script>

<script src="<%= request.getContextPath() %>/library/ng-table/ng-table.js"></script>
<script src="<%= request.getContextPath() %>/js/loading-bar.js"></script>


<!-- we'll combine/minify later -->
<script src="<%= request.getContextPath() %>/web/common/demographicServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/programServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/scheduleServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/securityServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/staticDataServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/billingServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/ticklerServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/formServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/noteServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/providerServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/patientDetailStatusServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/uxServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/messageServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/inboxServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/k2aServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/personaServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/consultServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/appServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/diseaseRegistryServices.js"></script>
<script src="<%= request.getContextPath() %>/web/common/rxServices.js"></script>
<script src="<%= request.getContextPath() %>/web/filters.js"></script>

<script src="<%= request.getContextPath() %>/web/record/rx/rx.component.js"></script>
<script src="<%= request.getContextPath() %>/web/record/rx/rxModel.js"></script>
<script src="<%= request.getContextPath() %>/web/record/rx/search/medsearch.component.js"></script>
<script src="<%= request.getContextPath() %>/web/record/rx/profile/profile.component.js"></script>
<script src="<%= request.getContextPath() %>/web/record/rx/print/print.component.js"></script>
<script src="<%= request.getContextPath() %>/web/record/rx/dsview/dsview.component.js"></script>
<script src="<%= request.getContextPath() %>/web/record/rx/discontinue/discontinue.component.js"></script>
<script src="<%= request.getContextPath() %>/web/record/rx/history/history.component.js"></script>
<script src="<%= request.getContextPath() %>/web/record/rx/reprint/reprint.component.js"></script>
<script src="<%= request.getContextPath() %>/web/record/rx/fullsearch/fullsearch.component.js"></script>
<script src="<%= request.getContextPath() %>/web/common/components/provider/provider.component.js"></script>
<script src="app.js.jsp"></script>

<script src="<%= request.getContextPath() %>/web/oscarController.js"></script>
<script src="<%= request.getContextPath() %>/webp/recordController.js"></script>
<%--

<script src="<%= request.getContextPath() %>/web/dashboard/dashboardController.js"></script>
<script src="<%= request.getContextPath() %>/web/common/navBarController.js"></script>
<script src="<%= request.getContextPath() %>/web/patientlist/patientListController.js"></script>

<script src="<%= request.getContextPath() %>/web/record/summary/summaryController.js"></script>
<script src="<%= request.getContextPath() %>/web/record/forms/formsController.js"></script>
<script src="<%= request.getContextPath() %>/web/record/details/detailsController.js"></script>
<script src="<%= request.getContextPath() %>/web/record/phr/phrController.js"></script>
<script src="<%= request.getContextPath() %>/web/record/tracker/trackerController.js"></script>

<script src="<%= request.getContextPath() %>/web/tickler/ticklerController.js"></script>
<script src="<%= request.getContextPath() %>/web/tickler/ticklerViewController.js"></script>
<script src="<%= request.getContextPath() %>/web/tickler/ticklerAddController.js"></script>

<script src="<%= request.getContextPath() %>/web/schedule/scheduleController.js"></script>
<script src="<%= request.getContextPath() %>/web/admin/adminController.js"></script>
<script src="<%= request.getContextPath() %>/web/billing/billingController.js"></script>
<script src="<%= request.getContextPath() %>/web/consults/consultRequestListController.js"></script>
<script src="<%= request.getContextPath() %>/web/consults/consultRequestController.js"></script>
<script src="<%= request.getContextPath() %>/web/consults/consultResponseListController.js"></script>
<script src="<%= request.getContextPath() %>/web/consults/consultResponseController.js"></script>
<script src="<%= request.getContextPath() %>/web/inbox/inboxController.js"></script>
<script src="<%= request.getContextPath() %>/web/patientsearch/patientSearchController.js"></script>

<script src="<%= request.getContextPath() %>/web/report/reportsController.js"></script>
<script src="<%= request.getContextPath() %>/web/document/documentsController.js"></script>
<script src="<%= request.getContextPath() %>/web/settings/settingsController.js"></script>
<script src="<%= request.getContextPath() %>/web/help/supportController.js"></script>
<script src="<%= request.getContextPath() %>/web/help/helpController.js"></script>
<script src="<%= request.getContextPath() %>/web/clinicalconnect/ccController.js"></script>

<script src="<%= request.getContextPath() %>/web/schedule/appointmentAddController.js"></script>
<script src="<%= request.getContextPath() %>/web/schedule/appointmentViewController.js"></script>
--%>

<!--

<script src="js/providerViewController.js"></script>
<script src="js/messengerController.js"></script  -->

<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/bootstrap-timepicker.min.js"></script>

<script>


</script>
</body>
</html>
