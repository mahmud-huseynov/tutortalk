CREATE TABLE learner
(
    id         int NOT NULL AUTO_INCREMENT,
    name       varchar(50),
    language_id int,
    tutor_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (language_id) REFERENCES language (id),
    FOREIGN KEY (tutor_id) REFERENCES tutor (id)
);