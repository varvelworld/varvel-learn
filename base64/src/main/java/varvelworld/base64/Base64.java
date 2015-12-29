package varvelworld.base64;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luzhonghao on 2015/12/28.
 */
public class Base64 {

    final static char[] CHARS_INDEX = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O'
            , 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
            , 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u'
            , 'v', 'w', 'x', 'y', 'z'
            , '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/', '='};

    final static Map<Character, Integer> CHARS_REVERSE_INDEX = new HashMap<Character, Integer>(){{
        put('A', 0); put('B', 1); put('C', 2); put('D', 3); put('E', 4);
        put('F', 5); put('G', 6); put('H', 7); put('I', 8); put('J', 9);
        put('K', 10); put('L', 11); put('M', 12); put('N', 13); put('O', 14);
        put('P', 15); put('Q', 16); put('R', 17); put('S', 18); put('T', 19);
        put('U', 20); put('V', 21); put('W', 22); put('X', 23); put('Y', 24);
        put('Z', 25);
        put('a', 26); put('b', 27); put('c', 28); put('d', 29); put('e', 30);
        put('f', 31); put('g', 32); put('h', 33); put('i', 34); put('j', 35);
        put('k', 36); put('l', 37); put('m', 38); put('n', 39); put('o', 40);
        put('p', 41); put('q', 42); put('r', 43); put('s', 44); put('t', 45);
        put('u', 46); put('v', 47); put('w', 48); put('x', 49); put('y', 50);
        put('z', 51);
        put('0', 52); put('1', 53); put('2', 54); put('3', 55); put('4', 56);
        put('5', 57); put('6', 58); put('7', 59); put('8', 60); put('9', 61);
        put('+', 62); put('/', 63); put('=', 64);
    }};

    public static String encrypt(byte[] bytes) {
        int offset = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while(offset + 3 <= bytes.length) {
            stringBuilder.append(CHARS_INDEX[bytes[offset] >>> 2]);
            stringBuilder.append(CHARS_INDEX[bytes[offset] << 4 & 0x30 | bytes[offset + 1] >>> 4]);
            stringBuilder.append(CHARS_INDEX[bytes[offset + 1] << 2 & 0x3C | bytes[offset + 2] >>> 6]);
            stringBuilder.append(CHARS_INDEX[bytes[offset + 2] & 0x3F]);
            offset += 3;
        }
        if(bytes.length % 3 == 1) {
            stringBuilder.append(CHARS_INDEX[bytes[offset] >>> 2]);
            stringBuilder.append(CHARS_INDEX[bytes[offset] << 4 & 0x30]);
            stringBuilder.append(CHARS_INDEX[64]);
            stringBuilder.append(CHARS_INDEX[64]);
        }
        else if(bytes.length % 3 == 2) {
            stringBuilder.append(CHARS_INDEX[bytes[offset] >>> 2]);
            stringBuilder.append(CHARS_INDEX[bytes[offset] << 4 & 0x30 | bytes[offset + 1] >>> 4]);
            stringBuilder.append(CHARS_INDEX[bytes[offset + 1] << 2 & 0x3C]);
            stringBuilder.append(CHARS_INDEX[64]);
        }
        return stringBuilder.toString();
    }

    public static byte[] decrypt(String ciphertext) {
        int b = 0;
        byte[] bytes = new byte[ciphertext.length() / 4 * 3];
        int i = 0;
        while (i < ciphertext.length()) {
            int d = ((CHARS_REVERSE_INDEX.get(ciphertext.charAt(i++)) & 0x3F) << 18)
                    | ((CHARS_REVERSE_INDEX.get(ciphertext.charAt(i++)) & 0x3F) << 12)
                    | ((CHARS_REVERSE_INDEX.get(ciphertext.charAt(i++)) & 0x3F) << 6)
                    | ((CHARS_REVERSE_INDEX.get(ciphertext.charAt(i++)) & 0x3F));
            bytes[b++] = (byte) ((d >> 16) & 0xFF);
            bytes[b++] = (byte) ((d >> 8) & 0xFF);
            bytes[b++] = (byte) (d & 0xFF);
        }
        int endBlank = 0;
        if(ciphertext.charAt(ciphertext.length() - 1) == CHARS_INDEX[64]) {
            ++endBlank;
            if(ciphertext.charAt(ciphertext.length() - 2) == CHARS_INDEX[64]) {
                ++endBlank;
            }
        }
        return Arrays.copyOf(bytes, bytes.length - endBlank);
    }
}
