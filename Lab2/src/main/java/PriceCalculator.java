import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriceCalculator implements PricingPolicy {
    private List<PricingRule> rules;

    public PriceCalculator(final Iterable<PricingRule> pricingRules) {
        setRules(pricingRules);
    }

    public void setRules(final Iterable<PricingRule> pricingRules) {
        if (pricingRules == null) {
            throw new IllegalArgumentException("Pricing rules cannot be null");
        }
        rules = new ArrayList<>();
        for (final PricingRule rule : pricingRules) {
            addRule(rule);
        }
    }

    public void addRule(final PricingRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("Pricing rule cannot be null");
        }
        rules.add(rule);
    }

    @Override
    public BigDecimal calculatePrice(final BigDecimal basePrice) {
        validateBasePrice(basePrice);
        BigDecimal currentPrice = basePrice;
        for (final PricingRule rule : rules) {
            currentPrice = rule.apply(currentPrice);
        }
        final RoundingMode rounding = RoundingMode.HALF_UP;
        return currentPrice.setScale(2, rounding);
    }

    private void validateBasePrice(final BigDecimal basePrice) {
        if (basePrice == null) {
            throw new IllegalArgumentException("Base price cannot be null");
        }
        final BigDecimal zero = BigDecimal.ZERO;
        final int comparison = basePrice.compareTo(zero);
        if (comparison < 0) {
            throw new IllegalArgumentException("Base price cannot be negative");
        }
    }

    public List<PricingRule> getRules() {
        return Collections.unmodifiableList(rules);
    }
}