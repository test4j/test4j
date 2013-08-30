create table MTN_ALL_TYPES
(
  ID          NUMBER(12) not null,
  T_BLOB      BLOB,
  T_CLOB      CLOB,
  T_CHAR      CHAR(100),
  T_DATE      DATE,
  T_NUMBER    NUMBER,
  T_RAW       RAW(100),
  T_TIMESTAMP TIMESTAMP(6),
  T_VARCHAR   VARCHAR2(100)
)