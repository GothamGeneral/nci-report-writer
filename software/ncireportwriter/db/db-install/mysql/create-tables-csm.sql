-- Create UPT database
--
-- ------------------------------------------------------

--
-- Create schema csmupt within ncirw
--

CREATE DATABASE IF NOT EXISTS ncirw;
USE ncirw;

--
-- Definition of table `CSM_APPLICATION`
--

CREATE TABLE CSM_APPLICATION (
    APPLICATION_ID BIGINT AUTO_INCREMENT  NOT NULL,
    APPLICATION_NAME VARCHAR(255) NOT NULL,
    APPLICATION_DESCRIPTION VARCHAR(200) NOT NULL,
    DECLARATIVE_FLAG BOOL NOT NULL DEFAULT 0,
    ACTIVE_FLAG BOOL NOT NULL DEFAULT 0,
    UPDATE_DATE DATE DEFAULT '0000-00-00',
    DATABASE_URL VARCHAR(100),
    DATABASE_USER_NAME VARCHAR(100),
    DATABASE_PASSWORD VARCHAR(100),
    DATABASE_DIALECT VARCHAR(100),
    DATABASE_DRIVER VARCHAR(100),
    PRIMARY KEY(APPLICATION_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `CSM_GROUP`
--

CREATE TABLE CSM_GROUP (
    GROUP_ID BIGINT AUTO_INCREMENT  NOT NULL,
    GROUP_NAME VARCHAR(255) NOT NULL,
    GROUP_DESC VARCHAR(200),
    UPDATE_DATE DATE NOT NULL DEFAULT '0000-00-00',
    APPLICATION_ID BIGINT NOT NULL,
    PRIMARY KEY(GROUP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `CSM_PRIVILEGE`
--

CREATE TABLE CSM_PRIVILEGE (
    PRIVILEGE_ID BIGINT AUTO_INCREMENT  NOT NULL,
    PRIVILEGE_NAME VARCHAR(100) NOT NULL,
    PRIVILEGE_DESCRIPTION VARCHAR(200),
    UPDATE_DATE DATE NOT NULL DEFAULT '0000-00-00',
    PRIMARY KEY(PRIVILEGE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `CSM_FILTER_CLAUSE`
--

CREATE TABLE CSM_FILTER_CLAUSE (
    FILTER_CLAUSE_ID BIGINT AUTO_INCREMENT  NOT NULL,
    CLASS_NAME VARCHAR(100) NOT NULL,
    FILTER_CHAIN VARCHAR(2000) NOT NULL,
    TARGET_CLASS_NAME VARCHAR (100) NOT NULL,
    TARGET_CLASS_ATTRIBUTE_NAME VARCHAR (100) NOT NULL,
    TARGET_CLASS_ATTRIBUTE_TYPE VARCHAR (100) NOT NULL,
    TARGET_CLASS_ALIAS VARCHAR (100),
    TARGET_CLASS_ATTRIBUTE_ALIAS VARCHAR (100),
    GENERATED_SQL_USER VARCHAR (4000) NOT NULL,
    GENERATED_SQL_GROUP VARCHAR (4000) NOT NULL,
    APPLICATION_ID BIGINT NOT NULL,
    UPDATE_DATE DATE NOT NULL DEFAULT '0000-00-00',
    PRIMARY KEY(FILTER_CLAUSE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `CSM_PROTECTION_ELEMENT`
--

CREATE TABLE CSM_PROTECTION_ELEMENT (
    PROTECTION_ELEMENT_ID BIGINT AUTO_INCREMENT  NOT NULL,
    PROTECTION_ELEMENT_NAME VARCHAR(100) NOT NULL,
    PROTECTION_ELEMENT_DESCRIPTION VARCHAR(200),
    OBJECT_ID VARCHAR(100) NOT NULL,
    ATTRIBUTE VARCHAR(100) ,
    ATTRIBUTE_VALUE VARCHAR(100) ,
    PROTECTION_ELEMENT_TYPE VARCHAR(100),
    APPLICATION_ID BIGINT NOT NULL,
    UPDATE_DATE DATE NOT NULL DEFAULT '0000-00-00',
    PRIMARY KEY(PROTECTION_ELEMENT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `CSM_PROTECTION_GROUP`
--

CREATE TABLE CSM_PROTECTION_GROUP (
    PROTECTION_GROUP_ID BIGINT AUTO_INCREMENT  NOT NULL,
    PROTECTION_GROUP_NAME VARCHAR(100) NOT NULL,
    PROTECTION_GROUP_DESCRIPTION VARCHAR(200),
    APPLICATION_ID BIGINT NOT NULL,
    LARGE_ELEMENT_COUNT_FLAG BOOL NOT NULL,
    UPDATE_DATE DATE NOT NULL DEFAULT '0000-00-00',
    PARENT_PROTECTION_GROUP_ID BIGINT,
    PRIMARY KEY(PROTECTION_GROUP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `CSM_PG_PE`
--

CREATE TABLE CSM_PG_PE (
    PG_PE_ID BIGINT AUTO_INCREMENT  NOT NULL,
    PROTECTION_GROUP_ID BIGINT NOT NULL,
    PROTECTION_ELEMENT_ID BIGINT NOT NULL,
    UPDATE_DATE DATE DEFAULT '0000-00-00',
    PRIMARY KEY(PG_PE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `CSM_ROLE`
--

CREATE TABLE CSM_ROLE (
    ROLE_ID BIGINT AUTO_INCREMENT  NOT NULL,
    ROLE_NAME VARCHAR(100) NOT NULL,
    ROLE_DESCRIPTION VARCHAR(200),
    APPLICATION_ID BIGINT NOT NULL,
    ACTIVE_FLAG BOOL NOT NULL,
    UPDATE_DATE DATE NOT NULL DEFAULT '0000-00-00',
    PRIMARY KEY(ROLE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `CSM_ROLE_PRIVILEGE`
--

CREATE TABLE CSM_ROLE_PRIVILEGE (
    ROLE_PRIVILEGE_ID BIGINT AUTO_INCREMENT  NOT NULL,
    ROLE_ID BIGINT NOT NULL,
    PRIVILEGE_ID BIGINT NOT NULL,
    PRIMARY KEY(ROLE_PRIVILEGE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `CSM_USER`
--

CREATE TABLE CSM_USER (
    USER_ID BIGINT AUTO_INCREMENT  NOT NULL,
    LOGIN_NAME VARCHAR(500) NOT NULL,
    MIGRATED_FLAG BOOL NOT NULL DEFAULT 0,
    FIRST_NAME VARCHAR(100) NOT NULL,
    LAST_NAME VARCHAR(100) NOT NULL,
    ORGANIZATION VARCHAR(100),
    DEPARTMENT VARCHAR(100),
    TITLE VARCHAR(100),
    PHONE_NUMBER VARCHAR(15),
    PASSWORD VARCHAR(100),
    EMAIL_ID VARCHAR(100),
    START_DATE DATE,
    END_DATE DATE,
    UPDATE_DATE DATE NOT NULL DEFAULT '0000-00-00',
    PREMGRT_LOGIN_NAME VARCHAR(100) ,
    PRIMARY KEY(USER_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `CSM_USER_GROUP`
--

CREATE TABLE CSM_USER_GROUP (
    USER_GROUP_ID BIGINT AUTO_INCREMENT  NOT NULL,
    USER_ID BIGINT NOT NULL,
    GROUP_ID BIGINT NOT NULL,
    PRIMARY KEY(USER_GROUP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `CSM_USER_GROUP_ROLE_PG`
--

CREATE TABLE CSM_USER_GROUP_ROLE_PG (
    USER_GROUP_ROLE_PG_ID BIGINT AUTO_INCREMENT  NOT NULL,
    USER_ID BIGINT,
    GROUP_ID BIGINT,
    ROLE_ID BIGINT NOT NULL,
    PROTECTION_GROUP_ID BIGINT NOT NULL,
    UPDATE_DATE DATE NOT NULL DEFAULT '0000-00-00',
    PRIMARY KEY(USER_GROUP_ROLE_PG_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `CSM_USER_PE`
--

CREATE TABLE CSM_USER_PE (
    USER_PROTECTION_ELEMENT_ID BIGINT AUTO_INCREMENT  NOT NULL,
    PROTECTION_ELEMENT_ID BIGINT NOT NULL,
    USER_ID BIGINT NOT NULL,
    PRIMARY KEY(USER_PROTECTION_ELEMENT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Create constraints and index for CSM tables
--

ALTER TABLE CSM_APPLICATION
ADD CONSTRAINT UQ_APPLICATION_NAME UNIQUE (APPLICATION_NAME);
CREATE INDEX idx_APPLICATION_ID ON CSM_GROUP(APPLICATION_ID);
ALTER TABLE CSM_GROUP
ADD CONSTRAINT UQ_GROUP_GROUP_NAME UNIQUE (APPLICATION_ID, GROUP_NAME);
ALTER TABLE CSM_PRIVILEGE
ADD CONSTRAINT UQ_PRIVILEGE_NAME UNIQUE (PRIVILEGE_NAME);
CREATE INDEX idx_APPLICATION_ID ON CSM_PROTECTION_ELEMENT(APPLICATION_ID);
ALTER TABLE CSM_PROTECTION_ELEMENT
ADD CONSTRAINT UQ_PE_PE_NAME_ATTRIBUTE_VALUE_APP_ID UNIQUE (OBJECT_ID, ATTRIBUTE, ATTRIBUTE_VALUE, APPLICATION_ID);
CREATE INDEX idx_APPLICATION_ID ON CSM_PROTECTION_GROUP(APPLICATION_ID);
ALTER TABLE CSM_PROTECTION_GROUP
ADD CONSTRAINT UQ_PROTECTION_GROUP_PROTECTION_GROUP_NAME UNIQUE (APPLICATION_ID, PROTECTION_GROUP_NAME);
CREATE INDEX idx_PARENT_PROTECTION_GROUP_ID ON CSM_PROTECTION_GROUP(PARENT_PROTECTION_GROUP_ID);
CREATE INDEX idx_PROTECTION_ELEMENT_ID ON CSM_PG_PE(PROTECTION_ELEMENT_ID);
CREATE INDEX idx_PROTECTION_GROUP_ID ON CSM_PG_PE(PROTECTION_GROUP_ID);
ALTER TABLE CSM_PG_PE
ADD CONSTRAINT UQ_PROTECTION_GROUP_PROTECTION_ELEMENT_PROTECTION_GROUP_ID UNIQUE (PROTECTION_ELEMENT_ID, PROTECTION_GROUP_ID);
CREATE INDEX idx_APPLICATION_ID ON CSM_ROLE(APPLICATION_ID);
ALTER TABLE CSM_ROLE
ADD CONSTRAINT UQ_ROLE_ROLE_NAME UNIQUE (APPLICATION_ID, ROLE_NAME);
CREATE INDEX idx_PRIVILEGE_ID ON CSM_ROLE_PRIVILEGE(PRIVILEGE_ID);
ALTER TABLE CSM_ROLE_PRIVILEGE
ADD CONSTRAINT UQ_ROLE_PRIVILEGE_ROLE_ID UNIQUE (PRIVILEGE_ID, ROLE_ID);
CREATE INDEX idx_ROLE_ID ON CSM_ROLE_PRIVILEGE(ROLE_ID);
ALTER TABLE CSM_USER
ADD CONSTRAINT UQ_LOGIN_NAME UNIQUE (LOGIN_NAME);
CREATE INDEX idx_USER_ID ON CSM_USER_GROUP(USER_ID);
CREATE INDEX idx_GROUP_ID ON CSM_USER_GROUP(GROUP_ID);
CREATE INDEX idx_GROUP_ID ON CSM_USER_GROUP_ROLE_PG(GROUP_ID);
CREATE INDEX idx_ROLE_ID ON CSM_USER_GROUP_ROLE_PG(ROLE_ID);
CREATE INDEX idx_PROTECTION_GROUP_ID ON CSM_USER_GROUP_ROLE_PG(PROTECTION_GROUP_ID);
CREATE INDEX idx_USER_ID ON CSM_USER_GROUP_ROLE_PG(USER_ID);
CREATE INDEX idx_USER_ID ON CSM_USER_PE(USER_ID);
CREATE INDEX idx_PROTECTION_ELEMENT_ID ON CSM_USER_PE(PROTECTION_ELEMENT_ID);

ALTER TABLE CSM_USER_PE
ADD CONSTRAINT UQ_USER_PROTECTION_ELEMENT_PROTECTION_ELEMENT_ID UNIQUE (USER_ID, PROTECTION_ELEMENT_ID);

ALTER TABLE CSM_GROUP ADD CONSTRAINT FK_APPLICATION_GROUP
FOREIGN KEY (APPLICATION_ID) REFERENCES CSM_APPLICATION (APPLICATION_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_FILTER_CLAUSE ADD CONSTRAINT FK_APPLICATION_FILTER_CLAUSE
FOREIGN KEY (APPLICATION_ID) REFERENCES CSM_APPLICATION (APPLICATION_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_PROTECTION_ELEMENT ADD CONSTRAINT FK_PE_APPLICATION
FOREIGN KEY (APPLICATION_ID) REFERENCES CSM_APPLICATION (APPLICATION_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_PROTECTION_GROUP ADD CONSTRAINT FK_PG_APPLICATION
FOREIGN KEY (APPLICATION_ID) REFERENCES CSM_APPLICATION (APPLICATION_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_PROTECTION_GROUP ADD CONSTRAINT FK_PROTECTION_GROUP
FOREIGN KEY (PARENT_PROTECTION_GROUP_ID) REFERENCES CSM_PROTECTION_GROUP (PROTECTION_GROUP_ID);

ALTER TABLE CSM_PG_PE ADD CONSTRAINT FK_PROTECTION_ELEMENT_PROTECTION_GROUP
FOREIGN KEY (PROTECTION_ELEMENT_ID) REFERENCES CSM_PROTECTION_ELEMENT (PROTECTION_ELEMENT_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_PG_PE ADD CONSTRAINT FK_PROTECTION_GROUP_PROTECTION_ELEMENT
FOREIGN KEY (PROTECTION_GROUP_ID) REFERENCES CSM_PROTECTION_GROUP (PROTECTION_GROUP_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_ROLE ADD CONSTRAINT FK_APPLICATION_ROLE
FOREIGN KEY (APPLICATION_ID) REFERENCES CSM_APPLICATION (APPLICATION_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_ROLE_PRIVILEGE ADD CONSTRAINT FK_PRIVILEGE_ROLE
FOREIGN KEY (PRIVILEGE_ID) REFERENCES CSM_PRIVILEGE (PRIVILEGE_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_ROLE_PRIVILEGE ADD CONSTRAINT FK_ROLE
FOREIGN KEY (ROLE_ID) REFERENCES CSM_ROLE (ROLE_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_USER_GROUP ADD CONSTRAINT FK_USER_GROUP
FOREIGN KEY (USER_ID) REFERENCES CSM_USER (USER_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_USER_GROUP ADD CONSTRAINT FK_UG_GROUP
FOREIGN KEY (GROUP_ID) REFERENCES CSM_GROUP (GROUP_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_USER_GROUP_ROLE_PG ADD CONSTRAINT FK_USER_GROUP_ROLE_PROTECTION_GROUP_GROUPS
FOREIGN KEY (GROUP_ID) REFERENCES CSM_GROUP (GROUP_ID)
ON DELETE CASCADE
;

ALTER TABLE CSM_USER_GROUP_ROLE_PG ADD CONSTRAINT FK_USER_GROUP_ROLE_PROTECTION_GROUP_ROLE
FOREIGN KEY (ROLE_ID) REFERENCES CSM_ROLE (ROLE_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_USER_GROUP_ROLE_PG ADD CONSTRAINT FK_USER_GROUP_ROLE_PROTECTION_GROUP_PROTECTION_GROUP
FOREIGN KEY (PROTECTION_GROUP_ID) REFERENCES CSM_PROTECTION_GROUP (PROTECTION_GROUP_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_USER_GROUP_ROLE_PG ADD CONSTRAINT FK_USER_GROUP_ROLE_PROTECTION_GROUP_USER
FOREIGN KEY (USER_ID) REFERENCES CSM_USER (USER_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_USER_PE ADD CONSTRAINT FK_PE_USER
FOREIGN KEY (USER_ID) REFERENCES CSM_USER (USER_ID)
ON DELETE CASCADE;

ALTER TABLE CSM_USER_PE ADD CONSTRAINT FK_PROTECTION_ELEMENT_USER
FOREIGN KEY (PROTECTION_ELEMENT_ID) REFERENCES CSM_PROTECTION_ELEMENT (PROTECTION_ELEMENT_ID)
ON DELETE CASCADE;

--
-- Data for table `CSM_APPLICATION`
--

INSERT INTO `CSM_APPLICATION` (`APPLICATION_ID`,`APPLICATION_NAME`,`APPLICATION_DESCRIPTION`,`DECLARATIVE_FLAG`,`ACTIVE_FLAG`,`UPDATE_DATE`,`DATABASE_URL`,`DATABASE_USER_NAME`,`DATABASE_PASSWORD`,`DATABASE_DIALECT`,`DATABASE_DRIVER`) VALUES
 (1,'csmupt','CSM UPT Super Admin Application',0,1,CURDATE(),'','','','org.hibernate.dialect.MySQLDialect','com.mysql.jdbc.Driver'),
 (2,'ncireportwriter','ncireportwriter',0,1,CURDATE(),'','','','org.hibernate.dialect.MySQLDialect','com.mysql.jdbc.Driver');

--
-- Data for table `CSM_GROUP`
--

INSERT INTO `CSM_GROUP` (`GROUP_ID`,`GROUP_NAME`,`GROUP_DESC`,`UPDATE_DATE`,`APPLICATION_ID`) VALUES
 (1,'admin','admin',CURDATE(),2),
 (2,'non-admin','non-admin read only',CURDATE(),2);

--
-- Data for table `CSM_PRIVILEGE`
--

INSERT INTO `CSM_PRIVILEGE` (`PRIVILEGE_ID`,`PRIVILEGE_NAME`,`PRIVILEGE_DESCRIPTION`,`UPDATE_DATE`) VALUES
 (1,'CREATE','This privilege grants permission to a user to create an entity. This entity can be an object, a database entry, or a resource such as a network connection',CURDATE()),
 (2,'ACCESS','This privilege allows a user to access a particular resource.  Examples of resources include a network or database connection, socket, module of the application, or even the application itself',CURDATE()),
 (3,'READ','This privilege permits the user to read data from a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to read data about a particular entry',CURDATE()),
 (4,'WRITE','This privilege allows a user to write data to a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to write data about a particular entity',CURDATE()),
 (5,'UPDATE','This privilege grants permission at an entity level and signifies that the user is allowed to update data for a particular entity. Entities may include an object, object attribute, database row etc',CURDATE()),
 (6,'DELETE','This privilege permits a user to delete a logical entity. This entity can be an object, a database entry, a resource such as a network connection, etc',CURDATE()),
 (7,'EXECUTE','This privilege allows a user to execute a particular resource. The resource can be a method, function, behavior of the application, URL, button etc',CURDATE());

--
-- Data for table `CSM_PROTECTION_ELEMENT`
--

INSERT INTO `CSM_PROTECTION_ELEMENT` (`PROTECTION_ELEMENT_ID`,`PROTECTION_ELEMENT_NAME`,`PROTECTION_ELEMENT_DESCRIPTION`,`OBJECT_ID`,`ATTRIBUTE`,`ATTRIBUTE_VALUE`,`PROTECTION_ELEMENT_TYPE`,`APPLICATION_ID`,`UPDATE_DATE`) VALUES
 (1,'csmupt','CSM UPT Super Admin Application Protection Element','csmupt','','','',1,CURDATE()),
 (2,'ncireportwriter','ncireportwriter','ncireportwriter','','','',1,CURDATE()),
 (3,'ncireportwriter','ncireportwriter','ncireportwriter','','','ncireportwriter',2,CURDATE()),
 (4,'csmupt','csmupt','csmupt','','','csmupt',2,CURDATE());

--
-- Data for table `CSM_PROTECTION_GROUP`
--

INSERT INTO `CSM_PROTECTION_GROUP` (`PROTECTION_GROUP_ID`,`PROTECTION_GROUP_NAME`,`PROTECTION_GROUP_DESCRIPTION`,`APPLICATION_ID`,`LARGE_ELEMENT_COUNT_FLAG`,`UPDATE_DATE`,`PARENT_PROTECTION_GROUP_ID`) VALUES
 (1,'admin_pg','pg for admins',2,0,CURDATE(),NULL),
 (2,'non-admin-pg','pg for non-admin',2,0,CURDATE(),NULL);

--
-- Data for table `CSM_PG_PE`
--

INSERT INTO `CSM_PG_PE` (`PG_PE_ID`,`PROTECTION_GROUP_ID`,`PROTECTION_ELEMENT_ID`,`UPDATE_DATE`) VALUES
 (1,1,3,'0000-00-00'),
 (2,2,3,'0000-00-00');

--
-- Data for table `CSM_ROLE`
--

INSERT INTO `CSM_ROLE` (`ROLE_ID`,`ROLE_NAME`,`ROLE_DESCRIPTION`,`APPLICATION_ID`,`ACTIVE_FLAG`,`UPDATE_DATE`) VALUES
 (1,'admin-role','admin-role',2,1,CURDATE()),
 (2,'non-admin-role','non-admin-role',2,1,CURDATE());

--
-- Data for table `CSM_ROLE_PRIVILEGE`
--

INSERT INTO `CSM_ROLE_PRIVILEGE` (`ROLE_PRIVILEGE_ID`,`ROLE_ID`,`PRIVILEGE_ID`) VALUES
 (3,1,1),
 (2,1,2),
 (4,1,3),
 (8,2,3),
 (1,1,4),
 (5,1,5),
 (6,1,6),
 (7,1,7);

--
-- Data for table `CSM_USER`
--

INSERT INTO `CSM_USER` (`USER_ID`,`LOGIN_NAME`,`MIGRATED_FLAG`,`FIRST_NAME`,`LAST_NAME`,`ORGANIZATION`,`DEPARTMENT`,`TITLE`,`PHONE_NUMBER`,`PASSWORD`,`EMAIL_ID`,`START_DATE`,`END_DATE`,`UPDATE_DATE`,`PREMGRT_LOGIN_NAME`) VALUES
 (1,'admin',0,'N/A','N/A','NGIT','NGIT','System administrator','','zJPWCwDeSgG8j2uyHEABIQ==','',CURDATE(),NULL,CURDATE(),''),
 (2,'testr',0,'N/A','N/A','NGIT','NGIT','Read only user','','zJPWCwDeSgG8j2uyHEABIQ==','',CURDATE(),NULL,CURDATE(),'');

--
-- Data for table `CSM_USER_GROUP`
--

INSERT INTO `CSM_USER_GROUP` (`USER_GROUP_ID`,`USER_ID`,`GROUP_ID`) VALUES
 (1,1,1),
 (2,2,2);

--
-- Data for table `CSM_USER_GROUP_ROLE_PG`
--

INSERT INTO `CSM_USER_GROUP_ROLE_PG` (`USER_GROUP_ROLE_PG_ID`,`USER_ID`,`GROUP_ID`,`ROLE_ID`,`PROTECTION_GROUP_ID`,`UPDATE_DATE`) VALUES
 (1,1,NULL,1,1,CURDATE()),
 (2,2,NULL,2,2,CURDATE()),
 (3,NULL,1,1,1,CURDATE()),
 (4,NULL,2,2,2,CURDATE());

--
-- Data for table `CSM_USER_PE`
--

INSERT INTO `CSM_USER_PE` (`USER_PROTECTION_ELEMENT_ID`,`PROTECTION_ELEMENT_ID`,`USER_ID`) VALUES
 (3,1,1),
 (2,2,1);

COMMIT;
