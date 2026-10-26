package com.replate.backend.adaptation.infrastructure.fake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.replate.backend.adaptation.domain.AdaptationGoal;

class FakeRecipeGeneratorTest {

	private final FakeRecipeGenerator generator = new FakeRecipeGenerator();

	@Test
	void shoudGenerateVeganDemo() {
		var result = generator.generate("Chicken curry with cream", AdaptationGoal.VEGAN);

		assertEquals("Demo Vegan Pasta", result.title());
		assertTrue(result.warnings().get(0).contains("not analyzed"));
	}

	@Test
	void shouldGenerateCalorieConsciousDemo() {
		var result = generator.generate(
				"Pasta with cream",
				AdaptationGoal.CALORIE_CONSCIOUS);

		assertEquals("Demo Light Pasta", result.title());
		assertTrue(result.warnings().get(0)
				.contains("not been verified"));
	}
}
