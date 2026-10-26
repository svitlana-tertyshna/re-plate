package com.replate.backend.adaptation.infrastructure.fake;

import java.util.List;

import org.springframework.stereotype.Component;

import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.application.port.RecipeGenerator;
import com.replate.backend.adaptation.domain.AdaptationGoal;

@Component
public class FakeRecipeGenerator implements RecipeGenerator {

	@Override
	public GeneratedRecipe generate(String recipeText, AdaptationGoal goal) {
		return switch (goal) {
			case VEGAN -> new GeneratedRecipe(
					"Demo Vegan Pasta",
					List.of(
							"200 g egg-free pasta",
							"100 ml oat cooking cream",
							"15 g nutritional yeast"),
					List.of(
							"Cook the pasta.",
							"Heat the oat cream.",
							"Add nutritional yeast and combine."),
					List.of("Diary cream replaced with oat cream."),
					List.of("Demo only the submitted recipe was not analyzed."));

			case CALORIE_CONSCIOUS -> new GeneratedRecipe(
					"Demo Light Pasta",
					List.of(
							"200 g pasta",
							"100 ml light cooking cream"),
					List.of(
							"Cook the pasta.",
							"Warm the light cream.",
							"Combine pasta and sauce."),
					List.of(
							"Regular cream replaced with light cream."),
					List.of(
							"Demo only: calorie reduction has not been verified."));
		};
	}
}
