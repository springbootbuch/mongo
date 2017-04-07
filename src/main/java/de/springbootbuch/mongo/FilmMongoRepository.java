package de.springbootbuch.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Part of springbootbuch.de.
 * @author Michael J. Simons
 * @author @rotnroll666
 */
public interface FilmMongoRepository 
	extends MongoRepository<Film, String> {
}
