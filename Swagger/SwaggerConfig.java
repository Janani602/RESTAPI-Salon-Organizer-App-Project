package com.demo.project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Salon Organizer API",
        version = "1.0",
        description = "API documentation for Salon Organizer application"
    )
)
public class SwaggerConfig {
}
