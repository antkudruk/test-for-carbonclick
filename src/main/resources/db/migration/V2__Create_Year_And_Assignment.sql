create table year (
  year_id bigint auto_increment primary key ,
  title varchar(256) not null unique,
  created_at datetime default current_timestamp
);

create table assignment(
  assignment_id bigint auto_increment primary key,
  year_id bigint not null references year(year_id),
  giver_id bigint not null references participant(participant_id),
  taker_id bigint not null references participant(participant_id),
  check (giver_id != taker_id),
  unique (year_id, giver_id),
  unique (year_id, taker_id)
);
