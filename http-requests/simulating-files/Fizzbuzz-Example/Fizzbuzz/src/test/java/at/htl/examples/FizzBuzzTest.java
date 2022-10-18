package at.htl.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FizzBuzzTest {

    final String testString = "Hello, World!\n";

    @BeforeEach
    public void setupOutput() {

    }

    @AfterEach
    public void restoreSystemInputOutput() {

    }

    @Test
    public void test001() {
        assertThat(HelloWorld.FizzBuzz(3)).isEqualTo("Fizz");
    }

    @Test
    public void test002(){
        assertThat(HelloWorld.FizzBuzz(5)).isEqualTo("Buzz");
    }

    @Test
    public void test003(){
        assertThat(HelloWorld.FizzBuzz(15)).isEqualTo("FizzBuzz");
    }

    @Test
    public void test004(){
        assertThat(HelloWorld.FizzBuzz(11)).isEqualTo("11");
    }

}
