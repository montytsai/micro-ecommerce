create table if not exists category
(
    id          integer not null primary key,
    name        varchar(255),
    description varchar(255),
    version     integer default 0,
    created_at  timestamp,
    updated_at  timestamp
);

create table if not exists product
(
    id                 integer        not null primary key,
    name               varchar(255),
    description        varchar(255),
    available_quantity numeric(19, 4) not null default 0,
    price              numeric(38, 2) not null default 0,
    category_id        integer,
    version            integer                 default 0,
    created_at         timestamp,
    updated_at         timestamp,

    constraint fk_category foreign key (category_id) references category (id)
);

create sequence if not exists category_seq start with 1 increment by 50;
create sequence if not exists product_seq start with 1 increment by 50;