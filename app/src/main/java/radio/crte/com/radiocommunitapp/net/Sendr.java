package radio.crte.com.radiocommunitapp.net;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Sendr {
    public static OutputStream outputStream = null;
    private static Executor executor;

    static {
        executor = Executors.newSingleThreadExecutor();
    }

    public static void sendData(final byte[] data) {
        if (outputStream == null) {
            return;
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream.write(data);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
