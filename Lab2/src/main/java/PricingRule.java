import java.math.BigDecimal;

@FunctionalInterface
public interface PricingRule {
    BigDecimal apply(final BigDecimal currentPrice);
}