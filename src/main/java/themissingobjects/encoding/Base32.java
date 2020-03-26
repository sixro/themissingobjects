package themissingobjects.encoding;

public class Base32 {

    private final char[] alphabet;
    private final int mask;
    private final int shift;

    public Base32() {
        this("ABCDEFGHIJKLMNOPQRSTUV0123456789".toCharArray());
    }

    public Base32(char[] alphabet) {
        this.alphabet = alphabet;
        this.mask = alphabet.length -1;
        this.shift = Integer.numberOfLeadingZeros(alphabet.length);
        System.out.println(mask + "; " + shift);
    }

    /*
    public String encode(byte[] data) {
        System.out.println(data.length);
        int outputLength = (data.length * 8 + shift - 1) / shift;
        System.out.println(outputLength);

        StringBuilder result = new StringBuilder(outputLength);

        int buffer = data[0];
        int next = 1;
        int bitsLeft = 8;
        while (bitsLeft > 0 || next < data.length) {
            if (bitsLeft < shift) {
                if (next < data.length) {
                    buffer <<= 8;
                    buffer |= (data[next++] & 0xff);
                    bitsLeft += 8;
                } else {
                    int pad = shift - bitsLeft;
                    buffer <<= pad;
                    bitsLeft += pad;
                }
            }
            int index = mask & (buffer >> (bitsLeft - shift));
            bitsLeft -= shift;
            result.append(alphapet[index]);
        }
        return result.toString();
    }

     */

    public String encode(byte[] data) {
        StringBuffer base32 = new StringBuffer((data.length * 8 + 4) / 5);
        int currByte, digit, i = 0;
        while (i < data.length) {
            // INVARIANTS FOR EACH STEP n in [0..5[; digit in [0..31[;
            // The remaining n bits are already aligned on top positions
            // of the 5 least bits of digit, the other bits are 0.
            // //// STEP n = 0; insert new 5 bits, leave 3 bits
            currByte = data[i++] & 255;
            base32.append(alphabet[(currByte >> 3)]);
            digit = (currByte & 7) << 2;
            if (i >= data.length) { // put the last 3 bits
                base32.append(alphabet[(digit)]);
                break;
            }
            // //// STEP n = 3: insert 2 new bits, then 5 bits, leave 1 bit
            currByte = data[i++] & 255;
            base32.append(alphabet[(digit | (currByte >> 6))]);
            base32.append(alphabet[((currByte >> 1) & 31)]);
            digit = (currByte & 1) << 4;
            if (i >= data.length) { // put the last 1 bit
                base32.append(alphabet[(digit)]);
                break;
            }
            // //// STEP n = 1: insert 4 new bits, leave 4 bit
            currByte = data[i++] & 255;
            base32.append(alphabet[(digit | (currByte >> 4))]);
            digit = (currByte & 15) << 1;
            if (i >= data.length) { // put the last 4 bits
                base32.append(alphabet[(digit)]);
                break;
            }
            // //// STEP n = 4: insert 1 new bit, then 5 bits, leave 2 bits
            currByte = data[i++] & 255;
            base32.append(alphabet[(digit | (currByte >> 7))]);
            base32.append(alphabet[((currByte >> 2) & 31)]);
            digit = (currByte & 3) << 3;
            if (i >= data.length) { // put the last 2 bits
                base32.append(alphabet[(digit)]);
                break;
            }
            // /// STEP n = 2: insert 3 new bits, then 5 bits, leave 0 bit
            currByte = data[i++] & 255;
            base32.append(alphabet[(digit | (currByte >> 5))]);
            base32.append(alphabet[(currByte & 31)]);
            // // This point is reached for bytes.length multiple of 5
        }
        return base32.toString();
    }
}
