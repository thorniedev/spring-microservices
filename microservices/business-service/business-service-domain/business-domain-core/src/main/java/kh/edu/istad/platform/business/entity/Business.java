package kh.edu.istad.platform.business.entity;

import kh.edu.istad.common.domain.entity.AggreateRoot;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderApprovalStatus;
import kh.edu.istad.common.domain.valueobject.OrderStatus;
import kh.edu.istad.platform.business.exception.BusinessDomainException;
import kh.edu.istad.platform.business.valueobject.BusinessContact;
import kh.edu.istad.platform.business.valueobject.BusinessName;
import kh.edu.istad.platform.business.valueobject.OrderApprovalId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Business extends AggreateRoot<BusinessId> {
    private final BusinessName businessName;
    private final BusinessContact businessContact;
    private boolean active;
    private List<Product> products;
    private OrderApproval orderApproval;
    private OrderDetail orderDetail;

    public void validateBusiness() {
        if (getId() != null) {
            throw new BusinessDomainException("Business id must be null for new registration!");
        }
        if (businessName == null) {
            throw new BusinessDomainException("Business name must not be null!");
        }
        if (businessContact == null) {
            throw new BusinessDomainException("Business contact must not be null!");
        }
    }

    public void initializeBusiness() {
        super.setId(new BusinessId(UUID.randomUUID()));
        this.active = true;
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
    }

    public void validateOrder(OrderDetail detail) {
        if (!active) {
            throw new BusinessDomainException("Business is not active!");
        }
        if (detail.getOrderStatus() != OrderStatus.PAID) {
            throw new BusinessDomainException("Payment is not completed for order: " + detail.getId().getValue());
        }

        Money total = detail.getProducts().stream()
                .map(Product::getPrice)
                .reduce(Money.ZERO, Money::add);

        if (!detail.getTotalAmount().equals(total)) {
            throw new BusinessDomainException("Order total does not match products total!");
        }
        this.orderDetail = detail;
    }

    public void constructOrderApproval(OrderApprovalStatus status) {
        if (orderDetail == null) {
            throw new BusinessDomainException("Cannot construct order approval without order detail!");
        }
        this.orderApproval = new OrderApproval(
                new OrderApprovalId(UUID.randomUUID()),
                super.getId(),
                orderDetail.getId(),
                status
        );
    }

    private Business(Builder builder) {
        super.setId(builder.businessId);
        businessName = builder.businessName;
        businessContact = builder.businessContact;
        active = builder.active;
        products = builder.products != null ? builder.products : new ArrayList<>();
        orderApproval = builder.orderApproval;
        orderDetail = builder.orderDetail;
    }

    public BusinessName getBusinessName() { return businessName; }
    public BusinessContact getBusinessContact() { return businessContact; }
    public boolean isActive() { return active; }
    public List<Product> getProducts() { return products; }
    public OrderApproval getOrderApproval() { return orderApproval; }
    public OrderDetail getOrderDetail() { return orderDetail; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private BusinessId businessId;
        private BusinessName businessName;
        private BusinessContact businessContact;
        private boolean active;
        private List<Product> products;
        private OrderApproval orderApproval;
        private OrderDetail orderDetail;

        public Builder businessId(BusinessId val) { businessId = val; return this; }
        public Builder businessName(BusinessName val) { businessName = val; return this; }
        public Builder businessContact(BusinessContact val) { businessContact = val; return this; }
        public Builder active(boolean val) { active = val; return this; }
        public Builder products(List<Product> val) { products = val; return this; }
        public Builder orderApproval(OrderApproval val) { orderApproval = val; return this; }
        public Builder orderDetail(OrderDetail val) { orderDetail = val; return this; }
        public Business build() { return new Business(this); }
    }
}