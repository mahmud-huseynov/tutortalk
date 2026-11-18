// ChatMessage.java
package nl.fontys.s3.domain.websocket;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String senderName;
    private String receiverName;
    private String message;
    private Timestamp date;
    @Enumerated(EnumType.STRING)
    private Status status;
}
