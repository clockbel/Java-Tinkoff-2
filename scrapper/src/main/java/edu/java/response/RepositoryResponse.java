package edu.java.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepositoryResponse(
    Long id,
    @JsonProperty("full_name")
    String fullName,
    String url,
    @JsonProperty("created_at") OffsetDateTime createdAt,
    @JsonProperty("pushed_at") OffsetDateTime pushedAt) {
}
