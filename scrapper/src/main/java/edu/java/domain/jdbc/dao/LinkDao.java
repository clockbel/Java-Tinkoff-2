package edu.java.domain.jdbc.dao;

import edu.java.domain.jdbc.model.link.Link;
import edu.java.domain.jdbc.model.link.LinkRow;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkDao {
    private final JdbcClient jdbcClient;
    private final LinkRow linkRowMapper;

    public List<Link> getAll() {
        String sql = "SELECT * FROM links";
        return jdbcClient.sql(sql)
            .query(linkRowMapper).list();
    }

    public Optional<Link> getByUrl(String url) {
        String sql = "SELECT * FROM links WHERE url = ?";
        return jdbcClient.sql(sql)
            .param(url)
            .query(linkRowMapper).optional();
    }

    public Optional<Link> getById(long id) {
        String sql = "SELECT * FROM links WHERE id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(linkRowMapper).optional();
    }

    public List<Link> getByLustUpdate(OffsetDateTime dateTime) {
        String sql = "SELECT * FROM links WHERE last_updated < ?";
        return jdbcClient.sql(sql)
            .param(dateTime)
            .query(linkRowMapper).list();
    }

    public int save(Link link) {
        String sql = "INSERT INTO links(url, last_updated) VALUES (?, ?)";
        return jdbcClient.sql(sql)
            .params(link.getUrl(), link.getLastUpdateAt())
            .update();
    }

    public void updateLastUpdateAtById(long id, OffsetDateTime dateTime) {
        String sql = "UPDATE links SET last_updated = ? WHERE id = ?";
        jdbcClient.sql(sql)
            .params(dateTime, id)
            .update();
    }

    public int deleteByUrl(String url) {
        String sql = "DELETE FROM links WHERE url = ?";
        return jdbcClient.sql(sql)
            .param(url)
            .update();
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM links WHERE id = ?";
        jdbcClient.sql(sql)
            .param(id)
            .update();
    }
}
