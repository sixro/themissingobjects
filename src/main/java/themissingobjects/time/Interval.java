package themissingobjects.time;

import java.io.Serializable;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an interval of {@code Temporal}s.
 *
 * <p>
 * Because it is based on {@code Temporal}, you can use {@code LocalDate}, {@code ZonedDateTime}, {@code Instant} or even {@code Year}...
 * </p>
 * <p>
 * The textual representation uses a ISO-8601 format that in date 2019 is in draft state yet, but it is clear:
 * </p>
 * <ul>
 *     <li>an interval between 2 times has a {@code /} between. E.g. {@code 2000/2010}</li>
 *     <li>an open interval has the missing part represented by {@code ..}. E.g. {@code ../2010} or {@code 2020/..}</li>
 * </ul>
 *
 * @param <T> a Temporal
 *
 * @author <a href="mailto:me@sixro.net" >Sixro</a>
 * @since 1.0
 */
public class Interval<T extends Temporal> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Map<Class<? extends Temporal>, ChronoUnit> TEMPORAL_UNIT_BY_TEMPORAL = newTemporalUnitByTemporal();

    private final T from;
    private final T to;

    private Interval(T from, T to) {
        if (from != null && to != null) {
            TemporalUnit temporalUnit = TEMPORAL_UNIT_BY_TEMPORAL.get(to.getClass());
            if (from.until(to, temporalUnit) <= 0)
                throw new IllegalArgumentException("'to' must be greater than 'from' (from is '" + from + "', to is '" + to + "')");
        } else if (from == null && to == null)
            throw new IllegalArgumentException("At least one between 'from' and 'to' is required (from is '" + from + "', to is '" + to + "')");

        this.from = from;
        this.to = to;
    }

    public static <T extends Temporal> Interval<T> between(T from, T to) {
        return new Interval<T>(from, to);
    }

    public static <T extends Temporal> Interval<T> beginningFrom(T from) {
        return new Interval<T>(from, null);
    }

    public static <T extends Temporal> Interval<T> endingTo(T to) {
        return new Interval<T>(null, to);
    }

    /**
     * Returns {@code true} if the specified temporal is contained in this interval.
     * @param t a temporal
     * @return {@code true} if the specified temporal is contained in this interval, otherwise {@code false}
     */
    public boolean contains(T t) {
        TemporalUnit temporalUnit = TEMPORAL_UNIT_BY_TEMPORAL.get(t.getClass());
        if (to != null && from != null) {
            long fromTo = from.until(to, temporalUnit);
            long tTo = t.until(to, temporalUnit);
            return fromTo >= tTo && tTo > 0;
        } else if (to != null) {
            long tTo = t.until(to, temporalUnit);
            return tTo > 0;
        } else {
            long fromT = from.until(t, temporalUnit);
            return fromT >= 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval<?> interval = (Interval<?>) o;
        return Objects.equals(from, interval.from) &&
                Objects.equals(to, interval.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return Objects.toString(from, "..") + "/" + Objects.toString(to, "..");
    }

    private static Map<Class<? extends Temporal>, ChronoUnit> newTemporalUnitByTemporal() {
        Map<Class<? extends Temporal>, ChronoUnit> map = new HashMap<>();
        map.put(Year.class, ChronoUnit.YEARS);
        map.put(YearMonth.class, ChronoUnit.MONTHS);
        map.put(LocalDate.class, ChronoUnit.DAYS);
        map.put(ZonedDateTime.class, ChronoUnit.MILLIS);
        map.put(Instant.class, ChronoUnit.MILLIS);
        return map;
    }

}
