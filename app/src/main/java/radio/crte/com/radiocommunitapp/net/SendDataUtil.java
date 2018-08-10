package radio.crte.com.radiocommunitapp.net;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import radio.crte.com.radiocommunitapp.App;

public class SendDataUtil {
    private ExecutorService executor;
    private Socket socket = null;
    private OutputStream os = null;
    private InputStream inputStream = null;

    public SendDataUtil() {
        executor = Executors.newSingleThreadExecutor();
    }

    public void initConnnect(final Handler handler, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.2.174", 8002);
                    os = socket.getOutputStream();
                    inputStream = socket.getInputStream();
                    App.receiver = new Receiver(inputStream, handler, context);
                    App.receiver.start();
                } catch (IOException e) {
                    Log.e("test", "socket connect fail");
                }
            }
        });
    }

    public void sendData(final byte[] data) {
        if (socket == null) {
            Log.e("SendDataUtil", "socket is not init");
            return;
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    os.write(data);
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void close() {
        if (socket != null) {
            try {
                socket.shutdownOutput();
                inputStream.close();
                os.close();
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
