package kh.edu.istad.platform.customer.entity;

import kh.edu.istad.common.domain.entity.AggreateRoot;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
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
    private String phoneNumber;
    private CustomerStatus status;

    // Added fields for Filter, Segment & Aggregations
    private Money totalSpent;
    private Integer loyaltyPoints;
    private String segmentId; // DDD Manual Reference to CustomerSegment



    // Domain Critical Logic
    public void initiateCustomer() {
        validateCustomerForInitiation();
        super.setId(new CustomerId(UUID.randomUUID()));
        this.status = CustomerStatus.ACTIVE;
        this.totalSpent = Money.ZERO;
        this.loyaltyPoints = 0;
    }

    private void validateCustomerForInitiation() {
        if (super.getId() != null) {
            throw new CustomerDomainException("Customer id must be null before initiation");
        }
        if (status != null) {
            throw new CustomerDomainException("Customer status must be null before initiation");
        }
    }

    public void updateCustomer(String familyName, String givenName) {
        if (familyName == null || familyName.isBlank() || givenName == null || givenName.isBlank()) {
            throw new CustomerDomainException("Customer familyName and givenName must not be empty");
        }
        this.familyName = familyName;
        this.givenName = givenName;
    }
    // 4-argument overload (used by UpdateCustomerUserCase)
    public void updateCustomer(String familyName, String givenName, String phoneNumber, String segmentId) {
        updateCustomer(familyName, givenName);
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            this.phoneNumber = phoneNumber;
        }
        if (segmentId != null) {
            this.segmentId = segmentId;
        }
    }

    public void deactivateCustomer() {
        if (this.status == CustomerStatus.INACTIVE) {
            throw new CustomerDomainException("Could not deactivate customer. Customer is already inactive");
        }
        this.status = CustomerStatus.INACTIVE;
    }

    public void assignSegment(String segmentId) {
        this.segmentId = segmentId;
    }

    // Constructors & Builder
    private Customer(Builder builder) {
        super.setId(builder.id);
        this.username = builder.username;
        this.familyName = builder.familyName;
        this.givenName = builder.givenName;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.status = builder.status;
        this.totalSpent = builder.totalSpent != null ? builder.totalSpent : Money.ZERO;
        this.loyaltyPoints = builder.loyaltyPoints != null ? builder.loyaltyPoints : 0;
        this.segmentId = builder.segmentId;
    }

    public static final class Builder {
        private CustomerId id;
        private String username;
        private String familyName;
        private String givenName;
        private String email;
        private String phoneNumber;
        private CustomerStatus status;
        private Money totalSpent;
        private Integer loyaltyPoints;
        private String segmentId;

        private Builder() {}
        public static Builder builder() { return new Builder(); }
        public Builder id(CustomerId val) { id = val; return this; }
        public Builder username(String val) { username = val; return this; }
        public Builder familyName(String val) { familyName = val; return this; }
        public Builder givenName(String val) { givenName = val; return this; }
        public Builder email(String val) { email = val; return this; }
        public Builder phoneNumber(String val) { phoneNumber = val; return this; }
        public Builder status(CustomerStatus val) { status = val; return this; }
        public Builder totalSpent(Money val) { totalSpent = val; return this; }
        public Builder loyaltyPoints(Integer val) { loyaltyPoints = val; return this; }
        public Builder segmentId(String val) { segmentId = val; return this; }
        public Customer build() { return new Customer(this); }
    }

    public String getUsername() { return username; }
    public String getFamilyName() { return familyName; }
    public String getGivenName() { return givenName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public CustomerStatus getStatus() { return status; }
    public Money getTotalSpent() { return totalSpent; }
    public Integer getLoyaltyPoints() { return loyaltyPoints; }
    public String getSegmentId() { return segmentId; }
    public static Builder builder() { return Builder.builder(); }


//    private void validateCustomer() {
//        if (super.getId() != null) {
//            throw new CustomerDomainException("Customer id must be null");
//        }
//        if(status != null) {
//            throw new CustomerDomainException("Customer status must be null");
//        }
//    }
//
//    public void updateCustomer(String familyName, String givenName) {
//        if (familyName == null || givenName == null) {
//            throw new CustomerDomainException("Customer familyName and givenName must be not null");
//        }
//        this.familyName = familyName;
//        this.givenName = givenName;
//    }
//
//    public void deactivateCustomer() {
//        if (status != CustomerStatus.ACTIVE) {
//            throw new CustomerDomainException("Could not deactivate customer. Customer is already inactive");
//        }
//        status = CustomerStatus.INACTIVE;
//    }

//    private Customer(Builder builder) {
//        super.setId(builder.id);
//        username = builder.username;
//        familyName = builder.familyName;
//        givenName = builder.givenName;
//        email = builder.email;
//        phoneNumber = builder.phoneNumber;
//        status = builder.status;
//    }
//
//    public static final class Builder {
//        private CustomerId id;
//        private String username;
//        private String familyName;
//        private String givenName;
//        private String email;
//        private String phoneNumber;
//        private CustomerStatus status;
//
//        private Builder() {
//        }
//
//        public static Builder builder() {
//            return new Builder();
//        }
//
//        public Builder id(CustomerId val) {
//            id = val;
//            return this;
//        }
//
//        public Builder username(String val) {
//            username = val;
//            return this;
//        }
//
//        public Builder familyName(String val) {
//            familyName = val;
//            return this;
//        }
//
//        public Builder givenName(String val) {
//            givenName = val;
//            return this;
//        }
//
//        public Builder email(String val) {
//            email = val;
//            return this;
//        }
//
//        public Builder phoneNumber(String val) {
//            phoneNumber = val;
//            return this;
//        }
//
//        public Builder status(CustomerStatus val) {
//            status = val;
//            return this;
//        }
//
//        public Customer build() {
//            return new Customer(this);
//        }
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public String getFamilyName() {
//        return familyName;
//    }
//
//    public String getGivenName() {
//        return givenName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public CustomerStatus getStatus() {
//        return status;
//    }
//
//
//    public static Builder builder() {
//        return Builder.builder();
//    }
}
