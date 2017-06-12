package de.springbootbuch.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Part of springbootbuch.de.
 *
 * @author Michael J. Simons
 * @author @rotnroll666
 */
public abstract class FilmConstructorMixIn {

	FilmConstructorMixIn(@JsonProperty("title") String a, @JsonProperty("releaseYear") Integer b) {
	}
}
