package com.replate.backend.api.error;

import java.util.Map;

public record ApiErrorResponse(
		int status,
		String title,
		Map<String, String> errors

) {

}
