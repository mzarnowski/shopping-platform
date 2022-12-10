package dev.mzarnowski.shopping.product.pricing;

interface PriceRoundingStrategy {
    Price round(Price price);
}
