package org.oscarehr.common.model.enumerator;

/**
 * Lab type values used for initialization of a matching handler/parser for
 * processing incoming lab reports.
 */
public enum LabType {
	ALPHA("ALPHA"),
	CML("CML"),
	EPSILON("EPSILON"),
	ExcellerisON("ExcellerisON"),
	PATHL7("PATHL7"),
	EXCELLERIS("PATHL7"),
	GDML("GDML"),
	HHSEMR("HHSEMR"),
	HRMXML("HRMXML"),
	ICL("ICL"),
	IHAPOI("IHA"),
	MDS("MDS"),
	MEDVUE("MEDVUE"),
	HL7("HL7"),
	OLIS_HL7("OLIS_HL7"),
	OSCAR_TO_OSCAR_HL7_V2("OSCAR_TO_OSCAR_HL7_V2"),
	PFHT("PFHT"),
	SIOUX("SIOUX"),
	TDIS("TDIS"),
	Spire("Spire"),
	OUL_R21("OUL_R21"),
	ORU_R01("ORU_R01"),
	BIOTEST("BIOTEST"),
	CLS("CLS"),
	CDL("CDL"),
	TRUENORTH("TRUENORTH"),
	MEDITECH("MEDITECH");

	private final String name;

	LabType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
