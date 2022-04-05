create table messages
(
    id      int8 generated by default as identity,
    message varchar(255),
    user_id int8,
    primary key (id)
);
create table users
(
    id       int8 generated by default as identity,
    password varchar(255),
    username varchar(255),
    primary key (id)
);
alter table if exists messages add constraint FKpsmh6clh3csorw43eaodlqvkn foreign key (user_id) references users