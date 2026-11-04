
package com.replate.backend.adaptation.infrastructure.ai;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.replate.backend.adaptation.application.exception.AiGenerationException;
import com.replate.backend.adaptation.application.model.GeneratedRecipe;

class GeneratedRecipeValidatorTest {

	@Test
	void shouldAcceptValidRecipe() {
		GeneratedRecipe recipe = validRecipe();

		assertDoesNotThrow(() -> GeneratedRecipeValidator.validate(recipe));
	}

	@Test
	void shouldAcceptEmptyWarnings() {
		GeneratedRecipe recipe = new GeneratedRecipe(
				"Vegan Pasta",
				List.of("200 g pasta"),
				List.of("Cook the pasta."),
				List.of("Replaced dairy cream."),
				List.of());

		assertDoesNotThrow(() -> GeneratedRecipeValidator.validate(recipe));
	}

	@Test
	void shouldRejectNullRecipe() {
		assertThrows(AiGenerationException.class, () -> GeneratedRecipeValidator.validate(null));
	}

	@Test
	void shouldRejectBlankTitle() {
		GeneratedRecipe recipe = new GeneratedRecipe(
				"   ",
				List.of("200 g pasta"),
				List.of("Cook the pasta."),
				List.of("Replaced dairy cream."),
				List.of());

		assertThrows(AiGenerationException.class, () -> GeneratedRecipeValidator.validate(recipe));
	}

	@Test
	void shouldRejectEmptyIngredients() {
		GeneratedRecipe recipe = new GeneratedRecipe(
				"Vegan Pasta",
				List.of(),
				List.of("Cook the pasta."),
				List.of("Replaced dairy cream."),
				List.of());

		assertThrows(AiGenerationException.class, () -> GeneratedRecipeValidator.validate(recipe));
	}

	@Test
	void shouldRejectBlankIngredient() {
		GeneratedRecipe recipe = new GeneratedRecipe(
				"Vegan Pasta",
				List.of(" "),
				List.of("Cook the pasta."),
				List.of("Replaced dairy cream."),
				List.of());

		assertThrows(AiGenerationException.class, () -> GeneratedRecipeValidator.validate(recipe));
	}

	@Test
	void shouldRejectMissingInstructions() {
		GeneratedRecipe recipe = new GeneratedRecipe(
				"Vegan Pasta",
				List.of("200 g pasta"),
				null,
				List.of("Replaced dairy cream."),
				List.of());

		assertThrows(AiGenerationException.class, () -> GeneratedRecipeValidator.validate(recipe));
	}

	@Test
	void shouldRejectMissingChanges() {
		GeneratedRecipe recipe = new GeneratedRecipe(
				"Vegan Pasta",
				List.of("200 g pasta"),
				List.of("Cook the pasta."),
				List.of(),
				List.of());

		assertThrows(AiGenerationException.class, () -> GeneratedRecipeValidator.validate(recipe));
	}

	@Test
	void shouldRejectNullWarnings() {
		GeneratedRecipe recipe = new GeneratedRecipe(
				"Vegan Pasta",
				List.of("200 g pasta"),
				List.of("Cook the pasta."),
				List.of("Replaced dairy cream."),
				null);

		assertThrows(AiGenerationException.class, () -> GeneratedRecipeValidator.validate(recipe));
	}

	private GeneratedRecipe validRecipe() {
		return new GeneratedRecipe(
				"Vegan Pasta",
				List.of("200 g pasta", "100 ml oat cream"),
				List.of("Cook the pasta.", "Add oat cream."),
				List.of("Replaced dairy cream with oat cream."),
				List.of("Check the pasta ingredients."));
	}
}
