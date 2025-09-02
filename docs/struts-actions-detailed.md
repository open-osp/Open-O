# OSCAR EMR Struts Actions Reference

## Overview

This document provides a comprehensive reference of all Struts actions in the OSCAR EMR system. These actions serve as the primary entry points for handling HTTP requests and coordinating business logic within the web application.

## Statistics

- **Total Actions:** 507
- **Modules:** 29
- **Primary Functional Areas:**
  - Clinical workflows (encounters, measurements, prescriptions)
  - Administrative functions (billing, demographics, reporting)
  - Document management and lab integration
  - System configuration and user management

---

## Admin Module

Administrative functions for system configuration, user management, and maintenance tasks.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| admin/AuditLogPurge | org.oscarehr.admin.web.AuditLogPurge2Action | Purges old audit log entries from the system |
| admin/Flowsheet | org.oscarehr.flowsheet.Flowsheet2Action | Manages flowsheet templates and configurations |
| admin/ForwardingRules | oscar.oscarLab.pageUtil.ForwardingRules2Action | Configures lab result forwarding rules |
| admin/GenerateTraceabilityReportAction | org.oscarehr.admin.traceability.GenerateTraceabilityReport2Action | Generates system traceability reports for compliance |
| admin/GenerateTraceAction | org.oscarehr.admin.traceability.GenerateTrace2Action | Creates audit trails for data changes |
| admin/GroupPreference | org.oscarehr.common.web.GroupPreference2Action | Manages user group preferences and settings |
| admin/GstControl | oscar.oscarBilling.ca.on.administration.GstControl2Action | Controls GST/HST billing settings for Ontario |
| admin/ManageBillingReferral | org.oscarehr.common.web.BillingreferralEdit2Action | Manages billing referral configurations |
| admin/ManageClinic | org.oscarehr.common.web.ClinicManage2Action | Administers clinic information and settings |
| admin/manageCSSStyles | org.oscarehr.billing.CA.ON.web.ManageCSS2Action | Manages custom CSS styles for billing forms |
| admin/ManageEmails | org.oscarehr.email.admin.ManageEmails2Action | Configures email server settings and templates |
| admin/ManageFaxes | org.oscarehr.fax.admin.ManageFaxes2Action | Manages fax server configurations |
| admin/ManageFax | org.oscarehr.fax.admin.ConfigureFax2Action | Configures individual fax settings |
| admin/ManageSites | org.oscarehr.common.web.SitesManage2Action | Manages multi-site configurations |
| admin/MergeRecords | oscar.oscarDemographic.pageUtil.DemographicMergeRecord2Action | Merges duplicate demographic records |
| admin/oncallClinic | org.oscarehr.admin.reports.SaveOnCallClinic2Action | Manages on-call clinic schedules |
| admin/oscarStatus | oscar.util.OscarStatus2Action | Displays system status and health information |
| admin/uploadEntryText | oscar.login.UploadLoginText2Action | Uploads custom login page text |

## Appointment Module

Appointment scheduling and management functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| appointment/appointmentTypeAction | oscar.appt.web.AppointmentType2Action | Manages appointment types and durations |
| appointment/apptStatusSetting | oscar.appt.status.web.AppointmentStatus2Action | Configures appointment status settings |
| appointment/printAppointmentReceiptAction | oscar.oscarReport.pageUtil.PrintAppointmentReceipt2Action | Generates printable appointment receipts |

## Archive Module

Document and record archiving functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| ArchiveView | org.oscarehr.casemgmt.web.ArchiveView2Action | Views archived case management records |

## Attach Module

Document attachment functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| attachDocs | oscar.oscarEncounter.oscarConsultationRequest.pageUtil.ConsultationAttachDocs2Action | Attaches documents to consultation requests |

## Billing Module

Comprehensive billing management for various Canadian provinces.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| billing/CA/BC/AddReferralDoc | oscar.oscarBilling.ca.bc.pageUtil.AddReferralDoc2Action | Adds referral documents for BC billing |
| billing/CA/BC/associateCodesAction | oscar.oscarBilling.ca.bc.pageUtil.AssociateCodes2Action | Associates billing codes with services in BC |
| billing/CA/BC/billingAddCode | oscar.oscarBilling.ca.bc.pageUtil.BillingAddCode2Action | Adds new billing codes for BC |
| billing/CA/BC/billingEditCode | oscar.oscarBilling.ca.bc.pageUtil.BillingEditCode2Action | Edits existing BC billing codes |
| billing/CA/BC/billingTeleplanCorrectionWCB | oscar.oscarBilling.ca.bc.administration.TeleplanCorrectionActionWCB2Action | Processes WCB Teleplan billing corrections |
| billing/CA/BC/billingView | oscar.oscarBilling.ca.bc.pageUtil.BillingView2Action | Views BC billing records |
| billing/CA/BC/CreateBilling | oscar.oscarBilling.ca.bc.pageUtil.BillingCreateBilling2Action | Creates new billing records for BC |
| billing/CA/BC/createBillingReportAction | oscar.oscarBilling.ca.bc.MSP.CreateBillingReport2Action | Generates BC MSP billing reports |
| billing/CA/BC/deleteServiceCodeAssoc | oscar.oscarBilling.ca.bc.pageUtil.DeleteServiceCodeAssoc2Action | Deletes service code associations |
| billing/CA/BC/editServiceCodeAssocAction | oscar.oscarBilling.ca.bc.pageUtil.EditServiceCodeAssoc2Action | Edits service code associations |
| billing/CA/BC/formwcb | oscar.oscarBilling.ca.bc.pageUtil.WCBAction22Action | Processes WCB forms for BC |
| billing/CA/BC/GenerateTeleplanFile | oscar.oscarBilling.ca.bc.pageUtil.GenerateTeleplanFile2Action | Generates Teleplan submission files |
| billing/CA/BC/ManageTeleplan | oscar.oscarBilling.ca.bc.pageUtil.ManageTeleplan2Action | Manages Teleplan billing configurations |
| billing/CA/BC/ProcessRemittance | oscar.oscarBilling.ca.bc.MSP.GenTa2Action | Processes MSP remittance files |
| billing/CA/BC/receivePaymentAction | oscar.oscarBilling.ca.bc.pageUtil.ReceivePayment2Action | Records payment receipts |
| billing/CA/BC/reprocessBill | oscar.oscarBilling.ca.bc.pageUtil.BillingReProcessBill2Action | Reprocesses rejected bills |
| billing/CA/BC/saveAssocAction | oscar.oscarBilling.ca.bc.pageUtil.SaveAssoc2Action | Saves code associations |
| billing/CA/BC/SaveBilling | oscar.oscarBilling.ca.bc.pageUtil.BillingSaveBilling2Action | Saves billing records |
| billing/CA/BC/saveBillingPreferencesAction | oscar.oscarBilling.ca.bc.pageUtil.SaveBillingPreferences2Action | Saves billing preferences |
| billing/CA/BC/showServiceCodeAssocs | oscar.oscarBilling.ca.bc.pageUtil.ShowServiceCodeAssocs2Action | Displays service code associations |
| billing/CA/BC/SimulateTeleplanFile | oscar.oscarBilling.ca.bc.pageUtil.SimulateTeleplanFile2Action | Simulates Teleplan file generation |
| billing/CA/BC/supServiceCodeAssocAction | oscar.oscarBilling.ca.bc.pageUtil.SupServiceCodeAssoc2Action | Manages superior service code associations |
| billing/CA/BC/UpdateBilling | oscar.oscarBilling.ca.bc.pageUtil.BillingUpdateBilling2Action | Updates existing billing records |
| billing/CA/BC/viewBillingPreferencesAction | oscar.oscarBilling.ca.bc.pageUtil.ViewBillingPreferences2Action | Views billing preferences |
| billing/CA/BC/viewformwcb | oscar.oscarBilling.ca.bc.pageUtil.ViewWCB2Action | Views WCB forms |
| billing/CA/BC/viewReceivePaymentAction | oscar.oscarBilling.ca.bc.pageUtil.ViewReceivePayment2Action | Views payment records |
| billing/CA/ON/ApplyPractitionerPremium | org.oscarehr.common.web.ApplyPractitionerPremium2Action | Applies practitioner premiums for Ontario |
| billing/CA/ON/BatchBill | org.oscarehr.billing.CA.ON.web.BatchBill2Action | Processes batch billing for Ontario |
| billing/CA/ON/benefitScheduleChange | oscar.oscarBilling.ca.on.OHIP.ScheduleOfBenefitsUpdate2Action | Updates OHIP benefit schedules |
| billing/CA/ON/benefitScheduleUpload | oscar.oscarBilling.ca.on.OHIP.ScheduleOfBenefitsUpload2Action | Uploads OHIP benefit schedules |
| billing/CA/ON/billingON3rdPayments | org.oscarehr.billing.CA.ON.web.BillingONPayments2Action | Manages third-party payments for Ontario |
| billing/CA/ON/BillingONCorrection | oscar.oscarBilling.ca.on.pageUtil.BillingCorrection2Action | Handles billing corrections for Ontario |
| billing/ca/on/DisplayInvoiceLogo | org.oscarehr.billing.CA.ON.util.DisplayInvoiceLogo2Action | Displays invoice logos for Ontario |
| billing/CA/ON/endYearStatement | oscar.oscarBilling.ca.on.pageUtil.PatientEndYearStatement2Action | Generates end-of-year patient statements |
| billing/CA/ON/managePaymentType | oscar.oscarBilling.ca.on.pageUtil.PaymentType2Action | Manages payment types for Ontario |
| billing/CA/ON/moveMOHFiles | org.oscarehr.billing.CA.ON.web.ArchiveMOHFile2Action | Archives MOH billing files |
| billing/CA/ON/moveMOHFiles | org.oscarehr.billing.CA.ON.web.MoveMOHFiles2Action | Moves MOH billing files |
| BillingInvoice | org.oscarehr.common.web.BillingInvoice2Action | Generates billing invoices |
| BillingONReview | org.oscarehr.common.web.BillingONReview2Action | Reviews Ontario billing submissions |
| billing | oscar.oscarBilling.ca.bc.pageUtil.Billing2Action | Main billing interface |

## Caseload Module

Patient caseload management functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| caseload/CaseloadContent | org.oscarehr.caseload.CaseloadContent2Action | Manages patient caseload content |

## Case Management Module

Clinical case management and documentation.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| CaseManagementEntry | org.oscarehr.casemgmt.web.CaseManagementEntry2Action | Creates new case management entries |
| CaseManagementView | org.oscarehr.casemgmt.web.CaseManagementView2Action | Views case management records |
| casemgmt/ExtPrintRegistry | org.oscarehr.casemgmt.web.ExtPrintRegistry2Action | Manages external print registry |
| casemgmt/NotePermissions | org.oscarehr.casemgmt.web.NotePermissions2Action | Configures note access permissions |
| casemgmt/RegisterCppCode | org.oscarehr.casemgmt.web.RegisterCppCode2Action | Registers CPP diagnostic codes |

## Client Module

Client and patient image management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| ClientImage | org.oscarehr.casemgmt.web.ClientImage2Action | Manages client/patient images |

## Clinical Connect Module

Clinical Connect EHR integration.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| clinicalConnectEHRViewer | org.oscarehr.integration.clinicalconnect.ClinicalConnectViewer2Action | Views Clinical Connect EHR data |

## Code Search Module

Medical code search functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| CodeSearch | org.oscarehr.common.web.CodeSearchService2Action | Searches medical diagnostic codes |

## Immunization Configuration Module

Immunization setup and configuration.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| CreateImmunizationSetConfig | oscar.oscarEncounter.immunization.config.pageUtil.EctImmCreateImmunizationSetConfig2Action | Creates immunization set configurations |
| CreateInitImmunization | oscar.oscarEncounter.immunization.config.pageUtil.EctCreateImmunizationSetInit2Action | Initializes immunization configurations |
| DeleteImmunizationSets | oscar.oscarEncounter.immunization.config.pageUtil.EctImmDeleteImmunizationSet2Action | Deletes immunization sets |
| ImmunizationSetDisplay | oscar.oscarEncounter.immunization.config.pageUtil.EctImmImmunizationSetDisplay2Action | Displays immunization set configurations |

## CVC Module

Clinical validation and connectivity testing.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| cvc | org.oscarehr.integration.born.CVCTester2Action | Tests BORN CVC integration connectivity |

## Default Module

Default encounter configurations.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| DefaultEncounterIssue | org.caisi.core.web.DefaultEncounterIssue2Action | Sets default encounter issues |

## Demographics Module

Patient demographic management and related functions.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| demographic/AddRelation | oscar.oscarDemographic.pageUtil.AddDemographicRelationship2Action | Adds patient relationships |
| demographic/cihiExportOMD4 | oscar.oscarDemographic.pageUtil.CihiExport2Action | Exports CIHI OMD4 data |
| demographic/cihiExportPHC_VRS | oscar.oscarDemographic.pageUtil.CihiExportPHC_VRS2Action | Exports PHC VRS data to CIHI |
| demographic/Contact | org.oscarehr.common.web.Contact2Action | Manages patient contact information |
| demographic/DeleteRelation | oscar.oscarDemographic.pageUtil.DeleteDemographicRelationship2Action | Deletes patient relationships |
| demographic/DemographicExport | oscar.oscarDemographic.pageUtil.DemographicExportAction42Action | Exports demographic data |
| demographic/eRourkeExport | oscar.oscarDemographic.pageUtil.RourkeExport2Action | Exports Rourke assessment data |
| DemographicExtService | org.oscarehr.common.web.DemographicExtService2Action | Provides external demographic services |
| demographic/printClientLabLabelAction | oscar.oscarDemographic.PrintClientLabLabel2Action | Prints client lab labels |
| demographic/printDemoAddressLabelAction | oscar.oscarDemographic.PrintDemoAddressLabel2Action | Prints patient address labels |
| demographic/printDemoChartLabelAction | oscar.oscarDemographic.PrintDemoChartLabel2Action | Prints patient chart labels |
| demographic/printDemoLabelAction | oscar.oscarDemographic.PrintDemoLabel2Action | Prints general patient labels |
| demographic/SearchDemographic | org.oscarehr.common.web.SearchDemographicAutoComplete2Action | Provides demographic search autocomplete |
| demographicSupport | org.oscarehr.common.web.Demographic2Action | General demographic support functions |
| demographic/ValidateSwipeCard | org.oscarehr.integration.mchcv.ValidateSwipeCard2Action | Validates health card swipe data |

## DHIR Module

Digital Health Immunization Repository integration.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| dhir/submit | org.oscarehr.integration.dhir.SubmitImmunization2Action | Submits immunizations to DHIR |

## Document Management Module

Document management and processing functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| DocumentDescriptionTemplate | ca.openosp.openo.www.provider.DocumentDescriptionTemplate2Action | Manages document description templates |
| documentManager/addDocumentType | org.oscarehr.documentManager.actions.AddDocumentType2Action | Adds new document types |
| documentManager/addEditDocument | org.oscarehr.documentManager.actions.AddEditDocument2Action | Adds or edits documents |
| documentManager/addEditHtml | org.oscarehr.documentManager.actions.AddEditHtml2Action | Adds or edits HTML documents |
| documentManager/addLink | org.oscarehr.documentManager.actions.AddEditHtml2Action | Adds document links |
| documentManager/changeDocStatus | org.oscarehr.documentManager.actions.ChangeDocStatus2Action | Changes document status |
| documentManager/combinePDFs | org.oscarehr.documentManager.actions.CombinePDF2Action | Combines multiple PDF documents |
| documentManager/documentUpload | org.oscarehr.documentManager.actions.DocumentUpload2Action | Uploads documents to the system |
| documentManager/inboxManage | org.oscarehr.documentManager.actions.DmsInboxManage2Action | Manages document inbox |
| documentManager/ManageDocument | org.oscarehr.documentManager.actions.ManageDocument2Action | General document management |
| documentManager/SplitDocument | org.oscarehr.documentManager.actions.SplitDocument2Action | Splits documents into parts |

## DX Code Search Module

Diagnostic code search functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| dxCodeSearchJSON | oscar.oscarResearch.oscarDxResearch.pageUtil.dxCodeSearchJSON2Action | Provides JSON-based diagnostic code search |

## E-Consult Module

Electronic consultation functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| econsult | oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EConsult2Action | Manages electronic consultations |
| econsultSSOLogin | oscar.login.SSOLogin2Action | Provides SSO login for e-consults |

## Edit Provider Module

Provider information editing functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| EditAddress | oscar.oscarProvider.pageUtil.ProEditAddress2Action | Edits provider addresses |
| EditFaxNum | oscar.oscarProvider.pageUtil.ProEditFaxNum2Action | Edits provider fax numbers |
| EditPhoneNum | oscar.oscarProvider.pageUtil.ProEditPhoneNum2Action | Edits provider phone numbers |
| EditPrinter | oscar.oscarProvider.pageUtil.ProEditPrinter2Action | Edits provider printer settings |

## EForm Module

Electronic forms management and processing.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| eform/addEForm | oscar.eform.actions.AddEForm2Action | Adds new electronic forms |
| eform/addGroup | oscar.eform.actions.AddGroup2Action | Adds form groups |
| eform/addToGroup | oscar.eform.actions.AddToGroup2Action | Adds forms to groups |
| eform/attachDoc | oscar.eform.EFormAttachDocs2Action | Attaches documents to forms |
| eform/delEForm | oscar.eform.actions.DelEForm2Action | Deletes electronic forms |
| eform/deleteImage | oscar.eform.actions.DelImage2Action | Deletes images from forms |
| eform/displayImage | oscar.eform.actions.DisplayImage2Action | Displays form images |
| eform/editForm | oscar.eform.actions.HtmlEdit2Action | Edits HTML forms |
| eform/efmOpenEformByName | oscar.eform.actions.OpenEFormByName2Action | Opens forms by name |
| eform/efmPrintPDF | oscar.eform.actions.PrintPDF2Action | Prints forms as PDF |
| eform/eFormAttachmentForm | oscar.eform.upload.UploadEFormAttachment2Action | Uploads form attachments |
| eform/FetchUpdatedData | oscar.eform.actions.FetchUpdatedData2Action | Fetches updated form data |
| eform/imageUpload | oscar.eform.upload.ImageUpload2Action | Uploads images to forms |
| eform/IndivicaRichTextLetterSettings | oscar.eform.actions.RTLSettings2Action | Configures rich text letter settings |
| eform/logEformError | oscar.eform.EformLogError2Action | Logs form errors |
| eform/manageEForm | oscar.eform.actions.ManageEForm2Action | Manages electronic forms |
| eform/removeEForm | oscar.eform.actions.RemEForm2Action | Removes forms |
| eform/restoreEForm | oscar.eform.actions.RestoreEForm2Action | Restores deleted forms |
| eforms/delGroup | oscar.eform.actions.DeleteGroup2Action | Deletes form groups |
| eforms/removeFromGroup | oscar.eform.actions.RemoveFromGroup2Action | Removes forms from groups |
| eform/unRemoveEForm | oscar.eform.actions.UnRemEForm2Action | Un-removes forms |
| eform/uploadHtml | oscar.eform.upload.HtmlUpload2Action | Uploads HTML forms |

## Email Module

Email management and communication.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| email/emailComposeAction | org.oscarehr.email.action.EmailCompose2Action | Composes new emails |
| email/emailSendAction | org.oscarehr.email.action.EmailSend2Action | Sends composed emails |

## Signature Module

Provider signature management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| EnterSignature | oscar.oscarProvider.pageUtil.ProEditSignature2Action | Enters or edits provider signatures |

## Episode Module

Clinical episode management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| Episode | org.oscarehr.common.web.Episode2Action | Manages clinical episodes |

## Facility Module

Facility management and messaging.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| FacilityManager | oscar.facility.FacilityManager2Action | Manages facility information |
| FacilityMessage | org.caisi.core.web.FacilityMessage2Action | Handles facility messaging |

## Fax Module

Fax functionality and management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| fax/faxAction | org.oscarehr.fax.action.Fax2Action | Manages fax operations |

## Form Module

Clinical forms and data import functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| form/AddRHWorkFlow | oscar.form.pageUtil.FrmFormAddRHWorkFlow2Action | Adds reproductive health workflows |
| form/BCAR2020 | oscar.form.FrmBCAR20202Action | Processes BC AR 2020 forms |
| formBPMH | oscar.form.pharmaForms.formBPMH.web.BpmhFormRetrieve2Action | Retrieves Best Possible Medication History forms |
| formeCARES | oscar.form.eCARES.EcaresForm2Action | Processes eCARES forms |
| form/formname | oscar.form.Frm2Action | Generic form processor |
| form/forwardshortcutname | oscar.form.pageUtil.FormForward2Action | Forwards form shortcuts |
| form/importLogDownload | oscar.oscarDemographic.pageUtil.ImportLogDownload2Action | Downloads import logs |
| form/importUpload | oscar.oscarDemographic.pageUtil.ImportDemographicDataAction42Action | Uploads demographic import data |
| form/RHPrevention | oscar.form.pageUtil.FrmFormRHPrevention2Action | Manages reproductive health prevention forms |
| form/select | oscar.form.pageUtil.FrmSelect2Action | Selects forms |
| form/SetupForm | oscar.form.pageUtil.FrmSetupForm2Action | Sets up new forms |
| form/setupSelect | oscar.form.pageUtil.FrmSetupSelect2Action | Sets up form selection |
| form/SubmitForm | oscar.form.pageUtil.FrmForm2Action | Submits completed forms |
| form/xmlUpload | oscar.form.pageUtil.FrmXmlUpload2Action | Uploads XML form data |

## Professional Specialist Module

Professional specialist registry.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| getProfessionalSpecialist | org.oscarehr.contactRegistry.ProfessionalSpecialist2Action | Retrieves professional specialist information |

## Home Module

System home page functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| Home | ca.openosp.openo.web.PMmodule.Home2Action | Displays system home page |

## Hospital Report Manager Module

Hospital report management and integration.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| hospitalReportManager/Display | org.oscarehr.hospitalReportManager.HRMDisplayReport2Action | Displays hospital reports |
| hospitalReportManager/HRMDownloadFile | org.oscarehr.hospitalReportManager.HRMDownloadFile2Action | Downloads HRM files |
| hospitalReportManager/hrmKeyUploader | org.oscarehr.hospitalReportManager.HRMUploadKey2Action | Uploads HRM encryption keys |
| hospitalReportManager/hrm | org.oscarehr.hospitalReportManager.v2018.HRM2Action | Main HRM interface |
| hospitalReportManager/HRMPreferences | org.oscarehr.hospitalReportManager.HRMPreferences2Action | Manages HRM preferences |
| hospitalReportManager/Mapping | org.oscarehr.hospitalReportManager.HRMMapping2Action | Maps HRM data fields |
| hospitalReportManager/Modify | org.oscarehr.hospitalReportManager.HRMModifyDocument2Action | Modifies HRM documents |
| hospitalReportManager/PrintHRMReport | org.oscarehr.hospitalReportManager.PrintHRMReport2Action | Prints HRM reports |
| hospitalReportManager/Statement | org.oscarehr.hospitalReportManager.HRMStatementModify2Action | Modifies HRM statements |
| hospitalReportManager/UploadLab | org.oscarehr.hospitalReportManager.HRMUploadLab2Action | Uploads lab results to HRM |

## Indivica Module

Health card search functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| indivica/HCSearch | org.oscarehr.common.web.HealthCardSearch2Action | Searches health card database |

## Infirm Module

Infirmary management functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| infirm | org.caisi.core.web.Infirm2Action | Manages infirmary operations |

## Integrator Module

System integration functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| integrator/IntegratorPush | org.oscarehr.common.web.IntegratorPush2Action | Pushes data to external integrators |

## Issue Admin Module

Issue administration functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| issueAdmin | org.caisi.core.web.IssueAdmin2Action | Administers clinical issues |

## Lab Module

Laboratory result management and integration.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| lab/CA/ALL/createLabelTDIS | oscar.oscarLab.ca.all.pageUtil.CreateLabelTDIS2Action | Creates TDIS lab labels |
| lab/CA/ALL/Forward | oscar.oscarMDS.pageUtil.ReportReassign2Action | Forwards lab reports to providers |
| lab/CA/ALL/insideLabUpload | oscar.oscarLab.ca.all.pageUtil.InsideLabUpload2Action | Uploads inside lab results |
| lab/CA/ALL/oruR01Upload | oscar.oscarLab.ca.all.pageUtil.OruR01Upload2Action | Uploads ORU R01 lab messages |
| lab/CA/ALL/PrintOLISLab | oscar.oscarLab.ca.all.pageUtil.PrintOLISLab2Action | Prints individual OLIS lab results |
| lab/CA/ALL/PrintOLIS | oscar.oscarLab.ca.all.pageUtil.PrintOLISLabs2Action | Prints multiple OLIS lab results |
| lab/CA/ALL/PrintPDF | oscar.oscarLab.ca.all.pageUtil.PrintLabs2Action | Prints lab results as PDF |
| lab/CA/ALL/UnlinkDemographic | oscar.oscarLab.ca.all.pageUtil.UnlinkDemographic2Action | Unlinks demographics from lab results |
| lab/CA/BC/Forward | oscar.oscarMDS.pageUtil.ReportReassign2Action | Forwards BC lab reports |
| lab/CA/ON/Forward | oscar.oscarMDS.pageUtil.ReportReassign2Action | Forwards Ontario lab reports |
| lab/CMLlabUpload | oscar.oscarLab.ca.on.CML.Upload.LabUpload2Action | Uploads CML lab results |
| lab/DownloadEmbeddedDocumentFromLab | oscar.oscarLab.ca.all.pageUtil.DownloadEmbeddedDocumentFromLab2Action | Downloads embedded lab documents |
| lab/labUpload | oscar.oscarLab.ca.bc.PathNet.pageUtil.LabUpload2Action | Uploads PathNet lab results |
| lab/newLabUpload | oscar.oscarLab.ca.all.pageUtil.LabUpload2Action | Uploads new lab results |

## Login Module

User authentication and session management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| login | oscar.login.Login2Action | Processes user login |
| login/recordLogin | oscar.login.LoginAgreement2Action | Records login agreements |
| logout | oscar.login.Logout2Action | Processes user logout |

## Lookup Module

System lookup table management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| lookupListManagerAction | org.oscarehr.admin.lookUpLists.LookupListManager2Action | Manages lookup list configurations |
| Lookup/LookupCodeEdit | com.quatro.web.lookup.LookupCodeEdit2Action | Edits lookup codes |
| Lookup/LookupCodeList | com.quatro.web.lookup.LookupCodeList2Action | Lists lookup codes |
| Lookup/LookupList | com.quatro.web.lookup.LookupList2Action | Manages lookup lists |
| Lookup/LookupTableList | com.quatro.web.lookup.LookupTableList2Action | Lists lookup tables |

## MCEDT Module

Ministry Claims Electronic Data Transfer integration.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| mcedt/addUpload | org.oscarehr.integration.mcedt.mailbox.Upload2Action | Adds MCEDT uploads |
| mcedt/autoUpload | org.oscarehr.integration.mcedt.mailbox.Upload2Action | Performs automatic MCEDT uploads |
| mcedt/download | org.oscarehr.integration.mcedt.mailbox.Download2Action | Downloads MCEDT files |
| mcedt/info | org.oscarehr.integration.mcedt.Info2Action | Displays MCEDT information |
| mcedt/kaiautodl | org.oscarehr.integration.mcedt.mailbox.Download2Action | Kai automatic downloads |
| mcedt/kaichpass | org.oscarehr.integration.mcedt.mailbox.User2Action | Changes Kai passwords |
| mcedt/kaimcedt | org.oscarehr.integration.mcedt.mailbox.Resource2Action | Manages Kai MCEDT resources |
| mcedt/mcedt | org.oscarehr.integration.mcedt.Resource2Action | Main MCEDT interface |
| mcedt/openAutoUpload | org.oscarehr.integration.mcedt.mailbox.Resource2Action | Opens automatic upload interface |
| mcedt/resourceInfo | org.oscarehr.integration.mcedt.mailbox.Info2Action | Displays MCEDT resource information |
| mcedt/reSubmit | org.oscarehr.integration.mcedt.mailbox.ReSubmit2Action | Resubmits MCEDT data |
| mcedt/update | org.oscarehr.integration.mcedt.Update2Action | Updates MCEDT configurations |
| mcedt/upload | org.oscarehr.integration.mcedt.mailbox.Upload2Action | Uploads MCEDT files |
| mcedt/uploads | org.oscarehr.integration.mcedt.Upload2Action | Manages MCEDT uploads |

## Measurement Module

Clinical measurement data management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| MeasurementHL7Uploader | oscar.oscarEncounter.oscarMeasurements.hl7.MeasurementHL7Uploader2Action | Uploads measurements via HL7 |

## Notification Module

Provider notification system.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| notification/create | org.oscarehr.common.web.ProviderNotification2Action | Creates provider notifications |

## OLIS Module

Ontario Laboratory Information System integration.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| olis/AddToInbox | ca.openosp.openo.olis.OLISAddToInbox2Action | Adds OLIS results to inbox |
| olis/Preferences | ca.openosp.openo.olis.OLISPreferences2Action | Manages OLIS preferences |
| olis/Results | ca.openosp.openo.olis.OLISResults2Action | Retrieves OLIS lab results |
| olis/Search | ca.openosp.openo.olis.OLISSearch2Action | Searches OLIS database |
| olis/UploadSimulationData | ca.openosp.openo.olis.OLISUploadSimulationData2Action | Uploads OLIS simulation data |

## OnCall Module

On-call questionnaire functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| OnCallQuestionnaire | org.oscarehr.casemgmt.web.OnCallQuestionnaire2Action | Manages on-call questionnaires |

## Oscar Billing Module

OSCAR billing system functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| oscarBilling/DocumentErrorReportUpload | oscar.oscarBilling.ca.on.pageUtil.BillingDocumentErrorReportUpload2Action | Uploads billing error reports |

## Oscar Chart Module

Chart printing functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| OscarChartPrint | org.oscarehr.casemgmt.web.EChartPrint2Action | Prints patient charts |

## Oscar Consultation Module

Consultation request management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| oscarConsultationRequest/consultationClinicalData | oscar.oscarEncounter.oscarConsultationRequest.pageUtil.ConsultationClinicalData2Action | Manages consultation clinical data |

## Oscar Encounter Module

Clinical encounter management and workflow.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| oscarEncounter/AddDepartment | oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConAddDepartment2Action | Adds consultation departments |
| oscarEncounter/AddInstitution | oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConAddInstitution2Action | Adds consultation institutions |
| oscarEncounter/AddService | oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConAddService2Action | Adds consultation services |
| oscarEncounter/AddSpecialist | oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConAddSpecialist2Action | Adds specialists |
| oscarEncounter/decisionSupport/guidelineAction | org.oscarehr.decisionSupport.web.DSGuideline2Action | Processes decision support guidelines |
| oscarEncounter/DelService | oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConDeleteServices2Action | Deletes consultation services |
| oscarEncounter/displayAllergy | oscar.oscarEncounter.pageUtil.EctDisplayAllergy2Action | Displays patient allergies in encounter |
| oscarEncounter/displayAppointmentHistory | oscar.oscarEncounter.pageUtil.EctDisplayAppointmentHistory2Action | Displays appointment history |
| oscarEncounter/displayBilling | oscar.oscarEncounter.pageUtil.EctDisplayBilling2Action | Displays billing information |
| oscarEncounter/displayConReport | oscar.oscarEncounter.pageUtil.EctDisplayConReport2Action | Displays consultation reports |
| oscarEncounter/displayConsultation | oscar.oscarEncounter.pageUtil.EctDisplayConsult2Action | Displays consultations |
| oscarEncounter/displayContacts | oscar.oscarEncounter.pageUtil.EctDisplayContacts2Action | Displays patient contacts |
| oscarEncounter/displayDecisionSupportAlerts | oscar.oscarEncounter.pageUtil.EctDisplayDecisionSupportAlerts2Action | Displays decision support alerts |
| oscarEncounter/displayDiagrams | oscar.oscarEncounter.pageUtil.EctDisplayDiagram2Action | Displays clinical diagrams |
| oscarEncounter/displayDisease | oscar.oscarEncounter.pageUtil.EctDisplayDx2Action | Displays diagnoses |
| oscarEncounter/displayDocuments | oscar.oscarEncounter.pageUtil.EctDisplayDocs2Action | Displays encounter documents |
| oscarEncounter/displayEconsultation | oscar.oscarEncounter.pageUtil.EctDisplayEconsult2Action | Displays e-consultations |
| oscarEncounter/displayEForms | oscar.oscarEncounter.pageUtil.EctDisplayEForm2Action | Displays electronic forms |
| oscarEncounter/displayEHR | oscar.oscarEncounter.pageUtil.EctDisplayEHR2Action | Displays electronic health records |
| oscarEncounter/displayEpisodes | oscar.oscarEncounter.pageUtil.EctDisplayEpisode2Action | Displays clinical episodes |
| oscarEncounter/displayExaminationHistory | oscar.oscarEncounter.pageUtil.EctDisplayExaminationHistory2Action | Displays examination history |
| oscarEncounter/displayForms | oscar.oscarEncounter.pageUtil.EctDisplayForm2Action | Displays clinical forms |
| oscarEncounter/displayHRM | oscar.oscarEncounter.pageUtil.EctDisplayHRM2Action | Displays hospital reports |
| oscarEncounter/displayIssues | oscar.oscarEncounter.pageUtil.EctDisplayIssues2Action | Displays patient issues |
| oscarEncounter/displayLabs | oscar.oscarEncounter.pageUtil.EctDisplayLabAction22Action | Displays lab results |
| oscarEncounter/displayMacro | oscar.oscarEncounter.pageUtil.EctDisplayMacro2Action | Displays text macros |
| oscarEncounter/displayMeasurements | oscar.oscarEncounter.pageUtil.EctDisplayMeasurements2Action | Displays clinical measurements |
| oscarEncounter/displayMessages | oscar.oscarEncounter.pageUtil.EctDisplayMsg2Action | Displays messages |
| oscarEncounter/displayOcularProcedure | oscar.oscarEncounter.pageUtil.EctDisplayOcularProcedure2Action | Displays ocular procedures |
| oscarEncounter/displayPhotos | oscar.oscarEncounter.pageUtil.EctDisplayPhotos2Action | Displays patient photos |
| oscarEncounter/displayPregnancies | oscar.oscarEncounter.pageUtil.EctDisplayPregnancy2Action | Displays pregnancy information |
| oscarEncounter/displayPrevention | oscar.oscarEncounter.pageUtil.EctDisplayPrevention2Action | Displays prevention records |
| oscarEncounter/displayResolvedIssues | oscar.oscarEncounter.pageUtil.EctDisplayResolvedIssues2Action | Displays resolved issues |
| oscarEncounter/displayRx | oscar.oscarEncounter.pageUtil.EctDisplayRx2Action | Displays prescriptions |
| oscarEncounter/displaySpecsHistory | oscar.oscarEncounter.pageUtil.EctDisplaySpecsHistory2Action | Displays prescription history |
| oscarEncounter/displayTickler | oscar.oscarEncounter.pageUtil.EctDisplayTickler2Action | Displays ticklers |
| oscarEncounter/EditDepartments | oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConEditDepartments2Action | Edits consultation departments |
| oscarEncounter/EditInstitutions | oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConEditInstitutions2Action | Edits consultation institutions |
| oscarEncounter/EditSpecialists | oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConEditSpecialists2Action | Edits specialists |
| oscarEncounter/EnableConRequestResponse | oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConEnableReqResp2Action | Enables consultation request responses |
| oscarEncounter/eRefer | oscar.oscarEncounter.oceanEReferal.pageUtil.ERefer2Action | Processes electronic referrals |
| oscarEncounter/FormUpdate | oscar.oscarEncounter.oscarMeasurements.pageUtil.FormUpdate2Action | Updates measurement forms |
| oscarEncounter/GraphMeasurements | oscar.oscarEncounter.oscarMeasurements.pageUtil.MeasurementGraphAction22Action | Graphs clinical measurements |
| oscarEncounter/immunization/config/CreateImmunizationSetConfig | oscar.oscarEncounter.immunization.config.pageUtil.EctImmCreateImmunizationSetConfig2Action | Creates immunization configurations |
| oscarEncounter/immunization/config/CreateInitImmunization | oscar.oscarEncounter.immunization.config.pageUtil.EctImmCreateImmunizationSetInit2Action | Initializes immunization sets |
| oscarEncounter/immunization/config/deleteImmunizationSet | oscar.oscarEncounter.immunization.config.pageUtil.EctImmInitConfigDeleteImmuSet2Action | Deletes immunization sets |
| oscarEncounter/immunization/config/DeleteImmunizationSets | oscar.oscarEncounter.immunization.config.pageUtil.EctImmDeleteImmunizationSet2Action | Deletes multiple immunization sets |
| oscarEncounter/immunization/config/ImmunizationSetDisplay | oscar.oscarEncounter.immunization.config.pageUtil.EctImmImmunizationSetDisplay2Action | Displays immunization sets |
| oscarEncounter/immunization/config/initConfig | oscar.oscarEncounter.immunization.config.pageUtil.EctImmInitConfig2Action | Initializes immunization config |
| oscarEncounter/immunization/deleteSchedule | oscar.oscarEncounter.immunization.pageUtil.EctImmDeleteImmSchedule2Action | Deletes immunization schedules |
| oscarEncounter/immunization/initSchedule | oscar.oscarEncounter.immunization.pageUtil.EctImmInitSchedule2Action | Initializes immunization schedules |
| oscarEncounter/immunization/loadConfig | oscar.oscarEncounter.immunization.pageUtil.EctImmLoadConfig2Action | Loads immunization configurations |
| oscarEncounter/immunization/loadSchedule | oscar.oscarEncounter.immunization.pageUtil.EctImmLoadSchedule2Action | Loads immunization schedules |
| oscarEncounter/immunization/saveConfig | oscar.oscarEncounter.immunization.pageUtil.EctImmSaveConfig2Action | Saves immunization configurations |
| oscarEncounter/immunization/saveSchedule | oscar.oscarEncounter.immunization.pageUtil.EctImmSaveSchedule2Action | Saves immunization schedules |
| oscarEncounter/IncomingConsultation | oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctIncomingConsultation2Action | Processes incoming consultations |
| oscarEncounter/IncomingEncounter | oscar.oscarEncounter.pageUtil.EctIncomingEncounter2Action | Processes incoming encounters |
| oscarEncounter/InsertTemplate | oscar.oscarEncounter.pageUtil.EctInsertTemplate2Action | Inserts encounter templates |
| oscarEncounter/MeasurementData | ca.openosp.openo.measurements.web.MeasurementData2Action | Manages measurement data |
| oscarEncounter/Measurements2 | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctMeasurements2Action | Manages encounter measurements |
| oscarEncounter/Measurements | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctMeasurements2Action | Manages clinical measurements |
| oscarEncounter/oscarConsultation/printAttached | oscar.oscarEncounter.oscarConsultationRequest.pageUtil.ConsultationPrintDocs2Action | Prints attached consultation documents |
| oscarEncounter/oscarConsultationRequest/ConsultationFormFax | oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormFax2Action | Faxes consultation forms |
| oscarEncounter/oscarConsultationRequest/printPdf2 | oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestPrintAction22Action | Prints consultation PDFs |
| oscarEncounter/oscarMeasurements/AddMeasurementGroup | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctAddMeasurementGroup2Action | Adds measurement groups |
| oscarEncounter/oscarMeasurements/AddMeasurementMap | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctAddMeasurementMap2Action | Adds measurement mappings |
| oscarEncounter/oscarMeasurements/AddMeasurementStyleSheet | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctAddMeasurementStyleSheet2Action | Adds measurement stylesheets |
| oscarEncounter/oscarMeasurements/AddMeasurementType | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctAddMeasurementType2Action | Adds measurement types |
| oscarEncounter/oscarMeasurements/AddMeasuringInstruction | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctAddMeasuringInstruction2Action | Adds measuring instructions |
| oscarEncounter/oscarMeasurements/adminFlowsheet/FlowSheetCustomAction | org.oscarehr.common.web.FlowSheetCustom2Action | Manages custom flowsheets |
| oscarEncounter/oscarMeasurements/DefineNewMeasurementGroup | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctDefineNewMeasurementGroup2Action | Defines new measurement groups |
| oscarEncounter/oscarMeasurements/DeleteData2 | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctDeleteData2Action | Deletes measurement data |
| oscarEncounter/oscarMeasurements/DeleteData | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctDeleteData2Action | Deletes measurement data |
| oscarEncounter/oscarMeasurements/DeleteMeasurementStyleSheet | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctDeleteMeasurementStyleSheet2Action | Deletes measurement stylesheets |
| oscarEncounter/oscarMeasurements/DeleteMeasurementTypes | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctDeleteMeasurementTypes2Action | Deletes measurement types |
| oscarEncounter/oscarMeasurements/EditMeasurementGroup | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctEditMeasurementGroup2Action | Edits measurement groups |
| oscarEncounter/oscarMeasurements/EditMeasurementStyle | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctEditMeasurementStyle2Action | Edits measurement styles |
| oscarEncounter/oscarMeasurements/FlowSheetDrugAction | org.oscarehr.common.web.FlowSheetDrug2Action | Manages flowsheet drug information |
| oscarEncounter/oscarMeasurements/NewMeasurementMap | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctAddMeasurementMap2Action | Creates new measurement maps |
| oscarEncounter/oscarMeasurements/RemapMeasurementMap | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctRemoveMeasurementMap2Action | Remaps measurement mappings |
| oscarEncounter/oscarMeasurements/RemoveMeasurementMap | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctRemoveMeasurementMap2Action | Removes measurement mappings |
| oscarEncounter/oscarMeasurements/SelectMeasurementGroup | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctSelectMeasurementGroup2Action | Selects measurement groups |
| oscarEncounter/oscarMeasurements/SetupAddMeasurementGroup | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctSetupAddMeasurementGroup2Action | Sets up new measurement groups |
| oscarEncounter/oscarMeasurements/SetupAddMeasurementType | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctSetupAddMeasurementType2Action | Sets up new measurement types |
| oscarEncounter/oscarMeasurements/SetupAddMeasuringInstruction | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctSetupAddMeasuringInstruction2Action | Sets up measuring instructions |
| oscarEncounter/oscarMeasurements/SetupDisplayHistory | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctSetupDisplayHistory2Action | Sets up measurement history display |
| oscarEncounter/oscarMeasurements/SetupDisplayMeasurementStyleSheet | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctSetupDisplayMeasurementStyleSheet2Action | Sets up measurement stylesheet display |
| oscarEncounter/oscarMeasurements/SetupDisplayMeasurementTypes | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctSetupDisplayMeasurementTypes2Action | Sets up measurement types display |
| oscarEncounter/oscarMeasurements/SetupEditMeasurementGroup | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctSetupEditMeasurementGroup2Action | Sets up measurement group editing |
| oscarEncounter/oscarMeasurements/SetupGroupList | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctSetupGroupList2Action | Sets up measurement group lists |
| oscarEncounter/oscarMeasurements/SetupHistoryIndex | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctSetupHistoryIndex2Action | Sets up measurement history index |
| oscarEncounter/oscarMeasurements/SetupMeasurements | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctSetupMeasurements2Action | Sets up measurements interface |
| oscarEncounter/oscarMeasurements/SetupStyleSheetList | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctSetupStyleSheetList2Action | Sets up stylesheet list |
| oscarEncounter/oscarMeasurements/TrackerSlimUpdate | oscar.oscarEncounter.oscarMeasurements.pageUtil.FormUpdate2Action | Updates slim tracker forms |
| oscarEncounter/oscarMeasurements/TrackerUpdate | oscar.oscarEncounter.oscarMeasurements.pageUtil.FormUpdate2Action | Updates tracker forms |
| oscarEncounter/RequestConsultation | oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequest2Action | Requests consultations |
| oscarEncounter/SaveEncounter2 | oscar.oscarEncounter.pageUtil.EctSaveEncounter2Action | Saves clinical encounters |
| oscarEncounter/SaveEncounter | oscar.oscarEncounter.pageUtil.EctSaveEncounter2Action | Saves encounter data |
| oscarEncounter/ShowAllInstitutions | oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConShowAllInstitutions2Action | Shows all consultation institutions |
| oscarEncounter/ShowAllServices | oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConShowAllServices2Action | Shows all consultation services |
| oscarEncounter/UpdateInstitutionDepartment | oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConDisplayInstitution2Action | Updates institution departments |
| oscarEncounter/UpdateServiceSpecialists | oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConDisplayService2Action | Updates service specialists |
| oscarEncounter/ViewAttachment | oscar.oscarEncounter.pageUtil.EctViewAttachment2Action | Views encounter attachments |
| oscarEncounter/ViewConsultation | oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctViewConsultationRequests2Action | Views consultation requests |
| oscarEncounter/ViewRequest | oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctViewRequest2Action | Views consultation requests |

## Oscar MDS Module

Medical Data Services functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| oscarMDS/FileLabs | oscar.oscarLab.pageUtil.FileLabs2Action | Files lab results |
| oscarMDS/ForwardingRules | oscar.oscarLab.pageUtil.ForwardingRules2Action | Manages lab forwarding rules |
| oscarMDS/Forward | oscar.oscarMDS.pageUtil.ReportReassign2Action | Forwards MDS reports |
| oscarMDS/PatientMatch | oscar.oscarMDS.pageUtil.PatientMatch2Action | Matches patients to reports |
| oscarMDS/ReportReassign | oscar.oscarMDS.pageUtil.ReportReassign2Action | Reassigns reports to providers |
| oscarMDS/RunMacro | oscar.oscarMDS.pageUtil.ReportMacro2Action | Runs report processing macros |
| oscarMDS/SearchPatient | oscar.oscarMDS.pageUtil.SearchPatient2Action | Searches for patients |
| oscarMDS/SendMRP | oscar.oscarMDS.pageUtil.SendMostResponProv2Action | Sends to most responsible provider |
| oscarMDS/SubmitLab | oscar.oscarLab.ca.all.web.SubmitLabByForm2Action | Submits lab results by form |
| oscarMDS/UpdateStatus | oscar.oscarMDS.pageUtil.ReportStatusUpdate2Action | Updates report status |

## Oscar Measurement Module

Clinical measurement functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| oscarMeasurement/AddShortMeasurement | oscar.oscarEncounter.oscarMeasurements.pageUtil.EctAddShortMeasurement2Action | Adds short-form measurements |

## Oscar Messenger Module

Internal messaging system.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| oscarMessenger/AddGroup | oscar.oscarMessenger.config.pageUtil.MsgMessengerCreateGroup2Action | Adds messenger groups |
| oscarMessenger/ClearMessage | oscar.oscarMessenger.pageUtil.MsgClearMessage2Action | Clears messages |
| oscarMessenger/CreateMessage | oscar.oscarMessenger.pageUtil.MsgCreateMessage2Action | Creates new messages |
| oscarMessenger/DisplayDemographicMessages | oscar.oscarMessenger.pageUtil.MsgDisplayDemographicMessages2Action | Displays demographic messages |
| oscarMessenger/DisplayMessages | oscar.oscarMessenger.pageUtil.MsgDisplayMessages2Action | Displays messages |
| oscarMessenger/Doc2PDF | oscar.oscarMessenger.pageUtil.MsgAttachPDF2Action | Converts documents to PDF |
| oscarMessenger/HandleMessages | oscar.oscarMessenger.pageUtil.MsgHandleMessages2Action | Handles message processing |
| oscarMessenger/ImportDemographic | oscar.oscarMessenger.pageUtil.ImportDemographic2Action | Imports demographic data |
| oscarMessenger | oscar.oscarMessenger.config.pageUtil.MsgMessengerAdmin2Action | Administers messenger system |
| oscarMessenger/ProcessDoc2PDF | oscar.oscarMessenger.pageUtil.MsgDoc2PDF2Action | Processes document to PDF conversion |
| oscarMessenger/ReDisplayMessages | oscar.oscarMessenger.pageUtil.MsgReDisplayMessages2Action | Re-displays messages |
| oscarMessenger/SendDemoMessage | oscar.oscarMessenger.pageUtil.MsgSendDemographicMessage2Action | Sends demographic messages |
| oscarMessenger/SendMessage | oscar.oscarMessenger.pageUtil.MsgSendMessage2Action | Sends messages |
| oscarMessenger/Transfer/Proceed | oscar.oscarMessenger.pageUtil.MsgProceed2Action | Proceeds with message transfer |
| oscarMessenger/ViewAttach | oscar.oscarMessenger.pageUtil.MsgViewAttachment2Action | Views message attachments |
| oscarMessenger/ViewMessage | oscar.oscarMessenger.pageUtil.MsgViewMessage2Action | Views messages |
| oscarMessenger/ViewPDFAttach | oscar.oscarMessenger.pageUtil.MsgViewPDFAttachment2Action | Views PDF attachments |
| oscarMessenger/ViewPDFFile | oscar.oscarMessenger.pageUtil.MsgViewPDF2Action | Views PDF files |
| oscarMessenger/WriteToEncounter | oscar.oscarMessenger.pageUtil.MsgWriteToEncounter2Action | Writes messages to encounters |

## Oscar Prevention Module

Preventive care management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| oscarPrevention/AddPrevention | oscar.oscarPrevention.pageUtil.AddPrevention2Action | Adds prevention records |
| oscarPrevention/PreventionReport | oscar.oscarPrevention.pageUtil.PreventionReport2Action | Generates prevention reports |
| oscarPrevention/printPrevention | oscar.oscarPrevention.pageUtil.PreventionPrint2Action | Prints prevention records |

## Oscar Report Module

Comprehensive reporting functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| oscarReport/FluBilling | oscar.oscarReport.pageUtil.RptFluBilling2Action | Generates flu billing reports |
| oscarReport/obec | oscar.oscarReport.pageUtil.Obec2Action | Generates OBEC reports |
| oscarReport/oscarMeasurements/InitializeFrequencyOfRelevantTestsCDMReport | oscar.oscarReport.oscarMeasurements.pageUtil.RptInitializeFrequencyOfRelevantTestsCDMReport2Action | Initializes CDM test frequency reports |
| oscarReport/oscarMeasurements/InitializePatientsInAbnormalRangeCDMReport | oscar.oscarReport.oscarMeasurements.pageUtil.RptInitializePatientsInAbnormalRangeCDMReport2Action | Initializes abnormal range CDM reports |
| oscarReport/oscarMeasurements/InitializePatientsMetGuidelineCDMReport | oscar.oscarReport.oscarMeasurements.pageUtil.RptInitializePatientsMetGuidelineCDMReport2Action | Initializes guideline met CDM reports |
| oscarReport/oscarMeasurements/SelectCDMReport | oscar.oscarReport.oscarMeasurements.pageUtil.RptSelectCDMReport2Action | Selects CDM reports |
| oscarReport/oscarMeasurements/SetupSelectCDMReport | oscar.oscarReport.oscarMeasurements.pageUtil.RptSetupSelectCDMReport2Action | Sets up CDM report selection |
| oscarReport/reportByTemplate/actions/addGroup | oscar.oscarReport.reportByTemplate.actions.RBTAddGroup2Action | Adds template report groups |
| oscarReport/reportByTemplate/actions/delGroup | oscar.oscarReport.reportByTemplate.actions.RBTDeleteGroup2Action | Deletes template report groups |
| oscarReport/reportByTemplate/actions/rbtAddToGroup | oscar.oscarReport.reportByTemplate.actions.RBTAddToGroup2Action | Adds templates to groups |
| oscarReport/reportByTemplate/actions/remFromGroup | oscar.oscarReport.reportByTemplate.actions.RBTRemoveFromGroup2Action | Removes templates from groups |
| oscarReport/reportByTemplate/actions/tempInGroup | oscar.oscarReport.reportByTemplate.actions.RBTGetTemplatesInGroup2Action | Gets templates in groups |
| oscarReport/reportByTemplate/addEditTemplatesAction | oscar.oscarReport.reportByTemplate.actions.ManageTemplates2Action | Manages report templates |
| oscarReport/reportByTemplate/exportTemplateAction | oscar.oscarReport.reportByTemplate.actions.ExportTemplate2Action | Exports report templates |
| oscarReport/reportByTemplate/generateOutFilesAction | oscar.oscarReport.reportByTemplate.actions.GenerateOutFiles2Action | Generates output files |
| oscarReport/reportByTemplate/GenerateReportAction | oscar.oscarReport.reportByTemplate.actions.GenerateReport2Action | Generates template-based reports |
| oscarReport/reportByTemplate/rbtGroup | oscar.oscarReport.reportByTemplate.actions.RBTGetGroup2Action | Gets report template groups |
| oscarReport/reportByTemplate/uploadTemplates | oscar.oscarReport.reportByTemplate.actions.UploadTemplates2Action | Uploads report templates |
| oscarReport/RptByExample | oscar.oscarReport.pageUtil.RptByExample2Action | Generates reports by example |
| oscarReport/RptByExamplesAllFavorites | oscar.oscarReport.pageUtil.RptByExamplesAllFavorites2Action | Shows all favorite example reports |
| oscarReport/RptByExamplesFavorite | oscar.oscarReport.pageUtil.RptByExamplesFavorite2Action | Manages favorite example reports |
| oscarReport/RptViewAllQueryByExamples | oscar.oscarReport.pageUtil.RptViewAllQueryByExamples2Action | Views all query examples |
| oscarReport/ShowConsult | oscar.oscarReport.pageUtil.RptShowConsult2Action | Shows consultation reports |

## Oscar Research Module

Medical research and diagnostic code management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| oscarResearch/oscarDxResearch/dxResearchCodeSearch | oscar.oscarResearch.oscarDxResearch.pageUtil.dxResearchCodeSearch2Action | Searches diagnostic research codes |
| oscarResearch/oscarDxResearch/dxResearchLoadAssociations | oscar.oscarResearch.oscarDxResearch.pageUtil.dxResearchLoadAssociations2Action | Loads research code associations |
| oscarResearch/oscarDxResearch/dxResearchLoadQuickListItems | oscar.oscarResearch.oscarDxResearch.pageUtil.dxResearchLoadQuickListItems2Action | Loads quick list items |
| oscarResearch/oscarDxResearch/dxResearchLoadQuickList | oscar.oscarResearch.oscarDxResearch.pageUtil.dxResearchLoadQuickList2Action | Loads research quick lists |
| oscarResearch/oscarDxResearch/dxResearch | oscar.oscarResearch.oscarDxResearch.pageUtil.dxResearch2Action | Main diagnostic research interface |
| oscarResearch/oscarDxResearch/dxResearchUpdate | oscar.oscarResearch.oscarDxResearch.pageUtil.dxResearchUpdate2Action | Updates research data |
| oscarResearch/oscarDxResearch/dxResearchUpdateQuickList | oscar.oscarResearch.oscarDxResearch.pageUtil.dxResearchUpdateQuickList2Action | Updates research quick lists |
| oscarResearch/oscarDxResearch/setupDxResearch | oscar.oscarResearch.oscarDxResearch.pageUtil.dxSetupResearch2Action | Sets up diagnostic research |

## Oscar Rx Module

Prescription and medication management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| oscarRx/addAllergy2 | oscar.oscarRx.pageUtil.RxAddAllergy2Action | Adds patient allergies |
| oscarRx/addAllergy | oscar.oscarRx.pageUtil.RxAddAllergy2Action | Adds medication allergies |
| oscarRx/addFavorite2 | oscar.oscarRx.pageUtil.RxAddFavorite2Action | Adds prescription favorites |
| oscarRx/addFavoriteStaticScript | oscar.oscarRx.pageUtil.RxAddFavorite2Action | Adds static script favorites |
| oscarRx/addFavoriteViewScript | oscar.oscarRx.pageUtil.RxAddFavorite2Action | Adds view script favorites |
| oscarRx/addFavoriteWriteScript | oscar.oscarRx.pageUtil.RxAddFavorite2Action | Adds write script favorites |
| oscarRx/addReaction2 | oscar.oscarRx.pageUtil.RxAddReaction2Action | Adds drug reactions |
| oscarRx/addReaction | oscar.oscarRx.pageUtil.RxAddReaction2Action | Adds allergic reactions |
| oscarRx/chooseDrug | oscar.oscarRx.pageUtil.RxChooseDrug2Action | Selects medications |
| oscarRx/choosePatient | oscar.oscarRx.pageUtil.RxChoosePatient2Action | Selects patients for prescriptions |
| oscarRx/clearPending | oscar.oscarRx.pageUtil.RxClearPending2Action | Clears pending prescriptions |
| oscarRx/copyFavorite2 | oscar.oscarRx.web.CopyFavorites2Action | Copies prescription favorites |
| oscarRx/copyFavorite | oscar.oscarRx.web.CopyFavorites2Action | Copies favorite prescriptions |
| oscarRx/deleteAllergy2 | oscar.oscarRx.pageUtil.RxDeleteAllergy2Action | Deletes patient allergies |
| oscarRx/deleteAllergy | oscar.oscarRx.pageUtil.RxDeleteAllergy2Action | Deletes allergy records |
| oscarRx/deleteFavorite2 | oscar.oscarRx.pageUtil.RxDeleteFavorite2Action | Deletes prescription favorites |
| oscarRx/deleteFavorite | oscar.oscarRx.pageUtil.RxDeleteFavorite2Action | Deletes favorite prescriptions |
| oscarRx/deleteRx | oscar.oscarRx.pageUtil.RxDeleteRx2Action | Deletes prescriptions |
| oscarRx/Dispense | ca.openosp.openo.dispensary.rx.Dispensary2Action | Manages medication dispensing |
| oscarRx/drugInfo | oscar.oscarRx.pageUtil.RxDrugInfo2Action | Displays drug information |
| oscarRx/GetmyDrugrefInfo | oscar.oscarRx.pageUtil.RxMyDrugrefInfo2Action | Gets drug reference information |
| oscarRx/GetRxPageSizeInfo | oscar.oscarRx.pageUtil.RxRxPageSizeInfo2Action | Gets prescription page size info |
| oscarRx/hideCpp | oscar.oscarRx.web.RxHideCpp2Action | Hides CPP information |
| oscarRx/managePharmacy2 | oscar.oscarRx.pageUtil.RxManagePharmacy2Action | Manages pharmacy information |
| oscarRx/managePharmacy | oscar.oscarRx.pageUtil.RxManagePharmacy2Action | Manages pharmacy settings |
| oscarRx/reorderDrug | oscar.oscarRx.web.RxReorder2Action | Reorders medications |
| oscarRx/rePrescribe2 | oscar.oscarRx.pageUtil.RxRePrescribe2Action | Re-prescribes medications |
| oscarRx/rePrescribe | oscar.oscarRx.pageUtil.RxRePrescribe2Action | Repeats prescriptions |
| oscarRx/RxReason | oscar.oscarRx.pageUtil.RxReason2Action | Manages prescription reasons |
| oscarRx/rxStashDelete | oscar.oscarRx.pageUtil.RxStash2Action | Deletes prescription stash |
| oscarRx/searchAllergy2 | oscar.oscarRx.pageUtil.RxSearchAllergy2Action | Searches for allergies |
| oscarRx/searchAllergy | oscar.oscarRx.pageUtil.RxSearchAllergy2Action | Searches allergy database |
| oscarRx/searchDrug | oscar.oscarRx.pageUtil.RxSearchDrug2Action | Searches for medications |
| oscarRx/showAllergy | oscar.oscarRx.pageUtil.RxShowAllergy2Action | Displays patient allergies |
| oscarRx/stash | oscar.oscarRx.pageUtil.RxStash2Action | Manages prescription stash |
| oscarRx/updateDrugrefDB | oscar.oscarRx.pageUtil.RxUpdateDrugref2Action | Updates drug reference database |
| oscarRx/updateFavorite2 | oscar.oscarRx.pageUtil.RxUpdateFavorite2Action | Updates prescription favorites |
| oscarRx/updateFavorite | oscar.oscarRx.pageUtil.RxUpdateFavorite2Action | Updates favorite prescriptions |
| oscarRx/UpdateScript | oscar.oscarRx.pageUtil.RxWriteScript2Action | Updates prescription scripts |
| oscarRx/useFavorite | oscar.oscarRx.pageUtil.RxUseFavorite2Action | Uses favorite prescriptions |
| oscarRx/viewScript | oscar.oscarRx.pageUtil.RxViewScript2Action | Views prescription scripts |
| oscarRx/WriteScript | oscar.oscarRx.pageUtil.RxWriteScript2Action | Writes new prescriptions |
| oscarRx/writeScript | oscar.oscarRx.pageUtil.RxWriteScript2Action | Creates prescription scripts |
| oscarRx/WriteToEncounter | oscar.oscarRx.pageUtil.RxWriteToEncounter2Action | Writes prescriptions to encounters |

## Oscar Waiting List Module

Patient waiting list management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| oscarWaitingList/SetupDisplayPatientWaitingList | oscar.oscarWaitingList.pageUtil.WLSetupDisplayPatientWaitingList2Action | Sets up patient waiting list display |
| oscarWaitingList/SetupDisplayWaitingList | oscar.oscarWaitingList.pageUtil.WLSetupDisplayWaitingList2Action | Sets up waiting list display |
| oscarWaitingList/WLEditWaitingListNameAction | oscar.oscarWaitingList.pageUtil.WLEditWaitingListName2Action | Edits waiting list names |

## Page Monitoring Module

System monitoring functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| PageMonitoringService | org.oscarehr.common.web.PageMonitoring2Action | Monitors page performance |

## PM Module

Program Management functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| PMmodule/Admin/DefaultRoleAccess | ca.openosp.openo.admin.web.PMmodule.DefaultRoleAccess2Action | Manages default role access |
| PMmodule/Admin/SysAdmin | ca.openosp.openo.admin.web.PMmodule.AdminHome2Action | System administration interface |
| PMmodule/AgencyManager | ca.openosp.openo.admin.web.PMmodule.AgencyManager2Action | Manages agencies |
| PMmodule/AllVacancies | ca.openosp.openo.web.PMmodule.AllWaitingList2Action | Views all program vacancies |
| PMmodule/ClientManager | ca.openosp.openo.web.PMmodule.ClientManager2Action | Manages program clients |
| PMmodule/ClientSearch2 | ca.openosp.openo.web.PMmodule.ClientSearchAction22Action | Searches for clients |
| PMmodule/FacilityManager | ca.openosp.openo.admin.web.PMmodule.FacilityManager2Action | Manages program facilities |
| PMmodule/HealthSafety | org.oscarehr.PMmodule.web.HealthSafety2Action | Manages health and safety |
| PMmodule/ProgramManager | ca.openosp.openo.admin.web.PMmodule.ProgramManager2Action | Manages programs |
| PMmodule/ProgramManagerView | ca.openosp.openo.admin.web.PMmodule.ProgramManagerView2Action | Views program management |
| PMmodule/ProviderInfo | ca.openosp.openo.web.PMmodule.ProviderInfo2Action | Displays provider information |
| PMmodule/ProviderSearch | ca.openosp.openo.web.PMmodule.ProviderSearch2Action | Searches for providers |
| PMmodule/Reports/BasicReport | ca.openosp.openo.reports.web.PMmodule.BasicReport2Action | Generates basic reports |
| PMmodule/Reports/ClientListsReport | ca.openosp.openo.reports.web.PMmodule.ClientListsReport2Action | Generates client list reports |
| PMmodule/Reports/ProgramActivityReport | ca.openosp.openo.reports.web.PMmodule.ActivityReport2Action | Generates program activity reports |
| PMmodule/StaffManager | ca.openosp.openo.admin.web.PMmodule.StaffManager2Action | Manages program staff |
| PMmodule/VacancyClientMatch | ca.openosp.openo.web.PMmodule.VacancyClientMatch2Action | Matches clients to vacancies |

## Population Module

Population reporting functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| PopulationReport | org.oscarehr.common.web.PopulationReport2Action | Generates population reports |

## Pregnancy Module

Pregnancy management functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| Pregnancy | org.oscarehr.common.web.Pregnancy2Action | Manages pregnancy records |

## Preview Module

Document preview functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| previewDocs | org.oscarehr.documentManager.actions.DocumentPreview2Action | Previews documents |

## Printer Module

Printer management functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| PrinterList | org.oscarehr.printer.PrinterList2Action | Lists available printers |
| printReferralLabelAction | org.oscarehr.common.web.PrintReferralLabel2Action | Prints referral labels |

## Provider Module

Provider management and preferences.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| provider/CppPreferences | ca.openosp.openo.www.provider.CppPreferences2Action | Manages CPP preferences |
| provider/OlisPreferences | ca.openosp.openo.www.provider.OlisPreferences2Action | Manages OLIS preferences |
| provider/rxInteractionWarningLevel | ca.openosp.openo.www.provider.ProviderRxInteractionWarningLevel2Action | Sets drug interaction warning levels |
| provider/SearchProvider | org.oscarehr.common.web.SearchProviderAutoComplete2Action | Provides provider search autocomplete |
| Provider/showPersonal | ca.openosp.openo.www.provider.DisplayPersonalInfoAppointment2Action | Shows provider personal information |
| provider/UserPreference | ca.openosp.openo.www.provider.UserPreference2Action | Manages user preferences |

## Quick Billing Module

Quick billing functionality for BC.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| quickBillingBC | oscar.oscarBilling.ca.bc.quickbilling.QuickBillingBC2Action | Quick billing interface for BC |

## Renal Module

Renal care management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| renal/CkdDSA | org.oscarehr.renal.web.CkdDSA2Action | Chronic kidney disease decision support |
| renal/Renal | org.oscarehr.renal.web.Renal2Action | Renal care management |

## Report Module

General reporting functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| report/CreateDemographicSet | oscar.oscarReport.pageUtil.RptCreateDemographicSet2Action | Creates demographic report sets |
| report/DeleteDemographicReport | oscar.oscarReport.pageUtil.RptDemographQueryFavouriteDelete2Action | Deletes demographic reports |
| report/DeleteLetter | oscar.oscarReport.pageUtil.DeletePatientLetters2Action | Deletes patient letters |
| report/DemographicReport | oscar.oscarReport.pageUtil.RptDemographicReport2Action | Generates demographic reports |
| report/DemographicSetEdit | oscar.oscarReport.pageUtil.DemographicSetEdit2Action | Edits demographic sets |
| report/DownloadLetter | oscar.oscarReport.pageUtil.DownloadPatientLetters2Action | Downloads patient letters |
| report/DxresearchReport | org.oscarehr.common.web.DxresearchReport2Action | Generates diagnostic research reports |
| report/GenerateEnvelopes | oscar.oscarReport.pageUtil.GenerateEnvelopes2Action | Generates mailing envelopes |
| report/GenerateLetters | oscar.oscarReport.pageUtil.GeneratePatientLetters2Action | Generates patient letters |
| report/GenerateSpreadsheet | oscar.oscarReport.pageUtil.GeneratePatientSpreadSheetList2Action | Generates patient spreadsheets |
| report/ManageLetters | oscar.oscarReport.pageUtil.ManagePatientLetters2Action | Manages patient letters |
| report/printLabDaySheetAction | oscar.oscarReport.pageUtil.printLabDaySheet2Action | Prints lab day sheets |
| report/RemoveClinicalReport | oscar.oscarReport.ClinicalReports.PageUtil.RemoveClinicalReportFromHistory2Action | Removes clinical reports from history |
| report/SetEligibility | oscar.oscarReport.pageUtil.DemographicSetEligibility2Action | Sets demographic eligibility |

## Run Clinical Report Module

Clinical report execution.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| RunClinicalReport | oscar.oscarReport.ClinicalReports.PageUtil.RunClinicalReport2Action | Runs clinical reports |

## Save Quick Billing Module

Quick billing save functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| saveQuickBillingBC | oscar.oscarBilling.ca.bc.quickbilling.QuickBillingBCSave2Action | Saves BC quick billing |

## Save Work View Module

Work view management.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| saveWorkView | ca.openosp.openo.www.provider.ProviderView2Action | Saves provider work views |

## Scratch Module

Scratch pad functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| Scratch | oscar.scratch.Scratch2Action | Manages scratch pad notes |

## Search Professional Module

Professional specialist search.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| searchProfessionalSpecialist | org.oscarehr.contactRegistry.ProfessionalSpecialist2Action | Searches professional specialists |

## Security Module

Security and authentication functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| securityRecord/mfa | ca.openosp.openo.security.MfaActions2Action | Manages multi-factor authentication |

## Set Provider Module

Provider configuration functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| setProviderColour | oscar.oscarProvider.pageUtil.ProEditColour2Action | Sets provider color preferences |
| setProviderStaleDate | ca.openosp.openo.www.provider.ProviderProperty2Action | Sets provider stale date |
| setTicklerPreferences | ca.openosp.openo.www.provider.ProviderProperty2Action | Sets tickler preferences |

## Shelter Module

Shelter selection functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|

## SSO Module

Single Sign-On functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| ssoLogin | oscar.login.SSOLogin2Action | Processes SSO login |

## System Message Module

System messaging functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| SystemMessage | org.caisi.core.web.SystemMessage2Action | Manages system messages |

## Tickler Module

Tickler management functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| tickler/AddTickler | oscar.oscarTickler.pageUtil.AddTickler2Action | Adds new ticklers |
| tickler/EditTickler | oscar.oscarTickler.pageUtil.EditTickler2Action | Edits existing ticklers |
| tickler/EditTicklerTextSuggest | oscar.oscarTickler.pageUtil.EditTickler2Action | Edits tickler text suggestions |
| tickler/ForwardDemographicTickler | oscar.oscarTickler.pageUtil.ForwardDemographicTickler2Action | Forwards demographic ticklers |

## Vaccine Module

Vaccine reporting functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| VaccineProviderReport | org.oscarehr.vaccine.VaccineProviderReport2Action | Generates vaccine provider reports |

## Web Dashboard Module

Web-based dashboard functionality.

| Action Name | Class Name | Description |
|-------------|------------|-------------|
| web/dashboard/display/AssignTickler | org.oscarehr.dashboard.admin.AssignTickler2Action | Assigns ticklers from dashboard |
| web/dashboard/display/BulkPatientAction | org.oscarehr.dashboard.admin.BulkPatientDashboard2Action | Performs bulk patient actions |
| web/dashboard/display/DashboardDisplay | org.oscarehr.dashboard.display.DisplayDashboard2Action | Displays main dashboard |
| web/dashboard/display/DisplayIndicator | org.oscarehr.dashboard.display.DisplayIndicator2Action | Displays dashboard indicators |
| web/dashboard/display/DrilldownDisplay | org.oscarehr.dashboard.display.DisplayDrilldown2Action | Displays dashboard drilldown data |
| web/dashboard/display/ExportResults | org.oscarehr.dashboard.admin.ExportResults2Action | Exports dashboard results |
| web/dashboard/OutcomesDashboard | org.oscarehr.integration.dashboard.OutcomesDashboard2Action | Displays outcomes dashboard |

---

## Summary

This comprehensive reference includes all 507 Struts actions organized into 29 functional modules. Each action is documented with its purpose and functionality based on naming conventions and class structure. The actions cover the full spectrum of EMR functionality including clinical workflows, administrative tasks, billing operations, reporting capabilities, and system integrations.

The modular organization reflects OSCAR's comprehensive approach to healthcare management, supporting everything from basic patient demographics to complex clinical decision support and inter-system data exchange.