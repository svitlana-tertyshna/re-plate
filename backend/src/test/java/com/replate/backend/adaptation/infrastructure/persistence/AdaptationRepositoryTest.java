
package com.replate.backend.adaptation.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.domain.AdaptationGoal;
import com.replate.backend.recipe.infrastructure.persistence.RecipeEntity;
import com.replate.backend.recipe.infrastructure.persistence.RecipeRepository;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
class AdaptationRepositoryTest {

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private AdaptationRepository adaptationRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void shouldSaveAndRetrieveAdaptationWithJsonb() {

		// Arrange: save the original recipe
		RecipeEntity recipe = recipeRepository.saveAndFlush(
				new RecipeEntity("Pasta with dairy cream"));

		GeneratedRecipe generated = new GeneratedRecipe(
				"Vegan Pasta",
				List.of("200 g pasta", "100 ml oat cream"),
				List.of("Cook pasta", "Add oat cream"),
				List.of("Replaced dairy cream"),
				List.of("Demo recipe"));

		AdaptationEntity adaptation = new AdaptationEntity(
				recipe,
				AdaptationGoal.VEGAN,
				generated);

		// Act: save the adaptation
		AdaptationEntity saved = adaptationRepository.saveAndFlush(adaptation);

		UUID savedId = saved.getId();

		// Clear the Hibernate persistence context
		entityManager.clear();

		// Read the adaptation from PostgreSQL
		AdaptationEntity found = adaptationRepository
				.findById(savedId)
				.orElseThrow();

		// Assert: verify the persisted data
		assertNotNull(found.getId());
		assertNotNull(found.getCreatedAt());

		assertEquals(AdaptationGoal.VEGAN, found.getGoal());
		assertEquals(recipe.getId(), found.getRecipe().getId());

		assertEquals("Vegan Pasta",
				found.getGeneratedRecipe().title());

		assertEquals(
				List.of("200 g pasta", "100 ml oat cream"),
				found.getGeneratedRecipe().ingredients());

		assertEquals(
				List.of("Replaced dairy cream"),
				found.getGeneratedRecipe().changes());
	}
}
