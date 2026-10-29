
package com.replate.backend.adaptation.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.domain.AdaptationGoal;
import com.replate.backend.adaptation.infrastructure.persistence.AdaptationEntity;
import com.replate.backend.adaptation.infrastructure.persistence.AdaptationRepository;
import com.replate.backend.recipe.infrastructure.persistence.RecipeRepository;

import jakarta.persistence.EntityManager;

@SpringBootTest
class AdaptationPersistenceServiceTest {

	@Autowired
	private AdaptationPersistenceService persistenceService;

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private AdaptationRepository adaptationRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	@Transactional
	void shouldSaveRecipeAndAdaptation() {

		GeneratedRecipe generated = new GeneratedRecipe(
				"Vegan Pasta",
				List.of("Pasta", "Oat cream"),
				List.of("Cook pasta", "Add oat cream"),
				List.of("Replaced dairy cream"),
				List.of());

		UUID id = persistenceService.save(
				"Pasta with dairy cream",
				AdaptationGoal.VEGAN,
				generated);

		entityManager.flush();
		entityManager.clear();

		AdaptationEntity found = adaptationRepository
				.findById(id)
				.orElseThrow();

		assertEquals(AdaptationGoal.VEGAN, found.getGoal());
		assertEquals("Vegan Pasta",
				found.getGeneratedRecipe().title());
		assertEquals("Pasta with dairy cream",
				found.getRecipe().getOriginalText());
	}

	@Test
	void shouldRollbackWhenAdaptationIsInvalid() {

		long recipesBefore = recipeRepository.count();
		long adaptationsBefore = adaptationRepository.count();

		assertThrows(RuntimeException.class, () -> persistenceService.save(
				"Rollback test recipe",
				AdaptationGoal.VEGAN,
				null));

		assertEquals(recipesBefore, recipeRepository.count());
		assertEquals(adaptationsBefore, adaptationRepository.count());
	}
}
