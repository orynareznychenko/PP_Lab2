public enum OrderStatus {
    CREATED,
    DIAGNOSED,
    APPROVED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    public boolean canTransitionTo(final OrderStatus nextStatus) {
        if (nextStatus == null) {
            return false;
        }
        if (CANCELLED.equals(nextStatus)) {
            return canBeCancelled();
        }
        if (CREATED.equals(this)) {
            return DIAGNOSED.equals(nextStatus);
        }
        if (DIAGNOSED.equals(this)) {
            return APPROVED.equals(nextStatus);
        }
        if (APPROVED.equals(this)) {
            return IN_PROGRESS.equals(nextStatus);
        }
        if (IN_PROGRESS.equals(this)) {
            return COMPLETED.equals(nextStatus);
        }
        return false;
    }

    private boolean canBeCancelled() {
        final boolean isCreated = CREATED.equals(this);
        final boolean isDiagnosed = DIAGNOSED.equals(this);
        final boolean isApproved = APPROVED.equals(this);
        if (isCreated || isDiagnosed || isApproved) {
            return true;
        }
        return false;
    }
}