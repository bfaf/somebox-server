package com.kchonov.someboxserver.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "movies", schema = "public", catalog = "postgres")
@IdClass(MoviesEntityPK.class)
public class MoviesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "movie_id")
    private int movieId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "filename")
    private String filename;
    @Basic
    @Column(name = "human_name")
    private String humanName;
    @Basic
    @Column(name = "start_from")
    private Long startFrom;
    @Basic
    @Column(name = "published")
    private Short published;
    @Basic
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Basic
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Basic
    @Column(name = "for_countries")
    private String forCountries;

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getHumanName() {
        return humanName;
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    public Long getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(Long startFrom) {
        this.startFrom = startFrom;
    }

    public Short getPublished() {
        return published;
    }

    public void setPublished(Short published) {
        this.published = published;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getForCountries() {
        return forCountries;
    }

    public void setForCountries(String forCountries) {
        this.forCountries = forCountries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoviesEntity that = (MoviesEntity) o;
        return movieId == that.movieId && Objects.equals(name, that.name) && Objects.equals(filename, that.filename) && Objects.equals(humanName, that.humanName) && Objects.equals(startFrom, that.startFrom) && Objects.equals(published, that.published) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(forCountries, that.forCountries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, name, filename, humanName, startFrom, published, createdAt, updatedAt, forCountries);
    }
}
