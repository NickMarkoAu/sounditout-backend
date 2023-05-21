create table if not exists comment
(
    id      bigint not null
        primary key,
    content text,
    date    timestamp,
    user_id bigint,
    post_id bigint
        constraint fkqb0rnht649ifuh6gev5lwvx8x
            references post
);


