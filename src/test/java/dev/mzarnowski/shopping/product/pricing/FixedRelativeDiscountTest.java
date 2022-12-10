package dev.mzarnowski.shopping.product.pricing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class FixedRelativeDiscountTest {
    private static final Price PRICE = new Price(new BigDecimal("9.99"));

    @Test
    void applies_fixed_discount_without_rounding() {
        // given fixed discount
        var discount = Discount.of(new BigDecimal("0.1"));

        // when applied
        var discounted = discount.applyTo(PRICE, 1);

        // then the result is not rounded
        assertThat(discounted.value()).isEqualTo(new BigDecimal("8.991"));
    }
}