/*L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
L*/

-- Create Report Writer database
--
-- ------------------------------------------------------

--
-- Insert reportwriter data
--

USE @database.name@;

--
-- Data for table `USER`
--

INSERT INTO `USER` (`ID`,`LOGIN_NAME`) VALUES
 (101,'@rw.admin.user.name@'),
 (102,'@rw.user.name@');

--
-- Data for table `hi_value`
-- Note: Need to set hi_value whenever you manually add rows to a table.
--

INSERT INTO `hi_value` (`next_value`) VALUES
 (82);

--
-- Data for table `REPORT_FORMAT`
--

INSERT INTO `REPORT_FORMAT` (`ID`,`DESCRIPTION`) VALUES
 (404,'Text (tab delimited)'),
 (405,'Microsoft Office Excel'),
 (406,'HyperText Markup Language');

--
-- Data for table `REPORT_STATUS`
--

INSERT INTO `REPORT_STATUS` (`ID`,`LABEL`,`DESCRIPTION`,`ACTIVE`) VALUES
 (505,'DRAFT','REPORT is a draft, not ready for download.',1),
 (506,'APPROVED','REPORT has been approved for download by USERs',1);

--
-- Data for table `STANDARD_REPORT_TEMPLATE`
--

INSERT INTO `STANDARD_REPORT_TEMPLATE` (`ID`,`LABEL`,`ROOT_CONCEPT_CODE`,`ASSOCIATION_NAME`,`DIRECTION`,`CODING_SCHEME_NAME`,`CODING_SCHEME_VERSION`,`LEVEL`,`DELIMITER`) VALUES
 (202,'FDA-UNII Subset REPORT','C63923','Concept_In_Subset',0,'NCI Thesaurus','10.11e',-1,'$'),
 (2323,'Individual Case Safety (ICS) Subset REPORT','C54447','Concept_In_Subset',0,'NCI Thesaurus','10.11e',-1,'$'),
 (3535,'Structured Product Labeling (SPL) REPORT','C54452','Concept_In_Subset',0,'NCI Thesaurus','10.11e',-1,'$'),
 (4040,'CDISC Subset REPORT ','C61410','Concept_In_Subset',0,'NCI Thesaurus','10.11e',-1,'$'),
 (4646,'CDRH Subset REPORT','C62596','Concept_In_Subset',0,'NCI Thesaurus','10.11e',-1,'$'),
 (6060,'FDA-SPL Country Code REPORT','Semantic_Type|null|null|null|Geographic Area|exactMatch','',0,'NCI Thesaurus','10.11e',-1,'$'),
 (6868,'CDISC SDTM Terminology (Orig)','C61410','Concept_In_Subset',0,'NCI Thesaurus','10.11e',-1,'$'),
 (7272,'NICHD Subset Report','C90259','Concept_In_Subset',0,'NCI Thesaurus','10.11e',-1,'$'),
 (7474,'CDISC ADaM Terminology','C81222','Concept_In_Subset',0,'NCI Thesaurus','10.11e',-1,'$'),
 (7676,'CDISC SEND Terminology','C77526','Concept_In_Subset',0,'NCI Thesaurus','10.11e',-1,'$'),
 (7878,'CDISC CDASH Terminology','C77527','Concept_In_Subset',0,'NCI Thesaurus','10.11e',-1,'$'),
 (8080,'CDISC SDTM Terminology','C66830','Concept_In_Subset',0,'NCI Thesaurus','10.11e',-1,'$');


--
-- Data for table `REPORT_COLUMN`
--

INSERT INTO `REPORT_COLUMN` (`ID`,`COLUMN_NUMBER`,`LABEL`,`FIELD_ID`,`PROPERTY_TYPE`,`PROPERTY_NAME`,`IS_PREFERRED`,`REPRESENTATIONAL_FORM`,`SOURCE`,`QUALIFIER_NAME`,`QUALIFIER_VALUE`,`DELIMITER`,`CONDITIONAL_COLUMN`,`BELONGS_TO`) VALUES
 (303,1,'FDA UNII Code (use for SPL)','Property','Generic','FDA_UNII_Code',NULL,'',' ','','','|',-1,202),
 (304,2,'FDA Preferred Term','Property','Presentation','FULL_SYN',NULL,'PT','FDA','','','|',-1,202),
 (1212,3,'NCI Concept Code','Code','','',NULL,'',' ','','','|',-1,202),
 (2929,1,'Source','Property','GENERIC','Contributing_Source',NULL,'',' ','','','|',-1,2323),
 (2930,2,'Subset Code','Associated Concept Code','','',NULL,'',' ','','','|',-1,2323),
 (2931,3,'Subset Name','Associated Concept Property','PRESENTATION','Preferred_Name',NULL,'',' ','','','|',-1,2323),
 (3131,4,'Concept Code','Code','','',NULL,'',' ','','','|',-1,2323),
 (3132,5,'Source PT','Property','PRESENTATION','FULL_SYN',NULL,'PT','FDA','','','|',-1,2323),
 (3333,6,'Source Definition','Property','DEFINITION','ALT_DEFINITION',NULL,'','FDA','','','|',-1,2323),
 (3334,6,'Source Synonym(s)','Property','PRESENTATION','FULL_SYN',NULL,'SY','FDA','','','|',-1,2323),
 (3636,1,'Source','Property','GENERIC','Contributing_Source',NULL,'',' ','','','|',-1,3535),
 (3637,2,'Subset Code','Associated Concept Code','','',NULL,'',' ','','','|',-1,3535),
 (3638,3,'Subset Name','Associated Concept Property','PRESENTATION','Preferred_Name',NULL,'',' ','','','|',-1,3535),
 (3639,4,'Concept Code','Code','','',NULL,'',' ','','','|',-1,3535),
 (3640,5,'Source PT','Property','PRESENTATION','FULL_SYN',NULL,'PT','FDA','','','|',-1,3535),
 (3641,6,'Source Definition','Property','DEFINITION','ALT_DEFINITION',NULL,'','FDA','','','|',-1,3535),
 (3642,7,'Source Synonym(s)','Property','PRESENTATION','FULL_SYN',NULL,'SY','FDA','','','|',-1,3535),
 (4141,1,'Source','Property','GENERIC','Contributing_Source',NULL,'',' ','','','|',-1,4040),
 (4142,2,'Subset Code','Associated Concept Code','','',NULL,'',' ','','','|',-1,4040),
 (4143,3,'Subset Name','Associated Concept Property','PRESENTATION','Preferred_Name',NULL,'',' ','','','|',-1,4040),
 (4144,4,'Concept Code','Code','','',NULL,'',' ','','','|',-1,4040),
 (4145,5,'Source PT','Property','PRESENTATION','FULL_SYN',NULL,'PT','CDISC','','','|',-1,4040),
 (4146,6,'Source PT Code','Property','PRESENTATION','Preferred_Name',NULL,'','','','','|',-1,4040),
 (4147,7,'Source Definition','Property','DEFINITION','ALT_DEFINITION',NULL,'','CDISC','','','|',-1,4040),
 (4148,8,'Source Synonym(s)','Property','PRESENTATION','FULL_SYN',NULL,'SY','CDISC','','','|',-1,4040),
 (4747,1,'Source','Property','GENERIC','Contributing_Source',NULL,'',' ','','','|',-1,4646),
 (4748,2,'NCIt Subset Code','Associated Concept Code','','',NULL,'',' ','','','|',-1,4646),
 (4749,3,'FDA Subset Name','Associated Concept Property','PRESENTATION','Preferred_Name',NULL,'',' ','','','|',-1,4646),
 (4750,4,'NCIt Concept Code','Code','','',NULL,'',' ','','','|',-1,4646),
 (4848,5,'FDA Source Code','Property Qualifier','PRESENTATION','FULL_SYN',NULL,'PT','FDA','source-code','CDRH','|',-1,4646),
 (4849,6,'FDA Source PT','Property','PRESENTATION','FULL_SYN',NULL,'PT','FDA','subsource-name','CDRH','|',-1,4646),
 (4850,7,'FDA Source Synonym(s)','Property','PRESENTATION','FULL_SYN',NULL,'SY','FDA','subsource-name','CDRH','|',-1,4646),
 (4851,8,'FDA Source Definition','Property','DEFINITION','ALT_DEFINITION',NULL,'','FDA','','','|',-1,4646),
 (4852,9,'NCIt Definition','Property','DEFINITION','DEFINITION',NULL,'',' ','','','|',-1,4646),
 (4853,10,'Parent Concept''s NCIt Concept Code','1st Associated Concept Code [ assocName = Has_CDRH_Parent ]','','',NULL,'','','','','|',-1,4646),
 (4854,11,'Parent Concept''s FDA Source Code','1st Associated Concept Property Qualifier [ assocName = Has_CDRH_Parent ]','PRESENTATION','FULL_SYN',NULL,'PT','FDA','source-code','CDRH','|',-1,4646),
 (4855,12,'Parent Concept''s FDA Source PT','1st Associated Concept Property [ assocName = Has_CDRH_Parent ]','PRESENTATION','FULL_SYN',NULL,'PT','FDA','','','|',-1,4646),
 (6161,1,'ISO Code','Property','PRESENTATION','FULL_SYN',NULL,'CA3','NCI','','','|',-1,6060),
 (6162,2,'NCI Concept Code','Code','','',NULL,'',' ','','','|',-1,6060),
 (6163,3,'NCI Preferred Term','Property','PRESENTATION','Preferred_Name',NULL,'',' ','','','|',-1,6060),
 (6969,1,'Code','Code','','',NULL,'','','','','|',-1,6868),
 (6970,2,'Codelist Code','Associated Concept Code','','',NULL,'','','','','|',-1,6868),
 (6971,3,'Codelist Extensible (Yes/No)','Associated Concept Property','GENERIC','Extensible_List',NULL,'','','','','|',-1,6868),
 (6972,4,'Codelist Name','Associated Concept Property','PRESENTATION','FULL_SYN',NULL,'SY','CDISC','','','|',-1,6868),
 (6973,5,'CDISC Submission Value','Property','PRESENTATION','FULL_SYN',NULL,'PT','CDISC','','','|',-1,6868),
 (6974,6,'CDISC Preferred Term','Property','PRESENTATION','FULL_SYN',NULL,'PT','CDISC','','','|',-1,6868),
 (6975,7,'CDISC Symonym(s)','Property','PRESENTATION','FULL_SYN',NULL,'SY','CDISC','','','|',-1,6868),
 (6976,8,'CDISC Definition','Property','DEFINITION','ALT_DEFINITION',NULL,'','CDISC','','','|',-1,6868),
 (6977,9,'NCI Preferred Term','Property','PRESENTATION','FULL_SYN',NULL,'PT','NCI','','','|',-1,6868),
 (7373,1,'NCIt Code','Code','','',NULL,'','','','','|',-1,7272),
 (7374,2,'NCIt PT','Property','PRESENTATION','FULL_SYN',NULL,'PT','NCI','','','|',-1,7272),
 (7375,3,'NICHD PT','Property','PRESENTATION','FULL_SYN',NULL,'PT','NICHD','','','|',-1,7272),
 (7376,4,'NICHD SY','Property','PRESENTATION','FULL_SYN',NULL,'SY','NICHD','','','|',-1,7272),
 (7377,5,'NCIt Definition','Property','DEFINITION','DEFINITION',NULL,'','','','','|',-1,7272),
 (7378,6,'NCIt Code of NICHD Parent','1st Associated Concept Code [ assocName = Has_NICHD_Parent ]','','',NULL,'','','','','|',-1,7272),
 (7379,7,'NICHD Parent PT','1st Associated Concept Property [ assocName = Has_NICHD_Parent ]','PRESENTATION','FULL_SYN',NULL,'PT','NICHD','','','|',-1,7272),
 (7380,8,'NCIt Code of second NICHD Parent','2nd Associated Concept Code [ assocName = Has_NICHD_Parent ]','','',NULL,'','','','','|',-1,7272),
 (7381,9,'NICHD second Parent PT','2nd Associated Concept Property [ assocName = Has_NICHD_Parent ]','PRESENTATION','FULL_SYN',NULL,'PT','NICHD','','','|',-1,7272),
 (7382,10,'NCIt Code of NICHD Subset','Associated Concept Code','','',NULL,'','','','','|',-1,7272),
 (7383,11,'NICHD PT of NICHD Subset','Associated Concept Property','PRESENTATION','FULL_SYN',NULL,'PT','NICHD','','','|',-1,7272),
 (7501,1,'Code','Code','','',NULL,'','','','','|',-1,7474),
 (7502,2,'Codelist Code','Associated Concept Code','','',NULL,'','','','','|',-1,7474),
 (7503,3,'Codelist Extensible (Yes/No)','Associated Concept Property','GENERIC','Extensible_List',NULL,'','','','','|',-1,7474),
 (7504,4,'Codelist Name','Associated Concept Property','PRESENTATION','FULL_SYN',NULL,'SY','CDISC','','','|',-1,7474),
 (7505,5,'CDISC Submission Value','Property','PRESENTATION','FULL_SYN',NULL,'PT','CDISC','','','|',-1,7474),
 (7506,6,'CDISC Symonym(s)','Property','PRESENTATION','FULL_SYN',NULL,'SY','CDISC','','','|',-1,7474),
 (7507,7,'CDISC Definition','Property','DEFINITION','ALT_DEFINITION',NULL,'','CDISC','','','|',-1,7474),
 (7508,8,'NCI Preferred Term','Property','PRESENTATION','FULL_SYN',NULL,'PT','NCI','','','|',-1,7474),
 (7701,1,'Code','Code','','',NULL,'','','','','|',-1,7676),
 (7702,2,'Codelist Code','Associated Concept Code','','',NULL,'','','','','|',-1,7676),
 (7703,3,'Codelist Extensible (Yes/No)','Associated Concept Property','GENERIC','Extensible_List',NULL,'','','','','|',-1,7676),
 (7704,4,'Codelist Name','Associated Concept Property','PRESENTATION','FULL_SYN',NULL,'SY','CDISC','','','|',-1,7676),
 (7705,5,'CDISC Submission Value','Property','PRESENTATION','FULL_SYN',NULL,'PT','CDISC','','','|',-1,7676),
 (7706,6,'CDISC Symonym(s)','Property','PRESENTATION','FULL_SYN',NULL,'SY','CDISC','','','|',-1,7676),
 (7707,7,'CDISC Definition','Property','DEFINITION','ALT_DEFINITION',NULL,'','CDISC','','','|',-1,7676),
 (7708,8,'NCI Preferred Term','Property','PRESENTATION','FULL_SYN',NULL,'PT','NCI','','','|',-1,7676),
 (7901,1,'Code','Code','','',NULL,'','','','','|',-1,7878),
 (7902,2,'Codelist Code','Associated Concept Code','','',NULL,'','','','','|',-1,7878),
 (7903,3,'Codelist Extensible (Yes/No)','Associated Concept Property','GENERIC','Extensible_List',NULL,'','','','','|',-1,7878),
 (7904,4,'Codelist Name','Associated Concept Property','PRESENTATION','FULL_SYN',NULL,'SY','CDISC','','','|',-1,7878),
 (7905,5,'CDISC Submission Value','Property','PRESENTATION','FULL_SYN',NULL,'PT','CDISC','','','|',-1,7878),
 (7906,6,'CDISC Symonym(s)','Property','PRESENTATION','FULL_SYN',NULL,'SY','CDISC','','','|',-1,7878),
 (7907,7,'CDISC Definition','Property','DEFINITION','ALT_DEFINITION',NULL,'','CDISC','','','|',-1,7878),
 (7908,8,'NCI Preferred Term','Property','PRESENTATION','FULL_SYN',NULL,'PT','NCI','','','|',-1,7878),
 (8101,1,'Code','Code','','',NULL,'','','','','|',-1,8080),
 (8102,2,'Codelist Code','Associated Concept Code','','',NULL,'','','','','|',-1,8080),
 (8103,3,'Codelist Extensible (Yes/No)','Associated Concept Property','GENERIC','Extensible_List',NULL,'','','','','|',-1,8080),
 (8104,4,'Codelist Name','Associated Concept Property','PRESENTATION','FULL_SYN',NULL,'SY','CDISC','','','|',-1,8080),
 (8105,5,'CDISC Submission Value','Property','PRESENTATION','FULL_SYN',NULL,'PT','CDISC','','','|',-1,8080),
 (8106,6,'CDISC Symonym(s)','Property','PRESENTATION','FULL_SYN',NULL,'SY','CDISC','','','|',-1,8080),
 (8107,7,'CDISC Definition','Property','DEFINITION','ALT_DEFINITION',NULL,'','CDISC','','','|',-1,8080),
 (8108,8,'NCI Preferred Term','Property','PRESENTATION','FULL_SYN',NULL,'PT','NCI','','','|',-1,8080);

--
-- Data for table `REPORT`
--

INSERT INTO `REPORT` (`ID`,`LABEL`,`LAST_MODIFIED`,`PATH_NAME`,`HAS_FORMAT`,`HAS_STATUS`,`MODIFIED_BY`,`CREATED_BY`) VALUES
 (5858,'Individual Case Safety (ICS) Subset REPORT.txt',NULL,'',404,505,NULL,101),
 (5859,'Individual Case Safety (ICS) Subset REPORT.xls',NULL,'',405,505,NULL,101),
 (5860,'Structured Product Labeling (SPL) REPORT.txt',NULL,'',404,505,NULL,101),
 (5861,'Structured Product Labeling (SPL) REPORT.xls',NULL,'',405,505,NULL,101),
 (5862,'CDISC Subset REPORT .txt',NULL,'',404,505,NULL,101),
 (5863,'CDISC Subset REPORT .xls',NULL,'',405,505,NULL,101),
 (5959,'CDRH Subset REPORT.txt',NULL,'',404,505,NULL,101),
 (5960,'CDRH Subset REPORT.xls',NULL,'',405,505,NULL,101),
 (6262,'FDA-UNII Subset REPORT.txt',NULL,'',404,505,NULL,101),
 (6263,'FDA-UNII Subset REPORT.xls',NULL,'',405,505,NULL,101),
 (6767,'FDA-SPL Country Code REPORT.txt',NULL,'',404,505,NULL,101),
 (6768,'FDA-SPL Country Code REPORT.xls',NULL,'',405,505,NULL,101);

--
-- Data for table `STANDARD_REPORT`
--

INSERT INTO `STANDARD_REPORT` (`REPORT_ID`,`BASED_ON_TEMPLATE`) VALUES
 (6262,202),
 (6263,202),
 (5858,2323),
 (5859,2323),
 (5860,3535),
 (5861,3535),
 (5862,4040),
 (5863,4040),
 (5959,4646),
 (5960,4646),
 (6767,6060),
 (6768,6060);

COMMIT;