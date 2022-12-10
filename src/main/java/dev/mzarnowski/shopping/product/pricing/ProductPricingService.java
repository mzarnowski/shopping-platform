package dev.mzarnowski.shopping.product.pricing;

import java.math.BigDecimal;
import java.util.Optional;
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

    Optional<Price> priceProducts(UUID productId, long quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Unsupported quantity: " + quantity);

        var unitPrice = unitPriceProvider.apply(productId);
        return unitPrice.map(it -> calculatePrice(productId, quantity, it));
    }

    private Price calculatePrice(UUID productId, long quantity, BigDecimal unitPrice) {
        var totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));

        var discount = discountProvider.getApplicableDiscountFor(productId, quantity);
        var discountedPrice = discount.applyTo(new Price(totalPrice), quantity);

        return roundingStrategy.round(discountedPrice);
    }
}
