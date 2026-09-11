package oscar.oscarDemographic.pageUtil;

import cds.LaboratoryResultsDocument;
import cdsDt.DateTimeFullOrPartial;
import org.apache.logging.log4j.Logger;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderLabRoutingModel;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The HL7CreateFile class is responsible for generating HL7 formatted messages
 * based on demographic information and laboratory results data from OMD CDS XML import files. It provides
 * methods for constructing various HL7 segments such as MSH, PID, OBR, OBX,
 * and custom segments like ZFR, ZMC, ZMN, and ZRG, which are specific to the
 * application's requirements.
 */
public class HL7CreateFile {
    private final Demographic demographic;
	private final ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
    private String LAB_TYPE = "";
    Integer resultCount = 1;
    private static final Logger logger = MiscUtils.getLogger();
    private static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat inputDateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat xmlTimezoneOffSetDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private static final SimpleDateFormat fullDateTime = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat fullDate = new SimpleDateFormat("yyyyMMdd");
	private static final List<String> aliasForPathL7 = new ArrayList<>(
			Arrays.asList("TRANSFHA", "FHAM", "LIFELABS", "EXCELLERIS", "BCB", "VPP-BCC",
					"SG", "CDC", "VPP-PHC", "VCH", "PATHL7", "VPP-CDC", "VPP-BCCA", "VPP-VCH", "PHC")
	);
	private Map<String, ProviderLabRoutingModel> providerLabRoutingQueue;
	private final StringBuilder reviewerComment;

    public HL7CreateFile(Demographic demographic){
        this.demographic = demographic;
	    this.reviewerComment = new StringBuilder();
    }
    
    public String generateHL7(List<LaboratoryResultsDocument.LaboratoryResults> labs) {
        StringBuilder hl7 = new StringBuilder();
        
        if (labs != null && !labs.isEmpty()) {
            resultCount = labs.size();

			// the first lab in the list will contain the correct header information for the remaining
	        // messages in the batch
            LaboratoryResultsDocument.LaboratoryResults firstLab = labs.get(0);
            String labType = firstLab.getLaboratoryName();

	        // if no lab identifier in the first result, then try the next lab in the list - if there is one.
            if (! StringUtils.filled(labType) && labs.size() > 1 && labs.get(1) != null) {
	            firstLab = labs.get(1);
	            labType = firstLab.getLaboratoryName();
            }

			// if the lab type is split
	        if(labType.contains("^")) {
	            labType = labType.split("\\^")[0];
		        if(! StringUtils.filled(labType)) {
			        labType = labType.split("\\^")[1];
		        }
	        }

			if(labType == null) {
				labType = "";
			}

	        labType = labType.trim().toUpperCase();

	        if (aliasForPathL7.contains(labType)) {
		        LAB_TYPE = "PATHL7";
            } else if(labType.equals("MDS")) {
                LAB_TYPE = "MDS";
            } else if (labType.equalsIgnoreCase("Gamma") || labType.equals("GDML")) {
                LAB_TYPE = "GDML";
            } else if (labType.equalsIgnoreCase("ExcellerisON")) {
                LAB_TYPE = "ExcellerisON";
            }
            
            hl7.append(generateMSH(firstLab)).append("\n");

            if (LAB_TYPE.equals("MDS")) {
                hl7.append("ZLB||||||||||||||||").append("\n");
                hl7.append(generateZRG(labs));
                hl7.append(generateZMN(labs));
                hl7.append(generateZMC(labs));
                hl7.append("ZCL||^^^^^^^^^^^^|^^^|||||||||").append("\n");
            }
            
            hl7.append(generatePID(demographic, firstLab)).append("\n");
            if (firstLab.getBlockedTestResult() != null && "Y".equals(firstLab.getBlockedTestResult().toString())) {
                hl7.append("ZPD|||Y|").append("\n");
            }
            
            if (LAB_TYPE.equals("MDS")) {
                hl7.append("PV1||R|^^^^^^^^|||||^^^^^|||||||||^^^^^^^^^^^^||||||||||||||||||||||||1|||").append("\n");
                hl7.append(generateZFR(firstLab)).append("\n");
                hl7.append("ZCT|||||||").append("\n");
            }

            if (LAB_TYPE.equals("CML") || LAB_TYPE.equals("PATHL7") || LAB_TYPE.equals("ExcellerisON")) {
                hl7.append(generateORC(firstLab)).append("\n");
            }
            
            hl7.append(generateOBR(firstLab)).append("\n");

			/* extract OBX segments from the entire batch of labs.
	         * also generates the NTE segments (laboratory comments) and the
	         * ProviderLabRoutingModel for routing labs to the provider
	         */
            hl7.append(generateOBX(labs));

            if (LAB_TYPE.equalsIgnoreCase("PATHL7") || LAB_TYPE.equalsIgnoreCase("ExcellerisON")
		            || LAB_TYPE.equalsIgnoreCase("default")) {
                addXMLWrapper(hl7);
            }
        }
        
        return hl7.toString();
    }


	public Map<String, ProviderLabRoutingModel> getProviderLabRoutingQueue() {
		if(providerLabRoutingQueue == null) {
			providerLabRoutingQueue = new HashMap<>();
		}
		return providerLabRoutingQueue;
	}

	private void acknowldegeLab(LaboratoryResultsDocument.LaboratoryResults lab) {

		/*
		 * PhysiciansNotes are notes that are added to the lab
		 * when the physician reviews the lab results.
		 * Some notes are added to every single OBX line in an unstructured lab
		 * result.
		 */
		String annotation = lab.getPhysiciansNotes();
		if (StringUtils.filled(annotation) && ! reviewerComment.toString().contains(annotation)) {
			reviewerComment.append(" ").append(lab.getPhysiciansNotes());
		}

		/*
		 * Extract the reviewers from the lab result.
		 * These are the providers that have reviewed and acknowledged the lab result
		 */
		Set<String> currentReviewer = null;
		for(LaboratoryResultsDocument.LaboratoryResults.ResultReviewer resultReviewer : lab.getResultReviewerArray()) {
			if(currentReviewer == null) {
				currentReviewer = new HashSet<>();
			}

			Date reviewDate = Util.dateTimeFPtoDate(resultReviewer.getDateTimeResultReviewed(),0);
			String reviewerId = resultReviewer.getOHIPPhysicianId();
			Provider provider = null;

			if(reviewerId != null && ! currentReviewer.contains(reviewerId)) {

				String reviewer = "";

				List<Provider> providerList = providerManager.getProvidersByOHIP(reviewerId);

				// use MRP if no results.
				if(providerList != null && ! providerList.isEmpty()) {
					provider = providerList.get(0);
				}

				if(provider != null) {
					reviewer = provider.getProviderNo();
				}

				currentReviewer.add(reviewerId);


				String status = StringUtils.filled(reviewer) ? "A" : "N";
				reviewer = status.equals("A") ? reviewer : "0";

				// reviewers are created without a lab number as this is not known until the lab is created
				getProviderLabRoutingQueue().put(reviewer, new ProviderLabRoutingModel(reviewer, null, status, reviewerComment.toString(), reviewDate, "HL7"));
			}
		}
	}


    private String generateMSH(LaboratoryResultsDocument.LaboratoryResults lab) {
        String labName = "";
        if (StringUtils.filled(lab.getLaboratoryName())) {
            if (lab.getLaboratoryName().split("\\^").length > 1) {
                labName = StringUtils.noNull(lab.getLaboratoryName().split("\\^")[0]);
            } else {
                labName = StringUtils.noNull(lab.getLaboratoryName());
            }
        }
        String labNameType = LAB_TYPE + "|" + labName;
        DateTimeFullOrPartial labDateString = lab.getLabRequisitionDateTime() != null ? lab.getLabRequisitionDateTime() : lab.getCollectionDateTime();
        String requisitionDate = getDateTime(labDateString);
        String version = "2.3";
        if (LAB_TYPE.equals("MDS")) {
            labNameType = labName + "|" + LAB_TYPE;
            version = version + ".0";
        }
        if (LAB_TYPE.equals("ExcellerisON")) {
            labNameType = "PATHL7" + "|" + labName;
            version = version + ".1";
        }
        
        return "MSH|^~\\&|" + labNameType + "|||" + requisitionDate + "||ORU^R01|" + StringUtils.noNull(lab.getAccessionNumber()) + "-" + resultCount + "|P|" + version + "||||";
    }
    
    private String generateNTE(LaboratoryResultsDocument.LaboratoryResults lab) {
        StringBuilder nte = new StringBuilder();
        
        if (StringUtils.filled(lab.getNotesFromLab())) {
            if (LAB_TYPE.equals("MDS")) {
                nte.append("NTE||MC|^").append(lab.getLabTestCode()).append("\n");
            } else {
                String[] noteParts = lab.getNotesFromLab().split("\n");

                StringBuilder nteSegment = new StringBuilder();
                for (int n = 0; n < noteParts.length; n++) {
                    int noteNum = (n + 1);
                    nteSegment.append("NTE|" + noteNum+ "|L|" + noteParts[n]).append("\n");
                }

                nte.append(nteSegment.toString());
            }
        }
        
        return nte.toString();
    }
    
    private String generateOBR(LaboratoryResultsDocument.LaboratoryResults lab) {
        DateTimeFullOrPartial reqDate = lab.getLabRequisitionDateTime();
        DateTimeFullOrPartial collectDate = lab.getCollectionDateTime();
        String requisitionDate = getDateTime(reqDate != null ? reqDate : collectDate);
        String collectionDate = getDateTime(collectDate != null ? collectDate : reqDate);
        String orderObservation = "1";
        
        if (!LAB_TYPE.equals("GDML")) {
            orderObservation = "1";
        }

		String labTestCode = lab.getLabTestCode();
		String testNameReportedByLab = lab.getTestNameReportedByLab();
		if(! StringUtils.filled(labTestCode)) {
			labTestCode = "000000";
		}
        if(StringUtils.filled(testNameReportedByLab)) {
	        testNameReportedByLab = lab.getTestName();
        }
        return "OBR|" + orderObservation + "|101||" + labTestCode + "^" + testNameReportedByLab + "|R|" + requisitionDate + "|" + collectionDate + "|||||||" + requisitionDate + "||||||||" + collectionDate + "||LAB|F|||";
    }
    
    private String generateOBX(List<LaboratoryResultsDocument.LaboratoryResults> labs) {
        int obxNo = 0;
        StringBuilder obx = new StringBuilder();

        for (LaboratoryResultsDocument.LaboratoryResults lab : labs) {
            String result = "";
            String unit = "";
            String valueType = "ST";
            if (lab.getResult() != null) {
                result = StringUtils.noNull(lab.getResult().getValue());
                result = result.replaceAll("\\n", "<br \\\\>");
                if (isBase64Pdf(result)) {
                    if (!LAB_TYPE.equals("ExcellerisON")) { result = "^TEXT^PDF^Base64^" + result; } 
                    valueType = "ED";
                }

                unit = StringUtils.noNull(lab.getResult().getUnitOfMeasure());
            }
            String collectionDate = getDateTime(lab.getCollectionDateTime());
            String referenceRange = "";
            String resultNormalAbnormalFlag = "";
            String testResultStatus = StringUtils.noNull(lab.getTestResultStatus());
            
            if (lab.getResultNormalAbnormalFlag() != null) {
                if(lab.getResultNormalAbnormalFlag().isSetResultNormalAbnormalFlagAsPlainText()) {
                    resultNormalAbnormalFlag = lab.getResultNormalAbnormalFlag().getResultNormalAbnormalFlagAsPlainText();
                } else if (lab.getResultNormalAbnormalFlag().isSetResultNormalAbnormalFlagAsEnum()) {
                    resultNormalAbnormalFlag = lab.getResultNormalAbnormalFlag().getResultNormalAbnormalFlagAsEnum().toString();
                }
            }
            if (lab.getReferenceRange() != null) {
                if (lab.getReferenceRange().getReferenceRangeText() != null) {
                    referenceRange = lab.getReferenceRange().getReferenceRangeText();
                } else if (lab.getReferenceRange().getLowLimit() != null && lab.getReferenceRange().getHighLimit() != null){
                    referenceRange = lab.getReferenceRange().getLowLimit() + "-" + lab.getReferenceRange().getHighLimit();
                }
            }
            
            obxNo++;
            String labTest = lab.getLabTestCode() + "^" + lab.getTestNameReportedByLab() + "^" + lab.getTestName();
            
            if (isFinal(testResultStatus)) {
                testResultStatus = "F";
            }
            
            String obxSegment = "OBX|" + obxNo + "|" + valueType + "|" + labTest+ "|Imported Test Results|" + result+ "|" +unit+ "|" + referenceRange + "|" + resultNormalAbnormalFlag+ "|||" + testResultStatus + "|||" + collectionDate;
            obx.append(obxSegment).append("\n");

			// also generates the NTE segments (laboratory comments)
            obx.append(generateNTE(lab));

			// creates a list of acknowledging providers on the lab report
			acknowldegeLab(lab);
        }

        return obx.toString();
    }

    private String generateORC(LaboratoryResultsDocument.LaboratoryResults lab) {
        String collectionDate = getDateTime(lab.getCollectionDateTime());
        String testResultStatus = StringUtils.noNull(lab.getTestResultStatus());
        if (isFinal(testResultStatus)) {
            testResultStatus = "F";
        }
        
        return "ORC|RE|" + lab.getAccessionNumber() + "|" + lab.getAccessionNumber() + "||" +testResultStatus+ "||||||||||" + collectionDate;
    }

    private String generatePID(Demographic demographic, LaboratoryResultsDocument.LaboratoryResults lab) {
        String demographicPhone =  StringUtils.noNull(demographic.getPhone());
        String demographicPhone2 = StringUtils.noNull(demographic.getPhone2());
        String healthCard = StringUtils.noNull(demographic.getHin());
        String pid19 = healthCard + " " + StringUtils.noNull(demographic.getVer());
        if (LAB_TYPE.equals("MDS")) {
            pid19 = "X" + healthCard;
        }
        
        return "PID|1|" + StringUtils.noNull(demographic.getHin()) + "|" + lab.getAccessionNumber() + "|" +healthCard + "|" + demographic.getLastName() + "^" + demographic.getFirstName() + "||" + fullDate.format(demographic.getBirthDay().getTime()) + "|" + demographic.getSex() + "|||||" + demographicPhone + "|" + demographicPhone2+ "|||||" + pid19;
    }

    private String generateZFR(LaboratoryResultsDocument.LaboratoryResults lab){
        String testResultStatus = StringUtils.noNull(lab.getTestResultStatus());
        if (isFinal(testResultStatus)) {
            testResultStatus = "1";
        } else {
            testResultStatus = "0";
        }
        
        return "ZFR||1|" + testResultStatus + "|||0|0";
    }

    private String generateZMC(List<LaboratoryResultsDocument.LaboratoryResults> labs){
        StringBuilder zmc = new StringBuilder();
        Integer zmcNo = 0;
        
        for (LaboratoryResultsDocument.LaboratoryResults lab : labs) {
            zmcNo += 1;
            if (StringUtils.filled(lab.getNotesFromLab())) {
                String[] noteParts = lab.getNotesFromLab().split("\n");

                StringBuilder zmcSegment = new StringBuilder();
                for (int n = 0; n < noteParts.length; n++) {
                    int noteNum = (n + 1);
                    zmcSegment.append("ZMC|" + zmcNo + "." + (n + 1) + "|" + lab.getLabTestCode() + "||" + noteParts.length+ "|Y|" + noteParts[n]).append("\n");
                }
                
                zmc.append(zmcSegment.toString());
            }
        }

        return zmc.toString();
    }
    
    private String generateZMN(List<LaboratoryResultsDocument.LaboratoryResults> labs){
        StringBuilder zmn = new StringBuilder();

        for (LaboratoryResultsDocument.LaboratoryResults lab : labs) {
            if (lab.getResult() != null) {
                String referenceRange = "";
                String unit = StringUtils.noNull(lab.getResult().getUnitOfMeasure());
                
                if (lab.getReferenceRange() != null) {
                    if (lab.getReferenceRange().getReferenceRangeText() != null) {
                        referenceRange = lab.getReferenceRange().getReferenceRangeText();
                    } else if (lab.getReferenceRange().getLowLimit() != null && lab.getReferenceRange().getHighLimit() != null){
                        referenceRange = lab.getReferenceRange().getLowLimit() + "-" + lab.getReferenceRange().getHighLimit();
                    }
                }
                
                String zmnSegment = "ZMN||" + lab.getTestNameReportedByLab() + "||" + lab.getTestName() + "|" +unit+ "||" + referenceRange + "|Imported Test Results||" + lab.getLabTestCode();

                zmn.append(zmnSegment).append("\n");
            }
        }

        return zmn.toString();
    }

    private String generateZRG(List<LaboratoryResultsDocument.LaboratoryResults> labs){
        StringBuilder zrg = new StringBuilder();
        int zrgNo = 0;
        
        for (LaboratoryResultsDocument.LaboratoryResults lab : labs) {
            if (lab.getResult() != null) {
                zrgNo += 1;
                String zrgSegment = "ZRG|" + zrgNo + ".1|" + lab.getLabTestCode() + "|||Imported Test Results|1|";
                zrg.append(zrgSegment).append("\n");
            }
        }

        return zrg.toString();
    }

    /**
     * Attempts to parse a Date object from the provided DateTimeFullOrPartial
     * @param dateObj The provided DateTimeFullOrPartial object
     * @return A parsed date string of the DateTimeFullOrPartial or if not parsable it takes the current Date()
     */
    private String getDateTime(DateTimeFullOrPartial dateObj) {
        Date date = null;
        if (dateObj != null) {
            SimpleDateFormat[] formats = { inputFormat, xmlTimezoneOffSetDateTime, inputDateOnlyFormat };
            for (SimpleDateFormat format : formats) {
                try {
                    if (dateObj.isSetFullDate()) {
                        date = format.parse(dateObj.getFullDate().toString() + " 00:00:00");
                    } else if (dateObj.isSetFullDateTime()) {
                        date = format.parse(dateObj.getFullDateTime().toString());
                    }
                } catch (ParseException e) { /* Do nothing */ }
                if (date != null) {
                    break;
                }
            }
        }
        if (date == null) {
            date = new Date();
        }
        
        return fullDateTime.format(date);
    }
    
    private boolean isFinal(String testResultStatus) {
        testResultStatus = StringUtils.noNull(testResultStatus);
        
        return testResultStatus.equalsIgnoreCase("Final") || testResultStatus.isEmpty();
    }

    private boolean isBase64Pdf(String str) {
        // Check if the string is null or empty
        if (str == null || str.isEmpty()) {
            return false; // Null or empty strings are not valid Base64
        }
    
        try {
            // Attempt to decode the string as Base64
            byte[] decodedBytes = Base64.getDecoder().decode(str);
    
            // Check if the decoded bytes represent a PDF file
            // PDF files start with the signature "%PDF-" (in ASCII)
            String pdfSignature = "%PDF-";
            if (decodedBytes.length < pdfSignature.length()) {
                return false; // Not enough bytes to match the PDF signature
            }
    
            // Convert the first few bytes to a string and compare with the PDF signature
            String header = new String(decodedBytes, 0, pdfSignature.length(), StandardCharsets.UTF_8);
            return pdfSignature.equals(header);
    
        } catch (IllegalArgumentException e) {
            // Decoding failed, so it's not valid Base64
            return false;
        } catch (Exception e) {
            // Handle unexpected exceptions (e.g., character encoding issues)
            return false;
        }
    }

    private void addXMLWrapper(StringBuilder hl7Message) {
        if (hl7Message == null || hl7Message.length() == 0) {
            throw new IllegalArgumentException("HL7 message cannot be null or empty");
        }
    
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                  .append("<HL7Messages MessageFormat=\"ORUR01\" MessageCount=\"1\" Version=\"2.3\">")
                  .append("<Message MsgID=\"1\"><![CDATA[")
                  .append(hl7Message)
                  .append("]]></Message>")
                  .append("</HL7Messages>");
    
        hl7Message.setLength(0); // Clear the original content
        hl7Message.append(xmlBuilder); // Replace it with the XML-wrapped content
    }

	public String getLabType() {
		return LAB_TYPE;
	}
}
