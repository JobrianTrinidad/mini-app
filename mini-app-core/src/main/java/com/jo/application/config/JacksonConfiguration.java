package com.jo.application.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addCustomSerializers() {
        return builder -> {
            builder.modules(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            builder.modules(new com.fasterxml.jackson.databind.module.SimpleModule().addSerializer(byte[].class, new com.fasterxml.jackson.databind.ser.std.ByteArraySerializer()));
        };
    }
}
