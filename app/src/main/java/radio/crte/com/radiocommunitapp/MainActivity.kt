package radio.crte.com.radiocommunitapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import radio.crte.com.radiocommunitapp.util.XNLDataUtil

class MainActivity : AppCompatActivity() {
    companion object {
        val MAIN_ACTION_CONNECT_SUCCESS = "com.crte.radio.CONNECT_SUCCESS"
        val MAIN_ACTION_CONNECT_FAIL = "com.crte.radio.CONNECT_FAIL"
    }

    var broadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            val action = p1!!.action
            when(action){
                MAIN_ACTION_CONNECT_SUCCESS->{
                    tip.text = "连接成功"
                }
                MAIN_ACTION_CONNECT_FAIL->{
                    tip.text = "连接失败，点击重试"
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        startService(Intent(this, RadioService::class.java))
        tip.text = "正在连接电台..."
        playTipVoice.setOnClickListener {
            App.sendDataUtil.sendData(XNLDataUtil.sendTipVoice())
        }
        radioBaseInfo.setOnClickListener {
            App.sendDataUtil.sendData(XNLDataUtil.sendGetBaseInfo())
        }
        getSignalingInfo.setOnClickListener {
            App.sendDataUtil.sendData(XNLDataUtil.sentGetSignaling())
        }
        initReceiver()
    }

    private fun initReceiver() {
        var intentFilter = IntentFilter()
        intentFilter.addAction(MAIN_ACTION_CONNECT_FAIL)
        intentFilter.addAction(MAIN_ACTION_CONNECT_SUCCESS)
        registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
//        stopService(Intent(this,RadioService::class.java))
    }
}
