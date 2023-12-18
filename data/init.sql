-- Initializing required tables with basic structure

create table if not exists customers(
    id bigint primary key not null unique,
    username varchar(255) not null unique,
    password varchar(255) not null
    );

create table if not exists timers(
    id bigint primary key not null unique,
    start_time timestamp,
    end_time timestamp,
    remaining_time bigint
    );

create type task_status as enum('TODO', 'WORKING', 'DONE');
create table if not exists tasks(
    id bigint primary key not null unique,
    title varchar(255) not null,
    description varchar(255),
    status task_status
    );

create table if not exists groups(
    id bigint primary key not null unique,
    name varchar(255) not null
    );

alter table timers
add column customer_id bigint not null references customers;

alter table customers
add column timer_id bigint references timers,
add column group_id bigint references groups;

alter table tasks
add column customer_id bigint not null references customers;