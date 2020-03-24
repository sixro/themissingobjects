package themissingobjects.finance;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Represents monetary values.
 *
 * <p>According to this page about <a href="https://en.wikipedia.org/wiki/ISO_4217">ISO 4217</a> every currency
 * supports a specific number of fraction digits.<br/>
 * This implementation of {@code Money} internally uses a {@code long} to represents the value and
 * fraction digits are part of it so that a {@code 12.34 EUR} has its value stored as {@code 1234},
 * while {@code 12.34 BHD} ("Bahraini dinar" supporting 3 fraction digits) has its value stored as {@code 12340}.
 * </p>
 * <p>
 * Because it is difficult to know how many fraction digits you should specify to the constructor, it is better
 * to use one of the provided factory methods such as the one using {@code BigDecimal}, {@code int} or
 * the {@code parse} method able to parse a money represented by text a specific locale (or in the default one).
 * </p>
 *
 * <p>
 * This class is basically working like {@code FastMoney} provided by <a href="https://javamoney.github.io/ri.html">Moneta</a> (the reference implementation of JavaMoney</a>.<br/>
 * The problem with that library is that I think it is quite complex for what we really need.<br/>
 * Usually I need just a class to represent money used in applications with a little bit of accounting in there.<br/>
 * I don't need different implementations.<br/>
 * So I wrote this 'cause this is another object I am missing the most.
 * </p>
 *
 * @author <a href="mailto:me@sixro.net" >Sixro</a>
 * @since 1.0
 */
public class Money {

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

    public static Money parse(String text) throws ParseException {
        return parse(text, Locale.getDefault());
    }

    public static Money parse(String text, Locale locale) throws ParseException {
        DecimalFormat df = new SmartDecimalFormat("¤#,##0.###", locale);
        df.setParseBigDecimal(true);
        BigDecimal parsed = (BigDecimal) df.parse(text);
        return Money.valueOf(parsed, df.getCurrency());
    }

    public Money plus(Money that) {
        if (! currency.equals(that.currency))
            throw new IllegalArgumentException("currency must be " + currency + " (found " + that.currency + ")");
        return new Money(value + that.value, currency);
    }

    public Money minus(Money that) {
        if (! currency.equals(that.currency))
            throw new IllegalArgumentException("currency must be " + currency + " (found " + that.currency + ")");
        return new Money(value - that.value, currency);
    }

    public Money times(int multiplier) {
        return new Money(value * multiplier, currency);
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(value, currency);
    }

    @Override
    public String toString() {
        return toString(Locale.getDefault());
    }

    public String toString(Locale locale) {
        int fractionDigits = currency.getDefaultFractionDigits();
        BigDecimal v = BigDecimal.valueOf(value).movePointLeft(fractionDigits);

        DecimalFormat df = new SmartDecimalFormat(newPattern(currency.getDefaultFractionDigits()), locale);
        df.setCurrency(currency);
        return df.format(v);
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
