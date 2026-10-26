package com.replate.backend.adaptation.api;

import com.replate.backend.adaptation.domain.AdaptationGoal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdaptationRequestDto(
		@NotBlank @Size(max = 10_000) String recipeText,

		@NotNull AdaptationGoal goal) {
}
