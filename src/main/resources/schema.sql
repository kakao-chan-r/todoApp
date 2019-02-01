/*

JPA Entity 사용으로 schema.sql은 사용하지않는다.
*/
CREATE TABLE TODO
(
  ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  REF_ID BIGINT DEFAULT 0,
  TASK VARCHAR2(255) NOT NULL,
  STATUS TINYINT(1) DEFAULT FALSE,
  REG_DATE DATETIME,
  MOD_DATE DATETIME
);

