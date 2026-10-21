package kh.edu.istad.platform.customer.restapi.mapper;

import kh.edu.istad.platform.customer.dto.*;
import kh.edu.istad.platform.customer.restapi.dto.*;
import kh.edu.istad.platform.customer.valueobject.Email;
import kh.edu.istad.platform.customer.valueobject.PhoneNumber;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerWebMapper
{
    InitiateCustomerCommand toCommand(CustomerInitiateRequest request);

    CustomerInitiateResponse toResponse(InitiateCustomerResult result);

    // Dynamic Filter Mappings (MapStruct handles the List and Record fields!)
    CustomerResponse toResponse(CustomerResult result);

    PaginatedResponse <CustomerResponse> toPaginatedResponse(PaginatedResponse<CustomerResult> paginatedResult);

    UpdateCustomerCommand toCommand(CustomerUpdateRequest request);
    CustomerSegmentCommand toCommand(CustomerSegmentRequest request);
    CustomerSegmentResponse toResponse(CustomerSegmentResult result);

    default Email mapEmail(String email) {
        return new Email(email);
    }
    default PhoneNumber mapPhoneNumber(String phoneNumber) {
        return new PhoneNumber(phoneNumber);
    }
}
