package com.replate.backend.adaptation.api;

import java.util.UUID;

import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.domain.AdaptationGoal;

public record AdaptationResponseDto(
		UUID id,
		AdaptationGoal goal,
		GeneratedRecipe recipe) {
}
