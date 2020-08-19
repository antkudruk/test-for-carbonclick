create table year (
  year_id bigint not null primary key auto_increment,
  title varchar(256) not null unique,
  created_at datetime default current_timestamp
);

create table assignment(
  assignment_id bigint not null primary key auto_increment,
  year_id bigint not null references year(year_id),
  giver_id bigint not null,
  taker_id bigint not null,
  foreign key (giver_id) references participant(participant_id),
  foreign key (taker_id) references participant(participant_id),
  check (giver_id != taker_id),
  unique (year_id, giver_id),
  unique (year_id, taker_id)
);
