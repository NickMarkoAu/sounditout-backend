create table if not exists reaction
(
    id           bigint not null
        primary key,
    reactiontype integer,
    post_id      bigint,
    user_id      bigint
);


