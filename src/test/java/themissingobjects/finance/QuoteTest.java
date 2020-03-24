package themissingobjects.finance;

import org.junit.Assert;
import org.junit.Test;
import themissingobjects.math.BigDecimalAsserts;

import java.math.BigDecimal;

public class QuoteTest {

    public static final Quote _10 = Quote.valueOf(10);
    public static final Quote _1 = Quote.valueOf(1);

    @Test public void represents_algo_maximum_BTC() {
        BigDecimal value = new BigDecimal("20999999.9769");
        Quote quote = Quote.valueOf(value);
        BigDecimalAsserts.assertBigDecimalEquals("algo_max", value, quote.toBigDecimal());
    }

    @Test public void represents_tam_bitcoin_BTC() {
        BigDecimal value = new BigDecimal("2814749.76710656");
        Quote quote = Quote.valueOf(value);
        BigDecimalAsserts.assertBigDecimalEquals("tam-bitcoin", value, quote.toBigDecimal());
    }

    @Test public void represents_millisatoshi_BTC() {
        BigDecimal value = new BigDecimal("0.00000000001");
        Quote quote = Quote.valueOf(value);
        BigDecimalAsserts.assertBigDecimalEquals("millisatoshi", value, quote.toBigDecimal());
    }

    @Test public void plus() {
        Assert.assertEquals(Quote.valueOf(11), _10.plus(_1));
        Assert.assertEquals(Quote.valueOf(new BigDecimal("12.3")), Quote.valueOf(new BigDecimal("2.3")).plus(_10));
        Assert.assertEquals(Quote.valueOf(new BigDecimal("12.3")), _10.plus(Quote.valueOf(new BigDecimal("2.3"))));
    }

    @Test public void minus() {
        Assert.assertEquals(Quote.valueOf(9), _10.minus(_1));
        Assert.assertEquals(Quote.valueOf(new BigDecimal("-7.7")), Quote.valueOf(new BigDecimal("2.3")).minus(_10));
        Assert.assertEquals(Quote.valueOf(new BigDecimal("7.7")), _10.minus(Quote.valueOf(new BigDecimal("2.3"))));
    }

    @Test public void times() {
        Assert.assertEquals(_10, _1.times(10));
        Assert.assertEquals(Quote.valueOf(111), _10.times(new BigDecimal("11.1")));
    }

    @Test public void equality() {
        Assert.assertEquals(Quote.valueOf(new BigDecimal("7.7")), Quote.valueOf(new BigDecimal("7.70000")));
    }

}
