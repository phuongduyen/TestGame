package com.example.havefly

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.view.MotionEvent
import android.view.SurfaceView
import java.util.Random
import  com.example.havefly.MovingCharacter.Cloud
import  com.example.havefly.MovingCharacter.Child
import  com.example.havefly.MovingCharacter.Jumper

@SuppressLint("ViewConstructor")
class GameView(activity: GameActivity, screenX: Float, screenY: Float) : SurfaceView(activity),
    Runnable {
    private var thread: Thread? = null
    private var isPlaying = false
    private var isGameOver = false
    private val screenX: Float
    private val screenY: Float
    private var score = 0
    private val paint: Paint
    private val children: Array<Child?>
    private val prefs: SharedPreferences
    private val random: Random
    private var soundPool: SoundPool? = null
    private val clouds: MutableList<Cloud>
    private val sound: Int
    private val jumper: Jumper
    private val activity: GameActivity
    var speed = 20f

    //    private val background1: Background
//    private val background2: Background
    private val sizeChild = 3
//    private val screenY.toInt(): Int = screenY.toInt()// - 100

    private val childrenPool: Array<Child?>
    private val cloudPool: Array<Cloud?>
    val weightedList = listOf(2, 2, 2, 2, 0, 0, 1, 3, 3, 4, 5, 6)

    init {
        this.activity = activity
        isFocusable = true
        isClickable = true
        isFocusableInTouchMode = true
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE)
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()
            SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build()
        } else SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        sound = soundPool!!.load(activity, R.raw.shoot, 1)
        this.screenX = screenX
        this.screenY = screenY
//        background1 = Background(screenX.toInt(), screenY.toInt(), resources)
//        background2 = Background(screenX.toInt(), screenY.toInt(), resources)
        jumper = Jumper(screenY.toInt(), resources, screenRatio)
        clouds = ArrayList<Cloud>()
//        background2.x = screenX.toFloat()
        paint = Paint()
        paint.textSize = 128f
        paint.color = Color.RED
        children = arrayOfNulls<Child>(sizeChild)
        childrenPool = arrayOfNulls<Child>(7)
        cloudPool = arrayOfNulls<Cloud>(4)
        random = Random()

        for (i in 0..<4) {
            val cloud = Cloud(resources, screenRatio, i)
            cloud.x = (screenX / (2 * sizeChild) + screenX).toInt()
            cloud.y = random.nextInt((screenY / 3).toInt())
            cloudPool[i] = cloud
        }

        for (i in weightedList) {
            val child = Child(resources, screenRatio, i)
            childrenPool[i] = child
            childrenPool[i]?.x = screenX.toInt() + screenX.toInt() * i / sizeChild
            if (childrenPool[i]!!.childID == 2) {
                childrenPool[i]!!.y = random.nextInt(screenY.toInt() / 4)
            } else {
                childrenPool[i]!!.y = screenY.toInt() - childrenPool[i]!!.height
            }
        }

        for (i in 0..<sizeChild) {
//            val child = Child(resources, screenRatio)
            val availableChildren = childrenPool.filterNotNull().filter { !it.isActive }
            children[i] = availableChildren.randomOrNull()
            children[i]?.isActive = true
            children[i]?.x = screenX.toInt() + screenX.toInt() * (i + 1) / sizeChild
            if (children[i]!!.childID == 2) {
                children[i]!!.y = random.nextInt(screenY.toInt() / 4)
            }
//            else {
//                children[i]!!.y = screenY.toInt() - children[i]!!.height
//            }
        }
    }

    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread!!.start()
    }

    fun pause() {
        try {
            isPlaying = false
            thread!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun run() {
        while (isPlaying) {
            if (!holder.surface.isValid) continue  // Chờ Surface sẵn sàng
            update()
            draw()
            sleep()
        }
    }

    private fun update() {
//        background1.x -= 10 * screenRatio
//        background2.x -= 10 * screenRatio
//        if (background1.x + background1.background.getWidth() < 0) {
//            background1.x = screenX.toFloat()
//        }
//        if (background2.x + background2.background.getWidth() < 0) {
//            background2.x = screenX.toFloat()
//        }
        if (jumper.isGoingUp) jumper.y -= (100 * screenRatio).toInt() else jumper.y += (30 * screenRatio).toInt()
        if (jumper.y < 0) jumper.y = 0
        if (jumper.y >= screenY - jumper.height) jumper.y = screenY.toInt() - jumper.height

        var i = 0
        speed += 0.005f
        for (child in children) {
            if (child != null) {
                child.x -= speed.toInt()
//                if (Rect.intersects(child.collisionShape, jumper.collisionShape)) {
//                    isGameOver = true
//                    return
//                }
                if (child.x + child.width < 0) {
//                    if (!child.wasShot) {
//                        isGameOver = true
//                        return
//                    }
//                    children[i] = Child(resources, screenRatio)
                    child.isActive = false
                    val availableChildren = childrenPool.filterNotNull().filter { !it.isActive }
                    children[i] = availableChildren.randomOrNull()
                    children[i]?.isActive = true

//                    val bound = (30 * screenRatio).toInt()
//                    children[i]!!.speed = random.nextInt(bound)
//                    if (children[i]!!.speed < 10 * screenRatio) children[i]!!.speed =
//                        (10 * screenRatio).toInt()
                    children[i]!!.x = screenX.toInt() + screenX.toInt() / sizeChild
//                    child.y = random.nextInt(screenY.toInt() - child.height)
                    if (children[i]!!.childID == 2) {
                        children[i]!!.y = random.nextInt(screenY.toInt() / 4)
                    }
//                    else {
//                        children[i]!!.y = screenY.toInt() - children[i]!!.height
//                    }
                    children[i]!!.wasShot = false
                    score++
                    if (score % 3 == 0) {
                        newcloud()
                    }
//                    children[i] = Child(resources)
                }
            }
            i++
        }

        val trash: MutableList<Cloud> = ArrayList<Cloud>()
        for (cloud in clouds) {
            if (cloud.x + cloud.width < 0) trash.add(cloud)
            cloud.x -= speed.toInt()

//            cloud.x -= 300 * screenRatio.toInt()
//            for (child in children) {
//                if (child != null && Rect.intersects(
//                        child.collisionShape,
//                        cloud.collisionShape
//                    )
//                ) {
//                    child.x = -500
//                    cloud.x = screenX + 500
//                    child.wasShot = true
//                }
//            }
        }
        for (cloud in trash) {
            cloud.isActive = false
            clouds.remove(cloud)
        }
    }

    private fun draw() {
//        if (holder.surface.isValid) {
        val canvas = holder.lockCanvas()
//            canvas.drawBitmap(background1.background, background1.x, background1.y, paint)
//            canvas.drawBitmap(background2.background, background2.x, background2.y, paint)

        canvas.drawColor(Color.BLACK)
        canvas.drawText(score.toString() + "", screenX / 2f, 164f, paint)
        if (isGameOver) {
            isPlaying = false
            canvas.drawBitmap(jumper.dead, jumper.x.toFloat(), jumper.y.toFloat(), paint)
            holder.unlockCanvasAndPost(canvas)
            saveIfHighScore()
            waitBeforeExiting()
            return
        }
        canvas.drawBitmap(jumper.jumper, jumper.x.toFloat(), jumper.y.toFloat(), paint)
        for (cloud in clouds) canvas.drawBitmap(
            cloud.cloud,
            cloud.x.toFloat(),
            cloud.y.toFloat(),
            paint
        )

        for (child in children) {
            if (child != null)
                canvas.drawBitmap(child.child, child.x.toFloat(), child.y.toFloat(), paint)
        }
        holder.unlockCanvasAndPost(canvas)
//        }
    }

    private fun waitBeforeExiting() {
        try {
            Thread.sleep(300)
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun saveIfHighScore() {
        if (prefs.getInt("highscore", 0) < score) {
            val editor = prefs.edit()
            editor.putInt("highscore", score)
            editor.apply()
        }
    }

    private fun sleep() {
        try {
            Thread.sleep(17)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (event.x < screenX / 2) {
                jumper.isGoingUp = true
            }

            MotionEvent.ACTION_UP -> {
                jumper.isGoingUp = false
                if (event.x > screenX / 2) jumper.toShoot++
            }
        }
        return true
    }

    fun newcloud() {
//        if (!prefs.getBoolean("isMute", false)) soundPool!!.play(sound, 1f, 1f, 0, 0, 1f)
        val availableCloud = cloudPool.filterNotNull().filter { !it.isActive }
        availableCloud.randomOrNull()?.let { cloud ->
            cloud.isActive = true
            cloud.x = (screenX / (2 * sizeChild) + screenX).toInt()
            cloud.y = random.nextInt((screenY / 3).toInt())
            clouds.add(cloud)
        }
    }

    companion object {
        var screenRatio: Float = 0.0f
    }
}
