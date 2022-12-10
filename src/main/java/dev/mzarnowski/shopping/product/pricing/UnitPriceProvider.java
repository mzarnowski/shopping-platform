package dev.mzarnowski.shopping.product.pricing;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@FunctionalInterface
interface UnitPriceProvider {
    Optional<BigDecimal> apply(UUID productId);
}
