create table if not exists songmetadata
(
    id          bigint not null
        primary key,
    albumarturl varchar(255),
    previewurl  varchar(255),
    spotifyurl  varchar(255),
    song_id     bigint
        constraint fklc1jhe3jmstt9s7eeqvjvv0yx
            references song
);

