create table if not exists applicationuser
(
    id               bigint  not null
        primary key,
    dateofbirth      timestamp,
    email            varchar(255),
    name             varchar(255),
    password         varchar(255),
    profile_image_id bigint,
    tokens_id        bigint
        constraint fkndywvu3ouou2kw4wdl2fyujdi
            references applicationusertokens,
    isemailconfirmed boolean not null,
    enabled          boolean not null
);