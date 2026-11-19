package nl.fontys.s3.configuration.websocket;

import nl.fontys.s3.configuration.security.token.AccessToken;
import nl.fontys.s3.configuration.security.token.AccessTokenDecoder;
import nl.fontys.s3.configuration.security.token.exception.InvalidAccessTokenException;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebSocketAuthInterceptorTest {

    private final AccessTokenDecoder accessTokenDecoder = mock(AccessTokenDecoder.class);
    private final MessageChannel channel = mock(MessageChannel.class);
    private final WebSocketAuthInterceptor interceptor = new WebSocketAuthInterceptor(accessTokenDecoder);

    private static final Principal EMMA = new UsernamePasswordAuthenticationToken("emma", null, List.of());

    private static Message<byte[]> frame(StompCommand command, Consumer<StompHeaderAccessor> setup) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(command);
        setup.accept(accessor);
        accessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }

    @Test
    void connect_WithValidToken_SetsTheUser() {
        AccessToken token = mock(AccessToken.class);
        when(token.getSubject()).thenReturn("emma");
        when(token.getRoles()).thenReturn(Set.of("LEARNER"));
        when(accessTokenDecoder.decode("valid-token")).thenReturn(token);
        Message<byte[]> connect = frame(StompCommand.CONNECT, a -> a.addNativeHeader("Authorization", "Bearer valid-token"));

        Message<?> result = interceptor.preSend(connect, channel);

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(result, StompHeaderAccessor.class);
        assertNotNull(accessor);
        assertEquals("emma", accessor.getUser().getName());
    }

    @Test
    void connect_WithoutToken_IsRejected() {
        Message<byte[]> connect = frame(StompCommand.CONNECT, a -> { });

        assertThrows(AccessDeniedException.class, () -> interceptor.preSend(connect, channel));
    }

    @Test
    void connect_WithInvalidToken_IsRejected() {
        when(accessTokenDecoder.decode("bad-token")).thenThrow(new InvalidAccessTokenException("expired"));
        Message<byte[]> connect = frame(StompCommand.CONNECT, a -> a.addNativeHeader("Authorization", "Bearer bad-token"));

        assertThrows(AccessDeniedException.class, () -> interceptor.preSend(connect, channel));
    }

    @Test
    void send_WithoutAuthenticatedUser_IsRejected() {
        Message<byte[]> send = frame(StompCommand.SEND, a -> a.setDestination("/app/private-message"));

        assertThrows(AccessDeniedException.class, () -> interceptor.preSend(send, channel));
    }

    @Test
    void send_WithAuthenticatedUser_IsAllowed() {
        Message<byte[]> send = frame(StompCommand.SEND, a -> { a.setDestination("/app/private-message"); a.setUser(EMMA); });

        assertSame(send, interceptor.preSend(send, channel));
    }

    @Test
    void subscribe_ToOwnPrivateDestination_IsAllowed() {
        Message<byte[]> subscribe = frame(StompCommand.SUBSCRIBE, a -> { a.setDestination("/user/emma/private"); a.setUser(EMMA); });

        assertSame(subscribe, interceptor.preSend(subscribe, channel));
    }

    @Test
    void subscribe_ToAnotherUsersPrivateDestination_IsRejected() {
        Message<byte[]> subscribe = frame(StompCommand.SUBSCRIBE, a -> { a.setDestination("/user/lucas/private"); a.setUser(EMMA); });

        assertThrows(AccessDeniedException.class, () -> interceptor.preSend(subscribe, channel));
    }

    @Test
    void subscribe_ToPublicChatroom_IsAllowed() {
        Message<byte[]> subscribe = frame(StompCommand.SUBSCRIBE, a -> { a.setDestination("/chatroom/public"); a.setUser(EMMA); });

        assertSame(subscribe, interceptor.preSend(subscribe, channel));
    }

    @Test
    void disconnect_PassesThrough() {
        Message<byte[]> disconnect = frame(StompCommand.DISCONNECT, a -> { });

        assertSame(disconnect, interceptor.preSend(disconnect, channel));
    }
}
