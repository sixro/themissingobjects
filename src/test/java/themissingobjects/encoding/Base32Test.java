package themissingobjects.encoding;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.junit.Test;

public class Base32Test {

    @Test public void encode() throws UnsupportedEncodingException {
        byte[] bytes = "test".getBytes(Charset.forName("US-ASCII"));
        byte[] encode = new org.apache.commons.codec.binary.Base32().encode(bytes);
        System.out.println(new String(encode));
        String encoded = new Base32().encode(bytes);
        System.out.println(encoded);
    }
}