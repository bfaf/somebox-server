package com.kchonov.someboxserver.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class MovieGanresEntityPK implements Serializable {
    @Column(name = "movie_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;
    @Column(name = "ganre_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ganreId;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public int getGanreId() {
        return ganreId;
    }

    public void setGanreId(Integer ganreId) {
        this.ganreId = ganreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieGanresEntityPK that = (MovieGanresEntityPK) o;
        return movieId == that.movieId && ganreId == that.ganreId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, ganreId);
    }
}
