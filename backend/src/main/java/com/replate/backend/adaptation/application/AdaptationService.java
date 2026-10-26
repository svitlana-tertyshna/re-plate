package com.replate.backend.adaptation.application;

import org.springframework.stereotype.Service;

import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.application.port.RecipeGenerator;
import com.replate.backend.adaptation.domain.AdaptationGoal;

@Service
public class AdaptationService {

	private final RecipeGenerator recipeGenerator;

	public AdaptationService(RecipeGenerator recipeGenerator) {
		this.recipeGenerator = recipeGenerator;
	}

	public GeneratedRecipe adapt(String recipeText, AdaptationGoal goal) {
		return recipeGenerator.generate(recipeText, goal);
	}
}
