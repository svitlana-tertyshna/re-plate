package com.replate.backend.adaptation.application;

import org.springframework.stereotype.Service;

@Service
public class AdaptationService {

	public String getStatus() {
		return "VALIDATED_ONLY";
	}

}
