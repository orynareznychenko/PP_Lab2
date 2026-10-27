import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryRepairOrderRepository implements RepairOrderRepository {
    private long nextOrderId;
    private List<RepairOrder> orders;

    public InMemoryRepairOrderRepository() {
        nextOrderId = 1L;
        orders = new ArrayList<>();
    }

    private long generateOrderId() {
        final long currentId = nextOrderId;
        nextOrderId++;
        return currentId;
    }

    @Override
    public RepairOrder save(final RepairOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        final long currentId = order.getId();
        if (currentId <= 0L) {
            final long newId = generateOrderId();
            order.setId(newId);
            orders.add(order);
            return order;
        }
        updateExistingOrder(order);
        return order;
    }

    private void updateExistingOrder(final RepairOrder updatedOrder) {
        final long targetId = updatedOrder.getId();
        for (final RepairOrder existingOrder : orders) {
            final long existingId = existingOrder.getId();
            if (existingId == targetId) {
                return;
            }
        }
        orders.add(updatedOrder);
    }

    @Override
    public RepairOrder findById(final long id) {
        for (final RepairOrder order : orders) {
            final long currentId = order.getId();
            if (currentId == id) {
                return order;
            }
        }
        final String message = String.format("Order with ID (%d) was not found", id);
        throw new IllegalArgumentException(message);
    }

    @Override
    public List<RepairOrder> findByStatus(final OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        final List<RepairOrder> result = new ArrayList<>();
        for (final RepairOrder order : orders) {
            final OrderStatus orderStatus = order.getStatus();
            final boolean matches = status.equals(orderStatus);
            if (matches) {
                result.add(order);
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public List<RepairOrder> findAll() {
        return Collections.unmodifiableList(orders);
    }
}