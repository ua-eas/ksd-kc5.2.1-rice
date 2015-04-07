-- 
-- Copyright 2008-2009 The Kuali Foundation
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
ALTER TABLE KR_KIM_ADDR_TYPE_T RENAME TO KRIM_ADDR_TYP_T
/
ALTER TABLE KRIM_ADDR_TYP_T RENAME COLUMN ADDR_TYP_NM TO NM
/
ALTER TABLE KRIM_ADDR_TYP_T RENAME CONSTRAINT KR_KIM_ADDR_TYPE_TC0 TO KRIM_ADDR_TYP_TC0
/
ALTER TABLE KRIM_ADDR_TYP_T RENAME CONSTRAINT KR_KIM_ADDR_TYPE_TC1 TO KRIM_ADDR_TYP_TI1
/
ALTER TABLE KR_KIM_AFLTN_TYPE_T RENAME TO KRIM_AFLTN_TYP_T
/
ALTER TABLE KRIM_AFLTN_TYP_T RENAME COLUMN AFLTN_TYP_NM TO NM
/
ALTER TABLE KRIM_AFLTN_TYP_T RENAME CONSTRAINT KR_KIM_AFLTN_TYPE_TC0 TO KRIM_AFLTN_TYP_TC0
/
ALTER TABLE KRIM_AFLTN_TYP_T RENAME CONSTRAINT KR_KIM_AFLTN_TYPE_TI1 TO KRIM_AFLTN_TYP_TI1
/
ALTER TABLE KR_KIM_ATTRIBUTE_T RENAME TO KRIM_ATTR_DEFN_T
/
ALTER TABLE KRIM_ATTR_DEFN_T RENAME COLUMN KIM_ATTRIB_ID TO KIM_ATTR_DEFN_ID
/
ALTER TABLE KRIM_ATTR_DEFN_T RENAME COLUMN ATTRIB_NM TO NM
/
ALTER TABLE KRIM_ATTR_DEFN_T RENAME COLUMN ATTRIB_LBL TO LBL
/
ALTER TABLE KRIM_ATTR_DEFN_T RENAME CONSTRAINT KR_KIM_ATTRIBUTE_TC0 TO KRIM_ATTR_DEFN_TI1
/
ALTER TABLE KR_KIM_CTZNSHP_STAT_T RENAME TO KRIM_CTZNSHP_STAT_T
/
ALTER TABLE KRIM_CTZNSHP_STAT_T RENAME COLUMN CTZNSHP_STAT_NM TO NM
/
ALTER TABLE KRIM_CTZNSHP_STAT_T RENAME CONSTRAINT KR_KIM_CTZNSHP_STAT_TC0 TO KRIM_CTZNSHP_STAT_TC0
/
ALTER TABLE KRIM_CTZNSHP_STAT_T RENAME CONSTRAINT KR_KIM_CTZNSHP_STAT_TC1 TO KRIM_CTZNSHP_STAT_TI1
/
ALTER TABLE KR_KIM_DELE_ATTR_DATA_T RENAME TO KRIM_DLGN_ATTR_DATA_T
/
ALTER TABLE KRIM_DLGN_ATTR_DATA_T RENAME COLUMN ATTRIB_DATA_ID TO ATTR_DATA_ID
/
ALTER TABLE KRIM_DLGN_ATTR_DATA_T RENAME COLUMN KIM_TYPE_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_DLGN_ATTR_DATA_T RENAME COLUMN KIM_ATTRIB_ID TO KIM_ATTR_DEFN_ID
/
ALTER TABLE KRIM_DLGN_ATTR_DATA_T RENAME COLUMN ATTRIB_VAL TO ATTR_VAL
/
ALTER TABLE KRIM_DLGN_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_DELE_ATTR_DATA_TC0 TO KRIM_DLGN_ATTR_DATA_TI1
/
ALTER TABLE KRIM_DLGN_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_DELE_ATTR_DATA_TR1 TO KRIM_DLGN_ATTR_DATA_TR1
/
ALTER TABLE KRIM_DLGN_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_DELE_ATTR_DATA_TR3 TO KRIM_DLGN_ATTR_DATA_TR3
/
ALTER TABLE KRIM_DLGN_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_DELE_ATTR_DATA_TR2 TO KRIM_DLGN_ATTR_DATA_TR2
/
ALTER TABLE KR_KIM_DELE_GROUP_T RENAME TO KRIM_DLGN_GRP_T
/
ALTER TABLE KRIM_DLGN_GRP_T RENAME COLUMN DELE_MBR_ID TO DLGN_MBR_ID
/
ALTER TABLE KRIM_DLGN_GRP_T RENAME COLUMN DELE_ID TO DLGN_ID
/
ALTER TABLE KRIM_DLGN_GRP_T RENAME CONSTRAINT KR_KIM_DELE_GROUP_TC0 TO KRIM_DLGN_GRP_TI1
/
ALTER TABLE KRIM_DLGN_GRP_T RENAME CONSTRAINT KR_KIM_DELE_GROUP_TR1 TO KRIM_DLGN_GRP_TR1
/
ALTER TABLE KR_KIM_DELE_MBR_ATTR_DATA_T RENAME TO KRIM_DLGN_MBR_ATTR_DATA_T
/
ALTER TABLE KRIM_DLGN_MBR_ATTR_DATA_T RENAME COLUMN ATTRIB_DATA_ID TO ATTR_DATA_ID
/
ALTER TABLE KRIM_DLGN_MBR_ATTR_DATA_T RENAME COLUMN KIM_TYPE_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_DLGN_MBR_ATTR_DATA_T RENAME COLUMN KIM_ATTRIB_ID TO KIM_ATTR_DEFN_ID
/
ALTER TABLE KRIM_DLGN_MBR_ATTR_DATA_T RENAME COLUMN ATTRIB_VAL TO ATTR_VAL
/
ALTER TABLE KRIM_DLGN_MBR_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_DELE_MBR_ATTR_DATA_TC0 TO KRIM_DELE_MBR_ATTR_DATA_TI1
/
ALTER TABLE KRIM_DLGN_MBR_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_DELE_MBR_ATTR_DATA_TR1 TO KRIM_DLGN_MBR_ATTR_DATA_TR1
/
ALTER TABLE KRIM_DLGN_MBR_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_DELE_MBR_ATTR_DATA_TR2 TO KRIM_DLGN_MBR_ATTR_DATA_TR2
/
ALTER TABLE KR_KIM_DELE_PRINCIPAL_T RENAME TO KRIM_DLGN_PRNCPL_T
/
ALTER TABLE KRIM_DLGN_PRNCPL_T RENAME COLUMN DELE_MBR_ID TO DLGN_MBR_ID
/
ALTER TABLE KRIM_DLGN_PRNCPL_T RENAME COLUMN DELE_ID TO DLGN_ID
/
ALTER TABLE KRIM_DLGN_PRNCPL_T RENAME CONSTRAINT KR_KIM_DELE_PRINCIPAL_TC0 TO KRIM_DLGN_PRNCPL_TI1
/
ALTER TABLE KRIM_DLGN_PRNCPL_T RENAME CONSTRAINT KR_KIM_DELE_PRINCIPAL_TR1 TO KRIM_DLGN_PRNCPL_TR1
/
ALTER TABLE KR_KIM_DELE_ROLE_T RENAME TO KRIM_DLGN_ROLE_T
/
ALTER TABLE KRIM_DLGN_ROLE_T RENAME COLUMN DELE_MBR_ID TO DLGN_MBR_ID
/
ALTER TABLE KRIM_DLGN_ROLE_T RENAME COLUMN DELE_ID TO DLGN_ID
/
ALTER TABLE KRIM_DLGN_ROLE_T RENAME CONSTRAINT KR_KIM_DELE_ROLE_TC0 TO KRIM_DLGN_ROLE_TI1
/
ALTER TABLE KRIM_DLGN_ROLE_T RENAME CONSTRAINT KR_KIM_DELE_ROLE_TR2 TO KRIM_DLGN_ROLE_TR2
/
ALTER TABLE KRIM_DLGN_ROLE_T RENAME CONSTRAINT KR_KIM_DELE_ROLE_TR1 TO KRIM_DLGN_ROLE_TR1
/
ALTER TABLE KR_KIM_DELE_T RENAME TO KRIM_DLGN_T
/
ALTER TABLE KRIM_DLGN_T RENAME COLUMN DELE_ID TO DLGN_ID
/
ALTER TABLE KRIM_DLGN_T RENAME COLUMN TYP_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_DLGN_T RENAME COLUMN DELE_TYP_CD TO DLGN_TYP_CD
/
ALTER TABLE KRIM_DLGN_T RENAME CONSTRAINT KR_KIM_DELE_TC0 TO KRIM_DLGN_TI1
/
ALTER TABLE KRIM_DLGN_T RENAME CONSTRAINT KR_KIM_DELE_TR2 TO KRIM_DLGN_TR2
/
ALTER TABLE KRIM_DLGN_T RENAME CONSTRAINT KR_KIM_DELE_TR1 TO KRIM_DLGN_TR1
/
ALTER TABLE KR_KIM_EMAIL_TYPE_T RENAME TO KRIM_EMAIL_TYP_T
/
ALTER TABLE KRIM_EMAIL_TYP_T RENAME COLUMN EMAIL_TYP_NM TO NM
/
ALTER TABLE KRIM_EMAIL_TYP_T RENAME CONSTRAINT KR_KIM_EMAIL_TYPE_TC0 TO KRIM_EMAIL_TYP_TI1
/
ALTER TABLE KRIM_EMAIL_TYP_T RENAME CONSTRAINT KR_KIM_EMAIL_TYPE_TC1 TO KRIM_EMAIL_TYP_TI2
/
ALTER TABLE KR_KIM_EMP_STAT_T RENAME TO KRIM_EMP_STAT_T
/
ALTER TABLE KRIM_EMP_STAT_T RENAME COLUMN EMP_STAT_NM TO NM
/
ALTER TABLE KRIM_EMP_STAT_T RENAME CONSTRAINT KR_KIM_EMP_STAT_TC0 TO KRIM_EMP_STAT_TI1
/
ALTER TABLE KRIM_EMP_STAT_T RENAME CONSTRAINT KR_KIM_EMP_STAT_TC1 TO KRIM_EMP_STAT_TI2
/
ALTER TABLE KR_KIM_EMP_TYPE_T RENAME TO KRIM_EMP_TYP_T
/
ALTER TABLE KRIM_EMP_TYP_T RENAME COLUMN EMP_TYP_NM TO NM
/
ALTER TABLE KRIM_EMP_TYP_T RENAME CONSTRAINT KR_KIM_EMP_TYPE_TC0 TO KRIM_EMP_TYP_TI1
/
ALTER TABLE KRIM_EMP_TYP_T RENAME CONSTRAINT KR_KIM_EMP_TYPE_TC1 TO KRIM_EMP_TYP_TI2
/
ALTER TABLE KR_KIM_ENTITY_ADDR_T RENAME TO KRIM_ENTITY_ADDR_T
/
ALTER TABLE KRIM_ENTITY_ADDR_T RENAME CONSTRAINT KR_KIM_ENTITY_ADDR_TC0 TO KRIM_ENTITY_ADDR_TI1
/
ALTER TABLE KRIM_ENTITY_ADDR_T RENAME CONSTRAINT KR_KIM_ENTITY_ADDR_TR2 TO KRIM_ENTITY_ADDR_TR2
/
ALTER TABLE KRIM_ENTITY_ADDR_T RENAME CONSTRAINT KR_KIM_ENTITY_ADDR_TR1 TO KRIM_ENTITY_ADDR_TR1
/
ALTER TABLE KR_KIM_ENTITY_AFLTN_T RENAME TO KRIM_ENTITY_AFLTN_T
/
ALTER TABLE KRIM_ENTITY_AFLTN_T RENAME CONSTRAINT KR_KIM_ENTITY_AFLTN_TC0 TO KRIM_ENTITY_AFLTN_TI1
/
ALTER TABLE KRIM_ENTITY_AFLTN_T RENAME CONSTRAINT KR_KIM_ENTITY_AFLTN_TR2 TO KRIM_ENTITY_AFLTN_TR2
/
ALTER TABLE KRIM_ENTITY_AFLTN_T RENAME CONSTRAINT KR_KIM_ENTITY_AFLTN_TR1 TO KRIM_ENTITY_AFLTN_TR1
/
ALTER TABLE KR_KIM_ENTITY_BIO_T RENAME TO KRIM_ENTITY_BIO_T
/
ALTER TABLE KRIM_ENTITY_BIO_T RENAME CONSTRAINT KR_KIM_ENTITY_BIO_TC0 TO KRIM_ENTITY_BIO_TI1
/
ALTER TABLE KR_KIM_ENTITY_CTZNSHP_T RENAME TO KRIM_ENTITY_CTZNSHP_T
/
ALTER TABLE KRIM_ENTITY_CTZNSHP_T RENAME CONSTRAINT KR_KIM_ENTITY_CTZNSHP_TC0 TO KRIM_ENTITY_CTZNSHP_TI1
/
ALTER TABLE KRIM_ENTITY_CTZNSHP_T RENAME CONSTRAINT KR_KIM_ENTITY_CTZNSHP_TR1 TO KRIM_ENTITY_CTZNSHP_TR1
/
ALTER TABLE KRIM_ENTITY_CTZNSHP_T RENAME CONSTRAINT KR_KIM_ENTITY_CTZNSHP_TR2 TO KRIM_ENTITY_CTZNSHP_TR2
/
ALTER TABLE KR_KIM_ENTITY_EMAIL_T RENAME TO KRIM_ENTITY_EMAIL_T
/
ALTER TABLE KRIM_ENTITY_EMAIL_T RENAME CONSTRAINT KR_KIM_ENTITY_EMAIL_TC0 TO KRIM_ENTITY_EMAIL_TC0
/
ALTER TABLE KRIM_ENTITY_EMAIL_T RENAME CONSTRAINT KR_KIM_ENTITY_EMAIL_TR2 TO KRIM_ENTITY_EMAIL_TR2
/
ALTER TABLE KRIM_ENTITY_EMAIL_T RENAME CONSTRAINT KR_KIM_ENTITY_EMAIL_TR1 TO KRIM_ENTITY_EMAIL_TR1
/
ALTER TABLE KR_KIM_ENTITY_EMP_INFO_T RENAME TO KRIM_ENTITY_EMP_INFO_T
/
ALTER TABLE KRIM_ENTITY_EMP_INFO_T RENAME CONSTRAINT KR_KIM_ENTITY_EMP_INFO_TC0 TO KRIM_ENTITY_EMP_INFO_TC0
/
ALTER TABLE KRIM_ENTITY_EMP_INFO_T RENAME CONSTRAINT KR_KIM_ENTITY_EMP_INFO_TR1 TO KRIM_ENTITY_EMP_INFO_TR1
/
ALTER TABLE KRIM_ENTITY_EMP_INFO_T RENAME CONSTRAINT KR_KIM_ENTITY_EMP_INFO_TR3 TO KRIM_ENTITY_EMP_INFO_TR3
/
ALTER TABLE KRIM_ENTITY_EMP_INFO_T RENAME CONSTRAINT KR_KIM_ENTITY_EMP_INFO_TR2 TO KRIM_ENTITY_EMP_INFO_TR2
/
ALTER TABLE KRIM_ENTITY_EMP_INFO_T RENAME CONSTRAINT KR_KIM_ENTITY_EMP_INFO_TR4 TO KRIM_ENTITY_EMP_INFO_TR4
/
ALTER TABLE KR_KIM_ENTITY_ENT_TYPE_T RENAME TO KRIM_ENTITY_ENT_TYP_T
/
ALTER TABLE KRIM_ENTITY_ENT_TYP_T RENAME CONSTRAINT KR_KIM_ENTITY_ENT_TYPE_TC0 TO KRIM_ENTITY_ENT_TYP_TC0
/
ALTER TABLE KRIM_ENTITY_ENT_TYP_T RENAME CONSTRAINT KR_KIM_ENTITY_ENT_TYPE_TR1 TO KRIM_ENTITY_ENT_TYP_TR1
/
ALTER TABLE KRIM_ENTITY_ENT_TYP_T RENAME CONSTRAINT KR_KIM_ENTITY_ENT_TYPE_TR2 TO KRIM_ENTITY_ENT_TYP_TR2
/
ALTER TABLE KR_KIM_ENTITY_EXT_ID_T RENAME TO KRIM_ENTITY_EXT_ID_T
/
ALTER TABLE KRIM_ENTITY_EXT_ID_T RENAME CONSTRAINT KR_KIM_ENTITY_EXT_ID_TC0 TO KRIM_ENTITY_EXT_ID_TC0
/
ALTER TABLE KRIM_ENTITY_EXT_ID_T RENAME CONSTRAINT KR_KIM_ENTITY_EXT_ID_TR2 TO KRIM_ENTITY_EXT_ID_TR2
/
ALTER TABLE KRIM_ENTITY_EXT_ID_T RENAME CONSTRAINT KR_KIM_ENTITY_EXT_ID_TR1 TO KRIM_ENTITY_EXT_ID_TR1
/
ALTER TABLE KR_KIM_ENTITY_NAME_T RENAME TO KRIM_ENTITY_NM_T
/
ALTER TABLE KRIM_ENTITY_NM_T RENAME COLUMN ENTITY_NAME_ID TO ENTITY_NM_ID
/
ALTER TABLE KRIM_ENTITY_NM_T RENAME COLUMN NAME_TYP_CD TO NM_TYP_CD
/
ALTER TABLE KRIM_ENTITY_NM_T RENAME CONSTRAINT KR_KIM_ENTITY_NAME_TC0 TO KRIM_ENTITY_NM_TC0
/
ALTER TABLE KRIM_ENTITY_NM_T RENAME CONSTRAINT KR_KIM_ENTITY_NM_TR2 TO KRIM_ENTITY_NM_TR2
/
ALTER TABLE KRIM_ENTITY_NM_T RENAME CONSTRAINT KR_KIM_ENTITY_NAME_TR1 TO KRIM_ENTITY_NM_TR1
/
ALTER TABLE KR_KIM_ENTITY_PHONE_T RENAME TO KRIM_ENTITY_PHONE_T
/
ALTER TABLE KRIM_ENTITY_PHONE_T RENAME CONSTRAINT KR_KIM_ENTITY_PHONE_TC0 TO KRIM_ENTITY_PHONE_TC0
/
ALTER TABLE KRIM_ENTITY_PHONE_T RENAME CONSTRAINT KR_KIM_ENTITY_PHONE_TR1 TO KRIM_ENTITY_PHONE_TR1
/
ALTER TABLE KRIM_ENTITY_PHONE_T RENAME CONSTRAINT KR_KIM_ENTITY_PHONE_TR2 TO KRIM_ENTITY_PHONE_TR2
/
ALTER TABLE KR_KIM_ENTITY_PRIV_PREF_T RENAME TO KRIM_ENTITY_PRIV_PREF_T
/
ALTER TABLE KRIM_ENTITY_PRIV_PREF_T RENAME COLUMN SUPPRESS_NAME_IND TO SUPPRESS_NM_IND
/
ALTER TABLE KRIM_ENTITY_PRIV_PREF_T RENAME CONSTRAINT KR_KIM_ENTITY_PRIV_PREF_TC0 TO KRIM_ENTITY_PRIV_PREF_TC0
/
ALTER TABLE KR_KIM_ENTITY_T RENAME TO KRIM_ENTITY_T
/
ALTER TABLE KRIM_ENTITY_T RENAME CONSTRAINT KR_KIM_ENTITY_TC0 TO KRIM_ENTITY_TC0
/
ALTER TABLE KR_KIM_ENT_NAME_TYPE_T RENAME TO KRIM_ENT_NM_TYP_T
/
ALTER TABLE KRIM_ENT_NM_TYP_T RENAME COLUMN ENT_NAME_TYP_CD TO ENT_NM_TYP_CD
/
ALTER TABLE KRIM_ENT_NM_TYP_T RENAME COLUMN ENT_NAME_TYP_NM TO NM
/
ALTER TABLE KRIM_ENT_NM_TYP_T RENAME CONSTRAINT KR_KIM_ENT_NAME_TYPE_TC0 TO KRIM_ENT_NM_TYP_TC0
/
ALTER TABLE KRIM_ENT_NM_TYP_T RENAME CONSTRAINT KR_KIM_ENT_NAME_TYPE_TC1 TO KRIM_ENT_NM_TYP_TC1
/
ALTER TABLE KR_KIM_ENT_TYPE_T RENAME TO KRIM_ENT_TYP_T
/
ALTER TABLE KRIM_ENT_TYP_T RENAME COLUMN ENT_TYP_NM TO NM
/
ALTER TABLE KRIM_ENT_TYP_T RENAME CONSTRAINT KR_KIM_ENT_TYPE_TC0 TO KRIM_ENT_TYP_TC0
/
ALTER TABLE KRIM_ENT_TYP_T RENAME CONSTRAINT KR_KIM_ENT_TYPE_TC1 TO KRIM_ENT_TYP_TC1
/
ALTER TABLE KR_KIM_EXT_ID_TYPE_T RENAME TO KRIM_EXT_ID_TYP_T
/
ALTER TABLE KRIM_EXT_ID_TYP_T RENAME COLUMN EXT_ID_TYP_NM TO NM
/
ALTER TABLE KRIM_EXT_ID_TYP_T RENAME CONSTRAINT KR_KIM_EXT_ID_TYPE_TC0 TO KRIM_EXT_ID_TYP_TC0
/
ALTER TABLE KRIM_EXT_ID_TYP_T RENAME CONSTRAINT KR_KIM_EXT_ID_TYPE_TC1 TO KRIM_EXT_ID_TYP_TC1
/
ALTER TABLE KR_KIM_GROUP_ATTR_DATA_T RENAME TO KRIM_GRP_ATTR_DATA_T
/
ALTER TABLE KRIM_GRP_ATTR_DATA_T RENAME COLUMN ATTRIB_DATA_ID TO ATTR_DATA_ID
/
ALTER TABLE KRIM_GRP_ATTR_DATA_T RENAME COLUMN KIM_TYPE_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_GRP_ATTR_DATA_T RENAME COLUMN KIM_ATTRIB_ID TO KIM_ATTR_DEFN_ID
/
ALTER TABLE KRIM_GRP_ATTR_DATA_T RENAME COLUMN ATTRIB_VAL TO ATTR_VAL
/
ALTER TABLE KRIM_GRP_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_GROUP_ATTR_DATA_TC0 TO KRIM_GRP_ATTR_DATA_TC0
/
ALTER TABLE KRIM_GRP_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_GROUP_ATTR_DATA_TR2 TO KRIM_GRP_ATTR_DATA_TR2
/
ALTER TABLE KRIM_GRP_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_GROUP_ATTR_DATA_TR3 TO KRIM_GRP_ATTR_DATA_TR3
/
ALTER TABLE KRIM_GRP_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_GROUP_ATTR_DATA_TR1 TO KRIM_GRP_ATTR_DATA_TR1
/
ALTER TABLE KR_KIM_GROUP_GROUP_T RENAME TO KRIM_GRP_GRP_T
/
ALTER TABLE KRIM_GRP_GRP_T RENAME COLUMN GRP_MEMBER_ID TO GRP_MBR_ID
/
ALTER TABLE KRIM_GRP_GRP_T RENAME COLUMN MEMBER_GRP_ID TO MBR_GRP_ID
/
ALTER TABLE KRIM_GRP_GRP_T RENAME CONSTRAINT KR_KIM_GROUP_GROUP_TC0 TO KRIM_GRP_GRP_TC0
/
ALTER TABLE KRIM_GRP_GRP_T RENAME CONSTRAINT KR_KIM_GROUP_GROUP_TR1 TO KRIM_GRP_GRP_TR1
/
ALTER TABLE KRIM_GRP_GRP_T RENAME CONSTRAINT KR_KIM_GROUP_GROUP_TR2 TO KRIM_GRP_GRP_TR2
/
ALTER TABLE KR_KIM_GROUP_PRINCIPAL_T RENAME TO KRIM_GRP_PRNCPL_T
/
ALTER TABLE KRIM_GRP_PRNCPL_T RENAME COLUMN GRP_MEMBER_ID TO GRP_MBR_ID
/
ALTER TABLE KRIM_GRP_PRNCPL_T RENAME CONSTRAINT KR_KIM_GROUP_PRINCIPAL_TC0 TO KRIM_GRP_PRNCPL_TC0
/
ALTER TABLE KRIM_GRP_PRNCPL_T RENAME CONSTRAINT KR_KIM_GROUP_PRINCIPAL_TR1 TO KRIM_GRP_PRNCPL_TR1
/
ALTER TABLE KR_KIM_GROUP_T RENAME TO KRIM_GRP_T
/
ALTER TABLE KRIM_GRP_T RENAME COLUMN TYP_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_GRP_T RENAME CONSTRAINT KR_KIM_GROUP_TC0 TO KRIM_GRP_TC0
/
ALTER TABLE KRIM_GRP_T RENAME CONSTRAINT KR_KIM_GROUP_TC1 TO KRIM_GRP_TC1
/
ALTER TABLE KRIM_GRP_T RENAME CONSTRAINT KR_KIM_GROUP_TR1 TO KRIM_GRP_TR1
/
ALTER TABLE KR_KIM_PERM_ATTR_DATA_T RENAME TO KRIM_PERM_ATTR_DATA_T
/
ALTER TABLE KRIM_PERM_ATTR_DATA_T RENAME COLUMN ATTRIB_DATA_ID TO ATTR_DATA_ID
/
ALTER TABLE KRIM_PERM_ATTR_DATA_T RENAME COLUMN KIM_TYPE_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_PERM_ATTR_DATA_T RENAME COLUMN KIM_ATTRIB_ID TO KIM_ATTR_DEFN_ID
/
ALTER TABLE KRIM_PERM_ATTR_DATA_T RENAME COLUMN ATTRIB_VAL TO ATTR_VAL
/
ALTER TABLE KRIM_PERM_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_PERM_ATTR_DATA_TC0 TO KRIM_PERM_ATTR_DATA_TC0
/
ALTER TABLE KRIM_PERM_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_PERM_ATTR_DATA_TR1 TO KRIM_PERM_ATTR_DATA_TR1
/
ALTER TABLE KRIM_PERM_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_PERM_ATTR_DATA_TR3 TO KRIM_PERM_ATTR_DATA_TR3
/
ALTER TABLE KRIM_PERM_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_PERM_ATTR_DATA_TR2 TO KRIM_PERM_ATTR_DATA_TR2
/
ALTER TABLE KR_KIM_PERM_T RENAME TO KRIM_PERM_T
/
ALTER TABLE KRIM_PERM_T RENAME COLUMN NAME TO NM
/
ALTER TABLE KRIM_PERM_T RENAME COLUMN DESCRIPTION TO DESC_TXT
/
ALTER TABLE KRIM_PERM_T RENAME COLUMN NAMESPACE_CD TO NMSPC_CD
/
ALTER TABLE KRIM_PERM_T RENAME CONSTRAINT KR_KIM_PERM_TC0 TO KRIM_PERM_TC0
/
ALTER TABLE KRIM_PERM_T RENAME CONSTRAINT KR_KIM_PERM_TR1 TO KRIM_PERM_TR1
/
ALTER TABLE KR_KIM_PERM_TMPL_T RENAME TO KRIM_PERM_TMPL_T
/
ALTER TABLE KRIM_PERM_TMPL_T RENAME COLUMN NAME TO NM
/
ALTER TABLE KRIM_PERM_TMPL_T RENAME COLUMN DESCRIPTION TO DESC_TXT
/
ALTER TABLE KRIM_PERM_TMPL_T RENAME COLUMN KIM_TYPE_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_PERM_TMPL_T RENAME COLUMN NAMESPACE_CD TO NMSPC_CD
/
ALTER TABLE KRIM_PERM_TMPL_T RENAME CONSTRAINT KR_KIM_PERM_TMPL_TC0 TO KRIM_PERM_TMPL_TC0
/
ALTER TABLE KRIM_PERM_TMPL_T RENAME CONSTRAINT KR_KIM_PERM_TMPL_TR1 TO KRIM_PERM_TMPL_TR1
/
ALTER TABLE KR_KIM_PHONE_TYPE_T RENAME TO KRIM_PHONE_TYP_T
/
ALTER TABLE KRIM_PHONE_TYP_T RENAME CONSTRAINT KR_KIM_PHONE_TYPE_TC0 TO KRIM_PHONE_TYP_TC0
/
ALTER TABLE KRIM_PHONE_TYP_T RENAME CONSTRAINT KR_KIM_PHONE_TYPE_TC1 TO KRIM_PHONE_TYP_TC1
/
ALTER TABLE KR_KIM_PRINCIPAL_T RENAME TO KRIM_PRNCPL_T
/
ALTER TABLE KRIM_PRNCPL_T RENAME CONSTRAINT KR_KIM_PRINCIPAL_TC0 TO KRIM_PRNCPL_TC0
/
ALTER TABLE KRIM_PRNCPL_T RENAME CONSTRAINT KR_KIM_PRINCIPAL_TC1 TO KRIM_PRNCPL_TC1
/
ALTER TABLE KRIM_PRNCPL_T RENAME CONSTRAINT KR_KIM_PRINCIPAL_TR1 TO KRIM_PRNCPL_TR1
/
ALTER TABLE KR_KIM_RESP_ATTR_DATA_T RENAME TO KRIM_RSP_ATTR_DATA_T
/
ALTER TABLE KRIM_RSP_ATTR_DATA_T RENAME COLUMN ATTRIB_DATA_ID TO ATTR_DATA_ID
/
ALTER TABLE KRIM_RSP_ATTR_DATA_T RENAME COLUMN KIM_TYPE_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_RSP_ATTR_DATA_T RENAME COLUMN KIM_ATTRIB_ID TO KIM_ATTR_DEFN_ID
/
ALTER TABLE KRIM_RSP_ATTR_DATA_T RENAME COLUMN ATTRIB_VAL TO ATTR_VAL
/
ALTER TABLE KRIM_RSP_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_RESP_ATTR_DATA_TC0 TO KRIM_RSP_ATTR_DATA_TC0
/
ALTER TABLE KRIM_RSP_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_RESP_ATTR_DATA_TR2 TO KRIM_RSP_ATTR_DATA_TR2
/
ALTER TABLE KRIM_RSP_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_RESP_ATTR_DATA_TR1 TO KRIM_RSP_ATTR_DATA_TR1
/
ALTER TABLE KRIM_RSP_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_RESP_ATTR_DATA_TR3 TO KRIM_RSP_ATTR_DATA_TR3
/
ALTER TABLE KR_KIM_RESP_T RENAME TO KRIM_RSP_T
/
ALTER TABLE KRIM_RSP_T RENAME COLUMN RESP_ID TO RSP_ID
/
ALTER TABLE KRIM_RSP_T RENAME COLUMN RESP_TMPL_ID TO RSP_TMPL_ID
/
ALTER TABLE KRIM_RSP_T RENAME COLUMN NAME TO NM
/
ALTER TABLE KRIM_RSP_T RENAME COLUMN DESCRIPTION TO DESC_TXT
/
ALTER TABLE KRIM_RSP_T RENAME COLUMN NAMESPACE_CD TO NMSPC_CD
/
ALTER TABLE KRIM_RSP_T RENAME CONSTRAINT KR_KIM_RESP_TC0 TO KRIM_RSP_TC0
/
ALTER TABLE KRIM_RSP_T RENAME CONSTRAINT KR_KIM_RESP_TR1 TO KRIM_RSP_TR1
/
ALTER TABLE KR_KIM_RESP_TMPL_T RENAME TO KRIM_RSP_TMPL_T
/
ALTER TABLE KRIM_RSP_TMPL_T RENAME COLUMN RESP_TMPL_ID TO RSP_TMPL_ID
/
ALTER TABLE KRIM_RSP_TMPL_T RENAME COLUMN NAME TO NM
/
ALTER TABLE KRIM_RSP_TMPL_T RENAME COLUMN KIM_TYPE_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_RSP_TMPL_T RENAME COLUMN DESCRIPTION TO DESC_TXT
/
ALTER TABLE KRIM_RSP_TMPL_T RENAME COLUMN NAMESPACE_CD TO NMSPC_CD
/
ALTER TABLE KRIM_RSP_TMPL_T RENAME CONSTRAINT KR_KIM_RESP_TMPL_TC0 TO KRIM_RSP_TMPL_TC0
/
ALTER TABLE KRIM_RSP_TMPL_T RENAME CONSTRAINT KR_KIM_RESP_TMPL_TR1 TO KRIM_RSP_TMPL_TR1
/
ALTER TABLE KR_KIM_ROLE_GROUP_T RENAME TO KRIM_ROLE_GRP_T
/
ALTER TABLE KRIM_ROLE_GRP_T RENAME COLUMN ROLE_MEMBER_ID TO ROLE_MBR_ID
/
ALTER TABLE KRIM_ROLE_GRP_T RENAME CONSTRAINT KR_KIM_ROLE_GROUP_TC0 TO KRIM_ROLE_GRP_TC0
/
ALTER TABLE KRIM_ROLE_GRP_T RENAME CONSTRAINT KR_KIM_ROLE_GROUP_TC1 TO KRIM_ROLE_GRP_TC1
/
ALTER TABLE KRIM_ROLE_GRP_T RENAME CONSTRAINT KR_KIM_ROLE_GROUP_TR1 TO KRIM_ROLE_GRP_TR1
/
ALTER TABLE KR_KIM_ROLE_MBR_ATTR_DATA_T RENAME TO KRIM_ROLE_MBR_ATTR_DATA_T
/
ALTER TABLE KRIM_ROLE_MBR_ATTR_DATA_T RENAME COLUMN ATTRIB_DATA_ID TO ATTR_DATA_ID
/
ALTER TABLE KRIM_ROLE_MBR_ATTR_DATA_T RENAME COLUMN KIM_TYPE_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_ROLE_MBR_ATTR_DATA_T RENAME COLUMN KIM_ATTRIB_ID TO KIM_ATTR_DEFN_ID
/
ALTER TABLE KRIM_ROLE_MBR_ATTR_DATA_T RENAME COLUMN ATTRIB_VAL TO ATTR_VAL
/
ALTER TABLE KRIM_ROLE_MBR_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_ROLE_MBR_ATTR_DATA_TC0 TO KRIM_ROLE_MBR_ATTR_DATA_TC0
/
ALTER TABLE KRIM_ROLE_MBR_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_ROLE_MBR_ATTR_DATA_TR1 TO KRIM_ROLE_MBR_ATTR_DATA_TR1
/
ALTER TABLE KRIM_ROLE_MBR_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_ROLE_MBR_ATTR_DATA_TR2 TO KRIM_ROLE_MBR_ATTR_DATA_TR2
/
ALTER TABLE KR_KIM_ROLE_PERM_T RENAME TO KRIM_ROLE_PERM_T
/
ALTER TABLE KRIM_ROLE_PERM_T RENAME CONSTRAINT KR_KIM_ROLE_PERM_TC0 TO KRIM_ROLE_PERM_TC0
/
ALTER TABLE KRIM_ROLE_PERM_T RENAME CONSTRAINT KR_KIM_ROLE_PERM_TR1 TO KRIM_ROLE_PERM_TR1
/
ALTER TABLE KR_KIM_ROLE_PRINCIPAL_T RENAME TO KRIM_ROLE_PRNCPL_T
/
ALTER TABLE KRIM_ROLE_PRNCPL_T RENAME COLUMN ROLE_MEMBER_ID TO ROLE_MBR_ID
/
ALTER TABLE KRIM_ROLE_PRNCPL_T RENAME CONSTRAINT KR_KIM_ROLE_PRINCIPAL_TC0 TO KRIM_ROLE_PRNCPL_TC0
/
ALTER TABLE KRIM_ROLE_PRNCPL_T RENAME CONSTRAINT KR_KIM_ROLE_PRINCIPAL_TR1 TO KRIM_ROLE_PRNCPL_TR1
/
ALTER TABLE KR_KIM_ROLE_REL_T RENAME TO KRIM_ROLE_REL_T
/
ALTER TABLE KRIM_ROLE_REL_T RENAME CONSTRAINT KR_KIM_ROLE_REL_TC0 TO KRIM_ROLE_REL_TC0
/
ALTER TABLE KRIM_ROLE_REL_T RENAME CONSTRAINT KR_KIM_ROLE_REL_TC1 TO KRIM_ROLE_REL_TC1
/
ALTER TABLE KRIM_ROLE_REL_T RENAME CONSTRAINT KR_KIM_ROLE_REL_TR1 TO KRIM_ROLE_REL_TR1
/
ALTER TABLE KRIM_ROLE_REL_T RENAME CONSTRAINT KR_KIM_ROLE_REL_TR2 TO KRIM_ROLE_REL_TR2
/
ALTER TABLE KR_KIM_ROLE_RESP_ACTN_T RENAME TO KRIM_ROLE_RSP_ACTN_T
/
ALTER TABLE KRIM_ROLE_RSP_ACTN_T RENAME COLUMN ROLE_RESP_ACTN_ID TO ROLE_RSP_ACTN_ID
/
ALTER TABLE KRIM_ROLE_RSP_ACTN_T RENAME COLUMN RESP_ID TO RSP_ID
/
ALTER TABLE KRIM_ROLE_RSP_ACTN_T RENAME CONSTRAINT KR_KIM_ROLE_RESP_ACTN_TC0 TO KRIM_ROLE_RSP_ACTN_TC0
/
ALTER TABLE KR_KIM_ROLE_RESP_ATTR_DATA_T RENAME TO KRIM_ROLE_RSP_ATTR_DATA_T
/
ALTER TABLE KRIM_ROLE_RSP_ATTR_DATA_T RENAME COLUMN ATTRIB_DATA_ID TO ATTR_DATA_ID
/
ALTER TABLE KRIM_ROLE_RSP_ATTR_DATA_T RENAME COLUMN KIM_TYPE_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_ROLE_RSP_ATTR_DATA_T RENAME COLUMN KIM_ATTRIB_ID TO KIM_ATTR_DEFN_ID
/
ALTER TABLE KRIM_ROLE_RSP_ATTR_DATA_T RENAME COLUMN ATTRIB_VAL TO ATTR_VAL
/
ALTER TABLE KRIM_ROLE_RSP_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_ROLE_RESP_ATTR_DATA_TC0 TO KRIM_ROLE_RSP_ATTR_DATA_TC0
/
ALTER TABLE KRIM_ROLE_RSP_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_ROLE_RESP_ATTR_DATA_TR2 TO KRIM_ROLE_RSP_ATTR_DATA_TR2
/
ALTER TABLE KRIM_ROLE_RSP_ATTR_DATA_T RENAME CONSTRAINT KR_KIM_ROLE_RESP_ATTR_DATA_TR1 TO KRIM_ROLE_RSP_ATTR_DATA_TR1
/
ALTER TABLE KR_KIM_ROLE_RESP_T RENAME TO KRIM_ROLE_RSP_T
/
ALTER TABLE KRIM_ROLE_RSP_T RENAME COLUMN ROLE_RESP_ID TO ROLE_RSP_ID
/
ALTER TABLE KRIM_ROLE_RSP_T RENAME COLUMN RESP_ID TO RSP_ID
/
ALTER TABLE KRIM_ROLE_RSP_T RENAME CONSTRAINT KR_KIM_ROLE_RESP_TC0 TO KRIM_ROLE_RSP_TC0
/
ALTER TABLE KRIM_ROLE_RSP_T RENAME CONSTRAINT KR_KIM_ROLE_RESP_TR1 TO KRIM_ROLE_RSP_TR1
/
ALTER TABLE KR_KIM_ROLE_T RENAME TO KRIM_ROLE_T
/
ALTER TABLE KRIM_ROLE_T RENAME COLUMN ROLE_DESC TO DESC_TXT
/
ALTER TABLE KRIM_ROLE_T RENAME COLUMN TYP_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_ROLE_T RENAME CONSTRAINT KR_KIM_ROLE_TC0 TO KRIM_ROLE_TC0
/
ALTER TABLE KRIM_ROLE_T RENAME CONSTRAINT KR_KIM_ROLE_TC1 TO KRIM_ROLE_TC1
/
ALTER TABLE KRIM_ROLE_T RENAME CONSTRAINT KR_KIM_ROLE_TR1 TO KRIM_ROLE_TR1
/
ALTER TABLE KR_KIM_TYPE_ATTRIBUTE_T RENAME TO KRIM_TYP_ATTR_T
/
ALTER TABLE KRIM_TYP_ATTR_T RENAME COLUMN KIM_TYPE_ATTRIB_ID TO KIM_TYP_ATTR_ID
/
ALTER TABLE KRIM_TYP_ATTR_T RENAME COLUMN KIM_TYPE_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_TYP_ATTR_T RENAME COLUMN KIM_ATTRIB_ID TO KIM_ATTR_DEFN_ID
/
ALTER TABLE KRIM_TYP_ATTR_T RENAME CONSTRAINT KR_KIM_TYPE_ATTRIBUTE_TC0 TO KRIM_TYP_ATTR_TC0
/
ALTER TABLE KRIM_TYP_ATTR_T RENAME CONSTRAINT KR_KIM_TYPE_ATTRIBUTE_TR1 TO KRIM_TYP_ATTRIBUTE_TR1
/
ALTER TABLE KRIM_TYP_ATTR_T RENAME CONSTRAINT KR_KIM_TYPE_ATTRIBUTE_TR2 TO KRIM_TYP_ATTR_TR2
/
ALTER TABLE KR_KIM_TYPE_T RENAME TO KRIM_TYP_T
/
ALTER TABLE KRIM_TYP_T RENAME COLUMN KIM_TYPE_ID TO KIM_TYP_ID
/
ALTER TABLE KRIM_TYP_T RENAME COLUMN TYPE_NM TO NM
/
ALTER TABLE KRIM_TYP_T RENAME CONSTRAINT KR_KIM_TYPE_TC0 TO KRIM_TYP_TC0
/
RENAME KR_KIM_ATTRIB_DATA_ID_SEQ TO KRIM_ATTR_DATA_ID_S
/
RENAME KR_KIM_ATTRIB_ID_SEQ TO KRIM_ATTR_DEFN_ID_S
/
RENAME KR_KIM_DELE_ID_SEQ TO KRIM_DLGN_ID_S
/
RENAME KR_KIM_DELE_MBR_ID_SEQ TO KRIM_DLGN_MBR_ID_S
/
RENAME KR_KIM_ENTITY_ADDR_ID_SEQ TO KRIM_ENTITY_ADDR_ID_S
/
RENAME KR_KIM_ENTITY_AFLTN_ID_SEQ TO KRIM_ENTITY_AFLTN_ID_S
/
RENAME KR_KIM_ENTITY_CTZNSHP_ID_SEQ TO KRIM_ENTITY_CTZNSHP_ID_S
/
RENAME KR_KIM_ENTITY_EMAIL_ID_SEQ TO KRIM_ENTITY_EMAIL_ID_S
/
RENAME KR_KIM_ENTITY_EMP_ID_SEQ TO KRIM_ENTITY_EMP_ID_S
/
RENAME KR_KIM_ENTITY_ENT_TYPE_ID_SEQ TO KRIM_ENTITY_ENT_TYP_ID_S
/
RENAME KR_KIM_ENTITY_EXT_ID_ID_SEQ TO KRIM_ENTITY_EXT_ID_ID_S
/
RENAME KR_KIM_ENTITY_ID_SEQ TO KRIM_ENTITY_ID_S
/
RENAME KR_KIM_ENTITY_NAME_ID_SEQ TO KRIM_ENTITY_NM_ID_S
/
RENAME KR_KIM_ENTITY_PHONE_ID_SEQ TO KRIM_ENTITY_PHONE_ID_S
/
RENAME KR_KIM_GROUP_ID_SEQ TO KRIM_GRP_ID_S
/
RENAME KR_KIM_GROUP_MEMBER_ID_SEQ TO KRIM_GRP_MBR_ID_S
/
RENAME KR_KIM_PERM_ID_SEQ TO KRIM_PERM_ID_S
/
RENAME KR_KIM_PERM_TMPL_ID_SEQ TO KRIM_PERM_TMPL_ID_S
/
RENAME KR_KIM_PRNCPL_ID_SEQ TO KRIM_PRNCPL_ID_S
/
RENAME KR_KIM_RESP_ID_SEQ TO KRIM_RSP_ID_S
/
RENAME KR_KIM_RESP_TMPL_ID_SEQ TO KRIM_RSP_TMPL_ID_S
/
RENAME KR_KIM_ROLE_ID_SEQ TO KRIM_ROLE_ID_S
/
RENAME KR_KIM_ROLE_MEMBER_ID_SEQ TO KRIM_ROLE_MBR_ID_S
/
RENAME KR_KIM_ROLE_PERM_ID_SEQ TO KRIM_ROLE_PERM_ID_S
/
RENAME KR_KIM_ROLE_REL_ID_SEQ TO KRIM_ROLE_REL_ID_S
/
RENAME KR_KIM_ROLE_RESP_ACTN_ID_SEQ TO KRIM_ROLE_RSP_ACTN_ID_S
/
RENAME KR_KIM_ROLE_RESP_ID_SEQ TO KRIM_ROLE_RSP_ID_S
/
RENAME KR_KIM_ROLE_RESP_RESOL_ID_SEQ TO KRIM_ROLE_RSP_RESOL_ID_S
/
RENAME KR_KIM_TYPE_ATTRIB_ID_SEQ TO KRIM_TYP_ATTR_ID_S
/
RENAME KR_KIM_TYPE_ID_SEQ TO KRIM_TYP_ID_S
/