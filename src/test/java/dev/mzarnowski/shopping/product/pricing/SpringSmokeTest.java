package dev.mzarnowski.shopping.product.pricing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "discount.conflict.resolution=single-highest-discount")
public class SpringSmokeTest {
	@Test
	public void contextLoads() {
	}
}