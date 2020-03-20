package themissingobjects.finance;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents monetary values.
 *
 * @author <a href="mailto:me@sixro.net" >Sixro</a>
 * @since 1.0
 */
public class Money {

    private final long value;
    private final Currency currency;

    public Money(long value, Currency currency) {
        this.value = value;
        this.currency = currency;
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
        int fractionDigits = currency.getDefaultFractionDigits();
        DecimalFormat df = new DecimalFormat(newPattern(fractionDigits), DecimalFormatSymbols.getInstance(Locale.US));
        df.setCurrency(currency);
        BigDecimal v = BigDecimal.valueOf(value).movePointLeft(fractionDigits);
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
