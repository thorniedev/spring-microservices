package kh.edu.istad.platform.business.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;
    private int quantity;
    private boolean available;

    public Product(ProductId productId, String name, Money price, int quantity, boolean available) {
        super.setId(productId);
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.available = available;
    }

    public String getName() { return name; }
    public Money getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public boolean isAvailable() { return available; }
}