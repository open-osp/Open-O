//CHECKSTYLE:OFF
/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.survey.web;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.xmlbeans.XmlOptions;
import org.oscarehr.common.dao.CaisiFormDao;
import org.oscarehr.common.model.Survey;
import org.oscarehr.survey.model.QuestionTypes;
import org.oscarehr.survey.service.*;
import org.oscarehr.survey.web.formbean.PageNavEntry;
import org.oscarehr.survey.web.formbean.SurveyManagerFormBean;
import org.oscarehr.surveymodel.*;
import org.oscarehr.surveymodel.DateDocument.Date.Enum;
import org.oscarehr.surveymodel.PossibleAnswersDocument.PossibleAnswers;
import org.oscarehr.surveymodel.SelectDocument.Select;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

public class SurveyManager2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();
    private static Logger log = MiscUtils.getLogger();

    private SurveyManager surveyManager = (SurveyManager) SpringUtils.getBean(SurveyManager.class);
    private SurveyTestManager surveyTestManager = (SurveyTestManager) SpringUtils.getBean(SurveyTestManager.class);
    private SurveyLaunchManager surveyLaunchManager = (SurveyLaunchManager) SpringUtils.getBean(SurveyLaunchManager.class);
    private QuestionTypes questionTypes = (QuestionTypes) SpringUtils.getBean(QuestionTypes.class);
    private CaisiFormDao caisiFormDao = SpringUtils.getBean(CaisiFormDao.class);
    private OscarFormManager oscarFormManager = (OscarFormManager) SpringUtils.getBean(OscarFormManager.class);
    private UserManager surveyUserManager = (UserManager) SpringUtils.getBean(UserManager.class);


    public String execute() {
        if ("reportForm".equals(request.getParameter("method"))) {
            return reportForm();
        }
        if ("test".equals(request.getParameter("method"))) {
            return test();
        }
        if ("new_survey".equals(request.getParameter("method"))) {
            return new_survey();
        }
        if ("create_survey".equals(request.getParameter("method"))) {
            return create_survey();
        }
        if ("edit".equals(request.getParameter("method"))) {
            return edit();
        }
        if ("form".equals(request.getParameter("method"))) {
            return form();
        }
        if ("navigate".equals(request.getParameter("method"))) {
            return navigate();
        }
        if ("add_introduction".equals(request.getParameter("method"))) {
            return add_introduction();
        }
        if ("remove_introduction".equals(request.getParameter("method"))) {
            return remove_introduction();
        }
        if ("add_page".equals(request.getParameter("method"))) {
            return add_page();
        }
        if ("remove_page".equals(request.getParameter("method"))) {
            return remove_page();
        }
        if ("add_closing".equals(request.getParameter("method"))) {
            return add_closing();
        }
        if ("remove_page".equals(request.getParameter("method"))) {
            return remove_page();
        }
        return list();
    }

    public String list() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        request.setAttribute("surveys", surveyManager.getSurveys());

        request.setAttribute("released_forms", caisiFormDao.getCaisiForms());
        return "list";
    }

    public String reportForm() {

        return "reportForm";
    }


    public String test() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        String surveyId = request.getParameter("id");
        if (surveyId != null) {
            surveyManager.updateStatus(surveyId, Survey.STATUS_TEST);
        }
        request.setAttribute("id", surveyId);
        return "execute";
    }

    public String new_survey() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        
        this.setSurvey(new Survey());
        this.setWeb(new SurveyManagerFormBean());
        this.setModel(null);
        this.setPageModel(null);

        request.setAttribute("templates", surveyManager.getSurveys());

        return "new_survey";
    }

    public String create_survey() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        SurveyManagerFormBean formBean = this.getWeb();

        //make sure there's a name
        if (survey.getDescription() != null && survey.getDescription().equals("")) {
            addActionMessage(getText("survey.noname"));
            request.setAttribute("templates", surveyManager.getSurveys());
            return "new_survey";
        }

        //check to see if name exists
        if (surveyManager.getSurveyByName(survey.getDescription()) != null) {
            addActionMessage(getText("name.exists"));
            request.setAttribute("templates", surveyManager.getSurveys());
            return "new_survey";
        }

        SurveyDocument model = null;

        //if using a template, load model into memory
        if (formBean.getTemplateId() > 0) {
            Survey template = surveyManager.getSurvey(String.valueOf(formBean.getTemplateId()));
            String xml = template.getSurveyData();
            try {
                model = SurveyDocument.Factory.parse(new StringReader(xml));
                model.getSurvey().setName(survey.getDescription());
                model.getSurvey().setVersion(0);
            } catch (Exception e) {
                addActionMessage(getText("survey.create_error", new String[]{"Error loading template model"}));
                request.setAttribute("templates", surveyManager.getSurveys());
                return "new_survey";
            }
        } else {
            //build a new model
            model = SurveyDocument.Factory.newInstance();
            SurveyDocument.Survey surveyData = model.addNewSurvey();
            surveyData.setName(survey.getDescription());
            surveyData.setBody(surveyData.addNewBody());
            Page page = surveyData.getBody().addNewPage();
            page.setDescription("Page 1");
        }

        formBean.setPage("1");
        this.setModel(model);

        return form();
    }

    public String edit() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        SurveyManagerFormBean formBean = this.getWeb();

        String id = request.getParameter("id");
        if (id == null)
            id = (String) request.getAttribute("id");

        if (id == null) {
            addActionMessage(getText("survey.noid"));
            return list();
        }


        Survey survey = surveyManager.getSurvey(id);

        if (survey == null) {
            addActionMessage(getText("survey.edit_error", new String[]{"Survey not found"}));
            return list();
        }

        if (survey.getStatus() == Survey.STATUS_TEST) {
            surveyTestManager.clearTestData(id);
        }

        this.setSurvey(survey);

        try {
            String xml = survey.getSurveyData();
            SurveyDocument model = SurveyDocument.Factory.parse(new StringReader(xml));
            this.setModel(model);
        } catch (Exception e) {
            log.error("Error", e);
            addActionMessage(getText("survey.edit_error", new String[]{e.getMessage()}));
            return list();
        }

        formBean.setPage("1");

        return form();
    }

    public String form() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        SurveyManagerFormBean formBean = this.getWeb();

        SurveyDocument.Survey survey = model.getSurvey();

        List pages = new ArrayList();
        if (survey.getIntroduction() != null) {
            pages.add(new PageNavEntry("Introduction", "Introduction"));
        }

        for (int x = 0; x < survey.getBody().getPageArray().length; x++) {
            pages.add(new PageNavEntry(String.valueOf(x + 1), survey.getBody().getPageArray(x).getDescription()));
        }

        if (survey.getClosing() != null) {
            pages.add(new PageNavEntry("Closing", "Closing"));
        }

        request.setAttribute("pages", pages);

        String pageName = formBean.getPage();

        if (pageName.equalsIgnoreCase("Introduction")) {
            // do nothing
        } else if (pageName.equals("Closing")) {
            // do nothing
        } else {

            if (pageName == null || pageName.length() == 0) {
                pageName = "1";
            }

            if (pageName.startsWith("Page")) {
                pageName = pageName.substring(5);
            }

            int pn = Integer.parseInt(pageName);

            Page p = survey.getBody().getPageArray(pn - 1);

            this.setPageModel(p);
            request.setAttribute("page_number", pageName);
        }

        request.setAttribute("QuestionTypes", questionTypes);

        setSectionProperties(request, survey, formBean);

        List<String> colorList = new ArrayList<String>();
        colorList.add("red");
        colorList.add("green");
        colorList.add("blue");
        colorList.add("yellow");
        colorList.add("beige");
        colorList.add("brown");
        colorList.add("cyan");
        colorList.add("grey");
        colorList.add("magenta");
        colorList.add("orange");
        colorList.add("pink");
        colorList.add("purple");
        request.setAttribute("colors", colorList);

        return "edit";
    }

    public String navigate() {
        //DynaActionForm surveyForm = (DynaActionForm)form;
        return form();

    }

    public String add_introduction() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }
        SurveyManagerFormBean formBean = this.getWeb();
        SurveyDocument.Survey surveyModel = model.getSurvey();

        if (surveyModel.getIntroduction() == null) {
            surveyModel.setIntroduction(surveyModel.addNewIntroduction());
            surveyModel.getIntroduction().setIncludeOnFirstPage(true);
        }
        formBean.setPage("Introduction");
        return form();
    }

    public String remove_introduction() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }


        SurveyManagerFormBean formBean = this.getWeb();
        SurveyDocument.Survey surveyModel = model.getSurvey();

        if (surveyModel.getIntroduction() != null) {
            surveyModel.unsetIntroduction();
        }

        formBean.setPage("1");
        return form();
    }

    public String add_page() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        SurveyManagerFormBean formBean = this.getWeb();

        SurveyDocument.Survey survey = model.getSurvey();
        Page p = survey.getBody().addNewPage();
        p.setDescription(String.valueOf("Page " + survey.getBody().getPageArray().length));
        formBean.setPage(String.valueOf(survey.getBody().getPageArray().length));
        return form();
    }

    public String remove_page() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        SurveyManagerFormBean formBean = this.getWeb();


        SurveyDocument.Survey survey = model.getSurvey();
        int pageNumber = Integer.parseInt(request.getParameter("id"));

        log.debug("removing page number " + pageNumber);
        //remove the page, only if it's not the only page
        if (survey.getBody().getPageArray().length == 1) {
            addActionMessage(getText("survey_1page"));
            return form();
        }

        Page p = survey.getBody().getPageArray(pageNumber - 1);
        if (p != null) {
            survey.getBody().removePage(pageNumber - 1);
        }

        formBean.setPage("1");

        return form();
    }

    public String add_closing() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        SurveyManagerFormBean formBean = this.getWeb();
        SurveyDocument.Survey surveyModel = model.getSurvey();

        if (surveyModel.getClosing() == null) {
            surveyModel.setClosing(surveyModel.addNewClosing());
            surveyModel.getClosing().setIncludeOnLastPage(true);
        }

        formBean.setPage("Closing");
        return form();
    }

    public String remove_closing() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        SurveyManagerFormBean formBean = this.getWeb();
        SurveyDocument.Survey surveyModel = model.getSurvey();

        if (surveyModel.getClosing() != null) {
            surveyModel.unsetClosing();
        }

        formBean.setPage("1");
        return form();
    }

    public String update_section() {
        request.setAttribute("updateSection", "true");
        return form();
    }

    public String add_section() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        SurveyManagerFormBean formBean = this.getWeb();
        SurveyDocument.Survey surveyModel = model.getSurvey();

        String pageNumber = formBean.getPage();
        Page page = surveyModel.getBody().getPageArray(Integer.parseInt(pageNumber) - 1);

        Section section = page.addNewQContainer().addNewSection();
        section.setDescription("");
        section.setId(getUnusedSectionId(page));

        return form();
    }

    public String remove_section() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        SurveyManagerFormBean formBean = this.getWeb();
        SurveyDocument.Survey surveyModel = model.getSurvey();

        int id = Integer.parseInt(request.getParameter("id"));
        String pageNumber = formBean.getPage();
        Page page = surveyModel.getBody().getPageArray(Integer.parseInt(pageNumber) - 1);


        for (int x = 0; x < page.getQContainerArray().length; x++) {
            if (page.getQContainerArray(x).isSetSection()) {
                Section section = page.getQContainerArray(x).getSection();
                if (section.getId() == id) {
                    //found it
                    page.removeQContainer(x);
                }
            }
        }

        return form();
    }

    public String add_question() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        SurveyManagerFormBean formBean = this.getWeb();
        SurveyDocument.Survey surveyModel = model.getSurvey();

        String pageNumber = formBean.getPage();
        int sectionId = formBean.getSection();
        String questionType = formBean.getQuestionTypeData();

        //log.debug("add_question: page=" + pageNumber);
        //log.debug("add_question: section=" + sectionId);
        //log.debug("add_question: questionType=" + questionType);

        /* find right place */
        Page page = surveyModel.getBody().getPageArray(Integer.parseInt(pageNumber) - 1);
        if (sectionId == 0) {
            //no section
            SurveyModelManager.createQuestion(surveyModel, pageNumber, questionType);
        } else {
            //find section
            Section section = SurveyModelManager.findSection(surveyModel, pageNumber, sectionId);
            if (section != null) {
                SurveyModelManager.createQuestion(surveyModel, page, section, questionType);
            }
        }

        formBean.setSection(0);
        formBean.setQuestionType("");
        formBean.setQuestionTypeData("");

        return form();
    }

    public String edit_question() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }


        SurveyManagerFormBean formBean = this.getWeb();
        SurveyDocument.Survey surveyModel = model.getSurvey();

        String questionId = request.getParameter("id");
        int qid = Integer.parseInt(questionId);
        String sectionId = request.getParameter("section");
        String pageNumber = request.getParameter("page");

        Question question = SurveyModelManager.findQuestion(surveyModel, pageNumber, Integer.parseInt(sectionId), qid);

        List<LabelValueBean> oscarVars = new ArrayList<LabelValueBean>();
        List<String> objects = new ArrayList<String>();

        if (question != null && question.getType().isSetSelect()) {
            Select select = question.getType().getSelect();
            PossibleAnswers pa = select.getPossibleAnswers();
            formBean.setNumAnswers(pa.getAnswerArray().length);
        }
        if (question != null && question.getType().isSetDate()) {
            DateDocument.Date.Enum enum1 = question.getType().getDate();
            List dateFormats = new ArrayList();
            for (int x = 1; x <= Enum.table.lastInt(); x++) {
                LabelValueBean bean = new LabelValueBean();
                bean.setValue(String.valueOf(Enum.table.forInt(x).intValue()));
                bean.setLabel(Enum.table.forInt(x).toString());
                dateFormats.add(bean);
            }
            formBean.setDateFormat(String.valueOf(enum1.intValue()));
            request.setAttribute("dateFormats", dateFormats);

            oscarVars.add(new LabelValueBean("Date of Birth", "Demographic/birthDate"));
            oscarVars.add(new LabelValueBean("Program Admission", "Program/admissionDate"));
        }
        if (question != null && question.getType().isSetOpenEnded()) {
            //objects
            objects.add("Current Issues");
            objects.add("Current Medications");
            objects.add("Allergies");

            oscarVars.add(new LabelValueBean("First Name", "Demographic/FirstName"));
            oscarVars.add(new LabelValueBean("Last Name", "Demographic/LastName"));
            oscarVars.add(new LabelValueBean("Program Admission Notes", "Program/admissionNotes"));
        }

        if (question != null && question.getType().isSetSelect()) {
            //oscarVars.add(new LabelValueBean("Program Selection","Program"));
            objects.add("Program Selector");
        }


        //request.setAttribute("caisiobjects", objects);
        //request.setAttribute("oscarVars", oscarVars);
        request.getSession().setAttribute("caisiobjects", objects);
        request.getSession().setAttribute("oscarVars", oscarVars);
        this.setQuestionModel(question);

        List<String> colorList = new ArrayList<String>();
        colorList.add("red");
        colorList.add("green");
        colorList.add("blue");
        colorList.add("yellow");
        colorList.add("beige");
        colorList.add("brown");
        colorList.add("cyan");
        colorList.add("grey");
        colorList.add("magenta");
        colorList.add("orange");
        colorList.add("pink");
        colorList.add("purple");
        request.getSession().setAttribute("colors", colorList);

        return "question_editor";
    }

    public String question_adjust_possible_answers() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }


        Question question = this.getQuestionModel();
        SurveyManagerFormBean formBean = this.getWeb();


        if (question.getType().isSetSelect()) {
            Select select = question.getType().getSelect();
            PossibleAnswers pa = select.getPossibleAnswers();
            if (formBean.getNumAnswers() > pa.getAnswerArray().length) {
                //adding more
                for (int x = 0; x < (formBean.getNumAnswers() - pa.getAnswerArray().length + 1); x++) {
                    pa.addNewAnswer();
                }
            } else if (formBean.getNumAnswers() < pa.getAnswerArray().length) {
                //remove some
                int index = pa.getAnswerArray().length - 1;
                int numToRemove = pa.getAnswerArray().length - formBean.getNumAnswers();
                log.debug("index=" + index);
                log.debug("numtoRemove=" + numToRemove);
                while (numToRemove-- > 0) {
                    pa.removeAnswer(index--);
                }
            }
            formBean.setNumAnswers(pa.getAnswerArray().length);
        }

        return "question_editor";
    }

    public String save_question() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        Question question = this.getQuestionModel();
        SurveyManagerFormBean formBean = this.getWeb();

        log.info("saving question: " + question.getDescription());

        if (question.getType().isSetSelect()) {
            Select select = question.getType().getSelect();
            PossibleAnswers pa = select.getPossibleAnswers();
            int numAnswers = formBean.getNumAnswers();
            for (int x = 0; x < numAnswers; x++) {
                String answer = request.getParameter("answer_" + (x + 1));
                pa.setAnswerArray(x, answer);
            }
        }
        if (question.getType().isSetDate()) {
            int dateFormat = Integer.parseInt(formBean.getDateFormat());
            question.getType().setDate(DateDocument.Date.Enum.forInt(dateFormat));
        }

        if (request.getParameter("questionModel.bold") == null) {
            question.setBold("");
        }
        if (request.getParameter("questionModel.underline") == null) {
            question.setUnderline("");
        }
        if (request.getParameter("questionModel.italics") == null) {
            question.setItalics("");
        }
        //return null;
        return form();


    }

    public String remove_question() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }


        SurveyManagerFormBean formBean = this.getWeb();
        SurveyDocument.Survey surveyModel = model.getSurvey();

        String id = request.getParameter("id");
        int sectionId = formBean.getSection();
        String pageNumber = formBean.getPage();

        SurveyModelManager.removeQuestion(surveyModel, pageNumber, sectionId, Integer.parseInt(id));

        return form();
    }


    public String clear_test_data() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        String surveyId = request.getParameter("id");
        if (surveyId != null) {
            surveyTestManager.clearTestData(surveyId);
        }
        return list();
    }

    public String launch() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        String surveyId = request.getParameter("id");
        if (surveyId != null) {
            Survey survey = surveyManager.getSurvey(surveyId);
            if (survey.getStatus() == 1) {
                long instanceId = surveyLaunchManager.launch(survey);
                survey.setLaunchedInstanceId((int) instanceId);
                surveyManager.saveSurvey(survey);
                surveyManager.updateStatus(surveyId, Survey.STATUS_LAUNCHED);
            }
        }
        return list();
    }

    public String close() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        String surveyId = request.getParameter("id");
        if (surveyId != null) {
            surveyLaunchManager.close(surveyManager.getSurvey(surveyId).getLaunchedInstanceId());
            surveyManager.updateStatus(surveyId, Survey.STATUS_CLOSED);
        }
        return list();
    }

    public String reopen() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        String surveyId = request.getParameter("id");
        if (surveyId != null) {
            surveyLaunchManager.reopen(surveyManager.getSurvey(surveyId).getLaunchedInstanceId());
            surveyManager.updateStatus(surveyId, Survey.STATUS_LAUNCHED);
        }
        return list();
    }


    public String save() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String providerNo = loggedInInfo.getLoggedInProviderNo();

        SurveyManagerFormBean formBean = this.getWeb();
        SurveyDocument.Survey surveyModel = model.getSurvey();

        if (this.isCancel()) {
            return list();
        }

        setSectionProperties(request, surveyModel, formBean);

        surveyModel.setVersion(surveyModel.getVersion() + 1);
        survey.setVersion(surveyModel.getVersion());

        //Add creation date
        if (survey.getDateCreated() == null) {
            survey.setDateCreated(new Date());
        }

        survey.setFacilityId(loggedInInfo.getCurrentFacility().getId());

        survey.setUserId(Integer.valueOf((String) request.getSession().getAttribute("user")));

        try {
            StringWriter sw = new StringWriter();
            model.save(sw);
            String xml = sw.toString();
            survey.setSurveyData(xml);
        } catch (Exception e) {
            addActionMessage(getText("survey.saved.error", new String[]{"Unabled to create XML :" + e.getMessage()}));
            return form();
        }

        survey.setStatus(new Short(Survey.STATUS_IN_REVIEW));

        surveyManager.saveSurvey(survey);

        addActionMessage(getText("survey.saved"));

        return "redirect";
    }

    public String delete() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        String id = request.getParameter("id");

        surveyManager.deleteSurvey(id);

        addActionMessage(getText("survey.deleted"));

        //return list(mapping,form,request,response);
        return "redirect";
    }

    public String show_import_form() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        return "import";
    }


    public String export() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        String id = request.getParameter("id");

        SurveyDocument survey = surveyManager.getSurveyDocument(id);
        if (survey == null) {
            addActionMessage("");
            return list();
        }

        response.setContentType("text/xml");
        response.setHeader("Content-disposition", "attachement;filename=" + survey.getSurvey().getName() + ".xml");
        try {
            XmlOptions options = new XmlOptions();
            options.setSavePrettyPrint();
            options.setSavePrettyPrintIndent(4);
            survey.save(response.getWriter(), options);
        } catch (Exception e) {
            log.error("Error", e);
        }

        return null;
    }

    public String copy() {
        if (!surveyUserManager.isAdmin(request)) {
            addActionMessage(getText("survey.auth"));
            return "auth";
        }

        String id = request.getParameter("id");

        log.debug("copying a survey");

        Survey survey = surveyManager.getSurvey(id);
        if (survey == null) {
            addActionMessage(getText("survey.missing"));
            return list();
        }

        Survey newSurvey = new Survey();
        newSurvey.setDateCreated(new Date());
        newSurvey.setDescription(survey.getDescription());
        newSurvey.setSurveyData(survey.getSurveyData());
        newSurvey.setUserId(new Long(surveyUserManager.getUserId(request)).intValue());
        newSurvey.setVersion(survey.getVersion());
        newSurvey.setStatus(new Short(Survey.STATUS_IN_REVIEW));

        surveyManager.saveSurvey(newSurvey);

        return list();
    }

    public String export_csv() {

        String id = request.getParameter("id");
        try {
            response.setContentType("APPLICATION/OCTET-STREAM");
            String strProjectInfoPageHeader = "Attachment;Filename=" + id + ".csv";
            response.setHeader("Content-Disposition", strProjectInfoPageHeader);
            this.oscarFormManager.generateCSV(Integer.valueOf(id), response.getOutputStream());
        } catch (IOException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return null;
    }

    public String export_inverse_csv() {

        String id = request.getParameter("id");
        try {
            response.setContentType("APPLICATION/OCTET-STREAM");
            String strProjectInfoPageHeader = "Attachment;Filename=" + id + ".csv";
            response.setHeader("Content-Disposition", strProjectInfoPageHeader);
            this.oscarFormManager.generateInverseCSV(Integer.valueOf(id), response.getOutputStream());
        } catch (IOException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return null;
    }

    public String export_to_db() {

        String id = request.getParameter("id");
        this.oscarFormManager.convertFormXMLToDb(Integer.valueOf(id));

        return list();
    }

    protected static int getUnusedSectionId(Page page) {
        int id = 1;
        for (int x = 0; x < page.getQContainerArray().length; x++) {
            if (page.getQContainerArray(x).isSetSection()) {
                if (page.getQContainerArray(x).getSection().getId() == id) {
                    id++;
                } else if (page.getQContainerArray(x).getSection().getId() > id) {
                    id = page.getQContainerArray(x).getSection().getId() + 1;
                }
            }
        }
        return id;
    }

    protected void setSectionProperties(HttpServletRequest request, SurveyDocument.Survey survey, SurveyManagerFormBean formBean) {
        Enumeration e = request.getParameterNames();
        //create a uniqeu list of sectionids;
        Map<String, Boolean> ids = new HashMap<String, Boolean>();

        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String pageName = formBean.getPage();
            if (name.startsWith("section_description_")) {
                String sectionId = name.substring(name.lastIndexOf("_") + 1);
                ids.put(sectionId, true);
                String description = request.getParameter(name);

                if (!(pageName.equalsIgnoreCase("Introduction")) && !(pageName.equalsIgnoreCase("Closing"))) {
                    Section section = SurveyModelManager.findSection(survey, formBean.getPage(), Integer.valueOf(sectionId).intValue());
                    if (section != null && request.getAttribute("updateSection") == "true") {
                        section.setDescription(description);
                        //log.debug("setting description for section " + sectionId);
                    }

                }
            }

            if (name.startsWith("section_bold_")) {
                String sectionId = name.substring(name.lastIndexOf("_") + 1);
                String description = request.getParameter(name);

                if (!(pageName.equalsIgnoreCase("Introduction")) && !(pageName.equalsIgnoreCase("Closing"))) {
                    Section section = SurveyModelManager.findSection(survey, formBean.getPage(), Integer.valueOf(sectionId).intValue());
                    if (section != null && request.getAttribute("updateSection") == "true") {
                        section.setBold(description);
                        //log.debug("setting description for section " + sectionId);
                    }

                }
            }

            if (name.startsWith("section_underline_")) {
                String sectionId = name.substring(name.lastIndexOf("_") + 1);
                String description = request.getParameter(name);

                if (!(pageName.equalsIgnoreCase("Introduction")) && !(pageName.equalsIgnoreCase("Closing"))) {
                    Section section = SurveyModelManager.findSection(survey, formBean.getPage(), Integer.valueOf(sectionId).intValue());
                    if (section != null && request.getAttribute("updateSection") == "true") {
                        section.setUnderline(description);
                        //log.debug("setting description for section " + sectionId);
                    }

                }
            }

            if (name.startsWith("section_italics_")) {
                String sectionId = name.substring(name.lastIndexOf("_") + 1);
                String description = request.getParameter(name);

                if (!(pageName.equalsIgnoreCase("Introduction")) && !(pageName.equalsIgnoreCase("Closing"))) {
                    Section section = SurveyModelManager.findSection(survey, formBean.getPage(), Integer.valueOf(sectionId).intValue());
                    if (section != null && request.getAttribute("updateSection") == "true") {
                        section.setItalics(description);
                        //log.debug("setting description for section " + sectionId);
                    }

                }
            }

            if (name.startsWith("section_color_")) {
                String sectionId = name.substring(name.lastIndexOf("_") + 1);
                String description = request.getParameter(name);

                if (!(pageName.equalsIgnoreCase("Introduction")) && !(pageName.equalsIgnoreCase("Closing"))) {
                    Section section = SurveyModelManager.findSection(survey, formBean.getPage(), Integer.valueOf(sectionId).intValue());
                    if (section != null && request.getAttribute("updateSection") == "true") {
                        section.setColor(description);
                        //log.debug("setting description for section " + sectionId);
                    }

                }
            }
        }

        for (Iterator<String> iter = ids.keySet().iterator(); iter.hasNext(); ) {
            String key = iter.next();
            //unset the checkboxes if necessary
            if (request.getParameter("section_bold_" + key) == null) {
                Section section = SurveyModelManager.findSection(survey, formBean.getPage(), Integer.valueOf(key).intValue());
                if (section != null && request.getAttribute("updateSection") == "true") {
                    section.setBold("");
                }
            }
            if (request.getParameter("section_underline_" + key) == null) {
                Section section = SurveyModelManager.findSection(survey, formBean.getPage(), Integer.valueOf(key).intValue());
                if (section != null && request.getAttribute("updateSection") == "true") {
                    section.setUnderline("");
                }
            }
            if (request.getParameter("section_italics_" + key) == null) {
                Section section = SurveyModelManager.findSection(survey, formBean.getPage(), Integer.valueOf(key).intValue());
                if (section != null && request.getAttribute("updateSection") == "true") {
                    section.setItalics("");
                }
            }
        }
    }

    @Deprecated
    public String getUcfReport() {

        Long id = Long.valueOf(request.getParameter("formId"));

        this.oscarFormManager.convertFormXMLToDb(Long.valueOf(id).intValue());
        //request.setAttribute("ucfReports", oscarFormManager.getFormReport(id, startDate, endDate));

        return "ucfReport";

    }
    private Survey survey;
    private SurveyManagerFormBean web;
    private SurveyDocument model;
    private Object pageModel;
    private List<Survey> templates;
    private Question questionModel;
    private boolean cancel;

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public SurveyManagerFormBean getWeb() {
        return web;
    }

    public void setWeb(SurveyManagerFormBean web) {
        this.web = web;
    }

    public SurveyDocument getModel() {
        return model;
    }

    public void setModel(SurveyDocument model) {
        this.model = model;
    }

    public Object getPageModel() {
        return pageModel;
    }

    public void setPageModel(Object pageModel) {
        this.pageModel = pageModel;
    }

    public List<Survey> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Survey> templates) {
        this.templates = templates;
    }

    public Question getQuestionModel() {
        return questionModel;
    }

    public void setQuestionModel(Question questionModel) {
        this.questionModel = questionModel;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}
