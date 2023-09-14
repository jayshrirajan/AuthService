package com.msys.authservice.config;

import com.msys.authservice.dto.UserDto;
import com.msys.authservice.util.CustomRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

@Slf4j
@Configuration
@EnableCaching
@EnableRedisRepositories()
public class RedisConfig {

    private @Value("${spring.redis.host}") String redisHost;
    private @Value("${spring.redis.port}") int redisPort;
    private @Value("${spring.redis.password}") String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() throws Exception{
        RedisProperties properties = properties();
        try {
            RedisStandaloneConfiguration configuration
                    = new RedisStandaloneConfiguration();
            configuration.setHostName(redisHost);
            configuration.setPort(redisPort);
          //  configuration.setPassword(password);
            return new JedisConnectionFactory(configuration);
        }
        catch (Exception e)
        {
            log.error("Exception::: "+e);
            throw new RuntimeException(e);
        }
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() throws Exception {
        return (builder) -> builder
                .withCacheConfiguration("User",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(30)))
                .withCacheConfiguration("Users",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(10)));
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate() throws Exception {
        try {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory());
            template.setKeySerializer(new JdkSerializationRedisSerializer());
            template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
            template.afterPropertiesSet();
            return template;
        }
        catch (Exception e)
        {
            log.error(e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Bean
    @Primary
    public RedisProperties properties()
    {
        return new RedisProperties();
    }
}
