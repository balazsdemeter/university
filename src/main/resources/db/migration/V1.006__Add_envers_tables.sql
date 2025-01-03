create sequence revinfo_seq start with 1 increment by 50;

create table student_aud
(
    rev                      integer not null,
    revtype                  smallint,
    id                       integer not null,
    name                     varchar(255),
    birth_date               date,
    semester                 integer,
    external_id              integer,
    number_of_free_semesters integer,
    primary key (rev, id)
);

create table teacher_aud
(
    rev        integer not null,
    revtype    smallint,
    id         integer not null,
    name       varchar(255),
    birth_date date,
    primary key (rev, id)
);

create table course_aud
(
    rev     integer not null,
    revtype smallint,
    id      integer not null,
    name    varchar(255),
    primary key (rev, id)
);

create table course_student_aud
(
    rev     integer not null,
    revtype smallint,
    course_id integer,
    student_id integer,
    primary key (rev, course_id, student_id)
);

create table course_teacher_aud
(
    rev     integer not null,
    revtype smallint,
    course_id integer,
    teacher_id integer,
    primary key (rev, course_id, teacher_id)
);

create table revinfo
(
    rev      integer not null,
    revtstmp bigint,
    primary key (rev)
);

alter table if exists student_aud add constraint FK_student_aud_rev foreign key (rev) references revinfo;
alter table if exists teacher_aud add constraint FK_teacher_aud_rev foreign key (rev) references revinfo;
alter table if exists course_aud add constraint FK_course_aud_rev foreign key (rev) references revinfo;
alter table if exists course_student_aud add constraint FK_course_student_aud_rev foreign key (rev) references revinfo;
alter table if exists course_teacher_aud add constraint FK_course_teacher_aud_rev foreign key (rev) references revinfo;
