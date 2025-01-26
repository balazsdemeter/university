create sequence semester_seq start with 1 increment by 50;

create table semester (id integer not null, length integer not null, start_date date, semester varchar(255) check (semester in ('SPRING','AUTUMN')), primary key (id));
create table semester_aud (id integer not null, length integer, rev integer not null, revtype smallint, start_date date, semester varchar(255) check (semester in ('SPRING','AUTUMN')), primary key (id, rev));
create table semester_time_table_aud (id integer not null, rev integer not null, revtype smallint, semester_id integer not null, primary key (id, rev, semester_id));

alter table if exists semester_aud add constraint FK8fu4qyj28lwdvcar1xy87wef5 foreign key (rev) references revinfo;
alter table if exists semester_time_table_aud add constraint FKlce3x86p4a0nfm8yq7b4jqrsi foreign key (rev) references revinfo;