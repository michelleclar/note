-- Documents table 
create table documents
(
    id              serial
        primary key,
    title           varchar(255),
    is_archive      boolean   default false             not null,
    parent_document_id integer,
    content         text,
    cover_image     text,
    icon            text,
    is_published    boolean   default false             not null,
    created_at      timestamp default CURRENT_TIMESTAMP not null,
    updated_at      timestamp default CURRENT_TIMESTAMP not null
);

comment on column documents.id is 'Document ID';

comment on column documents.title is 'Document title';

comment on column documents.is_archive is 'Indicates whether the document is archived';

comment on column documents.parent_document_id is 'Parent document ID';

comment on column documents.content is 'Document content';

comment on column documents.cover_image is 'URL or path of the document cover image';

comment on column documents.icon is 'URL or path of the document icon';

comment on column documents.is_published is 'Indicates whether the document is published';

comment on column documents.created_at is 'Creation timestamp';

comment on column documents.updated_at is 'Update timestamp';

create table user_documents
(
    user_id     integer not null,
    document_id integer not null,
    primary key (user_id, document_id)
);

comment on column user_documents.user_id is 'User ID';

comment on column user_documents.document_id is 'Document ID';

