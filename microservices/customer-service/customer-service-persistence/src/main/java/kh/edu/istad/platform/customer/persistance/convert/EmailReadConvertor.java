package kh.edu.istad.platform.customer.persistance.convert;


import kh.edu.istad.platform.customer.valueobject.Email;
import org.bson.BsonString;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class EmailReadConvertor implements Converter<BsonString, Email> {

    @Override
    public Email convert(BsonString source) {
        return new Email(source.getValue());
    }
}
