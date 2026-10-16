package kh.edu.istad.common.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

// 150$ => new Money(150)
// 100$ => new Money(100)
public record Money(
        BigDecimal amount
) {

    public static final Money ZERO =  new Money(BigDecimal.ZERO);
    public Money(int amount) {
        this(BigDecimal.valueOf(amount));
    }
    public boolean isGreaterThanZero() {
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Money money) {
        return this.amount != null && this.amount.compareTo(money.amount()) > 0;
    }
    public Money add(Money money) {
        return new Money(setScale(this.amount.add(money.amount())));
    }
    public Money subtract(Money money) {
        return new Money(setScale(this.amount.subtract(money.amount())));
    }
    public Money multiply(int multiplier) {
        return new Money(setScale(this.amount.multiply(new BigDecimal(multiplier))));
    }
    private BigDecimal setScale(BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }

//    public void isGreaterThanZero() {
//        if (!(amount.compareTo(BigDecimal.ZERO) > 0)){
//            System.out.println("Money is not greater than zero");
//            throw new RuntimeException("Money is not greater than zero");
//        }
//    }
    // More logic
}
