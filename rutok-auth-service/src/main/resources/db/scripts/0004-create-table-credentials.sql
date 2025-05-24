create table credentials
(
    id      bigserial NOT NULL,
    user_id bigint NOT NULL,
    salt    varchar(255) NOT NULL,
    hash    varchar(255) NOT NULL,
    CONSTRAINT pk_credentials_id PRIMARY KEY (id)
);
