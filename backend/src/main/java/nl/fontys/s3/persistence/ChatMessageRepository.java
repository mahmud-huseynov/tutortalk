// ChatMessageRepository.java
package nl.fontys.s3.persistence;

import nl.fontys.s3.domain.websocket.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderNameOrReceiverName(String senderName, String receiverName);
}
