CREATE TABLE chat_message (
      id BIGINT NOT NULL AUTO_INCREMENT,
      sender_name VARCHAR(255) NOT NULL,
      receiver_name VARCHAR(255) NOT NULL,
      message TEXT NOT NULL,
      date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      status VARCHAR(50) NOT NULL,
      PRIMARY KEY (id)
);
