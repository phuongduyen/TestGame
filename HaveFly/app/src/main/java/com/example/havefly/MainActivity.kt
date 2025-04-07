package com.example.havefly

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var isMute = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.play).setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    GameActivity::class.java
                )
            )
        }
        val highScoreTxt = findViewById<TextView>(R.id.highScoreTxt)
        val prefs = getSharedPreferences("game", MODE_PRIVATE)
        highScoreTxt.text = "HighScore: " + prefs.getInt("highscore", 0)
        isMute = prefs.getBoolean("isMute", false)
        val volumeCtrl = findViewById<ImageView>(R.id.volumeCtrl)
        if (isMute) volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp) else volumeCtrl.setImageResource(
            R.drawable.ic_volume_up_black_24dp
        )
        volumeCtrl.setOnClickListener {
            isMute = !isMute
            if (isMute) volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp) else volumeCtrl.setImageResource(
                R.drawable.ic_volume_up_black_24dp
            )
            val editor = prefs.edit()
            editor.putBoolean("isMute", isMute)
            editor.apply()
        }
    }
}

