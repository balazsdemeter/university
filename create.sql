create sequence course_seq start with 1 increment by 50;
create sequence student_seq start with 1 increment by 50;
create sequence teacher_seq start with 1 increment by 50;
create table course (id integer not null, name varchar(255), primary key (id));
create table course_student (course_id integer not null, student_id integer not null, primary key (course_id, student_id));
create table course_teacher (course_id integer not null, teacher_id integer not null, primary key (course_id, teacher_id));
create table student (birth_date date, id integer not null, semester integer not null, name varchar(255), primary key (id));
create table teacher (birth_date date, id integer not null, name varchar(255), primary key (id));
alter table if exists course_student add constraint FKlmj50qx9k98b7li5li74nnylb foreign key (course_id) references course;
alter table if exists course_student add constraint FK4xxxkt1m6afc9vxp3ryb0xfhi foreign key (student_id) references student;
alter table if exists course_teacher add constraint FKrna6ik293g84mo3rslnkk7m1a foreign key (course_id) references course;
alter table if exists course_teacher add constraint FKsb9lnio7h0a885b871ogw0ajg foreign key (teacher_id) references teacher;