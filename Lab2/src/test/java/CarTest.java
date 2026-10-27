import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CarTest {

    @Test
    public void shouldCreateCarSuccessfully() {
        final Client client = new Client(1L, "Jane", "+380");

        final Car car = new Car("VIN001", "ZAZ", "Lanos", 2008, client);
        final String info = car.formatInfo();

        Assertions.assertEquals("VIN001", car.getVin());
        Assertions.assertEquals("ZAZ", car.getBrand());
        Assertions.assertEquals("Lanos", car.getModel());
        Assertions.assertEquals(2008, car.getYear());
        Assertions.assertEquals(client, car.getOwner());
        Assertions.assertEquals("ZAZ Lanos (VIN001), year: 2008", info);
    }

    @Test
    public void shouldRejectInvalidVinValues() {
        final Client client = new Client(1L, "Jane", "+380");

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Car(null, "ZAZ", "Lanos", 2008, client));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Car("  ", "ZAZ", "Lanos", 2008, client));
    }

    @Test
    public void shouldRejectInvalidBrandValues() {
        final Client client = new Client(1L, "Jane", "+380");

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Car("VIN1", null, "Lanos", 2008, client));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Car("VIN1", "  ", "Lanos", 2008, client));
    }

    @Test
    public void shouldRejectInvalidModelValues() {
        final Client client = new Client(1L, "Jane", "+380");

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Car("VIN1", "ZAZ", null, 2008, client));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Car("VIN1", "ZAZ", "  ", 2008, client));
    }

    @Test
    public void shouldRejectInvalidManufactureYear() {
        final Client client = new Client(1L, "Jane", "+380");

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Car("VIN1", "ZAZ", "Lanos", 1800, client));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Car("VIN1", "ZAZ", "Lanos", 2050, client));
    }

    @Test
    public void shouldRejectNullOwner() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Car("VIN1", "ZAZ", "Lanos", 2008, null));
    }
}