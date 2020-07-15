package com.juzi.oerp.config;

import com.juzi.oerp.common.jackson.JacksonLocalDateTimeDeserializer;
import com.juzi.oerp.common.jackson.JacksonLocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    private JacksonLocalDateTimeSerializer jacksonLocalDateTimeSerializer;

    /**
     * LocalDateTime 反序列化器
     */
    @Autowired
    private JacksonLocalDateTimeDeserializer jacksonLocalDateTimeDeserializer;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, jacksonLocalDateTimeSerializer)
                .deserializerByType(LocalDateTime.class, jacksonLocalDateTimeDeserializer);
    }
}
