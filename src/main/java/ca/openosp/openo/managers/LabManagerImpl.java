//CHECKSTYLE:OFF

package ca.openosp.openo.managers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.openosp.openo.commn.dao.Hl7TextInfoDao;
import ca.openosp.openo.commn.dao.Hl7TextMessageDao;
import ca.openosp.openo.commn.dao.PatientLabRoutingDao;
import ca.openosp.openo.commn.dao.ProviderLabRoutingDao;
import ca.openosp.openo.commn.model.Hl7TextInfo;
import ca.openosp.openo.commn.model.Hl7TextMessage;
import ca.openosp.openo.commn.model.PatientLabRouting;
import ca.openosp.openo.commn.model.ProviderLabRoutingModel;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.managers.ProviderManager2;
import ca.openosp.openo.utility.MiscUtils;
import ca.openosp.openo.utility.PDFGenerationException;
import org.apache.logging.log4j.Logger;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.DocumentException;

import ca.openosp.openo.log.LogAction;
import ca.openosp.openo.lab.ca.all.pageUtil.LabPDFCreator;
import ca.openosp.openo.lab.ca.on.CommonLabResultData;
import ca.openosp.openo.util.StringUtils;


@Service
public class LabManagerImpl implements LabManager {

    private static final Logger logger = MiscUtils.getLogger();
    private static final String TEMP_PDF_DIRECTORY = "hl7PDF";
    private static final String DEFAULT_FILE_SUFFIX = ".pdf";

    @Autowired
    Hl7TextInfoDao hl7textInfoDao;

    @Autowired
    Hl7TextMessageDao hl7TextMessageDao;

    @Autowired
    private ProviderLabRoutingDao providerLabRoutingDao;

    @Autowired
    private NioFileManager nioFileManager;

    @Autowired
    private PatientLabRoutingDao patientLabRoutingDao;

    @Autowired
    SecurityInfoManager securityInfoManager;

    @Autowired
    ProviderManager2 providerManager2;

    public List<Hl7TextMessage> getHl7Messages(LoggedInInfo loggedInInfo, Integer demographicNo, int offset, int limit) {
        checkPrivilege(loggedInInfo, "r");

        LogAction.addLogSynchronous(loggedInInfo, "LabManager.getHl7Messages", "demographicNo=" + demographicNo);

        List<Hl7TextMessage> results = hl7TextMessageDao.findByDemographicNo(demographicNo, offset, limit);

        return results;
    }

    public List<Hl7TextInfo> getHl7TextInfo(LoggedInInfo loggedInInfo, int demographicNo) {
        checkPrivilege(loggedInInfo, "r");

        List<PatientLabRouting> patientLabRoutingList = patientLabRoutingDao.findByDemographicAndLabType(demographicNo, PatientLabRoutingDao.HL7);
        List<Integer> labIds = new ArrayList<Integer>();
        if (patientLabRoutingList != null) {
            for (PatientLabRouting patientLabRouting : patientLabRoutingList) {
                labIds.add(patientLabRouting.getLabNo());
            }
        }

        LogAction.addLogSynchronous(loggedInInfo, "LabManager.getHl7TextInfo", "demographicNo=" + demographicNo);

        return hl7textInfoDao.findByLabIdList(labIds);

    }

    public Hl7TextMessage getHl7Message(LoggedInInfo loggedInInfo, int labId) {
        checkPrivilege(loggedInInfo, "r");

        LogAction.addLogSynchronous(loggedInInfo, "LabManager.getHl7Message", "labId=" + labId);

        Hl7TextMessage result = hl7TextMessageDao.find(labId);

        return result;
    }

    public Path renderLab(LoggedInInfo loggedInInfo, Integer segmentId) throws PDFGenerationException {
        checkPrivilege(loggedInInfo, "r");
        LogAction.addLogSynchronous(loggedInInfo, "LabManager.getHl7MessageAsPDF", "labId=" + segmentId);

        Path path = null;
        try {
            String fileName = System.currentTimeMillis() + "_" + segmentId + "_LabReport";
            File tempPDF = File.createTempFile(fileName, "pdf");
            try (FileOutputStream fileOutputStream = new FileOutputStream(tempPDF);
                 ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();) {
                LabPDFCreator labPDFCreator = new LabPDFCreator(fileOutputStream, String.valueOf(segmentId), null);
                labPDFCreator.printPdf();
                labPDFCreator.addEmbeddedDocuments(tempPDF, byteOutputStream);
                path = nioFileManager.saveTempFile("temporaryPDF" + new Date().getTime(), byteOutputStream);
            }
            tempPDF.delete();
        } catch (IOException | DocumentException e) {
            throw new PDFGenerationException("Error Details: Lab [" + getDisplayLabName(segmentId) + "] could not be converted into a PDF", e);
        }

        return path;
    }

    /**
     * Returns all {@link ProviderLabRoutingModel} records that match the given lab number,
     * lab type, and provider number.
     *
     * @param loggedInInfo LoggedInInfo the currently logged-in user; used to enforce {@code _lab} read privilege
     * @param labId        Integer the unique lab segment ID to look up
     * @param labType      String the lab type (e.g. {@code "HL7"}, {@code "MDS"})
     * @param providerNo   String the provider number to filter routing records by
     * @return List&lt;ProviderLabRoutingModel&gt; matching routing records; empty list if none exist
     * @throws RuntimeException if the logged-in user lacks {@code _lab} read privilege
     */
    @Override
    public List<ProviderLabRoutingModel> findByLabNoAndLabTypeAndProviderNo(LoggedInInfo loggedInInfo, Integer labId, String labType, String providerNo) {
        checkPrivilege(loggedInInfo, "r");
        return providerLabRoutingDao.findByLabNoAndLabTypeAndProviderNo(labId, labType, providerNo);
    }

    /**
     * Files lab results for a provider up to (and including) a specific flagged lab,
     * depending on the fileUpToLabNo flag. Skips acknowledged or already filed results.
     * 
     * This method is specifically designed to support filing labs on behalf of another provider,
     * so the logic and conditions (such as checking for lab status 'N' when filing on behalf) 
     * are tailored for that use case.
     *
     * @param loggedInInfo                 the currently logged-in user
     * @param providerNo                   the provider number
     * @param flaggedLabId                 the lab ID that was flagged (i.e., selected by the user)
     * @param labType                      the type of the lab
     * @param comment                      the comment to add while filing
     * @param fileUpToLabNo                if true, file all labs up to and including flaggedLabId
     */
    @Override
    @Transactional
    public void fileLabsForProviderUpToFlaggedLab(LoggedInInfo loggedInInfo, String providerNo, String flaggedLabId, String labType, String comment, boolean fileUpToLabNo) {
        checkPrivilege(loggedInInfo, "w");

        int parsedFlaggedLabId;
        try {
            parsedFlaggedLabId = Integer.parseInt(flaggedLabId.trim());
        } catch (NumberFormatException e) {
            logger.error("fileLabsForProviderUpToFlaggedLab: invalid flaggedLabId='" + Encode.forJava(flaggedLabId) + "'");
            return;
        }

        CommonLabResultData commonLabResultData = new CommonLabResultData();

        // Gets lab IDs in order from oldest to latest (e.g., v1, v2, ..., vn)
        String labs = commonLabResultData.getMatchingLabs(flaggedLabId, labType);

        if (labs == null || labs.trim().isEmpty()) {
            logger.warn("fileLabsForProviderUpToFlaggedLab: no matching labs for flaggedLabId=" + Encode.forJava(flaggedLabId) + ", labType=" + Encode.forJava(labType));
            return;
        }

        // Derive on-behalf status from identity, not the request flag, to prevent bypass via crafted requests.
        boolean isFilingOnBehalf = !providerNo.equals(loggedInInfo.getLoggedInProviderNo());
        if (isFilingOnBehalf && !providerManager2.isHl7AllowOthersFileForYou(loggedInInfo, providerNo)) {
            throw new SecurityException("Provider " + providerNo + " has not allowed others to file on their behalf");
        }

        // Build the list of labs to file. getMatchingLabs returns them in version order
        // (oldest → newest), so we stop after the flagged lab rather than comparing IDs
        // numerically. ID-based comparison is unreliable because the query sorts by
        // (finalResultCount, obrDate, labNumber), so version order ≠ ID order in general.
        List<Integer> filteredLabs = new ArrayList<>();
        for (String part : labs.split(",")) {
            int labId = Integer.parseInt(part.trim());
            filteredLabs.add(labId);
            if (fileUpToLabNo && labId == parsedFlaggedLabId) {
                break;
            }
        }

        for (Integer labId : filteredLabs) {
            // Get routing info for the lab and provider
            List<ProviderLabRoutingModel> providerLabRoutings = findByLabNoAndLabTypeAndProviderNo(loggedInInfo, labId, labType, providerNo);
            if (providerLabRoutings.isEmpty()) continue;

            ProviderLabRoutingModel providerLabRouting = providerLabRoutings.get(0);

            // Determine whether to skip updating comment based on existing content
            boolean skipCommentOnUpdate = true;
            if (providerLabRouting.getComment() == null || providerLabRouting.getComment().trim().isEmpty()) {
                skipCommentOnUpdate = false;
            }

            // Skip if filing on behalf and lab is already Acknowledged or Filed
            String status = providerLabRouting.getStatus();
            if (isFilingOnBehalf && (ProviderLabRoutingDao.STATUS.A.name().equals(status) || ProviderLabRoutingDao.STATUS.F.name().equals(status))) {
                continue;
            }

            // Update report status and remove it from the queue
            CommonLabResultData.updateReportStatus(labId, providerNo, ProviderLabRoutingDao.STATUS.F.name().charAt(0),comment, labType, skipCommentOnUpdate);
            CommonLabResultData.removeFromQueue(labId);
            LogAction.addLogSynchronous(loggedInInfo, "LabManager.fileLabsForProviderUpToFlaggedLab",
                    "labId=" + labId + ", filedForProviderNo=" + providerNo + ", onBehalf=" + isFilingOnBehalf);
        }
    }

    @Override
    public List<ProviderLabRoutingModel> getProviderLabRouting(LoggedInInfo loggedInInfo, int labNo, String labType, String provider) {
        checkPrivilege(loggedInInfo, "r");
        return providerLabRoutingDao.findByLabNoAndLabTypeAndProviderNo(labNo, labType, provider);
    }

    private void checkPrivilege(LoggedInInfo loggedInInfo, String privilege) {
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_lab", privilege, null)) {
            throw new RuntimeException("missing required sec object (_lab)");
        }
    }

    private String getDisplayLabName(Integer segmentId) {
        Hl7TextInfo hl7TextInfo = hl7textInfoDao.findLabId(segmentId);
        return StringUtils.isNullOrEmpty(hl7TextInfo.getDiscipline()) ? "UNLABELLED" : hl7TextInfo.getDiscipline();
    }
}
