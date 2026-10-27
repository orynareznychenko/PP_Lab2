import java.math.BigDecimal;
import java.util.List;

public class CarService {
    private CarServiceStorage storage;
    private RepairOrderRepository orderRepository;
    private PricingPolicy pricingPolicy;

    public CarService(
            final CarServiceStorage serviceStorage,
            final RepairOrderRepository repository,
            final PricingPolicy policy
    ) {
        setStorage(serviceStorage);
        setOrderRepository(repository);
        setPricingPolicy(policy);
    }

    public void setStorage(final CarServiceStorage serviceStorage) {
        if (serviceStorage == null) {
            throw new IllegalArgumentException("Storage cannot be null");
        }
        storage = serviceStorage;
    }

    public void setOrderRepository(final RepairOrderRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Order repository cannot be null");
        }
        orderRepository = repository;
    }

    public void setPricingPolicy(final PricingPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Pricing policy cannot be null");
        }
        pricingPolicy = policy;
    }

    public Client registerClient(final String name, final String phone) {
        final long clientId = storage.generateClientId();
        final Client client = new Client(clientId, name, phone);
        storage.saveClient(client);
        return client;
    }

    public Car registerCar(
            final String vin,
            final String brand,
            final String model,
            final int year,
            final Client owner
    ) {
        if (vin == null) {
            throw new IllegalArgumentException("VIN cannot be null");
        }
        final String cleanVin = vin.strip();
        final boolean exists = storage.containsVin(cleanVin);
        if (exists) {
            final String error = String.format("Car with VIN (%s) is already registered", cleanVin);
            throw new IllegalArgumentException(error);
        }
        final Car car = new Car(cleanVin, brand, model, year, owner);
        storage.saveCar(car, cleanVin);
        return car;
    }

    public Mechanic registerMechanic(final String name, final String specialization) {
        final long mechanicId = storage.generateMechanicId();
        final Mechanic mechanic = new Mechanic(mechanicId, name, specialization);
        storage.saveMechanic(mechanic);
        return mechanic;
    }

    public RepairOrder createOrder(final Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }
        final String carVin = car.getVin();
        final boolean isRegistered = storage.containsVin(carVin);
        if (!isRegistered) {
            throw new IllegalStateException("Cannot create order for unregistered car");
        }
        final RepairOrder order = new RepairOrder(car);
        return orderRepository.save(order);
    }

    public RepairOrder getOrder(final long orderId) {
        return orderRepository.findById(orderId);
    }

    public void addWorkItem(final long orderId, final ServiceItem item) {
        final RepairOrder order = orderRepository.findById(orderId);
        order.addWorkItem(item);
        orderRepository.save(order);
    }

    public void diagnoseOrder(final long orderId) {
        final RepairOrder order = orderRepository.findById(orderId);
        order.diagnose();
        orderRepository.save(order);
    }

    public void approveOrder(final long orderId) {
        final RepairOrder order = orderRepository.findById(orderId);
        order.approve();
        orderRepository.save(order);
    }

    public void assignMechanic(final long orderId, final Mechanic mechanic) {
        final RepairOrder order = orderRepository.findById(orderId);
        order.assignMechanic(mechanic);
        orderRepository.save(order);
    }

    public void startOrder(final long orderId) {
        final RepairOrder order = orderRepository.findById(orderId);
        order.startProgress();
        orderRepository.save(order);
    }

    public void completeWorkItem(final long orderId, final ServiceItem item) {
        final RepairOrder order = orderRepository.findById(orderId);
        order.completeWorkItem(item);
        orderRepository.save(order);
    }

    public void completeOrder(final long orderId) {
        final RepairOrder order = orderRepository.findById(orderId);
        order.completeOrder();
        orderRepository.save(order);
    }

    public void cancelOrder(final long orderId) {
        final RepairOrder order = orderRepository.findById(orderId);
        order.cancelOrder();
        orderRepository.save(order);
    }

    public BigDecimal calculateBasePrice(final long orderId) {
        final RepairOrder order = orderRepository.findById(orderId);
        return order.calculateBasePrice();
    }

    public BigDecimal calculateCommercialPrice(final long orderId) {
        final RepairOrder order = orderRepository.findById(orderId);
        final BigDecimal basePrice = order.calculateBasePrice();
        return pricingPolicy.calculatePrice(basePrice);
    }

    public List<RepairOrder> findOrdersByStatus(final OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public CarServiceStorage getStorage() {
        return storage;
    }

    public RepairOrderRepository getOrderRepository() {
        return orderRepository;
    }

    public PricingPolicy getPricingPolicy() {
        return pricingPolicy;
    }
}