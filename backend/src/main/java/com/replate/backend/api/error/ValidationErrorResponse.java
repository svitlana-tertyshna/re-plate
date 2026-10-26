package com.replate.backend.api.error;

import java.util.Map;

public record ValidationErrorResponse(
		int status,
		String title,
		Map<String, String> errors

) {

}
