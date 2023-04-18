--liquibase formatted sql
--changeset ryusipov:CREATE_TABLE_medication dbms:h2
CREATE TABLE medication (
  code varchar(100) not null,
  version integer not null,
  name varchar(100) not null,
  weight  integer not null,
  image blob null,
  constraint medication_pk primary key (code)
);
--rollback drop table medication;
