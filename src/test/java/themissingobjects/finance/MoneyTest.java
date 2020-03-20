package themissingobjects.finance;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.*;

public class MoneyTest {

    public static final Currency EUR = Currency.getInstance("EUR");
    public static final Currency GBP = Currency.getInstance("GBP");
    public static final Currency USD = Currency.getInstance("USD");

    public static final Money _1EUR = new Money(100, EUR);
    public static final Money _2EUR = new Money(200, EUR);
    public static final Money _3EUR = new Money(300, EUR);

    public static final Money _1GBP = new Money(100, GBP);
    public static final Money _2GBP = new Money(200, GBP);

    @Test public void plus() {
        assertEquals(_3EUR, _1EUR.plus(_2EUR));
    }

    @Test public void minus() {
        assertEquals(_1EUR, _2EUR.minus(_1EUR));
    }

    @Test public void times() {
        assertEquals(new Money(1000, EUR), _2EUR.times(5));
        assertEquals(new Money(1000, EUR), _2EUR.times(BigDecimal.valueOf(5)));
        assertEquals(new Money(222, EUR), _2EUR.times(new BigDecimal("1.111")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void plus_fails_on_different_currencies() {
        _1EUR.plus(_1GBP);
    }

    @Test public void textual_representation() {
        assertEquals("€1,234.56", new Money(123456, EUR).toString());
        assertEquals("£1,234.56", new Money(123456, GBP).toString());
        assertEquals("$1,234.56", new Money(123456, USD).toString());
        assertEquals("BHD123.456", new Money(123456, Currency.getInstance("BHD")).toString());
    }

}
