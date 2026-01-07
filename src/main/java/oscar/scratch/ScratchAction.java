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


package oscar.scratch;

import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.ScratchPadDao;
import org.oscarehr.common.model.ScratchPad;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.owasp.encoder.Encode;
import oscar.form.JSONAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 * @author jay
 */
public class ScratchAction extends JSONAction {
    
    public ActionForward showVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String id = request.getParameter("id");
    	ScratchPadDao dao = SpringUtils.getBean(ScratchPadDao.class);
    	ScratchPad scratchPad = dao.find(Integer.parseInt(id));

    	request.setAttribute("ScratchPad", scratchPad);
    	return mapping.findForward("scratchPadVersion");
    }
    
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String providerNo =  (String) request.getSession().getAttribute("user");
        String pNo = request.getParameter("providerNo");
                
        if(providerNo.equals(pNo)){
        String id = request.getParameter("id");
        String scratchPad = request.getParameter("scratchpad");
        String windowId = request.getParameter("windowId");
        String returnId;
        String returnText;
        ScratchData scratch = new ScratchData();
        Map<String, String> h = scratch.getLatest(providerNo);

        if (h == null){  //FIRST TIME USE
           returnId = scratch.insert(providerNo,scratchPad);
           returnText = scratchPad;

        }else{

           returnText = h.get("text").trim();
            //Get current Id in scratch table
           int databaseId = Integer.parseInt(h.get("id"));
           returnId = ""+databaseId;
           MiscUtils.getLogger().debug( "database Id = "+databaseId+" request id "+id);

		   if (databaseId > Integer.parseInt(id)){
			   //check to see if the id in database is higher than in the request
              MiscUtils.getLogger().debug(" Database id greater than id");

		   }else if (isTextDifferent(scratchPad, returnText)){
	           returnId = scratch.insert(providerNo,scratchPad);   //save new record and return new id.
               returnText = scratchPad;
               MiscUtils.getLogger().debug("dirty field set");
           }
        }
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", Encode.forHtmlContent(returnId));
			jsonObject.put("text", Encode.forHtmlContent(returnText));
			jsonObject.put("windowId", Encode.forHtmlContent(windowId));
			jsonResponse(response,jsonObject);

        }else{
        	MiscUtils.getLogger().error("Scratch pad trying to save data for user " + pNo + " but session user is " + providerNo);
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
        return null;      
    }
    
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String id = request.getParameter("id");
	    JSONObject jsonObject = new JSONObject();

		if (id != null && ! id.isEmpty()) {
			ScratchPadDao scratchDao = SpringUtils.getBean(ScratchPadDao.class);
			ScratchPad scratch = scratchDao.find(Integer.parseInt(id));
			scratch.setStatus(false);
			scratchDao.merge(scratch);
			jsonObject.put("id", Encode.forHtmlContent(id));
			jsonObject.put("version", scratch.getDateTime() != null
					? scratch.getDateTime().toInstant().toString()
					: null);
			jsonObject.put("success", true);
		} else {
			jsonObject.put("success", false);
		}

	    jsonResponse(response,jsonObject);
    	return null;
    }


	private boolean isTextDifferent(String scratchPad, String returnText) {
		String s1 = scratchPad == null ? "" : scratchPad.trim();
		String s2 = returnText == null ? "" : returnText.trim();
		return !s1.equals(s2);
	}
    
    
}
