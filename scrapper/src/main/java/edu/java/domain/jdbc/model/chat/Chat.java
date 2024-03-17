package edu.java.domain.jdbc.model.chat;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Chat {
    private long id;
    private OffsetDateTime createdAt;
}
