-- auto-generated definition
-- User table stores basic user information; detailed information is stored in user_info table
-- Basic information (username, email, profile picture)
create table users
(
    id        serial
        primary key,
    username  varchar(100) not null
        unique,
    email     varchar(255)
        unique,
    image_url varchar(255)
);

comment on table users is 'User table';
comment on column users.id is 'User ID';
comment on column users.username is 'Username';
comment on column users.email is 'Email';
comment on column users.image_url is 'Profile picture URL';

-- auto-generated definition
create table user_info
(
    user_id       integer                             not null,
    is_delete     boolean   default false,
    created_at    timestamp default CURRENT_TIMESTAMP not null,
    updated_at    timestamp default CURRENT_TIMESTAMP not null,
    organize      varchar(100),
    refresh_token text,
    unique (user_id),
    unique (refresh_token)
);

comment on table user_info is 'User information table';

comment on column user_info.user_id is 'User ID';

comment on column user_info.created_at is 'Creation timestamp';

comment on column user_info.updated_at is 'Update timestamp';

comment on column user_info.organize is 'Organization';

comment on column user_info.refresh_token is 'Refresh token';


-- Third-party login provider table stores information about third-party login providers
-- Initialization required
create table oauth_providers
(
    id            serial
        primary key,
    name          varchar(100)                        not null,
    client_id     varchar(100)                        not null,
    client_secret varchar(100)                        not null,
    auth_url      varchar(255)                        not null,
    token_url     varchar(255)                        not null,
    redirect_uri  varchar(255)                        not null,
    created_at    timestamp default CURRENT_TIMESTAMP not null,
    updated_at    timestamp default CURRENT_TIMESTAMP not null,
    unique (name)
);
comment on table oauth_providers is 'Third-party login provider table';
comment on column oauth_providers.id is 'Third-party login provider ID';
comment on column oauth_providers.name is 'Third-party login provider name';
comment on column oauth_providers.client_id is 'Third-party login provider client ID';
comment on column oauth_providers.client_secret is 'Third-party login provider client secret';
comment on column oauth_providers.auth_url is 'Third-party login provider authorization URL';
comment on column oauth_providers.token_url is 'Third-party login provider token URL';
comment on column oauth_providers.redirect_uri is 'Third-party login provider redirect URI';
comment on column oauth_providers.created_at is 'Creation timestamp';
comment on column oauth_providers.updated_at is 'Update timestamp';

-- User third-party login information table stores user third-party login information for linking third-party and local accounts
create table user_oauth
(
    id               serial
        primary key,
    user_id          int                                 not null,
    provider_id      int                                 not null,
    provider_user_id int                                 not null,
    access_token     text,
    refresh_token    text,
    token_expires_at timestamp default CURRENT_TIMESTAMP,
    created_at       timestamp default CURRENT_TIMESTAMP not null,
    updated_at       timestamp default CURRENT_TIMESTAMP not null,
    userinfo         jsonb,
    unique (provider_id, provider_user_id)
);
comment on table user_oauth is 'User third-party login information table';
comment on column user_oauth.id is 'User third-party login information ID';
comment on column user_oauth.user_id is 'User ID';
comment on column user_oauth.provider_id is 'Third-party login provider ID';
comment on column user_oauth.provider_user_id is 'Third-party login provider user ID';
comment on column user_oauth.access_token is 'Access token';
comment on column user_oauth.refresh_token is 'Refresh token';
comment on column user_oauth.token_expires_at is 'Token expiration time';
comment on column user_oauth.created_at is 'Creation timestamp';
comment on column user_oauth.updated_at is 'Update timestamp';
comment on column user_oauth.userinfo is 'User information';

-- User role table stores user role information
-- Initialization required
create table roles
(
    id          serial
        primary key,
    name        varchar(100)                        not null,
    description text,
    created_at  timestamp default CURRENT_TIMESTAMP not null,
    updated_at  timestamp default CURRENT_TIMESTAMP not null,
    unique (name)
);
comment on table roles is 'Role table';
comment on column roles.id is 'Role ID';
comment on column roles.name is 'Role name';
comment on column roles.description is 'Role description';


-- User permission table stores user permission information
create table user_permissions
(
    id         serial
        primary key,
    user_id    int                                 not null,
    role_id    int                                 not null,
    granted_at timestamp default CURRENT_TIMESTAMP not null,
    revoked_at timestamp
);
comment on table user_permissions is 'User permission table';
comment on column user_permissions.id is 'User permission ID';
comment on column user_permissions.user_id is 'User ID';
comment on column user_permissions.role_id is 'Role ID';
comment on column user_permissions.granted_at is 'Granted timestamp';
comment on column user_permissions.revoked_at is 'Revoked timestamp';
