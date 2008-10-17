CREATE TABLE EN_DOC_TYP_T (
   	DOC_TYP_ID               	  NUMBER(19) NOT NULL ENABLE,
	DOC_TYP_PARNT_ID         	  NUMBER(19),
	DOC_TYP_NM               	  VARCHAR2(255),
	DOC_TYP_VER_NBR          	  NUMBER(10) DEFAULT 0,
	DOC_TYP_ACTV_IND         	  NUMBER(1),
	DOC_TYP_CUR_IND          	  NUMBER(1),
	DOC_TYP_LBL_TXT          	  VARCHAR2(255),
	DOC_TYP_PREV_VER   		 	  NUMBER(19),
	DOC_HDR_ID               	  NUMBER(14),
	DOC_TYP_DESC             	  VARCHAR2(255),
	DOC_TYP_HDLR_URL_ADDR         VARCHAR2(255),
	DOC_TYP_POST_PRCSR_NM         VARCHAR2(255),
	DOC_TYP_JNDI_URL_ADDR         VARCHAR2(255),
    WRKGRP_ID                     NUMBER(14),
    BLNKT_APPR_WRKGRP_ID          NUMBER(14),
    BLNKT_APPR_PLCY               VARCHAR2(10),
    RPT_WRKGRP_ID          		  NUMBER(14),
	ADV_DOC_SRCH_URL_ADDR         VARCHAR2(255),
    DOC_TYP_RTE_VER_NBR 		  VARCHAR(2) DEFAULT '1' NOT NULL,
    DOC_TYP_NOTIFY_ADDR 		  VARCHAR2(255),
    MESSAGE_ENTITY_NM			  VARCHAR2(10),
    DOC_TYP_EMAIL_XSL             VARCHAR2(255),
    DOC_TYP_SECURITY_XML          CLOB,
	DB_LOCK_VER_NBR	              NUMBER(8) DEFAULT 0,
	CONSTRAINT EN_DOC_TYP_T_PK PRIMARY KEY (DOC_TYP_ID)
)
/