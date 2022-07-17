drop table if exists category;

create table category
(
    id int auto_increment,
    branch varchar(100) null,
    name varchar(100) not null,
    code varchar(300) not null,
    level int not null,
    constraint category_pk
        primary key (id)
);
