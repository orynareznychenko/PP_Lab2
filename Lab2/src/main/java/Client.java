public class Client {
    private long id;
    private String name;
    private String phone;

    public Client(final long clientId, final String clientName, final String clientPhone) {
        setId(clientId);
        setName(clientName);
        setPhone(clientPhone);
    }

    public void setId(final long clientId) {
        final boolean invalidId = clientId <= 0L;
        if (invalidId) {
            throw new IllegalArgumentException("Client ID must be positive");
        }
        id = clientId;
    }

    public void setName(final String clientName) {
        validateName(clientName);
        name = clientName.strip();
    }

    public void setPhone(final String clientPhone) {
        validatePhone(clientPhone);
        phone = clientPhone.strip();
    }

    private void validateName(final String clientName) {
        if (clientName == null) {
            throw new IllegalArgumentException("Client name cannot be empty");
        }
        final boolean blank = clientName.isBlank();
        if (blank) {
            throw new IllegalArgumentException("Client name cannot be empty");
        }
    }

    private void validatePhone(final String clientPhone) {
        if (clientPhone == null) {
            throw new IllegalArgumentException("Client phone cannot be empty");
        }
        final boolean blank = clientPhone.isBlank();
        if (blank) {
            throw new IllegalArgumentException("Client phone cannot be empty");
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String formatInfo() {
        return String.format("Client #%d: %s (%s)", id, name, phone);
    }
}