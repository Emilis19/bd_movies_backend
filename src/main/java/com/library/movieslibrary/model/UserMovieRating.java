package com.library.movieslibrary.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "movieRatings")
public class UserMovieRating {
    @Id
    private String id;
    private String userId;
    private String imdbId;
    private int rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserMovieRating{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", imdbId='" + imdbId + '\'' +
                ", rating=" + rating +
                '}';
    }
}
