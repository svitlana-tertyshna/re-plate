package com.replate.backend.adaptation.api;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.replate.backend.adaptation.application.AdaptationService;
import com.replate.backend.adaptation.application.model.AdaptationResult;
import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.domain.AdaptationGoal;

@WebMvcTest(AdaptationController.class)
class AdaptationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AdaptationService adaptationService;

	@Test
	void shouldAcceptValidRequest() throws Exception {

		UUID id = UUID.randomUUID();

		String request = """
				{
				  "recipeText": "200 g pasta.",
				  "goal": "VEGAN"
				}
				""";

		GeneratedRecipe generated = new GeneratedRecipe(
				"Demo Vegan Pasta",
				List.of("200 g egg-free pasta"),
				List.of("Cook the pasta."),
				List.of("Replaced regular pasta"),
				List.of("Demo only"));

		when(adaptationService.adapt("200 g pasta.", AdaptationGoal.VEGAN))
				.thenReturn(new AdaptationResult(id, generated));

		mockMvc.perform(
				post("/api/v1/adaptations")
						.contentType(MediaType.APPLICATION_JSON)
						.content(request))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(id.toString()))
				.andExpect(jsonPath("$.goal").value("VEGAN"))
				.andExpect(jsonPath("$.recipe.title").value("Demo Vegan Pasta"))
				.andExpect(jsonPath("$.recipe.ingredients[0]")
						.value("200 g egg-free pasta"));

		verify(adaptationService)
				.adapt("200 g pasta.", AdaptationGoal.VEGAN);
	}

	@Test
	void shouldRejectBlankRecipe() throws Exception {
		String request = """
				    {
				        "recipeText": "   ",
				        "goal": "VEGAN"
				    }
				""";

		mockMvc.perform(post("/api/v1/adaptations")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.title").value("Validation failed"))
				.andExpect(jsonPath("$.errors.recipeText")
						.value("must not be blank"));
	}

	@Test
	void shouldRejectUnsupportedGoal() throws Exception {
		String request = """
							    {
							        "recipeText": "200g pasta.",
							        "goal": "KETO"
				}
							""";

		mockMvc.perform(post("/api/v1/adaptations")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.title")
						.value("Invalid request body"))
				.andExpect(jsonPath("$.errors.request")
						.value("Invalid JSON or unsupported field value"));
	}

	@Test
	void shouldRejectMissingGoal() throws Exception {
		String request = """
				    {
				        "recipeText": "200 g pasta."
				    }
				""";

		mockMvc.perform(post("/api/v1/adaptations")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.title").value("Validation failed"))
				.andExpect(jsonPath("$.errors.goal")
						.value("must not be null"));
	}

	@Test
	void shouldRejectMalformedJson() throws Exception {
		String request = """
				{
				    "recipeText": "200 g pasta."
				    "goal": "VEGAN"
				}
				""";

		mockMvc.perform(post("/api/v1/adaptations")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.title")
						.value("Invalid request body"))
				.andExpect(jsonPath("$.errors.request")
						.value("Invalid JSON or unsupported field value"));

		verifyNoInteractions(adaptationService);
	}

}
