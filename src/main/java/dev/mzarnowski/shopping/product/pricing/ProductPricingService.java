package dev.mzarnowski.shopping.product.pricing;

import java.math.BigDecimal;
import java.util.UUID;

public final class ProductPricingService {
    private final UnitPriceProvider unitPriceProvider;
    private final DiscountProvider discountProvider;
    private final PriceRoundingStrategy roundingStrategy;

    public ProductPricingService(UnitPriceProvider unitPriceProvider, DiscountProvider discountProvider, PriceRoundingStrategy roundingStrategy) {
        this.unitPriceProvider = unitPriceProvider;
        this.discountProvider = discountProvider;
        this.roundingStrategy = roundingStrategy;
    }

    Price priceProducts(UUID productId, long quantity) {
        var unitPrice = unitPriceProvider.apply(productId);
        var totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));

        var discount = discountProvider.getApplicableDiscountFor(productId, quantity);
        var discountedPrice = discount.applyTo(new Price(totalPrice));

        return roundingStrategy.round(discountedPrice);
    }
}
