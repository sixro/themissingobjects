package themissingobjects.finance;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class ExchangeRateTest {

    @Test public void equality() {
        ZonedDateTime now = ZonedDateTime.now();
        assertEquals(ExchangeRate.of(Quote.ONE, CurrencyPair.parse("EUR/EUR"), now), ExchangeRate.of(Quote.of(new BigDecimal("1.0")), CurrencyPair.parse("EUR/EUR"), now));
    }

    @Test public void textual_representation() {
        ZonedDateTime now = ZonedDateTime.now();
        ExchangeRate rate = ExchangeRate.of(Quote.ONE, CurrencyPair.parse("EUR/EUR"), now);
        assertThat(rate.toString(), containsString("EUR/EUR@1"));
    }

    @Test public void comparable_on_timestamp() {
        ExchangeRate rate1 = ExchangeRate.of(Quote.ONE, CurrencyPair.parse("EUR/EUR"), ZonedDateTime.parse("2019-01-01T00:00:00.000Z"));
        ExchangeRate rate2 = ExchangeRate.of(Quote.TEN, CurrencyPair.parse("EUR/EUR"), ZonedDateTime.parse("2018-01-01T00:00:00.000Z"));
        List<ExchangeRate> rates = Arrays.asList(rate1, rate2);
        Collections.sort(rates);
        assertEquals(rate2, rates.get(0));
    }

    @Test public void comparable_on_pair() {
        ExchangeRate rate1 = ExchangeRate.of(Quote.ONE, CurrencyPair.parse("EUR/EUR"), ZonedDateTime.parse("2019-01-01T00:00:00.000Z"));
        ExchangeRate rate2 = ExchangeRate.of(Quote.TEN, CurrencyPair.parse("EUR/USD"), ZonedDateTime.parse("2018-01-01T00:00:00.000Z"));
        List<ExchangeRate> rates = Arrays.asList(rate2, rate1);
        Collections.sort(rates);
        assertEquals(rate1, rates.get(0));
    }

}