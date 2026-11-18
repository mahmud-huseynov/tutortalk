CREATE TABLE tutor
(
    id         int NOT NULL AUTO_INCREMENT,
    name       varchar(50) NOT NULL,
    language_id int,
    email VARCHAR(255) NOT NULL,
    number VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (language_id) REFERENCES language (id)
);