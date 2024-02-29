package edu.java.dto.response.stackoverflow;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OwnerInformation {
    private long accountId;
    private long userId;
    private String displayName;
    private String link;
}
