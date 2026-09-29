package ru.classroom.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Содержит настройки для создания и проверки JWT-токенов.
 * @param secret секретный ключ для подписи и проверки JWT-токенов
 * @param expiration срок действия JWT-токена
 */
@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(String secret, Duration expiration) {
    // https://javarush.com/groups/posts/4126-kofe-breyk-230-chto-takoe-zapisi-records-v-java-i-kak-oni-rabotajut
}
