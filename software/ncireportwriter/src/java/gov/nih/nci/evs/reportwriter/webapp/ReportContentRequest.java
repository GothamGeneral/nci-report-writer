package gov.nih.nci.evs.reportwriter.webapp;

import java.util.*;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.reportwriter.service.*;
import gov.nih.nci.evs.reportwriter.utils.*;

import javax.servlet.http.*;

import org.LexGrid.codingSchemes.*;
import org.LexGrid.concepts.*;
import org.apache.log4j.*;

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

public class ReportContentRequest {
    private static Logger _logger =
        Logger.getLogger(ReportContentRequest.class);
    private StandardReportTemplate _standardReportTemplate = null;
    private String _selectedStandardReportTemplate = null;

    // -------------------------------------------------------------------------
    public ReportContentRequest(String selectedStandardReportTemplate) {
        init(selectedStandardReportTemplate);
    }

    private void init(String selectedStandardReportTemplate) {
        _selectedStandardReportTemplate = selectedStandardReportTemplate;
        UserSessionBean userSessionBean = BeanUtils.getUserSessionBean();
        _standardReportTemplate =
            userSessionBean
                .getStandardReportTemplate(_selectedStandardReportTemplate);
    }

    // -------------------------------------------------------------------------
    public String displayCodingSchemeWarning(HttpServletRequest request) {
        String csn = _standardReportTemplate.getCodingSchemeName();
        String version = _standardReportTemplate.getCodingSchemeVersion();
        String csnv = DataUtils.getCSNVKey(csn, version);

        String versionTmp = DataUtils.getCodingSchemeVersion(csnv);
        if (versionTmp != null)
            return null;

        CodingScheme cs = DataUtils.getCodingScheme(csn);
        if (cs == null)
            return HTTPUtils.warningMsg(request,
                "The following vocabulary is not loaded:\n" + "    * " + csnv);

        versionTmp = cs.getRepresentsVersion();
        String csnvLatest = DataUtils.getCSNVKey(csn, versionTmp);
        String msg = "";
        msg += "The selected report template is referencing an older";
        msg += " or invalid version of the coding scheme:\n";
        msg += "    * Current version: " + csnv + "\n";
        msg += "    * Latest version: " + csnvLatest + "\n";
        msg += "\n";
        msg += "Please update the version number of the coding scheme";
        msg += " by selecting the Modify button.";
        return HTTPUtils.warningMsg(request, msg);
    }

    // -------------------------------------------------------------------------
    public String editAction() {
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedStandardReportTemplate",
            _selectedStandardReportTemplate);

        String csn = _standardReportTemplate.getCodingSchemeName();
        String version = _standardReportTemplate.getCodingSchemeVersion();
        String csnv = DataUtils.getCSNVKey(csn, version);
        request.getSession().setAttribute("selectedOntology", csnv);

        String warningMsg = displayCodingSchemeWarning(request);
        return warningMsg == null ? "standard_report_column" : warningMsg;
    }

    public String generateAction() {
        HttpServletRequest request = SessionUtil.getRequest();

        String warningMsg = displayCodingSchemeWarning(request);
        if (warningMsg != null)
            return warningMsg;

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
                _logger.debug("generateStandardReportAction: version "
                    + version);

                if (!DataUtils.isValidCodingScheme(codingscheme, version)) {
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
                        DataUtils.getSupportedAssociations(
                            DataUtils.AssociationType.Names, key);
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
            StandardReportService.validReport(_selectedStandardReportTemplate,
                reportFormat_value, reportStatus_value, uid);

        if (message.compareTo("success") != 0) {
            request.getSession().setAttribute("message", message);
            return "message";
        }

        String download_dir =
            AppProperties.getInstance().getProperty(
                AppProperties.REPORT_DOWNLOAD_DIRECTORY);

        _logger.debug("download_dir " + download_dir);
        if (download_dir == null) {
            message =
                "The download directory has not been set up properly -- ask your administrator to check JBoss setting in properties-service.xml.";
            request.getSession().setAttribute("message", message);
            return "message";
        }

        String emailAddress =
            (String) request.getSession().getAttribute("email");
        _logger.debug("emailAddress: " + emailAddress);
        StandardReportService.generateStandardReport(download_dir,
            _selectedStandardReportTemplate, uid, emailAddress);

        message =
            "You request has been received. The report, "
                + templateId
                + ", in tab-delimited and Microsft Excel formats will be generated and placed in the designated output directory."
                + " Please review and assign an APPROVED status before making it available to the users.";
        if (emailAddress != null && emailAddress.length() > 0) {
            message +=
                "\n\nOnce the report is generated, an email notification will be sent to "
                    + emailAddress + ".";
        }
        request.getSession().setAttribute("message", message);
        return "message"; // replaced by a messsage page (back button)
    }
}
