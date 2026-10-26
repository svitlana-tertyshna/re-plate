package com.replate.backend.adaptation.api;

import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.domain.AdaptationGoal;

public record AdaptationResponseDto(
		AdaptationGoal goal,
		GeneratedRecipe recipe) {
}
