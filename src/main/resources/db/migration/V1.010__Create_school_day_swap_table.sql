create sequence school_day_swap_seq start with 1 increment by 50;

create table school_day_swap (id integer not null, school_day date, swapped_day date, primary key (id));
create table school_day_swap_aud (id integer not null, rev integer not null, revtype smallint, school_day date, swapped_day date, primary key (id, rev));

alter table if exists school_day_swap_aud add constraint FKgelofef57wo9mkdoh8s8j5tow foreign key (rev) references revinfo;