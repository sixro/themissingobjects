package themissingobjects.finance;

import org.junit.Test;

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

}
