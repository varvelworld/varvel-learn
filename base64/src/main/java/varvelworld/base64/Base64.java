package varvelworld.base64;

/**
 * Created by luzhonghao on 2015/12/28.
 */
public class Base64 {

    final static char[] CHARS_INDEX = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S'
            , 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
            , 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u'
            , 'v', 'w', 'x', 'y', 'z'
            , '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/', '='};

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
}
