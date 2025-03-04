package ru.blaskowitz.java.test.task.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI testTaskOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                              .title("User CRUD API")
                              .description("API для управление пользователями.")
                              .version("1.0.0")
                              .contact(new Contact()
                                               .name("Blaskowitz")
                                               .url("https://t.me/Iswkk")))
                .components(new Components()
                                    .addSecuritySchemes("bearer-key", new SecurityScheme()
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")))
        .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }
}
