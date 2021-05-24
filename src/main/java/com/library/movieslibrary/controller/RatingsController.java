package com.library.movieslibrary.controller;

import com.library.movieslibrary.model.UserMovieRating;
import com.library.movieslibrary.payload.MessageResponse;
import com.library.movieslibrary.service.UserMovieRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.OptionalDouble;

@SuppressWarnings("ALL")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ratings")
public class RatingsController {

    @Autowired
    private UserMovieRatingRepository userMovieRatingRepository;

    @GetMapping(value = "/{imdbId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Double> getRatingForMovie(@PathVariable("imdbId") String imdbId) {
        if (imdbId == null) {
            return new ResponseEntity(new MessageResponse("imdbId yra privalomas"), HttpStatus.BAD_REQUEST);
        }
        List<UserMovieRating> ratings = userMovieRatingRepository.findAllByImdbId(imdbId);
        if (CollectionUtils.isEmpty(ratings)) {
            return new ResponseEntity(0, HttpStatus.OK);
        }
        OptionalDouble average = ratings.stream().mapToDouble(UserMovieRating::getRating).average();
        if (average.isEmpty()) {
            return new ResponseEntity(new MessageResponse("Reitingai nerasti"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(average.getAsDouble(), HttpStatus.OK);
    }

    @PostMapping(value = "/save",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity updateRatingForMovie(@RequestBody UserMovieRating rating) {
        if (rating == null || rating.getUserId() == null || rating.getRating() == 0) {
            return new ResponseEntity(new MessageResponse("Reitingas yra privalomas"), HttpStatus.BAD_REQUEST);
        }
        UserMovieRating userRating = userMovieRatingRepository.findByImdbIdAndUserId(rating.getImdbId(), rating.getUserId());
        if (userRating == null) {
            userMovieRatingRepository.save(rating);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        userRating.setRating(rating.getRating());
        userMovieRatingRepository.save(userRating);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
