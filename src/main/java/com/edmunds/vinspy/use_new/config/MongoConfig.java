package com.edmunds.vinspy.use_new.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * @author alina
 */
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    private String databaseName = "vinspy";

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Bean
    public String inventoriesCollection() {
        return "inventory";
    }


    @Bean
    @Override
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        converter.setCustomConversions(customConversions());
        return converter;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(mongoClientURI());
    }

    public MongoClientURI mongoClientURI() {
        return new MongoClientURI("mongodb://vin-mongo.poc-vin.cloud.edmunds.com:27017");
    }

}