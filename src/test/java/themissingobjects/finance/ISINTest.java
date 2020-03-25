package themissingobjects.finance;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ISINTest {

    @Test
    public void create_valid() {
        assertNotNull(ISIN.valueOf("US0004026250"));
    }

    @Test
    public void create_valid_even_with_letters_in_the_middle() {
        assertNotNull(ISIN.valueOf("IE00BKM4GZ66"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fail_when_invalid() {
        assertNotNull(ISIN.valueOf("abc"));
    }

}
