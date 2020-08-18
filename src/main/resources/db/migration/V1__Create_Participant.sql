create table participant (
  participant_id bigint auto_increment primary key ,
  first_name varchar(64) not null ,
  last_name varchar(64) not null,
  email varchar(128) not null
);