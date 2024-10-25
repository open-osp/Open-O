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


package oscar.oscarMDS.pageUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.ProviderLabRoutingFavoritesDao;
import org.oscarehr.common.model.ProviderLabRoutingFavorite;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.on.CommonLabResultData;

public class ReportReassignAction extends Action {
    
    private final Logger logger = MiscUtils.getLogger();
    private final SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    public ReportReassignAction() {
    }
    
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
    	if(!securityInfoManager.hasPrivilege(loggedInInfo, "_lab", "w", null)) {
			throw new SecurityException("missing required security object (_lab)");
		}

    	String status = request.getParameter("status");
        String ajax=request.getParameter("ajax");
        String providerNo = loggedInInfo.getLoggedInProviderNo();
        String searchProviderNo = request.getParameter("searchProviderNo");
        JSONArray jsonArray = null;
        String[] selectedProvidersArray = new String[0];
        String[] arrNewFavs = new String[0];
        ArrayList<String[]> flaggedLabsList = new ArrayList<>();
        boolean success = Boolean.FALSE;
        /*
         * Group together any new favorite providers that may have been
         * set during the forward process.
         */
        String newFavorites = request.getParameter("selectedFavorites");
        if(newFavorites != null && ! newFavorites.isEmpty())
        {
            JSONObject jsonObject = JSONObject.fromObject(newFavorites);
            jsonArray = (JSONArray) jsonObject.get("favorites");
        }

        if(jsonArray != null) {
            arrNewFavs = (String[]) jsonArray.toArray(new String[jsonArray.size()]);
        }

        /*
         * Group together the providers selected during the forward
         * process.
         */
        String selectedProviders = request.getParameter("selectedProviders");
        logger.info("selected providers to forward labs to " + selectedProviders);

        if(selectedProviders != null && ! selectedProviders.isEmpty()) {
            JSONObject jsonObject = JSONObject.fromObject(selectedProviders);
            jsonArray = jsonObject.getJSONArray("providers");
        }

        if(jsonArray != null)
        {
            selectedProvidersArray = (String[]) jsonArray.toArray(new String[jsonArray.size()]);
        }

        /*
         * Group together the lab ids and types checked off during the
         * forwarding process.
         */
        String flaggedLabs = request.getParameter("flaggedLabs");
        if(flaggedLabs != null && ! flaggedLabs.isEmpty())
        {
            JSONObject jsonObject = JSONObject.fromObject(flaggedLabs);
            jsonArray = (JSONArray) jsonObject.get("files");
        }

        if(jsonArray != null)
        {
            String[] labid;
            for(int i = 0; i < jsonArray.size(); i++ )
            {
                labid = jsonArray.getString(i).split(":");
                flaggedLabsList.add(labid);
            }
        }

        String newURL = "";
        try {
        	//Only route if there are selected providers
        	if(selectedProvidersArray.length > 0) {
        		success = CommonLabResultData.updateLabRouting(flaggedLabsList, selectedProvidersArray);
        	}

            //update favorites
            ProviderLabRoutingFavoritesDao favDao = (ProviderLabRoutingFavoritesDao) SpringUtils.getBean(ProviderLabRoutingFavoritesDao.class);
            List<ProviderLabRoutingFavorite> currentFavorites = favDao.findFavorites(providerNo);

            if (arrNewFavs.length == 0) {
                for (ProviderLabRoutingFavorite fav : currentFavorites) {
                    favDao.remove(fav.getId());
                }
            } else {
                //Check for new favorites to add
                boolean isNew;
                for (int idx = 0; idx < arrNewFavs.length; ++idx) {
                    isNew = true;
                    for (ProviderLabRoutingFavorite fav : currentFavorites) {
                        if (fav.getRoute_to_provider_no().equals(arrNewFavs[idx])) {
                            isNew = false;
                            break;
                        }
                    }
                    if (isNew) {
                        ProviderLabRoutingFavorite newFav = new ProviderLabRoutingFavorite();
                        newFav.setProvider_no(providerNo);
                        newFav.setRoute_to_provider_no(arrNewFavs[idx]);
                        favDao.persist(newFav);
                    }
                }

                //check for favorites to remove
                boolean remove;
                for (ProviderLabRoutingFavorite fav : currentFavorites) {
                    remove = true;
                    for (int idx2 = 0; idx2 < arrNewFavs.length; ++idx2) {
                        if (fav.getRoute_to_provider_no().equals(arrNewFavs[idx2])) {
                            remove = false;
                            break;
                        }
                    }
                    if (remove) {
                        favDao.remove(fav.getId());
                    }
                }

            }

            newURL = mapping.findForward("success").getPath();
            if(newURL.contains("labDisplay.jsp")) {
                newURL = newURL + "?providerNo=" + providerNo + "&searchProviderNo=" + searchProviderNo + "&status=" + status + "&segmentID=" + flaggedLabsList.get(0);

                // the segmentID is needed when being called from a lab display
            } else {
                newURL = newURL + "&providerNo=" + providerNo + "&searchProviderNo=" + searchProviderNo + "&status=" + status + "&segmentID=" + flaggedLabsList.get(0);
            }
            if (request.getParameter("lname") != null) { newURL = newURL + "&lname="+request.getParameter("lname"); }
            if (request.getParameter("fname") != null) { newURL = newURL + "&fname="+request.getParameter("fname"); }
            if (request.getParameter("hnum") != null) { newURL = newURL + "&hnum="+request.getParameter("hnum"); }
        } catch (Exception e) {
            logger.error("exception in ReportReassignAction", e);
            newURL = mapping.findForward("failure").getPath();
        }

        if(ajax!=null && ajax.equals("yes")){
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", success);
            jsonResponse.put("files", jsonArray);
            try {
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(jsonResponse);
                out.flush();
            } catch (IOException e) {
                MiscUtils.getLogger().error("Error with JSON response ", e);
            }
            return null;
        }
        else{
            return (new ActionForward(newURL));
        }
    }
}
