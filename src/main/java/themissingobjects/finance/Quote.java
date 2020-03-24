package themissingobjects.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a <a href="https://en.wikipedia.org/wiki/Financial_quote" >quote</a>.
 */
public class Quote implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long value;
    private final int fractionDigits;

    private Quote(long value, int fractionDigits) {
        this.value = value;
        this.fractionDigits = fractionDigits;
    }

    public static Quote valueOf(BigDecimal value) {
        int fractionDigits = value.scale();
        return new Quote(value.movePointRight(fractionDigits).longValue(), fractionDigits);
    }

    public static Quote valueOf(int value) {
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
        return valueOf(result);
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(value).movePointLeft(fractionDigits);
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
