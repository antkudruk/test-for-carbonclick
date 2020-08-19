create table participant (
  participant_id bigint not null primary key auto_increment,
  first_name varchar(64) not null ,
  last_name varchar(64) not null,
  email varchar(128) not null
);