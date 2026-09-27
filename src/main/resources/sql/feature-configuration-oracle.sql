-- ============================================================
-- FEATURE CONFIGURATION TABLES
-- Oracle-compatible DDL
-- ============================================================
-- Notes:
-- 1. END_POINT_GROUP_ID is intentionally stored as NUMBER(19) only.
--    Add its FK after the endpoint group table/entity is ready.
-- 2. ATTRIBUTE_VALUE stores the comparison value as text/CLOB.
--    Examples:
--      EQ      -> U999
--      IN      -> ["TELLER","MANAGER"]
--      BETWEEN -> {"from":1000,"to":5000}
-- ============================================================

-- ============================================================
-- 1. FEATURE
-- ============================================================
CREATE TABLE FEATURE (
    FEATURE_ID        VARCHAR2(100 CHAR) NOT NULL,
    DISPLAY_NAME      VARCHAR2(255 CHAR) NOT NULL,
    DESCRIPTION       VARCHAR2(2000 CHAR),
    ENABLED_GLOBALLY  CHAR(1 CHAR) DEFAULT 'N' NOT NULL,
    MSG_EN            VARCHAR2(2000 CHAR),
    MSG_AR            VARCHAR2(2000 CHAR),
    CREATED_AT        TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    UPDATED_AT        TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CREATED_BY        VARCHAR2(50) NOT NULL,
    UPDATED_BY        VARCHAR2(50),
    IS_ACTIVE         NUMBER(1) DEFAULT 1,

    CONSTRAINT PK_FEATURE
        PRIMARY KEY (FEATURE_ID),

    CONSTRAINT CHK_FEATURE_ACTIVE
        CHECK (IS_ACTIVE IN (0, 1)),

    CONSTRAINT CHK_FEATURE_ENABLED
        CHECK (ENABLED_GLOBALLY IN ('Y', 'N'))
);

-- ============================================================
-- 2. USER_ATTRIBUTE
-- Defines where the runtime value is read from.
-- ============================================================
CREATE TABLE USER_ATTRIBUTE (
    ATTRIBUTE_KEY       VARCHAR2(100 CHAR) NOT NULL,
    DATA_TYPE           VARCHAR2(30 CHAR) NOT NULL,
    SOURCE_TYPE         VARCHAR2(30 CHAR) NOT NULL,
    SOURCE_PATH         VARCHAR2(500 CHAR) NOT NULL,
    START_DATE          DATE DEFAULT SYSDATE,
    END_DATE            DATE,
    PRIORITY            NUMBER(10),

    CONSTRAINT PK_USER_ATTRIBUTE
        PRIMARY KEY (ATTRIBUTE_KEY),

    CONSTRAINT CHK_USER_ATTRIBUTE_DATA_TYPE
        CHECK (DATA_TYPE IN ('STRING', 'NUMBER', 'DATE', 'BOOLEAN', 'LIST')),

    CONSTRAINT CHK_USER_ATTRIBUTE_SOURCE
        CHECK (SOURCE_TYPE IN ('CLAIM', 'HEADER', 'BODY')),

    CONSTRAINT CHK_USER_ATTRIBUTE_DATES
        CHECK (
            END_DATE IS NULL
            OR START_DATE IS NULL
            OR END_DATE >= START_DATE
        )
);

-- ============================================================
-- 3. FEATURE_RULE
-- ============================================================
CREATE SEQUENCE FEATURE_RULE_SEQ
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NOMAXVALUE
    NOCYCLE
    CACHE 20;

CREATE TABLE FEATURE_RULE (
    RULE_ID             NUMBER(19) DEFAULT FEATURE_RULE_SEQ.NEXTVAL NOT NULL,
    FEATURE_ID          VARCHAR2(100 CHAR) NOT NULL,
    END_POINT_GROUP_ID  NUMBER(19) NOT NULL,
    ENABLED             CHAR(1 CHAR) DEFAULT 'Y' NOT NULL,
    START_DATE          DATE DEFAULT SYSDATE,
    END_DATE            DATE,
    DENY_CODE           VARCHAR2(100 CHAR),
    DENY_MSG_EN         VARCHAR2(500 CHAR),
    DENY_MSG_AR         VARCHAR2(500 CHAR),

    CONSTRAINT PK_FEATURE_RULE
        PRIMARY KEY (RULE_ID),

    CONSTRAINT CHK_FEATURE_RULE_ENABLED
        CHECK (ENABLED IN ('Y', 'N')),

    CONSTRAINT CHK_FEATURE_RULE_DATES
        CHECK (
            END_DATE IS NULL
            OR START_DATE IS NULL
            OR END_DATE >= START_DATE
        ),

    CONSTRAINT FK_FEATURE_RULE_FEATURE
        FOREIGN KEY (FEATURE_ID)
        REFERENCES FEATURE (FEATURE_ID)
);

CREATE INDEX IDX_FEATURE_RULE_FEATURE_ID
    ON FEATURE_RULE (FEATURE_ID);

CREATE INDEX IDX_FEATURE_RULE_ENDPOINT_GROUP_ID
    ON FEATURE_RULE (END_POINT_GROUP_ID);

-- ============================================================
-- 4. RULE_CONDITION
-- Multiple rows per RULE_ID are evaluated as AND clauses.
-- ============================================================
CREATE SEQUENCE RULE_CONDITION_SEQ
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NOMAXVALUE
    NOCYCLE
    CACHE 20;

CREATE TABLE RULE_CONDITION (
    CONDITION_ID        NUMBER(19) DEFAULT RULE_CONDITION_SEQ.NEXTVAL NOT NULL,
    RULE_ID             NUMBER(19) NOT NULL,
    ATTRIBUTE_KEY       VARCHAR2(100 CHAR) NOT NULL,
    OPERATOR            VARCHAR2(30 CHAR) NOT NULL,
    ATTRIBUTE_VALUE     CLOB,

    CONSTRAINT PK_RULE_CONDITION
        PRIMARY KEY (CONDITION_ID),

    CONSTRAINT CHK_RULE_CONDITION_OPERATOR
        CHECK (OPERATOR IN (
            'EQ',
            'NE',
            'IN',
            'NOT_IN',
            'GT',
            'GTE',
            'LT',
            'LTE',
            'BETWEEN',
            'EXISTS',
            'NOT_EXISTS'
        )),

    CONSTRAINT FK_RULE_CONDITION_RULE
        FOREIGN KEY (RULE_ID)
        REFERENCES FEATURE_RULE (RULE_ID),

    CONSTRAINT FK_RULE_CONDITION_ATTRIBUTE
        FOREIGN KEY (ATTRIBUTE_KEY)
        REFERENCES USER_ATTRIBUTE (ATTRIBUTE_KEY)
);

CREATE INDEX IDX_RULE_CONDITION_RULE_ID
    ON RULE_CONDITION (RULE_ID);

CREATE INDEX IDX_RULE_CONDITION_ATTRIBUTE_KEY
    ON RULE_CONDITION (ATTRIBUTE_KEY);
