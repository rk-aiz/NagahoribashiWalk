package com.example.nagahoribashi_walk.dto;

import java.util.List;

public record SaveImagesResult(List<String> savedFilenames, List<String> errors) {}
