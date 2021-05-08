package com.library.movieslibrary.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "comments")
public class MovieComment {
    @Id
    private String id;
    private String imdbId;
    private String name;
    private Date date;
    private String comment;

    public MovieComment() {
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "MovieComment{" +
                "id='" + id + '\'' +
                ", imdbId='" + imdbId + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", comment='" + comment + '\'' +
                '}';
    }
}
