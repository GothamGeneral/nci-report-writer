package gov.nih.nci.evs.reportwriter.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate;
import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.utils.RemoteServerUtil;
import gov.nih.nci.evs.reportwriter.utils.SDKClientUtil;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.EVSApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Concept;
import org.LexGrid.concepts.ConceptProperty;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Instruction;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.relations.Relations;
import org.apache.log4j.Logger;

/**
  * <!-- LICENSE_TEXT_START -->
* Copyright 2008,2009 NGIT. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by NGIT and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "NGIT" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or NGIT
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  * <!-- LICENSE_TEXT_END -->
  */

/**
  * @author EVS Team
  * @version 1.0
  *
  * Modification history
  *     Initial implementation kim.ong@ngc.com
  *
 */

public class StandardReportService {

    private EVSApplicationService  appService = null;
    private LexBIGService lbSvc;

    private String serviceUrl = null;

	private String codingScheme = null;
	private String version = null;

	private PrintWriter writer = null;

	/**
	* Default constructor.
	*/
	public StandardReportService() {
		try {
			this.lbSvc = RemoteServerUtil.createLexBIGService();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public StandardReportService(String url) {
		try {
			Boolean retval = connect(url);
			if (retval == Boolean.TRUE)
			{
				this.lbSvc = RemoteServerUtil.createLexBIGService(url);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public LexBIGService getLexBIGService()
    {
		return this.lbSvc;
	}

	private Boolean connect(String serviceUrl)
	{
		if (serviceUrl == null || serviceUrl.compareTo("") == 0)
		{
			try {
				this.lbSvc = new LexBIGServiceImpl();
            return Boolean.TRUE;
		    } catch (Exception ex) {
				ex.printStackTrace();
				return Boolean.FALSE;
			}
		}
		try{
			System.out.println("URL: " + serviceUrl);
            this.serviceUrl = serviceUrl;
			this.appService = (EVSApplicationService) ApplicationServiceProvider.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");

            this.lbSvc = (LexBIGService) this.appService;

            return Boolean.TRUE;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	public PrintWriter openPrintWriter(String outputfile)
	{
		try {
			PrintWriter pw = new PrintWriter (new BufferedWriter(new FileWriter(outputfile)));
            return pw;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
    }

	public void closePrintWriter(PrintWriter pw)
	{
		if (pw == null)
		{
			System.out.println("WARNING: pw is not open.");
			return;
		}
		pw.close();
		pw = null;
	}


    public Boolean generateStandardReport(String outputDir, String standardReportLabel, String uid)
    {
        Thread reportgeneration_thread = new Thread(new ReportGenerationThread(outputDir, standardReportLabel, uid));
        reportgeneration_thread.start();

        // to be modified
        return Boolean.TRUE;
	}


	public String validReport(
          String standardReportTemplate_value,
		  String reportFormat_value,
		  String reportStatus_value,
		  String user_value) {

        try{
			    Boolean bool_obj = initializeReportFormats();
			    bool_obj = initializeReportStatus();

			    if (standardReportTemplate_value == null) return "Report template not specified.";
			    if (reportFormat_value == null) return "Report format not specified.";
			    if (reportStatus_value == null) return "Report status not specified.";
			    if (user_value == null) return "User authentication failed.";

        	    SDKClientUtil sdkclientutil = new SDKClientUtil();

				String FQName = null;
				String methodName = null;
				String key = null;

				StandardReportTemplate standardReportTemplate = null;
				FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
				methodName = "setLabel";
				key = standardReportTemplate_value;
				Object standardReportTemplate_obj = sdkclientutil.search(FQName, methodName, key);
				if (standardReportTemplate_obj == null) {
					System.out.println("Object " + standardReportTemplate_value + " not found.");
					return "Unidentifiable report template label -- " + standardReportTemplate_value;
				}

				ReportFormat reportFormat = null;
				FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
				methodName = "setDescription";
				key = reportFormat_value;
				Object reportFormat_obj = sdkclientutil.search(FQName, methodName, key);
				if (reportFormat_obj == null) {
					System.out.println("Object " + reportFormat_value + " not found.");
					return "Unidentifiable report format -- " + reportFormat_value;
				}

				ReportStatus reportStatus = null;
				FQName = "gov.nih.nci.evs.reportwriter.bean.ReportStatus";
				methodName = "setLabel";
				key = reportStatus_value;
				Object reportStatus_obj = sdkclientutil.search(FQName, methodName, key);
				if (reportStatus_obj == null) {
					System.out.println("Object " + reportStatus_value + " not found.");
					return "Unidentifiable report status -- " + reportStatus_value;
				}

				User user = null;
				FQName = "gov.nih.nci.evs.reportwriter.bean.User";
				methodName = "setLoginName";
				key = user_value;
				Object user_obj = sdkclientutil.search(FQName, methodName, key);
				if (user_obj == null) {
					System.out.println("Object " + user_value + " not found.");
					return "Unidentifiable user -- " + user_value;
				}

				return "success";

          } catch(Exception e) {
        	  e.printStackTrace();
          }

          return "Report validation falied";

	}


	public Boolean createStandardReport(
		String label,
		String pathName,

        String templateLabel,
		String format,
		String status,
		String uid) {

		// Method called upon generation of two reports (tab-delimited and Excel)
        try{

System.out.println("Validing report ");

			  String msg = validReport(templateLabel, format, status, uid);
			  if (msg.compareTo("success") != 0)
			  {
				  System.out.println("Report object not created.");
				  return Boolean.FALSE;
			  }

        	  SDKClientUtil sdkclientutil = new SDKClientUtil();
        	  String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
        	  String methodName = "setLabel";
        	  String key = label;
        	  Object[] objs = sdkclientutil.search(FQName);


System.out.println("Deleting old report objects from database. ");

        	  if (objs != null)
        	  {
				  // report already exists, delete it
				  for (int i=0; i<objs.length; i++)
				  {
                  	  StandardReport report = (StandardReport) objs[i];
                  	  String reportlabel = report.getLabel();
                  	  if (label.compareTo(reportlabel) == 0)
                  	  {
						  sdkclientutil.deleteStandardReport(report);
					  }
				  }
 			  }

System.out.println("Creating StandardReport object. ");

              java.util.Date lastModified = new Date(); // system date
        	  StandardReport report = sdkclientutil.createStandardReport(label, lastModified, pathName);

			  StandardReportTemplate standardReportTemplate = null;
        	  FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
        	  methodName = "setLabel";

System.out.println("Report template: " + label);
        	  key = templateLabel;

              Object template_obj = sdkclientutil.search(FQName, methodName, key);
              if (template_obj != null)
              {
				  report.setTemplate((StandardReportTemplate) template_obj);
			  }
			  else
			  {
System.out.println("WARNING: Report template: " + label + " not found???");
                  return Boolean.FALSE;
			  }

System.out.println("Assigning report format. ");

			  ReportFormat reportformat = null;
        	  FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
        	  methodName = "setDescription";
        	  key = format;

              Object format_obj = sdkclientutil.search(FQName, methodName, key);
              if (format_obj != null)
              {
				  report.setFormat((ReportFormat) format_obj);
			  }
			  else
			  {
				  System.out.println("WARNING: Format " + format + " not found -- report not created.");
				  return Boolean.FALSE;
			  }

System.out.println("Assigning report status. ");

			  ReportStatus reportstatus = null;
        	  FQName = "gov.nih.nci.evs.reportwriter.bean.ReportStatus";
        	  methodName = "setLabel";
        	  key = status;

              Object status_obj = sdkclientutil.search(FQName, methodName, key);
              if (status_obj != null)
              {
				  report.setStatus((ReportStatus) status_obj);
			  }

System.out.println("Assigning user. ");

			  gov.nih.nci.evs.reportwriter.bean.User user = null;
        	  FQName = "gov.nih.nci.evs.reportwriter.bean.User";
        	  methodName = "setLoginName";
        	  key = uid;

              Object user_obj = sdkclientutil.search(FQName, methodName, key);
              if (user_obj != null)
              {
				  report.setCreatedBy((gov.nih.nci.evs.reportwriter.bean.User) user_obj);
			  }

System.out.println("Writing record to database. ");

              sdkclientutil.insertStandardReport(report);

          } catch(Exception e) {
        	  e.printStackTrace();
        	  return Boolean.FALSE;
          }

          return Boolean.TRUE;
	}

	public Boolean initializeReportStatus() {
        try{
            SDKClientUtil util = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportStatus";
            Object[] objs = util.search(FQName);

            if (objs == null || objs.length == 0) {

				// Initial status (DRAFT and APPROVED)
				String label = "DRAFT";
				String description = "Report is a draft, not ready for download.";
				boolean active = true;
				try {
					util.insertReportStatus(label, description, active);
				} catch (Exception ex) {
 					System.out.println("====== insertReportStatus DRAFT failed." );
				}

				label = "APPROVED";
				description = "Report has been approved for download by users";
				active = true;
				try {
					util.insertReportStatus(label, description, active);
				} catch (Exception ex) {
					System.out.println("====== insertReportStatus APPROVED failed." );
				}
			}
        } catch(Exception e) {
        	e.printStackTrace();
        	return Boolean.FALSE;
        }
		return Boolean.TRUE;
	}


    public Boolean initializeReportFormats() {
        try{
            SDKClientUtil util = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
            Object[] objs = util.search(FQName);

            if (objs == null || objs.length == 0) {
				String description = "Text (tab delimited)";
				try {
					util.insertReportFormat(description);
				} catch (Exception ex) {
 					System.out.println("====== insertReportFormat " + description + " failed." );
				}

				description = "Microsoft Office Excel";
				try {
					util.insertReportFormat(description);
				} catch (Exception ex) {
					System.out.println("====== insertReportFormat " + description + " failed." );
				}

			}
        } catch(Exception e) {
        	e.printStackTrace();
        	return Boolean.FALSE;
        }
		return Boolean.TRUE;
	}



    public static void main(String[] args) {

	   try {
		   StandardReportService standardReportService = new StandardReportService();
		   String standardReportLabel = "FDA-UNII Subset Report";
		   String outputfile = standardReportLabel;
		   outputfile.replaceAll(" ", "_");
		   String outputDir = "G:\\ReportWriter\\test";
		   Boolean retval = standardReportService.generateStandardReport(outputDir, standardReportLabel, "kimong");

	   } catch (Exception e) {
		   System.out.println("REQUEST FAILED !!!");
	   }
   }
}
