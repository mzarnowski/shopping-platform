package dev.mzarnowski.shopping.product.pricing;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "discount.conflict.resolution=single-highest-discount")
public class PricingControllerTest {
    private static final UUID PRODUCT_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate rest;

    @Test
    public void receives_price_for_products() {
        // given
        var uri = priceProductUri(PRODUCT_ID, 10);

        // when
        var price = rest.getForObject(uri, Price.class);

        // then
        Assertions.assertThat(price).isEqualTo(new Price(new BigDecimal("19.9")));
    }

    @Test
    public void quantity_is_optional() {
        // given
        var uri = URI.create("http://localhost:%s/product/%s/price".formatted(port, PRODUCT_ID));

        // when
        var price = rest.getForObject(uri, Price.class);

        // then
        Assertions.assertThat(price).isEqualTo(new Price(new BigDecimal("1.99")));
    }

    @Test
    public void receives_404_for_non_existing_product() {
        // given
        var uri = priceProductUri(UUID.randomUUID(), 10);

        // when
        var response = rest.getForEntity(uri, Price.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
    }

    @Test
    public void receives_400_for_invalid_quantity() {
        // given
        var uri = priceProductUri(PRODUCT_ID, 0);

        // when
        var response = rest.getForEntity(uri, Price.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    }

    private URI priceProductUri(UUID uuid, long quantity) {
        return URI.create("http://localhost:%s/product/%s/price?quantity=%d".formatted(port, uuid, quantity));
    }
}