package kh.edu.istad.platform.business.restapi.mapper;

import kh.edu.istad.platform.business.dto.CreateBusinessCommand;
import kh.edu.istad.platform.business.dto.CreateBusinessResult;
import kh.edu.istad.platform.business.dto.ProductDto;
import kh.edu.istad.platform.business.restapi.dto.CreateBusinessRequest;
import kh.edu.istad.platform.business.restapi.dto.CreateBusinessResponse;
import kh.edu.istad.platform.business.restapi.dto.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BusinessWebMapper {
    CreateBusinessCommand toCommand(CreateBusinessRequest request);
    CreateBusinessResponse toResponse(CreateBusinessResult result);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "available", constant = "true")
    ProductDto toProductDto(ProductRequest request);
}