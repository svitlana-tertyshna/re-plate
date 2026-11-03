
package com.replate.backend.adaptation.infrastructure.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import com.replate.backend.adaptation.application.exception.AiGenerationException;
import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.domain.AdaptationGoal;

@ExtendWith(MockitoExtension.class)
class AiRecipeGeneratorTest {

	@Mock
	private ChatClient.Builder builder;

	@Mock
	private ChatClient chatClient;

	@Mock
	private ChatClient.ChatClientRequestSpec requestSpec;

	@Mock
	private ChatClient.CallResponseSpec responseSpec;

	@Mock
	private ChatClient.PromptUserSpec userSpec;

	private AiRecipeGenerator generator;

	@BeforeEach
	void setUp() {
		when(builder.build()).thenReturn(chatClient);
		when(chatClient.prompt()).thenReturn(requestSpec);

		when(requestSpec.system(anyString()))
				.thenReturn(requestSpec);

		when(requestSpec.user(
				org.mockito.ArgumentMatchers.<Consumer<ChatClient.PromptUserSpec>>any()))
				.thenAnswer(invocation -> {
					Consumer<ChatClient.PromptUserSpec> configureUser = invocation.getArgument(0);

					configureUser.accept(userSpec);
					return requestSpec;
				});

		when(userSpec.text(anyString())).thenReturn(userSpec);
		when(userSpec.param(anyString(), any())).thenReturn(userSpec);

		when(requestSpec.call()).thenReturn(responseSpec);

		generator = new AiRecipeGenerator(builder);
	}

	@Test
	void shouldGenerateRecipeWithCorrectGoalAndInput() {
		String originalRecipe = "Pasta with chicken and cream";

		GeneratedRecipe expected = new GeneratedRecipe(
				"Vegan Pasta",
				List.of("200 g pasta", "100 ml oat cream"),
				List.of("Cook pasta", "Add oat cream"),
				List.of("Replaced dairy cream"),
				List.of());

		when(responseSpec.entity(GeneratedRecipe.class))
				.thenReturn(expected);

		GeneratedRecipe actual = generator.generate(
				originalRecipe,
				AdaptationGoal.VEGAN);

		assertEquals(expected, actual);

		verify(userSpec).param("goal", "VEGAN");
		verify(userSpec).param("recipe", originalRecipe);
		verify(responseSpec).entity(GeneratedRecipe.class);
	}

	@Test
	void shouldRejectNullAiResponse() {
		when(responseSpec.entity(GeneratedRecipe.class))
				.thenReturn(null);

		AiGenerationException exception = assertThrows(
				AiGenerationException.class,
				() -> generator.generate(
						"Pasta with chicken",
						AdaptationGoal.VEGAN));

		assertEquals(
				"AI returned no generated recipe",
				exception.getMessage());
	}

	@Test
	void shouldRejectRecipeWithMissingTitle() {
		GeneratedRecipe invalid = new GeneratedRecipe(
				"",
				List.of("200 g pasta"),
				List.of("Cook the pasta"),
				List.of("Replaced wheat pasta"),
				List.of());

		when(responseSpec.entity(GeneratedRecipe.class))
				.thenReturn(invalid);

		AiGenerationException exception = assertThrows(
				AiGenerationException.class,
				() -> generator.generate(
						"Original pasta recipe",
						AdaptationGoal.VEGAN));

		assertEquals(
				"AI returned an incomplete recipe",
				exception.getMessage());
	}

	@Test
	void shouldRejectRecipeWithEmptyIngredients() {
		GeneratedRecipe invalid = new GeneratedRecipe(
				"Vegan Pasta",
				List.of(),
				List.of("Cook the pasta"),
				List.of("Replaced dairy cream"),
				List.of());

		when(responseSpec.entity(GeneratedRecipe.class))
				.thenReturn(invalid);

		assertThrows(
				AiGenerationException.class,
				() -> generator.generate(
						"Pasta with cream",
						AdaptationGoal.VEGAN));
	}

	@Test
	void shouldWrapAiClientFailure() {
		IllegalStateException originalFailure = new IllegalStateException("Provider unavailable");

		when(responseSpec.entity(GeneratedRecipe.class))
				.thenThrow(originalFailure);

		AiGenerationException exception = assertThrows(
				AiGenerationException.class,
				() -> generator.generate(
						"Pasta with cream",
						AdaptationGoal.VEGAN));

		assertEquals("AI request failed", exception.getMessage());
		assertSame(originalFailure, exception.getCause());
	}

}
