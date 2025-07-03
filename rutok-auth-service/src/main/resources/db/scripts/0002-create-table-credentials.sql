create table credentials
(
    id         bigserial    NOT NULL,
    user_email varchar(100) NOT NULL,
    salt       varchar(255) NOT NULL,
    hash       varchar(255) NOT NULL,
    CONSTRAINT pk_credentials_id PRIMARY KEY (id)
);
