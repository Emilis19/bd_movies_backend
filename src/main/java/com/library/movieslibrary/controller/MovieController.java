package com.library.movieslibrary.controller;

import com.library.movieslibrary.model.Movie;
import com.library.movieslibrary.model.SavedMovie;
import com.library.movieslibrary.payload.MessageResponse;
import com.library.movieslibrary.payload.SavedMovieRequest;
import com.library.movieslibrary.service.MovieApiService;
import com.library.movieslibrary.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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

}
