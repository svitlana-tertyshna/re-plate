package com.replate.backend.adaptation.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdaptationRepository
		extends JpaRepository<AdaptationEntity, UUID> {
}
