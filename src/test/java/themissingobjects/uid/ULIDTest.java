package themissingobjects.uid;

import static org.junit.Assert.*;

import org.junit.Test;

public class ULIDTest {

    @Test public void randomness() {
        assertNotEquals(ULID.randomULID(), ULID.randomULID());
    }

    // TODO validation
}