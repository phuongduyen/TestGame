package com.example.havefly

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory


class Background internal constructor(screenX: Int, screenY: Int, res: Resources?) {
    var x = 0f
    var y = 0f
    var background: Bitmap

    init {
        background = BitmapFactory.decodeResource(res, R.drawable.background)
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false)
    }
}
