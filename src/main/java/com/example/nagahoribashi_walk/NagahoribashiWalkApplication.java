package com.example.nagahoribashi_walk;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.nagahoribashi_walk.controller.HomeController;
import com.example.nagahoribashi_walk.dto.SpotSummary;
import com.example.nagahoribashi_walk.repository.SpotMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class NagahoribashiWalkApplication implements CommandLineRunner {

    private final HomeController homeController;

    NagahoribashiWalkApplication(HomeController homeController) {
        this.homeController = homeController;
    }

    public static void main(String[] args) {
        SpringApplication.run(NagahoribashiWalkApplication.class, args);
    }

    @Autowired
    private SpotMapper spotMapper;
    
    // アプリ起動時に実行される
    @Override
    public void run(String... args) throws Exception {

        log.info("長堀橋さんぽアプリ起動");

        
        List<SpotSummary> spots = spotMapper.findRecommendedSpots();
        
        for (SpotSummary spot : spots) {
        	System.out.println(spot);
        }
    }
}