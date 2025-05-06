//CHECKSTYLE:OFF
/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * Contributors:
 * <Quatro Group Software Systems inc.>  <OSCAR Team>
 */

package org.oscarehr.PMmodule.web;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.oscarehr.PMmodule.model.HealthSafety;
import org.oscarehr.PMmodule.service.HealthSafetyManager;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class HealthSafety2Action extends ActionSupport {
    private HttpServletRequest request = ServletActionContext.getRequest();

    private static Logger log = MiscUtils.getLogger();

    private HealthSafetyManager healthSafetyManager = SpringUtils.getBean(HealthSafetyManager.class);


    public String execute() {
//		logManager.log("read","full provider list","",request);
//		log.warn("Program doesn't have a name?");
        if ("savehealthSafety".equals(request.getParameter("method"))) {
            return savehealthSafety();
        }
        return form();
    }


    public String form() {


        String id = request.getParameter("id");

        HealthSafety healthsafety = healthSafetyManager.getHealthSafetyByDemographic(Long.valueOf(id));
        if (healthsafety != null) {
            this.setHealthsafety(healthsafety);
            request.setAttribute("healthsafety", healthsafety);
        } else {
            healthsafety = new HealthSafety();
            Provider provider = (Provider) request.getSession().getAttribute("provider");
            healthsafety.setUserName(provider.getFormattedName());
            healthsafety.setDemographicNo(Long.valueOf(id));
            this.setHealthsafety(healthsafety);
            request.setAttribute("healthsafety", healthsafety);
        }

        return "edit";
    }


    public String savehealthSafety() {

        log.debug("Saving health and Safety");



        HealthSafety healthsafety = this.getHealthsafety();

        healthsafety.setUpdateDate(new Timestamp(System.currentTimeMillis()));

        healthSafetyManager.saveHealthSafetyByDemographic(healthsafety);

        return SUCCESS;
    }

    private HealthSafety healthsafety;

    public HealthSafety getHealthsafety() {
        return healthsafety;
    }

    public void setHealthsafety(HealthSafety healthsafety) {
        this.healthsafety = healthsafety;
    }
}
