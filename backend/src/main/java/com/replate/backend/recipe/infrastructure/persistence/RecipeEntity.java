package com.replate.backend.recipe.infrastructure.persistence;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recipes")
public class RecipeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "original_text", nullable = false, columnDefinition = "text")
	private String originalText;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	protected RecipeEntity() {
	}

	public RecipeEntity(String originalText) {
		this.originalText = originalText;
	}

	public UUID getId() {
		return id;
	}

	public String getOriginalText() {
		return originalText;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}
