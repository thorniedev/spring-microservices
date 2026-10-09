package kh.edu.istad.platform.customer.entity;

import kh.edu.istad.common.domain.entity.AggreateRoot;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.execption.CustomerDomainException;
import kh.edu.istad.platform.customer.valueobject.CustomerStatus;

import java.util.UUID;

public class Customer extends AggreateRoot<CustomerId>
{
    // Some columns "final" some not cuz business logic

    // private final CustomerId customerId;
    private final String username;
    private String familyName;
    private String givenName;
    private final String email;
    private final String phoneNumber;
    private CustomerStatus status;


    // Domain Critical Logic
    public void initiateCustomer(){
        validateCustomer();
        super.setId(new CustomerId(UUID.randomUUID()));
        status = CustomerStatus.ACTIVE;
    }

    private void validateCustomer() {
        if (super.getId() != null) {
            throw new CustomerDomainException("Customer id must be null");
        }
        if(status != null) {
            throw new CustomerDomainException("Customer status must be null");
        }
    }

    public void updateCustomer(String familyName, String givenName) {
        if (familyName == null || givenName == null) {
            throw new CustomerDomainException("Customer familyName and givenName must be not null");
        }
        this.familyName = familyName;
        this.givenName = givenName;
    }

    public void deactivateCustomer() {
        if (status != CustomerStatus.ACTIVE) {
            throw new CustomerDomainException("Could not deactivate customer. Customer is already inactive");
        }
        status = CustomerStatus.INACTIVE;
    }

    private Customer(Builder builder) {
        super.setId(builder.id);
        username = builder.username;
        familyName = builder.familyName;
        givenName = builder.givenName;
        email = builder.email;
        phoneNumber = builder.phoneNumber;
        status = builder.status;
    }

    public static final class Builder {
        private CustomerId id;
        private String username;
        private String familyName;
        private String givenName;
        private String email;
        private String phoneNumber;
        private CustomerStatus status;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(CustomerId val) {
            id = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder familyName(String val) {
            familyName = val;
            return this;
        }

        public Builder givenName(String val) {
            givenName = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder phoneNumber(String val) {
            phoneNumber = val;
            return this;
        }

        public Builder status(CustomerStatus val) {
            status = val;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }

    public String getUsername() {
        return username;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public CustomerStatus getStatus() {
        return status;
    }


    public static Builder builder() {
        return Builder.builder();
    }
}
