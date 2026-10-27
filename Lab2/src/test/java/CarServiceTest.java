import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CarServiceTest {

    @Test
    public void shouldCompleteOrderLifecycleThroughService() {
        final CarServiceStorage storage = new CarServiceStorage();
        final RepairOrderRepository repository = new InMemoryRepairOrderRepository();
        final PricingPolicy policy = new PriceCalculator(new ArrayList<>());
        final CarService service = new CarService(storage, repository, policy);

        final Client client = service.registerClient("Jane Doe", "+380661234567");
        final Car car = service.registerCar("VIN001", "ZAZ", "Lanos", 2008, client);
        final Mechanic mechanic = service.registerMechanic("John", "Engine");
        final RepairOrder order = service.createOrder(car);
        final long orderId = order.getId();
        final ServiceItem item = new ServiceItem("Oil change", new BigDecimal("100.00"), new BigDecimal("50.00"));

        service.addWorkItem(orderId, item);
        service.diagnoseOrder(orderId);
        service.approveOrder(orderId);
        service.assignMechanic(orderId, mechanic);
        service.startOrder(orderId);
        service.completeWorkItem(orderId, item);
        service.completeOrder(orderId);

        final List<RepairOrder> completedOrders = service.findOrdersByStatus(OrderStatus.COMPLETED);
        Assertions.assertEquals(1, completedOrders.size());
        Assertions.assertEquals(new BigDecimal("150.00"), service.calculateBasePrice(orderId));
        Assertions.assertEquals(new BigDecimal("150.00"), service.calculateCommercialPrice(orderId));
        Assertions.assertEquals(storage, service.getStorage());
        Assertions.assertEquals(repository, service.getOrderRepository());
        Assertions.assertEquals(policy, service.getPricingPolicy());
    }

    @Test
    public void shouldCancelOrderThroughService() {
        final CarServiceStorage storage = new CarServiceStorage();
        final RepairOrderRepository repository = new InMemoryRepairOrderRepository();
        final PricingPolicy policy = new PriceCalculator(new ArrayList<>());
        final CarService service = new CarService(storage, repository, policy);

        final Client client = service.registerClient("Jane Doe", "+380661234567");
        final Car car = service.registerCar("VIN001", "ZAZ", "Lanos", 2008, client);
        final RepairOrder order = service.createOrder(car);

        service.cancelOrder(order.getId());

        Assertions.assertEquals(OrderStatus.CANCELLED, service.getOrder(order.getId()).getStatus());
    }

    @Test
    public void shouldRejectInvalidServiceCalls() {
        final CarServiceStorage storage = new CarServiceStorage();
        final RepairOrderRepository repository = new InMemoryRepairOrderRepository();
        final PricingPolicy policy = new PriceCalculator(new ArrayList<>());
        final CarService service = new CarService(storage, repository, policy);

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.setStorage(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.setOrderRepository(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.setPricingPolicy(null));

        final Client client = service.registerClient("Jane", "+380");
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.registerCar(null, "ZAZ", "Lanos", 2008, client));

        service.registerCar("VIN001", "ZAZ", "Lanos", 2008, client);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.registerCar("VIN001", "ZAZ", "Lanos", 2008, client));

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.createOrder(null));
        final Car unregisteredCar = new Car("VIN999", "ZAZ", "Lanos", 2008, client);
        Assertions.assertThrows(IllegalStateException.class, () -> service.createOrder(unregisteredCar));
    }

    @Test
    public void shouldDelegatePriceCalculationUsingMockito() {
        final CarServiceStorage storage = new CarServiceStorage();
        final RepairOrderRepository mockRepo = Mockito.mock(RepairOrderRepository.class);
        final PricingPolicy mockPolicy = Mockito.mock(PricingPolicy.class);
        final CarService service = new CarService(storage, mockRepo, mockPolicy);

        final Client client = new Client(1L, "Jane", "+38000");
        final Car car = new Car("VIN1", "ZAZ", "Lanos", 2008, client);
        final RepairOrder order = new RepairOrder(10L, car);
        order.addWorkItem(new ServiceItem("Fix", new BigDecimal("200.00"), new BigDecimal("100.00")));

        Mockito.when(mockRepo.findById(10L)).thenReturn(order);
        Mockito.when(mockPolicy.calculatePrice(new BigDecimal("300.00"))).thenReturn(new BigDecimal("270.00"));

        final BigDecimal commercialPrice = service.calculateCommercialPrice(10L);

        Assertions.assertEquals(new BigDecimal("270.00"), commercialPrice);
        Mockito.verify(mockRepo).findById(10L);
        Mockito.verify(mockPolicy).calculatePrice(new BigDecimal("300.00"));
    }

    @Test
    public void shouldRunDemoMainWithoutErrors() {
        CarServiceDemo.main(new String[0]);
    }
}