create sequence time_table_seq start with 1 increment by 50;

create table time_table (course_id integer, day_of_week smallint check (day_of_week between 0 and 6), end_time time(6), id integer not null, semester_id integer, star_time time(6), primary key (id));
create table time_table_aud (course_id integer, day_of_week smallint check (day_of_week between 0 and 6), end_time time(6), id integer not null, rev integer not null, revtype smallint, semester_id integer, star_time time(6), primary key (id, rev));

alter table if exists time_table_aud add constraint FKf8sk3gqite5mklgtj9is5rjer foreign key (rev) references revinfo;
alter table if exists time_table add constraint FKku33m00dfas0gyci3if6x5ec5 foreign key (course_id) references course;
alter table if exists time_table add constraint FKamlcsjasqrgr1og8xhddlw1qv foreign key (semester_id) references semester;