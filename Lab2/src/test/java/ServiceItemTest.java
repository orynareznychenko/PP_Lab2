import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServiceItemTest {

    @Test
    public void shouldCreateServiceItemSuccessfully() {
        final BigDecimal parts = new BigDecimal("150.00");
        final BigDecimal labor = new BigDecimal("50.00");
        final ServiceItem item = new ServiceItem("Oil change", parts, labor);

        item.markCompleted();
        final BigDecimal total = item.calculateTotalCost();

        Assertions.assertEquals("Oil change", item.getDescription());
        Assertions.assertEquals(parts, item.getPartsCost());
        Assertions.assertEquals(labor, item.getLaborCost());
        Assertions.assertTrue(item.isCompleted());
        Assertions.assertEquals(new BigDecimal("200.00"), total);
    }

    @Test
    public void shouldRejectInvalidServiceItemParameters() {
        final BigDecimal valid = BigDecimal.TEN;
        final BigDecimal negative = new BigDecimal("-1.00");

        Assertions.assertThrows(IllegalArgumentException.class, () -> new ServiceItem(null, valid, valid));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ServiceItem("  ", valid, valid));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ServiceItem("Oil", null, valid));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ServiceItem("Oil", negative, valid));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ServiceItem("Oil", valid, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ServiceItem("Oil", valid, negative));
    }
}