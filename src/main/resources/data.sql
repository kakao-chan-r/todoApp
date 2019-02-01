/**
Spring Boot can automatically create the schema (DDL scripts) of your DataSource and initialize it (DML scripts):
it loads SQL from the standard root classpath locations schema.sql and data.sql, respectively

Spring boot는 자동으로 DataSource의 스키마를 생성하고 초기화해주는데,
root classpath 경로에 schema.sql, data.sql을 각각 로드
 */


/*
INSERT INTO TODO (REFERENCE_ID, CONTENTS, STATUS, REG_DATE, MOD_DATE) VALUES (1, '해야할일1', 'TEST', '2011-10-30 14:33:44');
BOOLEAN 은 TINYINT(1) 동의어입니다. 0은 false 이고, 다른 것은 true 입니다.
*/

INSERT INTO TODO (TASK, STATUS, REG_DATE) VALUES ('한국 바레인전 축구', 0, '2017-10-30 14:33:44');
INSERT INTO TODO (TASK, STATUS, REG_DATE) VALUES ('일본 이란 축구', 1 ,'2018-10-30 11:53:21');
INSERT INTO TODO (TASK, STATUS, REG_DATE, MOD_DATE) VALUES ('일본 베트남전 축구', 1, '2017-10-30 14:33:44', '2017-11-30 04:33:44');
INSERT INTO TODO (TASK, STATUS, REG_DATE) VALUES ('한국 베트남전 축구', 0, '2017-10-30 14:33:44');
INSERT INTO TODO (REF_ID, TASK, STATUS, REG_DATE) VALUES (1, '황의조', 1, '2018-10-30 08:20:34');
INSERT INTO TODO (REF_ID, TASK, STATUS, REG_DATE, MOD_DATE) VALUES (1, '황희찬', 0, '2018-11-10 18:30:43', '2018-02-13 12:53:23');
INSERT INTO TODO (TASK, STATUS, REG_DATE) VALUES ('NAKATOMO', 0, '2016-11-20 16:08:49');
INSERT INTO TODO (REF_ID, TASK, STATUS, REG_DATE) VALUES (4, '손흥민', 0, '2019-01-28 11:12:28');
INSERT INTO TODO (REF_ID, TASK, STATUS, REG_DATE, MOD_DATE) VALUES (1, '김민재', 0, '2019-01-03 19:18:29', '2019-01-04 01:29:10');
INSERT INTO TODO (TASK, STATUS, REG_DATE) VALUES ('한국 카타르전 축구', 1, '2019-01-26 22:00:00');
