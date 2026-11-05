package com.replate.backend.adaptation.infrastructure.ai;

import java.util.List;

import com.replate.backend.adaptation.application.exception.AiGenerationException;
import com.replate.backend.adaptation.application.model.GeneratedRecipe;

final class GeneratedRecipeValidator {

	private GeneratedRecipeValidator() {
	}

	static void validate(GeneratedRecipe recipe) {
		if (recipe == null) {
			throw new AiGenerationException(
					"AI returned no generated recipe");
		}

		if (recipe.title() == null || recipe.title().isBlank()
				|| hasInvalidItems(recipe.ingredients())
				|| hasInvalidItems(recipe.instructions())
				|| hasInvalidItems(recipe.changes())
				|| recipe.warnings() == null) {

			throw new AiGenerationException(
					"AI returned an incomplete recipe");
		}
	}

	private static boolean hasInvalidItems(List<String> items) {
		return items == null
				|| items.isEmpty()
				|| items.stream().anyMatch(
						item -> item == null || item.isBlank());
	}
}
