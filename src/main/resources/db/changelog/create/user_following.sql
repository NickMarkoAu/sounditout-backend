create table if not exists user_following
(
    user_id      bigint not null
        constraint fkk6bafiou8ndl5uxv5vyh1skj7
            references applicationuser,
    following_id bigint not null
        constraint fkqj2w2sw8w4gpiddgwjn7w69jh
            references applicationuser
);
