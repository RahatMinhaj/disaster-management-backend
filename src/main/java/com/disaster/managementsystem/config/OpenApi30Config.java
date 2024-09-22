package com.disaster.managementsystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class OpenApi30Config {

    @Bean
    public OpenAPI springOpenAPI() {
        final String securitySchemeName = "Authorization";

        OpenAPI openAPI = new OpenAPI()
                .info(new Info().title("Disaster Management System API documentations").version("v1").description("Welcome to the API documentation for Disaster Management System. Obtain user access token from the \"Login\" for secure access (paste without \"Bearer\"). For queries, contact the backend dev team. Thank you."))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components().addSecuritySchemes(securitySchemeName,
                        new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer")
                                .bearerFormat("JWT")));
        return openAPI;
    }
}
