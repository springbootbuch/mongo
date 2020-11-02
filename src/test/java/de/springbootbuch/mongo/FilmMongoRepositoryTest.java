package de.springbootbuch.mongo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Part of springbootbuch.de.
 *
 * @author Michael J. Simons
 * @author @rotnroll666
 */
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class FilmMongoRepositoryTest {

	@Autowired
	private FilmMongoRepository filmRepository;

	@Test
	public void filmShouldBeSaved() {
		Film film = filmRepository.save(
			new Film("Der wilde wilde Westen", 
			1974));
		assertThat(film.getId()).isNotNull();
		assertThat(film.getReleaseYear())
			.isEqualTo(1974);
	}
}
