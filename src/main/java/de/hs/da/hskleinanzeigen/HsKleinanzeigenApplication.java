package de.hs.da.hskleinanzeigen;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@SpringBootApplication
@EnableCaching
public class HsKleinanzeigenApplication {

    public static void main(String[] args) {
        SpringApplication.run(HsKleinanzeigenApplication.class, args);
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("HS Kleinanzeigen")
                        .description("Plattform zum Verwalten von Kleinanzeigen")
                        .version("v2.0.23"));
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
