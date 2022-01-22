create table users(
	id serial,
	email varchar(255) not null,
	password varchar(255) not null,
	first_name varchar(50) not null,
	last_name varchar(100) not null,
	role varchar(20) not null,
	status varchar(20) default 'ACTIVE',
	PRIMARY KEY(id)

);

insert into users(email, password, first_name, last_name, role)
values
	('tyutterin_yasha@mail.ru', '12345', 'Yasha', 'Tyutterin', 'ADMIN'),
	('yasha20053@yandex.ru', '12345', 'Yasha', 'Tyutterin', 'USER');