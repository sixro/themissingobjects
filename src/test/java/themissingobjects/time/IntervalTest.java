package themissingobjects.time;

import org.junit.Test;

import java.time.*;

import static org.junit.Assert.*;

public class IntervalTest {

    @Test public void contains_with_LocalDate() {
        Interval<LocalDate> i = Interval.between(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-03-01"));
        assertTrue(i.contains(LocalDate.parse("2020-02-01")));
        assertFalse(i.contains(LocalDate.parse("2020-04-01")));
        assertFalse(i.contains(LocalDate.parse("2019-12-01")));
    }

    @Test public void contains_with_LocalDate_on_edges() {
        Interval<LocalDate> i = Interval.between(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-03-01"));
        assertFalse(i.contains(LocalDate.parse("2019-12-31")));
        assertTrue(i.contains(LocalDate.parse("2020-01-01")));
        assertTrue(i.contains(LocalDate.parse("2020-02-29")));
        assertFalse(i.contains(LocalDate.parse("2020-03-01")));
    }

    @Test public void contains_with_ZonedDateTime() {
        Interval<ZonedDateTime> i = Interval.between(ZonedDateTime.parse("2020-01-01T12:00:00Z"), ZonedDateTime.parse("2020-01-01T14:00:00Z"));
        assertTrue(i.contains(ZonedDateTime.parse("2020-01-01T13:00:00Z")));
        assertFalse(i.contains(ZonedDateTime.parse("2020-01-01T15:00:00Z")));
        assertFalse(i.contains(ZonedDateTime.parse("2020-01-01T11:00:00Z")));
    }

    @Test public void contains_with_ZonedDateTime_on_edges() {
        Interval<ZonedDateTime> i = Interval.between(ZonedDateTime.parse("2020-01-01T12:00:00Z"), ZonedDateTime.parse("2020-01-01T14:00:00Z"));
        assertFalse(i.contains(ZonedDateTime.parse("2020-01-01T11:59:59.000Z")));
        assertTrue(i.contains(ZonedDateTime.parse("2020-01-01T12:00:00.000Z")));
        assertTrue(i.contains(ZonedDateTime.parse("2020-01-01T13:59:59.000Z")));
        assertFalse(i.contains(ZonedDateTime.parse("2020-01-01T14:00:00Z")));
    }

    @Test public void contains_with_Year() {
        Interval<Year> i = Interval.between(Year.of(2000), Year.of(2020));
        assertTrue(i.contains(Year.of(2010)));
        assertFalse(i.contains(Year.of(1999)));
        assertFalse(i.contains(Year.of(2030)));
    }

    @Test public void contains_with_YearMonth() {
        Interval<YearMonth> i = Interval.between(YearMonth.parse("2000-01"), YearMonth.parse("2000-03"));
        assertTrue(i.contains(YearMonth.parse("2000-02")));
        assertFalse(i.contains(YearMonth.parse("1999-12")));
        assertFalse(i.contains(YearMonth.parse("2001-01")));
    }

    @Test public void contains_with_Instant() {
        Interval<Instant> i = Interval.between(Instant.parse("2020-01-01T12:00:00Z"), Instant.parse("2020-01-01T14:00:00Z"));
        assertTrue(i.contains(Instant.parse("2020-01-01T13:00:00Z")));
        assertFalse(i.contains(Instant.parse("2020-01-01T15:00:00Z")));
        assertFalse(i.contains(Instant.parse("2020-01-01T11:00:00Z")));
    }

    @Test public void with_just_the_begin() {
        Interval<ZonedDateTime> i = Interval.beginningFrom(ZonedDateTime.parse("2020-01-01T12:00:00Z"));
        assertFalse(i.contains(ZonedDateTime.parse("2020-01-01T11:59:59.000Z")));
        assertTrue(i.contains(ZonedDateTime.parse("2020-01-01T12:00:00.000Z")));
        assertTrue(i.contains(ZonedDateTime.parse("2999-01-01T00:00:00.000Z")));
    }

    @Test public void with_just_the_end() {
        Interval<ZonedDateTime> i = Interval.endingTo(ZonedDateTime.parse("2020-01-01T12:00:00Z"));
        assertFalse(i.contains(ZonedDateTime.parse("2020-01-01T12:00:00.000Z")));
        assertTrue(i.contains(ZonedDateTime.parse("2020-01-01T11:59:59.999Z")));
        assertTrue(i.contains(ZonedDateTime.parse("0999-01-01T00:00:00.000Z")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate() {
        Interval.between(Year.of(2000), Year.of(1000));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_on_same_values() {
        Interval.between(Year.of(2000), Year.of(2000));
    }

    @Test public void textual_representation() {
        assertEquals("2020-01-01/2020-03-01", Interval.between(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-03-01")).toString());
        assertEquals("2020-01-01/..", Interval.beginningFrom(LocalDate.parse("2020-01-01")).toString());
        assertEquals("../2020-03-01", Interval.endingTo(LocalDate.parse("2020-03-01")).toString());
        assertEquals("../2020", Interval.endingTo(Year.of(2020)).toString());
    }
}