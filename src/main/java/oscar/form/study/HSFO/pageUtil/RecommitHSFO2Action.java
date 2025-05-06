//CHECKSTYLE:OFF
/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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


package oscar.form.study.HSFO.pageUtil;

import com.opensymphony.xwork2.ActionSupport;
import noNamespace.HsfoHbpsDataDocument;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.caisi.dao.ProviderDAO;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.SecurityDao;
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
import oscar.form.study.HSFO.RecommitDAO;
import oscar.form.study.HSFO.RecommitSchedule;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecommitHSFO2Action extends ActionSupport {
    private HttpServletRequest request = ServletActionContext.getRequest();
    protected static Logger logger = org.oscarehr.util.MiscUtils.getLogger();

    public String execute() throws Exception {
        if ("saveSchedule".equals(request.getParameter("method"))) {
            return saveSchedule();
        }
        return showSchedule();
    }

    public String showSchedule() {
        RecommitDAO rd = new RecommitDAO();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

        // Handle last log schedule
        RecommitSchedule rsd = rd.getLastSchedule(true);
        if (rsd != null) {
            lastLogFlag = true;
            lastLogTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rsd.getSchedule_time());
            lastLog = rsd.getMemo() != null ? rsd.getMemo() : "";
        }

        // Handle current schedule
        if (rd.isLastActivExpire()) {
            rd.deActiveLast();
        }

        RecommitSchedule rs = rd.getLastSchedule(false);
        if (rs != null && !"D".equalsIgnoreCase(rs.getStatus())) {
            scheduleFlag = true;
            Date scheduleTime = rs.getSchedule_time();
            scheduleDate = dateFormat.format(scheduleTime);
            scheduleHour = hourFormat.format(scheduleTime);
            scheduleMinute = minuteFormat.format(scheduleTime);
            checkFlag = rs.isCheck_flag();
            scheduleId = rs.getId().toString();
        } else {
            scheduleFlag = false;
            scheduleDate = "";
            scheduleHour = "03";
            scheduleMinute = "30";
            checkFlag = true;
            scheduleId = "0";
        }

        return "schedulePage";
    }

    public String saveSchedule() throws Exception {
        RecommitDAO rd = new RecommitDAO();
        RecommitSchedule rs = new RecommitSchedule();

        String user = (String) request.getSession().getAttribute("user");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date scheduleTime = sf.parse(scheduleDate + " " + scheduleHour + ":" + scheduleMinute);

        if (scheduleTime.before(new Date())) {
            addActionError("Invalid time, please schedule another time!");
            return "schedulePage";
        }

        rs.setStatus("A");
        rs.setUser_no(user);
        rs.setCheck_flag(isCheck == null || !"false".equalsIgnoreCase(isCheck));
        rs.setSchedule_time(scheduleTime);

        Integer id = Integer.parseInt(scheduleId);
        if (id != 0) {
            rs.setId(id);
            rd.updateLastSchedule(rs);
        } else {
            rd.insertchedule(rs);
        }

        addActionMessage("Schedule successfully updated!");
        HsfoQuartzServlet.schedule();

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
                RecommitDAO rDao = new RecommitDAO();
                RecommitSchedule rs = rDao.getLastSchedule(false);
                ArrayList<String> message = new ArrayList<String>();
                String retS = null;
                if (!"D".equalsIgnoreCase(rs.getStatus())) {
                    rs.setStatus("D");

                    if (rs.isCheck_flag()) retS = rDao.SynchronizeDemoInfo(x);
                    else retS = rDao.checkProvider(x);

                    if (retS != null) {
                        rs.setMemo("Upload failure. Missing internal doctor for patient " + retS + ".");
                        rDao.updateLastSchedule(rs);
                        return;
                    }
                    HsfoHbpsDataDocument doc = tfutil.generateXML(x, rs.getUser_no(), 0);

                    if (doc == null) {
                        message.add("");
                        message.add("Patient(s) data not found in the database.");
                    }
                    message = tfutil.validateDoc(doc);
                    if (message.size() != 0) {
                        rs.setMemo(message.get(0));
                        rDao.updateLastSchedule(rs);
                        return;
                    }
                    String rstr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + doc.xmlText();
                    // send to hsfo web

                    try {
                        message = tfutil.soapHttpCall(tfutil.getSiteID().intValue(), tfutil.getUserId(), tfutil.getLoginPasswd(), rstr);
                    } catch (Exception e) {
                        logger.error("Error", e);
                        throw e;
                    }
                    String msg = "Code: " + message.get(0) + " " + message.get(1);
                    rs.setMemo(msg);
                    rDao.updateLastSchedule(rs);
                }
            } catch (Exception e) {
                MiscUtils.getLogger().error("Error", e);
            } finally {
                DbConnectionFilter.releaseAllThreadDbResources();
            }
        }

    }

    public String test() {

        return null;
    }

    private boolean lastLogFlag;
    private String lastLogTime;
    private String lastLog;
    private boolean scheduleFlag;
    private boolean checkFlag;
    private String scheduleDate;
    private String scheduleHour;
    private String scheduleMinute;
    private String scheduleId;
    private String isCheck;

    public boolean isLastLogFlag() {
        return lastLogFlag;
    }

    public void setLastLogFlag(boolean lastLogFlag) {
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

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }
}
