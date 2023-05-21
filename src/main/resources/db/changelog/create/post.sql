create table if not exists post
(
    id       bigint  not null
        primary key,
    content  varchar(255),
    date     timestamp,
    draft    boolean not null,
    likes    bigint,
    privacy  integer,
    tags     varchar(255),
    image_id bigint,
    song_id  bigint,
    user_id  bigint
        constraint fkebtvi8ipkd4ypp9o38ousnquw
            references applicationuser
);


