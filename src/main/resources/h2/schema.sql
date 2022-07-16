create table category
(
    id int auto_increment,
    name varchar(100) not null,
    code varchar(300) not null,
    level int not null,
    constraint category_pk
        primary key (id)
);

create table last_insert_cate_code
(
    root_cate_id int auto_increment,
    level int null,
    last_insert_code varchar(300) not null,
    constraint last_insert_cate_code_pk
        primary key (root_cate_id, level)
);


