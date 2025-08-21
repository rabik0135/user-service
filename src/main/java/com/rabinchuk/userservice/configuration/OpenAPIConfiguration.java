package com.rabinchuk.userservice.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI openAPIDescription() {
        Server localHostServer = new Server();
        localHostServer.setUrl("http://localhost:8080");
        localHostServer.setDescription("Local env");

        Contact contact = new Contact();
        contact.setName("Ivan Rabinchuk");
        contact.setEmail("ivan.rabinchuk@gmail.com");

        Info info = new Info()
                .title("User cards API")
                .version("1.0")
                .contact(contact)
                .description("API for user cards");

        return new OpenAPI().info(info).servers(List.of(localHostServer));
    }
}
