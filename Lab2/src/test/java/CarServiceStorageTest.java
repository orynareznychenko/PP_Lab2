import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CarServiceStorageTest {

    @Test
    public void shouldStoreEntitiesCorrectly() {
        final CarServiceStorage storage = new CarServiceStorage();
        final long clientId = storage.generateClientId();
        final long mechanicId = storage.generateMechanicId();
        final Client client = new Client(clientId, "Jane", "+380");
        final Car car = new Car("VIN001", "ZAZ", "Lanos", 2008, client);
        final Mechanic mechanic = new Mechanic(mechanicId, "John", "Engine");

        storage.saveClient(client);
        storage.saveCar(car, car.getVin());
        storage.saveMechanic(mechanic);

        Assertions.assertEquals(1, storage.getClients().size());
        Assertions.assertEquals(1, storage.getCars().size());
        Assertions.assertEquals(1, storage.getRegisteredVins().size());
        Assertions.assertEquals(1, storage.getMechanics().size());
        Assertions.assertTrue(storage.containsVin("vin001"));
        Assertions.assertFalse(storage.containsVin("UNKNOWN"));
        Assertions.assertFalse(storage.containsVin(null));
    }

    @Test
    public void shouldRejectNullEntitiesInStorage() {
        final CarServiceStorage storage = new CarServiceStorage();

        Assertions.assertThrows(IllegalArgumentException.class, () -> storage.saveClient(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> storage.saveCar(null, "VIN"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> storage.saveMechanic(null));
    }
}