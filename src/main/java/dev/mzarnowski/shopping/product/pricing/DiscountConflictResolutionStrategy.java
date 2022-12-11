package dev.mzarnowski.shopping.product.pricing;

import java.util.Comparator;
import java.util.List;

@FunctionalInterface
interface DiscountConflictResolutionStrategy {
    Discount resolve(List<Discount> discounts);

    DiscountConflictResolutionStrategy SINGLE_HIGHEST_DISCOUNT = (discounts) -> {
        // sadly, this must happen during runtime, since only then we will know the best discount
        return (price, quantity) -> discounts.stream().map(discount -> discount.applyTo(price, quantity))
                .min(Comparator.comparing(Price::value))
                .orElse(price);
    };
}
