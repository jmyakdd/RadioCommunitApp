package radio.crte.com.radiocommunitapp.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import radio.crte.com.radiocommunitapp.App;
import radio.crte.com.radiocommunitapp.MainActivity;
import radio.crte.com.radiocommunitapp.util.BroadCastSendUtil;
import radio.crte.com.radiocommunitapp.util.DataConvert;
import radio.crte.com.radiocommunitapp.util.TeaUtil;
import radio.crte.com.radiocommunitapp.util.XNLDataUtil;

public class Receiver extends Thread {
    private InputStream inputStream = null;
    public static Boolean isStartReceive = false;
    private Handler handler = null;
    private Context context;

    public Receiver(InputStream inputStream, Handler handler, Context context) {
        super();
        this.context = context;
        this.inputStream = inputStream;
        this.handler = handler;
    }

    @Override
    public synchronized void start() {
        isStartReceive = true;
        super.start();
    }

    @Override
    public void run() {
        super.run();
        try {
            while (true) {
                byte[] length = new byte[2];//获取数据长度
                inputStream.read(length);
                byte[] data = new byte[DataConvert.byteToInt(length)];//获取数据内容
                inputStream.read(data);
                analyticalData(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void analyticalData(byte[] data) {
        Log.e("test", "receive1=" + DataConvert.byteToHexString(data));
        switch (data[1]) {
            //请求连接返回值
            case XNLDataUtil.XNL_MASTER_STATUS_BRDCST:
                XNLDataUtil.dAddress[0] = data[6];
                XNLDataUtil.dAddress[1] = data[7];
                handMessage(data[1]);
                break;
            //请求身份验证码返回值
            case XNLDataUtil.XNL_DEVICE_AUTH_KEY_REPLY:
                XNLDataUtil.sAddress[0] = data[12];
                XNLDataUtil.sAddress[1] = data[13];
                byte[] rand = new byte[8];
                System.arraycopy(data, 14, rand, 0, 8);
                XNLDataUtil.authData = TeaUtil.encrypt(rand, 0, TeaUtil.KEY, 32);
                handMessage(data[1]);
                break;
            //请求建立连接返回值
            case XNLDataUtil.XNL_DEVICE_CONN_REPLY:
                if (data[12] == 0x01) {
                    System.arraycopy(data, 18, XNLDataUtil.authData, 0, 8);
                    XNLDataUtil.sAddress[0] = data[14];
                    XNLDataUtil.sAddress[1] = data[15];
                    Log.e("test", "连接成功");
                    BroadCastSendUtil.sendBroadcast(context, MainActivity.Companion.getMAIN_ACTION_CONNECT_SUCCESS());
                } else {
                    Log.e("test", "连接失败");
                    BroadCastSendUtil.sendBroadcast(context, MainActivity.Companion.getMAIN_ACTION_CONNECT_FAIL());
                }
                break;
            //xcmp请求数据返回值
            case XNLDataUtil.XNL_DATA_MSG_ACK:
                if (data[8] == XNLDataUtil.transaction[0] && data[9] == XNLDataUtil.transaction[1]
                        && XNLDataUtil.flag == data[3]) {
                    Log.e("receive", "电台接收数据成功");
                } else {
                    Log.e("receive", "电台接收数据失败");
                }
                break;
            //xcmp广播请求
            case XNLDataUtil.XNL_DATA_MSG:
                XNLDataUtil.confirmTransaction[0] = data[8];
                XNLDataUtil.confirmTransaction[1] = data[9];
                XNLDataUtil.confirmflag = data[3];
                handMessage(XNLDataUtil.XNL_DATA_MSG);
                analyticaXcmplData(data);
                break;
        }
    }

    private static final int REPLY_GET_RADIO_INFO = 0x800e;
    private static final int REPLY_TEXT = 0xb401;
    private static final int Broadcast_Physical_User_Input  = 0xb405;

    private void analyticaXcmplData(byte[] data) {
        /*byte[] length = new byte[]{data[10],data[11]};
        int l = DataConvert.byteToInt(length);*/
        int code = DataConvert.byteToInt(data, 12, 2);
        if(data[14]!=0x00){
            Log.e("error","xcmp request fali:"+data[14]);
            return;
        }
        switch (code) {
            case REPLY_GET_RADIO_INFO:
                Log.e("receive", "response");
                analyticalRadioStatusData(data);
                break;
            case REPLY_TEXT:
                break;
            case Broadcast_Physical_User_Input:
                break;
        }
    }

    private void analyticalRadioStatusData(byte[] data) {
        switch (data[15]){
            case 0x02://rssi
                break;
            case 0x07://product model number
                break;
            case 0x08://product serial number
                break;
            case 0x0d://Signaling
                break;
            case 0x0e://read radio id
                App.Companion.setRadioId(DataConvert.byteToInt(data,16,4));
                Log.e("test","radio id = "+App.Companion.getRadioId());
                break;
        }
    }

    public void handMessage(byte b) {
        Message msg = new Message();
        msg.what = 0;
        msg.obj = b;
        handler.sendMessage(msg);
    }
}
