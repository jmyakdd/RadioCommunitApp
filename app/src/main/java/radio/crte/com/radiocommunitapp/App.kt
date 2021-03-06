package radio.crte.com.radiocommunitapp

import android.app.Application
import radio.crte.com.radiocommunitapp.net.Receiver
import radio.crte.com.radiocommunitapp.net.SendDataUtil

class App : Application() {
    companion object {
        lateinit var sendDataUtil:SendDataUtil
        lateinit var receiver:Receiver
        var radioId:Int = 0
    }
    override fun onCreate() {
        super.onCreate()
    }
}