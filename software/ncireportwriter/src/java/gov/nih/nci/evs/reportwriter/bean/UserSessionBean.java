package gov.nih.nci.evs.reportwriter.bean;

import java.io.*;

import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.service.*;
import gov.nih.nci.evs.reportwriter.properties.*;

import java.util.*;

import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

import org.LexGrid.concepts.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction 
 * with the National Cancer Institute, and so to the extent government 
 * employees are co-authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *   1. Redistributions of source code must retain the above copyright 
 *      notice, this list of conditions and the disclaimer of Article 3, 
 *      below. Redistributions in binary form must reproduce the above 
 *      copyright notice, this list of conditions and the following 
 *      disclaimer in the documentation and/or other materials provided 
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution, 
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National 
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must 
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not 
 *      authorize the recipient to use any trademarks owned by either NCI 
 *      or NGIT 
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED 
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE 
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class UserSessionBean extends Object {
    private static Logger _logger = Logger.getLogger(UserSessionBean.class);
    // private final int COL_NUM = 0;
    private final int FIELD_NUM = 1;
    
    private Boolean _isAdmin = null;
    private String _selectedTask = null;

    // for templates with reports already been generated
    private List<SelectItem> _standardReportTemplateList_draft =
        new ArrayList<SelectItem>();
    // for templates with reports already been generated
    private List<SelectItem> _standardReportTemplateList_approved =
        new ArrayList<SelectItem>();

    private String _selectedStandardReportTemplate = null;
    private String _selectedStandardReportTemplate_draft = null;
    private String _selectedStandardReportTemplate_approved = null;

    private String _selectedPropertyType = null;
    private String _rootConceptCode = null;
    private String _selectedOntology = null;

    private String _selectedReportStatus = null;
    private List<SelectItem> _reportStatusList = null;
    private Vector<String> _reportStatusListData = null;

    private String _selectedReportFormat = null;
    private List<SelectItem> _reportFormatList = null;
    private Vector<String> _reportFormatListData = null;

    public void setIsAdmin(Boolean isAdmin) {
        _isAdmin = isAdmin;
    }

    public Boolean getIsAdmin() {
        return _isAdmin;
    }

    public String getSelectedTask() {
        return _selectedTask;
    }

    public void setSelectedTask(String selectedTask) {
        _selectedTask = selectedTask;
    }

    public String getSelectedPropertyType() {
        return _selectedPropertyType;
    }

    public void setSelectedPropertyType(String selectedPropertyType) {
        _selectedPropertyType = selectedPropertyType;
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedPropertyType",
            selectedPropertyType);
    }

    public void propertyTypeSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedPropertyType(newValue);
    }

    public String getSelectedOntology() {
        return _selectedOntology;
    }

    public void setSelectedOntology(String selectedOntology) {
        _selectedOntology = selectedOntology;
    }

    public List<SelectItem> getTaskList() {
        HttpServletRequest request = SessionUtil.getRequest();
        HttpSession session = request.getSession(false);

        Boolean isAdmin = null;
        if (session != null) {
            isAdmin =
                (Boolean) request.getSession(true).getAttribute("isAdmin");
        }

        List<SelectItem> list = DataUtils.getTaskList(isAdmin);
        if (list != null) {
            SelectItem item = (SelectItem) list.get(0);
            _selectedTask = item.getLabel();
        }
        // return DataUtils.getTaskList(isAdmin);
        return list;
    }

    public List<SelectItem> getPropertyTypeList() {
        List<SelectItem> list = DataUtils.getPropertyTypeList();
        return list;
    }

    public void changeTaskSelection(ValueChangeEvent vce) {
        String newValue = (String) vce.getNewValue();
        // logger.debug("========== changeTaskSelection " + newValue);
        _selectedTask = newValue;
    }

    public void reportSelectionChanged(ValueChangeEvent vce) {
        String newValue = (String) vce.getNewValue();
        setSelectedStandardReportTemplate(newValue);
    }

    public void reportSelectionChanged_draft(ValueChangeEvent vce) {
        String newValue = (String) vce.getNewValue();
        setSelectedStandardReportTemplate_draft(newValue);
    }

    public void reportSelectionChanged_approved(ValueChangeEvent vce) {
        String newValue = (String) vce.getNewValue();
        setSelectedStandardReportTemplate_approved(newValue);
    }

    public List<SelectItem> getStandardReportTemplateList() {

        List<SelectItem> list = DataUtils.getStandardReportTemplateList();
        if (_selectedStandardReportTemplate == null) {
            if (list != null && list.size() > 0) {
                if (getSelectedStandardReportTemplate() == null) {
                    SelectItem item = (SelectItem) list.get(0);
                    setSelectedStandardReportTemplate(item.getLabel());
                }
            }
        }
        return list;
    }

    public String getSelectedStandardReportTemplate() {
        return _selectedStandardReportTemplate;
    }

    public void setSelectedStandardReportTemplate(
        String selectedStandardReportTemplate) {
        _selectedStandardReportTemplate = selectedStandardReportTemplate;
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedStandardReportTemplate",
            selectedStandardReportTemplate);
    }

    public List<SelectItem> getStandardReportTemplateList_draft() {
        // Find all templates with reports already been generated
        List<SelectItem> list = new ArrayList<SelectItem>();
        HashSet<String> hset = new HashSet<String>();
        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            StandardReportTemplate standardReportTemplate = null;
            String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
            Object[] objs = sdkclientutil.search(FQName);

            if (objs != null && objs.length > 0) {
                for (int i = 0; i < objs.length; i++) {
                    StandardReport standardReport = (StandardReport) objs[i];
                    ReportStatus rs = standardReport.getStatus();
                    String status = rs.getLabel();
                    standardReportTemplate = standardReport.getTemplate();
                    if (standardReportTemplate != null) {
                        if (status.compareToIgnoreCase("DRAFT") == 0) {
                            if (!hset.contains(standardReportTemplate
                                .getLabel())) {
                                hset.add(standardReportTemplate.getLabel());
                                list.add(new SelectItem(standardReportTemplate
                                    .getLabel()));
                            }
                        }
                    }
                }

                if (list != null && list.size() > 0) {
                    SelectItem item = list.get(0);
                    setSelectedStandardReportTemplate_draft(item.getLabel());
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public String getSelectedStandardReportTemplate_draft() {
        return _selectedStandardReportTemplate_draft;
    }

    public void setSelectedStandardReportTemplate_draft(
        String selectedStandardReportTemplate_draft) {
        _selectedStandardReportTemplate_draft =
            selectedStandardReportTemplate_draft;
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute(
            "selectedStandardReportTemplate_draft",
            selectedStandardReportTemplate_draft);
    }

    public List<SelectItem> getStandardReportTemplateList_approved() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        HashSet<String> hset = new HashSet<String>();
        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            StandardReportTemplate standardReportTemplate = null;
            String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
            Object[] objs = sdkclientutil.search(FQName);
            if (objs != null && objs.length > 0) {
                for (int i = 0; i < objs.length; i++) {
                    StandardReport standardReport = (StandardReport) objs[i];
                    ReportStatus rs = standardReport.getStatus();
                    String status = rs.getLabel();
                    if (status.compareTo("APPROVED") == 0) {
                        standardReportTemplate = standardReport.getTemplate();
                        if (!hset.contains(standardReportTemplate.getLabel())) {
                            hset.add(standardReportTemplate.getLabel());
                            list.add(new SelectItem(standardReportTemplate
                                .getLabel()));
                        }
                    }
                }

                if (list == null) {
                    if (list != null && list.size() > 0) {
                        SelectItem item = list.get(0);
                        setSelectedStandardReportTemplate_approved(item
                            .getLabel());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public String getSelectedStandardReportTemplate_approved() {
        return _selectedStandardReportTemplate_approved;
    }

    public void setSelectedStandardReportTemplate_approved(
        String selectedStandardReportTemplate_draft) {
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute(
            "selectedStandardReportTemplate_approved",
            _selectedStandardReportTemplate_approved);
    }

    // taskSelectionChanged
    public void taskSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String task = (String) event.getNewValue();
        setSelectedTask(task);
    }

    public String performTask() {
        if (_selectedTask.compareTo("Administer Standard Reports") == 0) {
            List<SelectItem> list = getStandardReportTemplateList();
            if (list == null || list.size() == 0) {
                return "add_standard_report_template";
            }
            return "administer_standard_reports";
        } else if (_selectedTask.compareTo("Maintain Report Status") == 0)
            return "report_status";

        else if (_selectedTask.compareTo("Assign Report Status") == 0) {
            // Check if there is any DRAFT report waiting for approval:
            _standardReportTemplateList_draft =
                getStandardReportTemplateList_draft();
            if (_standardReportTemplateList_draft != null
                && _standardReportTemplateList_draft.size() > 0) {
                return "assign_report_status";
            } else {
                String message = "No draft report is found. ";
                HttpServletRequest request = SessionUtil.getRequest();
                request.getSession().setAttribute("message", message);
                return "message";
            }

        } else if (_selectedTask.compareTo("Retrieve Standard Reports") == 0) {
            HttpServletRequest request = SessionUtil.getRequest();
            Boolean isAdmin =
                (Boolean) request.getSession().getAttribute("isAdmin");
            if (isAdmin != null && isAdmin.equals(Boolean.TRUE)) {
                return "retrieve_standard_reports";
            } else {
                // Check if there is any APPROVED report waiting for approval:
                _standardReportTemplateList_approved =
                    getStandardReportTemplateList_approved();
                if (_standardReportTemplateList_approved != null
                    && _standardReportTemplateList_approved.size() > 0) {
                    return "retrieve_standard_reports";
                } else {
                    String message = "No approved report is found. ";
                    request.getSession().setAttribute("message", message);
                    return "message";
                }
            }
        }
        return null;
    }
    
    private int[] getColumnInfo() throws Exception {
        HttpServletRequest request = SessionUtil.getRequest();
        String selectedColumnInfo = request.getParameter("selectedColumnInfo");
        _logger.debug("Selected Column Info: " + selectedColumnInfo);
        if (selectedColumnInfo == null)
            throw new Exception("Please select a column.");
        
        StringTokenizer tokenizer = new StringTokenizer(selectedColumnInfo, ":");
        int[] info = new int[tokenizer.countTokens()];
        int i=0;
        while (tokenizer.hasMoreTokens())
            info[i++] = Integer.parseInt(tokenizer.nextToken());
        return info;
    }
    
    private void initColumnAction() {
        HttpServletRequest request = SessionUtil.getRequest();
        request.removeAttribute("warningMsg");
    }

    public String addColumnAction() {
        return "add_standard_report_column";
    }

    public String modifyColumnAction() {
        try {
            initColumnAction();
            int[] info = getColumnInfo();
            int fieldNum = info[FIELD_NUM];
            _logger.debug("Modify column with field number = " + fieldNum);
    
            ReportColumn reportColumn = getReportColumn(fieldNum);
            debug(reportColumn);
            SessionUtil.getRequest().setAttribute("reportColumn", reportColumn);
            return "add_standard_report_column";
        } catch (Exception e) {
            MiscUtils.printException(_logger, e);
            SessionUtil.getRequest().setAttribute("warningMsg",
                e.getMessage());
            return "standard_report_column";
        }
    }

    public String insertBeforeColumnAction() {
        return "add_standard_report_column";
    }

    public String insertAfterColumnAction() {
        return "add_standard_report_column";
    }

    public String deleteColumnAction() {
        try {
            initColumnAction();
            int info[] = getColumnInfo();
            int fieldNum = info[FIELD_NUM];
            _logger.debug("Deleting column with field number = " + fieldNum);
    
            ReportColumn reportColumn = getReportColumn(fieldNum);
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            sdkclientutil.deleteReportColumn(reportColumn);
            // setSelectedStandardReportTemplate(label);
            return "standard_report_column";
        } catch (Exception e) {
            SessionUtil.getRequest().setAttribute("warningMsg",
                e.getMessage());
            return "standard_report_column";
        }
    }

    private void debug(ReportColumn reportColumn) {
        if (! _logger.isDebugEnabled())
            return;
        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("ReportColumn: ");
        _logger.debug("* Column Number: " + reportColumn.getColumnNumber());
        _logger.debug("* Field Number (getId): " + reportColumn.getId());
        _logger.debug("* Field Label (getLabel): " + reportColumn.getLabel());
        _logger.debug("* Field Type (getFieldId): " + reportColumn.getFieldId());
        _logger.debug("* Property Type: " + reportColumn.getPropertyType());
        _logger.debug("* Property Name: " + reportColumn.getPropertyName());
        _logger.debug("* Is Preferred: " + reportColumn.getIsPreferred());
        _logger.debug("* Representational Form: " 
            + reportColumn.getRepresentationalForm());
        _logger.debug("* Source: " + reportColumn.getSource());
        _logger.debug("* Qualifier Name: " + reportColumn.getQualifierName());
        _logger.debug("* Qualifier Value: " + reportColumn.getQualifierValue());
        _logger.debug("* Delimiter: " + reportColumn.getDelimiter());
        _logger.debug("* Dependency (getConditionalColumnId): " 
            + reportColumn.getConditionalColumnId());
    }
    
    public String getRootConceptCode() {
        return _rootConceptCode;
    }

    public void setRootConceptCode(String rootConceptCode) {
        if (rootConceptCode == null)
            return;
        _rootConceptCode = rootConceptCode;
    }

    // public String addReportAction() {
    public String saveTemplateAction() {
        HttpServletRequest request = SessionUtil.getRequest();
        String codingSchemeNameAndVersion =
            (String) request.getSession().getAttribute("selectedOntology");
        String codingSchemeName =
            DataUtils.getCodingSchemeName(codingSchemeNameAndVersion);
        String codingSchemeVersion =
            DataUtils.getCodingSchemeVersion(codingSchemeNameAndVersion);
        String label = (String) request.getParameter("label");

        String rootConceptCode =
            (String) request.getParameter("rootConceptCode");
        String selectedAssociation =
            (String) request.getSession().getAttribute("selectedAssociation");
        String selectedLevel =
            (String) request.getSession().getAttribute("selectedLevel");
        // String selectedDirection = (String)
        // request.getSession().getAttribute("selectedDirection");

        String direction_str = (String) request.getParameter("direction");

        Boolean direction = null;

        // return to error page
        if (label == null || label.compareTo("") == 0) {
            _logger.warn("Incomplete data entry -- form not saved.");
            return "add_standard_report_template";
        }
        if (rootConceptCode == null || rootConceptCode.compareTo("") == 0) {
            _logger.warn("Incomplete data entry -- form not saved.");
            return "add_standard_report_template";
        }
        if (selectedLevel == null || selectedLevel.compareTo("") == 0) {
            _logger.warn("Incomplete data entry -- form not saved.");
            return "add_standard_report_template";
        }

        if (direction_str.compareToIgnoreCase("source") == 0)
            direction = Boolean.FALSE;
        else
            direction = Boolean.TRUE;

        char delimiter = '$';

        _logger.warn("==========");
        _logger.warn("label: " + label);
        _logger.warn("codingSchemeName: " + codingSchemeName);
        _logger.warn("codingSchemeVersion: " + codingSchemeVersion);
        _logger.warn("rootConceptCode: " + rootConceptCode);
        _logger.warn("associationname: " + selectedAssociation);
        _logger.warn("direction: " + direction);
        _logger.warn("level: " + selectedLevel);
        _logger.warn("delimiter: " + delimiter);

        // Save results using SDK writable API.

        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();

            String FQName =
                "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
            String methodName = "setLabel";
            String key = label;

            Object standardReportTemplate_obj =
                sdkclientutil.search(FQName, methodName, key);
            standardReportTemplate_obj =
                sdkclientutil.search(FQName, methodName, key);

            if (standardReportTemplate_obj != null) {
                String message =
                    "Unable to save -- the report template with the specified label, "
                        + label + ", already exists. ";
                request.getSession().setAttribute("message", message);
                return "message";
            }

            if (selectedLevel.equalsIgnoreCase(OntologyBean.LEVEL_ALL)) {
                selectedLevel = "-1";
            }
            sdkclientutil.insertStandardReportTemplate(label, codingSchemeName,
                codingSchemeVersion, rootConceptCode, selectedAssociation,
                direction, Integer.parseInt(selectedLevel), delimiter);
            setSelectedStandardReportTemplate(label);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return "generate_standard_report";
        return "standard_report_template";
    }

    public String saveModifiedTemplateAction() {
        HttpServletRequest request = SessionUtil.getRequest();
        String label =
            (String) request.getSession().getAttribute(
                "selectedStandardReportTemplate");

        String codingScheme = (String) request.getParameter("codingScheme");
        String version = (String) request.getParameter("version");

        _logger
            .debug("saveModifiedTemplateAction: codingScheme " + codingScheme);
        _logger.debug("saveModifiedTemplateAction: version " + version);

        if (codingScheme == null || version == null) {
            String message =
                "Software Error: codingScheme and version can not be null:"
                    + "\n  * Coding scheme: " + codingScheme
                    + "\n  * version: " + version
                    + "\nPlease report this issue.";
            request.getSession().setAttribute("message", message);
            return "message";
        }

        Boolean csnv_valid =
            DataUtils.validateCodingScheme(codingScheme, version);
        if (csnv_valid == null || csnv_valid.equals(Boolean.FALSE)) {
            String message =
                "Invalid coding scheme name "
                    + codingScheme
                    + " or version "
                    + version
                    + " -- The report template may be out of date. Please modify it and resubmit.";
            request.getSession().setAttribute("message", message);
            return "message";
        }

        String rootConceptCode =
            (String) request.getParameter("rootConceptCode");
        if (rootConceptCode == null || rootConceptCode.trim().length() == 0) {
            String message =
                "Invalid root concept code " + rootConceptCode
                    + " -- Please complete data entry.";
            request.getSession().setAttribute("message", message);
            return "message";
        }
        rootConceptCode = rootConceptCode.trim();
        _logger.debug("saveModifiedTemplateAction: rootConceptCode: "
            + rootConceptCode);

        OntologyBean ontologyBean = BeanUtils.getOntologyBean();
        String associationName = ontologyBean.getSelectedAssociation();
        _logger.debug("saveModifiedTemplateAction: associationName: "
            + associationName);

        if (associationName == null) {
            String message =
                "Software Error: associationName can not be null:"
                    + "\nPlease report this issue.";
            request.getSession().setAttribute("message", message);
            return "message";
        }

        String direction_str = (String) request.getParameter("direction");
        Boolean direction = null;
        if (direction_str.compareTo("source") == 0)
            direction = Boolean.FALSE;
        else
            direction = Boolean.TRUE;

        String level_str = ontologyBean.getSelectedLevel();

        // return to error page
        if (label == null || label.compareTo("") == 0) {
            _logger.warn("Incomplete data entry -- form not saved.");
            return "modify_standard_report_template";
        }
        if (rootConceptCode == null || rootConceptCode.compareTo("") == 0) {
            _logger.warn("Incomplete data entry -- form not saved.");
            return "modify_standard_report_template";
        }
        if (level_str == null || level_str.compareTo("") == 0) {
            _logger.warn("Incomplete data entry -- form not saved.");
            return "modify_standard_report_template";
        }

        Integer level = OntologyBean.levelToInt(level_str);
        if (level < -1) {
            String message =
                "Invalid level " + level
                    + " -- Please modify the report template and resubmit.";
            request.getSession().setAttribute("message", message);
            return "message";
        }

        // char delimiter = '$';

        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();

            StandardReportTemplate standardReportTemplate = null;
            String FQName =
                "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
            String methodName = "setLabel";
            String key = label;

            Object standardReportTemplate_obj =
                sdkclientutil.search(FQName, methodName, key);
            if (standardReportTemplate_obj == null) {
                String message =
                    "Unable to update template -- the report template with the specified label, "
                        + label + " is not found. ";
                request.getSession().setAttribute("message", message);
                return "message";
            }

            standardReportTemplate =
                (StandardReportTemplate) standardReportTemplate_obj;
            standardReportTemplate.setLabel(label);
            standardReportTemplate.setCodingSchemeName(codingScheme);
            standardReportTemplate.setCodingSchemeVersion(version);
            standardReportTemplate.setRootConceptCode(rootConceptCode);
            standardReportTemplate.setAssociationName(associationName);
            standardReportTemplate.setDirection(direction);
            standardReportTemplate.setLevel(level);
            sdkclientutil.updateStandardReportTemplate(standardReportTemplate);

            key = codingScheme + " (version: " + version + ")";
            request.getSession().setAttribute("selectedOntology", key);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "standard_report_template";
    }

    public String deleteReportTemplateAction() {
        HttpServletRequest request = SessionUtil.getRequest();
        String template_label =
            (String) request.getSession().getAttribute(
                "selectedStandardReportTemplate");

        _logger.warn("deleteReportTemplateAction: " + template_label);

        try {
            StandardReportTemplate template =
                getStandardReportTemplate(template_label);
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            sdkclientutil.deleteStandardReportTemplate(template);

            // setSelectedStandardReportTemplate(label);
            getStandardReportTemplateList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "standard_report_template";
    }

    public StandardReportTemplate getStandardReportTemplate(String label) {
        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            String FQName =
                "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
            String methodName = "setLabel";
            Object obj = sdkclientutil.search(FQName, methodName, label);
            StandardReportTemplate standardReportTemplate =
                (StandardReportTemplate) obj;
            return standardReportTemplate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ReportColumn getReportColumn(int id) {
        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportColumn";
            String methodName = "setId";
            Object obj = sdkclientutil.search(FQName, methodName, id);
            ReportColumn reportColumn = (ReportColumn) obj;
            return reportColumn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // public String addReportColumnAction() {
    public String saveReportColumnAction() {
        HttpServletRequest request = SessionUtil.getRequest();
        StandardReportTemplate standardReportTemplate = null;
        standardReportTemplate =
            getStandardReportTemplate(_selectedStandardReportTemplate);
        if (standardReportTemplate == null) {
            String message =
                "ERROR saving ReportColumn -- Unable to identify report template "
                    + _selectedStandardReportTemplate;
            request.getSession().setAttribute("message", message);
            return "message";
        }

        String fieldlabel = (String) request.getParameter("fieldlabel");
        String columnNumber_str = (String) request.getParameter("columnNumber");

        String fieldType =
            (String) request.getSession().getAttribute("selectedDataCategory");
        String propertyType =
            (String) request.getSession().getAttribute("selectedPropertyType");
        String propertyName =
            (String) request.getSession().getAttribute("selectedPropertyName");
        String representationalForm =
            (String) request.getSession().getAttribute(
                "selectedRepresentationalForm");
        String source =
            (String) request.getSession().getAttribute("selectedSource");
        String propertyQualifier =
            (String) request.getSession().getAttribute(
                "selectedPropertyQualifier");
        String qualifierValue = (String) request.getParameter("qualifiervalue");
        String conditionalColumnId =
            (String) request.getParameter("dependentfield");

        if (columnNumber_str == null || fieldlabel == null) {
            String message =
                "Unable to save ReportColumn -- please complete data entry.";
            request.getSession().setAttribute("message", message);
            return "message";
        }
        columnNumber_str = columnNumber_str.trim();
        fieldlabel = fieldlabel.trim();
        if (columnNumber_str.length() == 0 || fieldlabel.length() == 0) {
            String message =
                "Unable to save ReportColumn -- please complete data entry.";
            request.getSession().setAttribute("message", message);
            return "message";
        }

        int columnNumber = Integer.parseInt(columnNumber_str);
        int ccid = -1;
        if (conditionalColumnId != null && conditionalColumnId != "") {
            ccid = Integer.parseInt(conditionalColumnId);
        }

        String preferred = (String) request.getParameter("preferred");
        Boolean isPreferred = null;
        if (preferred != null) {
            if (preferred.equalsIgnoreCase("yes"))
                isPreferred = Boolean.TRUE;
            if (preferred.equalsIgnoreCase("no"))
                isPreferred = Boolean.FALSE;
        }

        String delim =
            (String) request.getSession().getAttribute("selectedDelimiter");
        char delimiter = ' ';
        if (delim != null) {
            if (delim.length() > 0) {
                delimiter = delim.charAt(0);
            }
        }

        _logger.debug("==========");
        _logger.debug("columnNumber: " + columnNumber);
        _logger.debug("fieldlabel: " + fieldlabel);
        _logger.debug("fieldType: " + fieldType);
        _logger.debug("propertyType: " + propertyType);
        _logger.debug("propertyName: " + propertyName);
        _logger.debug("isPreferred: " + isPreferred);
        _logger.debug("representationalForm: " + representationalForm);
        _logger.debug("source: " + source);
        _logger.debug("propertyQualifier: " + propertyQualifier);
        _logger.debug("qualifierValue: " + qualifierValue);
        _logger.debug("delim: " + delim);

        // Save results using SDK writable API.
        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            // check duplicate column number and column label
            Collection<ReportColumn> cc =
                standardReportTemplate.getColumnCollection();
            if (cc != null) {
                Object[] objs = cc.toArray();
                if (objs.length > 0) {
                    for (int i = 0; i < objs.length; i++) {
                        gov.nih.nci.evs.reportwriter.bean.ReportColumn c =
                            (gov.nih.nci.evs.reportwriter.bean.ReportColumn) objs[i];
                        String col_label = c.getLabel();
                        if (col_label.compareToIgnoreCase(fieldlabel) == 0) {
                            String message =
                                "Unable to save ReportColumn -- the column label already exists.";
                            request.getSession().setAttribute("message",
                                message);
                            return "message";
                        }
                        Integer col_num = c.getColumnNumber();
                        if (col_num.intValue() == columnNumber) {
                            String message =
                                "Unable to save ReportColumn -- the column number already exists.";
                            request.getSession().setAttribute("message",
                                message);
                            return "message";
                        }
                    }
                }
            }

            ReportColumn col =
                sdkclientutil.createReportColumn(fieldlabel, columnNumber,
                    fieldType, propertyType, propertyName, isPreferred,
                    representationalForm, source, propertyQualifier,
                    qualifierValue, delimiter, ccid);
            col.setReportTemplate(standardReportTemplate);
            sdkclientutil.insertReportColumn(col);
            _logger.debug("completed insertReportColumn: ");

            request.getSession().setAttribute("selectedStandardReportTemplate",
                _selectedStandardReportTemplate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "standard_report_column";
    }

    public List<SelectItem> getReportFormatList() {
        _reportFormatListData = DataUtils.getReportFormatListData();
        _reportFormatList = new ArrayList<SelectItem>();
        for (int i = 0; i < _reportFormatListData.size(); i++) {
            String t = _reportFormatListData.elementAt(i);
            _reportFormatList.add(new SelectItem(t));
        }
        if (_reportFormatList != null && _reportFormatList.size() > 0) {
            _selectedReportFormat = _reportFormatList.get(0).getLabel();
        }

        return _reportFormatList;
    }

    public void setSelectedReportFormat(String selectedReportFormat) {
        _selectedReportFormat = selectedReportFormat;
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedReportFormat",
            selectedReportFormat);
    }

    public String getSelectedReportFormat() {
        return _selectedReportFormat;
    }

    public void ReportFormatSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        setSelectedReportFormat(_selectedReportFormat);
    }

    public List<SelectItem> getReportStatusList() {
        _reportStatusListData = DataUtils.getReportStatusListData();
        _reportStatusList = new ArrayList<SelectItem>();
        for (int i = 0; i < _reportStatusListData.size(); i++) {
            String t = _reportStatusListData.elementAt(i);
            _reportStatusList.add(new SelectItem(t));
        }
        if (_reportStatusList != null && _reportStatusList.size() > 0) {
            _selectedReportStatus = _reportStatusList.get(0).getLabel();
        }

        return _reportStatusList;
    }

    public void setSelectedReportStatus(String selectedReportStatus) {
        _selectedReportStatus = selectedReportStatus;
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedReportStatus",
            selectedReportStatus);
    }

    public String getSelectedReportStatus() {
        return _selectedReportStatus;
    }

    public void reportStatusSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        setSelectedReportStatus(_selectedReportStatus);
    }

    public String addStatusAction() {
        // HttpServletRequest request = (HttpServletRequest) FacesContext
        // .getCurrentInstance().getExternalContext().getRequest();
        // String statusValue = (String) request.getParameter("statusValue");
        return "report_status";
    }

    public String assignStatusAction() {
        HttpServletRequest request = SessionUtil.getRequest();
        // save to database
        String reportTemplate =
            (String) request.getSession().getAttribute(
                "selectedStandardReportTemplate_draft");
        String statusValue =
            (String) request.getSession().getAttribute("selectedReportStatus");

        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            StandardReportTemplate standardReportTemplate = null;
            String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
            Object[] objs = sdkclientutil.search(FQName);
            if (objs != null && objs.length > 0) {
                for (int i = 0; i < objs.length; i++) {
                    StandardReport standardReport = (StandardReport) objs[i];
                    standardReportTemplate = standardReport.getTemplate();
                    if (standardReportTemplate != null) {
                        if (reportTemplate.compareTo(standardReportTemplate
                            .getLabel()) == 0) {
                            FQName =
                                "gov.nih.nci.evs.reportwriter.bean.ReportStatus";
                            String methodName = "setLabel";
                            String key = statusValue;

                            Object status_obj =
                                sdkclientutil.search(FQName, methodName, key);
                            if (status_obj != null) {
                                standardReport
                                    .setStatus((ReportStatus) status_obj);
                                java.util.Date lastModified = new Date(); // system
                                // date
                                standardReport.setLastModified(lastModified);
                                sdkclientutil
                                    .updateStandardReport(standardReport);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        _standardReportTemplateList_draft =
            getStandardReportTemplateList_draft();

        return "assign_report_status";
    }

    public String addReportTemplateAction() {
        OntologyBean ontologyBean = BeanUtils.getOntologyBean();
        ontologyBean.setSelectedAssociation(OntologyBean.DEFAULT_ASSOCIATION);
        ontologyBean.setSelectedLevel(null);

        return "add_standard_report_template";
    }

    public String modifyReportTemplateAction() {
        HttpServletRequest request = SessionUtil.getRequest();
        String templateLabel =
            (String) request.getSession().getAttribute(
                "selectedStandardReportTemplate");

        // find thesaurus name through template
        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();

            _logger.debug("modifyReportTemplateAction" + " " + templateLabel);

            StandardReportTemplate standardReportTemplate = null;
            String FQName =
                "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
            String methodName = "setLabel";
            String key = templateLabel;
            Object standardReportTemplate_obj =
                sdkclientutil.search(FQName, methodName, key);
            if (standardReportTemplate_obj != null) {
                standardReportTemplate =
                    (StandardReportTemplate) standardReportTemplate_obj;

                _logger.debug("modifyReportTemplateAction" + " "
                    + standardReportTemplate.getCodingSchemeName());

                _versionList =
                    getVersionList(standardReportTemplate.getCodingSchemeName());

                // StandardReportTemplate standardReportTemplate =
                // getStandardReportTemplate(selectedStandardReportTemplate);
                String ontologyNameAndVersion =
                    standardReportTemplate.getCodingSchemeName()
                        + " (version: "
                        + standardReportTemplate.getCodingSchemeVersion() + ")";
                request.getSession().setAttribute("selectedOntology",
                    ontologyNameAndVersion);

                OntologyBean ontologyBean = BeanUtils.getOntologyBean();
                String associationName =
                    standardReportTemplate.getAssociationName();
                ontologyBean.setSelectedAssociation(associationName);
                Integer level = standardReportTemplate.getLevel();
                ontologyBean.setSelectedLevel(level.toString());
            }
        } catch (Exception ex) {
            String message =
                "Unable to construct available coding scheme version list."
                    + "\n* Exception: " + ex.getLocalizedMessage();
            request.getSession().setAttribute("message", message);
            return "message";
        }

        return "modify_standard_report_template";
    }

    public String editReportContentAction() {
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedStandardReportTemplate",
            _selectedStandardReportTemplate);

        StandardReportTemplate standardReportTemplate =
            getStandardReportTemplate(_selectedStandardReportTemplate);
        String ontologyNameAndVersion =
            standardReportTemplate.getCodingSchemeName() + " (version: "
                + standardReportTemplate.getCodingSchemeVersion() + ")";
        request.getSession().setAttribute("selectedOntology",
            ontologyNameAndVersion);

        return "standard_report_column";
    }

    public String generateStandardReportAction() {
        HttpServletRequest request = SessionUtil.getRequest();
        String templateId =
            (String) request.getSession().getAttribute(
                "selectedStandardReportTemplate");

        _logger.debug("generateStandardReportAction: " + templateId);

        // boolean set_defined_by_code = true;
        String defining_set_desc = null;
        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            StandardReportTemplate standardReportTemplate = null;
            String FQName =
                "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
            String methodName = "setLabel";
            String key = templateId;
            Object standardReportTemplate_obj =
                sdkclientutil.search(FQName, methodName, key);
            if (standardReportTemplate_obj != null) {
                standardReportTemplate =
                    (StandardReportTemplate) standardReportTemplate_obj;

                String codingscheme =
                    standardReportTemplate.getCodingSchemeName();
                String version =
                    standardReportTemplate.getCodingSchemeVersion();

                _logger.debug("generateStandardReportAction: codingscheme "
                    + codingscheme);
                _logger
                    .debug("generateStandardReportAction: version " + version);

                Boolean csnv_valid =
                    DataUtils.validateCodingScheme(codingscheme, version);
                if (csnv_valid == null || csnv_valid.equals(Boolean.FALSE)) {
                    String message =
                        "Invalid coding scheme name "
                            + codingscheme
                            + " or version "
                            + version
                            + " -- The report template may be out of date. Please modify it and resubmit.";
                    request.getSession().setAttribute("message", message);
                    return "message";
                }

                defining_set_desc = standardReportTemplate.getRootConceptCode();
                String rootConceptCode = null;
                if (defining_set_desc.indexOf("|") == -1) {
                    rootConceptCode =
                        standardReportTemplate.getRootConceptCode();
                    String ltag = null;
                    Concept rootConcept =
                        DataUtils.getConceptByCode(codingscheme, version, ltag,
                            rootConceptCode);
                    if (rootConcept == null) {
                        String message =
                            "Invalid root concept code "
                                + rootConceptCode
                                + " -- Please modify the report template and resubmit.";
                        request.getSession().setAttribute("message", message);
                        return "message";
                    }
                    String associationName =
                        standardReportTemplate.getAssociationName();
                    key = codingscheme + " (version: " + version + ")";
                    Vector<String> associationname_vec =
                        DataUtils.getSupportedAssociationNames(key);
                    if (!associationname_vec.contains(associationName)) {
                        String message =
                            "Invalid association name "
                                + associationName
                                + " -- Please modify the report template and resubmit.";
                        request.getSession().setAttribute("message", message);
                        return "message";
                    }
                } else {
                    // set_defined_by_code = false;
                }
            }
        } catch (Exception ex) {
            String message =
                "Exception encountered when generating " + templateId + ".";
            request.getSession().setAttribute("message", message);
            return "message";
        }

        String uid = (String) request.getSession().getAttribute("uid");
        if (uid == null) {
            String message = "You must first login to perform this function.";
            request.getSession().setAttribute("message", message);
            return "message";
        }

        String reportFormat_value = "Text (tab delimited)";
        String reportStatus_value = "DRAFT";

        String message =
            new StandardReportService().validReport(
                _selectedStandardReportTemplate, reportFormat_value,
                reportStatus_value, uid);

        if (message.compareTo("success") != 0) {
            request.getSession().setAttribute("message", message);
            return "message";
        }

        String download_dir = null;
        try {
            download_dir =
                ReportWriterProperties
                    .getProperty(ReportWriterProperties.REPORT_DOWNLOAD_DIRECTORY);
        } catch (Exception ex) {

        }

        _logger.debug("download_dir " + download_dir);
        if (download_dir == null) {
            message =
                "The download directory has not been set up properly -- ask your administrator to check JBoss setting in properties-service.xml.";
            request.getSession().setAttribute("message", message);
            return "message";
        }

        new StandardReportService().generateStandardReport(download_dir,
            _selectedStandardReportTemplate, uid);

        message =
            "You request has been received. The report, "
                + templateId
                + ", in tab-delimited and Microsft Excel formats will be generated and placed in the designated output directory."
                + " Please review and assign an APPROVED status before making it available to the users.";
        request.getSession().setAttribute("message", message);
        return "message"; // replaced by a messsage page (back button)
    }

    public String downloadReportAction() {
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedStandardReportTemplate",
            _selectedStandardReportTemplate);

        getStandardReportTemplate(_selectedStandardReportTemplate);
        // String ontologyNameAndVersion =
        // standardReportTemplate.getCodingSchemeName() + " (version: " +
        // standardReportTemplate.getCodingSchemeVersion() + ")";

        _logger.debug("downloading report " + _selectedStandardReportTemplate);

        String download_dir = null;
        try {
            download_dir =
                ReportWriterProperties
                    .getProperty(ReportWriterProperties.REPORT_DOWNLOAD_DIRECTORY);
            // logger.debug("download_dir " + download_dir);

        } catch (Exception ex) {

            String message =
                "Unable to download the specified report -- download directory does not exist -- check with system administrator.";
            request.getSession().setAttribute("message", message);
            return "message";
        }

        File dir = new File(download_dir);
        if (!dir.exists()) {
            _logger
                .debug("Unable to download the specified report -- download directory does not exist. ");
            String message =
                "Unable to download " + _selectedStandardReportTemplate
                    + " -- download directory does not exist. ";
            request.getSession().setAttribute("message", message);
            return "message";
        }

        File[] fileList = dir.listFiles();
        int len = fileList.length;
        while (len > 0) {
            len--;
            if (!fileList[len].isDirectory()) {
                String name = fileList[len].getName();
                _logger.debug("File found in the download directory: " + name);
            }
        }

        // request.getSession().setAttribute("selectedOntology",
        // ontologyNameAndVersion);

        // find available reports in the download directory

        // Check if selectedStandardReportTemplate has been approved.

        // to be implemented:
        boolean approved = true;
        if (approved) {
            return "download";
        } else {
            String message =
                "The " + _selectedStandardReportTemplate
                    + " has not been approved for download.";
            request.getSession().setAttribute("message", message);
            return "message";
        }
        // if not, display a message page indicating such.

        // otherwise, route to generate_standard_report

        // return "generate_standard_report";
    }

    public String saveStatusAction() {
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedStandardReportTemplate",
            _selectedStandardReportTemplate);

        StandardReportTemplate standardReportTemplate =
            getStandardReportTemplate(_selectedStandardReportTemplate);

        String message =
            "The status of the " + standardReportTemplate.getLabel()
                + " has been updated successfully.";

        request.getSession().setAttribute("message", message);

        return "message"; // replaced by a messsage page (back button)
    }

    private String _selectedVersion = null;
    private List<SelectItem> _versionList = null;
    private Vector<String> _versionListData = null;

    public List<SelectItem> getVersionList(String codingschemename) {
        _versionListData = DataUtils.getVersionListData(codingschemename);
        _versionList = new ArrayList<SelectItem>();
        for (int i = 0; i < _versionListData.size(); i++) {
            String t = _versionListData.elementAt(i);

            _logger.debug("version: " + t);

            _versionList.add(new SelectItem(t));
        }
        if (_versionList != null && _versionList.size() > 0) {
            _selectedVersion = _versionList.get(0).getLabel();
        }
        return _versionList;
    }

    public void setSelectedVersion(String selectedVersion) {
        _selectedVersion = selectedVersion;
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedVersion", selectedVersion);
    }

    public String getSelectedVersion() {
        return _selectedVersion;
    }

    public void versionSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        // int id = Integer.parseInt((String) event.getNewValue());
        setSelectedVersion(_selectedVersion);
    }
}
