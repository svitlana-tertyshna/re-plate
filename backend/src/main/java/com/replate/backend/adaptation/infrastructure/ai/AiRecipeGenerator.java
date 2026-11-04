
package com.replate.backend.adaptation.infrastructure.ai;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.replate.backend.adaptation.application.exception.AiGenerationException;
import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.application.port.RecipeGenerator;
import com.replate.backend.adaptation.domain.AdaptationGoal;

@Component
@Profile("ai")
public class AiRecipeGenerator implements RecipeGenerator {

	private final ChatClient chatClient;

	public AiRecipeGenerator(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}

	@Override
	public GeneratedRecipe generate(String recipeText, AdaptationGoal goal) {

		GeneratedRecipe generated;

		try {
			generated = chatClient.prompt()
					.system(RecipePromptBuilder.buildSystemPrompt(goal))
					.user(user -> user
							.text("""
									Adaptation goal: {goal}
									Original recipe:
									{recipe}
									""")
							.param("goal", goal.name())
							.param("recipe", recipeText))
					.call()
					.entity(GeneratedRecipe.class);

		} catch (RuntimeException exception) {
			throw new AiGenerationException(
					"AI request failed",
					exception);
		}

		if (generated == null) {
			throw new AiGenerationException(
					"AI returned no generated recipe");
		}

		if (generated.title() == null || generated.title().isBlank()
				|| hasInvalidItems(generated.ingredients())
				|| hasInvalidItems(generated.instructions())
				|| hasInvalidItems(generated.changes())
				|| generated.warnings() == null) {

			throw new AiGenerationException(
					"AI returned an incomplete recipe");
		}

		return generated;
	}

	private boolean hasInvalidItems(List<String> items) {
		return items == null
				|| items.isEmpty()
				|| items.stream().anyMatch(
						item -> item == null || item.isBlank());
	}
}
