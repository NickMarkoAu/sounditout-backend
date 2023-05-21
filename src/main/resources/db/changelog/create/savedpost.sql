create table if not exists savedpost
(
    id      bigint not null
        primary key,
    post_id bigint,
    user_id bigint
);

alter table savedpost
    owner to sounditout;

