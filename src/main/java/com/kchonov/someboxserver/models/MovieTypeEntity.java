package com.kchonov.someboxserver.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "movie_type", schema = "public", catalog = "postgres")
public class MovieTypeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "movie_type_id")
    private Integer movieTypeId;
    @Basic
    @Column(name = "movie_id")
    private Integer movieId;
    @Basic
    @Column(name = "type")
    private String type;

    public Integer getMovieTypeId() {
        return movieTypeId;
    }

    public void setMovieTypeId(Integer movieTypeId) {
        this.movieTypeId = movieTypeId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieTypeEntity that = (MovieTypeEntity) o;
        return movieTypeId == that.movieTypeId && Objects.equals(movieId, that.movieId) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieTypeId, movieId, type);
    }
}
