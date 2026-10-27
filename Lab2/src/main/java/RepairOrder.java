import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepairOrder {
    private long id;
    private Car car;
    private Mechanic mechanic;
    private OrderStatus status;
    private List<ServiceItem> items;

    public RepairOrder(final Car orderCar) {
        setCar(orderCar);
        id = 0L;
        status = OrderStatus.CREATED;
        items = new ArrayList<>();
    }

    public RepairOrder(final long orderId, final Car orderCar) {
        setId(orderId);
        setCar(orderCar);
        status = OrderStatus.CREATED;
        items = new ArrayList<>();
    }

    public void setId(final long orderId) {
        final boolean invalidId = orderId <= 0L;
        if (invalidId) {
            throw new IllegalArgumentException("Order ID must be positive");
        }
        id = orderId;
    }

    public void setCar(final Car orderCar) {
        if (orderCar == null) {
            throw new IllegalArgumentException("Order must belong to a valid car");
        }
        car = orderCar;
    }

    public void addWorkItem(final ServiceItem item) {
        ensureModifiable();
        if (item == null) {
            throw new IllegalArgumentException("Work item cannot be null");
        }
        ensureCanAddWorkItems();
        items.add(item);
    }

    private void ensureCanAddWorkItems() {
        final OrderStatus created = OrderStatus.CREATED;
        final OrderStatus diagnosed = OrderStatus.DIAGNOSED;
        final boolean isCreated = created.equals(status);
        final boolean isDiagnosed = diagnosed.equals(status);
        if (!isCreated && !isDiagnosed) {
            throw new IllegalStateException("Work items can only be added before approval");
        }
    }

    public void assignMechanic(final Mechanic newMechanic) {
        ensureModifiable();
        if (newMechanic == null) {
            throw new IllegalArgumentException("Mechanic cannot be null");
        }
        newMechanic.occupy();
        if (mechanic != null) {
            mechanic.release();
        }
        mechanic = newMechanic;
    }

    public void diagnose() {
        ensureModifiable();
        final OrderStatus next = OrderStatus.DIAGNOSED;
        transitionTo(next);
    }

    public void approve() {
        ensureModifiable();
        final boolean empty = items.isEmpty();
        if (empty) {
            throw new IllegalStateException("Cannot approve order without work items");
        }
        final OrderStatus next = OrderStatus.APPROVED;
        transitionTo(next);
    }

    public void startProgress() {
        ensureModifiable();
        final OrderStatus requiredStatus = OrderStatus.APPROVED;
        final boolean isApproved = requiredStatus.equals(status);
        if (!isApproved) {
            throw new IllegalStateException("Order must be APPROVED before starting");
        }
        if (mechanic == null) {
            throw new IllegalStateException("Cannot start order without assigned mechanic");
        }
        final OrderStatus next = OrderStatus.IN_PROGRESS;
        transitionTo(next);
    }

    public void completeWorkItem(final ServiceItem item) {
        ensureModifiable();
        if (item == null) {
            throw new IllegalArgumentException("Work item cannot be null");
        }
        final OrderStatus requiredStatus = OrderStatus.IN_PROGRESS;
        final boolean isInProgress = requiredStatus.equals(status);
        if (!isInProgress) {
            throw new IllegalStateException("Work can be completed only in IN_PROGRESS status");
        }
        markItemAsDone(item);
    }

    private void markItemAsDone(final ServiceItem item) {
        for (final ServiceItem currentItem : items) {
            final boolean matches = item.equals(currentItem);
            if (matches) {
                currentItem.markCompleted();
                return;
            }
        }
        throw new IllegalArgumentException("Work item does not belong to this order");
    }

    public void completeOrder() {
        ensureModifiable();
        final OrderStatus requiredStatus = OrderStatus.IN_PROGRESS;
        final boolean isInProgress = requiredStatus.equals(status);
        if (!isInProgress) {
            throw new IllegalStateException("Only IN_PROGRESS order can be completed");
        }
        final boolean allDone = areAllItemsCompleted();
        if (!allDone) {
            throw new IllegalStateException("Cannot complete order: not all items are completed");
        }
        final OrderStatus next = OrderStatus.COMPLETED;
        transitionTo(next);
        releaseMechanicIfPresent();
    }

    public void cancelOrder() {
        ensureModifiable();
        final OrderStatus next = OrderStatus.CANCELLED;
        transitionTo(next);
        releaseMechanicIfPresent();
    }

    private boolean areAllItemsCompleted() {
        for (final ServiceItem currentItem : items) {
            final boolean done = currentItem.isCompleted();
            if (!done) {
                return false;
            }
        }
        return true;
    }

    private void transitionTo(final OrderStatus nextStatus) {
        final boolean allowed = status.canTransitionTo(nextStatus);
        if (!allowed) {
            final String message = String.format("Invalid transition from %s to %s", status, nextStatus);
            throw new IllegalStateException(message);
        }
        status = nextStatus;
    }

    private void ensureModifiable() {
        final OrderStatus completed = OrderStatus.COMPLETED;
        final OrderStatus cancelled = OrderStatus.CANCELLED;
        final boolean isCompleted = completed.equals(status);
        final boolean isCancelled = cancelled.equals(status);
        if (isCompleted || isCancelled) {
            throw new IllegalStateException("Completed or cancelled order cannot be modified");
        }
    }

    private void releaseMechanicIfPresent() {
        if (mechanic != null) {
            mechanic.release();
        }
    }

    public BigDecimal calculateBasePrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (final ServiceItem item : items) {
            final BigDecimal itemCost = item.calculateTotalCost();
            total = total.add(itemCost);
        }
        return total;
    }

    public long getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<ServiceItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}