package radio.crte.com.radiocommunitapp.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import radio.crte.com.radiocommunitapp.App;
import radio.crte.com.radiocommunitapp.net.SendDataUtil;
import radio.crte.com.radiocommunitapp.util.XNLDataUtil;

public class RadioService extends Service {
    SendDataUtil sendData = null;

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
                        sendData.sendData(XNLDataUtil.sendAuthRequest());
                        break;
                    }
                    case XNLDataUtil.XNL_DEVICE_AUTH_KEY_REPLY: {
                        sendData.sendData(XNLDataUtil.sendConnRequest());
                        break;
                    }
                    case XNLDataUtil.XNL_DATA_MSG: {
                        sendData.sendData(XNLDataUtil.sendACKData());
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

    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.crte.radio.RADIO_RE_CONNECT");
        registerReceiver(broadcastReceiver, filter);

        App.sendDataUtil = new SendDataUtil();
        sendData = App.sendDataUtil;
        sendData.initConnnect(handler, this);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        sendData.close();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
