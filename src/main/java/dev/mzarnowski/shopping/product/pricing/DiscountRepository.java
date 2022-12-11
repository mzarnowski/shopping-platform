package dev.mzarnowski.shopping.product.pricing;

import java.util.List;
import java.util.UUID;

@FunctionalInterface
interface DiscountRepository {
    List<Discount> getApplicableDiscountFor(UUID productId, long quantity);
}