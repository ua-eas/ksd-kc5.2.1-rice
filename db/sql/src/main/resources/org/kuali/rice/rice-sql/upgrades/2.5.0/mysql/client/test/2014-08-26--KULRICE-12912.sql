CREATE TABLE KRTST_COLL_PRNT_T (
  PK_PROP VARCHAR(40) NOT NULL,
  STR_PROP VARCHAR(20) COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
/

CREATE TABLE KRTST_COLL_CHLD_T (
  PK_PROP VARCHAR(40) NOT NULL,
  PRNT_KEY VARCHAR(40) NOT NULL,
  CHAR_PROP CHAR(1),
  BOOL_PROP VARCHAR(1),
  SHORT_PROP DECIMAL(5,0),
  INT_PROP DECIMAL(10,0),
  LONG_PROP DECIMAL(20,0),
  FLOAT_PROP DECIMAL(10,2),
  DOUBLE_PROP DECIMAL(20,2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
/