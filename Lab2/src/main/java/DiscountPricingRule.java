import java.math.BigDecimal;
import java.math.RoundingMode;

public class DiscountPricingRule implements PricingRule {
    private BigDecimal discountPercent;

    public DiscountPricingRule(final BigDecimal percent) {
        setDiscountPercent(percent);
    }

    public void setDiscountPercent(final BigDecimal percent) {
        validateDiscountPercent(percent);
        discountPercent = percent;
    }

    private void validateDiscountPercent(final BigDecimal percent) {
        if (percent == null) {
            throw new IllegalArgumentException("Discount percent cannot be null");
        }
        final BigDecimal zero = BigDecimal.ZERO;
        final BigDecimal hundred = new BigDecimal("100");
        final boolean negative = percent.compareTo(zero) < 0;
        final boolean overHundred = percent.compareTo(hundred) > 0;
        if (negative || overHundred) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100");
        }
    }

    @Override
    public BigDecimal apply(final BigDecimal currentPrice) {
        validatePrice(currentPrice);
        final BigDecimal hundred = new BigDecimal("100");
        final BigDecimal multiplied = currentPrice.multiply(discountPercent);
        final RoundingMode rounding = RoundingMode.HALF_UP;
        final BigDecimal discountAmount = multiplied.divide(hundred, 2, rounding);
        return currentPrice.subtract(discountAmount);
    }

    private void validatePrice(final BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        final BigDecimal zero = BigDecimal.ZERO;
        final int comparison = price.compareTo(zero);
        if (comparison < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }
}