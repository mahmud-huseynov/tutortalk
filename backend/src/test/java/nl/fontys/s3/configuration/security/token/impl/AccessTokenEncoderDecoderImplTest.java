package nl.fontys.s3.configuration.security.token.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import nl.fontys.s3.configuration.security.token.AccessToken;
import nl.fontys.s3.configuration.security.token.exception.InvalidAccessTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccessTokenEncoderDecoderImplTest {

    private static final String SECRET_KEY = "Y29tcGxleGtleXNob3VsZGJlMzJieXRlc2xvbmdmb3Jqd2tqZHJwZQ==";
    private AccessTokenEncoderDecoderImpl accessTokenEncoderDecoder;

    @BeforeEach
    void setUp() {
        accessTokenEncoderDecoder = new AccessTokenEncoderDecoderImpl(SECRET_KEY);
    }

    @Test
    void encode_shouldReturnValidJwt() {
        AccessToken accessToken = new AccessTokenImpl("user1", 123L, List.of("ROLE_USER"));

        String jwt = accessTokenEncoderDecoder.encode(accessToken);

        assertNotNull(jwt);
        assertFalse(jwt.isEmpty());
    }

    @Test
    void decode_shouldReturnValidAccessToken() {
        AccessToken accessToken = new AccessTokenImpl("user1", 123L, List.of("ROLE_USER"));
        String jwt = accessTokenEncoderDecoder.encode(accessToken);

        AccessToken decodedToken = accessTokenEncoderDecoder.decode(jwt);

        assertNotNull(decodedToken);
        assertEquals(accessToken.getSubject(), decodedToken.getSubject());
        assertEquals(accessToken.getStudentId(), decodedToken.getStudentId());
        assertEquals(accessToken.getRoles(), decodedToken.getRoles());
    }

    @Test
    void decode_withInvalidToken_shouldThrowInvalidAccessTokenException() {
        String invalidToken = "invalidToken";

        assertThrows(InvalidAccessTokenException.class, () -> accessTokenEncoderDecoder.decode(invalidToken));
    }

    @Test
    void decode_withExpiredToken_shouldThrowInvalidAccessTokenException() {
        AccessToken accessToken = new AccessTokenImpl("user1", 123L, List.of("ROLE_USER"));
        String jwt = Jwts.builder()
                .setSubject(accessToken.getSubject())
                .setIssuedAt(Date.from(Instant.now().minus(2, ChronoUnit.MINUTES)))
                .setExpiration(Date.from(Instant.now().minus(1, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)))
                .compact();

        assertThrows(InvalidAccessTokenException.class, () -> accessTokenEncoderDecoder.decode(jwt));
    }
}
