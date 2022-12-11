package dev.mzarnowski.shopping.product.pricing;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DiscountConflictResolutionStrategyConfiguration {
    @Bean
    @ConditionalOnExpression("#{'${discount.conflict.resolution}' == 'single-highest-discount'}")
    DiscountConflictResolutionStrategy singleHighestDiscount() {
        return DiscountConflictResolutionStrategy.SINGLE_HIGHEST_DISCOUNT;
    }
}
