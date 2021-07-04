package testing;

import org.junit.Assert;

import java.math.BigDecimal;

public class BigDecimalAsserts {

    private BigDecimalAsserts() { }

    public static void assertBigDecimalEquals(String message, BigDecimal expected, BigDecimal actual) {
        final int EQUALS = 0;
        Assert.assertTrue(message + " (expected " + expected + ", got " + actual + ")", expected.compareTo(actual) == EQUALS);
    }
}
