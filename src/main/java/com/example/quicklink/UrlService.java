package com.example.quicklink;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UrlService {

//    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisSerializer stringRedisSerializer;
    private final String baseUrl = "http://3.135.176.30:30080/api/url/";
    private final Map<String, Object> map = new HashMap<>();
//    private final RedisConfig redisConfig;

    public UrlService(/*RedisTemplate<String, Object> redisTemplate, RedisConfig redisConfig,*/) {
//        this.redisTemplate = redisTemplate;
        this.stringRedisSerializer = new StringRedisSerializer();
//        this.redisConfig = redisConfig;
    }

    public UrlDto shortenUrl(UrlDto urlDto) {
        // Generate short code
        String shortCode = generateShortCode();

        // Create and save URL
        Url url = new Url();
        url.setOriginalUrl(urlDto.getOriginalUrl());
        url.setShortCode(shortCode);
         // Default 24h

        map.put(shortCode, url);

        // Return DTO
        UrlDto response = new UrlDto();
        response.setOriginalUrl(url.getOriginalUrl());
        response.setShortUrl(baseUrl + shortCode);
//        response.setExpirationDate(url.getExpiresAt());
        return response;
    }

    public Url getUrl(String shortCode) {
        return (Url) map.get(shortCode);
    }

    public void incrementClickCount(String shortCode) {
        Url url = getUrl(shortCode);
        if (url != null) {
            url.setClicks(url.getClicks() + 1);
            map.put(shortCode, url);
        }
    }

    private String generateShortCode() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 6); // Simple implementation
    }

    public List<Url> getAllUrl() {
//        Set<String> keys = redisTemplate.keys("*");

        List<Url> urls = new ArrayList<>();
//        for (String key : keys) {
//            if (key.length() == 6) {
//                Url url = (Url) redisTemplate.opsForValue().get(key);
//                if (url != null) {
//                    urls.add(url);
//                }
//            }
//        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            urls.add((Url) entry.getValue());
        }
        return urls;
    }
}
