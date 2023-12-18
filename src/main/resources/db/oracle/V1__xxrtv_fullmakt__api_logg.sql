-------------------------------------------------------------------------------
-- Skjema            : OEBS-FULLMAKT-GODKJENNING-API
-- Tabell            : XXRTV_RESTAPIFULLMAKT_LOGG
-- Beskrivelse       : Loggtabell for API-kall.
-------------------------------------------------------------------------------
CREATE TABLE xxrtv.xxrtv_fullmakt_api_logg( kall_logg_id        NUMBER        NOT NULL
    , korrelasjon_id      VARCHAR2(50)  NOT NULL
    , tidspunkt           TIMESTAMP(9)  NOT NULL
    , type                VARCHAR2(10)  NOT NULL
    , kall_retning        VARCHAR2(10)  NOT NULL
    , method              VARCHAR2(10)
    , operation           VARCHAR2(100) NOT NULL
    , status              NUMBER
    , kalltid             NUMBER        NOT NULL
    , request             CLOB
    , response            CLOB
    , logginfo            CLOB
    , CONSTRAINT xxrtv_fullmakt_api_pk PRIMARY KEY(kall_logg_id))
    PARTITION BY RANGE(tidspunkt)
    INTERVAL(NUMTOYMINTERVAL(1, 'MONTH'))
( PARTITION kall_logg_data_p1 VALUES LESS THAN( DATE '2023-05-01'));

CREATE OR REPLACE SYNONYM XXRTV_FULLMAKT_API_LOGG FOR XXRTV.XXRTV_FULLMAKT_API_LOGG;
-- (utføres som xxrtv) GRANT ALL ON XXRTV_RESTAPI_LOGG TO APPS;

CREATE INDEX xxrtv_fullmakt_api_U3 ON xxrtv_fullmakt_api_logg
    (status) LOCAL;

-- Legg til sekvens og trigger
CREATE  OR REPLACE SEQUENCE xxrtv_fullmakt_api_seq
START WITH 1
                             INCREMENT BY 1;

ALTER TABLE xxrtv.xxrtv_fullmakt_api_logg
    ADD (CONSTRAINT xxrtv_fullmakt_type_ck1 CHECK (type IN ('PLSQL', 'REST')));

ALTER TABLE xxrtv.xxrtv_fullmakt_api_logg
    ADD (CONSTRAINT xxrtv_fullmakt_retning_ck1 CHECK (kall_retning IN ('INN','UT')));

CREATE INDEX xxrtv_fullmakt_api_U1 ON xxrtv_fullmakt_api_logg
    (operation, kall_retning);

CREATE INDEX xxrtv_fullmakt_api_kalo_U2 ON xxrtv_fullmakt_api_logg
    (korrelasjon_id);

