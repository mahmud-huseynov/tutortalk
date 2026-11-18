ALTER TABLE tutor_language
    ADD CONSTRAINT fk_tutor_language_language_id
        FOREIGN KEY (language_id)
            REFERENCES language (id)
            ON DELETE CASCADE;