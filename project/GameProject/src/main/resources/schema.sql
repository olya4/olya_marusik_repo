create table player(
id bigserial primary key,
ip varchar(100),
name varchar (100) unique,
points integer not null default 0,
max_wins_count integer not null default 0,
max_loses_count integer not null default 0
);

create table game(
id bigserial primary key,
dataTime varchar (100),
player_first bigint,
player_second bigint,
player_first_shot_count integer not null default 0,
player_second_shot_count integer not null default 0,
seconds_game_time_amount bigint,
foreign key (player_first) references player(id),
foreign key (player_second) references player(id)
);

create table shot(
id bigserial primary key,
dataTime varchar (100),
game bigint,
player_shooter bigint,
player_target bigint,
foreign key (game) references game (id),
foreign key (player_shooter) references player(id),
foreign key (player_target) references player(id)
);

