package edu.java.service;

public interface ChatService {
    String EXCEPTION = "id = %d";

    void register(long id);

    void unregister(long id);

    default String toExMsg(long id) {
        return String.format(EXCEPTION, id);
    }
}
