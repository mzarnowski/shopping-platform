# Decision

When applying discounts to the price,
some discounts might produce values smaller than the lowest denominator (0.01).
We need to apply some kind of rounding, based on the business requirements.

To simplify the logic of every discount, we have decided to apply this (business decided)
rounding once - after the discounts are applied.

The accepted downside is that the pricing module will get a bit more complex.

# Alternatives

- special-cased discount - we could leverage the current interface of the Discount to achieve the same, but this would
  break the metaphor:
    - suddenly, one discount would *have* to be applied at the end
    - in some cases, a *discount* would cause the price to *go up*
- no rounding - we could require downstream modules to apply the rounding themselves, but if there were two separate
  modules, there could be some discrepancies. More so, we are working on a *pricing* module, so we shouldn't delegate
  pricing-related activities to other modules