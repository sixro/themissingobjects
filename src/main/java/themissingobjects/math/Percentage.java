package themissingobjects.math;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a <a href="https://en.wikipedia.org/wiki/Percentage" >percentage</a> useful to make your core domain more expressive.
 *
 * @author Roberto Simoni
 * @since 1.0
 */
public class Percentage implements Comparable<Percentage>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final BigDecimal _100 = BigDecimal.valueOf(100);

    private static final Map<Integer, Percentage> CACHED = newCachedPercentages();

    private final BigDecimal value;

    private Percentage(BigDecimal value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    /**
     * Creates a percentage from textual representation like {@code 100%}.
     *
     * <p>
     * Even this factory method tries to take advantage of the cached values.
     * </p>
     * 
     * @param textualRepresentation a percentage textual representation
     * @return a percentage
     * 
     * @see #valueOf(int)
     */
    public static Percentage valueOf(String textualRepresentation) {
        DecimalFormat decimalFormat = newDecimalFormat();
        BigDecimal p = (BigDecimal) decimalFormat.parse(textualRepresentation, new ParsePosition(0));
        BigDecimal asInt = p.multiply(_100);
        boolean isInteger = asInt.stripTrailingZeros().scale() <= 0;
        return isInteger ? valueOf(asInt.intValue()) : new Percentage(p);
    }

    /**
     * Creates a percentage from a BigDecimal value where 0 means 0% and 1 means 100%.
     *
     * @param v the percentage expressed as BigDecimal
     * @return a percentage
     */
    public static Percentage valueOf(BigDecimal v) {
        return new Percentage(v);
    }

    /**
     * Creates a percentage from a double value where 0 means 0% and 1 means 100%.
     *
     * @param v the percentage expressed as double
     * @return a percentage
     */
    public static Percentage valueOf(double v) {
        return valueOf(BigDecimal.valueOf(v));
    }

    /**
     * Creates a percentage from an int value where 0 means 0% and 100 means 100%.
     *
     * <p>
     * This factory method takes advantage of cached percentages (e.g. from 0 to 100), so that the memory
     * consumed will be lower than the normal.
     * </p>
     *
     * @param v the percentage expressed as int
     * @return a percentage
     */
    public static Percentage valueOf(int v) {
        Percentage percentage = CACHED.get(v);
        if (percentage != null)
            return percentage;

        return new Percentage(BigDecimal.valueOf(v).divide(Percentage._100));
    }

    /**
     * Returns the number calculated applying this percentage to the specified value.
     *
     * @param v the starting value on which this percentage has to be applied
     * @return the number calculated applying this percentage
     */
    public BigDecimal of(BigDecimal v) {
        return value.multiply(v);
    }

    /**
     * Returns the number calculated applying this percentage to the specified value using the specified scale and rounding mode.
     *
     * @param v the starting value on which this percentage has to be applied
     * @return the number calculated applying this percentage
     */
    public BigDecimal of(BigDecimal v, int scale, RoundingMode roundingMode) {
        BigDecimal multiplied = value.multiply(v);
        return multiplied.setScale(scale, roundingMode);
    }

    public BigDecimal toBigDecimal() {
        return value;
    }

    @Override
    public int compareTo(Percentage that) {
        return value.compareTo(that.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Percentage that = (Percentage) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = newDecimalFormat();
        return decimalFormat.format(value);
    }

    private static DecimalFormat newDecimalFormat() {
        DecimalFormat dm = new DecimalFormat("#,##0.##%");
        dm.setParseBigDecimal(true);
        return dm;
    }

    private static Map<Integer, Percentage> newCachedPercentages() {
        Map<Integer, Percentage> map = new HashMap<>(101);
        for (int i = 0; i <= 100; i += 5) {
            BigDecimal p = BigDecimal.valueOf(i).divide(_100, 2, RoundingMode.HALF_UP);
            map.put(i, new Percentage(p));
        }
        return map;
    }

}
