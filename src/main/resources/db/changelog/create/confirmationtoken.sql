create table if not exists confirmationtoken
(
    token_id           bigint not null
        primary key,
    confirmation_token varchar(255),
    createddate        timestamp,
    user_id            bigint not null
        constraint fkm6kict7fnaof8sc6k7q3bihvx
            references applicationuser
);

alter table confirmationtoken
    owner to sounditout;

