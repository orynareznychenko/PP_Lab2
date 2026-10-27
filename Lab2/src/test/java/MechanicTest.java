import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MechanicTest {

    @Test
    public void shouldCreateMechanicSuccessfully() {
        final Mechanic mechanic = new Mechanic(1L, "John", "Engine");

        final String info = mechanic.formatInfo();

        Assertions.assertEquals(1L, mechanic.getId());
        Assertions.assertEquals("John", mechanic.getName());
        Assertions.assertEquals("Engine", mechanic.getSpecialization());
        Assertions.assertFalse(mechanic.isBusy());
        Assertions.assertEquals("Mechanic #1: John (Engine)", info);
    }

    @Test
    public void shouldChangeMechanicBusyState() {
        final Mechanic mechanic = new Mechanic(1L, "John", "Engine");

        mechanic.occupy();
        Assertions.assertTrue(mechanic.isBusy());

        Assertions.assertThrows(IllegalStateException.class, mechanic::occupy);

        mechanic.release();
        Assertions.assertFalse(mechanic.isBusy());
    }

    @Test
    public void shouldRejectInvalidMechanicParameters() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Mechanic(0L, "John", "Engine"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Mechanic(1L, null, "Engine"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Mechanic(1L, "  ", "Engine"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Mechanic(1L, "John", null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Mechanic(1L, "John", "  "));
    }
}