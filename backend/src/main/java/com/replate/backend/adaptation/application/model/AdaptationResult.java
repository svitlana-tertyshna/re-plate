package com.replate.backend.adaptation.application.model;

import java.util.UUID;

import com.replate.backend.adaptation.domain.AdaptationGoal;

public record AdaptationResult(
		UUID id,
		AdaptationGoal goal,
		GeneratedRecipe recipe) {
}
