package com.example.democoffee.service.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Example Coffee Machine")
                                .version("1.0.3")
                                .contact(
                                        new Contact()
                                                .email("kopernik.forwaror@gmail.com")
                                                .url("https://github.com/Alex91482/test_tasks-")
                                                .name("Alex K")
                                )
                );
    }
}
