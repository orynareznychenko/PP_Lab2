import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RepairOrderTest {

    private Car createSampleCar() {
        final Client client = new Client(1L, "Jane", "+380");
        return new Car("VIN001", "ZAZ", "Lanos", 2008, client);
    }

    @Test
    public void shouldExecuteFullOrderLifecycle() {
        final Car car = createSampleCar();
        final RepairOrder order = new RepairOrder(1L, car);
        final ServiceItem item = new ServiceItem("Oil", new BigDecimal("100.00"), new BigDecimal("50.00"));
        final Mechanic firstMechanic = new Mechanic(1L, "John", "Engine");
        final Mechanic secondMechanic = new Mechanic(2L, "Alex", "Engine");

        order.addWorkItem(item);
        order.diagnose();
        order.approve();
        order.assignMechanic(firstMechanic);
        order.assignMechanic(secondMechanic);
        order.startProgress();
        order.completeWorkItem(item);
        order.completeOrder();

        Assertions.assertEquals(1L, order.getId());
        Assertions.assertEquals(car, order.getCar());
        Assertions.assertEquals(secondMechanic, order.getMechanic());
        Assertions.assertEquals(OrderStatus.COMPLETED, order.getStatus());
        Assertions.assertEquals(1, order.getItems().size());
        Assertions.assertEquals(new BigDecimal("150.00"), order.calculateBasePrice());
        Assertions.assertFalse(firstMechanic.isBusy());
        Assertions.assertFalse(secondMechanic.isBusy());
    }

    @Test
    public void shouldCancelOrderWithMechanic() {
        final RepairOrder order = new RepairOrder(createSampleCar());
        final Mechanic mechanic = new Mechanic(1L, "John", "Engine");
        final ServiceItem item = new ServiceItem("Oil", BigDecimal.TEN, BigDecimal.TEN);

        order.diagnose();
        order.addWorkItem(item);
        order.assignMechanic(mechanic);
        order.cancelOrder();

        Assertions.assertEquals(OrderStatus.CANCELLED, order.getStatus());
        Assertions.assertFalse(mechanic.isBusy());
    }

    @Test
    public void shouldCancelOrderWithoutMechanic() {
        final RepairOrder order = new RepairOrder(createSampleCar());

        order.cancelOrder();

        Assertions.assertEquals(OrderStatus.CANCELLED, order.getStatus());
        Assertions.assertThrows(IllegalStateException.class, order::diagnose);
    }

    @Test
    public void shouldRejectInvalidOrderOperations() {
        final Car car = createSampleCar();
        Assertions.assertThrows(IllegalArgumentException.class, () -> new RepairOrder(0L, car));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new RepairOrder(null));

        final RepairOrder order = new RepairOrder(1L, car);
        Assertions.assertThrows(IllegalArgumentException.class, () -> order.addWorkItem(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> order.assignMechanic(null));
        Assertions.assertThrows(IllegalStateException.class, order::approve);
        Assertions.assertThrows(IllegalStateException.class, order::startProgress);
        Assertions.assertThrows(IllegalArgumentException.class, () -> order.completeWorkItem(null));

        final ServiceItem item = new ServiceItem("Oil", BigDecimal.TEN, BigDecimal.TEN);
        final ServiceItem otherItem = new ServiceItem("Other", BigDecimal.TEN, BigDecimal.TEN);
        order.addWorkItem(item);
        Assertions.assertThrows(IllegalStateException.class, () -> order.completeWorkItem(item));
        Assertions.assertThrows(IllegalStateException.class, order::completeOrder);

        order.diagnose();
        Assertions.assertThrows(IllegalStateException.class, order::diagnose);
        order.approve();
        Assertions.assertThrows(IllegalStateException.class, () -> order.addWorkItem(otherItem));
        Assertions.assertThrows(IllegalStateException.class, order::startProgress);

        final Mechanic mechanic = new Mechanic(1L, "John", "Engine");
        order.assignMechanic(mechanic);
        order.startProgress();
        Assertions.assertThrows(IllegalArgumentException.class, () -> order.completeWorkItem(otherItem));
        Assertions.assertThrows(IllegalStateException.class, order::completeOrder);

        order.completeWorkItem(item);
        order.completeOrder();
        Assertions.assertThrows(IllegalStateException.class, order::cancelOrder);
    }
}