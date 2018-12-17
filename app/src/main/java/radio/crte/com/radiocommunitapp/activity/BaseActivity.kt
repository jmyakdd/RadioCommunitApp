package radio.crte.com.radiocommunitapp.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.status_bar.*

abstract class BaseActivity : AppCompatActivity() {
    var systemReceiver = object :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            val action = p1!!.action
            when (action) {
                Intent.ACTION_BATTERY_CHANGED -> {
                    var current = p1.extras.getInt("level")
                    var total = p1.extras.getInt("scale")
                    var percent: Int = current * 100 / total
                    setBatteryLevel(percent)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        setRssiLevel(3)
        initReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(systemReceiver)
    }

    private fun initReceiver() {
        var intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(systemReceiver, intentFilter)
    }

    fun setRssiLevel(level: Int) {
        iv_rssi.drawable.setLevel(10000 * level / 4)
    }

    fun setBatteryLevel(level: Int) {
        iv_battery.drawable.setLevel(10000 * level / 100)
    }
}