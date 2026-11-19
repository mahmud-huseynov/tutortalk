package nl.fontys.s3.controller;

import nl.fontys.s3.configuration.TestConfig;
import nl.fontys.s3.domain.websocket.ChatMessage;
import nl.fontys.s3.domain.websocket.Status;
import nl.fontys.s3.persistence.ChatMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChatController.class)
@Import({TestConfig.class})
class ChatControllerTest {

    private static final String USERNAME = "testUser";
    private static final Principal TEST_USER = new UsernamePasswordAuthenticationToken(USERNAME, null, List.of());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChatController chatController;

    @MockBean
    private ChatMessageRepository chatMessageRepository;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void getAllMessages_ReturnsOnlyTheLoggedInUsersMessages() throws Exception {
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        List<ChatMessage> messages = List.of(
                new ChatMessage(1L, USERNAME, "recipient1", "Hello!", currentTime, null),
                new ChatMessage(2L, "sender2", USERNAME, "How are you?", currentTime, null)
        );
        when(chatMessageRepository.findBySenderNameOrReceiverName(USERNAME, USERNAME)).thenReturn(messages);

        // A username in the query string is ignored: the history always belongs to the authenticated user.
        mockMvc.perform(get("/messages/all")
                        .param("username", "someoneElse")
                        .principal(TEST_USER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].senderName").value(USERNAME))
                .andExpect(jsonPath("$.[0].receiverName").value("recipient1"))
                .andExpect(jsonPath("$.[0].message").value("Hello!"))
                .andExpect(jsonPath("$.[1].senderName").value("sender2"))
                .andExpect(jsonPath("$.[1].receiverName").value(USERNAME))
                .andExpect(jsonPath("$.[1].message").value("How are you?"));

        verify(chatMessageRepository, times(1)).findBySenderNameOrReceiverName(USERNAME, USERNAME);
        verifyNoMoreInteractions(chatMessageRepository);
    }

    @Test
    void saveMessage_UsesTheLoggedInUserAsSender() throws Exception {
        mockMvc.perform(post("/messages/save")
                        .principal(TEST_USER)
                        .contentType(APPLICATION_JSON)
                        .content("{\"senderName\":\"someoneElse\",\"receiverName\":\"tutor\",\"message\":\"Hi\",\"status\":\"MESSAGE\"}"))
                .andExpect(status().isOk());

        ArgumentCaptor<ChatMessage> saved = ArgumentCaptor.forClass(ChatMessage.class);
        verify(chatMessageRepository).save(saved.capture());
        assertEquals(USERNAME, saved.getValue().getSenderName());
        assertEquals("tutor", saved.getValue().getReceiverName());
    }

    @Test
    void privateMessage_IsSentToTheReceiverWithTheAuthenticatedSender() {
        ChatMessage message = new ChatMessage(null, "someoneElse", "tutor", "Hi", null, Status.MESSAGE);

        ChatMessage result = chatController.recMessage(message, TEST_USER);

        assertEquals(USERNAME, result.getSenderName());
        verify(messagingTemplate).convertAndSendToUser("tutor", "/private", message);
    }

    @Test
    void publicMessage_GetsTheAuthenticatedSender() {
        ChatMessage message = new ChatMessage(null, "someoneElse", null, "Hello everyone", null, Status.MESSAGE);

        assertEquals(USERNAME, chatController.receiveMessage(message, TEST_USER).getSenderName());
    }
}
