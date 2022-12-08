package dev.mzarnowski.shopping.product.pricing;

import java.math.BigDecimal;
import java.util.UUID;

public final class ProductPricingService {
    private final UnitPriceProvider unitPriceProvider;

    public ProductPricingService(UnitPriceProvider unitPriceProvider) {
        this.unitPriceProvider = unitPriceProvider;
    }

    Price priceProducts(UUID productId, long quantity) {
        var unitPrice = unitPriceProvider.apply(productId);
        var totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        return new Price(totalPrice);
    }
}
