package radio.crte.com.radiocommunitapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import radio.crte.com.radiocommunitapp.util.XNLDataUtil

class MainActivity : AppCompatActivity() {
    companion object {
        val MAIN_ACTION_CONNECT_SUCCESS = "com.crte.radio.CONNECT_SUCCESS"
        val MAIN_ACTION_CONNECT_FAIL = "com.crte.radio.CONNECT_FAIL"
        val ACTION_AUDIO_VOLUME_CHANGE = "android.media.VOLUME_CHANGED_ACTION"
    }
    lateinit var audioManager:AudioManager
    val STREAM_TYPE = AudioManager.STREAM_NOTIFICATION

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
                ACTION_AUDIO_VOLUME_CHANGE->{
                    setCurrentVolume()
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
//            App.sendDataUtil.sendData(XNLDataUtil.sendTipVoice())
        }
        radioBaseInfo.setOnClickListener {
            App.sendDataUtil.sendData(XNLDataUtil.sendGetBaseInfo(XNLDataUtil.BASE_INFO_RSSI))
        }
        getSignalingInfo.setOnClickListener {
            App.sendDataUtil.sendData(XNLDataUtil.sendGetBaseInfo(XNLDataUtil.BASE_INFO_SIGNALING))
        }
        initReceiver()
        initVolume()
    }

    private fun initVolume() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        var maxVolume = audioManager.getStreamMaxVolume(STREAM_TYPE)
        volume.max = maxVolume
        setCurrentVolume()
        volume.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    private fun setCurrentVolume(){
        var currentVolume = audioManager.getStreamVolume(STREAM_TYPE)
        volume.progress = currentVolume
    }

    private fun initReceiver() {
        var intentFilter = IntentFilter()
        intentFilter.addAction(MAIN_ACTION_CONNECT_FAIL)
        intentFilter.addAction(MAIN_ACTION_CONNECT_SUCCESS)
        intentFilter.addAction(ACTION_AUDIO_VOLUME_CHANGE)
        registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
//        stopService(Intent(this,RadioService::class.java))
    }
}
