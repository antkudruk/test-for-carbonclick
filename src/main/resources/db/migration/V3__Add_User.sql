create table user (
  user_id bigint not null primary key auto_increment,
  username varchar(256) not null,
  password varchar(256) not null,
  created_at datetime default current_timestamp,
  updated_at datetime default current_timestamp on update current_timestamp,
  unique (username)
);

-- Add a sample user
insert into user (username, password) values ('admin', '$2a$10$aDipcD0hx5janQqNYMqKBe.fBx6TSY8Kvyu4zfyQ0oU4/qs4TK/1O');
