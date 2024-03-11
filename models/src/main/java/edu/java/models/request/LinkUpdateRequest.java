package edu.java.models.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(
    @Min(1)
    Long id,
    @NotNull URI url,
    @NotNull String description,
    @NotEmpty List<Long> tgChatIds) {
}
