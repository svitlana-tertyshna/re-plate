package com.replate.backend.adaptation.application.model;

import java.util.List;

public record GeneratedRecipe(
		String title,
		List<String> ingredients,
		List<String> instructions,
		List<String> changes,
		List<String> warnings) {

}
