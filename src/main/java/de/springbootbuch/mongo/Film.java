package de.springbootbuch.mongo;

import java.time.Year;
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
}
