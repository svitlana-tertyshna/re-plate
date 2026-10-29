
package com.replate.backend.adaptation.infrastructure.persistence;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.replate.backend.adaptation.application.model.GeneratedRecipe;
import com.replate.backend.adaptation.domain.AdaptationGoal;
import com.replate.backend.recipe.infrastructure.persistence.RecipeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "adaptations")
public class AdaptationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "recipe_id", nullable = false)
	private RecipeEntity recipe;

	@Enumerated(EnumType.STRING)
	@Column(name = "goal", nullable = false, length = 32)
	private AdaptationGoal goal;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "generated_recipe", nullable = false, columnDefinition = "jsonb")
	private GeneratedRecipe generatedRecipe;

	protected AdaptationEntity() {
	}

	public AdaptationEntity(
			RecipeEntity recipe,
			AdaptationGoal goal,
			GeneratedRecipe generatedRecipe) {
		this.recipe = recipe;
		this.goal = goal;
		this.generatedRecipe = generatedRecipe;
	}

	public UUID getId() {
		return id;
	}

	public RecipeEntity getRecipe() {
		return recipe;
	}

	public AdaptationGoal getGoal() {
		return goal;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public GeneratedRecipe getGeneratedRecipe() {
		return generatedRecipe;
	}
}
