package themissingobjects.uid;

import java.util.Objects;

public class ULID {

    private final String code;

    private ULID(String code) {
        this.code = code;
    }

    public static ULID randomULID() {
        return new ULID("test");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ULID ulid = (ULID) o;
        return Objects.equals(code, ulid.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return code;
    }

}
