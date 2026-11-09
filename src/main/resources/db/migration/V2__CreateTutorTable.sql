CREATE TABLE tutor
(
    id         int NOT NULL AUTO_INCREMENT,
    name       varchar(50),
    language_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (language_id) REFERENCES language (id)
);