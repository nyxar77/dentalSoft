package ma.dentalsoft;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void printsGreeting() {
        PrintStream original = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream captured = new PrintStream(output)) {
            System.setOut(captured);
            App.main(new String[0]);
        } finally {
            System.setOut(original);
        }
        assertEquals("Hello World!", output.toString().trim());
    }
}
