import java.util.List;

public interface RepairOrderRepository {
    RepairOrder save(final RepairOrder order);

    RepairOrder findById(final long id);

    List<RepairOrder> findByStatus(final OrderStatus status);

    List<RepairOrder> findAll();
}