package com.library.movieslibrary.service;

import com.library.movieslibrary.model.MovieRecommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRecommentaionsRepository extends MongoRepository<MovieRecommendation, String> {
    List<MovieRecommendation> findAllByImdbId(String movieId);
    List<MovieRecommendation> findAllByUserId(String userId);
    List<MovieRecommendation> findAllByMovieIdAndUserId(String imdbId, String userId);
    void deleteByIdIn(List<String> ids);
    MovieRecommendation findByMovieIdAndImdbIdAndUserId(String movieId, String imdbId, String userId);
}
