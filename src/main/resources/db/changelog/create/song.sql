create table if not exists song
(
    id      bigint not null
        primary key,
    artist  varchar(255),
    imageid bigint,
    name    varchar(255),
    tags    varchar(255),
    userid  varchar(255)
);

alter table song
    owner to sounditout;

