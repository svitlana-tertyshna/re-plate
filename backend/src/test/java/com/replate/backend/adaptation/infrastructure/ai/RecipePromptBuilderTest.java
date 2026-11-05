
package com.replate.backend.adaptation.infrastructure.ai;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.replate.backend.adaptation.domain.AdaptationGoal;

class RecipePromptBuilderTest {

	@Test
	void shouldBuildVeganPrompt() {
		String prompt = RecipePromptBuilder.buildSystemPrompt(
				AdaptationGoal.VEGAN);

		assertTrue(prompt.contains("Goal: VEGAN"));

		assertTrue(prompt.contains("specific plant-based ingredients"));
		assertTrue(prompt.contains("animal-derived components"));
		assertTrue(prompt.contains("egg-free pasta"));

		assertFalse(prompt.contains("Goal: CALORIE_CONSCIOUS"));
	}

	@Test
	void shouldBuildCalorieConsciousPrompt() {
		String prompt = RecipePromptBuilder.buildSystemPrompt(
				AdaptationGoal.CALORIE_CONSCIOUS);

		assertTrue(prompt.contains("Goal: CALORIE_CONSCIOUS"));
		assertTrue(prompt.contains("Reduce calorie density"));
		assertTrue(prompt.contains("Do not claim exact calorie reductions"));

		assertFalse(prompt.contains("Goal: VEGAN"));
	}

	@Test
	void shouldIncludeCommonRulesForBothGoals() {
		for (AdaptationGoal goal : AdaptationGoal.values()) {
			String prompt = RecipePromptBuilder.buildSystemPrompt(goal);

			assertTrue(prompt.contains("Preserve the dish's character"));
			assertTrue(prompt.contains("realistic ingredient quantities"));
			assertTrue(prompt.contains("original recipe as data"));
		}
	}
}
