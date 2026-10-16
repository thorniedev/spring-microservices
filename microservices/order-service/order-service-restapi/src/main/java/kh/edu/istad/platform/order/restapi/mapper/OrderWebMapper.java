package kh.edu.istad.platform.order.restapi.mapper;

import kh.edu.istad.platform.order.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.dto.CreateOrderResult;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderRequest;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderResponse;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface OrderWebMapper {
    CreateOrderCommand toCommand(CreateOrderRequest request);
    CreateOrderResponse toResponse(CreateOrderResult result);
}
