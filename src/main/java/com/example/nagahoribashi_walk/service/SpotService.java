package com.example.nagahoribashi_walk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagahoribashi_walk.dto.SpotSummary;

public interface SpotService {

    Page<SpotSummary> getPage(Pageable pageable);
}
