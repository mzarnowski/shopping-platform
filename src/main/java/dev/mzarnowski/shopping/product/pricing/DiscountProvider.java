package dev.mzarnowski.shopping.product.pricing;

import java.util.UUID;

@FunctionalInterface
interface DiscountProvider {
    Discount getApplicableDiscountFor(UUID productId, long quantity);
}
