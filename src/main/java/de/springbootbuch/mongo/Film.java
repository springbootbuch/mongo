package de.springbootbuch.mongo;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Part of springbootbuch.de.
 * @author Michael J. Simons
 * @author @rotnroll666
 */
@Document(collection = "films")
public class Film {
	
	private String id;
	
	private final String title;
	
	private final Year releaseYear;
	
	private final List<Actor> actors = new ArrayList<>();

	public Film(String title, Year releaseYear) {
		this.title = title;
		this.releaseYear = releaseYear;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Year getReleaseYear() {
		return releaseYear;
	}

	public List<Actor> getActors() {
		return Collections.unmodifiableList(actors);
	}
}
