/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */


package oscar.oscarEncounter.pageUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.common.dao.OscarLogDao;
import org.oscarehr.hospitalReportManager.HRMReport;
import org.oscarehr.hospitalReportManager.HRMReportParser;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentSubClassDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.dao.HRMSubClassDao;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentSubClass;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.hospitalReportManager.model.HRMSubClass;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.util.DateUtils;
import oscar.util.StringUtils;

public class EctDisplayHRMAction extends EctDisplayAction {

	private static Logger logger = MiscUtils.getLogger();
	private static final String cmd = "HRM";
	private HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");
	private HRMDocumentDao hrmDocumentDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");
	private HRMDocumentSubClassDao hrmDocumentSubClassDao = (HRMDocumentSubClassDao) SpringUtils.getBean("HRMDocumentSubClassDao");
	private HRMSubClassDao hrmSubClassDao = (HRMSubClassDao) SpringUtils.getBean("HRMSubClassDao");
	private OscarLogDao oscarLogDao = (OscarLogDao) SpringUtils.getBean("oscarLogDao");
	
	public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
    	if(!securityInfoManager.hasPrivilege(loggedInInfo, "_hrm", "r", null)) {
			return true; //Prevention link won't show up on new CME screen.
		} else {

			String winName = "docs" + bean.demographicNo;
			String url = "popupPage(500,1115,'" + winName + "', '" + request.getContextPath() + "/hospitalReportManager/displayHRMDocList.jsp?demographic_no=" + bean.demographicNo + "')";
			Dao.setLeftURL(url);
			Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.Index.msgHRMDocuments"));

			Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action

			StringBuilder javascript = new StringBuilder("<script type=\"text/javascript\">");
			String js = "";
			List<HRMDocumentToDemographic> hrmDocListDemographic = hrmDocumentToDemographicDao.findByDemographicNo(bean.demographicNo);
			String dbFormat = "yyyy-MM-dd";
			String serviceDateStr = "";
			String key;
			String title;
			int hash;
			String BGCOLOUR = request.getParameter("hC");
			Date date;

			List<HRMDocument> allHrmDocsForDemo = new LinkedList<HRMDocument>();
			for (HRMDocumentToDemographic hrmDemoDocResult : hrmDocListDemographic) {
				List<HRMDocument> hrmDoc = hrmDocumentDao.findById(hrmDemoDocResult.getHrmDocumentId());
				allHrmDocsForDemo.addAll(hrmDoc);
			}

			for (HRMDocument hrmDocument : allHrmDocsForDemo) {
				// filter duplicate reports
				HRMReport hrmReport = HRMReportParser.parseReport(loggedInInfo, hrmDocument.getReportFile());
				if (hrmReport == null) continue;
				hrmReport.setHrmDocumentId(hrmDocument.getId());

				List<HRMDocumentSubClass> hrmDocumentSubClassList = hrmDocumentSubClassDao.getSubClassesByDocumentId(hrmDocument.getId());
				String reportStatus = hrmDocument.getReportStatus();
				String dispFilename = hrmDocument.getReportType();
				String dispSubClass ="";
				HRMSubClass subClass;
				String dispDocNo = hrmDocument.getId().toString();
				String description = hrmDocument.getDescription();

				if (hrmReport.getFirstReportClass().equalsIgnoreCase("Diagnostic Imaging Report") || hrmReport.getFirstReportClass().equalsIgnoreCase("Cardio Respiratory Report")) {
					//Get first sub class to display on eChart
					if (hrmDocumentSubClassList != null && hrmDocumentSubClassList.size()>0) {
						HRMDocumentSubClass firstSubClass = hrmDocumentSubClassList.get(0);
						subClass = hrmSubClassDao.findApplicableSubClassMapping(hrmReport.getFirstReportClass(), firstSubClass.getSubClass(), firstSubClass.getSubClassMnemonic(), hrmReport.getSendingFacilityId());
						dispSubClass = subClass!=null?subClass.getSubClassDescription():"";
					}

					if ((StringUtils.isNullOrEmpty(dispSubClass)) && hrmReport.getAccompanyingSubclassList().size()>0){
						// if sub class doesn't exist, display the accompanying subclass
						dispSubClass = hrmReport.getFirstAccompanyingSubClass();
					}
				} else {
					//Medical Records Report
					String[] reportSubClass = hrmReport.getFirstReportSubClass().split("\\^");
					dispSubClass = reportSubClass!=null&&reportSubClass.length>1?reportSubClass[1]:"";
				}

				// Determine text to display on eChart
				String t = "";
				if(!StringUtils.isNullOrEmpty(description)){
					t = description; //custom label
				}
				else if(!StringUtils.isNullOrEmpty(dispSubClass)){
					t = dispSubClass; // subclass
				}
				else {
					t = dispFilename; // report class
				}

				if (reportStatus != null && reportStatus.equalsIgnoreCase("C")) {
					t = "(Cancelled) " + t;
				}

				title = StringUtils.maxLenString(t, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
				
				DateFormat formatter = new SimpleDateFormat(dbFormat);
				String dateStr = hrmDocument.getTimeReceived().toString();
				NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
				try {
					date = formatter.parse(dateStr);
					serviceDateStr =  DateUtils.formatDate(date,request.getLocale()); 
				}
				catch(ParseException ex ) {
					MiscUtils.getLogger().debug("EctDisplayHRMAction: Error creating date " + ex.getMessage());
					serviceDateStr = "Error";
					date = null;
				}

				String user = (String) request.getSession().getAttribute("user");
				item.setDate(date);
				hash = Math.abs(winName.hashCode());
				
				url = "popupPage(700,800,'" + hash + "', '" + request.getContextPath() + "/hospitalReportManager/Display.do?id="+dispDocNo+"&segmentID="+dispDocNo+"');";

				String labRead = "";
				if(!oscarLogDao.hasRead(( (String) request.getSession().getAttribute("user")   ),"hrm",dispDocNo)){
                	labRead = "*";	
                }

				
				item.setLinkTitle(title + serviceDateStr);
				item.setTitle(labRead+title+labRead);
				key = StringUtils.maxLenString(dispFilename, MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + "(" + serviceDateStr + ")";
				key = StringEscapeUtils.escapeJavaScript(key);


				js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompleted['" + key + "'] = \"" + url + "\"; autoCompList.push('" + key + "');";
				javascript.append(js);
				url += "return false;";
				item.setURL(url);
				Dao.addItem(item);

			}
			javascript.append("</script>");

			Dao.setJavaScript(javascript.toString());
			Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);
			
			return true;
		}
	}

	@Override
	public String getCmd() {
		return cmd;
	}

}
