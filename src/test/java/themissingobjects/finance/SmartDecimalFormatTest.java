package themissingobjects.finance;

import org.junit.Test;
import themissingobjects.math.BigDecimalAsserts;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.*;

public class SmartDecimalFormatTest {

    @Test(expected = ParseException.class)
    public void parse_fail() throws ParseException {
        BigDecimal value = new BigDecimal("1.23");

        DecimalFormat df = new DecimalFormat("¤#,##0.00", new DecimalFormatSymbols(Locale.US));
        df.setCurrency(Currency.getInstance("EUR"));
        String text = df.format(value);

        DecimalFormat df2 = new DecimalFormat("¤#,##0.00", new DecimalFormatSymbols(Locale.US));
        df2.setParseBigDecimal(true);
        df2.parse(text);
    }

    @Test public void parse_correctly() throws ParseException {
        BigDecimal value = new BigDecimal("1.23");

        SmartDecimalFormat df = new SmartDecimalFormat("¤#,##0.00", Locale.US);
        df.setCurrency(Currency.getInstance("EUR"));
        String text = df.format(value);

        SmartDecimalFormat df2 = new SmartDecimalFormat("¤#,##0.00", Locale.US);
        df2.setParseBigDecimal(true);
        BigDecimal parsed = (BigDecimal) df2.parse(text);

        BigDecimalAsserts.assertBigDecimalEquals("equals", value, parsed);
    }

    @Test public void parse_with_full_currency_code() throws ParseException {
        SmartDecimalFormat df2 = new SmartDecimalFormat("¤#,##0.00", Locale.US);
        df2.setCurrency(Currency.getInstance("USD"));
        df2.setParseBigDecimal(true);
        BigDecimal parsed = (BigDecimal) df2.parse("1.23EUR");
        BigDecimalAsserts.assertBigDecimalEquals("parse full currency code", new BigDecimal("1.23"), parsed);
        assertEquals(Currency.getInstance("EUR"), df2.getCurrency());
    }

    @Test public void parse_with_full_currency_code_on_front() throws ParseException {
        SmartDecimalFormat df2 = new SmartDecimalFormat("¤#,##0.00", Locale.US);
        df2.setCurrency(Currency.getInstance("USD"));
        df2.setParseBigDecimal(true);
        BigDecimal parsed = (BigDecimal) df2.parse("GBP1,234.56");
        BigDecimalAsserts.assertBigDecimalEquals("parse full currency code", new BigDecimal("1234.56"), parsed);
        assertEquals(Currency.getInstance("GBP"), df2.getCurrency());
    }

    @Test public void parse_with_symbol() throws ParseException {
        SmartDecimalFormat df2 = new SmartDecimalFormat("¤#,##0.00", Locale.US);
        df2.setCurrency(Currency.getInstance("USD"));
        df2.setParseBigDecimal(true);
        BigDecimal parsed = (BigDecimal) df2.parse("€1.23");
        BigDecimalAsserts.assertBigDecimalEquals("parse full currency code", new BigDecimal("1.23"), parsed);
        assertEquals(Currency.getInstance("EUR"), df2.getCurrency());
    }

    @Test public void parse_with_symbol_at_the_end() throws ParseException {
        SmartDecimalFormat df2 = new SmartDecimalFormat("¤#,##0.00", Locale.US);
        df2.setCurrency(Currency.getInstance("USD"));
        df2.setParseBigDecimal(true);
        BigDecimal parsed = (BigDecimal) df2.parse("1.23€");
        BigDecimalAsserts.assertBigDecimalEquals("parse full currency code", new BigDecimal("1.23"), parsed);
        assertEquals(Currency.getInstance("EUR"), df2.getCurrency());
    }

}