package com.library.movieslibrary;

import com.library.movieslibrary.controller.CommentsController;
import com.library.movieslibrary.controller.MovieController;
import com.library.movieslibrary.model.Movie;
import com.library.movieslibrary.model.MovieComment;
import com.library.movieslibrary.model.SavedMovie;
import com.library.movieslibrary.payload.SavedMovieRequest;
import com.library.movieslibrary.service.MovieApiService;
import com.library.movieslibrary.service.MovieCommentsRepository;
import com.library.movieslibrary.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class MovieslibraryApplicationTests {

	@Autowired
	private MovieApiService movieApiService;
	@InjectMocks
	private MovieController movieController;
	@InjectMocks
	private CommentsController commentsController;
	@MockBean
	private MovieService movieService;
	@MockBean
	private MovieCommentsRepository commentService;
	private SavedMovie movie;
	private MovieComment comment;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		movie = new SavedMovie();
		movie.setUserId("random");
		movie.setTitle("random");
		movie.setYear("2000");
		movie.setResponse("True");

		comment = new MovieComment();
		comment.setComment("random");
		comment.setId("1");
		comment.setImdbId("2");
		comment.setName("random");
	}

	@Test
	@DisplayName("Get movies by title")
	public void testSearchMoviesByTitle() {
		var response = movieApiService.getMoviesBySearchTitle("batman");
		assertFalse(CollectionUtils.isEmpty(response));
	}

	@Test
	@DisplayName("Get movies by id")
	public void testSearchMoviesById() {
		var response = movieApiService.getMovieById("random");
		assertEquals(response.getResponse(), "False");
		response = movieApiService.getMovieById("tt0120737");
		assertNotNull(response);
		assertEquals(response.getResponse(), "True");
	}


	@Test
	public void testSavingMovie() {
		when(movieService.save(any(SavedMovie.class))).thenReturn(movie);

		SavedMovieRequest req = new SavedMovieRequest();
		req.setMovie(new Movie());
		req.setUserId("random");
		ResponseEntity<?> res = movieController.saveMovie(req);
		assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

	}

	@Test
	public void testGetMovie() {
		when(movieService.findById(anyString())).thenReturn(java.util.Optional.ofNullable(movie));

		ResponseEntity<SavedMovie> res = movieController.getMovieById(anyString());
		assertEquals(res.getStatusCode(), HttpStatus.OK);
		assertNotNull(res.getBody());
		assertEquals(res.getBody().getTitle(), "random");
	}

	@Test
	public void testSavingComment() {
		when(commentService.save(any(MovieComment.class))).thenReturn(comment);

		ResponseEntity<?> res = commentsController.saveMovieComment(new MovieComment());
		assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

	}

	@Test
	public void testGetComment() {
		when(commentService.findAllByImdbId(anyString())).thenReturn(List.of(comment));

		ResponseEntity<List<MovieComment>> res = commentsController.getCommentsByImdbId(anyString());
		assertEquals(res.getStatusCode(), HttpStatus.OK);
		assertNotNull(res.getBody());
		assertEquals(res.getBody().get(0).getImdbId(), "2");
	}

}
