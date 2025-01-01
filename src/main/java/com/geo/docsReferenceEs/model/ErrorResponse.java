package com.geo.docsReferenceEs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse(boolean success, @JsonProperty("error") ErrorMessage errorMessage) {
}
