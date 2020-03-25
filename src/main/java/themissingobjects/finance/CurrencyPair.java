package themissingobjects.finance;

import java.io.Serializable;
import java.util.Currency;
import java.util.Objects;

/**
 * Represents a <a href="https://en.wikipedia.org/wiki/Currency_pair" >currency pair</a> such as {@code EUR/USD}.
 *
 * @author <a href="mailto:me@sixro.net" >Sixro</a>
 * @since 1.0
 ``*/
public class CurrencyPair implements Comparable<CurrencyPair>, Serializable {

    private static final long serialVersionUID = 1L;

    private final Currency base;
    private final Currency quote;

    private CurrencyPair(Currency base, Currency quote) {
        Objects.requireNonNull(base);
        Objects.requireNonNull(quote);
        this.base = base;
        this.quote = quote;
    }

    /**
     * Returns a {@code CurrencyPair} built with the specified currencies.
     *
     * @param base the base currency
     * @param quote the quote currency
     * @return a {@code CurrencyPair}
     */
    public static CurrencyPair valueOf(Currency base, Currency quote) {
        return new CurrencyPair(base, quote);
    }

    /**
     * Returns a {@code CurrencyPair} parsing the specified text (for example {@code EUR/USD}).
     *
     * @param text a textual representation of a currency pair
     * @return a {@code CurrencyPair}
     */
    public static CurrencyPair valueOf(String text) {
        if (! text.contains("/"))
            throw new IllegalArgumentException("'text' does not represent a currency pair (got '" + text + "', expected '<base>/<quote>')");
        String[] parts = text.split("/");
        if (parts.length != 2)
            throw new IllegalArgumentException("'text' does not represent a currency pair (got '" + text + "', expected '<base>/<quote>')");

        return valueOf(Currency.getInstance(parts[0]), Currency.getInstance(parts[1]));
    }

    public Currency base() {
        return base;
    }

    public Currency quote() {
        return quote;
    }

    @Override
    public int compareTo(CurrencyPair o) {
        int cbase = base.getCurrencyCode().compareTo(o.base.getCurrencyCode());
        if (cbase != 0)
            return cbase;
        return quote.getCurrencyCode().compareTo(o.quote.getCurrencyCode());
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
