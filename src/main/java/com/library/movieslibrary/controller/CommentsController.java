package com.library.movieslibrary.controller;

import com.library.movieslibrary.model.MovieComment;
import com.library.movieslibrary.payload.MessageResponse;
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

@SuppressWarnings("ALL")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comments")
public class CommentsController {

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

    @DeleteMapping(value = "/delete/{id}")
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

    @GetMapping(value = "/{imdbId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<MovieComment>> getCommentsByImdbId(@PathVariable("imdbId") String imdbId) {
        List<MovieComment> comments = movieCommentsRepository.findAllByImdbId(imdbId);
        if (CollectionUtils.isEmpty(comments)) {
            return new ResponseEntity(new MessageResponse("Komentarai nerasti"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity saveMovieComment(@RequestBody MovieComment comment) {
        if (comment == null || comment.getImdbId() == null || comment.getComment() == null) {
            return new ResponseEntity(new MessageResponse("Komentaras yra netinkamas"), HttpStatus.BAD_REQUEST);
        }
        comment.setDate(new Date());
        movieCommentsRepository.save(comment);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
