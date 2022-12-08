package dev.mzarnowski.shopping.product.pricing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductPricingServiceTest {
    public static final UUID PRODUCT_ID = UUID.randomUUID();
    public static final BigDecimal UNIT_PRICE = BigDecimal.valueOf(999, 3); // 9.99

    @Test
    public void applies_only_the_unit_price_when_do_discounts_specified() {
        // given
        var quantity = 10_000_000;

        var service = new ProductPricingService(id -> UNIT_PRICE);

        // when calculating price
        var price = service.priceProducts(PRODUCT_ID, quantity);

        // then final price is based only on the unit price
        var expectedTotalPrice = UNIT_PRICE.multiply(BigDecimal.valueOf(quantity));
        assertEquals(expectedTotalPrice, price.value());
    }
}
