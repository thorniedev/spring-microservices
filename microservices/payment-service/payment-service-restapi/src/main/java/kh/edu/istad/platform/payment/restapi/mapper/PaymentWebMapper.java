package kh.edu.istad.platform.payment.restapi.mapper;

import kh.edu.istad.platform.payment.dto.ProcessPaymentCommand;
import kh.edu.istad.platform.payment.dto.ProcessPaymentResult;
import kh.edu.istad.platform.payment.restapi.dto.PaymentRequest;
import kh.edu.istad.platform.payment.restapi.dto.PaymentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentWebMapper {
    ProcessPaymentCommand toCommand(PaymentRequest request);
    PaymentResponse toResponse(ProcessPaymentResult result);
}