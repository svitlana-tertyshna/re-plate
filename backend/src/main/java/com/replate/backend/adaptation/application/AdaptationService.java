package com.replate.backend.adaptation.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.replate.backend.adaptation.application.model.AdaptationResult;
import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.application.port.RecipeGenerator;
import com.replate.backend.adaptation.domain.AdaptationGoal;

@Service
public class AdaptationService {

	private final RecipeGenerator recipeGenerator;
	private final AdaptationPersistenceService persistenceService;

	public AdaptationService(RecipeGenerator recipeGenerator, AdaptationPersistenceService persistenceService) {
		this.recipeGenerator = recipeGenerator;
		this.persistenceService = persistenceService;
	}

	public AdaptationResult adapt(String recipeText, AdaptationGoal goal) {
		GeneratedRecipe generated = recipeGenerator.generate(recipeText, goal);

		UUID adaptationId = persistenceService.save(recipeText, goal, generated);

		return new AdaptationResult(adaptationId, generated);
	}
}
