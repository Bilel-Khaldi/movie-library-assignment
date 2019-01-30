package org.springframework.moviesLibrary.dataStructure;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Movie {

	private static AtomicInteger counter = new AtomicInteger(0);
	private String index;
	private String title = null;
	private String director = null;
	private String releaseDate = null;
	private String type = null;

	@SuppressWarnings("unchecked")
	public String get(String key) {
		ObjectMapper omp = new ObjectMapper();
		Map<String, String> movieMap = omp.convertValue(this, Map.class);
		return movieMap.get(key);
	}

	// standard Getters and Setters

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Movie() {
		this.index = "" + counter.getAndIncrement();
	}

	public Movie(String index, String title, String director,
			String releaseDate, String type) {
		this.setIndex(index);
		this.setTitle(title);
		this.setDirector(director);
		this.setReleaseDate(releaseDate);
		this.setType(type);
	}
}