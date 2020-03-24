package themissingobjects.finance;

import java.time.ZonedDateTime;

/**
 * Represents an <a href="https://en.wikipedia.org/wiki/Exchange_rate" >exchange rate</a>.
 */
public class ExchangeRate {

    private final Quote quote;
    private final CurrencyPair currencyPair;
    private final ZonedDateTime timestamp;

    public ExchangeRate(Quote quote, CurrencyPair currencyPair, ZonedDateTime timestamp) {
        this.quote = quote;
        this.currencyPair = currencyPair;
        this.timestamp = timestamp;
    }

    public Quote quote() {
        return quote;
    }

    public CurrencyPair currencyPair() {
        return currencyPair;
    }

    public ZonedDateTime timestamp() {
        return timestamp;
    }

    // TODO impl
}
