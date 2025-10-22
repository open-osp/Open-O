//CHECKSTYLE:OFF
package ca.openosp.openo.billings.ca.bc.privateBilling;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Author: Charles Liu <charles.liu@nondfa.com>
 * Company: WELL Health Technologies Corp.
 * Date: December 6, 2018
 */
public class PrivateBillingDAO {
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet rs;

    public PrivateBillingDAO() {
        connection = null;
        statement = null;
        rs = null;
    }

    public HashMap<String, String> getRecipientById(String recipientId) {
        HashMap<String, String> recipient = new HashMap<String, String>() {{
            put("name", "");
            put("address", "");
            put("city", "");
            put("province", "");
            put("postal", "");
        }};

        try {
            String sqlstmt = "SELECT name, address, city, province, postal FROM bill_recipients WHERE id=?";
            connection = DbUtil.getConnection();
            statement = connection.prepareStatement(sqlstmt);
            statement.setString(1, recipientId);
            rs = statement.executeQuery();
            while (rs.next()) {
                recipient.put("name", rs.getString("name"));
                recipient.put("address", rs.getString("address"));
                recipient.put("city", rs.getString("city"));
                recipient.put("province", rs.getString("province"));
                recipient.put("postal", rs.getString("postal"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return recipient;
    }

    public List<HashMap<String, String>> listPrivateBillItems(String demographicNumber, String recipientName) {
        List<HashMap<String, String>> bills = new ArrayList<HashMap<String, String>>();

        try {
            String sqlstmt = String.join(" ",
                    "SELECT COALESCE(br.id,'') recipient,",
                    "br.name,",
                    "b.billing_no,",
                    "b.demographic_no,",
                    "b.provider_no,",
                    "b.demographic_name,",
                    "b.billing_date,",
                    "b.total,",
                    "b.status,",
                    "bm.payee_no,",
                    "bm.billing_unit,",
                    "bm.bill_amount,",
                    "bm.billingmaster_no,",
                    "bm.billing_code,",
                    "bm.billing_unit,",
                    "bm.gst,",
                    "bm.gst_no,",
                    "bh.amount,",
                    "SUM(bh.amount_received) AS amount_received,",
                    "bs.description",
                    "FROM bill_recipients br",
                    "RIGHT JOIN billing b ON (b.billing_no = br.billingNo)",
                    "LEFT JOIN billingmaster bm USING (billing_no)",
                    "LEFT JOIN billing_history bh ON (bm.billingmaster_no = bh.billingmaster_no)",
                    "INNER JOIN billingservice bs ON (bm.billing_code = bs.service_code)",
                    "WHERE b.billingtype = 'PRI'",
                    "AND bh.billingtype = 'PRI'",
                    "AND bm.billingstatus LIKE 'P'",
                    "AND b.demographic_no LIKE ?", // <- demographic number
                    "AND COALESCE(br.name,'') = ?", // <- recipient name?? why not using recipient Id??
                    "GROUP BY bh.billingmaster_no",
                    "HAVING bh.billingmaster_no",
                    "ORDER BY b.billing_date DESC");
            connection = DbUtil.getConnection();
            statement = connection.prepareStatement(sqlstmt);
            statement.setString(1, demographicNumber);
            statement.setString(2, recipientName);
            rs = statement.executeQuery();
            while (rs.next()) {
                HashMap<String, String> invoiceItem = new HashMap<String, String>();
                invoiceItem.put("name", rs.getString("name"));
                invoiceItem.put("billing_no", rs.getString("billing_no"));
                invoiceItem.put("demographic_no", rs.getString("demographic_no"));
                invoiceItem.put("provider_no", rs.getString("provider_no"));
                invoiceItem.put("demographic_name", rs.getString("demographic_name"));
                invoiceItem.put("billing_date", rs.getString("billing_date"));
                invoiceItem.put("total", rs.getString("total"));
                invoiceItem.put("status", rs.getString("status"));
                invoiceItem.put("payee_no", rs.getString("payee_no"));
                invoiceItem.put("billing_unit", rs.getString("billing_unit"));
                invoiceItem.put("bill_amount", rs.getString("bill_amount"));
                invoiceItem.put("billingmaster_no", rs.getString("billingmaster_no"));
                invoiceItem.put("billing_code", rs.getString("billing_code"));
                invoiceItem.put("billing_unit", rs.getString("billing_unit"));
                invoiceItem.put("gst", rs.getString("gst"));
                invoiceItem.put("gstNo", rs.getString("gst_no"));
                invoiceItem.put("amount", rs.getString("amount"));
                invoiceItem.put("amount_received", rs.getString("amount_received"));
                invoiceItem.put("description", rs.getString("description"));
                bills.add(invoiceItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bills;
    }

    public List<PrivateBillingModel> listPrivateBills(String providerId) {
        List<PrivateBillingModel> bills = new ArrayList<PrivateBillingModel>();
        try {
            String sqlstmt = String.join(" ",
                    "SELECT COUNT(bill.demographic_no) AS 'items', ",
                    "bill.recipient,",
                    "bill.demographic_name,",
                    "bill.demographic_no,",
                    "bill.billing_no,",
                    "bill.billingtype,",
                    "bill.provider_no,",
                    "bill.name,",
                    "bill.billing_date,",
                    "bill.status,",
                    "bm.billingstatus,",
                    "bill.provider_ohip_no,",
                    "SUM(bm.bill_amount) AS balance,",
                    "bm.billingmaster_no,",
                    "bm.billing_code,",
                    "bm.billing_unit",
                    "FROM billingmaster bm",
                    "INNER JOIN (",
                    "SELECT br.id AS 'recipient', br.name, b.*",
                    "FROM bill_recipients br",
                    "RIGHT JOIN billing b",
                    "ON (b.billing_no = br.billingNo)",
                    "WHERE b.billingtype = 'PRI' AND b.status NOT LIKE 'A') bill",
                    "ON (bill.billing_no = bm.billing_no)",
                    "WHERE bm.billingstatus LIKE 'P' AND bill.provider_no LIKE ?",
                    "GROUP BY bill.demographic_no, bill.name",
                    "ORDER BY bill.billing_date DESC");
            connection = DbUtil.getConnection();
            statement = connection.prepareStatement(sqlstmt);
            statement.setString(1, providerId);

            rs = statement.executeQuery();
            while (rs.next()) {
                PrivateBillingModel model = new PrivateBillingModel();
                model.setBillingCount(rs.getInt("items"));
                model.setBillingNumber(rs.getString("billing_no"));
                model.setBillingDate(rs.getString("billing_date"));
                model.setBillingType(rs.getString("billingtype"));
                model.setBillingStatus(rs.getString("billingstatus"));
                model.setDemographicName(rs.getString("demographic_name"));
                model.setDemographicNumber(rs.getString("demographic_no"));
                model.setProviderNumber(rs.getString("provider_no"));
                model.setRecipientId(rs.getString("recipient"));
                model.setRecipientName(rs.getString("name"));
                model.setBalance(rs.getString("balance"));
                model.setStatus(rs.getString("status"));
                bills.add(model);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bills;
    }
}