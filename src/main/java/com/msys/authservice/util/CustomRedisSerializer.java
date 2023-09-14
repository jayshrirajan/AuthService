package com.msys.authservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msys.authservice.dto.ApiResponse;
import com.msys.authservice.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;
import java.lang.runtime.ObjectMethods;

//@AllArgsConstructor
public class CustomRedisSerializer implements RedisSerializer<UserDto> {

    private final ObjectMapper objectMapper;

    public CustomRedisSerializer() {
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    @Override
    public byte[] serialize(UserDto o) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serialize object :",e);
        }
    }

    @Override
    public UserDto deserialize(byte[] bytes) throws SerializationException {
        if(bytes == null)
        {
            return null;
        }

        try {
            return objectMapper.readValue(bytes,UserDto.class);
        } catch (IOException e) {
            throw new SerializationException("Error Deserialize object ",e);
        }
    }
}
