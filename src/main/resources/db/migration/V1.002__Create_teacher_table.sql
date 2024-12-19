create sequence teacher_seq start with 1 increment by 50;
create table teacher (id integer not null, name varchar(255), birth_date date, primary key (id));