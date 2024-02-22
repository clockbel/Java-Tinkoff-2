package edu.java.response.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerInformation {
    @JsonProperty("account_id")
    private long accountId;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("display_name")
    private String displayName;

    private String link;
}
