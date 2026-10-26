package com.replate.backend.adaptation;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.replate.backend.adaptation.application.AdaptationService;
import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.application.port.RecipeGenerator;
import com.replate.backend.adaptation.domain.AdaptationGoal;

class AdaptationServiceTest {

	@Test
	void shouldDelegateRecipeGeneration() {

		RecipeGenerator generator = mock(RecipeGenerator.class);
		AdaptationService service = new AdaptationService(generator);

		String recipeText = "Pasta with cheese";
		AdaptationGoal goal = AdaptationGoal.VEGAN;

		GeneratedRecipe expected = new GeneratedRecipe(
				"Vegan Pasta",
				List.of("Pasta", "Plant-based cheese"),
				List.of("Cook pasta.", "Add plan-based cheese."),
				List.of("Replaced dairy cheese."),
				List.of());

		when(generator.generate(recipeText, goal)).thenReturn(expected);

		GeneratedRecipe actual = service.adapt(recipeText, goal);

		assertSame(expected, actual);
		verify(generator).generate(recipeText, goal);
	}

}
