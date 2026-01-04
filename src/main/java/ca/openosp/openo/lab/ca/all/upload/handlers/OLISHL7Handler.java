//CHECKSTYLE:OFF
/**
 * Copyright (c) 2008-2012 Indivica Inc.
 * <p>
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

/*
 * HL7Handler
 * Upload handler
 *
 */
package ca.openosp.openo.lab.ca.all.upload.handlers;

import java.util.ArrayList;

import ca.openosp.openo.utility.MiscUtils;
import org.apache.logging.log4j.Logger;
import ca.openosp.openo.commn.dao.Hl7TextInfoDao;
import ca.openosp.openo.olis.OLISUtils;
import ca.openosp.openo.utility.DbConnectionFilter;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.SpringUtils;

import ca.openosp.openo.log.LogAction;
import ca.openosp.openo.lab.ca.all.parsers.Factory;
import ca.openosp.openo.lab.ca.all.upload.MessageUploader;
import ca.openosp.openo.lab.ca.all.upload.ProviderLabRouting;
import ca.openosp.openo.lab.ca.all.upload.RouteReportResults;
import ca.openosp.openo.lab.ca.all.util.Utilities;

/**
 *
 */
public class OLISHL7Handler implements MessageHandler {

    Logger logger = MiscUtils.getLogger();
    Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao) SpringUtils.getBean(Hl7TextInfoDao.class);

    private int lastSegmentId = 0;

    public OLISHL7Handler() {
        logger.info("NEW OLISHL7Handler UPLOAD HANDLER instance just instantiated. ");
    }

    public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr) {
        return parse(loggedInInfo, serviceName, fileName, fileId, false);
    }

    public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, boolean routeToCurrentProvider) {
        int i = 0;
        String lastTimeStampAccessed = null;
        RouteReportResults results = new RouteReportResults();

        try {
            ArrayList<String> messages = Utilities.separateMessages(fileName);

            for (i = 0; i < messages.size(); i++) {
                String msg = messages.get(i);
                logger.info(msg);

                lastTimeStampAccessed = getLastUpdateInOLIS(msg);

                if (OLISUtils.isDuplicate(loggedInInfo, msg)) {
                    LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), "OLIS", "DUPLICATE", fileName, null);
                    continue;
                }
                MessageUploader.routeReport(loggedInInfo, serviceName, "OLIS_HL7", msg.replace("\\E\\", "\\SLASHHACK\\").replace("µ", "\\MUHACK\\").replace("\\H\\", "\\.H\\").replace("\\N\\", "\\.N\\"), fileId, results);
                if (routeToCurrentProvider) {
                    ProviderLabRouting routing = new ProviderLabRouting();
                    routing.route(results.segmentId, loggedInInfo.getLoggedInProviderNo(), DbConnectionFilter.getThreadLocalDbConnection(), "HL7");

                }
                this.lastSegmentId = results.segmentId;
            }
            logger.info("Parsed OK");
        } catch (Exception e) {
            MessageUploader.clean(fileId);
            logger.error("Could not upload message", e);
            return null;
        }
        return lastTimeStampAccessed;
    }

    public int getLastSegmentId() {
        return this.lastSegmentId;
    }
    //TODO: check HIN
    //TODO: check # of results

    private String getLastUpdateInOLIS(String msg) {
        ca.openosp.openo.lab.ca.all.parsers.OLISHL7Handler h = (ca.openosp.openo.lab.ca.all.parsers.OLISHL7Handler) Factory.getHandler("OLIS_HL7", msg);
        return h.getLastUpdateInOLISUnformated();
    }
}
