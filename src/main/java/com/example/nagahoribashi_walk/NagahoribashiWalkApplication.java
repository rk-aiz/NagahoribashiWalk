package com.example.nagahoribashi_walk;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class NagahoribashiWalkApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NagahoribashiWalkApplication.class, args);
    }

    // アプリ起動時に実行される
    @Override
    public void run(String... args) throws Exception {

        log.info("長堀橋さんぽアプリ起動");

        System.out.println();
    }
}