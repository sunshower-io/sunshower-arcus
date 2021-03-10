
create domain if not exists inet as text;
create domain if not exists cidr as text;
create domain if not exists macaddr as text;

create table TEST_ENTITY (
  id binary(16) primary key,
  name varchar(200)
);