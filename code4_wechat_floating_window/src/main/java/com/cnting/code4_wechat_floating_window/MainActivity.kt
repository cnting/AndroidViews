package com.cnting.code4_wechat_floating_window

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 仿微信悬浮窗
 */
class MainActivity : AppCompatActivity() {

    lateinit var floatingWindow: FloatingWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        floatingWindow = FloatingWindow(this)
        addBtn.setOnClickListener {
            addArticle()
        }
    }


    private fun addArticle() {

    }
}


