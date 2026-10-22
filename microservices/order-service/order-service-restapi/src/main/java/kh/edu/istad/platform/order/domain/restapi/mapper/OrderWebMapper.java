package kh.edu.istad.platform.order.domain.restapi.mapper;

import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.restapi.dto.CreateOrderRequest;
import kh.edu.istad.platform.order.domain.restapi.dto.CreateOrderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderWebMapper {
    CreateOrderCommand toCommand(CreateOrderRequest request);
    CreateOrderResponse toResponse(CreateOrderResult result);
}
