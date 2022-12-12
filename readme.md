# Dockerized application

To build the image, run

```sh
./gradlew build-docker-image
```

Image will be called `shopping-platform:${VERSION}`
Rest endpoint listens on the port 8080 within the container.

## Running the container

```sh
docker container run 
      -e JAVA_OPTS="<property-1> <property-2> <property-n>" \
      -i -p <host-port>:8080 \ 
      --rm -t shopping-platform:<version>
```

Application can be configured by specifying various properties (in JAVA_OPTS argument). They must follow
the standard format: `-D<property-name>=<property-value>`.

The `discount.conflict.resolution` property is the only one necessary, since it doesn't seem like there is a "reasonable
default" for that.

e.g.

```sh
docker container run -e JAVA_OPTS="-Ddiscount.conflict.resolution=single-highest-discount" -i -p 8080:8080 --rm -t shopping-platform:1.0-SNAPSHOT
```

Properties have to be specified as part of the `JAVA_OPTS` environment variable

Also, see available [discounts](#available-discount-strategies) that can be enabled this way

## Predefined products

These are the four predefined products, along with their base unit-price:

| product-id | price |
|--|--|
| `aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa` | 1.99 |
| `bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb` | 0.10 |
| `cccccccc-cccc-cccc-cccc-cccccccccccc` | 99.99 |
| `dddddddd-dddd-dddd-dddd-dddddddddddd` | 0.01 |

## Using the containerized service

### Checking the unit price

```sh
curl http://localhost:8080/product/<product-id>/price
```

e.g.

```sh
curl http://localhost:8080/product/dddddddd-dddd-dddd-dddd-dddddddddddd/price
```

### Checking the price for *N* items of a product

```sh
curl http://localhost:8080/product/<product-id>/price?quantity=<value-greater-than-zero>
```

e.g.

```sh
curl http://localhost:8080/product/dddddddd-dddd-dddd-dddd-dddddddddddd/price?quantity=1234
```

## Directory layout

Application is installed in the container in `/opt/shopping-platform`. It consists of two directories:

- `lib` - with the classpath of the application
- `bin` - with the startup scripts


# REST API

## Price the products

`/product/<product-id>/price[?quantity=<value-greater-than-zero>]`

This endpoint allows us to check the price for the given quantity (default = 1) for the specified product.
It returns the price after the discounts are applied.

# Available discount strategies

By default, none of these strategies is enabled.
Whenever more than one is enabled, the [Discount Resolution Strategy](#discount-conflict-resolution-strategy)
is used to determine the actual logic to be used.

## Absolute Percentage Discount

This discount shaves off a specified percentage off the price, e.g.
given discount of `10%` and price of `20`, the discounted price would be `20 * 0.9 = 18`

use `discount.absolute.percentage=<normalized-value>` to enable the discount.

Note, that: `0 <= <normalized-value> <= 1`

## Linear Relative Percentage Discount

The percentage of the discount grows linearly with the number of items.
`percentage = <number-of-items> * <growth-rate>`.

Additionally, the discount can be bounded to ensure it is:

- not lower than the specified minimum value
- not higher than the specified maximum value

use these three properties to enable the discount:

- `discount.linear.min=<normalized-value>`
- `discount.linear.max=0<normalized-value>`
- `discount.linear.rate=<normalized-value>`

Note, that: `0 <= <normalized-value> <= 1`

## Further development

### Multiple discounts of the same kind

Currently, system supports only one of the two available discounts.
There is no reason for it to do so, except development time conservation.

We could extend the system to support multiple discounts, e.g. various discount rates with different boundaries could
give different incentives to the user.

### Discount DSL

Current implementation relies solely on the system properties. It greatly limits the ability to quickly define new (even
time based) discounts.

The benefit of such a declarative interface is easy of use. With it, not only developers could define new discounts.
Now,
"business people" could use it, through e.g. web interface to define new, and edit current discounts.

One idea is to use a simplest, "lispy" form. It abstracts over syntax, so the learning curve is only to understand the
possible clauses, which would have to be done with e.g. JSON, YAML, XML or even python anyway.

```lisp
(discount (given
            (product <product-id>)
            (time-after 22:00)) 
          (when (< quantity 10))
          (then (* price 0.97)))
```

### Non-static discounts

Currently, discounts are loaded on application startup. Introducing new discount should not require the whole
application to be restarted.
What is worse, removing invalid discount should not require restarting all the running pricing services.

### New discount types

With the proposed interfaces (`(product-id, quantity) -> discount-strategy`
and  `(total-price, quantity) -> discounted-price`), it is easy to introduce new discount strategies and to enable them
for the specific products (and quantities) only.

### User-based discounts

Current interfaces doesn't allow for discounts for VIPs or other privileged groups. 
The interface would have to be `(user-id, product-id, quantity) -> discount-strategy`.
It would require to passing the user-id in the REST request. It would complicate the domain twofold:
- we would have to think about not exposing user data,
- we would increase the coupling between services in the system

# Discount Conflict Resolution Strategy

When applying discount, it might happen that more than one discount strategy is available for the given product and
quantity.

## Single Highest Discount

Given multiple discounts, use the one best for the buyer.

Enable with property:
`discount.conflict.resolution=single-highest-discount`

## Further development

### More conflict resolution strategies
Those should be driven by the business. 

### Discount Conflict Resolution DSL 
In general, a DSL could also be applies here in some form, but it seems like
smaller priority than for discounts