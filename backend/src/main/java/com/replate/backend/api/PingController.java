package com.replate.backend.api;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class PingController {

	@GetMapping("/api/v1/ping")
	public Map<String, String> ping() {
		return Map.of("message", "RePlate API is running");
	}

}
