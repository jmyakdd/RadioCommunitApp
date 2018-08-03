package radio.crte.com.radiocommunitapp.util;

import android.util.Log;

public class XNLDataUtil {
	//control
	public final static byte XNL_MASTER_STATUS_BRDCST = 0x02;
	public final static byte XNL_DEVICE_MASTER_QUERY = 0x03;
	public final static byte XNL_DEVICE_AUTH_KEY_REQUEST  = 0x04;
	public final static byte XNL_DEVICE_AUTH_KEY_REPLY  = 0x05;
	public final static byte XNL_DEVICE_CONN_REQUEST = 0x06;
	public final static byte XNL_DEVICE_CONN_REPLY  = 0x07;
	public final static byte XNL_DEVICE_SYSMAP_REQUEST = 0x08;
	public final static byte XNL_DEVICE_SYSMAP_BRDCST  = 0x09;
	//data
	public final static byte XNL_DATA_MSG   = 0x000B;
	public final static byte XNL_DATA_MSG_ACK    = 0x000C;

	//Protocol ID数据类型
	public static byte XNL_PROTO_XNL_CTRL = 0x00;//xnl
	public static byte XNL_PROTO_XCMP  = 0x01;//xcmp

	public static byte flag = 0x00;
	public static byte[] dAddress = new byte[2];
	public static byte[] sAddress = new byte[2];
	public static byte[] transaction = new byte[2];

	public static byte[] randNum = new byte[8];

	private static byte[] packetXnlData(byte xnlopcode, byte[] xnlData) {
		byte[] data = new byte[32];
		// opcode
		data[0] = 0x00;
		data[1] = xnlopcode;
		// Proto id
		data[2] = XNL_PROTO_XNL_CTRL;
		// Flag
		data[3] = flag;
		// Destination Address
		data[4] = dAddress[0];
		data[5] = dAddress[1];
		// Source Address
		data[6] = sAddress[0];
		data[7] = sAddress[1];
		// Transaction id
		data[8] = transaction[0];
		data[9] = transaction[1];
		if (xnlData == null) {
			data[10] = 0x00;
			data[11] = 0x00;
		} else {
			byte[] payloadLength = DataConvert.intTo2byte(xnlData.length);
			data[10]=payloadLength[0];
			data[11]=payloadLength[1];
			for (int i = 0; i < xnlData.length; i++) {
				data[12 + i] = xnlData[i];
			}
		}

		Log.e("test", "send="+DataConvert.byteToHexString(data));
		return data;
	}

	public static byte[] getQueryData(){
		return packetXnlData(XNL_DEVICE_MASTER_QUERY,null);
	}

	public static byte[] getAuthRequest(){
		return packetXnlData(XNL_DEVICE_AUTH_KEY_REQUEST,null);
	}

	public static byte[] getConnRequest(){
		byte[] xnlData = new byte[12];
		xnlData[0] = 0x00;
		xnlData[1] = 0x00;
		xnlData[2] = 0x0A;
		xnlData[3] = 0x01;
		System.arraycopy(randNum,0,xnlData,4,8);
		return packetXnlData(XNL_DEVICE_CONN_REQUEST,xnlData);
	}
}
