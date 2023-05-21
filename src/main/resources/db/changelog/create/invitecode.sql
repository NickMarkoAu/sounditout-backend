create table if not exists invitecode
(
    id      bigint  not null
        primary key,
    code    varchar(255),
    used    boolean not null,
    user_id bigint
        constraint fkme40hsaurukap9r2akpgfxn6q
            references applicationuser
);

alter table invitecode
    owner to sounditout;

