package com.example.nagahoribashi_walk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.nagahoribashi_walk.entity.Spot;
import com.example.nagahoribashi_walk.service.SpotService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class NagahoribashiWalkApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NagahoribashiWalkApplication.class, args);
    }

    @Autowired
    private SpotService spotService;
    
    // アプリ起動時に実行される
    @Override
    public void run(String... args) throws Exception {

        log.info("長堀橋さんぽアプリ起動");
        
        Spot spot = new Spot();
        spot.setSpotName("テストスポット");
        spot.setAddress("テスト住所");
        spot.setBusinessHours("テスト営業時間");
        spot.setEstimatedBudget("テスト予算");
        spot.setGmapUrl("テストgmapアドレス");
        spot.setClosedDays("テスト定休日");
        spot.setWebsiteUrl("テストアドレス");
        
        
        spotService.addSpot(spot);
        
        
    }
}