package themissingobjects.math;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.*;

public class PercentageTest {

    private static final int GREATER_THAN = 1;
    private static final int LESS_THAN = -1;
    private static final int EQUALS = 0;

    @Test public void _100() {
        BigDecimalAsserts.assertBigDecimalEquals("100%", BigDecimal.TEN, Percentage.of(100).calculateOn(BigDecimal.TEN));
        BigDecimalAsserts.assertBigDecimalEquals("100%", BigDecimal.TEN, Percentage.of(1d).calculateOn(BigDecimal.TEN));
        BigDecimalAsserts.assertBigDecimalEquals("100%", BigDecimal.TEN, Percentage.of("100%").calculateOn(BigDecimal.TEN));
    }

    @Test public void _0() {
        BigDecimalAsserts.assertBigDecimalEquals("0%", BigDecimal.ZERO, Percentage.of(0).calculateOn(BigDecimal.TEN));
    }

    @Test public void a_tenth() {
        BigDecimalAsserts.assertBigDecimalEquals("10%", BigDecimal.ONE, Percentage.of(10).calculateOn(BigDecimal.TEN));
        BigDecimalAsserts.assertBigDecimalEquals("10%", BigDecimal.ONE, Percentage.of("10%").calculateOn(BigDecimal.TEN));
    }

    @Test public void _200() {
        BigDecimalAsserts.assertBigDecimalEquals("200%", BigDecimal.valueOf(20), Percentage.of(200).calculateOn(BigDecimal.TEN));
    }

    @Test public void one_third() {
        Percentage oneThird = Percentage.of("33.33%");
        BigDecimalAsserts.assertBigDecimalEquals("33.33%", BigDecimal.valueOf(20), oneThird.calculateOn(BigDecimal.valueOf(60), 0, RoundingMode.HALF_UP));
    }

    @Test public void textual_representation() {
        assertEquals("100%", Percentage.of(100).toString());
        assertEquals("-100%", Percentage.of(-100).toString());
        assertEquals("33.33%", Percentage.of(1 / 3d).toString());
    }

    @Test public void comparable() {
        assertTrue(Percentage.of(100).compareTo(Percentage.of(99)) == GREATER_THAN);
        assertTrue(Percentage.of("10%").compareTo(Percentage.of("20%")) == LESS_THAN);
        assertTrue(Percentage.of("10%").compareTo(Percentage.of("10%")) == EQUALS);
    }

    @Test public void equals_by_sign_in_some_case() {
        assertSame(Percentage.of(100), Percentage.of("100%"));
        assertSame(Percentage.of(50), Percentage.of("50%"));
        assertSame(Percentage.of(25), Percentage.of("25%"));

        assertNotSame(Percentage.of(12), Percentage.of("12%"));
    }

}
