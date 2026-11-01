
package com.replate.backend.adaptation.application.exception;

import java.util.UUID;

public class AdaptationNotFoundException extends RuntimeException {

	public AdaptationNotFoundException(UUID id) {
		super("Adaptation not found: " + id);
	}
}
