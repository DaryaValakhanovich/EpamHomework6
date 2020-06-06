CREATE DATABASE socialNetwork;
USE socialNetwork;

CREATE TABLE users(id BIGINT AUTO_INCREMENT, name VARCHAR(30) NOT NULL, surname VARCHAR(35) NOT NULL, birthday DATE NOT NULL, PRIMARY KEY(id));
CREATE TABLE friendships(user_id1 BIGINT NOT NULL, user_id2 BIGINT NOT NULL, timestamp DATE NOT NULL, PRIMARY KEY (user_id1, user_id2), FOREIGN KEY (user_id1) REFERENCES users(id), FOREIGN KEY (user_id2) REFERENCES users(id));
CREATE TABLE posts(id BIGINT AUTO_INCREMENT, user_id BIGINT NOT NULL, text  VARCHAR(100) NULL, timestamp DATE NOT NULL, PRIMARY KEY (id), FOREIGN KEY (user_id) REFERENCES users(id));
CREATE TABLE likes(user_id BIGINT NOT NULL, post_id BIGINT NOT NULL, timestamp DATE NOT NULL, PRIMARY KEY(user_id, post_id), FOREIGN KEY (user_id) REFERENCES users(id), FOREIGN KEY (post_id) REFERENCES posts(id));