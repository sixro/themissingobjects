package themissingobjects.finance;

import org.junit.Assert;
import org.junit.Test;
import testing.BigDecimalAsserts;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuoteTest {

    @Test public void represents_algo_maximum_BTC() {
        BigDecimal value = new BigDecimal("20999999.9769");
        Quote quote = Quote.of(value);
        BigDecimalAsserts.assertBigDecimalEquals("algo_max", value, quote.toBigDecimal());
    }

    @Test public void represents_tam_bitcoin_BTC() {
        BigDecimal value = new BigDecimal("2814749.76710656");
        Quote quote = Quote.of(value);
        BigDecimalAsserts.assertBigDecimalEquals("tam-bitcoin", value, quote.toBigDecimal());
    }

    @Test public void represents_millisatoshi_BTC() {
        BigDecimal value = new BigDecimal("0.00000000001");
        Quote quote = Quote.of(value);
        BigDecimalAsserts.assertBigDecimalEquals("millisatoshi", value, quote.toBigDecimal());
    }

    @Test public void plus() {
        Assert.assertEquals(Quote.of(11), Quote.TEN.plus(Quote.ONE));
        Assert.assertEquals(Quote.of(new BigDecimal("12.3")), Quote.of(new BigDecimal("2.3")).plus(Quote.TEN));
        Assert.assertEquals(Quote.of(new BigDecimal("12.3")), Quote.TEN.plus(Quote.of(new BigDecimal("2.3"))));
    }

    @Test public void minus() {
        Assert.assertEquals(Quote.of(9), Quote.TEN.minus(Quote.ONE));
        Assert.assertEquals(Quote.of(new BigDecimal("-7.7")), Quote.of(new BigDecimal("2.3")).minus(Quote.TEN));
        Assert.assertEquals(Quote.of(new BigDecimal("7.7")), Quote.TEN.minus(Quote.of(new BigDecimal("2.3"))));
    }

    @Test public void times() {
        Assert.assertEquals(Quote.TEN, Quote.ONE.times(10));
        Assert.assertEquals(Quote.of(111), Quote.TEN.times(new BigDecimal("11.1")));
    }

    @Test public void equality() {
        Assert.assertEquals(Quote.of(new BigDecimal("7.7")), Quote.of(new BigDecimal("7.70000")));
    }

    @Test public void comparable() {
        List<Quote> l = Arrays.asList(Quote.TEN, Quote.ONE);
        Collections.sort(l);
        Assert.assertEquals(Quote.ONE, l.get(0));
    }

}
