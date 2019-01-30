package org.springframework.moviesLibrary.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.moviesLibrary.dataStructure.Movie;
import org.springframework.moviesLibrary.dataStructure.MoviesLibrary;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/moviesLibrary")
public class MoviesLibraryController {
	private MoviesLibrary movies;

	@Autowired
	public MoviesLibraryController(MoviesLibrary movies) {
		this.movies = movies;
	}

	// CRUD services
	// Exceptions are handled in called methods's side

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(@RequestBody Movie movie) throws IOException,
			ParseException {
		String index = this.movies.create(movie);
		return index;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<Movie> getAll() {
		List<Movie> result = this.movies.getAll();
		return result;
	}

	@RequestMapping(value = "/{index}", method = RequestMethod.GET)
	public Movie get(@PathVariable("index") String index) {
		return this.movies.get(index);
	}

	@RequestMapping(value = "/{key}/{value}", method = RequestMethod.GET)
	public List<Movie> get(@PathVariable("key") String key,
			@PathVariable("value") String value)
			throws IllegalArgumentException, NoSuchFieldException {
		return this.movies.get(key, value);
	}

	@RequestMapping(value = "/{index}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("index") String index,
			@RequestBody Movie movie) throws IOException, ParseException {
		this.movies.update(index, movie);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@RequestMapping(value = "/{index}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("index") String index)
			throws NoSuchElementException, IOException {
		this.movies.delete(index);
		return ResponseEntity.ok(HttpStatus.OK);
	}
}