package dev.mzarnowski.shopping.product.pricing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class LinearRelativeDiscountTest {
    private static final Price PRICE = new Price(new BigDecimal("9.99"));

    private static final Discount LINEAR_DISCOUNT =
            Discount.linearRelative(new BigDecimal("0.1"), new BigDecimal("0.3"), new BigDecimal("0.07"));

    @Test
    void applies_linear_relative_discount_without_rounding() {
        // when applying linear discount
        var discountedPrice = applyLinearDiscountToQuantity(2);

        // then the result is not rounded
        assertThat(discountedPrice.value()).isGreaterThanOrEqualTo(new BigDecimal("0.1"));
    }

    @Test
    void linear_relative_discount_does_not_exceed_min_threshold() {
        // when applied linear discount would exceed its min threshold
        var discountedPrice = applyLinearDiscountToQuantity(1);

        // then discount doesn't exceed the max threshold
        var appliedDiscount = calculateAppliedDiscount(discountedPrice, 1);
        assertThat(appliedDiscount).isGreaterThanOrEqualTo(new BigDecimal("0.1"));
    }

    @Test
    void linear_relative_discount_does_not_exceed_max_threshold() {
        // when applied linear discount would exceed its max threshold
        var discountedPrice = applyLinearDiscountToQuantity(10);

        // then discount doesn't exceed the max threshold
        var appliedDiscount = calculateAppliedDiscount(discountedPrice, 10);
        assertThat(appliedDiscount).isLessThanOrEqualTo(new BigDecimal("0.3"));
    }

    @Test
    void linear_relative_discount_uses_linear_ratio() {
        // given linear discount
        var discount = LINEAR_DISCOUNT;

        // when applied to twice as big quantity
        var smallerQuantityPrice = applyLinearDiscountToQuantity(2);
        var biggerQuantityPrice = applyLinearDiscountToQuantity(4);

        // then discount for bigger quantity should be proportionally bigger
        var smallerQuantityDiscount = calculateAppliedDiscount(smallerQuantityPrice, 2);
        var biggerQuantityDiscount = calculateAppliedDiscount(biggerQuantityPrice, 4);

        assertThat(biggerQuantityDiscount).isEqualTo(smallerQuantityDiscount.multiply(BigDecimal.valueOf(2)));
    }

    private Price applyLinearDiscountToQuantity(long quantity) {
        return LINEAR_DISCOUNT.applyTo(PRICE.multiplyBy(BigDecimal.valueOf(quantity)), quantity);
    }

    private BigDecimal calculateAppliedDiscount(Price price, long quantity) {
        var unitPrice = price.value().divide(BigDecimal.valueOf(quantity));
        return PRICE.value().subtract(unitPrice).divide(PRICE.value());
    }
}