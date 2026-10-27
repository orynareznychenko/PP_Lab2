import java.math.BigDecimal;

@FunctionalInterface
public interface PricingPolicy {
    BigDecimal calculatePrice(final BigDecimal basePrice);
}