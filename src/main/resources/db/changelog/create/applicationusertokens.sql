create table if not exists applicationusertokens
(
    id         bigint not null
        primary key,
    freetokens bigint,
    tokens     bigint
);
