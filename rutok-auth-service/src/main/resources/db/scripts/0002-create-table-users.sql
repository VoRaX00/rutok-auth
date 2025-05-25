CREATE TABLE users
(
    id       bigserial    NOT NULL,
    email    varchar(100) NOT NULL,
    name     varchar(100) NOT NULL,
    gender   varchar(10)  NOT NULL,
    birthday DATE         NOT NULL,
    password varchar(255) NOT NULL,
    CONSTRAINT pk_users_id PRIMARY KEY (id),
    CONSTRAINT uc_users_login UNIQUE (email)
);