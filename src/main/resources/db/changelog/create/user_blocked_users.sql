create table if not exists user_blocked_users
(
    user_id         bigint not null
        constraint fkg4mcq9owprw3qki1mpltkt7eb
            references applicationuser,
    blockedusers_id bigint not null
        constraint uk_jf6j88xllm9v5udp1n1q7c4xh
            unique
        constraint fkelks6dyainswoogn8oydwbgdt
            references applicationuser
);

alter table user_blocked_users
    owner to sounditout;

