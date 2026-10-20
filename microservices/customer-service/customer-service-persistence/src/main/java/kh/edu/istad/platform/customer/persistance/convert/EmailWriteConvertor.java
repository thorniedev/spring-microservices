package kh.edu.istad.platform.customer.persistance.convert;

import kh.edu.istad.platform.customer.valueobject.Email;
import org.bson.BsonString;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class EmailWriteConvertor implements Converter<Email, BsonString> {

    @Override
    public BsonString convert(Email source) {
        return new BsonString(source.value());
    }
}
