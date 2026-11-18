package nl.fontys.s3.configuration.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SwaggerConfigurationTest {

    @Test
    void customizeOpenAPI_shouldAddBearerAuthSecurityScheme() {
        SwaggerConfiguration swaggerConfiguration = new SwaggerConfiguration();

        OpenAPI openAPI = swaggerConfiguration.customizeOpenAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getComponents());

        Components components = openAPI.getComponents();
        assertTrue(components.getSecuritySchemes().containsKey("bearerAuth"));

        SecurityScheme securityScheme = components.getSecuritySchemes().get("bearerAuth");
        assertEquals("bearer", securityScheme.getScheme());
        assertEquals("JWT", securityScheme.getBearerFormat());

        assertNotNull(openAPI.getSecurity());
        assertFalse(openAPI.getSecurity().isEmpty());
        SecurityRequirement securityRequirement = openAPI.getSecurity().get(0);
        assertTrue(securityRequirement.containsKey("bearerAuth"));
    }
}
