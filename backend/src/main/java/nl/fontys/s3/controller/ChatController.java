package nl.fontys.s3.controller;

import nl.fontys.s3.domain.websocket.ChatMessage;
import nl.fontys.s3.persistence.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void saveMessage(@RequestBody ChatMessage message) {
        chatMessageRepository.save(message);
    }

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public ChatMessage receiveMessage(@Payload ChatMessage message) {
        return message;
    }

    @MessageMapping("/private-message")
    public ChatMessage recMessage(@Payload ChatMessage message) {
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
        return message;
    }

    @GetMapping("/all")
    public List<ChatMessage> getAllMessages(@RequestParam(name = "username") String username) {
        return chatMessageRepository.findBySenderNameOrReceiverName(username, username);
    }
}
