package com.lms.lmssystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI lmsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LMS System API")
                        .description("API для системы управления обучением (LMS)")
                        .version("1.0.0"));
    }
}
