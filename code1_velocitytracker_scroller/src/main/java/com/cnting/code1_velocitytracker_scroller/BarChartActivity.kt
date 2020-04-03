package com.cnting.code1_velocitytracker_scroller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_barchart.*

/**
 * Created by cnting on 2020-03-08
 *
 */
class BarChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barchart)
        startAnimBtn.setOnClickListener {
            barChart.startAnim()
        }
    }
}