package org.springframework.moviesLibrary;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.moviesLibrary.controller.MoviesLibraryController;
import org.springframework.moviesLibrary.dataStructure.Movie;
import org.springframework.moviesLibrary.dataStructure.MoviesLibrary;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(MoviesLibraryController.class)
public class MoviesLibraryApplicationTests {
	List<Movie> allMovies = new ArrayList<Movie>();
	private static final Logger LOGGER = Logger
			.getLogger(MoviesLibraryApplicationTests.class.getName());
	URI URL_ALL;
	URI URL_INDEX;
	URI URL_GET_KEY_VALUE;
	{
		try {
			URL_ALL = new URI("http://localhost:8080/moviesLibrary/");
			URL_INDEX = new URI("http://localhost:8080/moviesLibrary/1");
			URL_GET_KEY_VALUE = new URI(
					"http://localhost:8080/moviesLibrary/releaseDate/2018");
		} catch (URISyntaxException e) {
			LOGGER.log(
					Level.SEVERE,
					e.getMessage() != null && !e.getMessage().isEmpty() ? e
							.getMessage() : e.toString(), e);
		}
		allMovies.add(new Movie("1", "Movie1", "Director1", "02/08/2017",
				"action"));
		allMovies.add(new Movie("2", "Movie2", "Director2", "24/05/2018",
				"action"));
		allMovies.add(new Movie("3", "Movie3", "Director1", "19/03/2018",
				"adventure"));
		allMovies.add(new Movie("4", "Movie4", "Director1", "09/01/2019",
				"thriller"));
		allMovies.add(new Movie("5", "Movie5", "Director5", "22/12/2019",
				"comedy"));
	}

	@Autowired
	private MockMvc mvc;
	@MockBean
	private MoviesLibraryController moviesControllerMock;

	@Test
	// create(object) valid parameter
	public void given_index_and_valid_object_when_requesting_create_then_return_updated_Movies_list()
			throws Exception {
		moviesControllerMock = new MoviesLibraryController(new MoviesLibrary(
				allMovies));
		Movie newObject = create("6", "NewTitle", "NewDirector", "10/10/2019",
				"comedy");
		moviesControllerMock.create(newObject);
		assertEquals(moviesControllerMock.get("6").getTitle(), "NewTitle");
	}

	@Test(expected = IllegalArgumentException.class)
	// create(object) wrong object
	public void given_index_and_wrong_object_when_requesting_create_then_throw_exception()
			throws Exception {
		moviesControllerMock = new MoviesLibraryController(new MoviesLibrary(
				allMovies));
		Movie newObject = create("6", "NewTitle", "NewDirector", null, "comedy");
		moviesControllerMock.create(newObject);
	}

	@Test
	// getALL
	public void given_url_when_requesting_getAll_then_return_AllMovies()
			throws Exception {
		given(moviesControllerMock.getAll()).willReturn(allMovies);
		mvc.perform(get(URL_ALL).contentType(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0].title", equalTo("Movie1")));
	}

	@Test
	// get(index) valid parameter
	public void given_index_when_requesting_get_then_return_matched_Movie()
			throws Exception {
		given(moviesControllerMock.get("1")).willReturn(allMovies.get(0));
		mvc.perform(get(URL_INDEX).contentType(APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("title", equalTo("Movie1")));

	}

	@Test(expected = NoSuchElementException.class)
	// get(index) wrong parameter
	public void given_wrong_index_when_requesting_get_then_throw_exception()
			throws Exception {
		// initial size = 5
		moviesControllerMock = new MoviesLibraryController(new MoviesLibrary(
				allMovies));
		// get the 6th element => NoSuchElementException expected
		moviesControllerMock.get("6");
	}

	@Test
	// get(key, value) valid parameters
	public void given_key_and_value_when_requesting_get_then_return_matched_Movies_list()
			throws Exception {
		given(moviesControllerMock.get("releaseDate", "2018")).willReturn(
				allMovies.subList(1, 3));
		mvc.perform(get(URL_GET_KEY_VALUE).contentType(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].releaseDate", endsWith("2018")))
				.andExpect(jsonPath("$[1].releaseDate", endsWith("2018")));
	}

	@Test(expected = NoSuchFieldException.class)
	// get(key, value) wrong parameters
	public void given_wrong_key_and_value_when_requesting_get_then_throw_exception()
			throws Exception {
		moviesControllerMock = new MoviesLibraryController(new MoviesLibrary(
				allMovies));
		moviesControllerMock.get("releaseDates", "2019");
	}

	@Test(expected = IllegalArgumentException.class)
	// get(key, value) null parameters
	public void given_null_key_and_value_when_requesting_get_then_throw_exception()
			throws Exception {
		moviesControllerMock = new MoviesLibraryController(new MoviesLibrary(
				allMovies));
		moviesControllerMock.get(null, "2019");
	}

	@Test
	// update(index, object) valid parameters
	public void given_index_and_valid_object_when_requesting_update_then_return_updated_Movies_list()
			throws Exception {
		moviesControllerMock = new MoviesLibraryController(new MoviesLibrary(
				allMovies));
		Movie newVersion = create("3", "NewTitle", "NewDirector", "10/10/2019",
				"comedy");
		moviesControllerMock.update("3", newVersion);
		assertEquals(moviesControllerMock.get("3").getTitle(), "NewTitle");
	}

	@Test(expected = NoSuchElementException.class)
	// update(index, object) wrong index
	public void given_wrong_index_and_valid_object_when_requesting_update_then_throw_exception()
			throws Exception {
		moviesControllerMock = new MoviesLibraryController(new MoviesLibrary(
				allMovies));
		Movie newVersion = create("7", "NewTitle", "NewDirector", "10/10/2019",
				"comedy");
		moviesControllerMock.update("7", newVersion);
	}

	@Test(expected = IllegalArgumentException.class)
	// update(index, object) wrong object
	public void given_index_and_wrong_object_when_requesting_update_then_throw_exception()
			throws Exception {
		moviesControllerMock = new MoviesLibraryController(new MoviesLibrary(
				allMovies));
		Movie newVersion = create("3", "NewTitle", "NewDirector", null,
				"comedy");
		moviesControllerMock.update("3", newVersion);
	}

	@Test
	// delete(index) valid parameter
	public void given_index_when_requesting_delete_then_return_OK()
			throws Exception {
		// initial size = 5
		moviesControllerMock = new MoviesLibraryController(new MoviesLibrary(
				allMovies));
		moviesControllerMock.delete("1");
		// verify if size after delete = 4
		assertEquals(moviesControllerMock.getAll().size(), 4);
	}

	@Test(expected = NoSuchElementException.class)
	// delete(index) wrong parameter
	public void given_wrong_index_when_requesting_delete_then_throw_exception()
			throws Exception {
		// initial size = 5
		moviesControllerMock = new MoviesLibraryController(new MoviesLibrary(
				allMovies));
		// delete the 6th element => NoSuchElementException expected
		moviesControllerMock.delete("6");
	}

	public Movie create(String index, String title, String director,
			String releaseDate, String type) {
		Movie movie = new Movie();
		movie.setIndex(index);
		movie.setTitle(title);
		movie.setDirector(director);
		movie.setReleaseDate(releaseDate);
		movie.setType(type);
		return movie;
	}
}
