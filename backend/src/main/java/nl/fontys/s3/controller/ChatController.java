package nl.fontys.s3.controller;

import nl.fontys.s3.domain.websocket.ChatMessage;
import nl.fontys.s3.persistence.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Chat over REST (history) and STOMP (live messages). The sender of a message is always
 * the authenticated user, never a name the client puts in the payload.
 */
@RestController
@RequestMapping("/messages")
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatController(SimpMessagingTemplate simpMessagingTemplate, ChatMessageRepository chatMessageRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
    }

    @PostMapping("/save")
    public void saveMessage(@RequestBody ChatMessage message, Principal principal) {
        message.setSenderName(principal.getName());
        chatMessageRepository.save(message);
    }

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public ChatMessage receiveMessage(@Payload ChatMessage message, Principal principal) {
        message.setSenderName(principal.getName());
        return message;
    }

    @MessageMapping("/private-message")
    public ChatMessage recMessage(@Payload ChatMessage message, Principal principal) {
        message.setSenderName(principal.getName());
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
        return message;
    }

    /** Returns only the conversations of the logged-in user. */
    @GetMapping("/all")
    public List<ChatMessage> getAllMessages(Principal principal) {
        String username = principal.getName();
        return chatMessageRepository.findBySenderNameOrReceiverName(username, username);
    }
}
