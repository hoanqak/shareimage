create database shareimage;
use shareimage;

create table user(
	id int primary key auto_increment,
	username varchar(100) not null unique,
    password varchar(200) not null,
    email varchar(40) not null,
    active bit default 0,
    createdAt date,
    updatedAt date
);

create table profile(
	id int auto_increment primary key,
    full_name nvarchar(100),
    address nvarchar(200),
    age int,
    sex bit, 	
    birthday date,
    user_id int not null,
    avatar varchar(500),
    foreign key(user_id) references user(id),
    createdAt date,
    updatedAt date
);

create table access_token(
	id int primary key auto_increment,
    accessToken varchar(200) not null,
    user_id int not null,
    constraint FK_User foreign key(user_id) references user(id),
	createdAt date,
    updatedAt date
);
create table image(
	id int primary key auto_increment,
    file_name varchar(100),
    url_image varchar(500),
    size int,
    user_id int,
    foreign key(user_id) references user(id),
    createdAt date,
    updatedAt date
);
