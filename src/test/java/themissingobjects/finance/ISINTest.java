package themissingobjects.finance;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ISINTest {

    @Test
    public void create_valid() {
        assertNotNull(ISIN.fromString("US0004026250"));
    }

    @Test
    public void create_valid_even_with_letters_in_the_middle() {
        assertNotNull(ISIN.fromString("IE00BKM4GZ66"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fail_when_invalid() {
        assertNotNull(ISIN.fromString("abc"));
    }

    @Test public void comparable() {
        ISIN ie = ISIN.fromString("IE00BKM4GZ66");
        ISIN us = ISIN.fromString("US0004026250");
        List<ISIN> l = Arrays.asList(us, ie);
        Collections.sort(l);
        assertEquals(ie, l.get(0));
    }
}
