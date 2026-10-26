package com.replate.backend.adaptation.application.port;

import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.domain.AdaptationGoal;

public interface RecipeGenerator {

	GeneratedRecipe generate(String recipeText, AdaptationGoal goal);

}
