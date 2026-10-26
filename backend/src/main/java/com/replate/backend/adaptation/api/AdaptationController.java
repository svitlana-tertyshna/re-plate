package com.replate.backend.adaptation.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.replate.backend.adaptation.application.AdaptationService;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/adaptations")
public class AdaptationController {

	private final AdaptationService adaptationService;

	public AdaptationController(AdaptationService adaptationService) {
		this.adaptationService = adaptationService;
	}

	@PostMapping
	public ResponseEntity<Map<String, String>> createAdaptation(
			@Valid @RequestBody AdaptationRequestDto request) {
		return ResponseEntity.ok(Map.of(
				"status", adaptationService.getStatus(),
				"goal", request.goal().name()));
	}
}
