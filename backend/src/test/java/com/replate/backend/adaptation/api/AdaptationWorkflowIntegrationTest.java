
package com.replate.backend.adaptation.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AdaptationWorkflowIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void shouldPersistAdaptationFromHttpRequest() throws Exception {

		String originalText = "Test curry " + UUID.randomUUID();

		String request = """
				{
				    "recipeText": "%s",
				    "goal": "VEGAN"
				}
				""".formatted(originalText);

		try {
			MvcResult result = mockMvc.perform(
					post("/api/v1/adaptations")
							.contentType(MediaType.APPLICATION_JSON)
							.content(request))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.goal").value("VEGAN"))
					.andExpect(jsonPath("$.recipe.title")
							.value("Demo Vegan Pasta"))
					.andReturn();

			String returnedId = JsonPath.read(
					result.getResponse().getContentAsString(),
					"$.id");

			Map<String, Object> stored = jdbcTemplate.queryForMap("""
					SELECT
					    a.id::text AS id,
					    a.goal,
					    a.generated_recipe->>'title' AS title
					FROM adaptations a
					JOIN recipes r ON r.id = a.recipe_id
					WHERE r.original_text = ?
					""", originalText);

			assertEquals(returnedId, stored.get("id"));
			assertEquals("VEGAN", stored.get("goal"));
			assertEquals("Demo Vegan Pasta", stored.get("title"));

		} finally {
			jdbcTemplate.update("""
					DELETE FROM adaptations
					WHERE recipe_id IN (
					    SELECT id FROM recipes
					    WHERE original_text = ?
					)
					""", originalText);

			jdbcTemplate.update(
					"DELETE FROM recipes WHERE original_text = ?",
					originalText);
		}
	}
}
