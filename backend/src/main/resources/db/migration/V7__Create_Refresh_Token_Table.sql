CREATE TABLE refresh_token (
       id         INT AUTO_INCREMENT PRIMARY KEY,
       token      VARCHAR(255) NOT NULL,
       user_id    INT NOT NULL,
       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
       expires_at DATETIME NOT NULL,
       FOREIGN KEY (user_id) REFERENCES `user` (id),
       UNIQUE (token)
);