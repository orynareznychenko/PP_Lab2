import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemoryRepairOrderRepositoryTest {

    @Test
    public void shouldSaveRetrieveUpdateOrders() {
        final InMemoryRepairOrderRepository repository = new InMemoryRepairOrderRepository();
        final Client client = new Client(1L, "Jane Doe", "+380661234567");
        final Car car = new Car("VIN001", "ZAZ", "Lanos", 2008, client);
        final RepairOrder newOrder = new RepairOrder(car);
        final RepairOrder presetOrder = new RepairOrder(50L, car);

        final RepairOrder savedOrder = repository.save(newOrder);
        savedOrder.diagnose();
        repository.save(savedOrder);
        repository.save(presetOrder);

        final RepairOrder foundOrder = repository.findById(savedOrder.getId());
        final List<RepairOrder> diagnosedOrders = repository.findByStatus(OrderStatus.DIAGNOSED);
        final List<RepairOrder> allOrders = repository.findAll();

        Assertions.assertEquals(1L, savedOrder.getId());
        Assertions.assertEquals(savedOrder, foundOrder);
        Assertions.assertEquals(1, diagnosedOrders.size());
        Assertions.assertEquals(2, allOrders.size());
    }

    @Test
    public void shouldRejectInvalidRepositoryCalls() {
        final InMemoryRepairOrderRepository repository = new InMemoryRepairOrderRepository();

        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.save(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.findById(999L));
        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.findByStatus(null));
    }
}