package com.example.havefly

import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft


class GameActivity : AppCompatActivity() {
    private var gameView: GameView? = null
    private val DESIGN_WIDTH = 1920f // x
    private val DESIGN_HEIGHT = 1080f // y

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        GameView.screenRatio = ( point.x) / DESIGN_WIDTH  // Fit theo chiều ngang
        val scaledHeight = DESIGN_HEIGHT * GameView.screenRatio

        if(scaledHeight <= point.y.toFloat()){
            gameView = GameView(this, (point.x.toFloat()) , scaledHeight)
        } else {
            GameView.screenRatio = ( point.y) / DESIGN_HEIGHT  // Fit theo chiều ngang
            val scaledWith = DESIGN_WIDTH * GameView.screenRatio
            gameView = GameView(this, scaledWith , (point.y.toFloat()))
        }

//        val frameLayout = FrameLayout(this)
//        val layoutParams = FrameLayout.LayoutParams(
//            FrameLayout.LayoutParams.WRAP_CONTENT,
//            FrameLayout.LayoutParams.WRAP_CONTENT
//        )
//        layoutParams.gravity = Gravity.CENTER
//        layoutParams.setMargins(50, 50, 50, 50)  // trái, trên, phải, dưới (pixels)
//        gameView!!.layoutParams = layoutParams
//        frameLayout.addView(gameView)
//        setContentView(frameLayout)
        setContentView(gameView)
    }

    override fun onPause() {
        super.onPause()
        gameView!!.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView!!.resume()
    }
}
