package dev.mzarnowski.shopping.product.pricing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
class StaticDiscountsConfiguration {
    @Bean
    @ConditionalOnProperty("discount.linear.min")
    Discount linearDiscountPolicy(@Value("${discount.linear.min}") BigDecimal min,
                                  @Value("${discount.linear.max}") BigDecimal max,
                                  @Value("${discount.linear.rate}") BigDecimal rate) {
        return Discount.linearRelative(min, max, rate);
    }

    @Bean
    @ConditionalOnProperty("discount.absolute.percentage")
    Discount absoluteDiscountPolicy(@Value("${discount.absolute.percentage}") BigDecimal value) {
        return Discount.of(value);
    }

    @Bean
    DiscountRepository staticDiscountRepository(List<Discount> discounts) {
        return (id, quantity) -> discounts;
    }
}
