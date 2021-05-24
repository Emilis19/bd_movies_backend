package com.library.movieslibrary.controller;

import com.library.movieslibrary.model.MovieRecommendation;
import com.library.movieslibrary.payload.MessageResponse;
import com.library.movieslibrary.service.MovieRecommentaionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationsController {
    @Autowired
    private MovieRecommentaionsRepository movieRecommentaionsRepository;


    @PostMapping(value = "/save",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity saveMovieRecommendation(@RequestBody MovieRecommendation movieRecommendation) {
        if (movieRecommendation == null || movieRecommendation.getUserId() == null || movieRecommendation.getImdbId() == null) {
            return new ResponseEntity(new MessageResponse("Neteisinga užklausa"), HttpStatus.BAD_REQUEST);
        }
        MovieRecommendation recommendation = movieRecommentaionsRepository.findByMovieIdAndImdbIdAndUserId(movieRecommendation.getMovieId(),
                movieRecommendation.getImdbId(), movieRecommendation.getUserId());
        if (recommendation != null) {
            recommendation.setRecommendationText(movieRecommendation.getRecommendationText());
            movieRecommentaionsRepository.save(recommendation);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        movieRecommentaionsRepository.save(movieRecommendation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{movieId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<MovieRecommendation>> getRecommendationsForMovie(@PathVariable("movieId") String movieId) {
        if (movieId == null) {
            return new ResponseEntity(new MessageResponse("MoiveId yra privalomas"), HttpStatus.BAD_REQUEST);
        }
        List<MovieRecommendation> recommendations = movieRecommentaionsRepository.findAllByImdbId(movieId);
        if (CollectionUtils.isEmpty(recommendations)) {
            return new ResponseEntity(new MessageResponse("Rekomendacijos nerastos"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<MovieRecommendation>> getRecommendationsForMovieByUserId(@PathVariable("userId") String userId) {
        if (userId == null) {
            return new ResponseEntity(new MessageResponse("UserId yra privalomas"), HttpStatus.BAD_REQUEST);
        }
        List<MovieRecommendation> recommendations = movieRecommentaionsRepository.findAllByUserId(userId);
        if (CollectionUtils.isEmpty(recommendations)) {
            return new ResponseEntity(new MessageResponse("Rekomendacijos nerastos"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{movieId}/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteRecommendationByMovieIdAndUserId(@PathVariable("movieId") String movieId, @PathVariable("userId") String userId) {
        if (movieId == null || userId == null) {
            return new ResponseEntity(new MessageResponse("Id yra privalomas"), HttpStatus.BAD_REQUEST);
        }
        List<MovieRecommendation> recommendations = movieRecommentaionsRepository.findAllByMovieIdAndUserId(movieId, userId);
        if (CollectionUtils.isEmpty(recommendations)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        List<String> ids = recommendations.stream().map(MovieRecommendation::getId).collect(Collectors.toList());
        movieRecommentaionsRepository.deleteByIdIn(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/delete/{recommendationId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteRecommendationById(@PathVariable("recommendationId") String recommendationId) {
        if (recommendationId == null) {
            return new ResponseEntity(new MessageResponse("Id yra privalomas"), HttpStatus.BAD_REQUEST);
        }
        if (!movieRecommentaionsRepository.existsById(recommendationId)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        movieRecommentaionsRepository.deleteById(recommendationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
