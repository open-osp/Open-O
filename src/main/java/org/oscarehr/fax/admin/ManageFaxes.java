/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.fax.admin;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.FaxClientLogDao;
import org.oscarehr.common.dao.FaxConfigDao;
import org.oscarehr.common.dao.FaxJobDao;
import org.oscarehr.common.model.FaxClientLog;
import org.oscarehr.common.model.FaxConfig;
import org.oscarehr.common.model.FaxJob;
import org.oscarehr.fax.action.FaxAction;
import org.oscarehr.managers.FaxManager;
import org.oscarehr.managers.FaxManagerImpl;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ManageFaxes extends FaxAction {
	
	private final Logger log = MiscUtils.getLogger();
	private final SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	private final FaxManager faxManager = SpringUtils.getBean(FaxManager.class);

	@SuppressWarnings("unused")
	public ActionForward CancelFax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String jobId = request.getParameter("jobId");
				
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }
		
		FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);
		FaxConfigDao faxConfigDao = SpringUtils.getBean(FaxConfigDao.class);
		FaxJob faxJob = faxJobDao.find(Integer.parseInt(jobId));
		FaxConfig faxConfig = faxConfigDao.getConfigByNumber(faxJob.getFax_line());
		String result = "{success:false}";				
		
		log.info("TRYING TO CANCEL FAXJOB " + faxJob.getJobId());

		if( faxConfig == null ) {
			log.error("Could not find faxConfig while processing fax id: " + faxJob.getId() + " Has the fax number changed?");
		} else if( faxConfig.isActive() ) {
			
			if( faxJob.getStatus().equals(FaxJob.STATUS.SENT)) {
				faxJob.setStatus(FaxJob.STATUS.CANCELLED);
				faxJobDao.merge(faxJob);
				result = "{success:true}";
				
			}
			
			if( faxJob.getJobId() != null ) {	
				
				if( faxJob.getStatus().equals(FaxJob.STATUS.WAITING)) {
					try(DefaultHttpClient client = new DefaultHttpClient()){
						client.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(faxConfig.getSiteUser(), faxConfig.getPasswd()));

						HttpPut mPut = new HttpPut(faxConfig.getUrl() + "/fax/" + faxJob.getJobId());
						mPut.setHeader("accept", "application/json");
						mPut.setHeader("user", faxConfig.getFaxUser());
						mPut.setHeader("passwd", faxConfig.getFaxPasswd());

						HttpResponse httpResponse = client.execute(mPut);	                
		                
		                if( httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
		                	
		                	HttpEntity httpEntity = httpResponse.getEntity();
		                	result = EntityUtils.toString(httpEntity);
		                	
		                	faxJob.setStatus(FaxJob.STATUS.CANCELLED);
		                	faxJobDao.merge(faxJob);
		                }
		                
					}
					catch( IOException e ) {
						log.error("PROBLEM COMM WITH WEB SERVICE");
					}
				}
			}
		}

		jsonResponse(response, JSONObject.fromObject(result));

		return null;
		
	}
	
	@SuppressWarnings("unused")
	public ActionForward ResendFax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObject = JSONObject.fromObject("{success:false}");
		String JobId = request.getParameter("jobId");
		String faxNumber = request.getParameter("faxNumber");
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_admin", "w", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }

		boolean success = false;

		/*
		 *  Dont even try to resend a fax if the service is not enabled.
		 */
		if(FaxManager.isEnabled()){
			success = faxManager.resendFax(loggedInInfo, JobId, faxNumber);
		}

		jsonResponse(response, JSONObject.fromObject("{success:" + success + "}"));

		return null;
	}
	

	@SuppressWarnings("unused")
	public void viewFax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_edoc", "r", null)) {
        	throw new SecurityException("missing required security object (_edoc)");
        }

		getPreview(mapping, form, request, response);
	}
	
	@SuppressWarnings("unused")
	public ActionForward fetchFaxStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String statusStr = request.getParameter("status");
		String teamStr = request.getParameter("team");
		String dateBeginStr = request.getParameter("dateBegin");
		String dateEndStr = request.getParameter("dateEnd");
		String provider_no = request.getParameter("oscarUser");
		String demographic_no = request.getParameter("demographic_no");
		
		if( provider_no.equalsIgnoreCase("-1") ) {
			provider_no = null;
		}
		
		if( statusStr.equalsIgnoreCase("-1") ) {
			statusStr = null;
		}
		
		if( teamStr.equalsIgnoreCase("-1") ) {
			teamStr = null;
		}
		
		if( "null".equalsIgnoreCase(demographic_no) || "".equals(demographic_no) ) {
			demographic_no = null;
		}
		
		Calendar calendar = GregorianCalendar.getInstance();
		Date dateBegin=null, dateEnd = null;
		String datePattern[] = new String[] {"yyyy-MM-dd"};
		
		if(dateBeginStr != null && ! dateBeginStr.isEmpty())
		{
			try {
				dateBegin = DateUtils.parseDate(dateBeginStr, datePattern);
				calendar.setTime(dateBegin);
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				dateBegin = calendar.getTime();
			}
			catch( ParseException e ) {
				dateBegin = null;
				MiscUtils.getLogger().error("UNPARSEABLE DATE " + dateBeginStr);
			}
		}
		if(dateEndStr != null && ! dateEndStr.isEmpty())
		{
			try {
				dateEnd = DateUtils.parseDate(dateEndStr, datePattern);
				calendar.setTime(dateEnd);
				calendar.set(Calendar.HOUR, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.MILLISECOND, 59);
				dateEnd = calendar.getTime();
	
			}
			catch( ParseException e ) {
				dateEnd = null;
				MiscUtils.getLogger().error("UNPARSEABLE DATE " + dateEndStr);
			}
		}
		
		FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);
		FaxClientLogDao faxClientLogDao = SpringUtils.getBean(FaxClientLogDao.class);
		
		List<FaxJob> faxJobList = faxJobDao.getFaxStatusByDateDemographicProviderStatusTeam(demographic_no, provider_no, statusStr, teamStr, dateBegin, dateEnd);
		
		List<Integer> faxIds = new ArrayList<>();
		for (FaxJob faxJob : faxJobList) { faxIds.add(faxJob.getId()); }
		List<FaxClientLog> faxClientLogs = faxClientLogDao.findClientLogbyFaxIds(faxIds);

		request.setAttribute("faxes", faxJobList);
		request.setAttribute("faxClientLogs", faxClientLogs);
		
		return mapping.findForward("faxstatus");
	}
	
	@SuppressWarnings("unused")
	public void SetCompleted(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }

		
		String id = request.getParameter("jobId");		
		FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);		
		
		FaxJob faxJob = faxJobDao.find(Integer.parseInt(id));		
		faxJob.setStatus(FaxJob.STATUS.RESOLVED);		
		faxJobDao.merge(faxJob);
	}

}
