package nl.fontys.s3.configuration;

import nl.fontys.s3.configuration.security.token.AccessTokenDecoder;
import nl.fontys.s3.persistence.ChatMessageRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public AccessTokenDecoder accessTokenDecoder() {
        return mock(AccessTokenDecoder.class);
    }

    @Bean
    @Primary
    public ChatMessageRepository chatMessageRepository() {
        return Mockito.mock(ChatMessageRepository.class);
    }
}
