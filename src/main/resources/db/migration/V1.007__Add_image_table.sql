create sequence image_seq start with 1 increment by 50;
create table image (student_id bigint, id bigint not null, data bytea, primary key (id));
create table image_aud (rev integer not null, revtype smallint, id bigint not null, data bytea, primary key (rev, id));
create table student_image_aud (rev integer not null, revtype smallint, student_id bigint not null, id bigint not null, primary key (rev, student_id, id));

alter table if exists image add constraint FKr3v11sq0fbqjqhb5xenrcob3c foreign key (student_id) references student;
alter table if exists image_aud add constraint FKetc5y2t13bkdk5yuj4eswagd4 foreign key (rev) references revinfo;
alter table if exists student_image_aud add constraint FK57vbhpo86af1sgnb6wifqrsc foreign key (rev) references revinfo;