package com.msys.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash()
public class UserDto implements Serializable {

    String id;
    String firstName;

    String lastName;

    String username;

    String email;

    Boolean enabled;

    List<Credentials> credentials;

}
