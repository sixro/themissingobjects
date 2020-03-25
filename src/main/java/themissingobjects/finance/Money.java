package themissingobjects.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Represents monetary values.
 *
 * <p>According to this page about <a href="https://en.wikipedia.org/wiki/ISO_4217">ISO 4217</a> every currency
 * supports a specific number of fraction digits.<br>
 * This implementation of {@code Money} internally uses a {@code long} to represents the value.
 * Fraction digits are part of this internal value so that a {@code 12.34 EUR} is internally stored as {@code 1234},
 * while {@code 12.34 BHD} ("Bahraini dinar" supporting 3 fraction digits) is internally stored as {@code 12340}.
 * </p>
 * <p>
 * Because it is difficult to know how many fraction digits you should specify to the constructor, it is better
 * to use one of the provided factory methods such as the one using {@code BigDecimal}, {@code int} or
 * the {@code parse} method able to parse a money represented by text in a specific locale (or in the default one).
 * </p>
 *
 * <p>
 * This class is basically working like {@code FastMoney} provided by <a href="https://javamoney.github.io/ri.html">Moneta</a>
 * (the reference implementation of JavaMoney).<br>
 * The problem with that library is that I think it is quite complex for what we really need.<br>
 * Usually I need just a class to represent money used in applications with a little bit of accounting in there.<br>
 * I don't need an interface with different implementations.<br>
 * So I wrote this 'cause this is another object I am missing the most.
 * </p>
 *
 * @author <a href="mailto:me@sixro.net" >Sixro</a>
 * @since 1.0
 */
public class Money implements Comparable<Money>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Map<Integer, Integer> MULTIPLIER_BY_FRACTION_DIGITS = new HashMap<Integer, Integer>() {{
        put(0, 1);
        put(1, 10);
        put(2, 100);
        put(3, 1000);
        put(4, 10000);
    }};

    private final long value;
    private final Currency currency;

    /**
     * Create a {@code Money} using specified value and currency where the value contains all fraction digits required by the currency.
     *
     * @param value a value
     * @param currency a currency
     */
    public Money(long value, Currency currency) {
        this.value = value;
        this.currency = currency;
    }

    /**
     * Returns a {@code Money} using specified value and currency.
     *
     * @param value a value expressed using {@code BigDecimal}
     * @param currency a currency
     * @return a {@code Money}
     */
    public static Money valueOf(BigDecimal value, Currency currency) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(currency);
        return new Money(value.movePointRight(currency.getDefaultFractionDigits()).longValue(), currency);
    }

    /**
     * Returns a {@code Money} using specified value and currency assuming the value does not contain fraction digits.
     *
     * <p>
     * For example {@code Money.value(1, Currency.getInstance("EUR")} is equivalent to {@code new Money(100l, Currency.getInstance("EUR")}.
     * </p>
     * @param valueWithoutFractionDigits an integer value without any fraction digit
     * @param currency a currency
     * @return a {@code Money}
     */
    public static Money valueOf(int valueWithoutFractionDigits, Currency currency) {
        Integer m = MULTIPLIER_BY_FRACTION_DIGITS.get(currency.getDefaultFractionDigits());
        return new Money(valueWithoutFractionDigits * m, currency);
    }

    /**
     * Returns a {@code Money} parsing the specified text in the default {@link Locale}.
     *
     * @param text a text
     * @return a {@code Money}
     * @throws ParseException if text cannot be parsed
     *
     * @see SmartDecimalFormat
     */
    public static Money parse(String text) throws ParseException {
        return parse(text, Locale.getDefault());
    }

    /**
     * Returns a {@code Money} parsing the specified text in the specified {@link Locale}.
     *
     * @param text a text
     * @param locale a {@link Locale}
     * @return a {@code Money}
     * @throws ParseException if text cannot be parsed
     *
     * @see SmartDecimalFormat
     */
    public static Money parse(String text, Locale locale) throws ParseException {
        DecimalFormat df = new SmartDecimalFormat("¤#,##0.###", locale);
        df.setParseBigDecimal(true);
        BigDecimal parsed = (BigDecimal) df.parse(text);
        return Money.valueOf(parsed, df.getCurrency());
    }

    /**
     * Returns the sum of this money and the specified one.
     *
     * @param that another money
     * @return the sum of this money and the specified one
     */
    public Money plus(Money that) {
        if (! currency.equals(that.currency))
            throw new IllegalArgumentException("currency must be " + currency + " (found " + that.currency + ")");
        return new Money(value + that.value, currency);
    }

    /**
     * Returns the subtraction of this money and the specified one.
     *
     * @param that another money
     * @return the subtraction of this money and the specified one
     */
    public Money minus(Money that) {
        if (! currency.equals(that.currency))
            throw new IllegalArgumentException("currency must be " + currency + " (found " + that.currency + ")");
        return new Money(value - that.value, currency);
    }

    /**
     * Returns the result of multiplying this money for the specified multiplier.
     *
     * @param multiplier a multiplier
     * @return the result of multiplying this money for the specified multiplier.
     */
    public Money times(int multiplier) {
        return new Money(value * multiplier, currency);
    }

    /**
     * Returns the result of multiplying this money for the specified multiplier.
     *
     * @param multiplier a multiplier
     * @return the result of multiplying this money for the specified multiplier.
     */
    public Money times(BigDecimal multiplier) {
        return new Money(BigDecimal.valueOf(value).multiply(multiplier).longValue(), currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return value == money.value &&
                Objects.equals(currency, money.currency);
    }

    /**
     * Transform this money to the {@link BigDecimal} representing its value.
     *
     * @return a {@link BigDecimal}
     */
    public BigDecimal toBigDecimal() {
        int fractionDigits = currency.getDefaultFractionDigits();
        return BigDecimal.valueOf(value).movePointLeft(fractionDigits);
    }

    /**
     * Returns the {@link Currency}.
     *
     * @return a {@link Currency}
     */
    public Currency currency() {
        return currency;
    }

    /**
     * Returns the converted money using specified exchange rate.
     *
     * <p>
     * This operation is able to convert to {@code quote} or {@code base} according to the
     * actual currency. So, calling this to a {@code 10 EUR} with an exchange rate of
     * {@code 10 EUR/USD} will return {@code 100 USD}, while calling this to a {@code 10 USD} with
     * the same exchange rate ({@code 10 EUR/USD}) will return {@code 1 EUR}.
     * </p>
     *
     * @param rate an exchange rate
     * @return a money of the alternate currency found in the currency pair of the specified exchange rate
     * @throws IllegalArgumentException if the specified {@link ExchangeRate} is on a {@link CurrencyPair} not related to the currency of this money
     */
    public Money convert(ExchangeRate rate) {
        Currency base = rate.currencyPair().base();
        if (currency.equals(base)) {
            BigDecimal v = toBigDecimal().multiply(rate.quote().toBigDecimal());
            return valueOf(v, rate.currencyPair().quote());
        }
        if (currency.equals(rate.currencyPair().quote())) {
            BigDecimal v = toBigDecimal().divide(rate.quote().toBigDecimal(), base.getDefaultFractionDigits(), RoundingMode.HALF_UP);
            return valueOf(v, base);
        }

        throw new IllegalArgumentException("exchange rate must be related to currency of this money (found " + rate + " while this currency is " + currency + ")");
    }

    @Override
    public int compareTo(Money o) {
        int c = currency.getCurrencyCode().compareTo(o.currency.getCurrencyCode());
        if (c != 0)
            return c;
        return Long.compare(value, o.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, currency);
    }

    @Override
    public String toString() {
        return toString(Locale.getDefault());
    }

    /**
     * Returns this money represented in the specified {@link Locale} (e.g. {@code €1.23}).
     *
     * @param locale a {@link Locale}
     * @return a textual representation of this money
     */
    public String toString(Locale locale) {
        DecimalFormat df = new SmartDecimalFormat(newPattern(currency.getDefaultFractionDigits()), locale);
        df.setCurrency(currency);
        return df.format(toBigDecimal());
    }

    private String newPattern(int fractionDigits) {
        String pattern = "¤#,##0";
        if (fractionDigits > 0) {
            pattern += ".";
            for (int i = 0; i < fractionDigits; i++)
                pattern += "0";
        }
        return pattern;
    }

}
