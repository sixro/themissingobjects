package themissingobjects.finance;

import java.util.Currency;
import java.util.Objects;

/**
 * Represents a <a href="https://en.wikipedia.org/wiki/Currency_pair" >currency pair</a> such as {@code EUR/USD}.
 */
public class CurrencyPair {

    private final Currency base;
    private final Currency quote;

    private CurrencyPair(Currency base, Currency quote) {
        Objects.requireNonNull(base);
        Objects.requireNonNull(quote);
        this.base = base;
        this.quote = quote;
    }

    public static CurrencyPair valueOf(Currency base, Currency quote) {
        return new CurrencyPair(base, quote);
    }

    public static CurrencyPair valueOf(String textual) {
        if (! textual.contains("/"))
            throw new IllegalArgumentException("'textual' does not represent a currency pair (got '" + textual + "', expected '<base>/<quote>')");
        String[] parts = textual.split("/");
        return valueOf(Currency.getInstance(parts[0]), Currency.getInstance(parts[1]));
    }

    public Currency base() {
        return base;
    }

    public Currency quote() {
        return quote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyPair that = (CurrencyPair) o;
        return Objects.equals(base, that.base) &&
                Objects.equals(quote, that.quote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, quote);
    }

    @Override
    public String toString() {
        return base + "/" + quote;
    }

}
