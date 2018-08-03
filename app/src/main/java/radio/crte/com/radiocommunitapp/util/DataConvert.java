package radio.crte.com.radiocommunitapp.util;

import android.annotation.SuppressLint;

public class DataConvert {

    @SuppressLint("DefaultLocale")
    public static String byteToHexString(byte[] b) {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            buffer.append(hex.toUpperCase() + " ");
        }
        return buffer.toString().substring(0, buffer.toString().length() - 1);
    }

    public static byte[] intTo4byte(int id) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (id & 0xff);
        targets[2] = (byte) ((id >> 8) & 0xff);
        targets[1] = (byte) ((id >> 16) & 0xff);
        targets[0] = (byte) ((id >> 24) & 0xff);
        return targets;
    }

    public static byte[] intTo3byte(int id) {
        byte[] targets = new byte[3];
        targets[2] = (byte) (id & 0xff);
        targets[1] = (byte) ((id >> 8) & 0xff);
        targets[0] = (byte) ((id >> 16) & 0xff);
        return targets;
    }

    public static byte[] intTo2byte(int id) {
        byte[] targets = new byte[2];
        targets[1] = (byte) (id & 0xff);
        targets[0] = (byte) ((id >> 8) & 0xff);
        return targets;
    }

    public static byte intTo1byte(int id) {
        byte target = (byte) (id & 0xff);
        return target;
    }

    /**
     * long型转化为byte
     *
     * @param num
     * @return
     */
    public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    // 判断两个byte数组是否相等
    public static boolean isEqual(byte[] aa, byte[] bb) {
        if (aa == null || bb == null || aa.length != bb.length) {
            return false;
        }
        for (int i = 0; i < aa.length; i++) {
            if (aa[i] != bb[i])
                return false;
        }
        return true;
    }

    public static int byteToInt(byte[] buffer) {
        int intValue = 0;
        if (buffer != null) {
            for (int i = 0; i < buffer.length; i++) {
                intValue += (buffer[i] & 0xff) << (8 * (buffer.length - 1 - i));
            }
        }
        return intValue;
    }

    public static int byteToInt(byte[] buffer, int offSet, int lenth) {
        byte[] data = new byte[lenth];
        for (int i = 0; i < lenth; i++) {
            data[i] = buffer[offSet + i];
        }
        return byteToInt(data);
    }

    public static String byteToStringIp(byte[] buffer, int offSet, int lenth) {
        String reslut = "";
        byte[] data = new byte[1];
        for (int i = 0; i < lenth; i++) {
            data[0] = buffer[offSet + i];
            if (i == lenth - 1) {
                reslut += byteToInt(data);
            } else {
                reslut += byteToInt(data) + ".";
            }
        }
        return reslut;
    }

    public static double byteToDouble(byte[] buffer) {

        int intValue = 0;
        int len = buffer.length;
        for (int i = 0; i < len; i++) {
            intValue += (buffer[i] & 0xff) << (8 * (len - 1 - i));
        }
        return intValue / 3600.0000;
    }

    public static char byteToChar(byte[] b) {
        char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
        return c;
    }

    //浮点到字节转换
    public static byte[] doubleToBytes(double d) {
        byte writeBuffer[] = new byte[10];
        long v = Double.doubleToLongBits(d);
        writeBuffer[0] = (byte) (v >>> 56);
        writeBuffer[1] = (byte) (v >>> 48);
        writeBuffer[2] = (byte) (v >>> 40);
        writeBuffer[3] = (byte) (v >>> 32);
        writeBuffer[4] = (byte) (v >>> 24);
        writeBuffer[5] = (byte) (v >>> 16);
        writeBuffer[6] = (byte) (v >>> 8);
        writeBuffer[7] = (byte) (v >>> 0);
        writeBuffer[8] = (byte) (v >>> 0);
        writeBuffer[9] = (byte) (v >>> 0);
        return writeBuffer;
    }

    //字节到浮点转换
    public static double bytesToDouble(byte[] readBuffer) {
        return Double.longBitsToDouble((((long) readBuffer[0] << 56) +
                ((long) (readBuffer[1] & 255) << 48) +
                ((long) (readBuffer[2] & 255) << 40) +
                ((long) (readBuffer[3] & 255) << 32) +
                ((long) (readBuffer[4] & 255) << 24) +
                ((readBuffer[5] & 255) << 16) +
                ((readBuffer[6] & 255) << 8) +
                ((readBuffer[7] & 255) << 0))
        );
    }

    static double ArryToDouble(byte[] Array, int Pos) {
        long accum = 0;
        accum = Array[Pos + 0] & 0xFF;
        accum |= (long) (Array[Pos + 1] & 0xFF) << 8;
        accum |= (long) (Array[Pos + 2] & 0xFF) << 16;
        accum |= (long) (Array[Pos + 3] & 0xFF) << 24;
        accum |= (long) (Array[Pos + 4] & 0xFF) << 32;
        accum |= (long) (Array[Pos + 5] & 0xFF) << 40;
        accum |= (long) (Array[Pos + 6] & 0xFF) << 48;
        accum |= (long) (Array[Pos + 7] & 0xFF) << 56;
        return Double.longBitsToDouble(accum);
    }

}
