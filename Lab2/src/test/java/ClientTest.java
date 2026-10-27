import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClientTest {

    @Test
    public void shouldCreateClientSuccessfully() {
        final Client client = new Client(1L, "Jane Doe", "+380661234567");

        final String info = client.formatInfo();

        Assertions.assertEquals(1L, client.getId());
        Assertions.assertEquals("Jane Doe", client.getName());
        Assertions.assertEquals("+380661234567", client.getPhone());
        Assertions.assertEquals("Client #1: Jane Doe (+380661234567)", info);
    }

    @Test
    public void shouldRejectNonPositiveClientId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Client(0L, "Jane", "+380"));
    }

    @Test
    public void shouldRejectNullClientName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Client(1L, null, "+380"));
    }

    @Test
    public void shouldRejectBlankClientName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Client(1L, "   ", "+380"));
    }

    @Test
    public void shouldRejectNullClientPhone() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Client(1L, "Jane", null));
    }

    @Test
    public void shouldRejectBlankClientPhone() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Client(1L, "Jane", "   "));
    }
}