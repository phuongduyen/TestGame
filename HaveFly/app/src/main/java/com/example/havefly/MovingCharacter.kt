package com.example.havefly

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

open class MovingCharacter internal constructor(val res: Resources?, screenRatio: Float) {
    var x = 0
    var y: Int = 0
    var width: Int = 0
    var height: Int = 0
    var widthD: Float = 10f
    var isActive = false

    val collisionShape: Rect
        get() = Rect(x, y, x + width, y + height)

    class Cloud internal constructor(res: Resources?, screenRatio: Float, id: Int) :
        MovingCharacter(res, screenRatio) {
        var cloud: Bitmap

        //        val weightedList = listOf(0, 1)
        val bitmapArray = arrayOf(
            R.drawable.cloud,
            R.drawable.clouds,
            R.drawable.cloudy,
            R.drawable.cloudy2
        )

        init {
            val cloudID = id
            cloud = BitmapFactory.decodeResource(res, bitmapArray[cloudID])
            width = (cloud.width * GameView.screenRatio / (widthD - 5)).toInt()
            height = (cloud.height * GameView.screenRatio / (widthD - 5)).toInt()
            cloud = Bitmap.createScaledBitmap(cloud, width, height, false)
        }
    }

    class Jumper internal constructor(
        screenY: Int,
        res: Resources?, screenRatio: Float
    ) : MovingCharacter(res, screenRatio) {
        var toShoot = 0
        var isGoingUp = false
        var friend: Bitmap
        var dead: Bitmap

        init {
            friend = BitmapFactory.decodeResource(res, R.drawable.finn)

            widthD = screenRatio * 100
            height = (widthD * friend.height / friend.width).toInt()
            width = widthD.toInt()

//            width = (friend.width * GameView.screenRatio / (widthD - 5)).toInt()
//            height = (friend.height * GameView.screenRatio / (widthD - 5)).toInt()
            friend = Bitmap.createScaledBitmap(friend, width, height, false)
            dead = BitmapFactory.decodeResource(res, R.drawable.finn)
            dead = Bitmap.createScaledBitmap(dead, width, height, false)
            y = screenY / 2
            x = (64 * GameView.screenRatio).toInt()
        }

        val jumper: Bitmap
            get() {
                return friend
            }
    }

    class Child internal constructor(res: Resources?, screenRatio: Float, id: Int) :
        MovingCharacter(res, screenRatio) {
        var wasShot = true
        var childID = 0
        var mChild: Bitmap
        val bitmapArray = arrayOf(
            R.drawable.leo,
            R.drawable.cooki_g,
            R.drawable.lisa_g,
            R.drawable.two_g,
            R.drawable.three_g,
            R.drawable.friends_all_g,
            R.drawable.friends_all_g
        )
//        val weightedList = listOf(0, 1, 2, 2, 2, 3, 4, 5, 6)

        init {
            childID = id //weightedList.random()
            mChild = BitmapFactory.decodeResource(res, bitmapArray[childID])
            widthD = screenRatio * 100
            height = (widthD * mChild.height / mChild.width).toInt()
            width = widthD.toInt()
//            width = (mChild.width * GameView.screenRatio / (widthD - 5)).toInt()
//            height = (mChild.height * GameView.screenRatio / (widthD - 5)).toInt()
            mChild = Bitmap.createScaledBitmap(child, width, height, false)
        }

        val child: Bitmap
            get() {
                return mChild
            }
    }
}