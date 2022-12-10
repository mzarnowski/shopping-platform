package dev.mzarnowski.shopping.product.pricing;

import java.math.BigDecimal;

interface Discount {
    Discount NONE = (x, q) -> x;

    Price applyTo(Price price, long quantity);

    private static void assertNormalized(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("value cannot be negative");
        if (value.compareTo(BigDecimal.ONE) > 0)
            throw new IllegalArgumentException("value value must be normalized");
    }

    static Discount of(BigDecimal discount) {
        assertNormalized(discount);

        var multiplier = BigDecimal.ONE.subtract(discount);
        return (price, quantity) -> price.multiplyBy(multiplier);
    }

    static Discount linearRelative(BigDecimal min, BigDecimal max, BigDecimal rate) {
        assertNormalized(min);
        assertNormalized(max);
        assertNormalized(rate);

        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Minimum discount cannot be bigger than the maximum one");
        }

        return (price, quantity) -> {
            var discount = rate.multiply(BigDecimal.valueOf(quantity));
            if (discount.compareTo(min) < 0) discount = min;
            if (discount.compareTo(max) > 0) discount = max;

            return price.multiplyBy(BigDecimal.ONE.subtract(discount));
        };
    }
}
