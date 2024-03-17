package edu.java.domain.jdbc.model.link;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Link {
    private Long id;
    private String url;
    private OffsetDateTime lastUpdateAt;
}
