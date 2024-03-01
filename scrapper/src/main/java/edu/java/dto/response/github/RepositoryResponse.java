package edu.java.dto.response.github;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RepositoryResponse {
    private Long id;
    private String fullName;
    private String url;
    OffsetDateTime createdAt;
    OffsetDateTime pushedAt;
}
