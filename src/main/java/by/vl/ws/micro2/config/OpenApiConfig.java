package by.vl.ws.micro2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Execution Service API")
                        .version("1.0.0")
                        .description("API для управления асинхронными задачами")
                        .contact(new Contact()
                                .name("Developer")
                                .email("dev@example.com")));
    }
}
