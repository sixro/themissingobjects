package themissedobjects.math;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.*;

public class PercentageTest {

    private static final int GREATER_THAN = 1;
    private static final int LESS_THAN = -1;
    private static final int EQUALS = 0;

    @Test public void _100() {
        BigDecimalAsserts.assertBigDecimalEquals("100%", BigDecimal.TEN, Percentage.valueOf(100).of(BigDecimal.TEN));
        BigDecimalAsserts.assertBigDecimalEquals("100%", BigDecimal.TEN, Percentage.valueOf(1d).of(BigDecimal.TEN));
        BigDecimalAsserts.assertBigDecimalEquals("100%", BigDecimal.TEN, Percentage.valueOf("100%").of(BigDecimal.TEN));
    }

    @Test public void _0() {
        BigDecimalAsserts.assertBigDecimalEquals("0%", BigDecimal.ZERO, Percentage.valueOf(0).of(BigDecimal.TEN));
    }

    @Test public void a_tenth() {
        BigDecimalAsserts.assertBigDecimalEquals("10%", BigDecimal.ONE, Percentage.valueOf(10).of(BigDecimal.TEN));
        BigDecimalAsserts.assertBigDecimalEquals("10%", BigDecimal.ONE, Percentage.valueOf("10%").of(BigDecimal.TEN));
    }

    @Test public void _200() {
        BigDecimalAsserts.assertBigDecimalEquals("200%", BigDecimal.valueOf(20), Percentage.valueOf(200).of(BigDecimal.TEN));
    }

    @Test public void one_third() {
        Percentage oneThird = Percentage.valueOf("33.33%");
        BigDecimalAsserts.assertBigDecimalEquals("33.33%", BigDecimal.valueOf(20), oneThird.of(BigDecimal.valueOf(60), 0, RoundingMode.HALF_UP));
    }

    @Test public void textual_representation() {
        assertEquals("100%", Percentage.valueOf(100).toString());
        assertEquals("-100%", Percentage.valueOf(-100).toString());
        assertEquals("33.33%", Percentage.valueOf(1 / 3d).toString());
    }

    @Test public void comparable() {
        assertTrue(Percentage.valueOf(100).compareTo(Percentage.valueOf(99)) == GREATER_THAN);
        assertTrue(Percentage.valueOf("10%").compareTo(Percentage.valueOf("20%")) == LESS_THAN);
        assertTrue(Percentage.valueOf("10%").compareTo(Percentage.valueOf("10%")) == EQUALS);
    }

    @Test public void equals_by_sign_in_some_case() {
        assertSame(Percentage.valueOf(100), Percentage.valueOf("100%"));
        assertSame(Percentage.valueOf(50), Percentage.valueOf("50%"));
        assertSame(Percentage.valueOf(25), Percentage.valueOf("25%"));

        assertNotSame(Percentage.valueOf(12), Percentage.valueOf("12%"));
    }

}
