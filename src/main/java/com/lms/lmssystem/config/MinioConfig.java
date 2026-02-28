package com.lms.lmssystem.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${spring.minio.url}")
    private String url;

    @Value("${spring.minio.user}")
    private String user;

    @Value("${spring.minio.password}")
    private String password;

    @Bean
    public MinioClient minioClient(){
        return MinioClient
                .builder()
                .endpoint(url)
                .credentials(user, password)
                .build();
    }




}
