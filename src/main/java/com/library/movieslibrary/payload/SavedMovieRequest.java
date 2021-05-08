package com.library.movieslibrary.payload;

import com.library.movieslibrary.model.Movie;

public class SavedMovieRequest {
    private String userId;
    private Movie movie;

    public SavedMovieRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public String toString() {
        return "SavedMovieRequest{" +
                "userId='" + userId + '\'' +
                ", movie=" + movie +
                '}';
    }
}
