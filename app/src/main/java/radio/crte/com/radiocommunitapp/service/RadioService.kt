package radio.crte.com.radiocommunitapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class RadioService : Service() {
    lateinit var socket: Socket
    lateinit var inputStream: InputStream
    lateinit var outputStream: OutputStream
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        socket = Socket("192.168.2.133",50050)
        inputStream = socket.getInputStream()
        outputStream = socket.getOutputStream()
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}