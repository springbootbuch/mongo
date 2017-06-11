package de.springbootbuch.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Part of springbootbuch.de.
 *
 * @author Michael J. Simons
 * @author @rotnroll666
 */
public abstract class ActorConstructorMixIn {

	ActorConstructorMixIn(@JsonProperty("firstName") String a, @JsonProperty("lastName") String b) {
	}
}
