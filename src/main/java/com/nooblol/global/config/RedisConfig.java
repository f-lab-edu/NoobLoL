package com.nooblol.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * Jedis와 Lettuce중 Lettuce를 사용해야 하는 이유 : https://jojoldu.tistory.com/418
 * <p>
 * 설정 잡는데 참고한 Reference
 * <p>
 * https://kitty-geno.tistory.com/135  /  https://www.baeldung.com/spring-data-redis-tutorial
 */
@Configuration
public class RedisConfig {

  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.port}")
  private int port;

  @Value("${spring.redis.password}")
  private String password;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(host);
    redisStandaloneConfiguration.setPort(port);
    redisStandaloneConfiguration.setPassword(password);
    return new LettuceConnectionFactory(redisStandaloneConfiguration);
  }

  /**
   * 기존에 웹상의 사람들의 예제를 보고 일단 그대로 셋팅을 진행하였으나, 나의 경우 Value에 대해서 Dto타입으로 저장이 필요한 경우였슴. 즉 Class Type으로의
   * 저장이 필요한 시점이며 불러올때 역시 클래스타입으로 불러올 예정이었기에 타입에 대해서 수정함.
   * <p>
   * 타입에 대해서 https://loosie.tistory.com/807 해당 포스트의 하단부분의 RedisTemplate에 대한 설정을 재참고 함.
   *
   * @return
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());

    //redisTemplate.setValueSerializer(new StringRedisSerializer());
    GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
    redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);

    return redisTemplate;
  }
}
