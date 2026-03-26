package com.example.nagahoribashi_walk.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class User {

	private Long id;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private String role;
	
	private String displayName;
	
	private LocalDateTime deletedAt;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	private boolean enabled;
}
