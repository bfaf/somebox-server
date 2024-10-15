package com.kchonov.someboxserver.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "movies", schema = "public", catalog = "postgres")
public class MoviesEntity implements Serializable {

    public MoviesEntity() {}
    public MoviesEntity(String name, String releaseYear, String filename) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.filename = filename;
        this.startFrom = 0L;
        this.published = (short)1;
        this.createdAt = new Timestamp(new Date().getTime());
        this.updatedAt = new Timestamp(new Date().getTime());
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "movie_id")
    private Integer movieId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "release_year")
    private String releaseYear;
    @Basic
    @Column(name = "filename")
    private String filename;
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

    @OneToOne(mappedBy = "moviesEntity", cascade = CascadeType.ALL)
    private MoviesMetadataEntity moviesMetadata;

//    @ManyToMany
//    private List<MovieGanresEntity> movieGanres;

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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

    public MoviesMetadataEntity getMoviesMetadataEntity() {
        return moviesMetadata;
    }

    public void setMoviesMetadataEntity(MoviesMetadataEntity moviesMetadataEntity) {
        this.moviesMetadata = moviesMetadataEntity;
    }

//    public List<MovieGanresEntity> getMoviesGanres() {
//        return movieGanres;
//    }
//
//    public void setMoviesGanres(List<MovieGanresEntity> movieGanres) {
//        this.movieGanres = movieGanres;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoviesEntity that = (MoviesEntity) o;
        return movieId == that.movieId && Objects.equals(name, that.name) && Objects.equals(releaseYear, that.releaseYear) && Objects.equals(filename, that.filename) && Objects.equals(startFrom, that.startFrom) && Objects.equals(published, that.published) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, name, releaseYear, filename, startFrom, published, createdAt, updatedAt);
    }
}
