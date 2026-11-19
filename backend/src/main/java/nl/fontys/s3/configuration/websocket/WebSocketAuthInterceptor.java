package nl.fontys.s3.configuration.websocket;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.configuration.security.token.AccessToken;
import nl.fontys.s3.configuration.security.token.AccessTokenDecoder;
import nl.fontys.s3.configuration.security.token.exception.InvalidAccessTokenException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

/**
 * Secures the STOMP connection: a client must send a valid JWT in the CONNECT frame,
 * may only send messages once connected, and may only subscribe to its own private destination.
 */
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USER_DESTINATION_PREFIX = "/user/";

    private final AccessTokenDecoder accessTokenDecoder;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }
        StompCommand command = accessor.getCommand();
        if (command == StompCommand.CONNECT) {
            accessor.setUser(authenticate(accessor.getFirstNativeHeader("Authorization")));
        } else if (command == StompCommand.SUBSCRIBE) {
            checkSubscription(accessor);
        } else if (command == StompCommand.SEND) {
            requireUser(accessor);
        }
        return message;
    }

    private Principal authenticate(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new AccessDeniedException("Missing access token");
        }
        AccessToken accessToken;
        try {
            accessToken = accessTokenDecoder.decode(authorizationHeader.substring(BEARER_PREFIX.length()));
        } catch (InvalidAccessTokenException e) {
            throw new AccessDeniedException("Invalid access token");
        }
        List<SimpleGrantedAuthority> authorities = accessToken.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
        return new UsernamePasswordAuthenticationToken(accessToken.getSubject(), null, authorities);
    }

    private Principal requireUser(StompHeaderAccessor accessor) {
        Principal user = accessor.getUser();
        if (user == null) {
            throw new AccessDeniedException("Not authenticated");
        }
        return user;
    }

    private void checkSubscription(StompHeaderAccessor accessor) {
        Principal user = requireUser(accessor);
        String destination = accessor.getDestination();
        if (destination != null && destination.startsWith(USER_DESTINATION_PREFIX)
                && !destination.startsWith(USER_DESTINATION_PREFIX + user.getName() + "/")) {
            throw new AccessDeniedException("Cannot subscribe to another user's messages");
        }
    }
}
