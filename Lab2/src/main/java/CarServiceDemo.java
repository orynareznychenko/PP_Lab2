import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CarServiceDemo {
    public static void main(final String[] args) {
        final CarServiceStorage storage = new CarServiceStorage();
        final RepairOrderRepository orderRepository = new InMemoryRepairOrderRepository();

        final List<PricingRule> rules = new ArrayList<>();
        rules.add(new DiscountPricingRule(new BigDecimal("10")));
        rules.add(new MinimumPriceRule(new BigDecimal("250.00")));
        final PricingPolicy pricingPolicy = new PriceCalculator(rules);

        final CarService service = new CarService(storage, orderRepository, pricingPolicy);

        System.out.println("1. SUCCESSFUL REGISTRATION & ORDER CREATION");
        final Client client = service.registerClient("Jane Doe", "+380661234567");
        System.out.println(client.formatInfo());

        final Car car1 = service.registerCar("VIN001", "ZAZ", "Lanos", 2008, client);
        System.out.println(car1.formatInfo());

        final RepairOrder createdOrder = service.createOrder(car1);
        final long order1Id = createdOrder.getId();
        final RepairOrder fetchedOrder = service.getOrder(order1Id);
        System.out.println("Fetched Order #" + fetchedOrder.getId() + ", status: " + fetchedOrder.getStatus());

        System.out.println("\n2 & 3. FULL LIFECYCLE, BASE PRICE & COMMERCIAL PRICE");
        final ServiceItem item1 = new ServiceItem("Oil change", new BigDecimal("150.00"), new BigDecimal("50.00"));
        final ServiceItem item2 = new ServiceItem("Brake replacement", new BigDecimal("300.00"), new BigDecimal("200.00"));

        service.addWorkItem(order1Id, item1);
        service.addWorkItem(order1Id, item2);
        service.diagnoseOrder(order1Id);
        service.approveOrder(order1Id);

        final Mechanic mechanic1 = service.registerMechanic("John", "Engine master");
        service.assignMechanic(order1Id, mechanic1);
        service.startOrder(order1Id);

        service.completeWorkItem(order1Id, item1);
        service.completeWorkItem(order1Id, item2);
        service.completeOrder(order1Id);

        final BigDecimal basePrice = service.calculateBasePrice(order1Id);
        final BigDecimal commercialPrice = service.calculateCommercialPrice(order1Id);
        System.out.println("Order #" + order1Id + " status: " + service.getOrder(order1Id).getStatus());
        System.out.println("Base price: " + basePrice + " | Commercial price (10% discount): " + commercialPrice);

        System.out.println("\n4. NEGATIVE SCENARIOS (REJECTING INVALID OPERATIONS)");
        final Car car2 = service.registerCar("VIN002", "Dodge", "Charger", 1970, client);
        final RepairOrder order2 = service.createOrder(car2);
        final long order2Id = order2.getId();
        service.diagnoseOrder(order2Id);

        try {
            service.approveOrder(order2Id);
        } catch (final IllegalStateException e) {
            System.out.println("Expected error (empty order): " + e.getMessage());
        }

        final ServiceItem item3 = new ServiceItem("Filter change", new BigDecimal("20.00"), new BigDecimal("10.00"));
        service.addWorkItem(order2Id, item3);
        service.approveOrder(order2Id);

        try {
            service.startOrder(order2Id);
        } catch (final IllegalStateException e) {
            System.out.println("Expected error (no mechanic): " + e.getMessage());
        }

        try {
            service.completeOrder(order2Id);
        } catch (final IllegalStateException e) {
            System.out.println("Expected error (forbidden transition): " + e.getMessage());
        }

        try {
            service.getOrder(999L);
        } catch (final IllegalArgumentException e) {
            System.out.println("Expected error (missing order): " + e.getMessage());
        }

        System.out.println("Order #2 commercial price (with minimum price rule 250.00): "
                + service.calculateCommercialPrice(order2Id));
    }
}