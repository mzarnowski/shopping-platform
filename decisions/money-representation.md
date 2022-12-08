# Decision
When working with money, we have to decide on its data carrier type.

Given that we will be applying division to the value many times,
we must avoid the precision loss.

We have decided to use BigDecimals to keep the precision, accepting that the computations will be slower. 

# Alternatives
- float, double - not suitable for representing money due to rounding errors and imprecise calculations
- int, long - could represent the value expressed in the subunit. Easier to use, but doesn't work nicely with division, would probably lead to miscalculations when composing multiple discounts
- pair of values for (main-unit, sub-unit) - harder to use