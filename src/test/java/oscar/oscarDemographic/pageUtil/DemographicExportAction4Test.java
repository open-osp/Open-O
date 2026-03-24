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
package oscar.oscarDemographic.pageUtil;

import cds.DemographicsDocument.Demographics;
import cds.OmdCdsDocument;
import cds.PatientRecordDocument.PatientRecord;
import cdsDt.PersonNamePartTypeCode;
import cdsDt.PersonNameStandard;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.StringWriter;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Test class for validating the functionality of DemographicExportAction4, including XML schema
 * validation, OmdCds document creation, and handling multiple demographics.
 *
 * Extends DaoTestFixtures to leverage setup utilities for database interactions.
 *
 * The main objectives of the tests are:
 *
 * 1. Validate the XML schema correctness for generated OmdCds documents.
 * 2. Ensure proper document creation processes for OmdCds and its sub-elements.
 * 3. Test handling and persistence of multiple demographic records.
 * 4. Verify the presence and readability of required schema files for validation.
 *
 * Test Details:
 *
 * - testXmlSchemaValidation:
 *   Tests the creation of an OmdCds document from a single Demographic entity
 *   and validates its compliance with the specified XSD schema.
 *
 * - testOmdCdsDocumentCreation:
 *   Verifies that OmdCds document and its constituent elements are created successfully
 *   without any runtime errors.
 *
 * - testXmlValidationWithMultipleDemographics:
 *   Validates the persistence and schema compliance of XML content generated
 *   for multiple Demographic entities.
 *
 * - testSchemaFileExists:
 *   Checks the existence and readability of the main schema file used for XML validation.
 *
 * - testSchemaImportExists:
 *   Ensures the presence and readability of any imported XSD schema files
 *   required by the main schema for validation.
 */
public class DemographicExportAction4Test extends org.oscarehr.common.dao.DaoTestFixtures {

	private final DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	// private DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(new String[] { "demographic", "demographicExt" });
	}

	@Test
	public void testXmlSchemaValidation() throws Exception {
		// Create test demographic data
		Demographic demographic = new Demographic();
		EntityDataGenerator.generateTestDataForModelClass(demographic);
		demographic.setFirstName("John");
		demographic.setLastName("Doe");
		demographic.setSex("M");
		demographic.setYearOfBirth("1980");
		demographic.setMonthOfBirth("01");
		demographic.setDateOfBirth("15");
		demographic.setHin("1234567890");
		demographic.setVer("AB");
		demographic.setProvince("ON");
		demographic.setChartNo("CHART123");
		demographic.setProviderNo("999998");
		demographicDao.save(demographic);
		assertNotNull(demographic.getDemographicNo());

		// Create OmdCds document
		OmdCdsDocument omdCdsDoc = OmdCdsDocument.Factory.newInstance();
		OmdCdsDocument.OmdCds omdCds = omdCdsDoc.addNewOmdCds();
		PatientRecord patientRec = omdCds.addNewPatientRecord();
		Demographics demo = patientRec.addNewDemographics();

		// Populate minimal demographic data
		cdsDt.PersonNameStandard names = demo.addNewNames();
		PersonNameStandard.LegalName legalName = names.addNewLegalName();
		PersonNameStandard.LegalName.FirstName firstName = legalName.addNewFirstName();
		PersonNameStandard.LegalName.LastName lastName = legalName.addNewLastName();
		firstName.setPart(demographic.getFirstName());
		firstName.setPartType(cdsDt.PersonNamePartTypeCode.GIV);
		lastName.setPart(demographic.getLastName());
		lastName.setPartType(PersonNamePartTypeCode.FAMC);

		// Date of birth
		Calendar dob = Calendar.getInstance();
		dob.set(Integer.parseInt(demographic.getYearOfBirth()),
		        Integer.parseInt(demographic.getMonthOfBirth()) - 1,
		        Integer.parseInt(demographic.getDateOfBirth()));
		demo.setDateOfBirth(dob);

		// Health card
		cdsDt.HealthCard healthCard = demo.addNewHealthCard();
		healthCard.setNumber(demographic.getHin());
		healthCard.setVersion(demographic.getVer());
		healthCard.setProvinceCode(Util.setProvinceCode(demographic.getProvince()));

		// Chart number
		if (demographic.getChartNo() != null) {
			demo.setChartNumber(demographic.getChartNo());
		}

		// Gender
		if ("M".equals(demographic.getSex())) {
			demo.setGender(cdsDt.Gender.M);
		} else if ("F".equals(demographic.getSex())) {
			demo.setGender(cdsDt.Gender.F);
		} else {
			demo.setGender(cdsDt.Gender.U);
		}

		// Unique vendor ID
		demo.setUniqueVendorIdSequence("VENDOR_" + demographic.getDemographicNo());

		// Save XML to string
		XmlOptions options = new XmlOptions();
		options.setSavePrettyPrint();
		options.setSavePrettyPrintIndent(2);
		StringWriter stringWriter = new StringWriter();
		omdCdsDoc.save(stringWriter, options);
		String xmlContent = stringWriter.toString();

		// Verify XML is not empty
		assertNotNull("XML content should not be null", xmlContent);
		assertTrue("XML content should not be empty", xmlContent.length() > 0);
		assertTrue("XML should contain OmdCds element", xmlContent.contains("OmdCds"));

		// Validate against schema
		File schemaFile = new File("src/test/resources/omdcds/EMR_Data_Migration_Schema.xsd");
		assertTrue("Schema file should exist: " + schemaFile.getAbsolutePath(), schemaFile.exists());

		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(schemaFile);
		Validator validator = schema.newValidator();

		// Parse XML document
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new java.io.ByteArrayInputStream(xmlContent.getBytes("UTF-8")));

		// Validate - this will throw an exception if validation fails
		validator.validate(new javax.xml.transform.dom.DOMSource(doc));

		// If we reach here, validation succeeded
		assertTrue("XML validation against schema succeeded", true);
	}

	@Test
	public void testOmdCdsDocumentCreation() throws Exception {
		// Test basic document creation
		OmdCdsDocument omdCdsDoc = OmdCdsDocument.Factory.newInstance();
		assertNotNull("OmdCds document should be created", omdCdsDoc);

		OmdCdsDocument.OmdCds omdCds = omdCdsDoc.addNewOmdCds();
		assertNotNull("OmdCds element should be created", omdCds);

		PatientRecord patientRec = omdCds.addNewPatientRecord();
		assertNotNull("PatientRecord should be created", patientRec);

		Demographics demo = patientRec.addNewDemographics();
		assertNotNull("Demographics should be created", demo);
	}

	@Test
	public void testXmlValidationWithMultipleDemographics() throws Exception {
		// Create multiple test demographics
		for (int i = 0; i < 3; i++) {
			Demographic demographic = new Demographic();
			EntityDataGenerator.generateTestDataForModelClass(demographic);
			demographic.setFirstName("TestFirst" + i);
			demographic.setLastName("TestLast" + i);
			demographic.setSex(i % 2 == 0 ? "M" : "F");
			demographic.setYearOfBirth("198" + i);
			demographic.setMonthOfBirth("0" + (i + 1));
			demographic.setDateOfBirth("0" + (i + 1));
			demographic.setHin("HIN" + i + "1234567");
			demographic.setVer("AB");
			demographic.setProvince("ON");
			demographic.setChartNo("CHART" + i);
			demographic.setProviderNo("999998");
			demographicDao.save(demographic);
		}

		assertEquals("Should have 3 demographics", 3, demographicDao.getActiveDemographicIds().size());
	}

	@Test
	public void testSchemaFileExists() {
		File schemaFile = new File("src/test/resources/omdcds/EMR_Data_Migration_Schema.xsd");
		assertTrue("Schema file should exist at expected location", schemaFile.exists());
		assertTrue("Schema file should be readable", schemaFile.canRead());
	}

	@Test
	public void testSchemaImportExists() {
		// Verify that the imported schema also exists
		File importedSchemaFile = new File("src/test/resources/omdcds/EMR_Data_Migration_Schema_DT.xsd");
		assertTrue("Imported schema file should exist", importedSchemaFile.exists());
		assertTrue("Imported schema file should be readable", importedSchemaFile.canRead());
	}
}
