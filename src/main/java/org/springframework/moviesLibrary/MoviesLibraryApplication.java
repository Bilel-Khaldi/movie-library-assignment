package org.springframework.moviesLibrary;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.moviesLibrary.dataStructure.MoviesLibrary;

@SpringBootApplication
public class MoviesLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesLibraryApplication.class, args);
	}

	 @Bean
	    public MoviesLibrary create() throws IOException {
	        return new MoviesLibrary("./movies.json");
	    }
}

