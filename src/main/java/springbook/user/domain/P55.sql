-- create table users (
--     id varchar(10) primary key,
--     name varchar(20) not null,
--     password varchar(10) not null
-- )

-- 5장 서비스 추상화에서 수정
create table users (
    id varchar(10) primary key,
    name varchar(20) not null,
    password varchar(10) not null,
    level tinyint not null, -- tinyint 범위 : -128 ~ 127
    login int not null,
    recommend int not null
)