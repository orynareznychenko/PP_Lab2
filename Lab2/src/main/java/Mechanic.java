public class Mechanic {
    private long id;
    private String name;
    private String specialization;
    private boolean busy;

    public Mechanic(
            final long mechanicId,
            final String mechanicName,
            final String mechanicSpecialization
    ) {
        setId(mechanicId);
        setName(mechanicName);
        setSpecialization(mechanicSpecialization);
        busy = false;
    }

    public void setId(final long mechanicId) {
        final boolean invalidId = mechanicId <= 0L;
        if (invalidId) {
            throw new IllegalArgumentException("Mechanic ID must be positive");
        }
        id = mechanicId;
    }

    public void setName(final String mechanicName) {
        validateName(mechanicName);
        name = mechanicName.strip();
    }

    public void setSpecialization(final String mechanicSpecialization) {
        validateSpecialization(mechanicSpecialization);
        specialization = mechanicSpecialization.strip();
    }

    private void validateName(final String mechanicName) {
        if (mechanicName == null) {
            throw new IllegalArgumentException("Mechanic name cannot be empty");
        }
        final boolean blank = mechanicName.isBlank();
        if (blank) {
            throw new IllegalArgumentException("Mechanic name cannot be empty");
        }
    }

    private void validateSpecialization(final String mechanicSpecialization) {
        if (mechanicSpecialization == null) {
            throw new IllegalArgumentException("Specialization cannot be empty");
        }
        final boolean blank = mechanicSpecialization.isBlank();
        if (blank) {
            throw new IllegalArgumentException("Specialization cannot be empty");
        }
    }

    public void occupy() {
        if (busy) {
            throw new IllegalStateException("Mechanic is already busy with another active order");
        }
        busy = true;
    }

    public void release() {
        busy = false;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public boolean isBusy() {
        return busy;
    }

    public String formatInfo() {
        return String.format("Mechanic #%d: %s (%s)", id, name, specialization);
    }
}