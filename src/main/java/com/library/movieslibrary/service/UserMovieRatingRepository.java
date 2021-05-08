package com.library.movieslibrary.service;

import com.library.movieslibrary.model.UserMovieRating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserMovieRatingRepository extends MongoRepository<UserMovieRating, String> {
    UserMovieRating findByImdbIdAndUserId(String imdbId, String userId);
    List<UserMovieRating> findAllByImdbId(String imdbId);
}
