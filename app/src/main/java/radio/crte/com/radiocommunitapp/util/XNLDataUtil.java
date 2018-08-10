package radio.crte.com.radiocommunitapp.util;

import android.util.Log;

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

    //开始连接
    public static byte[] sendQueryData() {
        transaction = new byte[2];
        flag = 0x00;
        return packetXnlData(XNL_DEVICE_MASTER_QUERY, null, 14);
    }

    //请求身份密钥
    public static byte[] sendAuthRequest() {
        return packetXnlData(XNL_DEVICE_AUTH_KEY_REQUEST, null, 14);
    }

    //请求建立连接
    public static byte[] sendConnRequest() {
        byte[] xnlData = new byte[12];
        xnlData[0] = 0x00;
        xnlData[1] = 0x00;
        xnlData[2] = 0x0A;
        xnlData[3] = 0x01;
        System.arraycopy(authData, 0, xnlData, 4, 8);
        return packetXnlData(XNL_DEVICE_CONN_REQUEST, xnlData, 26);
    }

    public static byte[] sendTipVoice() {
        byte[] data = new byte[10];
        data[0] = 0x04;
        data[1] = 0x09;
        return packetXcmpData(data);
    }

    public static byte[] sendGetBaseInfo() {
        byte[] data = new byte[3];
        data[0] = 0x00;
        data[1] = 0x0e;
        data[2] = 0x02;
        return packetXcmpData(data);
    }

    public static byte[] sentGetSignaling(){
        byte[] data = new byte[3];
        data[0] = 0x00;
        data[1] = 0x0e;
        data[2] = 0x0d;
        return packetXcmpData(data);
    }

    public static byte[] sendACKData() {
        return packetXnlData(XNL_DATA_MSG_ACK, null, 14);
    }
}