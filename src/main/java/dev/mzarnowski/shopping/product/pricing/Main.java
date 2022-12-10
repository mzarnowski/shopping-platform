package dev.mzarnowski.shopping.product.pricing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    UnitPriceProvider staticPriceProvider() {
        var staticPrices = Map.of(
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), BigDecimal.valueOf(1.99D),
                UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"), BigDecimal.valueOf(0.1D),
                UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"), BigDecimal.valueOf(99.99D),
                UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"), BigDecimal.valueOf(0.01D)
        );

        return productId -> Optional.ofNullable(staticPrices.get(productId));
    }

    @Bean
    PriceRoundingStrategy staticRoundingStrategy() {
        var smallestValue = BigDecimal.valueOf(1, 2);
        return (price) -> new Price(smallestValue.max(price.value()).stripTrailingZeros());
    }

    @Bean
    DiscountProvider staticDiscountProvider() {
        var ninetyPercent = BigDecimal.valueOf(9, 1);
        return (id, quantity) -> (price) -> new Price(price.value().multiply(ninetyPercent));
    }

    @Bean
    ProductPricingService service(UnitPriceProvider unitPriceProvider, DiscountProvider discountProvider, PriceRoundingStrategy roundingStrategy) {
        return new ProductPricingService(unitPriceProvider, discountProvider, roundingStrategy);
    }
}
