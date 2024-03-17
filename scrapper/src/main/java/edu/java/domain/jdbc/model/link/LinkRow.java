package edu.java.domain.jdbc.model.link;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkRow implements RowMapper<Link> {
    @Override
    @SuppressWarnings("MagicNumber")
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        Link link = new Link();
        link.setId(rs.getLong(1));
        link.setUrl(rs.getString(2));
        link.setLastUpdateAt(rs.getObject(3, OffsetDateTime.class));
        return link;
    }
}
