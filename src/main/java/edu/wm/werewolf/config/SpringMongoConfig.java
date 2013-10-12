package edu.wm.werewolf.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
 

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

	@Configuration
	public class SpringMongoConfig {
	 
		
		public @Bean MongoDbFactory mongoDbFactory() throws Exception {
			return new SimpleMongoDbFactory(new MongoClient(new MongoClientURI(System.getenv("MONGOHQ_URL"))), "werewolf");
		}
		
		public @Bean MongoTemplate mongoTemplate() throws Exception {
	 
			MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
	 
			return mongoTemplate;
	 
		}
	 
	}

