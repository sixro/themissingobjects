package themissingobjects.finance;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Represents an <a href="https://en.wikipedia.org/wiki/Exchange_rate" >exchange rate</a>.
 *
 * @author <a href="mailto:me@sixro.net" >Sixro</a>
 * @since 1.0
 */
public class ExchangeRate implements Comparable<ExchangeRate>, Serializable {

    private static final long serialVersionUID = 1L;

    private final Quote quote;
    private final CurrencyPair currencyPair;
    private final ZonedDateTime timestamp;

    private ExchangeRate(Quote quote, CurrencyPair currencyPair, ZonedDateTime timestamp) {
        this.quote = quote;
        this.currencyPair = currencyPair;
        this.timestamp = timestamp;
    }

    /**
     * Returns an exchange rate with specified quote and pair assuming it is related to the current time.
     * @param quote a {@link Quote}
     * @param currencyPair a {@link CurrencyPair}
     * @return an exchange rate
     */
    public static ExchangeRate valueOf(Quote quote, CurrencyPair currencyPair) {
        return valueOf(quote, currencyPair, ZonedDateTime.now());
    }

    /**
     * Returns an exchange rate with specified quote, pair and timestamp
     * @param quote a {@link Quote}
     * @param currencyPair a {@link CurrencyPair}
     * @param timestamp a timestamp
     * @return an exchange rate
     */
    public static ExchangeRate valueOf(Quote quote, CurrencyPair currencyPair, ZonedDateTime timestamp) {
        return new ExchangeRate(quote, currencyPair, timestamp);
    }

    /**
     * Returns the quote.
     * @return a {@link Quote}
     */
    public Quote quote() {
        return quote;
    }

    /**
     * Returns the currency pair
     * @return a {@link CurrencyPair}
     */
    public CurrencyPair currencyPair() {
        return currencyPair;
    }

    /**
     * Returns the timestamp.
     * @return a timestamp
     */
    public ZonedDateTime timestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(ExchangeRate o) {
        int cpair = currencyPair.compareTo(o.currencyPair);
        if (cpair != 0)
            return cpair;
        return timestamp.compareTo(o.timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Objects.equals(quote, that.quote) &&
                Objects.equals(currencyPair, that.currencyPair) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quote, currencyPair, timestamp);
    }

    @Override
    public String toString() {
        return String.format("%s@%s (%s)", currencyPair, quote, timestamp);
    }

}
