<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>

        <title>Schedule to resubmit hsfo xml</title>
    </head>
    <link rel="stylesheet" type="text/css" media="all"
          href="<%= request.getContextPath() %>/share/calendar/calendar.css" title="win2k-cold-1"/>
    <!-- main calendar program -->
    <script type="text/javascript" src="<%= request.getContextPath() %>/share/calendar/calendar.js"></script>
    <!-- language for the calendar -->
    <script type="text/javascript"
            src="<%= request.getContextPath() %>/share/calendar/lang/calendar-en.js"></script>
    <!-- the following script defines the Calendar.setup helper function, which makes
    adding a calendar a matter of 1 or 2 lines of code. -->
    <script type="text/javascript" src="<%= request.getContextPath() %>/share/calendar/calendar-setup.js"></script>
    <script type="text/javascript">
        function setCopy() {
            document.inputForm.isCheck.value = document.inputForm.copyCheck.checked;

        }

        function confirmSch() {

            return confirm("Are you sure you want to schedule above time to resubmit the HSFO XML?");
        }
    </script>
    <body>
    <c:if test="${requestScope.schedule_message!=null}">
        <script type="text/javascript">
            window.close();
        </script>
    </c:if>
    <c:if test="${requestScope.schedule_message==null}">
	<span style="color: red"><c:out
            value="${requestScope.schedule_err}"/></span>
        <h3>Schedule to resubmit hsfo xml</h3>
        <form action="<%=request.getContextPath()%>/admin/RecommitHSFO2.do" method="post">
            <input type="hidden" name="method" value="saveSchedule"/>
            <input type="hidden" name="isCheck" value="${isCheck}"/>
            <input type="hidden" name="schedule_flag" value="${schedule_flag}"/>
            <input type="hidden" name="schedule_id" value="${schedule_id}"/>
            <input type="hidden" name="lastlog_flag" value="${lastlog_flag}"/>
            <input type="hidden" name="lastlog" value="${lastlog}"/>
            <input type="hidden" name="check_flag" value="${check_flag}"/>
            <table>
                <tr>
                    <td colspan="2">
                        <c:choose>
                            <c:when test="${schedule_flag}">
                                Current schedule time (date hour:minute):
                            </c:when>
                            <c:otherwise>
                                New schedule time (date hour:minute):
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="text" id="schedule_date" name="schedule_date" onfocus="this.blur()" value="${schedule_date}" />
                        <img src="${pageContext.request.contextPath}/images/cal.gif" id="schedule_vdate_cal">
                        <select name="schedule_shour">
                            <c:forEach begin="0" end="23" var="i">
                                <option value="${i}" ${i eq schedule_shour ? 'selected' : ''}>${i lt 10 ? '0' : ''}${i}</option>
                            </c:forEach>
                        </select> :
                        <select name="schedule_min">
                            <option value="0" ${schedule_min eq '0' ? 'selected' : ''}>00</option>
                            <option value="15" ${schedule_min eq '15' ? 'selected' : ''}>15</option>
                            <option value="30" ${schedule_min eq '30' ? 'selected' : ''}>30</option>
                            <option value="45" ${schedule_min eq '45' ? 'selected' : ''}>45</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        Synchronize HSFO demographic info:
                        <input type="checkbox" name="copyCheck" onclick="setCopy();" ${check_flag ? 'checked' : ''}/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <center>
                            <button type="submit" onclick="return confirmSch();">save schedule</button>
                        </center>
                    </td>
                </tr>
            </table>
            <hr>
            <c:choose>
                <c:when test="${lastlog_flag == true}">
                    Last resubmit log at ${lastlog_time}
                <br>
        
                    <c:if test="${not empty lastlog}">
                        ${lastlog}
                    </c:if>
                    <c:if test="${empty lastlog}">
                        Last resubmit didn't run.
                    </c:if>
                </c:when>
                <c:when test="${lastlog_flag == false}">
                    <tr>
                        <td colspan="2">No log in the system yet.</td>
                    </tr>
                </c:when>
            </c:choose>


            <script type="text/javascript">
                Calendar.setup({
                    inputField: "schedule_date",
                    ifFormat: "%Y-%m-%d",
                    showsTime: false,
                    button: "schedule_vdate_cal",
                    singleClick: true,
                    step: 1
                });
            </script>

        </form>
    </c:if>
    </body>

</html>
