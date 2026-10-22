package kh.edu.istad.platform.order.entity;

import kh.edu.istad.common.domain.entity.AggreateRoot;
import kh.edu.istad.common.domain.valueobject.BusinessId;

import java.util.List;

public class Business extends AggreateRoot<BusinessId> {

    private final List<Product> products;
    private boolean active;

    // Constructor
    public Business(List<Product> products, boolean active) {
        this.products = products;
        this.active = active;
    }

    // Constructor
    private Business(Builder builder) {
        super.setId(builder.businessId);
        this.products = builder.products;
        this.active = builder.active;
    }

    // Getters
    public List<Product> getProducts() {
        return products;
    }
    public boolean isActive() {
        return active;
    }

    public static Builder builder() { return new Builder(); }


    public static final class Builder {
        private BusinessId businessId;
        private List<Product> products;
        private boolean active;

        public Builder businessId(BusinessId val) { businessId = val; return this; }
        public Builder products(List<Product> val) { products = val; return this; }
        public Builder active(boolean val) { active = val; return this; }
        public Business build() { return new Business(this); }
    }

}
