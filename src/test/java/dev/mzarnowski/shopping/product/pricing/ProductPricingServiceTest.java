package dev.mzarnowski.shopping.product.pricing;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

class ProductPricingServiceTest {
    public static final UUID PRODUCT_ID = UUID.randomUUID();
    public static final int QUANTITY = 10_000_000;
    public static final BigDecimal UNIT_PRICE = BigDecimal.valueOf(999, 3); // 9.99
    public static final UnitPriceProvider UNIT_PRICE_PROVIDER = id -> Optional.of(UNIT_PRICE);
    public static final PriceRoundingStrategy NO_ROUNDING = x -> x;

    @Test
    public void total_price_is_calculated_by_multiplying_quantity_and_unit_price() {
        var service = new ProductPricingService(UNIT_PRICE_PROVIDER, (id, n) -> Discount.NONE, NO_ROUNDING);

        // when calculating price
        var price = service.priceProducts(PRODUCT_ID, QUANTITY);

        // then final price is based only on the unit price
        var expectedTotalPrice = UNIT_PRICE.multiply(BigDecimal.valueOf(QUANTITY));
        Assertions.assertThat(price).contains(new Price(expectedTotalPrice));
    }

    @Test
    public void applies_discount() {
        // given a discount
        Discount discount = (originalPrice) -> new Price(BigDecimal.ONE);
        var service = new ProductPricingService(UNIT_PRICE_PROVIDER, (id, n) -> discount, NO_ROUNDING);

        // when calculating price
        var price = service.priceProducts(PRODUCT_ID, QUANTITY);

        // then the final price is the one after the discount
        Assertions.assertThat(price).contains(new Price(BigDecimal.ONE));
    }

    @Test
    public void rounds_discounted_price() {
        // given a discount and rounding strategy
        Discount discount = (price) -> new Price(BigDecimal.ONE);
        PriceRoundingStrategy rounding = (price) -> new Price(BigDecimal.TEN);
        var service = new ProductPricingService(UNIT_PRICE_PROVIDER, (id, n) -> discount, rounding);

        // when calculating price
        var price = service.priceProducts(PRODUCT_ID, QUANTITY);

        // then the final price is the one after the being rounded
        Assertions.assertThat(price).contains(new Price(BigDecimal.TEN));
    }

    @Test
    public void supports_only_positive_quantities() {
        // given
        var service = new ProductPricingService(UNIT_PRICE_PROVIDER, (id, n) -> Discount.NONE, NO_ROUNDING);

        // then cannot calculate price with non-positive quantity
        Assertions.assertThatThrownBy(() -> service.priceProducts(PRODUCT_ID, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
