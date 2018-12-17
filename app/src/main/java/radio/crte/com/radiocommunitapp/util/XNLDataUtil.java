package radio.crte.com.radiocommunitapp.util;

import android.util.Log;

/**
 * 协议封装类
 */
public class XNLDataUtil {
    //control
    public final static byte XNL_MASTER_STATUS_BRDCST = 0x02;
    public final static byte XNL_DEVICE_MASTER_QUERY = 0x03;
    public final static byte XNL_DEVICE_AUTH_KEY_REQUEST = 0x04;
    public final static byte XNL_DEVICE_AUTH_KEY_REPLY = 0x05;
    public final static byte XNL_DEVICE_CONN_REQUEST = 0x06;
    public final static byte XNL_DEVICE_CONN_REPLY = 0x07;
    public final static byte XNL_DEVICE_SYSMAP_REQUEST = 0x08;
    public final static byte XNL_DEVICE_SYSMAP_BRDCST = 0x09;
    //data
    public final static byte XNL_DATA_MSG = 0x0B;
    public final static byte XNL_DATA_MSG_ACK = 0x0C;

    //Protocol ID数据类型
    public static byte XNL_PROTO_XNL_CTRL = 0x00;//xnl
    public static byte XNL_PROTO_XCMP = 0x01;//xcmp

    public static byte flag = 0x00;
    public static byte confirmflag = 0x00;
    public static byte[] dAddress = new byte[2];
    public static byte[] sAddress = new byte[2];
    public static byte[] transaction = new byte[2];
    public static byte[] confirmTransaction = new byte[2];

    public static byte[] authData = new byte[8];

    /**
     * xnl命令封装
     *
     * @param xnlopcode
     * @param xnlData
     * @param dataLength
     * @return
     */
    private static byte[] packetXnlData(byte xnlopcode, byte[] xnlData, int dataLength) {
        byte[] data = new byte[dataLength];
        byte[] xnlDataLength = DataConvert.intTo2byte(dataLength - 2);
        data[0] = xnlDataLength[0];
        data[1] = xnlDataLength[1];
        // opcode
        data[2] = 0x00;
        data[3] = xnlopcode;
        // Proto id
        data[4] = XNL_PROTO_XNL_CTRL;
        // Flag
        if (xnlopcode == XNL_DATA_MSG_ACK) {
            data[5] = confirmflag;
        } else {
            data[5] = flag;
        }
        // Destination Address
        data[6] = dAddress[0];
        data[7] = dAddress[1];
        // Source Address
        data[8] = sAddress[0];
        data[9] = sAddress[1];
        // Transaction id
        if (xnlopcode == XNL_DATA_MSG_ACK) {
            data[10] = confirmTransaction[0];
            data[11] = confirmTransaction[1];
        } else {
            data[10] = transaction[0];
            data[11] = transaction[1];
        }
        if (xnlData == null) {
            data[12] = 0x00;
            data[13] = 0x00;
        } else {
            byte[] payloadLength = DataConvert.intTo2byte(xnlData.length);
            data[12] = payloadLength[0];
            data[13] = payloadLength[1];
            for (int i = 0; i < xnlData.length; i++) {
                data[14 + i] = xnlData[i];
            }
        }

        Log.e("test", "send xnl=" + DataConvert.byteToHexString(data));
        return data;
    }

    /**
     * xcmp命令封装
     *
     * @param xcmpData
     * @return
     */
    private static byte[] packetXcmpData(byte[] xcmpData) {
        byte[] data = new byte[14 + xcmpData.length];
        byte[] xnlDataLength = DataConvert.intTo2byte(12 + xcmpData.length);
        data[0] = xnlDataLength[0];
        data[1] = xnlDataLength[1];
        // opcode
        data[2] = 0x00;
        data[3] = 0x0B;
        // Proto id
        data[4] = XNL_PROTO_XCMP;
        // Flag
        flag++;
        if (flag >= 0x07) {
            flag = 0x00;
        }
        data[5] = flag;
        // Destination Address
        data[6] = dAddress[0];
        data[7] = dAddress[1];
        // Source Address
        data[8] = sAddress[0];
        data[9] = sAddress[1];
        // Transaction id
        initTransaction();
        data[10] = transaction[0];
        data[11] = transaction[1];
        if (xcmpData == null) {
            data[12] = 0x00;
            data[13] = 0x00;
        } else {
            byte[] payloadLength = DataConvert.intTo2byte(xcmpData.length);
            data[12] = payloadLength[0];
            data[13] = payloadLength[1];
            for (int i = 0; i < xcmpData.length; i++) {
                data[14 + i] = xcmpData[i];
            }
        }

        Log.e("test", "send xcmp=" + DataConvert.byteToHexString(data));
        return data;
    }

    private static void initTransaction() {
        transaction[1]++;
        if (transaction[1] == 0) {
            transaction[0]++;
        }
    }

    /**
     * 开始连接
     *
     * @return
     */
    public static byte[] sendQueryData() {
        transaction = new byte[2];
        flag = 0x00;
        return packetXnlData(XNL_DEVICE_MASTER_QUERY, null, 14);
    }

    /**
     * 请求身份密钥
     *
     * @return
     */
    public static byte[] sendAuthRequest() {
        return packetXnlData(XNL_DEVICE_AUTH_KEY_REQUEST, null, 14);
    }


    /**
     * 请求建立连接
     *
     * @return
     */
    public static byte[] sendConnRequest() {
        byte[] xnlData = new byte[12];
        xnlData[0] = 0x00;
        xnlData[1] = 0x00;
        xnlData[2] = 0x0A;
        xnlData[3] = 0x01;
        System.arraycopy(authData, 0, xnlData, 4, 8);
        return packetXnlData(XNL_DEVICE_CONN_REQUEST, xnlData, 26);
    }

    public static final byte TONE_FUN_STOP = 0x00;
    public static final byte TONE_FUN_START = 0x01;
    public static final byte TONE_FUN_DISABLE = 0x02;
    public static final byte TONE_FUN_ENABLE = 0x03;

    public static final int TONE_IDENTIFIER_NONE = 0x0000;
    public static final int TONE_IDENTIFIER_START_TALKING = 0x0003;
    public static final int TONE_IDENTIFIER_GOOD_KEY = 0x0005;
    public static final int TONE_IDENTIFIER_BAD_KEY = 0x0006;
    public static final int TONE_IDENTIFIER_PRIORITY_BEEP = 0x000C;
    public static final int TONE_IDENTIFIER_POWER_UP_BEEP = 0x000D;

    /**
     * _409
     *
     * @return
     */
    public static byte[] sendTipVoice() {
        byte[] data = new byte[10];
        data[0] = 0x04;
        data[1] = 0x09;
        return packetXcmpData(data);
    }

    public static final byte BASE_INFO_RSSI = 0x02;
    public static final byte BASE_INFO_MODEL_NUMBER = 0x07;
    public static final byte BASE_INFO_SERIAL_NUMBER = 0x08;
    public static final byte BASE_INFO_SIGNALING = 0x0d;
    public static final byte BASE_INFO_RADIO_ID = 0x0e;
    public static final byte BASE_INFO_BLUETOOTH = 0x24;

    /**
     * _00e
     */
    public static byte[] sendGetBaseInfo(byte condition) {
        byte[] data = new byte[3];
        data[0] = 0x00;
        data[1] = 0x0e;
        data[2] = condition;
        return packetXcmpData(data);
    }

    public static final byte VERSION_INFO_HOST_SOFTWARE = 0x00;
    public static final byte VERSION_INFO_DSP = 0x010;
    public static final byte VERSION_INFO_REGIONAL_INFORMATION = 0x47;
    public static final byte VERSION_INFO_RF_BAND = 0x63;
    public static final byte VERSION_INFO_POWER_LEVEL = 0x65;
    public static final byte VERSION_INFO_FLASH_SIZE = 0x6D;

    public static byte[] sendGetVersionInfo(byte type) {
        byte[] data = new byte[3];
        data[0] = 0x00;
        data[1] = 0x0f;
        data[2] = type;
        return packetXcmpData(data);
    }

    public static byte[] sendGPSStatus() {
        byte[] data = new byte[8];
        data[0] = 0x04;
        data[1] = 0x4b;
        data[2] = 0x00;
        data[3] = 0x00;
        data[4] = 0x04;
        data[5] = 0x00;
        data[6] = 0x01;
        data[7] = 0x00;
        return packetXcmpData(data);
    }

    public static byte[] getChannelInfo() {
        byte[] data = new byte[7];
        data[0] = 0x04;
        data[1] = 0x0d;
        data[2] = (byte) 0x81;
        data[3] = 0x00;
        data[4] = 0x00;
        data[5] = 0x00;
        data[6] = 0x00;
        return packetXcmpData(data);
    }

    /**
     * 发送确认信息
     *
     * @return
     */
    public static byte[] sendACKData() {
        return packetXnlData(XNL_DATA_MSG_ACK, null, 14);
    }
}