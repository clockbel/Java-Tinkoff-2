package edu.java.models.request;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record RemoveLinkRequest(@NotNull URI url) {
}
