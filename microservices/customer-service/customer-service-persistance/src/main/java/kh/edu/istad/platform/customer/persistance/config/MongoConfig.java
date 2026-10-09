package kh.edu.istad.platform.customer.persistance.config;

import kh.edu.istad.platform.customer.persistance.convert.CustomerIdReadConverter;
import kh.edu.istad.platform.customer.persistance.convert.CustomerIdWriteConverter;
import org.bson.UuidRepresentation;
import org.springframework.boot.mongodb.autoconfigure.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions(){
        //return MongoCustomConversions.create()
        return new MongoCustomConversions(List.of(
                new CustomerIdReadConverter(),
                new CustomerIdWriteConverter()
        ));
    }

    public MongoClientSettingsBuilderCustomizer mongoClientSettingsBuilderCustomizer() {
        return builder -> builder.uuidRepresentation(UuidRepresentation.STANDARD);
    }
}
