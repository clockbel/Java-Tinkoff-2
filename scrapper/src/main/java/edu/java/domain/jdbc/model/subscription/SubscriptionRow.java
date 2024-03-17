package edu.java.domain.jdbc.model.subscription;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionRow implements RowMapper<Subscription> {
    @Override
    public Subscription mapRow(ResultSet rs, int rowNum) throws SQLException {
        Subscription subscription = new Subscription();
        subscription.setIdChat(rs.getLong(1));
        subscription.setIdLink(rs.getLong(2));
        return subscription;
    }
}
