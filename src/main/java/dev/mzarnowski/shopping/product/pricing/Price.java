package dev.mzarnowski.shopping.product.pricing;

import java.math.BigDecimal;

public record Price(BigDecimal value) {
    public Price multiplyBy(BigDecimal value) {
        return new Price(this.value.multiply(value));
    }
}
