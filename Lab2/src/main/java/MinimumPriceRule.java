import java.math.BigDecimal;

public class MinimumPriceRule implements PricingRule {
    private BigDecimal minimumPrice;

    public MinimumPriceRule(final BigDecimal minPrice) {
        setMinimumPrice(minPrice);
    }

    public void setMinimumPrice(final BigDecimal minPrice) {
        validatePriceValue(minPrice, "Minimum price cannot be negative or null");
        minimumPrice = minPrice;
    }

    private void validatePriceValue(final BigDecimal price, final String message) {
        if (price == null) {
            throw new IllegalArgumentException(message);
        }
        final BigDecimal zero = BigDecimal.ZERO;
        final int comparison = price.compareTo(zero);
        if (comparison < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public BigDecimal apply(final BigDecimal currentPrice) {
        validatePriceValue(currentPrice, "Current price cannot be negative or null");
        final int comparison = currentPrice.compareTo(minimumPrice);
        if (comparison < 0) {
            return minimumPrice;
        }
        return currentPrice;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }
}