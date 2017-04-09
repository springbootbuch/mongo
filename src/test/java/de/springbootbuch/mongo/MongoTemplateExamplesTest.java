package de.springbootbuch.mongo;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import org.bson.codecs.configuration.CodecRegistry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.replaceRoot;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Part of springbootbuch.de.
 *
 * @author Michael J. Simons
 * @author @rotnroll666
 */
@RunWith(SpringRunner.class)
@DataMongoTest
public class MongoTemplateExamplesTest {

	@TestConfiguration
	public static class PopulatorConfiguration {

		@Bean
		public ObjectMapper objectMapper() {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.addMixIn(Film.class, FilmConstructorMixIn.class);
			mapper.addMixIn(Actor.class, ActorConstructorMixIn.class);
			mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper;
		}

		@Bean
		public Jackson2RepositoryPopulatorFactoryBean
			repositoryPopulator(final ObjectMapper objectMapper)
		{
			final Jackson2RepositoryPopulatorFactoryBean factory
				= new Jackson2RepositoryPopulatorFactoryBean();
			factory.setMapper(objectMapper);
			factory.setResources(new Resource[]{
				new ClassPathResource("data.json")});
			return factory;
		}
	}

	static class ActorCodec implements Codec<Actor> {

		private final Codec<Document> documentCodec;

		public ActorCodec(Codec<Document> documentCodec) {
			this.documentCodec = documentCodec;
		}

		@Override
		public void encode(BsonWriter writer, Actor value, EncoderContext encoderContext) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public Class<Actor> getEncoderClass() {
			return Actor.class;
		}

		@Override
		public Actor decode(BsonReader reader, DecoderContext decoderContext) {
			Document document = documentCodec.decode(reader, decoderContext);
			return new Actor(document.getString("firstName"), document.getString("lastName"));
		}
	}

	@TestConfiguration
	public static class MongoClientConfig {
		@Bean
		public MongoClientOptions mongoClientOption() {
			final CodecRegistry defaultCodecRegistry = 
				MongoClient.getDefaultCodecRegistry();
			final Codec<Document> defaultDocumentCodec =
				defaultCodecRegistry.get(Document.class);

			final CodecRegistry codecRegistry = 
				CodecRegistries.fromRegistries(
					defaultCodecRegistry,
					fromCodecs(new ActorCodec(defaultDocumentCodec))
				);

			return MongoClientOptions
				.builder()
				.codecRegistry(codecRegistry)
				.build();
		}
	}

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void queryBySubdocument() {
		List<Film> films = this.mongoTemplate
			.find(query(where("actors.lastName").is("GUINESS")),
				Film.class);
		assertThat(films.size(), is(2));
	}

	/**
	 * Only support on MongoDB > 3.4, Flapdoodle 2.0 doesn't support replaceRoot
	 */
	@Test
	@Ignore
	public void distinctElementsOfSubcollectionMongo34() {
		Aggregation agg = Aggregation.newAggregation(
			unwind("actors"),
			group("firstName", "lastName")
				.addToSet("actors")
				.as("uniqueActors"),
			unwind("uniqueActors"),
			replaceRoot("uniqueActors")
		);
		AggregationResults<Actor> results = mongoTemplate
			.aggregate(agg, "films", Actor.class);
		List<Actor> distinctActors = results.getMappedResults();
		assertThat(distinctActors.size(), is(53));
	}

	@Test
	public void distinctElementsOfSubcollectionWithDistinct() {
		List<Actor> distinctActors = mongoTemplate
			.getCollection("films")
			.distinct("actors", Actor.class)
			.into(new ArrayList<>());
		assertThat(distinctActors.size(), is(53));
	}
}
