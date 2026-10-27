import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PriceCalculatorTest {

    @ParameterizedTest
    @CsvSource({
            "1000.00, 10, 200.00, 900.00",
            "500.00, 20, 200.00, 400.00",
            "100.00, 50, 200.00, 200.00",
            "0.00, 15, 0.00, 0.00"
    })
    public void shouldCalculateCommercialPriceWithRules(
            final String baseStr,
            final String discountStr,
            final String minStr,
            final String expectedStr
    ) {
        final DiscountPricingRule discountRule = new DiscountPricingRule(new BigDecimal(discountStr));
        final MinimumPriceRule minimumRule = new MinimumPriceRule(new BigDecimal(minStr));
        final List<PricingRule> rules = new ArrayList<>();
        rules.add(discountRule);
        rules.add(minimumRule);
        final PriceCalculator calculator = new PriceCalculator(rules);

        final BigDecimal actualPrice = calculator.calculatePrice(new BigDecimal(baseStr));

        Assertions.assertEquals(new BigDecimal(expectedStr), actualPrice);
        Assertions.assertEquals(new BigDecimal(discountStr), discountRule.getDiscountPercent());
        Assertions.assertEquals(new BigDecimal(minStr), minimumRule.getMinimumPrice());
        Assertions.assertEquals(2, calculator.getRules().size());
    }

    @Test
    public void shouldApplyFixedSurchargeRule() {
        final FixedSurchargeRule surchargeRule = new FixedSurchargeRule(new BigDecimal("25.00"));
        final List<PricingRule> rules = new ArrayList<>();
        rules.add(surchargeRule);
        final PriceCalculator calculator = new PriceCalculator(rules);

        final BigDecimal result = calculator.calculatePrice(new BigDecimal("100.00"));

        Assertions.assertEquals(new BigDecimal("125.00"), result);
        Assertions.assertEquals(new BigDecimal("25.00"), surchargeRule.getSurchargeAmount());
    }

    @Test
    public void shouldRejectInvalidPricingRuleInputs() {
        final BigDecimal negative = new BigDecimal("-5.00");
        final BigDecimal overHundred = new BigDecimal("105.00");

        Assertions.assertThrows(IllegalArgumentException.class, () -> new DiscountPricingRule(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DiscountPricingRule(negative));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DiscountPricingRule(overHundred));

        final DiscountPricingRule discountRule = new DiscountPricingRule(BigDecimal.TEN);
        Assertions.assertThrows(IllegalArgumentException.class, () -> discountRule.apply(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discountRule.apply(negative));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new MinimumPriceRule(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new MinimumPriceRule(negative));

        final MinimumPriceRule minRule = new MinimumPriceRule(BigDecimal.TEN);
        Assertions.assertThrows(IllegalArgumentException.class, () -> minRule.apply(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> minRule.apply(negative));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new FixedSurchargeRule(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FixedSurchargeRule(negative));

        final FixedSurchargeRule surchargeRule = new FixedSurchargeRule(BigDecimal.TEN);
        Assertions.assertThrows(IllegalArgumentException.class, () -> surchargeRule.apply(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> surchargeRule.apply(negative));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new PriceCalculator(null));
        final PriceCalculator calculator = new PriceCalculator(new ArrayList<>());
        Assertions.assertThrows(IllegalArgumentException.class, () -> calculator.addRule(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> calculator.calculatePrice(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> calculator.calculatePrice(negative));
    }
}