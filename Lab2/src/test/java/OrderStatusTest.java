import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderStatusTest {

    @Test
    public void shouldValidateAllTransitions() {
        Assertions.assertFalse(OrderStatus.CREATED.canTransitionTo(null));

        Assertions.assertTrue(OrderStatus.CREATED.canTransitionTo(OrderStatus.DIAGNOSED));
        Assertions.assertFalse(OrderStatus.CREATED.canTransitionTo(OrderStatus.APPROVED));

        Assertions.assertTrue(OrderStatus.DIAGNOSED.canTransitionTo(OrderStatus.APPROVED));
        Assertions.assertFalse(OrderStatus.DIAGNOSED.canTransitionTo(OrderStatus.COMPLETED));

        Assertions.assertTrue(OrderStatus.APPROVED.canTransitionTo(OrderStatus.IN_PROGRESS));
        Assertions.assertFalse(OrderStatus.APPROVED.canTransitionTo(OrderStatus.COMPLETED));

        Assertions.assertTrue(OrderStatus.IN_PROGRESS.canTransitionTo(OrderStatus.COMPLETED));
        Assertions.assertFalse(OrderStatus.IN_PROGRESS.canTransitionTo(OrderStatus.APPROVED));

        Assertions.assertFalse(OrderStatus.COMPLETED.canTransitionTo(OrderStatus.CREATED));
    }

    @Test
    public void shouldValidateCancellationTransitions() {
        Assertions.assertTrue(OrderStatus.CREATED.canTransitionTo(OrderStatus.CANCELLED));
        Assertions.assertTrue(OrderStatus.DIAGNOSED.canTransitionTo(OrderStatus.CANCELLED));
        Assertions.assertTrue(OrderStatus.APPROVED.canTransitionTo(OrderStatus.CANCELLED));
        Assertions.assertFalse(OrderStatus.IN_PROGRESS.canTransitionTo(OrderStatus.CANCELLED));
        Assertions.assertFalse(OrderStatus.COMPLETED.canTransitionTo(OrderStatus.CANCELLED));
    }
}