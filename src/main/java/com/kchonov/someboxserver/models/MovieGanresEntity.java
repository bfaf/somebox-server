package com.kchonov.someboxserver.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "movie_ganres", schema = "public", catalog = "postgres")
@IdClass(MovieGanresEntityPK.class)
public class MovieGanresEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "movie_id")
    private Integer movieId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ganre_id")
    private Integer ganreId;

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Integer getGanreId() {
        return ganreId;
    }

    public void setGanreId(Integer ganreId) {
        this.ganreId = ganreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieGanresEntity that = (MovieGanresEntity) o;
        return movieId == that.movieId && ganreId == that.ganreId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, ganreId);
    }
}
