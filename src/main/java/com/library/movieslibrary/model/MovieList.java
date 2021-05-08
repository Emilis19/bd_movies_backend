package com.library.movieslibrary.model;

import java.util.Arrays;

public class MovieList {
    public Movie[] Search;
    public String Response; // True | False

    public boolean success() {
        return Response != null && Response.equals("True");
    }

    public Movie[] getSearch() {
        return Search;
    }

    public void setSearch(Movie[] search) {
        Search = search;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    @Override
    public String toString() {
        return "MovieList{" +
                "Search=" + Arrays.toString(Search) +
                ", Response='" + Response + '\'' +
                '}';
    }
}
