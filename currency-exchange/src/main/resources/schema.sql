create table if not exists exchange_rates (
    ID serial primary key,
    CODE_FROM varchar(10) not null,
    CODE_TO varchar(10) not null,
    RATE numeric
);

create table if not exists outbox (
    ID serial primary key,
    AGGREGATE_ID numeric not null,
    AGGREGATE_TYPE varchar(50) not null,
    EVENT_TYPE varchar(10) not null,
    PAYLOAD text not null,
    CREATED_AT timestamp not null
);

create sequence if not exists exchange_rates_seq;
create sequence if not exists outbox_seq;