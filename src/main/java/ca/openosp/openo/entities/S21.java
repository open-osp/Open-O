//CHECKSTYLE:OFF

package ca.openosp.openo.entities;

/**
 * Entity representing S21 payment reconciliation records from BC MSP (Medical Services Plan) billing system.
 *
 * <p>The S21 record format is part of British Columbia's Teleplan billing system and contains detailed
 * payment information for healthcare provider billing transactions. Each S21 record represents a single
 * payment line item showing amounts billed, amounts paid, balances, and cheque information.</p>
 *
 * <p>This entity is typically used for:</p>
 * <ul>
 *   <li>Processing payment remittance advice from BC MSP</li>
 *   <li>Reconciling billed amounts with actual payments received</li>
 *   <li>Tracking payment history and outstanding balances</li>
 *   <li>Generating financial reports for healthcare providers</li>
 * </ul>
 *
 * <p>Key fields include payee identification (payeeNo, payeeName), financial data (amtBilled, amtPaid,
 * balanceFwd, newBalance, cheque), and administrative metadata (dataCentre, dataSeq, mspCTLno).</p>
 *
 * @since 2026-01-23
 * @see ca.openosp.openo.billing.CA.BC
 */
public class S21 {

    private String s21Id;
    private String fileName;
    private String dataCentre;
    private String dataSeq;
    private String paymentDate;
    private String lineCode;
    private String payeeNo;
    private String mspCTLno;
    private String payeeName;
    private String amtBilled;
    private String amtPaid;
    private String balanceFwd;
    private String cheque;
    private String newBalance;
    private String filler;
    private String status;

    /**
     * Default constructor for S21 payment reconciliation record.
     *
     * <p>Creates a new S21 instance with all fields initialized to null.
     * Fields should be populated using the setter methods after construction.</p>
     */
    public S21() {
    }

    /**
     * Gets the unique identifier for this S21 record.
     *
     * @return String the unique S21 record identifier
     */
    public String getS21Id() {
        return s21Id;
    }

    /**
     * Sets the unique identifier for this S21 record.
     *
     * @param s21Id String the unique S21 record identifier to set
     */
    public void setS21Id(String s21Id) {
        this.s21Id = s21Id;
    }

    /**
     * Gets the name of the file from which this S21 record was imported.
     *
     * @return String the source file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the name of the file from which this S21 record was imported.
     *
     * @param fileName String the source file name to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the data centre identifier where this payment record originated.
     *
     * @return String the data centre code
     */
    public String getDataCentre() {
        return dataCentre;
    }

    /**
     * Sets the data centre identifier where this payment record originated.
     *
     * @param dataCentre String the data centre code to set
     */
    public void setDataCentre(String dataCentre) {
        this.dataCentre = dataCentre;
    }

    /**
     * Gets the data sequence number for ordering records within a batch.
     *
     * @return String the sequence number
     */
    public String getDataSeq() {
        return dataSeq;
    }

    /**
     * Sets the data sequence number for ordering records within a batch.
     *
     * @param dataSeq String the sequence number to set
     */
    public void setDataSeq(String dataSeq) {
        this.dataSeq = dataSeq;
    }

    /**
     * Gets the date when the payment was processed by BC MSP.
     *
     * @return String the payment date
     */
    public String getPaymentDate() {
        return paymentDate;
    }

    /**
     * Sets the date when the payment was processed by BC MSP.
     *
     * @param paymentDate String the payment date to set
     */
    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Gets the line code identifying the type of payment record entry.
     *
     * @return String the line code
     */
    public String getLineCode() {
        return lineCode;
    }

    /**
     * Sets the line code identifying the type of payment record entry.
     *
     * @param lineCode String the line code to set
     */
    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    /**
     * Gets the payee number identifying the healthcare provider receiving payment.
     *
     * @return String the payee number (provider billing number)
     */
    public String getPayeeNo() {
        return payeeNo;
    }

    /**
     * Sets the payee number identifying the healthcare provider receiving payment.
     *
     * @param payeeNo String the payee number (provider billing number) to set
     */
    public void setPayeeNo(String payeeNo) {
        this.payeeNo = payeeNo;
    }

    /**
     * Gets the MSP control number for tracking and reference purposes.
     *
     * @return String the MSP control number
     */
    public String getMspCTLno() {
        return mspCTLno;
    }

    /**
     * Sets the MSP control number for tracking and reference purposes.
     *
     * @param mspCTLno String the MSP control number to set
     */
    public void setMspCTLno(String mspCTLno) {
        this.mspCTLno = mspCTLno;
    }

    /**
     * Gets the name of the healthcare provider receiving payment.
     *
     * @return String the payee name (provider name)
     */
    public String getPayeeName() {
        return payeeName;
    }

    /**
     * Sets the name of the healthcare provider receiving payment.
     *
     * @param payeeName String the payee name (provider name) to set
     */
    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    /**
     * Gets the total amount billed to BC MSP for healthcare services.
     *
     * @return String the amount billed (stored as string for precision)
     */
    public String getAmtBilled() {
        return amtBilled;
    }

    /**
     * Sets the total amount billed to BC MSP for healthcare services.
     *
     * @param amtBilled String the amount billed to set (stored as string for precision)
     */
    public void setAmtBilled(String amtBilled) {
        this.amtBilled = amtBilled;
    }

    /**
     * Gets the actual amount paid by BC MSP for the billed services.
     *
     * <p>This amount may differ from the amount billed due to adjustments,
     * denials, or partial payments.</p>
     *
     * @return String the amount paid (stored as string for precision)
     */
    public String getAmtPaid() {
        return amtPaid;
    }

    /**
     * Sets the actual amount paid by BC MSP for the billed services.
     *
     * @param amtPaid String the amount paid to set (stored as string for precision)
     */
    public void setAmtPaid(String amtPaid) {
        this.amtPaid = amtPaid;
    }

    /**
     * Gets the balance brought forward from previous payment periods.
     *
     * <p>Represents any outstanding balance or credit from prior billing cycles
     * that affects the current payment calculation.</p>
     *
     * @return String the balance forward amount (stored as string for precision)
     */
    public String getBalanceFwd() {
        return balanceFwd;
    }

    /**
     * Sets the balance brought forward from previous payment periods.
     *
     * @param balanceFwd String the balance forward amount to set (stored as string for precision)
     */
    public void setBalanceFwd(String balanceFwd) {
        this.balanceFwd = balanceFwd;
    }

    /**
     * Gets the cheque number or payment reference for this transaction.
     *
     * @return String the cheque number or payment reference
     */
    public String getCheque() {
        return cheque;
    }

    /**
     * Sets the cheque number or payment reference for this transaction.
     *
     * @param cheque String the cheque number or payment reference to set
     */
    public void setCheque(String cheque) {
        this.cheque = cheque;
    }

    /**
     * Gets the new balance after applying this payment transaction.
     *
     * <p>Calculated as: balance forward + amount billed - amount paid = new balance</p>
     *
     * @return String the new balance amount (stored as string for precision)
     */
    public String getNewBalance() {
        return newBalance;
    }

    /**
     * Sets the new balance after applying this payment transaction.
     *
     * @param newBalance String the new balance amount to set (stored as string for precision)
     */
    public void setNewBalance(String newBalance) {
        this.newBalance = newBalance;
    }

    /**
     * Gets the filler field reserved for future use or additional data.
     *
     * <p>This field is part of the S21 record format specification and may be
     * used for padding or storing supplementary information.</p>
     *
     * @return String the filler field content
     */
    public String getFiller() {
        return filler;
    }

    /**
     * Sets the filler field reserved for future use or additional data.
     *
     * @param filler String the filler field content to set
     */
    public void setFiller(String filler) {
        this.filler = filler;
    }

    /**
     * Gets the processing status of this payment record.
     *
     * <p>Indicates whether the record has been processed, is pending review,
     * or requires reconciliation action.</p>
     *
     * @return String the processing status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the processing status of this payment record.
     *
     * @param status String the processing status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
