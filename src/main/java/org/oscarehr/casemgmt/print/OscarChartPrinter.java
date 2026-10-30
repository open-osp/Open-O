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


package org.oscarehr.casemgmt.print;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;


import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.DemographicCustDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.model.*;
import org.oscarehr.managers.PrescriptionManager;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.SxmlMisc;
import oscar.oscarClinic.ClinicData;
import oscar.oscarDemographic.data.DemographicRelationship;

/**
 * This will create a PDF + assemble e-forms,documents,labs into a package
 *
 * @author Marc Dumontier
 *
 */
public class OscarChartPrinter {

    public final int LINESPACING = 1;
    public final float LEADING = 12;
    public final float FONTSIZE = 10;
    public final int NUMCOLS = 2;

    private static final float MARGIN_SIDE = 40;
    private static final float MARGIN_TOP = 60;
    private static final float MARGIN_BOTTOM = 56;
    private static final float MIN_SECTION_SPACE = 72;
    private static final String NOT_RECORDED = "Not recorded";

    private static final BaseColor ACCENT = new BaseColor(31, 58, 96);
    private static final BaseColor TEXT_MUTED = new BaseColor(100, 100, 100);
    private static final BaseColor RULE = new BaseColor(190, 190, 190);
    private static final BaseColor HEADING_BG = new BaseColor(230, 235, 242);
    private static final BaseColor ROW_ALT_BG = new BaseColor(246, 247, 249);

    private Demographic demographic;
    private String signingProvider;

    private PdfWriter writer;

	private HttpServletRequest request;
    private OutputStream os;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
    private Document document;
    private BaseFont bf;
    private Font font, boldFont;
    private Font titleFont, sectionFont, subSectionFont, labelFont, smallFont, tableHeaderFont, notRecordedFont;
    private String clinicName = "";
    boolean newPage;

    private ProviderDao providerDao = (ProviderDao)SpringUtils.getBean(ProviderDao.class);
    private DemographicCustDao demographicCustDao = (DemographicCustDao)SpringUtils.getBean(DemographicCustDao.class);
    private DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean(DemographicDao.class);
    private OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean(OscarAppointmentDao.class);
    private PreventionDao preventionDao = (PreventionDao)SpringUtils.getBean(PreventionDao.class);
    private DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
    private TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);

	public OscarChartPrinter(OutputStream os) throws DocumentException, IOException {
		document = new Document(PageSize.LETTER, MARGIN_SIDE, MARGIN_SIDE, MARGIN_TOP, MARGIN_BOTTOM);
		// writer = PdfWriterFactory.newInstance(document, os, FontSettings.HELVETICA_10PT);

		writer = PdfWriter.getInstance(document, os);
		writer.setPageEvent(new EndPage());
		//Create the font we are going to print to
		bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		font = new Font(bf, FONTSIZE, Font.NORMAL);
		boldFont = new Font(bf, FONTSIZE, Font.BOLD);
		titleFont = new Font(bf, 15, Font.BOLD, ACCENT);
		sectionFont = new Font(bf, 11, Font.BOLD, ACCENT);
		subSectionFont = new Font(bf, FONTSIZE, Font.BOLD, ACCENT);
		labelFont = new Font(bf, 8.5f, Font.BOLD, TEXT_MUTED);
		smallFont = new Font(bf, 8.5f, Font.NORMAL, TEXT_MUTED);
		tableHeaderFont = new Font(bf, 9, Font.BOLD, BaseColor.WHITE);
		notRecordedFont = new Font(bf, FONTSIZE, Font.ITALIC, TEXT_MUTED);
		document.open();
	}

	public OscarChartPrinter(HttpServletRequest request, OutputStream os) throws DocumentException,IOException {
		this(os);
		this.request = request;
		this.os = os;
	}

	public OscarChartPrinter(Demographic demographic, OutputStream os) throws DocumentException, IOException {
		this(os);
		this.demographic = demographic;
		this.os = os;
	}

    public HttpServletRequest getRequest() {
    	return request;
    }

    public OutputStream getOutputStream() {
    	return os;
    }

    public void setDemographic(Demographic demographic) {
    	this.demographic=demographic;
    }

    public Font getFont() {
    	return font;
    }
    public SimpleDateFormat getFormatter() {
    	return formatter;
    }

    public Document getDocument() {
    	return document;
    }

    public boolean getNewPage() {
    	return newPage;
    }
    public void setNewPage(boolean b) {
    	this.newPage = b;
    }

    public BaseFont getBaseFont() {
    	return bf;
    }

    public void finish() {
        document.close();
    }


	public void printDocHeaderFooter() throws DocumentException {
        ResourceBundle propResource = ResourceBundle.getBundle("oscarResources");

        ClinicData clinicData = new ClinicData();
        clinicData.refreshClinicData();
        clinicName = nvl(clinicData.getClinicName());

        if( newPage ) {
            document.newPage();
            newPage=false;
        }

        PdfPTable table = new PdfPTable(new float[] {55f, 45f});
        table.setWidthPercentage(100f);

        //clinic details on the left
        Paragraph clinic = new Paragraph();
        clinic.add(new Phrase(clinicName + "\n", titleFont));
        String cityLine = joinNonBlank(", ", clinicData.getClinicCity(), clinicData.getClinicProvince());
        cityLine = joinNonBlank("  ", cityLine, clinicData.getClinicPostal());
        clinic.add(new Phrase(LEADING, joinNonBlank("\n", clinicData.getClinicAddress(), cityLine), smallFont));
        if(!isBlank(clinicData.getClinicPhone())) {
        	clinic.add(new Phrase(LEADING, "\nTel: " + clinicData.getClinicPhone(), smallFont));
        }
        PdfPCell cell = new PdfPCell();
        cell.addElement(clinic);
        styleHeaderCell(cell);
        table.addCell(cell);

        //patient summary on the right
        Paragraph patient = new Paragraph();
        patient.setAlignment(Element.ALIGN_RIGHT);
        patient.add(new Phrase(nvl(demographic.getFormattedName()) + "\n", titleFont));
        String line = propResource.getString("oscarEncounter.pdfPrint.dob") + " " + nvl(demographic.getFormattedDob())
        		+ "   " + propResource.getString("oscarEncounter.pdfPrint.age") + " " + nvl(demographic.getAge())
        		+ "   " + propResource.getString("oscarEncounter.pdfPrint.gender") + " " + nvl(demographic.getSex());
        patient.add(new Phrase(LEADING, line, font));
        String genderLine = joinNonBlank("   ",
        		isBlank(demographic.getGender()) ? null : "Gender: " + demographic.getGender(),
        		isBlank(demographic.getPronoun()) ? null : "Pronouns: " + demographic.getPronoun());
        if(!isBlank(genderLine)) {
        	patient.add(new Phrase(LEADING, "\n" + genderLine, font));
        }
        if(!isBlank(demographic.getHin())) {
        	patient.add(new Phrase(LEADING, "\n" + propResource.getString("oscarEncounter.pdfPrint.phn") + " "
        			+ joinNonBlank(" ", demographic.getHin(), demographic.getVer()), font));
        }
        String mrp = getProviderName(demographic.getProviderNo());
        patient.add(new Phrase(LEADING, "\n" + propResource.getString("oscarEncounter.pdfPrint.mrp") + " "
        		+ (isBlank(mrp) ? NOT_RECORDED : mrp), font));
        cell = new PdfPCell();
        cell.addElement(patient);
        styleHeaderCell(cell);
        table.addCell(cell);

        table.setSpacingAfter(4f);
        document.add(table);

        if(signingProvider != null) {
        	Paragraph signed = new Paragraph(LEADING, "Signed Provider: " + signingProvider, smallFont);
        	signed.setAlignment(Element.ALIGN_RIGHT);
        	document.add(signed);
        }
    }


	public void printMasterRecord() throws DocumentException {
		if( newPage ) {
            document.newPage();
            newPage=false;
        }

        addSectionHeading("Patient Information");

        addSubSectionHeading("Demographics");
        PdfPTable table = createFieldTable();
        //name
        addField(table, "Last Name", demographic.getLastName());
        addField(table, "First Name", demographic.getFirstName());
        addField(table, "Middle Name", demographic.getMiddleNames());
        addField(table, "Preferred Name", demographic.getPrefName());
        addRequiredField(table, "Title", demographic.getTitle());
        table.completeRow();
        //identity
        addField(table, "Date of Birth", demographic.getFormattedDob());
        addField(table, "Sex", demographic.getSex());
        addField(table, "Gender", demographic.getGender());
        addField(table, "Pronouns", demographic.getPronoun());
        table.completeRow();
        //language
        addField(table, "Official Language", demographic.getOfficialLanguage());
        addRequiredField(table, "Spoken Language", demographic.getSpokenLanguage());
        addFieldTable(table);

        addSubSectionHeading("Enrolment");
        table = createFieldTable();
        addRequiredField(table, "Roster Status", demographic.getRosterStatus());
        addField(table, "Date Rostered", formatDate(demographic.getRosterDate()));
        addField(table, "Patient Status", demographic.getPatientStatus());
        addRequiredField(table, "Chart No (MRN)", demographic.getChartNo());
        addField(table, "Date Joined", formatDate(demographic.getDateJoined()));
        addField(table, "End Date", formatDate(demographic.getEndDate()));
        addFieldTable(table);

        List<DemographicExt> exts = demographicExtDao.getDemographicExtByDemographicNo(demographic.getDemographicNo());
        String phoneExt = null;
        String cell = null;
        for(DemographicExt ext:exts) {
        	if(ext.getKey().equals("wPhoneExt")) {
        		phoneExt = ext.getValue();
        	}
        	if(ext.getKey().equals("demo_cell")) {
        		cell = ext.getValue();
        	}
        }

        addSubSectionHeading("Contact");
        table = createFieldTable();
        addField(table, "Address", demographic.getAddress());
        addField(table, "City", demographic.getCity());
        addField(table, "Province", demographic.getProvince());
        addField(table, "Postal Code", demographic.getPostal());
        addField(table, "Phone", demographic.getPhone());
        addField(table, "Work Phone", isBlank(phoneExt) ? demographic.getPhone2() : nvl(demographic.getPhone2()) + " ext: " + phoneExt);
        addField(table, "Cell Phone", cell);
        addField(table, "Email", demographic.getEmail());
        addFieldTable(table);

        addSubSectionHeading("Health Insurance");
        table = createFieldTable();
        addRequiredField(table, "Health Insurance #", demographic.getHin());
        addRequiredField(table, "Version Code", demographic.getVer());
        addField(table, "HC Type", demographic.getHcType());
        addField(table, "Eff Date", formatDate(demographic.getEffDate()));
        addFieldTable(table);

        DemographicCust demographicCust = demographicCustDao.find(demographic.getDemographicNo());

        addSubSectionHeading("Care Team");
        table = createFieldTable();
        addRequiredField(table, "Physician", getProviderName(demographic.getProviderNo()));
        if(demographicCust != null) {
	        addField(table, "Nurse", getProviderName(demographicCust.getNurse()));
	        addField(table, "Midwife", getProviderName(demographicCust.getMidwife()));
	        addField(table, "Resident", getProviderName(demographicCust.getResident()));
        }
        addField(table, "Referral Doctor", getReferralDoctor(demographic.getFamilyDoctor()));
        addFieldTable(table);

        //alerts & notes
        if(demographicCust != null) {
        	String alert = demographicCust.getAlert();
        	String notes = (demographicCust.getNotes() != null && demographicCust.getNotes().length() > 0)
        			? SxmlMisc.getXmlContent(demographicCust.getNotes(),"unotes") : null;
        	if(!isBlank(alert) || !isBlank(notes)) {
        		addSubSectionHeading("Alerts & Notes");
        		table = createFieldTable(new float[] {18f, 82f});
        		addField(table, "Alerts", alert);
        		addField(table, "Notes", notes);
        		addFieldTable(table);
        	}
        }

        //relationships
        DemographicRelationship demoRel = new DemographicRelationship();
		List<Map<String,String>> demoR = demoRel.getDemographicRelationships(String.valueOf(demographic.getDemographicNo()));
		if(demoR != null && !demoR.isEmpty()) {
			addSubSectionHeading("Relationships");
			table = createDataTable(new float[] {16f, 26f, 14f, 14f, 30f}, "Relation", "Name", "Substitute Decision Maker", "Emergency Contact", "Notes");
			for (int j=0; j<demoR.size(); j++) {
			    Map<String,String> r = demoR.get(j);
			    Demographic relationDemographic = demographicDao.getClientByDemographicNo(Integer.parseInt(r.get("demographic_no")));
			    addDataRow(table, j,
			    		r.get("relation"),
			    		relationDemographic != null ? relationDemographic.getFormattedName() : "",
			    		yesNo(r.get("sub_decision_maker")),
			    		yesNo(r.get("emergency_contact")),
			    		r.get("notes"));
			}
			document.add(table);
		}
	}

	private String getReferralDoctor(String field) {
		if(field!=null&&field.length()>0) {
			return SxmlMisc.getXmlContent(field,"rd") ;
		}
		return "";
	}

	private String getProviderName(String providerNo) {
		if(providerNo == null || providerNo.length()==0) {
			return "";
		}
		Provider p = providerDao.getProvider(providerNo);
		if(p != null) {
			return p.getFormattedName();
		}
		return "";
	}


	public void printAppointmentHistory() throws DocumentException {
		if( newPage ) {
            document.newPage();
            newPage=false;
        }

		List<Appointment> appts = appointmentDao.getAppointmentHistory(demographic.getDemographicNo());

		addSectionHeading("Appointment History");

		if(appts == null || appts.isEmpty()) {
			addEmptyNotice("No appointments on record.");
			return;
		}

		PdfPTable table = createDataTable(new float[] {13f, 8f, 8f, 25f, 18f, 28f}, "Date", "From", "To", "Reason", "Provider", "Notes");
		int row = 0;
        for(Appointment appt:appts) {
	        Provider apptProvider = providerDao.getProvider(appt.getProviderNo());
	        addDataRow(table, row++,
	        		formatDate(appt.getAppointmentDate()),
	        		appt.getStartTime() != null ? timeFormatter.format(appt.getStartTime()) : "",
	        		appt.getEndTime() != null ? timeFormatter.format(appt.getEndTime()) : "",
	        		appt.getReason(),
	        		apptProvider != null ? apptProvider.getFormattedName() : appt.getProviderNo(),
	        		appt.getNotes());
        }

        document.add(table);
	}


    public void printCPPItem(String heading, Collection<CaseManagementNote> notes) throws DocumentException {
        if( newPage )
            document.newPage();
      //  else
      //      newPage = true;

        addSectionHeading(heading);
        newPage = false;
        this.printNotes(notes,true);

    }


    public void printNotes(Collection<CaseManagementNote>notes, boolean compact) throws DocumentException{

        for(CaseManagementNote note : notes) {
            Paragraph p = new Paragraph();
            p.setLeading(LEADING);
            p.setSpacingAfter(compact ? 4f : 8f);

            if(compact) {
            	p.add(new Chunk(formatDate(note.getObservation_date()) + "   ", boldFont));
            } else {
            	p.add(new Chunk("Impression/Plan (" + formatDate(note.getObservation_date()) + ")\n", subSectionFont));
            }
            p.add(new Chunk(nvl(note.getNote()).trim(), font));
            document.add(p);
        }
    }

    public void printBlankLine() throws DocumentException {
    	Paragraph p = new Paragraph(LEADING, " ", font);
    	p.setSpacingAfter(2f);
    	document.add(p);
    }

    /**
     * Prints a full width section heading, used to start each major section of the chart.
     */
    public void addSectionHeading(String title) throws DocumentException {
    	//don't strand a heading at the bottom of a page without any of its content
    	if(writer.getVerticalPosition(true) - document.bottom() < MIN_SECTION_SPACE) {
    		document.newPage();
    	}

    	PdfPTable table = new PdfPTable(1);
    	table.setWidthPercentage(100f);
    	table.setSpacingBefore(10f);
    	table.setSpacingAfter(6f);

    	PdfPCell cell = new PdfPCell(new Phrase(title.toUpperCase(), sectionFont));
    	cell.setBackgroundColor(HEADING_BG);
    	cell.setBorder(Rectangle.LEFT);
    	cell.setBorderColor(ACCENT);
    	cell.setBorderWidthLeft(3f);
    	cell.setPaddingTop(4f);
    	cell.setPaddingBottom(6f);
    	cell.setPaddingLeft(8f);
    	table.addCell(cell);

    	document.add(table);
    }

    private void addSubSectionHeading(String title) throws DocumentException {
    	PdfPTable table = new PdfPTable(1);
    	table.setWidthPercentage(100f);
    	table.setSpacingBefore(4f);
    	table.setSpacingAfter(2f);

    	PdfPCell cell = new PdfPCell(new Phrase(title, subSectionFont));
    	cell.setBorder(Rectangle.BOTTOM);
    	cell.setBorderColor(RULE);
    	cell.setBorderWidthBottom(0.5f);
    	cell.setPaddingLeft(0f);
    	cell.setPaddingBottom(3f);
    	table.addCell(cell);

    	document.add(table);
    }

    private void addEmptyNotice(String text) throws DocumentException {
    	Paragraph p = new Paragraph(LEADING, text, smallFont);
    	p.setIndentationLeft(8f);
    	document.add(p);
    }

    private void styleHeaderCell(PdfPCell cell) {
    	cell.setBorder(Rectangle.BOTTOM);
    	cell.setBorderColor(ACCENT);
    	cell.setBorderWidthBottom(1.5f);
    	cell.setPaddingLeft(0f);
    	cell.setPaddingRight(0f);
    	cell.setPaddingBottom(8f);
    }

    /**
     * A label/value grid laid out as two label/value pairs per row.
     */
    private PdfPTable createFieldTable() throws DocumentException {
    	return createFieldTable(new float[] {18f, 32f, 18f, 32f});
    }

    private PdfPTable createFieldTable(float[] widths) throws DocumentException {
    	PdfPTable table = new PdfPTable(widths);
    	table.setWidthPercentage(100f);
    	table.setSpacingAfter(4f);
    	//completeRow() pads with default cells, which must not draw borders
    	table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
    	return table;
    }

    private void addField(PdfPTable table, String label, String value) {
    	if(isBlank(value)) {
    		return;
    	}
    	addFieldCells(table, label, new Phrase(value.trim(), font));
    }

    /**
     * Like addField, but a blank value is printed as "Not recorded" so missing chart data is visible.
     */
    private void addRequiredField(PdfPTable table, String label, String value) {
    	if(isBlank(value)) {
    		addFieldCells(table, label, new Phrase(NOT_RECORDED, notRecordedFont));
    	} else {
    		addField(table, label, value);
    	}
    }

    private void addFieldCells(PdfPTable table, String label, Phrase value) {
    	PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
    	labelCell.setBorder(Rectangle.NO_BORDER);
    	labelCell.setPadding(2f);
    	labelCell.setPaddingLeft(0f);
    	table.addCell(labelCell);

    	PdfPCell valueCell = new PdfPCell(value);
    	valueCell.setBorder(Rectangle.NO_BORDER);
    	valueCell.setPadding(2f);
    	table.addCell(valueCell);
    }

    private void addFieldTable(PdfPTable table) throws DocumentException {
    	table.completeRow();
    	if(table.size() > 0) {
    		document.add(table);
    	} else {
    		addEmptyNotice("No information recorded.");
    	}
    }

    /**
     * A bordered table with a shaded header row that repeats across pages.
     */
    private PdfPTable createDataTable(float[] widths, String... headers) throws DocumentException {
    	PdfPTable table = new PdfPTable(widths);
    	table.setWidthPercentage(100f);
    	table.setSpacingAfter(6f);
    	table.setHeaderRows(1);
    	for(String header : headers) {
    		PdfPCell cell = new PdfPCell(new Phrase(header, tableHeaderFont));
    		cell.setBackgroundColor(ACCENT);
    		cell.setBorderColor(ACCENT);
    		cell.setPadding(4f);
    		cell.setPaddingBottom(5f);
    		table.addCell(cell);
    	}
    	return table;
    }

    private void addDataRow(PdfPTable table, int rowIndex, String... values) {
    	for(String value : values) {
    		PdfPCell cell = new PdfPCell(new Phrase(nvl(value), font));
    		cell.setBorder(Rectangle.BOTTOM);
    		cell.setBorderColor(RULE);
    		cell.setBorderWidthBottom(0.5f);
    		cell.setPadding(4f);
    		if(rowIndex % 2 == 1) {
    			cell.setBackgroundColor(ROW_ALT_BG);
    		}
    		table.addCell(cell);
    	}
    }


    public void printAllergies(List<Allergy> allergies) throws DocumentException {

        addSectionHeading("Allergies");

        PdfPTable table = createDataTable(new float[] {30f, 45f, 12f, 13f}, "Allergen", "Reaction", "Severity", "Start Date");
        int row = 0;
        for(Allergy allergy:allergies) {
        	String startDate = formatDate(allergy.getStartDate());
        	addDataRow(table, row++, allergy.getDescription(), allergy.getReaction(),
        			isBlank(allergy.getSeverityOfReaction()) ? NOT_RECORDED : allergy.getSeverityOfReactionDesc(),
        			isBlank(startDate) ? NOT_RECORDED : startDate);
        }
        document.add(table);
    }


    public void printRx(LoggedInInfo loggedInInfo, String demoNo) throws DocumentException {
        printRx(loggedInInfo, demoNo,null);
    }

    public void printRx(LoggedInInfo loggedInInfo, String demoNo,List<CaseManagementNote> cpp) throws DocumentException {
        if( demoNo == null )
            return;
        /*
        if( newPage )
            document.newPage();
        else
            newPage = true;
        */

	    PrescriptionManager prescriptionManager = SpringUtils.getBean(PrescriptionManager.class);
	    List<Drug> drugList = prescriptionManager.getUniqueDrugsByPatient(loggedInInfo, Integer.parseInt(demoNo));

        if(drugList == null || drugList.isEmpty()) {
        	return;
        }

        addSectionHeading("Prescriptions");

        PdfPTable table = createDataTable(new float[] {13f, 61f, 13f, 13f}, "Rx Date", "Prescription", "End Date", "Status");
        int row = 0;
        for(Drug drug : drugList) {
        	addDataRow(table, row++,
        			formatDate(drug.getRxDate()),
        			nvl(drug.getFullOutLine()).replace(";", " "),
        			formatDate(drug.getEndDate()),
        			getDrugStatus(drug));
        }
        document.add(table);
    }

    private String getDrugStatus(Drug drug) {
    	if(drug.isArchived()) {
    		return "Archived";
    	}
    	if(drug.isDiscontinued()) {
    		return "Discontinued";
    	}
    	if(drug.isLongTerm()) {
    		return "Long Term";
    	}
    	return drug.isCurrent() ? "Current" : "Expired";
    }


    public void printPreventions() throws DocumentException {
        List<Prevention> preventions = preventionDao.findNotDeletedByDemographicId(demographic.getDemographicNo());

        if(preventions.size()==0) {
        	return;
        }

        addSectionHeading("Preventions");

        PdfPTable table = createDataTable(new float[] {70f, 30f}, "Prevention", "Date");
        int row = 0;
        for(Prevention prevention:preventions) {
        	addDataRow(table, row++, prevention.getPreventionType(), formatDate(prevention.getPreventionDate()));
        }
        document.add(table);
    }

    public void printTicklers(LoggedInInfo loggedInInfo) throws DocumentException {
    	CustomFilter filter = new CustomFilter();
    	filter.setDemographicNo(String.valueOf(demographic.getDemographicNo()));
    	List<Tickler> ticklers = ticklerManager.getTicklers(loggedInInfo, filter);

    	if(ticklers.size()==0) {
    		return;
    	}

    	addSectionHeading("Ticklers");

    	PdfPTable table = createDataTable(new float[] {12f, 9f, 10f, 15f, 15f, 39f}, "Service Date", "Priority", "Status", "Provider", "Assignee", "Message");
    	int row = 0;
        for(Tickler tickler:ticklers) {
        	addDataRow(table, row++,
        			tickler.getServiceDateWeb(),
        			tickler.getPriority() != null ? tickler.getPriority().toString() : "",
        			getTicklerStatus(tickler.getStatus()),
        			tickler.getProvider() != null ? tickler.getProvider().getFormattedName() : "",
        			tickler.getAssignee() != null ? tickler.getAssignee().getFormattedName() : "",
        			tickler.getMessage());
        }
        document.add(table);
    }

    private String getTicklerStatus(Tickler.STATUS status) {
    	if(status == null) {
    		return "";
    	}
    	switch(status) {
    		case A: return "Active";
    		case C: return "Completed";
    		case D: return "Deleted";
    		default: return status.toString();
    	}
    }

    public void printDiseaseRegistry() throws DocumentException {
    	DxresearchDAO dxDao = (DxresearchDAO)SpringUtils.getBean(DxresearchDAO.class);
    	IssueDAO issueDao = (IssueDAO)SpringUtils.getBean(IssueDAO.class);

    	List<Dxresearch> dxs = dxDao.getDxResearchItemsByPatient(demographic.getDemographicNo());

    	if(dxs.size()==0) {
    		return;
    	}

    	addSectionHeading("Disease Registry");

    	PdfPTable table = createDataTable(new float[] {14f, 56f, 20f, 10f}, "Start Date", "Issue", "Code", "Status");
    	int row = 0;
        for(Dxresearch dx:dxs) {
        	String codingSystem = dx.getCodingSystem();
        	String code = dx.getDxresearchCode();
        	Issue issue = issueDao.findIssueByTypeAndCode(codingSystem, code);

        	addDataRow(table, row++,
        			formatDate(dx.getStartDate()),
        			issue != null ? issue.getDescription() : "<Unknown>",
        			joinNonBlank(" ", codingSystem, code),
        			getDxStatus(dx.getStatus()));
        }
        document.add(table);
    }

    private String getDxStatus(Character status) {
    	if(status == null) {
    		return "";
    	}
    	switch(status) {
    		case 'A': return "Active";
    		case 'C': return "Resolved";
    		case 'D': return "Deleted";
    		default: return String.valueOf(status);
    	}
    }

    public void printCurrentAdmissions() throws DocumentException {
    	AdmissionDao admissionDao = (AdmissionDao)SpringUtils.getBean(AdmissionDao.class);
    	ProgramDao programDao = (ProgramDao)SpringUtils.getBean(ProgramDao.class);

    	List<Admission> admissions = admissionDao.getCurrentAdmissions(demographic.getDemographicNo());

    	if(admissions.size()==0) {
    		return;
    	}

    	addSectionHeading("Current Admissions");

    	PdfPTable table = createDataTable(new float[] {25f, 12f, 18f, 13f, 32f}, "Program", "Type", "Provider", "Admitted", "Admission Notes");
    	int row = 0;
        for(Admission admission:admissions) {
        	String providerName = getProviderName(admission.getProviderNo());
        	addDataRow(table, row++,
        			programDao.getProgramName(admission.getProgramId()),
        			admission.getProgramType(),
        			isBlank(providerName) ? "unknown" : providerName,
        			admission.getAdmissionDate("yyyy-MM-dd"),
        			admission.getAdmissionNotes());
        }
        document.add(table);
    }

    public void printPastAdmissions() throws DocumentException {
    	AdmissionDao admissionDao = (AdmissionDao)SpringUtils.getBean(AdmissionDao.class);
    	ProgramDao programDao = (ProgramDao)SpringUtils.getBean(ProgramDao.class);

    	List<Admission> admissions = admissionDao.getAdmissions(demographic.getDemographicNo());
    	admissions = filterOutCurrentAdmissions(admissions);

    	if(admissions.size()==0) {
    		return;
    	}

    	addSectionHeading("Past Admissions");

    	PdfPTable table = createDataTable(new float[] {18f, 9f, 15f, 11f, 18f, 11f, 18f},
    			"Program", "Type", "Provider", "Admitted", "Admission Notes", "Discharged", "Discharge Notes");
    	int row = 0;
        for(Admission admission:admissions) {
        	String providerName = getProviderName(admission.getProviderNo());
        	addDataRow(table, row++,
        			programDao.getProgramName(admission.getProgramId()),
        			admission.getProgramType(),
        			isBlank(providerName) ? "unknown" : providerName,
        			admission.getAdmissionDate("yyyy-MM-dd"),
        			admission.getAdmissionNotes(),
        			formatDate(admission.getDischargeDate()),
        			admission.getDischargeNotes());
        }
        document.add(table);
    }

    public void printCurrentIssues() throws  DocumentException {
    	CaseManagementIssueDAO cmIssueDao = (CaseManagementIssueDAO)SpringUtils.getBean(CaseManagementIssueDAO.class);

    	List<CaseManagementIssue> issues = cmIssueDao.getIssuesByDemographic(String.valueOf(demographic.getDemographicNo()));

    	addSectionHeading("Current Issues");

    	if(issues == null || issues.isEmpty()) {
    		addEmptyNotice("No current issues.");
    		return;
    	}

        for(CaseManagementIssue issue:issues) {
        	Paragraph p = new Paragraph(LEADING, "•  " + nvl(issue.getIssue().getDescription()), font);
        	p.setIndentationLeft(8f);
        	p.setSpacingAfter(2f);
        	document.add(p);
        }

    }

    private List<Admission> filterOutCurrentAdmissions(List<Admission> admissions) {
    	List<Admission> results = new ArrayList<Admission>();
    	for(Admission a:admissions) {
    		if(!a.getAdmissionStatus().equals("current")) {
    			results.add(a);
    		}
    	}
    	return results;
    }

    private String formatDate(Date date) {
    	return date != null ? formatter.format(date) : "";
    }

    private static String nvl(String value) {
    	return (value == null || "null".equalsIgnoreCase(value)) ? "" : value;
    }

    private static boolean isBlank(String value) {
    	return nvl(value).trim().isEmpty();
    }

    private static String yesNo(String value) {
    	if(isBlank(value)) {
    		return "";
    	}
    	String v = value.trim();
    	return ("1".equals(v) || "true".equalsIgnoreCase(v) || "yes".equalsIgnoreCase(v) || "y".equalsIgnoreCase(v)) ? "Yes" : "No";
    }

    private static String joinNonBlank(String separator, String... values) {
    	StringBuilder sb = new StringBuilder();
    	for(String value : values) {
    		if(isBlank(value)) {
    			continue;
    		}
    		if(sb.length() > 0) {
    			sb.append(separator);
    		}
    		sb.append(value.trim());
    	}
    	return sb.toString();
    }

    /*
     *Used to print the running header and footer on each page
     */
    class EndPage extends PdfPageEventHelper {
        private Date now;
        private String promoTxt;
        private PdfTemplate totalPages;
        private int lastPageNumber;

        public EndPage() {
            now = new Date();
            promoTxt = OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT");
            if( promoTxt == null ) {
                promoTxt = new String();
            }
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            totalPages = writer.getDirectContent().createTemplate(30, 12);
        }

        @Override
        public void onEndPage( PdfWriter writer, Document document ) {
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();

            float left = document.left();
            float right = document.right();
            lastPageNumber = writer.getPageNumber();

            //Running header with patient identifiers on every page after the first
            if(lastPageNumber > 1 && demographic != null) {
                float headerBase = document.top() + 16;
                String patient = joinNonBlank("   |   ", demographic.getFormattedName(),
                		isBlank(demographic.getFormattedDob()) ? null : "DOB: " + demographic.getFormattedDob(),
                		isBlank(demographic.getHin()) ? null : "HIN: " + demographic.getHin());
                ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(patient, labelFont), left, headerBase, 0);
                ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, new Phrase(clinicName, smallFont), right, headerBase, 0);
                drawRule(cb, left, right, headerBase - 5);
            }

            //Footer with print date and page numbers
            float footerBase = document.bottom() - 24;
            drawRule(cb, left, right, footerBase + 11);

            String strFooter = joinNonBlank("   |   ", "Confidential", "Printed " + formatter.format(now), promoTxt);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(strFooter, smallFont), left, footerBase, 0);

            //trailing whitespace is trimmed by showTextAligned, so leave a gap before the total page template
            float totalWidth = 14f;
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, new Phrase("Page " + lastPageNumber + " of", smallFont), right - totalWidth - 3f, footerBase, 0);
            cb.addTemplate(totalPages, right - totalWidth, footerBase);

            cb.restoreState();
        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(totalPages, Element.ALIGN_LEFT, new Phrase(String.valueOf(lastPageNumber), smallFont), 0, 0, 0);
        }

        private void drawRule(PdfContentByte cb, float left, float right, float y) {
            cb.setColorStroke(RULE);
            cb.setLineWidth(0.5f);
            cb.moveTo(left, y);
            cb.lineTo(right, y);
            cb.stroke();
        }
    }


}
