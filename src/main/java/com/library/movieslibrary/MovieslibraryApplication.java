package com.library.movieslibrary;

import com.library.movieslibrary.controller.MovieController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class MovieslibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieslibraryApplication.class, args);
	}

}
