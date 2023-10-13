alter table account drop constraint account_ibfk_1;
alter table account add foreign key (owner) references person (username)
    on update cascade on delete cascade;

alter table transaction drop constraint transaction_ibfk_1;
alter table transaction add foreign key (account_id) references account (id)
    on update cascade on delete cascade;