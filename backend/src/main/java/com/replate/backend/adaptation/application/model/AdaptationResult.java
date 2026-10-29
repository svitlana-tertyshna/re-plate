package com.replate.backend.adaptation.application.model;

import java.util.UUID;

public record AdaptationResult(
		UUID id,
		GeneratedRecipe recipe) {
}
