
package com.replate.backend.recipe.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
class RecipeRepositoryTest {

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void shouldSaveAndFindRecipe() {

		// Arrange
		RecipeEntity recipe = new RecipeEntity("Pasta with tomato sauce");

		// Act: save the recipe to PostgreSQL
		RecipeEntity saved = recipeRepository.saveAndFlush(recipe);
		UUID savedId = saved.getId();

		// Clear Hibernate's in-memory persistence context
		entityManager.clear();

		// Retrieve the recipe from PostgreSQL
		RecipeEntity found = recipeRepository
				.findById(savedId)
				.orElseThrow();

		// Assert
		assertNotNull(savedId);
		assertNotNull(found.getCreatedAt());
		assertEquals(
				"Pasta with tomato sauce",
				found.getOriginalText());
	}
}
