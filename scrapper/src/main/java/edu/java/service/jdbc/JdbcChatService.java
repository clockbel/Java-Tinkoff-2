package edu.java.service.jdbc;

import edu.java.domain.jdbc.dao.ChatDao;
import edu.java.domain.jdbc.dao.LinkDao;
import edu.java.domain.jdbc.dao.SubscriptionDao;
import edu.java.domain.jdbc.model.chat.Chat;
import edu.java.domain.jdbc.model.subscription.Subscription;
import edu.java.exception.errors.DuplicateRegistrationException;
import edu.java.exception.errors.NotFoundIdChatException;
import edu.java.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static java.time.OffsetDateTime.now;

@Service
@RequiredArgsConstructor
@Transactional
public class JdbcChatService implements ChatService {
    private final ChatDao chatDao;
    private final LinkDao linkDao;
    private final SubscriptionDao chatLinkDao;

    @Override
    public void register(long tgChatId) {
        if (chatDao.getById(tgChatId).isPresent()) {
            throw new DuplicateRegistrationException(toExMsg(tgChatId));
        }
        Chat chat = new Chat();
        chat.setId(tgChatId);
        chat.setCreatedAt(now());
        chatDao.save(chat);
    }

    @Override
    public void unregister(long tgChatId) {
        long id = chatDao.getById(tgChatId)
            .orElseThrow(
                () -> new NotFoundIdChatException(toExMsg(tgChatId))
            ).getId();
        //Удаление связанных ссылок
        List<Subscription> links = chatLinkDao.getByChatId(id);
        for (Subscription chatLink : links) {
            long linkId = chatLink.getIdLink();
            int countChatTrackLink = chatLinkDao.getByLinkId(linkId).size();
            chatLinkDao.delete(id, linkId);
            //Если ссылку отслеживает 1 чат, удалить из таблицы ссылок
            if (countChatTrackLink == 1) {
                linkDao.deleteById(linkId);
            }
        }
        chatDao.delete(tgChatId);
    }
}
