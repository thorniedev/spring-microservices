package kh.edu.istad.platform.customer.restapi.mapper;

import kh.edu.istad.platform.customer.dto.CustomerResult;
import kh.edu.istad.platform.customer.dto.InitiateCustomerCommand;
import kh.edu.istad.platform.customer.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.dto.PaginatedResponse;
import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerResponse;
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

    default Email mapEmail(String email) {
        return new Email(email);
    }

    default PhoneNumber mapPhoneNumber(String phoneNumber) {
        return new PhoneNumber(phoneNumber);
    }
}
