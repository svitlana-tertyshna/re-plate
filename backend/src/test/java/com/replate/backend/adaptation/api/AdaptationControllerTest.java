package com.replate.backend.adaptation.api;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.replate.backend.adaptation.api.AdaptationController;
import com.replate.backend.adaptation.application.AdaptationService;

@WebMvcTest(AdaptationController.class)
class AdaptationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AdaptationService adaptationService;

	@Test
	void shouldAcceptValidRequest() throws Exception {
		when(adaptationService.getStatus()).thenReturn("VALIDATED_ONLY");

		String request = """
				{
				    "recipeText": "200g pasta and 100 ml cream.",
				    "goal": "VEGAN"
				}
				""";

		mockMvc.perform(post("/api/v1/adaptations")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request)).andExpect(status().isOk())
				.andExpect(jsonPath("$.status")
						.value("VALIDATED_ONLY"))
				.andExpect(jsonPath("$.goal")
						.value("VEGAN"));

		verify(adaptationService).getStatus();
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
				.andExpect(status().isBadRequest());
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
				.andExpect(status().isBadRequest());
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
				.andExpect(status().isBadRequest());
	}
}
