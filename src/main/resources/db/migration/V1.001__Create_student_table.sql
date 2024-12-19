create sequence student_seq start with 1 increment by 50;
create table student (id integer not null, name varchar(255), birth_date date, semester integer not null, primary key (id));