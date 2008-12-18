package gov.nih.nci.evs.reportwriter.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

/**
  * <!-- LICENSE_TEXT_START -->
* Copyright 2007 NGIT. This software was developed in conjunction with the National Cancer Institute,
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
 */

public class DataUtils {

	Connection con;
	Statement stmt;
	ResultSet rs;

	private List supportedStandardReportTemplateList = new ArrayList();
	private List supportedStandardReportList = new ArrayList();

	private static List standardReportTemplateList = null;
	private static List adminTaskList = null;
    private static List userTaskList = null;

    public DataUtils()
    {
		adminTaskList = new ArrayList();
		adminTaskList.add(new SelectItem("Administer Standard Reports", "Administer Standard Reports"));

		userTaskList = new ArrayList();
		userTaskList.add(new SelectItem("Retrieve Standard Reports", "Retrieve Standard Reports"));

		standardReportTemplateList = new ArrayList();
	}

	public static List getTaskList(Boolean isAdmin) {

		if (isAdmin == null) return null;
		else if (isAdmin.equals(Boolean.TRUE))
		{
			if (adminTaskList == null)
			{
				adminTaskList = new ArrayList();
				adminTaskList.add(new SelectItem("Administer Standard Reports"));
			}
		    return adminTaskList;
		}
		else
		{
			if (userTaskList == null)
			{
				userTaskList = new ArrayList();
				userTaskList.add(new SelectItem("Retrieve Standard Reports"));
			}
		    return userTaskList;
		}
	}

	public static List getStandardReportTemplateList(Boolean isAdmin) {

		if (isAdmin == null || isAdmin == Boolean.FALSE) return null;

		/*
		if (standardReportTemplateList == null) {
				standardReportTemplateList = new ArrayList();
				// to be implemented
				// Retrieve report labels from database through DAO
				//standardReportTemplateList.add(new SelectItem("Select a Standard Report Template or Click Add to Create new template"));
		}
		*/
        /*
		if (standardReportTemplateList.size() == 0)
		{
			standardReportTemplateList.add(new SelectItem("No report template is available.");
		}
		*/
        standardReportTemplateList = new ArrayList();
	    return standardReportTemplateList;
	}


	public List getSupportedStandardReports() {
		return supportedStandardReportList;
	}


}
