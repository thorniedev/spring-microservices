package kh.edu.istad.platform.customer.persistance.config;

import kh.edu.istad.platform.customer.persistance.convert.CustomerIdReadConverter;
import kh.edu.istad.platform.customer.persistance.convert.CustomerIdWriteConverter;
import kh.edu.istad.platform.customer.persistance.convert.EmailReadConvertor;
import kh.edu.istad.platform.customer.persistance.convert.EmailWriteConvertor;
import org.bson.UuidRepresentation;
import org.springframework.boot.mongodb.autoconfigure.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.List;


@Configuration
@EnableReactiveMongoAuditing(auditorAwareRef = "auditorAwareConfig", modifyOnCreate = false)
@EnableReactiveMongoRepositories(basePackages = "kh.edu.istad.platform.customer.persistance.repository")
public class MongoConfig {

//    @Bean
//    public MongoCustomConversions mongoCustomConversions(){
//        /*
//        return MongoCustomConversions.create(
//                adapter -> {
//                    adapter.registerConverter(new CustomerIdReadConverter());
//                    adapter.registerConverter(new CustomerIdWriteConverter());
//                    adapter.registerConverter(new EmailReadConvertor());
//                    adapter.registerConverter(new EmailWriteConvertor());
//                }
//        ); */
//    }

    /*    public MongoClientSettingsBuilderCustomizer mongoClientSettingsBuilderCustomizer() {
            return builder -> builder.uuidRepresentation(UuidRepresentation.STANDARD);
        }*/

}
