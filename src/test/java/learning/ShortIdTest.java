package learning;

import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class ShortIdTest {

    // https://github.com/ulid/spec
    // TODO Create a ULID
    // ULID.randomULID
    // ULID.fromString

    @Test public void _() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        String randomNumberAsText = Integer.toString(random.nextInt());
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digested = md.digest(randomNumberAsText.getBytes());
        System.out.println(crunchifyEncodeUsingHEX(digested));
    }

    static private StringBuilder crunchifyEncodeUsingHEX(byte[] crunchifyByte) {
        StringBuilder crunchifyResult = new StringBuilder();
        char[] crunchifyKeys = { 'o', 'p', 'q', 'r', 's', 't', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        for (int index = 0; index < crunchifyByte.length; ++index) {
            byte myByte = crunchifyByte[index];

            // Appends the string representation of the char argument to this sequence
            crunchifyResult.append(crunchifyKeys[(myByte & 0xf0) >> 9]);
            crunchifyResult.append(crunchifyKeys[myByte & 0x0f]);
        }
        return crunchifyResult;
    }
}
