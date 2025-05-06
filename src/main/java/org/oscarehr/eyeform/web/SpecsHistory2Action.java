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


package org.oscarehr.eyeform.web;

import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;
import org.apache.struts2.ServletActionContext;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.eyeform.dao.EyeformSpecsHistoryDao;
import org.oscarehr.eyeform.model.EyeformSpecsHistory;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SpecsHistory2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private EyeformSpecsHistoryDao dao = (EyeformSpecsHistoryDao) SpringUtils.getBean(EyeformSpecsHistoryDao.class);

    @Override
    public String execute() throws Exception {
        String method = request.getParameter("method");
        if ("cancel".equals(method)) {
            return cancel();
        } else if ("list".equals(method)) {
            return list();
        } else if ("save".equals(method)) {
            return save();
        } else if ("copySpecs".equals(method)) {
            return copySpecs();
        }
        return form();
    }

    public String cancel() {
        return form();
    }

    public String list() {
        String demographicNo = request.getParameter("demographicNo");

        List<EyeformSpecsHistory> specs = dao.getByDemographicNo(Integer.parseInt(demographicNo));
        request.setAttribute("specs", specs);

        return "list";
    }

    public String form() {
        ProviderDao providerDao = (ProviderDao) SpringUtils.getBean(ProviderDao.class);

        request.setAttribute("providers", providerDao.getActiveProviders());

        if (request.getParameter("specs.id") != null) {
            int shId = Integer.parseInt(request.getParameter("specs.id"));
            EyeformSpecsHistory specs = dao.find(shId);
            setSpecs(specs);

            if (request.getParameter("json") != null && request.getParameter("json").equalsIgnoreCase("true")) {
                try {
                    HashMap<String, EyeformSpecsHistory> hashMap = new HashMap<String, EyeformSpecsHistory>();

                    hashMap.put("specs", specs);

                    JsonConfig config = new JsonConfig();
                    config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

                    JSONObject json = JSONObject.fromObject(hashMap, config);
                    response.getOutputStream().write(json.toString().getBytes());
                } catch (Exception e) {
                    MiscUtils.getLogger().error("Can't write json encoded message", e);
                }

                return null;
            }
        }

        return "form";
    }

    public String save() throws Exception {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        if (specs.getId() != null && specs.getId() == 0) {
            specs.setId(null);
        }
        specs.setProvider(loggedInInfo.getLoggedInProviderNo());

        if (request.getParameter("specs.id") != null && request.getParameter("specs.id").length() > 0) {
            specs.setId(Integer.parseInt(request.getParameter("specs.id")));
        }

        if (specs.getId() != null && specs.getId() == 0) {
            specs.setId(null);
        }
        specs.setUpdateTime(new Date());

        if (specs.getId() == null) {
            dao.persist(specs);
        } else {
            dao.merge(specs);
        }


        if (request.getParameter("json") != null && request.getParameter("json").equalsIgnoreCase("true")) {
            HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
            hashMap.put("saved", specs.getId());

            JSONObject json = JSONObject.fromObject(hashMap);
            response.getOutputStream().write(json.toString().getBytes());

            return null;
        }

        request.setAttribute("parentAjaxId", "specshistory");
        return SUCCESS;
    }

    public String copySpecs() throws IOException {
        String demographicNo = request.getParameter("demographicNo");
        List<EyeformSpecsHistory> specs = dao.getByDemographicNo(Integer.parseInt(demographicNo));
        if (specs.size() > 0) {
            EyeformSpecsHistory latestSpecs = specs.get(0);
            PrintWriter out = response.getWriter();
            out.println("setfieldvalue(\"od_manifest_refraction_sph\",\"" + latestSpecs.getOdSph() + "\");");
            out.println("setfieldvalue(\"os_manifest_refraction_sph\",\"" + latestSpecs.getOsSph() + "\");");
            out.println("setfieldvalue(\"od_manifest_refraction_cyl\",\"" + latestSpecs.getOdCyl() + "\");");
            out.println("setfieldvalue(\"os_manifest_refraction_cyl\",\"" + latestSpecs.getOsCyl() + "\");");
            out.println("setfieldvalue(\"od_manifest_refraction_axis\",\"" + latestSpecs.getOdAxis() + "\");");
            out.println("setfieldvalue(\"os_manifest_refraction_axis\",\"" + latestSpecs.getOsAxis() + "\");");
        } else {
            PrintWriter out = response.getWriter();
            out.println("alert('No Specs Found.');");
        }
        return null;
    }

    private EyeformSpecsHistory specs;

    public EyeformSpecsHistory getSpecs() {
        return specs;
    }

    public void setSpecs(EyeformSpecsHistory specs) {
        this.specs = specs;
    }
}
