--liquibase formatted sql
--changeset ryusipov:CREATE_TABLE_drone dbms:h2
create table drone
(
    serial_number varchar(100) not null,
    version       integer      not null,
    type          integer      not null,
    weight        integer      not null,
    battery_level integer      not null,
    state         integer      not null,
    CONSTRAINT serial_number_pk PRIMARY KEY (serial_number)
);
--rollback drop table drone;

--changeset ryusipov:CREATE_TABLE_loaded_meds dbms:h2
CREATE TABLE loaded_meds
(
    master_id varchar(100) not null,
    med_code  varchar(100) not null,
    quantity  integer      not null,
    CONSTRAINT loaded_meds_pk PRIMARY KEY (master_id, med_code),
    CONSTRAINT loaded_meds_fk FOREIGN KEY (master_id) REFERENCES drone (serial_number)
);
