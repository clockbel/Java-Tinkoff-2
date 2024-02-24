package edu.java.bot.repository;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.database_utils.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserBase {
    private Map<Long, User> userMap = new HashMap<>();

    public Optional<User> findById(Update update) {
        return Optional.ofNullable(userMap.get(update.message().chat().id()));
    }

    public void putUserToBase(User user) {
        userMap.put(user.getChatId(), user);
    }

    public Map<Long, User> getUserMap() {
        return userMap;
    }

}
