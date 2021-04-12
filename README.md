# tweet-app-

create database tweetapp;


use tweetapp;


show tables;


CREATE TABLE users (

    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    gender varchar(255) NOT NULL,
    date_of_birth varchar(255),
    email varchar(255) ,
    password varchar(255) NOT NULL,
    logged_in varchar(255) default 'false',
    primary key(email)
);


select * from users;


select * from tweets;


create table tweets (
 tweet varchar(255),
 email varchar(255),
 foreign key (email) references users(email)
);
