<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>


<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, ca.openosp.*, java.net.*,ca.openosp.MyDateFormat" %>

<%@ page import="ca.openosp.openo.utility.SpringUtils" %>
<%@ page import="ca.openosp.openo.commn.model.CtlBillingService" %>
<%@ page import="ca.openosp.openo.commn.dao.CtlBillingServiceDao" %>
<%
    CtlBillingServiceDao ctlBillingServiceDao = SpringUtils.getBean(CtlBillingServiceDao.class);
%>
<%
    String typeid = request.getParameter("typeid");
    String type = request.getParameter("type");

    for (CtlBillingService b : ctlBillingServiceDao.findByServiceType(typeid)) {
        ctlBillingServiceDao.remove(b.getId());
    }

    String[] group = new String[4];
    int rowsAffected = -100;

    for (int j = 1; j < 4; j++) {
        group[j] = request.getParameter("group" + j);

        for (int i = 0; i < 20; i++) {
            String field = "group" + j + "_service" + i;
            String serviceCode = request.getParameter(field);
            serviceCode = (serviceCode == null) ? "" : serviceCode.trim();

            if (!serviceCode.isEmpty()) {
                // Any invalid order (blank, non-numeric, negative, or out of int range) falls back to the row position so it can't crash the page.
                String orderParam = request.getParameter(field + "_order");
                orderParam = (orderParam == null) ? "" : orderParam.trim();
                int serviceOrder;
                try {
                    serviceOrder = Integer.parseInt(orderParam);
                    if (serviceOrder < 0) {
                        serviceOrder = i;
                    }
                } catch (NumberFormatException e) {
                    serviceOrder = i;
                }

                CtlBillingService cbs = new CtlBillingService();
                cbs.setServiceTypeName(type);
                cbs.setServiceType(typeid);
                cbs.setServiceCode(serviceCode);
                cbs.setServiceGroupName(group[j]);
                cbs.setServiceGroup("Group" + j);
                cbs.setStatus("A");
                cbs.setServiceOrder(serviceOrder);
                ctlBillingServiceDao.persist(cbs);
            }
        }
    }

    response.sendRedirect("manageBillingform.jsp");
%>
