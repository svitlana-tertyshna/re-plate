package com.replate.backend.adaptation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.replate.backend.adaptation.application.AdaptationService;
import com.replate.backend.adaptation.application.model.GeneratedRecipe;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/adaptations")
public class AdaptationController {

	private final AdaptationService adaptationService;

	public AdaptationController(AdaptationService adaptationService) {
		this.adaptationService = adaptationService;
	}

	@PostMapping
	public ResponseEntity<AdaptationResponseDto> adapt(
			@Valid @RequestBody AdaptationRequestDto request) {

		GeneratedRecipe recipe = adaptationService.adapt(
				request.recipeText(),
				request.goal());

		AdaptationResponseDto response = new AdaptationResponseDto(
				request.goal(),
				recipe);

		return ResponseEntity.ok(response);
	}
}
