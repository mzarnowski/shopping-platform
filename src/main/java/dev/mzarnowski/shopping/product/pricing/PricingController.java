package dev.mzarnowski.shopping.product.pricing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
class PricingController {
    private final ProductPricingService pricingService;

    PricingController(ProductPricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping(path = "/product/{id}/price")
    public Price price(@PathVariable UUID id, Optional<Long> quantity) {
        try {
            var price = pricingService.priceProducts(id, quantity.orElse(1L));
            return price.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "No such product: " + id));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }
}
