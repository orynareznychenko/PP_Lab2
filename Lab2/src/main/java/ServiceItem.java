import java.math.BigDecimal;

public class ServiceItem {
    private String description;
    private BigDecimal partsCost;
    private BigDecimal laborCost;
    private boolean completed;

    public ServiceItem(
            final String workDescription,
            final BigDecimal itemPartsCost,
            final BigDecimal itemLaborCost
    ) {
        setDescription(workDescription);
        setPartsCost(itemPartsCost);
        setLaborCost(itemLaborCost);
        completed = false;
    }

    public void setDescription(final String workDescription) {
        validateDescription(workDescription);
        description = workDescription.strip();
    }

    public void setPartsCost(final BigDecimal itemPartsCost) {
        validatePartsCost(itemPartsCost);
        partsCost = itemPartsCost;
    }

    public void setLaborCost(final BigDecimal itemLaborCost) {
        validateLaborCost(itemLaborCost);
        laborCost = itemLaborCost;
    }

    private void validateDescription(final String text) {
        if (text == null) {
            throw new IllegalArgumentException("Work description cannot be null");
        }
        final boolean blank = text.isBlank();
        if (blank) {
            throw new IllegalArgumentException("Work description cannot be blank");
        }
    }

    private void validatePartsCost(final BigDecimal cost) {
        if (cost == null) {
            throw new IllegalArgumentException("Parts cost cannot be negative or null");
        }
        final BigDecimal zero = BigDecimal.ZERO;
        final int comparison = cost.compareTo(zero);
        if (comparison < 0) {
            throw new IllegalArgumentException("Parts cost cannot be negative or null");
        }
    }

    private void validateLaborCost(final BigDecimal cost) {
        if (cost == null) {
            throw new IllegalArgumentException("Labor cost cannot be negative or null");
        }
        final BigDecimal zero = BigDecimal.ZERO;
        final int comparison = cost.compareTo(zero);
        if (comparison < 0) {
            throw new IllegalArgumentException("Labor cost cannot be negative or null");
        }
    }

    public void markCompleted() {
        completed = true;
    }

    public BigDecimal calculateTotalCost() {
        return partsCost.add(laborCost);
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPartsCost() {
        return partsCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public boolean isCompleted() {
        return completed;
    }
}