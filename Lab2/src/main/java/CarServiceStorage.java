import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarServiceStorage {
    private long nextClientId;
    private long nextMechanicId;
    private List<Client> clients;
    private List<Car> cars;
    private List<String> registeredVins;
    private List<Mechanic> mechanics;

    public CarServiceStorage() {
        nextClientId = 1L;
        nextMechanicId = 1L;
        clients = new ArrayList<>();
        cars = new ArrayList<>();
        registeredVins = new ArrayList<>();
        mechanics = new ArrayList<>();
    }

    public long generateClientId() {
        final long currentId = nextClientId;
        nextClientId++;
        return currentId;
    }

    public long generateMechanicId() {
        final long currentId = nextMechanicId;
        nextMechanicId++;
        return currentId;
    }

    public void saveClient(final Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        clients.add(client);
    }

    public void saveCar(final Car car, final String vin) {
        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }
        cars.add(car);
        registeredVins.add(vin);
    }

    public boolean containsVin(final String vinToFind) {
        if (vinToFind == null) {
            return false;
        }
        for (final String existingVin : registeredVins) {
            final boolean matches = vinToFind.equalsIgnoreCase(existingVin);
            if (matches) {
                return true;
            }
        }
        return false;
    }

    public void saveMechanic(final Mechanic mechanic) {
        if (mechanic == null) {
            throw new IllegalArgumentException("Mechanic cannot be null");
        }
        mechanics.add(mechanic);
    }

    public List<Client> getClients() {
        return Collections.unmodifiableList(clients);
    }

    public List<Car> getCars() {
        return Collections.unmodifiableList(cars);
    }

    public List<String> getRegisteredVins() {
        return Collections.unmodifiableList(registeredVins);
    }

    public List<Mechanic> getMechanics() {
        return Collections.unmodifiableList(mechanics);
    }
}