package com.lms.lmssystem.config;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
