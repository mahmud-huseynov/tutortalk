package nl.fontys.s3.configuration.security.token.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class AccessTokenImplTest {

    private AccessTokenImpl accessToken;

    @BeforeEach
    void setUp() {
        accessToken = new AccessTokenImpl("subject", 123L, List.of("ROLE_USER"));
    }

    @Test
    void constructor_withNullRoles_shouldInitializeEmptySet() {
        AccessTokenImpl accessToken = new AccessTokenImpl("subject", 123L, null);
        assertNotNull(accessToken.getRoles());
        assertTrue(accessToken.getRoles().isEmpty());
    }

    @Test
    void constructor_withNonNullRoles_shouldInitializeWithRoles() {
        assertNotNull(accessToken.getRoles());
        assertFalse(accessToken.getRoles().isEmpty());
        assertTrue(accessToken.getRoles().contains("ROLE_USER"));
    }

    @Test
    void equals_sameInstance_shouldReturnTrue() {
        assertEquals(accessToken, accessToken);
    }

    @Test
    void equals_differentInstanceWithSameValues_shouldReturnTrue() {
        AccessTokenImpl accessToken2 = new AccessTokenImpl("subject", 123L, List.of("ROLE_USER"));
        assertEquals(accessToken, accessToken2);
    }

    @Test
    void equals_differentInstanceWithDifferentSubject_shouldReturnFalse() {
        AccessTokenImpl accessToken2 = new AccessTokenImpl("differentSubject", 123L, List.of("ROLE_USER"));
        assertNotEquals(accessToken, accessToken2);
    }

    @Test
    void equals_differentInstanceWithDifferentStudentId_shouldReturnFalse() {
        AccessTokenImpl accessToken2 = new AccessTokenImpl("subject", 456L, List.of("ROLE_USER"));
        assertNotEquals(accessToken, accessToken2);
    }

    @Test
    void equals_differentInstanceWithDifferentRoles_shouldReturnFalse() {
        AccessTokenImpl accessToken2 = new AccessTokenImpl("subject", 123L, List.of("ROLE_ADMIN"));
        assertNotEquals(accessToken, accessToken2);
    }

    @Test
    void equals_differentInstanceWithNullRoles_shouldReturnFalse() {
        AccessTokenImpl accessToken2 = new AccessTokenImpl("subject", 123L, null);
        assertNotEquals(accessToken, accessToken2);
    }

    @Test
    void equals_nullObject_shouldReturnFalse() {
        assertNotEquals(null, accessToken);
    }

    @Test
    void equals_differentTypeObject_shouldReturnFalse() {
        assertNotEquals("notAccessToken", accessToken);
    }

    @Test
    void hashCode_sameInstance_shouldReturnSameHashCode() {
        assertEquals(accessToken.hashCode(), accessToken.hashCode());
    }

    @Test
    void hashCode_differentInstanceWithSameValues_shouldReturnSameHashCode() {
        AccessTokenImpl accessToken2 = new AccessTokenImpl("subject", 123L, List.of("ROLE_USER"));
        assertEquals(accessToken.hashCode(), accessToken2.hashCode());
    }
}
