package org.oscarehr.managers;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.model.enumerator.CppCode;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.conversion.CaseManagementIssueConverter;
import org.oscarehr.ws.rest.to.model.NoteExtTo1;
import org.oscarehr.ws.rest.to.model.NoteTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteManager {
    
    private static Logger logger = MiscUtils.getLogger();

    @Autowired
    private CaseManagementManager caseManagementManager;

    @Autowired
    private CaseManagementNoteDAO caseManagementNoteDAO;

    @Autowired
    private IssueDAO issueDAO;

    public List<NoteTo1> getCppNotes(LoggedInInfo loggedInInfo, Integer demographicNo){
        List<CaseManagementNote> notes = new ArrayList<>(caseManagementNoteDAO.findNotesByDemographicAndIssueCode(demographicNo, CppCode.toArray()));
        List<NoteTo1> noteTo1s = new ArrayList<>();
        for(CaseManagementNote note : notes){
            noteTo1s.add(convertNote(loggedInInfo, note));
        }
        return noteTo1s;
    }

    public List<NoteTo1> getActiveCppNotes(LoggedInInfo loggedInInfo, Integer demographicNo){
        String[] issueIds = getIssueIds(null);
        List<CaseManagementNote> notes = new ArrayList<>(caseManagementNoteDAO.getActiveNotesByDemographic(String.valueOf(demographicNo), issueIds));
        List<NoteTo1> noteTo1s = new ArrayList<>();
        for(CaseManagementNote note : notes){
            noteTo1s.add(convertNote(loggedInInfo, note));
        }
        return noteTo1s;
    }

    public List<NoteTo1> getActiveCppNotes(LoggedInInfo loggedInInfo, Integer demographicNo, String[] newCppCodes){
        String[] issueIds = getIssueIds(newCppCodes);
        List<CaseManagementNote> notes = new ArrayList<>(caseManagementNoteDAO.getActiveNotesByDemographic(String.valueOf(demographicNo), issueIds));
        List<NoteTo1> noteTo1s = new ArrayList<>();
        for(CaseManagementNote note : notes){
            noteTo1s.add(convertNote(loggedInInfo, note));
        }
        return noteTo1s;
    }

    public NoteTo1 convertNote(LoggedInInfo loggedInInfo, CaseManagementNote caseManagementNote){
        NoteTo1 note = new NoteTo1();
        note.setNoteId(caseManagementNote.getId().intValue());
        note.setIsSigned(caseManagementNote.isSigned());
        note.setRevision(caseManagementNote.getRevision());
        note.setObservationDate(caseManagementNote.getObservation_date());
        note.setUpdateDate(caseManagementNote.getUpdate_date());
        note.setProviderName(caseManagementNote.getProviderName());
        note.setProviderNo(caseManagementNote.getProviderNo());
        note.setStatus(caseManagementNote.getStatus());
        note.setProgramName(caseManagementNote.getProgramName());
        note.setRoleName(caseManagementNote.getRoleName());
        note.setUuid(caseManagementNote.getUuid());
        note.setHasHistory(caseManagementNote.getHasHistory());
        note.setLocked(caseManagementNote.isLocked());
        note.setNote(caseManagementNote.getNote());
        note.setRxAnnotation(caseManagementNote.isRxAnnotation());
        note.setEncounterType(caseManagementNote.getEncounter_type());
        note.setPosition(caseManagementNote.getPosition());
        note.setAppointmentNo(caseManagementNote.getAppointmentNo());
        note.setCpp(false);
        
        //get all note extra values	
        List<CaseManagementNoteExt> lcme = new ArrayList<CaseManagementNoteExt>();
        lcme.addAll(caseManagementManager.getExtByNote( caseManagementNote.getId() ));

        NoteExtTo1 noteExt = new NoteExtTo1();
        noteExt.setNoteId( caseManagementNote.getId() );

        for(CaseManagementNoteExt l : lcme){
            logger.debug("NOTE EXT KEY:" +l.getKeyVal() + l.getValue());

            if(l.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)){
                noteExt.setStartDate(l.getDateValueStr());
            }else if(l.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)){
                noteExt.setResolutionDate(l.getDateValueStr());
            }else if(l.getKeyVal().equals(CaseManagementNoteExt.PROCEDUREDATE)){
                noteExt.setProcedureDate(l.getDateValueStr());
            }else if(l.getKeyVal().equals(CaseManagementNoteExt.AGEATONSET)){
                noteExt.setAgeAtOnset(l.getValue());
            }else if(l.getKeyVal().equals(CaseManagementNoteExt.TREATMENT)){
                noteExt.setTreatment(l.getValue());
            }else if(l.getKeyVal().equals(CaseManagementNoteExt.PROBLEMSTATUS)){
                noteExt.setProblemStatus(l.getValue());
            }else if(l.getKeyVal().equals(CaseManagementNoteExt.EXPOSUREDETAIL)){
                noteExt.setExposureDetail(l.getValue());
            }else if(l.getKeyVal().equals(CaseManagementNoteExt.RELATIONSHIP)){
                noteExt.setRelationship(l.getValue());
            }else if(l.getKeyVal().equals(CaseManagementNoteExt.LIFESTAGE)){
                noteExt.setLifeStage(l.getValue());
            }else if(l.getKeyVal().equals(CaseManagementNoteExt.HIDECPP)){
                noteExt.setHideCpp(l.getValue());
            }else if(l.getKeyVal().equals(CaseManagementNoteExt.PROBLEMDESC)){
                noteExt.setProblemDesc(l.getValue());
            }

        }
        
        List<CaseManagementIssue> cmIssues = new ArrayList<CaseManagementIssue>(caseManagementNote.getIssues());
        
        StringBuilder summaryCodes = new StringBuilder();
        for(CaseManagementIssue issue : cmIssues) {
            if(isCppCode(issue)) {
                note.setCpp(true);
            }
            summaryCodes.append((summaryCodes.toString().isEmpty()? "" : ", ") + issue.getIssue().getCode());
        }
        
        note.setSummaryCode(summaryCodes.toString());
        note.setNoteExt(noteExt);
        note.setAssignedIssues(new CaseManagementIssueConverter().getAllAsTransferObjects(loggedInInfo, cmIssues));
        
        return note;
    }

    public boolean isCppCode(CaseManagementIssue cmeIssue) {
        return (CppCode.toStringList()).contains(cmeIssue.getIssue().getCode());
    }

    public String[] getIssueIds(String[] newCppCodes) {
        List<Issue> issues = new ArrayList<>();
        if (newCppCodes != null && newCppCodes.length > 0) { 
            issues = issueDAO.findIssueByCode(newCppCodes);
        } else {
            issues = issueDAO.findIssueByCode(CppCode.toArray());
        }

        List<String> issueIdList = new ArrayList<>();
        for (Issue issue : issues) {
            issueIdList.add(String.valueOf(issue.getId()));
        }

        return issueIdList.toArray(new String[issueIdList.size()]);
    } 
    
}
