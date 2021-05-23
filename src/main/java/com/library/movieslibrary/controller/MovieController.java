package com.library.movieslibrary.controller;

import com.library.movieslibrary.model.*;
import com.library.movieslibrary.payload.MessageResponse;
import com.library.movieslibrary.payload.SavedMovieRequest;
import com.library.movieslibrary.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;


@SuppressWarnings("ALL")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private static final String FINISHED = "FINISHED";
    private static final String IN_PROGRESS = "IN_PROGRESS";
    private static final String NO_STATUS = "NO_STATUS";

    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieApiService movieApiService;
    @Autowired
    private MovieCommentsRepository movieCommentsRepository;
    @Autowired
    private UserMovieRatingRepository userMovieRatingRepository;
    @Autowired
    private MovieRecommentaionsRepository movieRecommentaionsRepository;

    @GetMapping(value = "/get/{title}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Movie>> getMovieBySearch(@PathVariable("title") String title) {
        var response = movieApiService.getMoviesBySearchTitle(title);
        if (response == null) {
            return new ResponseEntity(new MessageResponse("Filmai nerasti"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/imdb/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Movie> getMovieByImdbId(@PathVariable("id") String id) {
        var response = movieApiService.getMovieById(id);
        if (response == null) {
            return new ResponseEntity(new MessageResponse("Filmas nerastas"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/id/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<SavedMovie> getMovieById(@PathVariable("id") String id) {
        var response = movieService.findById(id);
        if (response == null || !response.isPresent()) {
            return new ResponseEntity(new MessageResponse("Filmas nerastas"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity saveMovie(@RequestBody SavedMovieRequest movie) {
        if (movie == null || movie.getMovie() == null || movie.getUserId() == null) {
            return new ResponseEntity(new MessageResponse("Filmas yra privalomas"), HttpStatus.BAD_REQUEST);
        }
        SavedMovie savedMovie = new SavedMovie(movie.getMovie(), movie.getUserId());
        if (movieService.findByUserIdAndTitle(savedMovie.getUserId(), savedMovie.getTitle()) != null) {
            return new ResponseEntity(new MessageResponse("Toks filmas jau egzistuoja"), HttpStatus.BAD_REQUEST);
        }
        savedMovie.setMovieStatus("NO_STATUS");
        movieService.save(savedMovie);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/save/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity saveMovieStatus(@PathVariable("id") String id, @RequestBody String status) {
        if (status == null) {
            return new ResponseEntity(new MessageResponse("Statusas yra privalomas"), HttpStatus.BAD_REQUEST);
        }
        Optional<SavedMovie> movie = movieService.findById(id);
        if (!movie.isPresent()) {
            return new ResponseEntity(new MessageResponse("Toks filmas ne egzistuoja sąraše"), HttpStatus.BAD_REQUEST);
        }
        movie.get().setMovieStatus(status);
        movieService.save(movie.get());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/user/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<SavedMovie>> getUserMovies(@PathVariable("userId") String userId) {
        List<SavedMovie> movie = movieService.findAllByUserId(userId);
        if (CollectionUtils.isEmpty(movie)) {
            return new ResponseEntity(new MessageResponse("Filmai nerasti"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @PostMapping(value = "/comments/save")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity saveMovieComment(@RequestBody MovieComment comment) {
        if (comment == null || comment.getImdbId() == null || comment.getComment() == null) {
            return new ResponseEntity(new MessageResponse("Komentaras yra netinkamas"), HttpStatus.BAD_REQUEST);
        }
        comment.setDate(new Date());
        movieCommentsRepository.save(comment);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/comments/{imdbId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<MovieComment>> getCommentsByImdbId(@PathVariable("imdbId") String imdbId) {
        List<MovieComment> comments = movieCommentsRepository.findAllByImdbId(imdbId);
        if (CollectionUtils.isEmpty(comments)) {
            return new ResponseEntity(new MessageResponse("Komentarai nerasti"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping(value = "/finished/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<SavedMovie>> getFinishedMoviesByUserId(@PathVariable("userId") String userId) {
        if (userId == null) {
            return new ResponseEntity(new MessageResponse("Id yra privalomas"), HttpStatus.BAD_REQUEST);
        }
        List<SavedMovie> movies = movieService.findAllByUserId(userId);
        if (CollectionUtils.isEmpty(movies)) {
            return new ResponseEntity(new MessageResponse("Filmai nerasti"), HttpStatus.BAD_REQUEST);
        }
        movies = movies.stream().filter(a -> FINISHED.equals(a.getMovieStatus())).collect(Collectors.toList());
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping(value = "/progress/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<SavedMovie>> getInProgressMoviesByUserId(@PathVariable("userId") String userId) {
        if (userId == null) {
            return new ResponseEntity(new MessageResponse("Id yra privalomas"), HttpStatus.BAD_REQUEST);
        }
        List<SavedMovie> movies = movieService.findAllByUserId(userId);
        if (CollectionUtils.isEmpty(movies)) {
            return new ResponseEntity(new MessageResponse("Filmai nerasti"), HttpStatus.BAD_REQUEST);
        }
        movies = movies.stream().filter(a -> IN_PROGRESS.equals(a.getMovieStatus())).collect(Collectors.toList());
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping(value = "/validmovies/{userId}/{imdbId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<SavedMovie>> getValidUserMovies(@PathVariable("userId") String userId, @PathVariable("imdbId") String imdbId) {
        if (userId == null) {
            return new ResponseEntity(new MessageResponse("Id yra privalomas"), HttpStatus.BAD_REQUEST);
        }
        List<SavedMovie> movies = movieService.findAllByUserId(userId);
        if (CollectionUtils.isEmpty(movies)) {
            return new ResponseEntity(new MessageResponse("Filmai nerasti"), HttpStatus.BAD_REQUEST);
        }
        movies = movies.stream().filter(a -> !NO_STATUS.equals(a.getMovieStatus()) && !a.getImdbID().equals(imdbId)).collect(Collectors.toList());
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteMovieById(@PathVariable("id") String id) {
        if (id == null) {
            return new ResponseEntity(new MessageResponse("Id yra privalomas"), HttpStatus.BAD_REQUEST);
        }
        Optional<SavedMovie> movie = movieService.findById(id);
        if (!movie.isPresent()) {
            return new ResponseEntity(new MessageResponse("Filmas nerastas"), HttpStatus.BAD_REQUEST);
        }
        movieService.delete(movie.get());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/comments/delete/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteMovieCommentById(@PathVariable("id") String id) {
        if (id == null) {
            return new ResponseEntity(new MessageResponse("Id yra privalomas"), HttpStatus.BAD_REQUEST);
        }
        Optional<MovieComment> comment = movieCommentsRepository.findById(id);
        if (!comment.isPresent()) {
            return new ResponseEntity(new MessageResponse("Komentaras nerastas"), HttpStatus.BAD_REQUEST);
        }
        movieCommentsRepository.delete(comment.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/ratings/{imdbId}",
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

    @PostMapping(value = "/ratings/save",
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


    @PostMapping(value = "/recommendations/save",
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

    @GetMapping(value = "/recommendations/{movieId}",
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

    @GetMapping(value = "/recommendations/user/{userId}",
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

    @DeleteMapping(value = "/recommendations/delete/{movieId}/{userId}")
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

    @DeleteMapping(value = "/recommendations/delete/{recommendationId}")
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
