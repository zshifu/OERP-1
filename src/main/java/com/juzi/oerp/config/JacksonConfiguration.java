package com.juzi.oerp.config;

import com.juzi.oerp.common.jackson.LocalDateTimeDeserializer;
import com.juzi.oerp.common.jackson.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;

/**
 * @author Juzi
 * @date 2020/7/15 09:55
 */
@Configuration
public class JacksonConfiguration {

    /**
     * LocalDateTime 序列化器
     */
    @Autowired
    private LocalDateTimeSerializer localDateTimeSerializer;

    /**
     * LocalDateTime 反序列化器
     */
    @Autowired
    private LocalDateTimeDeserializer localDateTimeDeserializer;

    @Bean
    @Primary
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, localDateTimeSerializer)
                .deserializerByType(LocalDateTime.class, localDateTimeDeserializer);
    }
}
