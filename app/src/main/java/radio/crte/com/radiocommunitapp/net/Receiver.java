package radio.crte.com.radiocommunitapp.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import radio.crte.com.radiocommunitapp.util.DataConvert;
import radio.crte.com.radiocommunitapp.util.TeaUtil;
import radio.crte.com.radiocommunitapp.util.XNLDataUtil;

public class Receiver extends Thread {
    private InputStream inputStream = null;
    public static Boolean isStartReceive = false;
    public byte[] data = new byte[64];
    private Handler handler = null;
    private BufferedReader bf;
    private DataInputStream dataInputStream;

    public Receiver(InputStream inputStream, Handler handler) {
        super();
        this.inputStream = inputStream;
        this.handler = handler;
        dataInputStream = new DataInputStream(inputStream);
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
            while (dataInputStream.read(data) != -1) {
                Log.e("test", "test");
                analyticalData(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "test");
        }
    }

    public void analyticalData(byte[] data) {
        Log.e("test", "receive1=" + DataConvert.byteToHexString(data));
        switch (data[3]) {
            case XNLDataUtil.XNL_MASTER_STATUS_BRDCST:
                XNLDataUtil.dAddress[0] = data[8];
                XNLDataUtil.dAddress[1] = data[9];
                handMessage(data[3]);
                break;
            case XNLDataUtil.XNL_DEVICE_AUTH_KEY_REPLY:
                XNLDataUtil.sAddress[0] = data[14];
                XNLDataUtil.sAddress[1] = data[15];
                byte[] rand = new byte[8];
                System.arraycopy(data, 18, rand, 0, 8);
                XNLDataUtil.randNum = TeaUtil.encrypt(rand, 0, TeaUtil.KEY, 32);
                handMessage(data[3]);
                break;
            case XNLDataUtil.XNL_DEVICE_CONN_REPLY:
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
