package oscar.oscarBilling.ca.bc.administration;


import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.DateRange;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import oscar.entities.Billingmaster;
import oscar.util.ConversionUtils;

import java.util.Properties;
import java.util.Vector;

public class GstReport {

    public Vector<Properties> getGST(LoggedInInfo loggedInInfo, String[] providerNos, String startDate, String endDate)  {
        Properties props;
        Vector<Properties> list = new Vector<Properties>();
        BillingDao dao = SpringUtils.getBean(BillingDao.class);
        DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
        ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);

        // For every bill the provider is involved with, search the gst value, date, demo no within the chosen dates
        DateRange dateRange = new DateRange(ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate));
        if (providerNos != null && providerNos.length > 0) {
            for (Object[] i : dao.findProviderBillingsWithGst(providerNos, dateRange)) {
                Billingmaster bm = (Billingmaster) i[0];
                Billing b = (Billing) i[1];
                props = new Properties();

                props.setProperty("billNo", String.valueOf(b.getId()));
                props.setProperty("date", ConversionUtils.toDateString(b.getBillingDate()));
                props.setProperty("demographic_no", "" + bm.getDemographicNo());

                Demographic demo = demographicManager.getDemographic(loggedInInfo,bm.getDemographicNo());
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
