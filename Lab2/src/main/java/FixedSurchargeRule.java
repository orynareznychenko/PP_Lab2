import java.math.BigDecimal;

public class FixedSurchargeRule implements PricingRule {
    private BigDecimal surchargeAmount;

    public FixedSurchargeRule(final BigDecimal surcharge) {
        setSurchargeAmount(surcharge);
    }

    public void setSurchargeAmount(final BigDecimal surcharge) {
        validateAmount(surcharge);
        surchargeAmount = surcharge;
    }

    private void validateAmount(final BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        final BigDecimal zero = BigDecimal.ZERO;
        final int comparison = amount.compareTo(zero);
        if (comparison < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }

    @Override
    public BigDecimal apply(final BigDecimal currentPrice) {
        validateAmount(currentPrice);
        return currentPrice.add(surchargeAmount);
    }

    public BigDecimal getSurchargeAmount() {
        return surchargeAmount;
    }
}