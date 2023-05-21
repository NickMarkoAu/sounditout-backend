create table if not exists userprofile
(
    id      bigint not null
        primary key,
    bio     varchar(255),
    song_id bigint
        constraint fkdae6eqdnwmo3ubvvk2a03m6ut
            references song,
    user_id bigint
        constraint fk2ueco7k31sbkhungw9cuyg28m
            references applicationuser
);

alter table userprofile
    owner to sounditout;

