package springbook.learningtest.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class CalcSumTest {

    Calculator calculator;
    String numFilepath;

    @BeforeEach
    public void setUp() {
        this.calculator = new Calculator();
//        this.numFilepath = getClass().getResource("numbers.txt").getPath();
        this.numFilepath = "C:\\Users\\ara\\Desktop\\ara\\spring\\numbers.txt";
    }

    @Test
    public void sumOfNumbers() throws IOException {
//        Calculator calculator = new Calculator();
////        int sum = calculator.calcSum(getClass().getResource("numbers.txt").getPath());
//        int sum = calculator.calcSum("C:\\Users\\ara\\Desktop\\ara\\spring\\numbers.txt");
//        assertThat(sum).isEqualTo(10);
        assertThat(calculator.calcSum(this.numFilepath)).isEqualTo(10);
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        assertThat(calculator.calcMultiply(this.numFilepath)).isEqualTo(24);
    }

    @Test
    public void concatenateStrings() throws IOException {
        assertThat(calculator.concatenate(this.numFilepath)).isEqualTo("1234");
    }

}
