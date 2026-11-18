package nl.fontys.s3.controller;

import nl.fontys.s3.configuration.TestConfig;
import nl.fontys.s3.domain.websocket.ChatMessage;
import nl.fontys.s3.persistence.ChatMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChatController.class)
@Import({TestConfig.class})
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatMessageRepository chatMessageRepository;

    @MockBean
    private SimpMessagingTemplate messagingTemplate; // Mock SimpMessagingTemplate

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void findChatMessages_ReturnsListOfMessages() throws Exception {
        String username = "testUser";
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());

        List<ChatMessage> messages = List.of(
                new ChatMessage(1L, username, "recipient1", "Hello!", currentTime, null),
                new ChatMessage(2L, "sender2", username, "How are you?", currentTime, null)
        );

        // Mock the repository behavior
        when(chatMessageRepository.findBySenderNameOrReceiverName(username, username)).thenReturn(messages);

        mockMvc.perform(get("/messages/all")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].senderName").value(username))
                .andExpect(jsonPath("$.[0].receiverName").value("recipient1"))
                .andExpect(jsonPath("$.[0].message").value("Hello!"))
                .andExpect(jsonPath("$.[1].senderName").value("sender2"))
                .andExpect(jsonPath("$.[1].receiverName").value(username))
                .andExpect(jsonPath("$.[1].message").value("How are you?"));

        // Verify that the repository method was called with the correct username
        verify(chatMessageRepository, times(1)).findBySenderNameOrReceiverName(username, username);
        verifyNoMoreInteractions(chatMessageRepository);
    }
}
