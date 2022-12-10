package dev.mzarnowski.shopping.product.pricing;

interface Discount {
    Discount NONE = x -> x;

    Price applyTo(Price price);
}
