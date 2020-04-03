package com.cnting.code1_velocitytracker_scroller

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import kotlin.math.abs
// TODO: 1.折线变成曲线  2.代码优化
class BarChartView : View {
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

    private val primaryColor = Color.BLUE
    private val gradientColor1 = Color.parseColor("#4D4DFF")
    private val gradientColor2 = Color.parseColor("#E0E0FF")
    private val barSpacing = 100f
    private val dotRadius = 10f
    private val pointerPaint: Paint
    private var gradientPaint: Paint? = null
    private val pointers = mutableMapOf<Int, Float>()
    private val scroller: Scroller
    private val velocityTracker: VelocityTracker
    private val minVelocity: Int
    private val maxVelocity: Int
    private val pointNum = 100
    private val canvasWidth: Float
    private var anim: ValueAnimator
    private var animRate: Float = 0f
    private var lineBottom: Float = 0f

    init {
        pointerPaint = Paint().also {
            it.color = primaryColor
            it.isAntiAlias = true
            it.strokeWidth = 2f
            it.textSize = 30f
            it.textAlign = Paint.Align.CENTER
        }
        scroller = Scroller(context)
        velocityTracker = VelocityTracker.obtain()
        minVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity   //最小速度
        maxVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
        canvasWidth = pointNum * barSpacing + paddingLeft + paddingRight

        anim = ValueAnimator.ofFloat(0f, 1f)
        anim.duration = 500
        anim.addUpdateListener {
            animRate = it.animatedValue as Float
            postInvalidate()
        }

        for (i in 0..pointNum) {
            pointers[i] = (Math.random() * 300).toFloat()
        }
    }

    fun startAnim() {
        animRate = 0f
        if (anim.isRunning) {
            anim.cancel()
        }
        anim.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        lineBottom = height.toFloat() - 35f
        gradientPaint = Paint().also {
            it.shader = LinearGradient(
                w.toFloat(),
                0f,
                w.toFloat(),
                h.toFloat(),
                gradientColor1,
                gradientColor2,
                Shader.TileMode.CLAMP
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        var startDrawIndex = (scrollX / barSpacing).toInt()
        while (shouldDraw(startDrawIndex * barSpacing)) {
            val startX = startDrawIndex * barSpacing + paddingLeft
            val lastStartX = startX - barSpacing
            val value = pointers[startDrawIndex] ?: 0f
            val lastValue = if (startDrawIndex == 0) 0f else pointers[startDrawIndex - 1] ?: 0f

            if (startDrawIndex >= 1 && (value != 0f || lastValue != 0f)) {
                val lastValueRate =  (lineBottom - animRate * lastValue) + dotRadius / 1.5f
                val valueRate = (lineBottom - animRate * value) + dotRadius / 1.5f
                val path = Path()
                path.moveTo(lastStartX, lineBottom)
//                path.quadTo(lastStartX, lastValueRate,startX, valueRate)
                path.lineTo(lastStartX, lastValueRate)
                path.lineTo(startX, valueRate)
                path.lineTo(startX, lineBottom)
                path.close()


                val pathMeasure = PathMeasure()
                pathMeasure.setPath(path,true)

                canvas.drawPath(path, gradientPaint!!)
            }

            canvas.drawLine(startX, 0f, startX, lineBottom, pointerPaint)
            canvas.drawCircle(startX, ((lineBottom - animRate * value)), dotRadius, pointerPaint)
            canvas.drawText("${startDrawIndex}日", startX, height.toFloat(), pointerPaint)

            startDrawIndex++
        }
    }

    private fun shouldDraw(curX: Float): Boolean {
        return (curX >= (scrollX - barSpacing) && curX <= (scrollX + width + barSpacing))
    }

    private var touchX = 0f
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        velocityTracker.addMovement(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
                scroller.forceFinished(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val newX = scrollX + (touchX - event.x)
                if (newX <= (canvasWidth - width) && newX > 0) {
                    scrollBy((touchX - event.x).toInt(), 0)
                }
                touchX = event.x
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker.computeCurrentVelocity(1000, maxVelocity.toFloat())
                val xVelocity = velocityTracker.xVelocity

                //小于最小速度，就不滑动。xVelocity的正负表示方向
                if (abs(xVelocity) >= minVelocity) {
                    scrollInitX = scrollX
                    scroller.fling(
                        scrollX,
                        scrollY,
                        xVelocity.toInt(),
                        0,
                        0,
                        (canvasWidth - width).toInt(),
                        0,
                        0
                    )
                    invalidate()
                }
            }
        }
        return true
    }

    private var scrollInitX: Int = 0
    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            val currX = scroller.currX
            var diffX = scrollInitX - currX

            var stop = false
            if (diffX != 0) {
                if (diffX + scrollX >= (canvasWidth - width)) {
                    diffX = (canvasWidth - width - scrollX).toInt()
                    stop = true
                } else if (scrollX <= 0) {
                    diffX = -scrollX
                    stop = true
                }
                if (!scroller.isFinished) {
                    scrollBy(diffX, 0)
                }
                scrollInitX = currX
            }
            if (!stop)
                postInvalidate()
        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        velocityTracker.recycle()
        anim.cancel()
    }

}
