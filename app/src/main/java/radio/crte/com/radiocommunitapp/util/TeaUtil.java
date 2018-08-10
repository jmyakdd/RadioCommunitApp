package radio.crte.com.radiocommunitapp.util;

/**
 * Tea算法
 * 每次操作可以处理8个字节数据
 * KEY为16字节,应为包含4个int型数的int[]，一个int为4个字节
 * 加密解密轮数应为8的倍数，推荐加密轮数为64轮
 */
public class TeaUtil {
    public final static long[] KEY = new long[]{//加密解密所用的KEY
            0x152C7E9DL, 0x38BE41C7L, 0x71E96CA4L, 0x6CAC1AFCL
    };

    // 加密
    public static byte[] encrypt(byte[] content, int offset, long[] key, int times) {//times为加密轮数
        long y = bytesToUint4(content, offset);
        long z = bytesToUint4(content, offset + 4);
        long sum = 0, i;
        long delta = 0x9E3779B9L; //这是算法标准给的值
        long a = key[0], b = key[1], c = key[2], d = key[3];

        for (i = 0; i < times; i++) {
            sum = (sum + delta) & 0xffffffffL;
            y = (y + ((z << 4) + a ^ z + sum ^ (z >> 5) + b)) & 0xffffffffL;
            z = (z + ((y << 4) + c ^ y + sum ^ (y >> 5) + d)) & 0xffffffffL;
        }

        byte[] ret = new byte[8];

        uint4ToByte(y, ret, 0);
        uint4ToByte(z, ret, 4);
        return ret;
    }

    public static long bytesToUint4(byte arr[], int offset) {
        return ((long) (arr[0 + offset] & 0xFF) << 24) + ((long) (arr[1 + offset] & 0xFF) << 16)
                + ((long) (arr[2 + offset] & 0xFF) << 8) + ((long) (arr[3 + offset] & 0xFF));
    }

    public static void uint4ToByte(long content, byte arr[], int offset) {
        arr[offset + 3] = (byte) (content & 0xff);
        arr[offset + 2] = (byte) ((content >> 8) & 0xff);
        arr[offset + 1] = (byte) ((content >> 16) & 0xff);
        arr[offset] = (byte) ((content >> 24) & 0xff);

    }

}