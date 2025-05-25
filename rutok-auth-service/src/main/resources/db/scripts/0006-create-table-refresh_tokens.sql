create table refresh_tokens
(
    id          bigserial                NOT NULL,
    user_id     bigint                   NOT NULL,
    token       text                     NOT NULL,
    access_jti  uuid                     NOT NULL,
    refresh_jti uuid                     NOT NULL,
    issued_at   timestamp with time zone NOT NULL,
    expired_at  timestamp with time zone NOT NULL,
    CONSTRAINT pk_refresh_tokens_id PRIMARY KEY (id),
    CONSTRAINT fk_refresh_tokens_user_id FOREIGN KEY (user_id) REFERENCES "users" (id) ON DELETE CASCADE
)
