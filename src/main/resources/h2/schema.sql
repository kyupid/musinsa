create table category
(
    id int auto_increment,
    branch int not null,
    name varchar(100) not null,
    code varchar(300) not null,
    level int not null,
    constraint category_pk
        primary key (id)
);


