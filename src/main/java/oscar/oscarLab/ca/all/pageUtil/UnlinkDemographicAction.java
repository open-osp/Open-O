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
package oscar.oscarLab.ca.all.pageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.apache.logging.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.ProviderLabRoutingDao;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.ProviderLabRoutingModel;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.OscarAuditLogger;
import org.oscarehr.util.SpringUtils;

import oscar.form.JSONAction;
import oscar.log.LogConst;
import java.util.List;

/**
 *
 * @author mweston4
 */
public class UnlinkDemographicAction  extends JSONAction {
    
    private static Logger logger = MiscUtils.getLogger();
    private final SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    private final PatientLabRoutingDao plrDao = SpringUtils.getBean(PatientLabRoutingDao.class);

    private final ProviderLabRoutingDao providerLabRoutingDao = SpringUtils.getBean(ProviderLabRoutingDao.class);
    public UnlinkDemographicAction () {}
    
    @Override
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
    	
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    	if(!securityInfoManager.hasPrivilege(loggedInInfo, "_lab", "u", null)) {
			throw new SecurityException("missing required security object (_lab)");
		}
        boolean success = false;
		//set the demographicNo in the patientLabRouting table 
        String reason = request.getParameter("reason");
        String labNoStr = request.getParameter("labNo");
        Integer labNo = Integer.parseInt(labNoStr);
        PatientLabRouting plr = plrDao.findByLabNo(labNo);
        Integer demoNo = 0;

        if(plr != null) {
            demoNo = plr.getDemographicNo();
            plr.setDemographicNo(PatientLabRoutingDao.UNMATCHED);
            plrDao.merge(plr);
            if(plr.getDemographicNo().equals(PatientLabRoutingDao.UNMATCHED)) {
                success = true;
            }
        }

        OscarAuditLogger.getInstance().log(loggedInInfo, LogConst.UNLINK, LogConst.CON_HL7_LAB, String.valueOf(labNo), request.getRemoteAddr(), demoNo, reason);
        
        logger.debug("Unlinked lab with segmentID: " + labNo + " from eChart of Demographic " + demoNo);

        /* ensure the lab requisitioning provider is aware by inserting lab back into their inbox.
         * or into the unattached inbox if no provider is identified.
         */
        List<ProviderLabRoutingModel> providerLabRoutingModel = providerLabRoutingDao.findAllLabRoutingByIdandType(labNo, ProviderLabRoutingDao.LAB_TYPE.HL7.name());
        for(ProviderLabRoutingModel providerLabRouting : providerLabRoutingModel) {
            String currentStatus = providerLabRouting.getStatus();
            providerLabRouting.setStatus(ProviderLabRoutingDao.STATUS.N.name());
            providerLabRouting.setComment(providerLabRouting.getComment() + " " + " Lab unlinked from incorrect demographic number: " + demoNo);
            providerLabRoutingDao.merge(providerLabRouting);
            OscarAuditLogger.getInstance().log(loggedInInfo, LogConst.UNLINK, LogConst.CON_HL7_LAB, String.valueOf(labNo), request.getRemoteAddr(), demoNo,"Provider " + providerLabRouting.getProviderNo() + " Ack status from " + currentStatus + " to " + ProviderLabRoutingDao.STATUS.N.name());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success",success);
        jsonObject.put("unlinkedDemographicNo", demoNo);
        jsonObject.put("labNo", labNo);
        jsonObject.put("reason", reason);

        jsonResponse(response, jsonObject);

        return null;
    }
    
    
}
