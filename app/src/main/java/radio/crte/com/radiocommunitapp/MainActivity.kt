package radio.crte.com.radiocommunitapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import radio.crte.com.radiocommunitapp.activity.BaseActivity
import radio.crte.com.radiocommunitapp.service.RadioService
import radio.crte.com.radiocommunitapp.util.XNLDataUtil

class MainActivity : BaseActivity() {
    companion object {
        val MAIN_ACTION_CONNECT_SUCCESS = "com.crte.radio.CONNECT_SUCCESS"
        val MAIN_ACTION_CONNECT_FAIL = "com.crte.radio.CONNECT_FAIL"
        val ACTION_AUDIO_VOLUME_CHANGE = "android.media.VOLUME_CHANGED_ACTION"
    }

    var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val action = p1!!.action
            when (action) {
                MAIN_ACTION_CONNECT_SUCCESS -> {

                }
                MAIN_ACTION_CONNECT_FAIL -> {

                }
                ACTION_AUDIO_VOLUME_CHANGE -> {

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this,RadioService::class.java))
        initReceiver()
        about.setOnClickListener {
//            startActivity(Intent(this, AboutActivity::class.java))
            var sendUtil = App.sendDataUtil
            sendUtil.sendData(XNLDataUtil.sendGetBaseInfo(XNLDataUtil.BASE_INFO_RADIO_ID))
        }
        contact.setOnClickListener {
//            startActivity(Intent(this, ContactsActivity::class.java))
            var sendUtil = App.sendDataUtil
            sendUtil.sendData(XNLDataUtil.sendGetBaseInfo(XNLDataUtil.BASE_INFO_SERIAL_NUMBER))
        }
        work.setOnClickListener {
            var sendUtil = App.sendDataUtil
            sendUtil.sendData(XNLDataUtil.getChannelInfo())
        }
    }

    private fun initReceiver() {
        var intentFilter = IntentFilter()
        intentFilter.addAction(MAIN_ACTION_CONNECT_FAIL)
        intentFilter.addAction(MAIN_ACTION_CONNECT_SUCCESS)
        intentFilter.addAction(ACTION_AUDIO_VOLUME_CHANGE)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}
