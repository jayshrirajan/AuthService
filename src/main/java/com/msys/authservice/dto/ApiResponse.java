package com.msys.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(timeToLive = 10L)
public class ApiResponse<T> implements Serializable {


    private boolean success;
    private String message;
    private Map<String, Object> result;

    public ApiResponse(boolean success, String message, String beanName, Object bean) {
        this.success = success;
        this.message = message;
        this.result = new HashMap<>();
        this.result.put(beanName, bean);
    }

}
