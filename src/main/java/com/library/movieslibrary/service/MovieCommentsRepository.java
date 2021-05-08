package com.library.movieslibrary.service;

import com.library.movieslibrary.model.MovieComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieCommentsRepository extends MongoRepository<MovieComment, String> {
    List<MovieComment> findAllByImdbId(String imdbId);
}
