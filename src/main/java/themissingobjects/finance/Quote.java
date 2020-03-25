package themissingobjects.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a <a href="https://en.wikipedia.org/wiki/Financial_quote" >financial quote</a>.
 *
 * @author <a href="mailto:me@sixro.net" >Sixro</a>
 * @since 1.0
 */
public class Quote implements Comparable<Quote>, Serializable {

    private static final long serialVersionUID = 1L;

    public static final Quote ZERO = Quote.of(0);
    public static final Quote ONE = Quote.of(1);
    public static final Quote TEN = Quote.of(10);
    public static final Quote ONE_HUNDRED = Quote.of(100);
    public static final Quote ONE_THOUSAND = Quote.of(1000);

    private final long value;
    private final int fractionDigits;

    private Quote(long value, int fractionDigits) {
        this.value = value;
        this.fractionDigits = fractionDigits;
    }

    public static Quote of(BigDecimal value) {
        int fractionDigits = value.scale();
        return new Quote(value.movePointRight(fractionDigits).longValue(), fractionDigits);
    }

    public static Quote of(int value) {
        return new Quote(value, 0);
    }

    public Quote plus(Quote that) {
        if (fractionDigits == that.fractionDigits)
            return new Quote(   value + that.value, fractionDigits);

        int maxDigits = Math.max(fractionDigits, that.fractionDigits);
        return scale(maxDigits).plus(that.scale(maxDigits));
    }

    public Quote minus(Quote that) {
        if (fractionDigits == that.fractionDigits)
            return new Quote(value - that.value, fractionDigits);

        int maxDigits = Math.max(fractionDigits, that.fractionDigits);
        return scale(maxDigits).minus(that.scale(maxDigits));
    }

    public Quote times(int multiplier) {
        return new Quote(value * multiplier, fractionDigits);
    }

    public Quote times(BigDecimal multiplier) {
        BigDecimal result = toBigDecimal().multiply(multiplier);
        return of(result);
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(value).movePointLeft(fractionDigits);
    }

    @Override
    public int compareTo(Quote o) {
        return Long.compare(value, o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quote that = (Quote) o;
        if (fractionDigits == that.fractionDigits)
            return value == that.value;

        int maxDigits = Math.max(fractionDigits, that.fractionDigits);
        return scale(maxDigits).equals(that.scale(maxDigits));
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, fractionDigits);
    }

    @Override
    public String toString() {
        return toBigDecimal().toPlainString();
    }

    private Quote scale(int newFractionDigits) {
        if (newFractionDigits == fractionDigits)
            return this;
        return new Quote((long) (value * Math.pow(10d, newFractionDigits - fractionDigits)), newFractionDigits);
    }

}
