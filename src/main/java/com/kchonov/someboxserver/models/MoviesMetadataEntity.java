package com.kchonov.someboxserver.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "movies_metadata", schema = "public", catalog = "postgres")
public class MoviesMetadataEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "movie_id")
    private Integer movieId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "plot")
    private String plot;
    @Basic
    @Column(name = "rating")
    private String rating;
    @Basic
    @Column(name = "duration")
    private Long duration;
    @Basic
    @Column(name = "poster")
    private String poster;
    @Basic
    @Column(name = "format")
    private String format;
    @Basic
    @Column(name = "type")
    private String type;
    @Basic
    @Column(name = "release_year")
    private String releaseYear;
    @Basic
    @Column(name = "rated")
    private String rated;
    @Basic
    @Column(name = "ganre")
    private String ganre;
    @Basic
    @Column(name = "skip_intro_at")
    private Long skipIntroAt;
    @Basic
    @Column(name = "skip_credits_at")
    private Long skipCreditsAt;
    @Basic
    @Column(name = "column_info")
    private String columnInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getGanre() {
        return ganre;
    }

    public void setGanre(String ganre) {
        this.ganre = ganre;
    }

    public Long getSkipIntroAt() {
        return skipIntroAt;
    }

    public void setSkipIntroAt(Long skipIntroAt) {
        this.skipIntroAt = skipIntroAt;
    }

    public Long getSkipCreditsAt() {
        return skipCreditsAt;
    }

    public void setSkipCreditsAt(Long skipCreditsAt) {
        this.skipCreditsAt = skipCreditsAt;
    }

    public String getColumnInfo() {
        return columnInfo;
    }

    public void setColumnInfo(String columnInfo) {
        this.columnInfo = columnInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoviesMetadataEntity that = (MoviesMetadataEntity) o;
        return id == that.id && Objects.equals(movieId, that.movieId) && Objects.equals(name, that.name) && Objects.equals(plot, that.plot) && Objects.equals(rating, that.rating) && Objects.equals(duration, that.duration) && Objects.equals(poster, that.poster) && Objects.equals(format, that.format) && Objects.equals(type, that.type) && Objects.equals(releaseYear, that.releaseYear) && Objects.equals(rated, that.rated) && Objects.equals(ganre, that.ganre) && Objects.equals(skipIntroAt, that.skipIntroAt) && Objects.equals(skipCreditsAt, that.skipCreditsAt) && Objects.equals(columnInfo, that.columnInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movieId, name, plot, rating, duration, poster, format, type, releaseYear, rated, ganre, skipIntroAt, skipCreditsAt, columnInfo);
    }
}
