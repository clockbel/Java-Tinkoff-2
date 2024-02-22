package edu.java.response.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepositoryResponse {
    private Long id;
    @JsonProperty("full_name")
    private String fullName;
    private String url;
    @JsonProperty("created_at") OffsetDateTime createdAt;
    @JsonProperty("pushed_at") OffsetDateTime pushedAt;
}
