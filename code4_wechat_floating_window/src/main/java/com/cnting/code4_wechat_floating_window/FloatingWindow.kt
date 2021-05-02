package com.cnting.code4_wechat_floating_window

import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.*
import android.widget.ImageView
import android.widget.Toast

/**
 * Created by cnting on 2020/4/7
 *
 */
data class Article(val imgId: Int, val title: String)

class FloatingWindow(val context: Context) {
    private val articles = mutableListOf<Article>()

    private val floatingView = FloatingView(context)
    private val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        0,
        0,
        PixelFormat.TRANSPARENT
    )
    private val windowManager: WindowManager

    init {
        floatingView.state = FloatingView.STATE.RIGHT
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        layoutParams.gravity = Gravity.START or Gravity.TOP
        layoutParams.x = UiUtil.getScreenWidth() - floatingView.floatingWidth
        layoutParams.y = 100
        layoutParams.packageName = context.packageName
        layoutParams.type =
            WindowManager.LayoutParams.TYPE_APPLICATION  //需要设置，不然报错：the specified window type 0 is not valid
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        addTouchListener()

        windowManager.addView(floatingView, layoutParams)
    }

    private fun addTouchListener() {
        floatingView.setOnTouchListener(object : View.OnTouchListener {
            var lastX: Float = 0f
            var lastY: Float = 0f
            val screenWidth = UiUtil.getScreenWidth()
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.rawX
                        lastY = event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        layoutParams.x += (event.rawX - lastX).toInt()
                        layoutParams.y += (event.rawY - lastY).toInt()
                        floatingView.state = FloatingView.STATE.FLOATING
                        windowManager.updateViewLayout(floatingView, layoutParams)
                        lastX = event.rawX
                        lastY = event.rawY
                    }
                    MotionEvent.ACTION_UP -> {
                        if (event.rawX < screenWidth / 2) {
                            layoutParams.x = 0
                            layoutParams.y += (event.rawY - lastY).toInt()
                            floatingView.state = FloatingView.STATE.LEFT
                            windowManager.updateViewLayout(floatingView, layoutParams)
                        } else {
                            layoutParams.x = screenWidth - floatingView.finalWidth
                            layoutParams.y += (event.rawY - lastY).toInt()
                            floatingView.state = FloatingView.STATE.RIGHT
                            windowManager.updateViewLayout(floatingView, layoutParams)
                        }
                        lastX = 0f
                        lastY = 0f
                    }
                }
                return false
            }
        })
    }

    fun addArticle(article: Article) {
        if (getArticleCount() >= 5) {
            Toast.makeText(context, "不能再加啦", Toast.LENGTH_SHORT).show()
            return
        }
        articles.add(article)
        val imageView = ImageView(context)
        imageView.setImageResource(article.imgId)
        floatingView.addView(imageView)
    }

    fun getArticleCount(): Int {
        return articles.size
    }

}

class FloatingView : ViewGroup {
    constructor(context: Context?) : super(context)
    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs)

    constructor (
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    val finalWidth = UiUtil.dpToPx(context, 100)
    val finalHeight = UiUtil.dpToPx(context, 50)
    val floatingWidth = UiUtil.dpToPx(context, 70)
    var state = STATE.RIGHT
        set(value) {
            field = value
            when (value) {
                STATE.LEFT -> setBackgroundResource(R.drawable.floating_window_bg_left)
                STATE.RIGHT -> setBackgroundResource(R.drawable.floating_window_bg_right)
                STATE.FLOATING -> setBackgroundResource(R.drawable.floating_window_bg_floating)
            }
        }

    enum class STATE {
        LEFT, RIGHT, FLOATING
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (state == STATE.FLOATING) {
            val spec = MeasureSpec.makeMeasureSpec(floatingWidth, MeasureSpec.EXACTLY)
            setMeasuredDimension(spec, spec)
        } else {
            val width =
                MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.getMode(widthMeasureSpec))
            val height =
                MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.getMode(heightMeasureSpec))
            setMeasuredDimension(width, height)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            getChildAt(i).layout(l + i * 10, t, r, b)
        }

    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

}