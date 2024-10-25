package com.kchonov.someboxserver.batch;

import com.kchonov.someboxserver.models.MoviesEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MoviesRowMapper implements RowMapper<MoviesEntity> {

    public MoviesEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        MoviesEntity m = new MoviesEntity();

        m.setMovieId(rs.getInt("movie_id"));
        m.setName(rs.getString("name"));
        m.setReleaseYear(rs.getString("release_year"));
        m.setFilename(rs.getString("filename"));
        m.setPublished(rs.getShort("published"));
        m.setStartFrom(rs.getLong("start_from"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        m.setUpdatedAt(rs.getTimestamp("updated_at"));

        return m;
    }
}
