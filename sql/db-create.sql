
DROP DATABASE IF EXISTS practice8db_1;
CREATE DATABASE practice8db_1;
USE practice8db_1;

CREATE TABLE users (
	id INT AUTO_INCREMENT PRIMARY KEY,
	login VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE teams (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE users_teams (
	user_id INT ,
	team_id INT,
	PRIMARY KEY (user_id, team_id),
	KEY user_id_reference (user_id),
     CONSTRAINT user_id_reference FOREIGN KEY (user_id) REFERENCES
     users (id) ON DELETE CASCADE,
	KEY team_id_reference (team_id),
     CONSTRAINT team_id_reference FOREIGN KEY (team_id) REFERENCES
     teams (id) ON DELETE CASCADE
);

INSERT INTO users VALUES (1, 'ivanov');

INSERT INTO teams VALUES (1, 'teamA');
