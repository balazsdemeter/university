create table university_user (password varchar(255), username varchar(255) not null, primary key (username));
create table university_user_roles (university_user_username varchar(255) not null, roles varchar(255));
alter table if exists university_user_roles add constraint FKj9jifhxemic2jh6a7wxcefki4 foreign key (university_user_username) references university_user;
