package com.kchonov.someboxserver.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "movies_metadata", schema = "public", catalog = "postgres")
public class MoviesMetadataEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "movies_metadata_id")
    private Integer moviesMetadataId;
    @Basic
    @Column(name = "movie_id")
    private Integer movieId;
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
    @Column(name = "rated")
    private String rated;
    @Basic
    @Column(name = "ganre")
    private String ganre;
    @Basic
    @Column(name = "skip_intro_at")
    private Long skipIntroAt;
    @Basic
    @Column(name = "skip_intro_duration")
    private Long skipIntroDuration;
    @Basic
    @Column(name = "skip_credits_at")
    private Long skipCreditsAt;
    @Basic
    @Column(name = "column_info")
    private String columnInfo;
    @Basic
    @Column(name = "play_count")
    private Long playCount;

    public MoviesMetadataEntity() { }

    public MoviesMetadataEntity(Integer movieId, String plot, String rating, Long duration, String poster, String format, String rated, String ganre, Long skipIntroAt, Long skipIntroDuration, Long skipCreditsAt, String columnInfo, Long playCount) {
        this.movieId = movieId;
        this.plot = plot;
        this.rating = rating;
        this.duration = duration;
        this.poster = poster;
        this.format = format;
        this.rated = rated;
        this.ganre = ganre;
        this.skipIntroAt = skipIntroAt;
        this.skipIntroDuration = skipIntroDuration;
        this.skipCreditsAt = skipCreditsAt;
        this.columnInfo = columnInfo;
        this.playCount = playCount;
    }

    public Integer getMoviesMetadataId() {
        return moviesMetadataId;
    }

    public void setMoviesMetadataId(Integer moviesMetadataId) {
        this.moviesMetadataId = moviesMetadataId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
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

    public Long getSkipIntroDuration() {
        return skipIntroDuration;
    }

    public void setSkipIntroDuration(Long skipIntroDuration) {
        this.skipIntroDuration = skipIntroDuration;
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

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoviesMetadataEntity that = (MoviesMetadataEntity) o;
        return moviesMetadataId == that.moviesMetadataId && Objects.equals(movieId, that.movieId) && Objects.equals(plot, that.plot) && Objects.equals(rating, that.rating) && Objects.equals(duration, that.duration) && Objects.equals(poster, that.poster) && Objects.equals(format, that.format) && Objects.equals(rated, that.rated) && Objects.equals(ganre, that.ganre) && Objects.equals(skipIntroAt, that.skipIntroAt) && Objects.equals(skipIntroDuration, that.skipIntroDuration) && Objects.equals(skipCreditsAt, that.skipCreditsAt) && Objects.equals(columnInfo, that.columnInfo) && Objects.equals(playCount, that.playCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moviesMetadataId, movieId, plot, rating, duration, poster, format, rated, ganre, skipIntroAt, skipIntroDuration, skipCreditsAt, columnInfo, playCount);
    }
}
