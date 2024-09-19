package com.sergio.spring.rest.usuariosvehiculos.app.auth.config;

import com.sergio.spring.rest.usuariosvehiculos.app.auth.dto.AuthLoginRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.auth.dto.AuthResponse;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .paths(new Paths().addPathItem("/login", new PathItem()
                        .post(new Operation()
                                .summary("Login User")
                                .description("Authenticate a user and return the authentication token")
                                .addTagsItem("Authentication")
                                .requestBody(new RequestBody()
                                        .content(new Content()
                                                .addMediaType("application/json", new MediaType()
                                                        .schema(new Schema<AuthLoginRequest>()
                                                                .example(new AuthLoginRequest("admin@usantoto.edu.co", "admin"))))))
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse()
                                                .description("Successful authentication")
                                                .content(new Content()
                                                        .addMediaType("application/json", new MediaType()
                                                                .schema(new Schema<AuthResponse>()
                                                                        .example(new AuthResponse("Token generado con éxito", "admin@usantoto.edu.co", "")))))))
                        )));
    }
}
