package edu.java.domain.jdbc.model.subscription;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionWithUrl {
    private long idChat;
    private long idLink;
    private String url;
}
