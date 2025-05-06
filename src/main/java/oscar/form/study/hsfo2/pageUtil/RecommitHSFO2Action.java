//CHECKSTYLE:OFF
/**
 * Copyright (C) 2007  Heart & Stroke Foundation
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.form.study.hsfo2.pageUtil;


import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.caisi.dao.ProviderDAO;
import org.hsfo.v2.HsfHmpDataDocument;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.Hsfo2RecommitScheduleDao;
import org.oscarehr.common.dao.SecurityDao;
import org.oscarehr.common.model.Hsfo2RecommitSchedule;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import oscar.OscarProperties;
import oscar.form.study.hsfo2.pageUtil.XMLTransferUtil.SoapElementKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RecommitHSFO2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    static Logger logger = MiscUtils.getLogger();
    private static Hsfo2RecommitScheduleDao rd = (Hsfo2RecommitScheduleDao) SpringUtils.getBean(Hsfo2RecommitScheduleDao.class);

    public String execute() throws Exception {
        if ("saveSchedule".equals(request.getParameter("method"))) {
            return saveSchedule();
        }
        return showSchedule();
    }
    
    public String showSchedule() {
        Hsfo2RecommitSchedule rsd = rd.getLastSchedule(true);
        if (rsd != null) {
            lastLogFlag = "true";
            SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            lastLogTime = sf1.format(rsd.getSchedule_time());
            lastLog = rsd.getMemo() != null ? rsd.getMemo() : "";
        }
        if (rd.isLastActivExpire()) {
            rd.deActiveLast();
        }
        Hsfo2RecommitSchedule rs = rd.getLastSchedule(false);
        if (rs != null && !"D".equalsIgnoreCase(rs.getStatus())) {
            scheduleFlag = true;
            Date dd = rs.getSchedule_time();
            SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sf2 = new SimpleDateFormat("HH");
            SimpleDateFormat sf3 = new SimpleDateFormat("mm");
            scheduleDate = sf1.format(dd);
            scheduleHour = sf2.format(dd);
            scheduleMinute = sf3.format(dd);
            checkFlag = rs.isCheck_flag();
            scheduleId = rs.getId().toString();
        }
        return "schedulePage";
    }

    public String saveSchedule() throws Exception {
        Hsfo2RecommitSchedule rs = new Hsfo2RecommitSchedule();
        String user = (String) request.getSession().getAttribute("user");

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            // 拼接日期和时间，并解析为 Date 对象
            Date stime = sf.parse(scheduleDate + " " + scheduleHour + ":" + scheduleMinute);

            if (stime.before(new Date())) {
                scheduleMessage = "Invalid time, please schedule another time!";
                return "schedulePage"; // 返回错误页面
            }

            // 设置 Hsfo2RecommitSchedule 的属性
            rs.setStatus("A");
            rs.setUser_no(user);
            rs.setCheck_flag(isCheck);
            rs.setSchedule_time(stime);

            // 更新或插入计划
            Integer id = Integer.valueOf(scheduleId);
            if (id != 0) {
                rs.setId(id);
                rd.updateLastSchedule(rs);
            } else {
                rd.insertchedule(rs);
            }

        } catch (Exception e) {
            // 捕获异常并记录日志
            logger.info(e.getMessage());
            scheduleMessage = "An error occurred while saving the schedule.";
            return "schedulePage"; // 返回错误页面
        }

        // 设置成功消息
        scheduleMessage = "Successfully updated!";
        oscar.form.study.hsfo2.pageUtil.HsfoQuartzServlet.schedule();

        return "schedulePage";
    }

    public static class ResubmitJob implements Job {

        public void execute(JobExecutionContext ctx) throws JobExecutionException {
            String providerNo = OscarProperties.getInstance().getProperty("hsfo_job_run_as_provider");
            if (providerNo == null) {
                return;
            }

            ProviderDAO providerDao = SpringUtils.getBean(ProviderDao.class);
            Provider provider = providerDao.getProvider(providerNo);

            if (provider == null) {
                return;
            }

            SecurityDao securityDao = SpringUtils.getBean(SecurityDao.class);
            List<Security> securityList = securityDao.findByProviderNo(providerNo);

            if (securityList.isEmpty()) {
                return;
            }

            LoggedInInfo x = new LoggedInInfo();
            x.setLoggedInProvider(provider);
            x.setLoggedInSecurity(securityList.get(0));


            try {
                XMLTransferUtil tfutil = new XMLTransferUtil();
                //Hsfo2RecommitScheduleDao rDao = new Hsfo2RecommitScheduleDao();

                Hsfo2RecommitSchedule rs = rd.getLastSchedule(false);
                // if no schedule, do nothing
                if (rs == null)
                    return;

                String retS = null;
                //if ( !"D".equalsIgnoreCase( rs.getStatus() ) )
                //{
                rs.setStatus("D");
                rs.setSchedule_time(new Date());

                if (rs.isCheck_flag())
                    retS = rd.SynchronizeDemoInfo(x);
                else
                    retS = rd.checkProvider(x);

                if (retS != null) {
                    rs.setMemo("Upload failure. Missing internal doctor for patient " + retS + ".");
                    rd.updateLastSchedule(rs);
                    return;
                }

                // send to hsfo web

                try {
                    StringBuilder memoMsg = new StringBuilder();
                    boolean updateSuccess = uploadXmlToServer(tfutil, rs.getUser_no(), 0, memoMsg);

                    rs.setMemo(memoMsg.toString());
                    rd.updateLastSchedule(rs);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                    throw e;
                }

                // }
            } catch (Exception e) {
                logger.info(e.getMessage());
            } finally {
                DbConnectionFilter.releaseAllThreadDbResources();
            }
        }

    }

    public static Calendar toCalendar(String sDate) {
        return toCalendar(sDate, "yyyy-MM-dd");
    }

    public static Calendar toCalendar(String sDate, String datePattern) {
        if (sDate == null || sDate.length() == 0) {
            return null;
        }

        SimpleDateFormat oFormat = new SimpleDateFormat(datePattern);

        try {
            Date oDate = oFormat.parse(sDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(oDate);
            return calendar;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean uploadXmlToServer(XMLTransferUtil tfutil, String providerNo, Integer demographicNo, StringBuilder memoMsg) throws Exception {
        //GetDataDateRange
        Map<SoapElementKey, Object> output = tfutil.soapHttpCallGetDataDateRange(tfutil.getSiteID().intValue(), tfutil.getUserId(),
                tfutil.getLoginPasswd());
        if ((Integer) output.get(SoapElementKey.responseStatusCode) != 200 || !output.get(SoapElementKey.GetDataDateRangeResult).equals("0")) {
            memoMsg.append(" GetDataDateRange status code: " + output.get(SoapElementKey.responseStatusCode)
                    + "; GetDataDateRange result code: " + output.get(SoapElementKey.GetDataDateRangeResult));
            return false;
        }

        // DataVault
        String sBeginDate = (String) output.get(SoapElementKey.DataBeginDate);
        String sEndDate = (String) output.get(SoapElementKey.DataEndDate);
        Calendar beginDate = toCalendar(sBeginDate);
        Calendar endDate = toCalendar(sEndDate);

        HsfHmpDataDocument doc = tfutil.generateDataVaultXML(providerNo, demographicNo, beginDate, endDate);

        if (doc == null) {
            memoMsg.append(" Patient(s) data not found in the database.");
            return false;
        }

        List message = tfutil.validateDoc(doc);
        if (message.size() != 0) {
            memoMsg.append(message.get(0).toString());
            return false;
        }

        String rstr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + doc.xmlText();

        output = tfutil.soapHttpCallDataVault(tfutil.getSiteID().intValue(), tfutil.getUserId(), tfutil.getLoginPasswd(), rstr);

        if ((Integer) output.get(SoapElementKey.responseStatusCode) != 200 || !"0".equals(output.get(SoapElementKey.GetDataDateRangeResult))) {
            memoMsg.append("DataVault status code: " + output.get(SoapElementKey.responseStatusCode)
                    + " DataVaultStatusStrResult: " + output.get(SoapElementKey.DataVaultStatusStrResult)
                    + " StatusMessage:" + output.get(SoapElementKey.StatusMessage));

        } else {
            memoMsg.append("DataVault status code: " + output.get(SoapElementKey.responseStatusCode)
                    + " DataVaultStatusStrResult: " + output.get(SoapElementKey.DataVaultStatusStrResult)
                    + " StatusMessage: " + output.get(SoapElementKey.StatusMessage)
                    + " DataBeginDate: " + output.get(SoapElementKey.DataBeginDate)
                    + " DataEndDate: " + output.get(SoapElementKey.DataEndDate));
        }
        return true;
    }

    public String test() {

        return null;
    }

    private boolean scheduleFlag;
    private boolean checkFlag;
    private String scheduleDate = "";
    private String scheduleHour = "03";
    private String scheduleMinute = "30";
    private String lastLogFlag = "false";
    private String lastLogTime = "";
    private String lastLog = "";
    private String scheduleId = "0";
    private String scheduleMessage;
    private boolean isCheck;

    public boolean isScheduleFlag() {
        return scheduleFlag;
    }

    public void setScheduleFlag(boolean scheduleFlag) {
        this.scheduleFlag = scheduleFlag;
    }

    public boolean isCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(boolean checkFlag) {
        this.checkFlag = checkFlag;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleHour() {
        return scheduleHour;
    }

    public void setScheduleHour(String scheduleHour) {
        this.scheduleHour = scheduleHour;
    }

    public String getScheduleMinute() {
        return scheduleMinute;
    }

    public void setScheduleMinute(String scheduleMinute) {
        this.scheduleMinute = scheduleMinute;
    }

    public String getLastLogFlag() {
        return lastLogFlag;
    }

    public void setLastLogFlag(String lastLogFlag) {
        this.lastLogFlag = lastLogFlag;
    }

    public String getLastLogTime() {
        return lastLogTime;
    }

    public void setLastLogTime(String lastLogTime) {
        this.lastLogTime = lastLogTime;
    }

    public String getLastLog() {
        return lastLog;
    }

    public void setLastLog(String lastLog) {
        this.lastLog = lastLog;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleMessage() {
        return scheduleMessage;
    }

    public void setScheduleMessage(String scheduleMessage) {
        this.scheduleMessage = scheduleMessage;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
