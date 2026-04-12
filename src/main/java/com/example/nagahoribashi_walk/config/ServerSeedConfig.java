package com.example.nagahoribashi_walk.config;

import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerSeedConfig {

    @Bean("serverStartupSeed")
    public Long serverSeed() {
        // サーバー起動時に一意のIDを生成
        return new SecureRandom().nextLong();
    }
}