package com.library.movieslibrary.service;

import com.library.movieslibrary.model.Movie;
import com.library.movieslibrary.model.MovieList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovieApiService {

    private static final String baseUrl = "http://www.omdbapi.com";
    @Autowired
    private RestTemplate restTemplate;


    public List<Movie> getMoviesBySearchTitle(String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("s", title);
        params.put("type", "movie");
        params.put("apikey", "f9a41d49");
        MovieList response = restTemplate.getForObject(getUri(params), MovieList.class);
        if (response == null || (response.getSearch() == null || response.getSearch().length == 0)) {
            return null; // empty
        }
        return Arrays.asList(response.getSearch());
    }

    public Movie getMovieById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("i", id);
        params.put("apikey", "f9a41d49");
        return restTemplate.getForObject(getUri(params), Movie.class);
    }

    private URI getUri(Map<String, Object> params) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(baseUrl);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            uri.queryParam(entry.getKey(), entry.getValue());
        }
        return uri.build().encode().toUri();
    }


}
