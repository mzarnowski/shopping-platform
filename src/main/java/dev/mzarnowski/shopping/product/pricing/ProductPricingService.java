package dev.mzarnowski.shopping.product.pricing;

import java.math.BigDecimal;
import java.util.UUID;

public final class ProductPricingService {
    private final UnitPriceProvider unitPriceProvider;
    private final DiscountProvider discountProvider;

    public ProductPricingService(UnitPriceProvider unitPriceProvider, DiscountProvider discountProvider) {
        this.unitPriceProvider = unitPriceProvider;
        this.discountProvider = discountProvider;
    }

    Price priceProducts(UUID productId, long quantity) {
        var unitPrice = unitPriceProvider.apply(productId);
        var totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));

        var discount = discountProvider.getApplicableDiscountFor(productId, quantity);
        return discount.applyTo(new Price(totalPrice));
    }
}
