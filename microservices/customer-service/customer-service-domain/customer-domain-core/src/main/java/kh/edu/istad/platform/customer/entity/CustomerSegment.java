package kh.edu.istad.platform.customer.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.platform.customer.valueobject.SegmentType;

import java.math.BigDecimal;
import java.util.List;

public class CustomerSegment extends BaseEntity<String> {

    private SegmentType segmentType;
    private List<String> benefits;
    private Integer minPoints;
    private Money minSpend;
    private BigDecimal discountRate;

    public CustomerSegment(String id, SegmentType segmentType, List<String> benefits,
                           Integer minPoints, Money minSpend, BigDecimal discountRate) {
        super.setId(id);
        this.segmentType = segmentType;
        this.benefits = benefits != null ? List.copyOf(benefits) : List.of();
        this.minPoints = minPoints != null ? minPoints : 0;
        this.minSpend = minSpend != null ? minSpend : Money.ZERO;
        this.discountRate = discountRate != null ? discountRate : BigDecimal.ZERO;
    }

    public void updateSegment(SegmentType segmentType, List<String> benefits,
                              Integer minPoints, Money minSpend, BigDecimal discountRate) {
        this.segmentType = segmentType;
        this.benefits = benefits != null ? List.copyOf(benefits) : List.of();
        this.minPoints = minPoints != null ? minPoints : 0;
        this.minSpend = minSpend != null ? minSpend : Money.ZERO;
        this.discountRate = discountRate != null ? discountRate : BigDecimal.ZERO;
    }

    public SegmentType getSegmentType() { return segmentType; }
    public List<String> getBenefits() { return benefits; }
    public Integer getMinPoints() { return minPoints; }
    public Money getMinSpend() { return minSpend; }
    public BigDecimal getDiscountRate() { return discountRate; }
}