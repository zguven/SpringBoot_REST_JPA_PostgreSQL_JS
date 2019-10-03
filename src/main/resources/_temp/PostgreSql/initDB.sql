--Use app.properties ddlauto=create
or

(--Drop DATABASE resttest;

--CONNECT database as EMPTY:
--jdbc:postgresql://localhost:5432/
--DbName: Lowercase!
CREATE DATABASE resttest
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

--ADD?
    LC_COLLATE = 'Turkish_Turkey.1254'
    LC_CTYPE = 'Turkish_Turkey.1254'

--Drop Table public.User;

Create Table public.User (
 Id  bigserial NOT NULL,
 Code VARCHAR(30) NOT NULL,
 Name VARCHAR(30) NOT NULL,
 Surname VARCHAR(30) NOT NULL,
 Password VARCHAR(100) NOT NULL,
 Phone VARCHAR(20),
 Status Int2 NOT NULL,
 primary key(Id)
);


Alter table public.User
 add constraint UIdx_User_Code unique(Code);



/* Error: (org.postgresql.util.PSQLException)HATA (ERROR): "user_id_seq" nesnesi zaten mevcut
CREATE SEQUENCE public.user_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
*/

ALTER SEQUENCE public.user_id_seq
    OWNER TO postgres;


/*INFO: Columns are in lowercase:
user Table Columns:
===================
id BIGSERIAL NOT NULL,
code VARCHAR(30) NOT NULL,
name VARCHAR(30) NOT NULL,
surname VARCHAR(30) NOT NULL,
password VARCHAR(100) NOT NULL,
phone VARCHAR(20),
status INT2 NOT NULL

user Table Indexes:
===================
index uidx_user_code on user(code)
index user_pkey on user(id)
*/



--EXTRA:
    create table public.user_login (
        id  bigserial not null,
        creation_time timestamp not null,
        creation_user int8 not null,
        revision_time timestamp not null,
        revision_user int8,
        error_message varchar(2000),
        login_ip varchar(50),
        login_time timestamp not null,
        logout_time timestamp,
        user_name varchar(255) not null,
        user_id int8,
        primary key (id)
    )


    create table public.Role (
        id int8 not null,
        creation_time timestamp not null,
        creation_user int8 not null,
        revision_time timestamp not null,
        revision_user int8,
        status int4 not null,
        definition varchar(500) not null,
        name varchar(100) not null,
        role_type int4 not null,
        primary key (id)
    )

    create table public.Permission (
        id  bigserial not null,
        creation_time timestamp not null,
        creation_user int8 not null,
        revision_time timestamp not null,
        revision_user int8,
        name varchar(100) not null,
        path varchar(500) not null,
        primary key (id)
    )


    create table public.User_Role (
        id  bigserial not null,
        creation_time timestamp not null,
        creation_user int8 not null,
        revision_time timestamp not null,
        revision_user int8,
        role_id int8 not null,
        user_id int8 not null,
        primary key (id)
    )


    create table public.Role_Permission (
        id  bigserial not null,
        creation_time timestamp not null,
        creation_user int8 not null,
        revision_time timestamp not null,
        revision_user int8,
        permission_id int8 not null,
        role_id int8 not null,
        primary key (id)
    )
    
    
-->
INFO:
    alter table public.Role_Permission 
        add constraint UK80t4kqgjgn4a86hqko28tf854 unique (permission_id, role_id)

    alter table public.User_Role 
        add constraint UKd82iy0uww8p3jnk98lwy7hg6n unique (user_id, role_id)

