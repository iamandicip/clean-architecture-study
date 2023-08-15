create table if not exists exchange_rates (
    ID serial primary key,
    CODE_FROM varchar(10) not null,
    CODE_TO varchar(10) not null,
    RATE numeric
);

create table if not exists outbox (
    ID serial primary key,
    EVENT_TYPE varchar(10) not null,
    CREATED_AT timestamp not null,
    EVENT_DATA varchar(4000) not null
);

CREATE SEQUENCE exchange_rates_seq;
CREATE SEQUENCE outbox_seq;