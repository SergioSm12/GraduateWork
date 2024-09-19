package com.sergio.spring.rest.usuariosvehiculos.app.auth.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;


@OpenAPIDefinition(
        info = @Info(
                title = "API Parqueadero de la Universidad Santo Tomás, Seccional Tunja",
                description = "Esta API facilita la gestión administrativa de un parqueadero, proporcionando funcionalidades para el control de acceso, registro de vehículos, generación de reportes, entre otros",
                contact = @Contact(
                        name = "Sergio Mesa / Departamento TIC USTA Tunja",
                        url = "https://github.com/SergioSm12",
                        email = "sergio.mesa@usantoto.edu.co"
                ),
                version = "1.0.0"
        ),
        servers = {
                @Server(
                        description = "DEV SERVER",
                        url = "http://localhost:8080"
                )
        }
)

public class SwaggerConfig {
}
