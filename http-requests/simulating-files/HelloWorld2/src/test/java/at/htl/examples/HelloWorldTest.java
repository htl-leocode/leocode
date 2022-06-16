package at.htl.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class HelloWorldTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    final String testString = "Hello, World!\n";

    @BeforeEach
    public void setupOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        return testOut.toString();
    }

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    private String testHelper(){
        provideInput(testString);
        HelloWorld.main(null);
        return getOutput();
    }

    @Test
    public void test001() {
        assertThat(testString).isEqualTo(testHelper());
    }

    @Test
    public void test002(){
        assertThat(false).isEqualTo(true);
        assertThat("false").isEqualTo(true);
    }

}
