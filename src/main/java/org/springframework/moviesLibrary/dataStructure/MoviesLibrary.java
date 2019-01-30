package org.springframework.moviesLibrary.dataStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MoviesLibrary {
	private static final String INVALID_SEARCH_KEY = "Your search criteria is invalid, please verify it !";
	private static final String DELETE_ERROR_MSG = "No object found for given identifier !";
	private static final String UPDATE_ERROR_MSG = "Your input can not be used to update an object, please make sure that you are not using a null value or a date in wrong format !";
	private static final String CREATE_ERROR_MSG = "Your input can not be used to create an object of type Movie, please make sure that you are not using a null value or a date in wrong format !";
	private static final String dateFormatPattern = "dd/MM/yyyy";
	private List<Movie> movies;
	private String jsonInput;
	private String jsonOutput;
	private ObjectMapper mapper = new ObjectMapper();

	public MoviesLibrary(String jsonFile) throws IOException {
		this.mapper.setDateFormat(new SimpleDateFormat(dateFormatPattern));
		this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
		this.jsonInput = jsonFile;
		this.jsonOutput = "./moviesLib.json";
		this.movies = this.mapper.readValue(
				new File(this.jsonInput),
				this.mapper.getTypeFactory().constructCollectionType(
						List.class, Movie.class));
		writeJsonFile(this.movies);
	}

	public MoviesLibrary(List<Movie> moviesList) throws IOException {
		this.movies = moviesList;
	}

	public String create(Movie movie) throws IOException, ParseException {
		if (verifyDataIntegrity(movie)) {
			String index = determineMovieIndex();
			movie.setIndex(index);
			this.movies.add(movie);
			if (this.jsonOutput != null) {
				writeJsonFile(this.movies);
			}
			return index;
		} else {
			throw new IllegalArgumentException(CREATE_ERROR_MSG);
		}
	}

	public List<Movie> getAll() {
		return this.movies;
	}

	public Movie get(String index) {
		return findByIndex(index);
	}

	public List<Movie> get(String key, String value)
			throws NoSuchFieldException, IllegalArgumentException {
		// verify field validity
		if (key != null && value != null) {
			Movie.class.getDeclaredField(key);
		} else {
			throw new IllegalArgumentException(INVALID_SEARCH_KEY);
		}
		return this.movies.stream().filter(m -> m.get(key).contains(value))
				.collect(Collectors.toList());
	}

	public void update(String index, Movie newVersion) throws IOException,
			ParseException {
		if (verifyDataIntegrity(newVersion)) {
			if (findByIndex(index) != null) {
				Movie movieToUpdate = findByIndex(index);
				movieToUpdate.setTitle(newVersion.getTitle());
				movieToUpdate.setDirector(newVersion.getDirector());
				movieToUpdate.setReleaseDate(newVersion.getReleaseDate());
				movieToUpdate.setType(newVersion.getType());
				if (this.jsonOutput != null) {
					writeJsonFile(this.movies);
				}
			} else {
				throw new NoSuchElementException(DELETE_ERROR_MSG);
			}
		} else {
			throw new IllegalArgumentException(UPDATE_ERROR_MSG);
		}
	}

	public void delete(String index) throws IOException {
		if (findByIndex(index) != null) {
			this.movies.removeIf(m -> m.getIndex().equals(index));
			if (this.jsonOutput != null) {
				writeJsonFile(this.movies);
			}
		} else {
			throw new NoSuchElementException(DELETE_ERROR_MSG);
		}
	}

	public String determineMovieIndex() {
		Movie lastAddedMovie = Collections.max(this.movies,
				Comparator.comparing(m -> m.getIndex()));
		return String.valueOf(Integer.parseInt(lastAddedMovie.getIndex()) + 1);
	}

	public Movie findByIndex(String index) {
		Optional<Movie> matchingObject = this.movies.stream()
				.filter(m -> m.getIndex().equals(index)).findFirst();
		if (matchingObject.get() != null) {
			return matchingObject.get();
		} else {
			throw new NoSuchElementException(DELETE_ERROR_MSG);
		}
	}
	
	public void writeJsonFile(List<Movie> movies)
			throws JsonGenerationException, JsonMappingException,
			FileNotFoundException, IOException {
		this.mapper.writeValue(new FileOutputStream(this.jsonOutput), movies);
	}

	private boolean verifyDataIntegrity(Movie movie) throws ParseException {
		return movie.getDirector() != null && movie.getTitle() != null
				&& movie.getType() != null && movie.getReleaseDate() != null
				&& checkDateFormat(movie.getReleaseDate());
	}

	private boolean checkDateFormat(String date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
		dateFormat.parse(date);
		return true;
	}
}