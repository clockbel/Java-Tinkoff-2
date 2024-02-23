package edu.java.bot.model.database_utils;

import java.net.URL;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long chatId;
    private List<URL> uriList;

    public User(Long chatId, List<URL> uriList) {
        this.chatId = chatId;
        this.uriList = uriList;
    }
}
