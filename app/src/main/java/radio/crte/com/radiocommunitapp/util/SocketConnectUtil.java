package radio.crte.com.radiocommunitapp.util;

public class SocketConnectUtil {
    static {
        System.loadLibrary("native_lib");
    }

    public static void receiveDataCallBack(byte[] data, int length) {
        //解析收到的xnl数据
        int code = DataConvert.byteToInt(data, 0, 2);
        switch (code) {
            case 0x000e:
                break;
            case 0x0001:
                break;
        }

    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String stringFromJNI();

    public native int initSocket(String ip, int port);

    public native int sendData(byte[] data);

    public native int receiveData();

    public native int close();
}
