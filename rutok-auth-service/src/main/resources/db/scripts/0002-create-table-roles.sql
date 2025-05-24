CREATE TABLE roles
(
    id bigserial   NOT NULL,
    name varchar(50) NOT NULL,
    code varchar(50) NOT NULL,
    CONSTRAINT pk_roles_id PRIMARY KEY (id),
    CONSTRAINT uc_roles_code UNIQUE (code)
);
