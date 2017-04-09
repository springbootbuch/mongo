package de.springbootbuch.mongo;

import java.time.Year;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Part of springbootbuch.de.
 *
 * @author Michael J. Simons
 * @author @rotnroll666
 */
@RunWith(SpringRunner.class)
@DataMongoTest
public class FilmMongoRepositoryTest {

	@Autowired
	private FilmMongoRepository filmRepository;

	@Test
	public void filmShouldBeSaved() {
		Film film = filmRepository.save(
			new Film("Der wilde wilde Westen", 
			Year.of(1974)));
		assertThat(film.getId(), is(notNullValue()));
		assertThat(film.getReleaseYear().getValue(), 
			is(1974));
	}
}
