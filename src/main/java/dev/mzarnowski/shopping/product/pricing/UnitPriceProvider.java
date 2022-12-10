package dev.mzarnowski.shopping.product.pricing;

import java.math.BigDecimal;
import java.util.UUID;

@FunctionalInterface
interface UnitPriceProvider {
    BigDecimal apply(UUID productId);
}
