package kh.edu.istad.platform.customer.persistance.convert;

import jakarta.validation.constraints.NotNull;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.UUID;

public class CustomerIdReadConverter implements Converter<UUID, CustomerId> {

    @Override
    public CustomerId convert(@NonNull UUID source) {
        //return new CustomerId(UUID.fromString(source.getString("_id").toString()));
        return new CustomerId(source);
    }
}
