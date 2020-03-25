package themissingobjects.finance;

import org.junit.Test;

import java.util.Currency;

import static org.junit.Assert.*;

public class CurrencyPairTest {

    @Test public void construction() {
        CurrencyPair expected = CurrencyPair.valueOf("EUR/USD");
        CurrencyPair actual = CurrencyPair.valueOf(Currency.getInstance("EUR"), Currency.getInstance("USD"));
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void construction_failure() {
        CurrencyPair.valueOf("Oh nooooooo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void construction_failure_due_too_many_currencies() {
        CurrencyPair.valueOf("EUR/USD/JPY");
    }

}