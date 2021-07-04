package themissingobjects.finance;

import java.time.ZonedDateTime;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import testing.BigDecimalAsserts;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class MoneyTest {

    public static final Currency EUR = Currency.getInstance("EUR");
    public static final Currency GBP = Currency.getInstance("GBP");
    public static final Currency USD = Currency.getInstance("USD");

    public static final Money _1EUR = new Money(100, EUR);
    public static final Money _2EUR = new Money(200, EUR);
    public static final Money _3EUR = new Money(300, EUR);

    public static final Money _1GBP = new Money(100, GBP);

    public static final ZonedDateTime IGNORE_TIMESTAMP = ZonedDateTime.now();
    public static final Currency BHD = Currency.getInstance("BHD");

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
        // NOTE: unfortunately Java does not guarantee the same behaviour even if you specify the Locale.
        assertCurrency(new Money(123456, EUR).toString(Locale.US), "1,234.56", "€", "EUR");
        assertCurrency(new Money(123456, GBP).toString(Locale.US), "1,234.56", "£", "GBP");
        assertCurrency(new Money(123456, USD).toString(Locale.US), "1,234.56", "$", "USD");
        assertEquals("USD1.234,56", new Money(123456, USD).toString(Locale.ITALY));
        assertEquals("BHD123.456", new Money(123456, BHD).toString(Locale.US));
    }

    private void assertCurrency(String text, String numberAsText, String symbol, String iso) {
        assertThat(text, allOf(containsString(numberAsText), CoreMatchers.anyOf(containsString(symbol), containsString(iso))));
    }

    @Test public void created_by_bigdecimal() {
        assertEquals(new Money(1000, EUR), Money.of(BigDecimal.TEN, EUR));
    }

    @Test public void created_by_int() {
        assertEquals(new Money(100, EUR), Money.of(1, EUR));
    }

    @Test public void parse() throws ParseException {
        assertEquals(new Money(123, EUR), Money.parse("€1.23", Locale.US));
        assertEquals(new Money(123, EUR), Money.parse("1.23€", Locale.US));
        assertEquals(new Money(123, EUR), Money.parse("1.23EUR", Locale.US));
        assertEquals(new Money(123, EUR), Money.parse("1,23EUR", Locale.ITALY));
        assertEquals(new Money(123, EUR), Money.parse("EUR1.23", Locale.US));
    }

    @Test public void toBigDecimal() throws ParseException {
        BigDecimalAsserts.assertBigDecimalEquals("toBigDecimal", new BigDecimal("1.230"), Money.parse("€1.23", Locale.US).toBigDecimal());
    }

    @Test public void convert_from_base_to_quote() {
        Money _10eur = Money.of(10, EUR);
        ExchangeRate rate = ExchangeRate.of(Quote.of(new BigDecimal("10.1234")), CurrencyPair.parse("EUR/BHD"), IGNORE_TIMESTAMP);
        assertEquals(Money.of(new BigDecimal("101.234"), BHD), _10eur.convert(rate));
    }

    @Test public void convert_from_quote_to_base() {
        Money _xBHD = Money.of(new BigDecimal("10.189"), BHD);
        ExchangeRate rate = ExchangeRate.of(Quote.TEN, CurrencyPair.parse("EUR/BHD"), IGNORE_TIMESTAMP);
        assertEquals(Money.of(new BigDecimal("1.02"), EUR), _xBHD.convert(rate));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrong_exchange_rate() {
        Money _10usd = Money.of(10, USD);
        ExchangeRate rate = ExchangeRate.of(Quote.TEN, CurrencyPair.parse("EUR/GBP"), IGNORE_TIMESTAMP);
        _10usd.convert(rate);
    }

    @Test public void comparable() {
        Money _1 = Money.of(1, EUR);
        Money _10 = Money.of(10, EUR);
        List<Money> l = Arrays.asList(_10, _1);
        Collections.sort(l);
        assertEquals(_1, l.get(0));
    }

    @Test public void comparable_on_different_currencies() {
        Money _10eur = Money.of(10, EUR);
        Money _1usd = Money.of(1, USD);
        List<Money> l = Arrays.asList(_10eur, _1usd);
        Collections.sort(l);
        assertEquals(_10eur, l.get(0));
    }

}
