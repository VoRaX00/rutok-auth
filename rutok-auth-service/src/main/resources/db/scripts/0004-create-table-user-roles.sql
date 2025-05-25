CREATE TABLE "users_roles"
(
    id      bigserial NOT NULL,
    role_id bigint NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT pk_user_role_id PRIMARY KEY (id),
    CONSTRAINT fk_user_role_role_id FOREIGN KEY (role_id) REFERENCES "roles" (id),
    CONSTRAINT fk_user_role_user_id FOREIGN KEY (user_id) REFERENCES "users" (id),
    CONSTRAINT uc_user_role UNIQUE (user_id, role_id)
);