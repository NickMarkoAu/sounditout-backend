create table if not exists image
(
    id     bigint not null
        primary key,
    s3uri  varchar(255),
    tags   varchar(255),
    userid bigint
);

alter table image
    owner to sounditout;