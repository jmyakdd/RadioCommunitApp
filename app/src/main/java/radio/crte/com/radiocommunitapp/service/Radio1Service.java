package radio.crte.com.radiocommunitapp.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import radio.crte.com.radiocommunitapp.net.Receiver;
import radio.crte.com.radiocommunitapp.net.Sendr;
import radio.crte.com.radiocommunitapp.util.XNLDataUtil;

public class Radio1Service extends Service {
    private Socket socket = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private Receiver receiver = null;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startConnect();
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                byte code = (byte) msg.obj;
                switch (code) {
                    case XNLDataUtil.XNL_MASTER_STATUS_BRDCST: {
                        Sendr.sendData(XNLDataUtil.getAuthRequest());
                        break;
                    }
                    case XNLDataUtil.XNL_DEVICE_AUTH_KEY_REPLY: {
                        Sendr.sendData(XNLDataUtil.getConnRequest());
                        break;
                    }
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void startConnect() {
        Sendr.sendData(XNLDataUtil.getQueryData());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.crte.radio.RADIO_RE_CONNECT");
        registerReceiver(broadcastReceiver, filter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.2.173", 8002);
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                    Sendr.outputStream = outputStream;

                    receiver = new Receiver(inputStream, handler);
                    receiver.start();
                    startConnect();
                } catch (IOException e) {
                    Log.e("test", "socket connect fail");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
