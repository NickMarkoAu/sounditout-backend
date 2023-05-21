create table if not exists search
(
    id      bigint not null
        primary key,
    date    timestamp,
    query   varchar(255),
    type    integer,
    user_id bigint
        constraint fkoetowknt3t0ba4ljrr3k6limx
            references applicationuser
);
