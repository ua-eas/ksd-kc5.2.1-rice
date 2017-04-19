--
-- Copyright 2005-2016 The Kuali Foundation
--
-- Licensed under the Educational Community License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.opensource.org/licenses/ecl2.php
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- Travel Destination Table

CREATE TABLE TRVL_DEST_T (
    TRVL_DEST_ID    VARCHAR(40) NOT NULL,
    OBJ_ID          VARCHAR(36) NOT NULL,
    VER_NBR         DECIMAL(8,0) NOT NULL DEFAULT '1',
    DEST_NM         VARCHAR(40) NOT NULL,
    POSTAL_CNTRY_CD VARCHAR(40) NOT NULL,
    POSTAL_STATE_CD VARCHAR(40) NOT NULL,
    ACTV_IND        VARCHAR(1) NOT NULL DEFAULT 'Y',
    CONSTRAINT TRVL_DEST_PK PRIMARY KEY (TRVL_DEST_ID),
    UNIQUE INDEX TRVL_DEST_TC1 (OBJ_ID)
) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/

CREATE TABLE TRVL_DEST_ID_S (
  id bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
/

INSERT INTO TRVL_DEST_ID_S values (0)
/

INSERT INTO TRVL_DEST_T (TRVL_DEST_ID, OBJ_ID, VER_NBR, DEST_NM, POSTAL_CNTRY_CD, POSTAL_STATE_CD, ACTV_IND)
VALUES (last_insert_id(), uuid(), 1, 'Colorado', 'US', 'CO', 'Y')
/

-- Millage Rate Table

CREATE TABLE TRVL_MLG_RT_T  (
    MLG_RT_ID VARCHAR(40) NOT NULL,
    MLG_RT_CD VARCHAR(40) NOT NULL,
    OBJ_ID    VARCHAR(36) NOT NULL,
    VER_NBR   DECIMAL(8,0) NOT NULL DEFAULT '1',
    MLG_RT_NM VARCHAR(40) NOT NULL,
    MLG_RT    DECIMAL(8,0) NOT NULL,
    ACTV_IND  VARCHAR(1) NULL DEFAULT 'Y',
    CONSTRAINT TRVL_MLG_RT_PK PRIMARY KEY (MLG_RT_ID),
    UNIQUE INDEX TRVL_MIL_RT_TC1 (OBJ_ID)
) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/

CREATE TABLE TRVL_MLG_RT_ID_S (
  id bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
/

INSERT INTO TRVL_MLG_RT_ID_S VALUES (0)
/

INSERT INTO TRVL_MLG_RT_T (MLG_RT_ID, MLG_RT_CD,OBJ_ID,VER_NBR,MLG_RT_NM,MLG_RT, ACTV_IND)
VALUES (last_insert_id(), 'DO', uuid(), 1,'Domestic', 30, 'Y')
/

-- Travel Expense Company Table

CREATE TABLE TRVL_CO_T (
    CO_ID         VARCHAR(40) NOT NULL,
    CO_NM         VARCHAR(40) NOT NULL,
    OBJ_ID        VARCHAR(36) NOT NULL,
    VER_NBR       DECIMAL(8,0) NOT NULL DEFAULT '1',
    ACTV_IND      VARCHAR(1) NOT NULL DEFAULT 'Y',
    CONSTRAINT TRVL_CO_PK PRIMARY KEY (CO_ID),
    UNIQUE INDEX TRVL_CO_TC1 (OBJ_ID)
) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/

CREATE TABLE TRVL_CO_ID_S (
  id bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
/

INSERT INTO TRVL_CO_ID_S VALUES ('0')
/

INSERT INTO TRVL_CO_T (CO_ID, CO_NM,OBJ_ID,VER_NBR, ACTV_IND ) VALUES (last_insert_id(), 'Value Rentals', uuid(),1,'Y')
/

 -- Travel Authorization Table

DROP TABLE IF EXISTS TRVL_AUTH_DOC_T
/

CREATE TABLE TRVL_AUTH_DOC_T (
    TRVL_AUTH_DOC_ID  VARCHAR(40) NOT NULL,
    TRAVELER_DTL_ID   VARCHAR(14),
    TRVL_TYP_CD       VARCHAR(3),
    TRVL_BGN_DT       DATETIME,
    TRVL_END_DT       DATETIME,
    TRVL_DEST_ID      VARCHAR(40),
    EXP_LMT           DECIMAL(19,2) DEFAULT '0.00',
    TRVL_DESC         VARCHAR(255) DEFAULT NULL,
    CELL_PH_NUM       VARCHAR(20),
    OBJ_ID            VARCHAR(36) NOT NULL,
    VER_NBR           DECIMAL(8,0) NOT NULL DEFAULT '1',
    CONSTRAINT TRVL_AUTH_DOC_PK PRIMARY KEY (TRVL_AUTH_DOC_ID),
    UNIQUE INDEX TRVL_AUTH_DOC_TC1 (OBJ_ID),
    CONSTRAINT TRVL_AUTH_DOC_FK1 FOREIGN KEY (TRVL_DEST_ID)
    REFERENCES TRVL_DEST_T (TRVL_DEST_ID)
) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/

INSERT INTO TRVL_AUTH_DOC_T (TRVL_AUTH_DOC_ID,TRAVELER_DTL_ID,
 TRVL_TYP_CD,TRVL_BGN_DT,TRVL_END_DT,TRVL_DEST_ID,EXP_LMT,TRVL_DESC,
 CELL_PH_NUM,OBJ_ID,VER_NBR)
VALUES('10000','1','IS','2013-12-01 00:00:00','2013-12-04 00:00:00','10000',
1000.00,'Kuali Days Conference','1234567', uuid(),'1')
/

-- Per Diem Table

CREATE TABLE TRVL_PD_EXP_T  (
    PD_EXP_ID         VARCHAR(40) NOT NULL,
    OBJ_ID            VARCHAR(36) NOT NULL,
    VER_NBR           DECIMAL(8,0) NOT NULL DEFAULT '1',
    TRVL_DEST_ID      VARCHAR(40) NOT NULL,
    TRVL_AUTH_DOC_ID  VARCHAR(40) NOT NULL,
    PD_DT             DATETIME NULL,
    BKFST_VAL         DECIMAL(19,2)  NOT NULL,
    LNCH_VAL          DECIMAL(19,2)  NOT NULL,
    DNNR_VAL          DECIMAL(19,2)  NOT NULL,
    INCD_VAL          DECIMAL(19,2)  NOT NULL,
    MLG_RT_ID         VARCHAR(40) NOT NULL,
    MLG_EST           DECIMAL(8,0) NOT NULL,
    CONSTRAINT TRVL_PD_EXP_PK PRIMARY KEY (PD_EXP_ID),
    UNIQUE INDEX TRVL_PD_EXP_TC1 (OBJ_ID),
    CONSTRAINT TRVL_PD_EXP_FK1 FOREIGN KEY (MLG_RT_ID)
    REFERENCES TRVL_MLG_RT_T (MLG_RT_ID),
    CONSTRAINT TRVL_PD_EXP_FK2 FOREIGN KEY (TRVL_DEST_ID)
    REFERENCES TRVL_DEST_T (TRVL_DEST_ID),
    CONSTRAINT TRVL_PD_EXP_FK3 FOREIGN KEY (TRVL_AUTH_DOC_ID)
    REFERENCES TRVL_AUTH_DOC_T (TRVL_AUTH_DOC_ID)
) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/

CREATE TABLE TRVL_PD_EXP_ID_S (
  id bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
/

INSERT INTO TRVL_PD_EXP_ID_S values ( 0 )
/

INSERT INTO TRVL_PD_EXP_T (PD_EXP_ID, OBJ_ID, VER_NBR, TRVL_DEST_ID, TRVL_AUTH_DOC_ID, PD_DT, BKFST_VAL, LNCH_VAL, DNNR_VAL, INCD_VAL, MLG_RT_ID, MLG_EST)
VALUES (last_insert_id(), uuid(),1,10000,10000, NOW(), 10.00, 10.00, 15.00, 20.00, 10000, 30)
/

-- Expense Item Table

CREATE TABLE TRVL_EXP_ITM_T  (
    EXP_ITM_ID        VARCHAR(40) NOT NULL,
    OBJ_ID            VARCHAR(36) NOT NULL,
    VER_NBR           DECIMAL(8,0) NOT NULL DEFAULT '1',
    TRVL_AUTH_DOC_ID  VARCHAR(40) NOT NULL,
    TRVL_CO_NM        VARCHAR(40) NOT NULL,
    EXP_TYP_CD        VARCHAR(40) NOT NULL,
    EXP_AMT           DECIMAL(19,2) NOT NULL,
    EXP_DESC          VARCHAR(255) NOT NULL,
    EXP_DT            DATETIME NULL,
    EXP_REIMB         VARCHAR(1) NULL DEFAULT 'Y',
    EXP_TXBL          VARCHAR(1) NULL DEFAULT 'Y',
    CONSTRAINT TRVL_EXP_ITM_PK PRIMARY KEY (EXP_ITM_ID),
    UNIQUE INDEX TRVL_EXP_ITM_TC1 (OBJ_ID),
    CONSTRAINT TRVL_EXP_ITM_FK1 FOREIGN KEY (TRVL_AUTH_DOC_ID)
    REFERENCES TRVL_AUTH_DOC_T (TRVL_AUTH_DOC_ID)
) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/

CREATE TABLE TRVL_EXP_ITM_ID_S (
  id bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
/

INSERT INTO TRVL_EXP_ITM_ID_S VALUES (0)
/

INSERT INTO TRVL_EXP_ITM_T (EXP_ITM_ID, OBJ_ID,VER_NBR,TRVL_AUTH_DOC_ID,TRVL_CO_NM,EXP_TYP_CD,EXP_AMT,EXP_DESC, EXP_DT, EXP_REIMB, EXP_TXBL)
VALUES (last_insert_id(), uuid(),1,10000,'Discount Travel','ME', 30.00, 'Family Related', NOW(), 'Y','Y')
/