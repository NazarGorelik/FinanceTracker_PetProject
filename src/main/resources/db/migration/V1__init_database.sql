create table person
(
    id         int primary key auto_increment,
    username   varchar(50) unique,
    created_at timestamp,
    role       varchar(50),
    password   varchar(100)
);


create table account
(
    id         int primary key auto_increment,
    balance    double,
    created_at timestamp,
    owner      varchar(50),
    name       varchar(50)
);
alter table account add foreign key (owner) references person (username);


create table transaction(
    id int primary key auto_increment,
    type varchar(10),
    amount double,
    description varchar(100),
    created_at timestamp,
    account_name varchar(100),
    account_id int
);
alter table transaction add foreign key (account_id) references account (id);
alter table transaction add check (type in ('EXPENSE','INCOME'))