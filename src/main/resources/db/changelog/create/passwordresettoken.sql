create table if not exists passwordresettoken
(
    token_id           bigint not null
        primary key,
    expirydate         timestamp,
    confirmation_token varchar(255),
    user_id            bigint not null
        constraint fk107q28x1p1049g0mcaacswvgv
            references applicationuser
);


