package edu.java.bot.model.database_utils;

import java.net.URL;
import java.util.List;

public class User {
    private Long chatId;
    private List<URL> uriList;

    public User(Long chatId, List<URL> uriList) {
        this.chatId = chatId;
        this.uriList = uriList;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public List<URL> getUriList() {
        return uriList;
    }

    public void setUriList(List<URL> uriList) {
        this.uriList = uriList;
    }
}
