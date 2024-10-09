package com.kchonov.someboxserver.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "video_standards", schema = "public", catalog = "postgres")
public class VideoStandardsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "video_standard_id")
    private Integer videoStandardId;
    @Basic
    @Column(name = "movie_id")
    private Integer movieId;
    @Basic
    @Column(name = "standard")
    private String standard;

    public Integer getVideoStandardId() {
        return videoStandardId;
    }

    public void setVideoStandardId(Integer videoStandardId) {
        this.videoStandardId = videoStandardId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoStandardsEntity that = (VideoStandardsEntity) o;
        return videoStandardId == that.videoStandardId && Objects.equals(movieId, that.movieId) && Objects.equals(standard, that.standard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoStandardId, movieId, standard);
    }
}
