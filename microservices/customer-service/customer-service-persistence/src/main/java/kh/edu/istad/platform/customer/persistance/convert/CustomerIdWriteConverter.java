package kh.edu.istad.platform.customer.persistance.convert;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import org.bson.Document;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.UUID;

@WritingConverter // Informs Spring Data Mongo this is for writes
public class CustomerIdWriteConverter implements Converter<CustomerId, Document> {

    @Override
    public Document convert(CustomerId source) {
        return new Document("_id", source.value().toString());
    }


//    @Override
//    public UUID convert(@NonNull CustomerId source) {
//        //return new Document("_id", source.value());
//        return source.value();
//    }

//    public UUID convert(CustomerId source) {
//        return new Document("_id", source.value());
//        return  source != null ? source.value() : null;
//    }

}
