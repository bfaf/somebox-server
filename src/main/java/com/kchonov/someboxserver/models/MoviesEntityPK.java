package com.kchonov.someboxserver.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class MoviesEntityPK implements Serializable {
    @Column(name = "movie_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieId;
    @Column(name = "name")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoviesEntityPK that = (MoviesEntityPK) o;
        return movieId == that.movieId && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, name);
    }
}
