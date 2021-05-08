package com.library.movieslibrary.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
public class Movie {
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Year")
    private String year;
    @JsonProperty("Rated")
    private String rated;
    @JsonProperty("Released")
    private String released;
    @JsonProperty("Runtime")
    private String runtime;
    @JsonProperty("Genre")
    private String genre;
    @JsonProperty("Director")
    private String director;
    @JsonProperty("Writer")
    private String writer;
    @JsonProperty("Actors")
    private String actors;
    @JsonProperty("Plot")
    private String plot;
    @JsonProperty("Language")
    private String language;
    @JsonProperty("Country")
    private String country;
    @JsonProperty("Poster")
    private String poster;
    @JsonProperty("Ratings")
    private List<Rating> ratings;
    private String imdbRating;
    private String imdbID;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Response")
    private String response;

    public Movie() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", rated='" + rated + '\'' +
                ", released='" + released + '\'' +
                ", runtime='" + runtime + '\'' +
                ", genre='" + genre + '\'' +
                ", director='" + director + '\'' +
                ", writer='" + writer + '\'' +
                ", actors='" + actors + '\'' +
                ", plot='" + plot + '\'' +
                ", language='" + language + '\'' +
                ", country='" + country + '\'' +
                ", poster='" + poster + '\'' +
                ", ratings=" + ratings +
                ", imdbRating='" + imdbRating + '\'' +
                ", imdbID='" + imdbID + '\'' +
                ", type='" + type + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
