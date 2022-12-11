package dev.mzarnowski.shopping.product.pricing;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static dev.mzarnowski.shopping.product.pricing.DiscountConflictResolutionStrategy.SINGLE_HIGHEST_DISCOUNT;

class SingleBestDiscountConflictResolutionStrategyTest {
    @Test
    public void selects_best_discount() {
        // given discounts
        var discounts = List.of(
                Discount.of(new BigDecimal("0.1")),
                Discount.of(new BigDecimal("0.2")),
                Discount.of(new BigDecimal("0.5")));

        // when selecting the best one
        var discount = SINGLE_HIGHEST_DISCOUNT.resolve(discounts);

        // then selected discount is the highest
        var discounted = discount.applyTo(new Price(BigDecimal.TEN), 1);
        Assertions.assertThat(discounted.value()).isEqualTo(new BigDecimal("5.0"));
    }
}