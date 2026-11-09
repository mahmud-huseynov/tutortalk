CREATE TABLE tutor_language
(
    tutor_id   int NOT NULL,
    language_id int NOT NULL,
    PRIMARY KEY (tutor_id, language_id),
    FOREIGN KEY (tutor_id) REFERENCES tutor (id),
    FOREIGN KEY (language_id) REFERENCES language (id)
);