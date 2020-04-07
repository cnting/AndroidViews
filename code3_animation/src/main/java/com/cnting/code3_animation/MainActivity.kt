package com.cnting.code3_animation

import android.animation.*
import android.graphics.Path
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.PathInterpolator
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initLayoutTransition()

        addBtn.setOnClickListener {
            val img = ImageView(this@MainActivity)
            img.setImageResource(R.mipmap.ic_launcher)
            img.layoutParams = ViewGroup.LayoutParams(60, 60)
            container1.addView(img)
        }
        removeBtn.setOnClickListener {
            val count = container1.childCount
            if (count > 0) {
                container1.removeViewAt(count - 1)
            }
        }
        addBtn1.setOnClickListener {
            enterAnim()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun enterAnim() {
        val path = Path().apply {
            arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true)

        }
        val pathInterpolator = PathInterpolator(path)
        val animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path).apply {
            duration = 2000

            start()
        }
    }

    private fun initLayoutTransition() {
        val layoutTransition = LayoutTransition()

        val properAnimator1 = PropertyValuesHolder.ofFloat("translationX", 100f, 0f)
        val properAnimator2 = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
        val animIn =
            ObjectAnimator.ofPropertyValuesHolder(null as View?, properAnimator1, properAnimator2)
        layoutTransition.setAnimator(LayoutTransition.APPEARING, animIn)
        layoutTransition.setDuration(LayoutTransition.APPEARING, 200)

        val properAnimator3 = PropertyValuesHolder.ofFloat("translationY", 0f, -100f)
        val properAnimator4 = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
        val animOut =
            ObjectAnimator.ofPropertyValuesHolder(null as View?, properAnimator3, properAnimator4)
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, animOut)
        layoutTransition.setDuration(LayoutTransition.DISAPPEARING, 200)

        container1.layoutTransition = layoutTransition
    }
}
