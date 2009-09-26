package gov.nih.nci.evs.reportwriter.utils;

import java.io.*;
import java.util.*;
import gov.nih.nci.system.applicationservice.*;
import gov.nih.nci.evs.reportwriter.bean.*;
import javax.faces.model.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import gov.nih.nci.evs.reportwriter.properties.*;
import org.LexGrid.LexBIG.Exceptions.*;

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

public class DataUtils {
    private int maxReturn = 5000;

    private static List<SelectItem> standardReportTemplateList = null;
    private static List<SelectItem> adminTaskList = null;
    private static List<SelectItem> userTaskList = null;
    private static List<SelectItem> propertyTypeList = null;
    private static List<SelectItem> _ontologies = null;

    public org.LexGrid.LexBIG.Utility.ConvenienceMethods lbConvMethods = null;
    public CodingSchemeRenderingList csrl = null;
    private static HashMap<String, CodingScheme> codingSchemeMap = null;

    private static HashMap<String, String> csnv2codingSchemeNameMap = null;
    private static HashMap<String, String> csnv2VersionMap = null;

    //==========================================================================
    // For customized query use
    //==========================================================================
    public static int ALL = 0;
    public static int PREFERRED_ONLY = 1;
    public static int NON_PREFERRED_ONLY = 2;

//    private static int RESOLVE_SOURCE = 1;
//    private static int RESOLVE_TARGET = -1;
//    private static int RESTRICT_SOURCE = -1;
//    private static int RESTRICT_TARGET = 1;

    public static final int SEARCH_NAME_CODE = 1;
    public static final int SEARCH_DEFINITION = 2;

    public static final int SEARCH_PROPERTY_VALUE = 3;
    public static final int SEARCH_ROLE_VALUE = 6;
    public static final int SEARCH_ASSOCIATION_VALUE = 7;

//    private static final List<String> STOP_WORDS =
//        Arrays.asList(new String[] { "a", "an", "and", "by", "for", "of", "on",
//                "in", "nos", "the", "to", "with" });

    //==========================================================================
    public DataUtils() {
        adminTaskList = new ArrayList<SelectItem>();
        adminTaskList.add(new SelectItem("Administer Standard Reports"));
        adminTaskList.add(new SelectItem("Maintain Report Status"));
        adminTaskList.add(new SelectItem("Assign Report Status"));
        adminTaskList.add(new SelectItem("Retrieve Standard Reports"));

        userTaskList = new ArrayList<SelectItem>();
        userTaskList.add(new SelectItem("Retrieve Standard Reports"));

        standardReportTemplateList = new ArrayList<SelectItem>();

        setCodingSchemeMap();

        String max_return_str = null;
        try {
            max_return_str =
                ReportWriterProperties
                        .getProperty(ReportWriterProperties.MAXIMUM_RETURN);
            if (max_return_str != null) {
                maxReturn = Integer.parseInt(max_return_str);
            }
        } catch (Exception ex) {

        }
    }

    public static List<SelectItem> getPropertyTypeList() {
        if (propertyTypeList == null) {
            propertyTypeList = new ArrayList<SelectItem>();
            propertyTypeList.add(new SelectItem(""));
            propertyTypeList.add(new SelectItem("COMMENT"));
            propertyTypeList.add(new SelectItem("DEFINITION"));
            propertyTypeList.add(new SelectItem("GENERIC"));
            propertyTypeList.add(new SelectItem("INSTRUCTION"));
            propertyTypeList.add(new SelectItem("PRESENTATION"));
        }
        return propertyTypeList;
    }

    public static List<SelectItem> getTaskList(Boolean isAdmin) {

        if (isAdmin == null)
            return null;
        else if (isAdmin.equals(Boolean.TRUE)) {
            if (adminTaskList == null) {
                adminTaskList = new ArrayList<SelectItem>();
                adminTaskList
                        .add(new SelectItem("Administer Standard Reports"));
                adminTaskList.add(new SelectItem("Maintain Report Status"));
                adminTaskList.add(new SelectItem("Assign Report Status"));
                adminTaskList.add(new SelectItem("Retrieve Standard Reports"));
            }
            return adminTaskList;
        } else {
            if (userTaskList == null) {
                userTaskList = new ArrayList<SelectItem>();
                userTaskList.add(new SelectItem("Retrieve Standard Reports"));
            }
            return userTaskList;
        }
    }

    // public static List getStandardReportTemplateList(Boolean isAdmin) {
    public static List<SelectItem> getStandardReportTemplateList() {
        // if (isAdmin == null || isAdmin == Boolean.FALSE) return null;

        standardReportTemplateList = new ArrayList<SelectItem>();
        try {
            SDKClientUtil util = new SDKClientUtil();
            String FQName =
                "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
            Object[] objs = util.search(FQName);
            if (objs.length == 0)
                return standardReportTemplateList;
            Vector<String> v = new Vector<String>();
            for (int i = 0; i < objs.length; i++) {
                StandardReportTemplate standardReportTemplate =
                    (StandardReportTemplate) objs[i];
                // standardReportTemplateList.add(new
                // SelectItem(standardReportTemplate.getLabel()));
                v.add(standardReportTemplate.getLabel());
            }
            SortUtils.quickSort(v);
            for (int i = 0; i < v.size(); i++) {
                String name = (String) v.elementAt(i);
                standardReportTemplateList.add(new SelectItem(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return standardReportTemplateList;
    }

    public static List<SelectItem> getOntologyList() {
        if (_ontologies == null)
            setCodingSchemeMap();
        return _ontologies;
    }

    public static Boolean validateCodingScheme(String formalname, String version) {
        // to be implemented:
        /*
         * System.out.println("DataUtils 	validateCodingScheme " ); if
         * (csnv2codingSchemeNameMap == null || csnv2VersionMap == null) {
         * System
         * .out.println("DataUtils 	validateCodingScheme calling setCodingSchemeMap"
         * ); setCodingSchemeMap(); return validateCodingScheme(formalname,
         * version); }
         * 
         * String key = formalname + " (version: " + version + ")";
         * System.out.println("DataUtils 	validateCodingScheme key: " + key); if
         * (csnv2codingSchemeNameMap.get(key) == null ||
         * csnv2VersionMap.get(key) == null ) {System.out.println( "DataUtils
         * validateCodingScheme csnv2codingSchemeNameMap.get(key) == null ||
         * csnv2VersionMap.get(key) == null :??? " );
         * System.out.println("DataUtils 	Boolean.FALSE "); return
         * Boolean.FALSE; } else {
         * System.out.println("DataUtils 	Boolean.TRUE "); }
         */
        return Boolean.TRUE;
    }

    private static void setCodingSchemeMap() {
        // if (_ontologies != null) return;
        _ontologies = new ArrayList<SelectItem>();
        codingSchemeMap = new HashMap<String, CodingScheme>();
        csnv2codingSchemeNameMap = new HashMap<String, String>();
        csnv2VersionMap = new HashMap<String, String>();

        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
            if (csrl == null)
                System.out.println("csrl is NULL");

            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i = 0; i < csrs.length; i++) {
                CodingSchemeRendering csr = csrs[i];
                Boolean isActive =
                    csr.getRenderingDetail().getVersionStatus().equals(
                            CodingSchemeVersionStatus.ACTIVE);
                if (isActive != null && isActive.equals(Boolean.TRUE)) {
                    CodingSchemeSummary css = csr.getCodingSchemeSummary();
                    String formalname = css.getFormalName();
                    String representsVersion = css.getRepresentsVersion();
                    CodingSchemeVersionOrTag vt =
                        new CodingSchemeVersionOrTag();
                    vt.setVersion(representsVersion);

                    CodingScheme scheme = null;
                    try {
                        try {
                            scheme = lbSvc.resolveCodingScheme(formalname, vt);
                        } catch (Exception ex) {
                        }
                        if (scheme != null) {
                            codingSchemeMap.put(formalname, scheme);

                            String value =
                                formalname + " (version: " + representsVersion
                                        + ")";
                            _ontologies.add(new SelectItem(value, value));

                            csnv2codingSchemeNameMap.put(value, formalname);
                            csnv2VersionMap.put(value, representsVersion);

                        }

                    } catch (Exception e) {
                        String urn = css.getCodingSchemeURN();
                        try {
                            scheme = lbSvc.resolveCodingScheme(urn, vt);
                            if (scheme != null) {
                                codingSchemeMap.put(formalname, scheme);

                                String value =
                                    formalname + " (version: "
                                            + representsVersion + ")";
                                _ontologies.add(new SelectItem(value, value));

                                csnv2codingSchemeNameMap.put(value, formalname);
                                csnv2VersionMap.put(value, representsVersion);

                            }

                        } catch (Exception ex) {

                            String localname = css.getLocalName();
                            try {
                                scheme =
                                    lbSvc.resolveCodingScheme(localname, vt);
                                if (scheme != null) {
                                    codingSchemeMap.put(formalname, scheme);

                                    String value =
                                        formalname + " (version: "
                                                + representsVersion + ")";
                                    _ontologies
                                            .add(new SelectItem(value, value));

                                    csnv2codingSchemeNameMap.put(value,
                                            formalname);
                                    csnv2VersionMap.put(value,
                                            representsVersion);

                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Vector<String> getSupportedAssociationNames(String key) {
        if (csnv2codingSchemeNameMap == null) {
            setCodingSchemeMap();
            return getSupportedAssociationNames(key);
        }
        String codingSchemeName = csnv2codingSchemeNameMap.get(key);
        if (codingSchemeName == null)
            return null;
        String version = csnv2VersionMap.get(key);
        if (version == null)
            return null;
        return getSupportedAssociationNames(codingSchemeName, version);
    }

    public static Vector<String> getSupportedAssociationNames(
            String codingSchemeName, String version) {
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null) {
            vt.setVersion(version);
        }

        CodingScheme scheme = null;
        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
            if (scheme == null) {
                System.out.println("scheme is NULL");
                return null;
            }

            Vector<String> v = new Vector<String>();
            SupportedAssociation[] assos =
                scheme.getMappings().getSupportedAssociation();
            for (int i = 0; i < assos.length; i++) {
                SupportedAssociation sa = (SupportedAssociation) assos[i];
                v.add(sa.getLocalId());
            }
            return v;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Vector<String> getPropertyNameListData(String key) {
        if (csnv2codingSchemeNameMap == null) {
            setCodingSchemeMap();
        }

        String codingSchemeName = csnv2codingSchemeNameMap.get(key);
        if (codingSchemeName == null) {
            return null;
        }
        String version = csnv2VersionMap.get(key);
        if (version == null) {
            return null;
        }
        return getPropertyNameListData(codingSchemeName, version);
    }

    public static Vector<String> getPropertyNameListData(
            String codingSchemeName, String version) {
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null) {
            vt.setVersion(version);
        }
        CodingScheme scheme = null;
        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
            if (scheme == null)
                return null;
            Vector<String> propertyNameListData = new Vector<String>();
            SupportedProperty[] properties =
                scheme.getMappings().getSupportedProperty();
            for (int i = 0; i < properties.length; i++) {
                SupportedProperty property = properties[i];
                propertyNameListData.add(property.getLocalId());
            }
            return propertyNameListData;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getCodingSchemeName(String key) {
        return csnv2codingSchemeNameMap.get(key);
    }

    public static String getCodingSchemeVersion(String key) {
        return csnv2VersionMap.get(key);
    }

    public static Vector<String> getRepresentationalFormListData(String key) {
        String codingSchemeName = csnv2codingSchemeNameMap.get(key);
        if (codingSchemeName == null)
            return null;
        String version = csnv2VersionMap.get(key);
        if (version == null)
            return null;
        return getRepresentationalFormListData(codingSchemeName, version);
    }

    public static Vector<String> getRepresentationalFormListData(
            String codingSchemeName, String version) {
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null) {
            vt.setVersion(version);
        }
        CodingScheme scheme = null;
        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
            if (scheme == null)
                return null;
            Vector<String> propertyNameListData = new Vector<String>();
            SupportedRepresentationalForm[] forms =
                scheme.getMappings().getSupportedRepresentationalForm();
            if (true) {
                System.out.println(StringUtils.SEPARATOR);
                System.out.println("MethodgetRepresentationalFormListData");
                System.out.println("* codingSchemeName: " + codingSchemeName);
                System.out.println("* version: " + version);
                System.out.println("* forms: ");
                if (forms != null) {
                    for (int i = 0; i < forms.length; ++i)
                        System.out.println("  " + i + ") "
                                + forms[i].getLocalId());
                }
            }
            if (forms != null) {
                for (int i = 0; i < forms.length; i++) {
                    SupportedRepresentationalForm form = forms[i];
                    propertyNameListData.add(form.getLocalId());
                }
            }
            return propertyNameListData;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Vector<String> getPropertyQualifierListData(String key) {
        String codingSchemeName = csnv2codingSchemeNameMap.get(key);
        if (codingSchemeName == null)
            return null;
        String version = csnv2VersionMap.get(key);
        if (version == null)
            return null;
        return getPropertyQualifierListData(codingSchemeName, version);
    }

    public static Vector<String> getPropertyQualifierListData(
            String codingSchemeName, String version) {
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null) {
            vt.setVersion(version);
        }
        CodingScheme scheme = null;
        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
            if (scheme == null)
                return null;
            Vector<String> propertyQualifierListData = new Vector<String>();
            SupportedPropertyQualifier[] qualifiers =
                scheme.getMappings().getSupportedPropertyQualifier();
            for (int i = 0; i < qualifiers.length; i++) {
                SupportedPropertyQualifier qualifier = qualifiers[i];
                propertyQualifierListData.add(qualifier.getLocalId());
            }

            return propertyQualifierListData;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Vector<String> getSourceListData(String key) {
        if (csnv2codingSchemeNameMap == null) {
            setCodingSchemeMap();
            return getSourceListData(key);
        }
        String codingSchemeName = csnv2codingSchemeNameMap.get(key);
        if (codingSchemeName == null)
            return null;
        String version = csnv2VersionMap.get(key);
        if (version == null)
            return null;
        return getSourceListData(codingSchemeName, version);
    }

    public static Vector<String> getSourceListData(String codingSchemeName,
            String version) {
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null) {
            vt.setVersion(version);
        }
        CodingScheme scheme = null;
        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
            if (scheme == null)
                return null;
            Vector<String> sourceListData = new Vector<String>();

            // Insert your code here
            SupportedSource[] sources =
                scheme.getMappings().getSupportedSource();
            for (int i = 0; i < sources.length; i++) {
                SupportedSource source = sources[i];
                sourceListData.add(source.getLocalId());
            }

            return sourceListData;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Vector<String> getReportStatusListData() {
        Vector<String> reportStatusListData = new Vector<String>();
        try {
            SDKClientUtil util = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportStatus";
            Object[] objs = util.search(FQName);

            if (objs != null && objs.length > 0) {
                for (int i = 0; i < objs.length; i++) {
                    ReportStatus reportStatus = (ReportStatus) objs[i];
                    reportStatusListData.add(reportStatus.getLabel());
                }
            } else {
                // Initial status (DRAFT and APPROVED)
                String label = "DRAFT";
                String description =
                    "Report is a draft, not ready for download.";
                boolean active = true;
                try {
                    util.insertReportStatus(label, description, active);
                } catch (Exception ex) {
                    System.out
                            .println("====== insertReportStatus DRAFT failed.");
                }

                label = "APPROVED";
                description = "Report has been approved for download by users";
                active = true;
                try {
                    util.insertReportStatus(label, description, active);
                } catch (Exception ex) {
                    System.out
                            .println("====== insertReportStatus APPROVED failed.");
                }

                objs = util.search(FQName);
                if (objs != null && objs.length > 0) {
                    for (int i = 0; i < objs.length; i++) {
                        ReportStatus reportStatus = (ReportStatus) objs[i];
                        reportStatusListData.add(reportStatus.getLabel());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportStatusListData;
    }

    public static Vector<String> getReportFormatListData() {
        Vector<String> reportFormatListData = new Vector<String>();
        try {
            SDKClientUtil util = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
            Object[] objs = util.search(FQName);

            if (objs != null && objs.length > 0) {
                for (int i = 0; i < objs.length; i++) {
                    ReportFormat reportFormat = (ReportFormat) objs[i];
                    reportFormatListData.add(reportFormat.getDescription());
                }
            } else {
                String description = "Text (tab delimited)";
                try {
                    util.insertReportFormat(description);
                } catch (Exception ex) {
                    System.out.println("====== insertReportFormat "
                            + description + " failed.");
                }

                description = "Microsoft Office Excel";
                try {
                    util.insertReportFormat(description);
                } catch (Exception ex) {
                    System.out.println("====== insertReportFormat "
                            + description + " failed.");
                }

                objs = util.search(FQName);
                if (objs != null && objs.length > 0) {
                    for (int i = 0; i < objs.length; i++) {
                        ReportFormat reportFormat = (ReportFormat) objs[i];
                        reportFormatListData.add(reportFormat.getDescription());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportFormatListData;
    }

    public static List<ReportFormat> getAvailableReportFormat() {
        List<ReportFormat> list = new ArrayList<ReportFormat>();
        try {
            SDKClientUtil util = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
            Object[] objs = util.search(FQName);

            if (objs == null || objs.length == 0) {
                String description = "Text (tab delimited)";
                try {
                    util.insertReportFormat(description);
                } catch (Exception ex) {
                    System.out.println("====== insertReportFormat "
                            + description + " failed.");
                }

                description = "Microsoft Office Excel";
                try {
                    util.insertReportFormat(description);
                } catch (Exception ex) {
                    System.out.println("====== insertReportFormat "
                            + description + " failed.");
                }

                objs = util.search(FQName);
                if (objs != null && objs.length > 0) {
                    for (int i = 0; i < objs.length; i++) {
                        ReportFormat reportFormat = (ReportFormat) objs[i];
                        list.add(reportFormat);
                    }
                }
            }

            else // if (objs != null && objs.length > 0)
            {
                for (int i = 0; i < objs.length; i++) {
                    ReportFormat reportFormat = (ReportFormat) objs[i];
                    list.add(reportFormat);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String int2String(Integer int_obj) {
        if (int_obj == null) {
            return null;
        }

        String retstr = Integer.toString(int_obj);
        return retstr;
    }

    // =========================================================================

    public static Concept getConceptByCode(String codingSchemeName,
            String vers, String ltag, String code) {
        try {
            ReportWriterProperties
                    .getProperty(ReportWriterProperties.EVS_SERVICE_URL);
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            if (lbSvc == null) {
                System.out.println("lbSvc == null???");
                return null;
            }

            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(vers);

            ConceptReferenceList crefs =
                createConceptReferenceList(new String[] { code },
                        codingSchemeName);

            CodedNodeSet cns = null;

            try {

                System.out
                        .println("DataUtils calling getCodingSchemeConcepts: "
                                + code);

                cns =
                    lbSvc.getCodingSchemeConcepts(codingSchemeName,
                            versionOrTag);
            } catch (Exception e1) {
                System.out
                        .println("DataUtils lbSvc.getCodingSchemeConcepts threw exception??? "
                                + code);
                // e1.printStackTrace();
                return null;
            }

            cns = cns.restrictToCodes(crefs);
            ResolvedConceptReferenceList matches =
                cns.resolveToList(null, null, null, 1);

            if (matches == null) {
                System.out.println("Concept not found.");
                return null;
            }

            // Analyze the result ...
            if (matches.getResolvedConceptReferenceCount() > 0) {
                ResolvedConceptReference ref =
                    (ResolvedConceptReference) matches
                            .enumerateResolvedConceptReference().nextElement();

                Concept entry = ref.getReferencedEntry();
                return entry;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static NameAndValueList createNameAndValueList(String[] names,
            String[] values) {
        NameAndValueList nvList = new NameAndValueList();
        for (int i = 0; i < names.length; i++) {
            NameAndValue nv = new NameAndValue();
            nv.setName(names[i]);
            if (values != null) {
                nv.setContent(values[i]);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }

    public Vector<Concept> getAssociationTargets(String scheme, String version,
            String code, String assocName) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        ResolvedConceptReferenceList matches = null;
        Vector<Concept> v = new Vector<Concept>();
        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            if (lbSvc == null) {
                System.out.println("lbSvc == null???");
                return null;
            }
            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
            NameAndValueList nameAndValueList =
                createNameAndValueList(new String[] { assocName }, null);

            NameAndValueList nameAndValueList_qualifier = null;
            cng =
                cng.restrictToAssociations(nameAndValueList,
                        nameAndValueList_qualifier);

            matches =
                cng.resolveAsList(ConvenienceMethods.createConceptReference(
                        code, scheme), true, false, 1, 1, new LocalNameList(),
                        null, null, maxReturn);

            if (matches.getResolvedConceptReferenceCount() > 0) {
                Enumeration<ResolvedConceptReference> refEnum =
                    matches.enumerateResolvedConceptReference();

                while (refEnum.hasMoreElements()) {
                    ResolvedConceptReference ref = refEnum.nextElement();
                    AssociationList sourceof = ref.getSourceOf();
                    Association[] associations = sourceof.getAssociation();

                    for (int i = 0; i < associations.length; i++) {
                        Association assoc = associations[i];
                        // KLO
                        assoc = processForAnonomousNodes(assoc);
                        AssociatedConcept[] acl =
                            assoc.getAssociatedConcepts()
                                    .getAssociatedConcept();
                        for (int j = 0; j < acl.length; j++) {
                            AssociatedConcept ac = acl[j];
                            v.add(ac.getReferencedEntry());
                        }
                    }
                }
                SortUtils.quickSort(v);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }

    public ResolvedConceptReferenceList getNext(
            ResolvedConceptReferencesIterator iterator) {
        return iterator.getNext();
    }

    /**
     * Dump_matches to output, for debug purposes
     * 
     * @param iterator the iterator
     * @param maxToReturn the max to return
     */
    public Vector<Concept> resolveIterator(
            ResolvedConceptReferencesIterator iterator, int maxToReturn) {
        Vector<Concept> v = new Vector<Concept>();
        if (iterator == null) {
            System.out.println("No match.");
            return v;
        }
        try {
            int iteration = 0;
            while (iterator.hasNext()) {
                iteration++;
                iterator = iterator.scroll(maxToReturn);
                ResolvedConceptReferenceList rcrl = iterator.getNext();
                ResolvedConceptReference[] rcra =
                    rcrl.getResolvedConceptReference();
                for (int i = 0; i < rcra.length; i++) {
                    ResolvedConceptReference rcr = rcra[i];
                    Concept ce = rcr.getReferencedEntry();
                    // System.out.println("Iteration " + iteration + " " +
                    // ce.getId() + " " +
                    // ce.getEntityDescription().getContent());
                    v.add(ce);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public Vector<Concept> getAssociationSources(String scheme, String version,
            String code, String assocName) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        ResolvedConceptReferenceList matches = null;
        Vector<Concept> v = new Vector<Concept>();
        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);

            NameAndValueList nameAndValueList =
                createNameAndValueList(new String[] { assocName }, null);

            NameAndValueList nameAndValueList_qualifier = null;
            cng =
                cng.restrictToAssociations(nameAndValueList,
                        nameAndValueList_qualifier);

            matches =
                cng.resolveAsList(ConvenienceMethods.createConceptReference(
                        code, scheme), false, true, 1, 1, new LocalNameList(),
                        null, null, maxReturn);

            if (matches.getResolvedConceptReferenceCount() > 0) {
                Enumeration<ResolvedConceptReference> refEnum =
                    matches.enumerateResolvedConceptReference();

                while (refEnum.hasMoreElements()) {
                    ResolvedConceptReference ref = refEnum.nextElement();
                    AssociationList targetof = ref.getTargetOf();
                    Association[] associations = targetof.getAssociation();

                    for (int i = 0; i < associations.length; i++) {
                        Association assoc = associations[i];
                        // KLO
                        assoc = processForAnonomousNodes(assoc);
                        AssociatedConcept[] acl =
                            assoc.getAssociatedConcepts()
                                    .getAssociatedConcept();
                        for (int j = 0; j < acl.length; j++) {
                            AssociatedConcept ac = acl[j];
                            v.add(ac.getReferencedEntry());
                        }
                    }
                }
                SortUtils.quickSort(v);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }

    public Vector<String> getParentCodes(String scheme, String version,
            String code) {
        Vector<String> hierarchicalAssoName_vec =
            getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec == null
                || hierarchicalAssoName_vec.size() == 0) {
            return null;
        }
        String hierarchicalAssoName =
            (String) hierarchicalAssoName_vec.elementAt(0);
        // KLO, 01/23/2009
        // Vector<Concept> superconcept_vec = util.getAssociationSources(scheme,
        // version, code, hierarchicalAssoName);
        Vector<String> superconcept_vec =
            getAssociationSourceCodes(scheme, version, code,
                    hierarchicalAssoName);
        if (superconcept_vec == null)
            return null;
        // SortUtils.quickSort(superconcept_vec, SortUtils.SORT_BY_CODE);
        return superconcept_vec;

    }

    public Vector<String> getAssociationSourceCodes(String scheme,
            String version, String code, String assocName) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        ResolvedConceptReferenceList matches = null;
        Vector<String> v = new Vector<String>();
        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);

            NameAndValueList nameAndValueList =
                createNameAndValueList(new String[] { assocName }, null);

            NameAndValueList nameAndValueList_qualifier = null;
            cng =
                cng.restrictToAssociations(nameAndValueList,
                        nameAndValueList_qualifier);

            matches =
                cng.resolveAsList(ConvenienceMethods.createConceptReference(
                        code, scheme), false, true, 1, 1, new LocalNameList(),
                        null, null, maxReturn);

            if (matches.getResolvedConceptReferenceCount() > 0) {
                Enumeration<ResolvedConceptReference> refEnum =
                    matches.enumerateResolvedConceptReference();

                while (refEnum.hasMoreElements()) {
                    ResolvedConceptReference ref = refEnum.nextElement();
                    AssociationList targetof = ref.getTargetOf();
                    Association[] associations = targetof.getAssociation();

                    for (int i = 0; i < associations.length; i++) {
                        Association assoc = associations[i];
                        // KLO
                        assoc = processForAnonomousNodes(assoc);
                        AssociatedConcept[] acl =
                            assoc.getAssociatedConcepts()
                                    .getAssociatedConcept();
                        for (int j = 0; j < acl.length; j++) {
                            AssociatedConcept ac = acl[j];
                            v.add(ac.getReferencedEntry().getId());
                        }
                    }
                }
                SortUtils.quickSort(v);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }

    public static ConceptReferenceList createConceptReferenceList(
            String[] codes, String codingSchemeName) {
        if (codes == null) {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++) {
            ConceptReference cr = new ConceptReference();
            cr.setCodingScheme(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }

    public Vector<String> getSubconceptCodes(String scheme, String version,
            String code) { // throws LBException{
        Vector<String> v = new Vector<String>();
        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                        .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);

            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(version);
            try {
                lbscm.createCodeNodeSet(new String[] { code }, scheme, csvt)
                        .resolveToList(null, null, null, 1)
                        .getResolvedConceptReference(0).getEntityDescription()
                        .getContent();

            } catch (Exception e) {
            }

            // Iterate through all hierarchies and levels ...
            String[] hierarchyIDs = lbscm.getHierarchyIDs(scheme, csvt);
            for (int k = 0; k < hierarchyIDs.length; k++) {
                String hierarchyID = hierarchyIDs[k];
                AssociationList associations = null;
                associations = null;
                try {
                    associations =
                        lbscm.getHierarchyLevelNext(scheme, csvt, hierarchyID,
                                code, false, null);
                } catch (Exception e) {
                    System.out
                            .println("getSubconceptCodes - Exception lbscm.getHierarchyLevelNext  ");
                    return v;
                }

                for (int i = 0; i < associations.getAssociationCount(); i++) {
                    Association assoc = associations.getAssociation(i);
                    AssociatedConceptList concepts =
                        assoc.getAssociatedConcepts();
                    for (int j = 0; j < concepts.getAssociatedConceptCount(); j++) {
                        AssociatedConcept concept =
                            concepts.getAssociatedConcept(j);
                        String nextCode = concept.getConceptCode();
                        v.add(nextCode);
                    }
                }
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
        }
        return v;
    }

    public Vector<String> getSuperconceptCodes(String scheme, String version,
            String code) { // throws LBException{
        long ms = System.currentTimeMillis();
        Vector<String> v = new Vector<String>();
        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                        .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(version);
            try {
                lbscm.createCodeNodeSet(new String[] { code }, scheme, csvt)
                        .resolveToList(null, null, null, 1)
                        .getResolvedConceptReference(0).getEntityDescription()
                        .getContent();
            } catch (Exception e) {
            }

            // Iterate through all hierarchies and levels ...
            String[] hierarchyIDs = lbscm.getHierarchyIDs(scheme, csvt);
            for (int k = 0; k < hierarchyIDs.length; k++) {
                String hierarchyID = hierarchyIDs[k];
                AssociationList associations =
                    lbscm.getHierarchyLevelPrev(scheme, csvt, hierarchyID,
                            code, false, null);
                for (int i = 0; i < associations.getAssociationCount(); i++) {
                    Association assoc = associations.getAssociation(i);
                    AssociatedConceptList concepts =
                        assoc.getAssociatedConcepts();
                    for (int j = 0; j < concepts.getAssociatedConceptCount(); j++) {
                        AssociatedConcept concept =
                            concepts.getAssociatedConcept(j);
                        String nextCode = concept.getConceptCode();
                        v.add(nextCode);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("Run time (ms): "
                    + (System.currentTimeMillis() - ms));
        }
        return v;
    }

    public Vector<String> getHierarchyAssociationId(String scheme,
            String version) {

        Vector<String> association_vec = new Vector<String>();
        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();

            // Will handle secured ontologies later.
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(version);
            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, versionOrTag);
            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            String[] ids = hierarchies[0].getAssociationIds();

            for (int i = 0; i < ids.length; i++) {
                if (!association_vec.contains(ids[i])) {
                    association_vec.add(ids[i]);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return association_vec;
    }

    public static String getVocabularyVersionByTag(String codingSchemeName,
            String ltag) {
        if (codingSchemeName == null)
            return null;
        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList lcsrl = lbSvc.getSupportedCodingSchemes();
            CodingSchemeRendering[] csra = lcsrl.getCodingSchemeRendering();
            for (int i = 0; i < csra.length; i++) {
                CodingSchemeRendering csr = csra[i];
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                if (css.getFormalName().compareTo(codingSchemeName) == 0
                        || css.getLocalName().compareTo(codingSchemeName) == 0) {
                    if (ltag == null)
                        return css.getRepresentsVersion();
                    RenderingDetail rd = csr.getRenderingDetail();
                    CodingSchemeTagList cstl = rd.getVersionTags();
                    java.lang.String[] tags = cstl.getTag();
                    for (int j = 0; j < tags.length; j++) {
                        String version_tag = (String) tags[j];
                        if (version_tag.compareToIgnoreCase(ltag) == 0) {
                            return css.getRepresentsVersion();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Version corresponding to tag " + ltag
                + " is not found " + " in " + codingSchemeName);
        return null;
    }

    public static Vector<String> getVersionListData(String codingSchemeName) {

        Vector<String> v = new Vector<String>();
        try {
            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
            if (csrl == null)
                System.out.println("csrl is NULL");

            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i = 0; i < csrs.length; i++) {
                CodingSchemeRendering csr = csrs[i];
                Boolean isActive =
                    csr.getRenderingDetail().getVersionStatus().equals(
                            CodingSchemeVersionStatus.ACTIVE);
                if (isActive != null && isActive.equals(Boolean.TRUE)) {
                    CodingSchemeSummary css = csr.getCodingSchemeSummary();
                    String formalname = css.getFormalName();
                    if (formalname.compareTo(codingSchemeName) == 0) {
                        String representsVersion = css.getRepresentsVersion();
                        v.add(representsVersion);
                    }
                }
            }
        } catch (Exception ex) {

        }
        return v;
    }

    public static String getFileName(String pathname) {
        File file = new File(pathname);
        String filename = file.getName();
        return filename;
    }

    protected static Association processForAnonomousNodes(Association assoc) {
        // clone Association except associatedConcepts
        Association temp = new Association();
        temp.setAssociatedData(assoc.getAssociatedData());
        temp.setAssociationName(assoc.getAssociationName());
        temp.setAssociationReference(assoc.getAssociationReference());
        temp.setDirectionalName(assoc.getDirectionalName());
        temp.setAssociatedConcepts(new AssociatedConceptList());

        for (int i = 0; i < assoc.getAssociatedConcepts()
                .getAssociatedConceptCount(); i++) {
            // Conditionals to deal with anonymous nodes and UMLS top nodes
            // "V-X"
            // The first three allow UMLS traversal to top node.
            // The last two are specific to owl anonymous nodes which can act
            // like false
            // top nodes.
            if (assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getReferencedEntry() != null
                    && assoc.getAssociatedConcepts().getAssociatedConcept(i)
                            .getReferencedEntry().getIsAnonymous() != null
                    && assoc.getAssociatedConcepts().getAssociatedConcept(i)
                            .getReferencedEntry().getIsAnonymous() != false
                    && !assoc.getAssociatedConcepts().getAssociatedConcept(i)
                            .getConceptCode().equals("@")
                    && !assoc.getAssociatedConcepts().getAssociatedConcept(i)
                            .getConceptCode().equals("@@")) {
                // do nothing
            } else {
                temp.getAssociatedConcepts().addAssociatedConcept(
                        assoc.getAssociatedConcepts().getAssociatedConcept(i));
            }
        }
        return temp;
    }

    // /////////////////////////////////////////////////////////////////////////

    public static LocalNameList vector2LocalNameList(Vector<String> v) {
        if (v == null)
            return null;
        LocalNameList list = new LocalNameList();
        for (int i = 0; i < v.size(); i++) {
            String vEntry = (String) v.elementAt(i);
            list.addEntry(vEntry);
        }
        return list;
    }

    protected static NameAndValueList createNameAndValueList(
            Vector<String> names, Vector<String> values) {
        if (names == null)
            return null;
        NameAndValueList nvList = new NameAndValueList();
        for (int i = 0; i < names.size(); i++) {
            String name = names.elementAt(i);
            String value = values.elementAt(i);
            NameAndValue nv = new NameAndValue();
            nv.setName(name);
            if (value != null) {
                nv.setContent(value);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }

    public static Vector<org.LexGrid.concepts.Concept> restrictToMatchingProperty(
            String codingSchemeName, String version,
            Vector<String> property_vec, Vector<String> source_vec,
            Vector<String> qualifier_name_vec,
            Vector<String> qualifier_value_vec, java.lang.String matchText,
            java.lang.String matchAlgorithm, java.lang.String language,
            int maxToReturn) {

        LocalNameList propertyList = vector2LocalNameList(property_vec);
        CodedNodeSet.PropertyType[] propertyTypes = null;
        LocalNameList sourceList = vector2LocalNameList(source_vec);

        NameAndValueList qualifierList =
            createNameAndValueList(qualifier_name_vec, qualifier_value_vec);

        return restrictToMatchingProperty(codingSchemeName, version,
                propertyList, propertyTypes, sourceList, qualifierList,
                matchText, matchAlgorithm, language, maxToReturn);

    }

    public static Vector<org.LexGrid.concepts.Concept> restrictToProperty(
            String codingSchemeName, String version,
            Vector<String> property_vec, Vector<String> source_vec,
            Vector<String> qualifier_name_vec,
            Vector<String> qualifier_value_vec, int maxToReturn) {

        LocalNameList propertyList = vector2LocalNameList(property_vec);
        CodedNodeSet.PropertyType[] propertyTypes = null;
        LocalNameList sourceList = vector2LocalNameList(source_vec);
        NameAndValueList qualifierList =
            createNameAndValueList(qualifier_name_vec, qualifier_value_vec);
        return restrictToProperty(codingSchemeName, version, propertyList,
                propertyTypes, sourceList, qualifierList, maxToReturn);
    }

    public static Vector<org.LexGrid.concepts.Concept> restrictToProperty(
            String codingSchemeName, String version,

            LocalNameList propertyList,
            CodedNodeSet.PropertyType[] propertyTypes,
            LocalNameList sourceList, NameAndValueList qualifierList,
            int maxToReturn)

    // throws LBParameterException
    {
        CodedNodeSet cns = null;
        Vector<org.LexGrid.concepts.Concept> v =
            new Vector<org.LexGrid.concepts.Concept>();
        try {
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(version);

            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();

            cns = lbSvc.getCodingSchemeConcepts(codingSchemeName, versionOrTag);
            if (cns == null)
                return v;

            LocalNameList contextList = null;
            cns =
                cns.restrictToProperties(propertyList, propertyTypes,
                        sourceList, contextList, qualifierList);

            LocalNameList restrictToProperties = new LocalNameList();
            SortOptionList sortCriteria =
                Constructors.createSortOptionList(new String[] {
                        "matchToQuery", "code" });

            ResolvedConceptReferenceList list = null;
            try {
                list =
                    cns.resolveToList(sortCriteria, restrictToProperties, null,
                            maxToReturn);
            } catch (Exception ex) {
                throw new LBParameterException(ex.getMessage());
            }

            if (list == null)
                return v;
            ResolvedConceptReference[] rcrArray =
                list.getResolvedConceptReference();
            if (rcrArray == null) {
                System.out
                        .println("WARNING: DLBWrapper getResolvedConceptReference returns null");
            }

            for (int i = 0; i < rcrArray.length; i++) {
                ResolvedConceptReference rcr =
                    (ResolvedConceptReference) rcrArray[i];
                v.add(rcr.getReferencedEntry());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return v;
        }
        return v;
    }

    public static Vector<org.LexGrid.concepts.Concept> restrictToMatchingProperty(
            String codingSchemeName, String version,

            LocalNameList propertyList,
            CodedNodeSet.PropertyType[] propertyTypes,
            LocalNameList sourceList, NameAndValueList qualifierList,

            java.lang.String matchText, java.lang.String matchAlgorithm,
            java.lang.String language,

            int maxToReturn)

    // throws LBParameterException
    {
        CodedNodeSet cns = null;
        Vector<org.LexGrid.concepts.Concept> v =
            new Vector<org.LexGrid.concepts.Concept>();
        try {
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(version);

            EVSApplicationService lbSvc =
                RemoteServerUtil.createLexBIGService();

            if (lbSvc == null) {
                System.out.println("ERROR: lbSvc == null???");
                return null;
            }

            cns = lbSvc.getCodingSchemeConcepts(codingSchemeName, versionOrTag);
            if (cns == null)
                return v;

            LocalNameList contextList = null;
            cns =
                cns.restrictToMatchingProperties(propertyList, propertyTypes,
                        sourceList, contextList, qualifierList, matchText,
                        matchAlgorithm, language);

            LocalNameList restrictToProperties = new LocalNameList();
            SortOptionList sortCriteria =
            // Constructors.createSortOptionList(new String[]{"matchToQuery",
                // "code"});
                Constructors
                        .createSortOptionList(new String[] { "matchToQuery" });

            ResolvedConceptReferenceList list = null;
            try {
                list =
                    cns.resolveToList(sortCriteria, restrictToProperties, null,
                            maxToReturn);
            } catch (Exception ex) {
                throw new LBParameterException(ex.getMessage());
            }

            if (list == null)
                return v;
            ResolvedConceptReference[] rcrArray =
                list.getResolvedConceptReference();
            if (rcrArray == null) {
                System.out
                        .println("WARNING: getResolvedConceptReference returns null");
            }

            for (int i = 0; i < rcrArray.length; i++) {
                ResolvedConceptReference rcr =
                    (ResolvedConceptReference) rcrArray[i];
                v.add(rcr.getReferencedEntry());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return v;
        }
        return SortUtils.quickSort(v);
    }
}
