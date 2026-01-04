//CHECKSTYLE:OFF
package ca.openosp.openo.billings.ca.bc.administration;


import ca.openosp.openo.PMmodule.dao.ProviderDao;
import ca.openosp.openo.commn.dao.BillingDao;
import ca.openosp.openo.commn.dao.BillingServiceDao;
import ca.openosp.openo.commn.model.Billing;
import ca.openosp.openo.commn.model.BillingService;
import ca.openosp.openo.commn.model.Demographic;
import ca.openosp.openo.commn.model.Provider;
import ca.openosp.openo.managers.DemographicManager;
import ca.openosp.openo.utility.DateRange;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.SpringUtils;
import ca.openosp.openo.entities.Billingmaster;
import ca.openosp.openo.util.ConversionUtils;

import java.util.Properties;
import java.util.Vector;

public class GstReport {

    public Vector<Properties> getGST(LoggedInInfo loggedInInfo, String[] providerNos, String startDate, String endDate) {
        Properties props;
        Vector<Properties> list = new Vector<Properties>();
        BillingDao dao = SpringUtils.getBean(BillingDao.class);
        DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
        ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);

        // For every bill the providers is involved with, search the gst value, date, demo no within the chosen dates
        DateRange dateRange = new DateRange(ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate));
        if (providerNos != null && providerNos.length > 0) {
            for (Object[] i : dao.findProviderBillingsWithGst(providerNos, dateRange)) {
                Billingmaster bm = (Billingmaster) i[0];
                Billing b = (Billing) i[1];
                props = new Properties();

                props.setProperty("billNo", String.valueOf(b.getId()));
                props.setProperty("date", ConversionUtils.toDateString(b.getBillingDate()));
                props.setProperty("demographic_no", "" + bm.getDemographicNo());

                Demographic demo = demographicManager.getDemographic(loggedInInfo, bm.getDemographicNo());
                if (demo != null) {
                    props.setProperty("name", demo.getFirstName() + " " + demo.getLastName());
                }

                Provider provider = providerDao.getProvider(b.getProviderNo());
                if (provider != null) {
                    props.setProperty("providerName", provider.getFullName());
                }
                props.setProperty("total", bm.getBillAmount());

                list.add(props);
            }
        }


        return list;
    }

    public String getGstFlag(String code, String date) {
        BillingServiceDao dao = SpringUtils.getBean(BillingServiceDao.class);
        for (BillingService bs : dao.findGst(code, ConversionUtils.fromDateString(date))) {
            return ConversionUtils.toBoolString(bs.getGstFlag());
        }
        return "";
    }
}
