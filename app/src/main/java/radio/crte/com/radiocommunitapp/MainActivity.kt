package radio.crte.com.radiocommunitapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import radio.crte.com.radiocommunitapp.service.Radio1Service

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this,Radio1Service::class.java))
        tip.setOnClickListener {  }
    }
}
