package com.replate.backend.adaptation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.replate.backend.adaptation.application.AdaptationPersistenceService;
import com.replate.backend.adaptation.application.AdaptationService;
import com.replate.backend.adaptation.application.model.AdaptationResult;
import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.application.port.RecipeGenerator;
import com.replate.backend.adaptation.domain.AdaptationGoal;

class AdaptationServiceTest {

	@Test
	void shouldDelegateRecipeGeneration() {

		RecipeGenerator generator = mock(RecipeGenerator.class);
		AdaptationPersistenceService persistenceService = mock(AdaptationPersistenceService.class);

		AdaptationService service = new AdaptationService(generator, persistenceService);

		String recipeText = "Pasta with cheese";
		AdaptationGoal goal = AdaptationGoal.VEGAN;

		GeneratedRecipe expected = new GeneratedRecipe(
				"Vegan Pasta",
				List.of("Pasta", "Plant-based cheese"),
				List.of("Cook pasta.", "Add plan-based cheese."),
				List.of("Replaced dairy cheese."),
				List.of());

		UUID savedId = UUID.randomUUID();

		when(persistenceService.save(recipeText, goal, expected))
				.thenReturn(savedId);

		when(generator.generate(recipeText, goal)).thenReturn(expected);

		AdaptationResult actual = service.adapt(recipeText, goal);

		assertEquals(savedId, actual.id());
		assertSame(expected, actual.recipe());
		verify(generator).generate(recipeText, goal);
		verify(persistenceService).save(recipeText, goal, expected);
	}

}
