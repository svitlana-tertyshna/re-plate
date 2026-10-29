package com.replate.backend.adaptation.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.domain.AdaptationGoal;
import com.replate.backend.adaptation.infrastructure.persistence.AdaptationEntity;
import com.replate.backend.adaptation.infrastructure.persistence.AdaptationRepository;
import com.replate.backend.recipe.infrastructure.persistence.RecipeEntity;
import com.replate.backend.recipe.infrastructure.persistence.RecipeRepository;

@Service
public class AdaptationPersistenceService {

	private final RecipeRepository recipeRepository;
	private final AdaptationRepository adaptationRepository;

	public AdaptationPersistenceService(
			RecipeRepository recipeRepository,
			AdaptationRepository adaptationRepository) {
		this.recipeRepository = recipeRepository;
		this.adaptationRepository = adaptationRepository;
	}

	@Transactional
	public UUID save(
			String originalText,
			AdaptationGoal goal,
			GeneratedRecipe generatedRecipe) {
		RecipeEntity recipe = recipeRepository.save(
				new RecipeEntity(originalText));

		AdaptationEntity adaptation = new AdaptationEntity(
				recipe,
				goal,
				generatedRecipe);

		AdaptationEntity saved = adaptationRepository.save(adaptation);

		return saved.getId();
	}
}
