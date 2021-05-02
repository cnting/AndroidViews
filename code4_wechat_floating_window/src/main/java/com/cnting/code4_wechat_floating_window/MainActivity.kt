package com.cnting.code4_wechat_floating_window

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
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
            val dialog = Dialog(this.applicationContext)
            val textView = TextView(this)
            textView.setText("啊啊啊")
            dialog.setContentView(textView)
            dialog.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            dialog.show()
//            addArticle()
        }
    }

    val imgs = arrayOf(R.mipmap.app1, R.mipmap.app2, R.mipmap.app3, R.mipmap.app4, R.mipmap.app5)

    private fun addArticle() {
        val count = floatingWindow.getArticleCount()
        floatingWindow.addArticle(Article(imgs[count], "第${count + 1}篇文章"))
    }
}


