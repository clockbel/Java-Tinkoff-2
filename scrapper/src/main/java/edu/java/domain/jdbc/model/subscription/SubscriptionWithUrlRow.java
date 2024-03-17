package edu.java.domain.jdbc.model.subscription;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionWithUrlRow implements RowMapper<SubscriptionWithUrl> {
    @Override
    @SuppressWarnings("MagicNumber")
    public SubscriptionWithUrl mapRow(ResultSet rs, int rowNum) throws SQLException {
        SubscriptionWithUrl subscriptionWithUrl = new SubscriptionWithUrl();
        subscriptionWithUrl.setIdChat(rs.getLong(1));
        subscriptionWithUrl.setIdLink(rs.getLong(2));
        subscriptionWithUrl.setUrl(rs.getString(3));
        return subscriptionWithUrl;
    }
}
