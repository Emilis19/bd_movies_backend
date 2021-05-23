package com.library.movieslibrary.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class MovieRecommendation {
    @Id
    private String id;
    //movie ids from db (saved movies)
    private String movieId;
    private String title;
    private String poster;
    private Date date;
    private String recommendationText;
    //used for initial movie
    private String imdbId;
    private String passedImdbId;
    private String userId;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRecommendationText() {
        return recommendationText;
    }

    public void setRecommendationText(String recommendationText) {
        this.recommendationText = recommendationText;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassedImdbId() {
        return passedImdbId;
    }

    public void setPassedImdbId(String passedImdbId) {
        this.passedImdbId = passedImdbId;
    }

    @Override
    public String toString() {
        return "MovieRecommendation{" +
                "id='" + id + '\'' +
                ", movieId='" + movieId + '\'' +
                ", Title='" + title + '\'' +
                ", poster='" + poster + '\'' +
                ", date=" + date +
                ", recommendationText='" + recommendationText + '\'' +
                ", imdbId='" + imdbId + '\'' +
                '}';
    }
}
