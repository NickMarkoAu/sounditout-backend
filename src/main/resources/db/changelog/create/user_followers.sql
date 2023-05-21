create table if not exists user_followers
(
    user_id     bigint not null
        constraint fk646xpqh4f8itvuv398p7bwdox
            references applicationuser,
    follower_id bigint not null
        constraint fkaeph5olm5maccs6gaea570196
            references applicationuser
);

